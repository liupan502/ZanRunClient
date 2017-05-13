package com.example.liupan.zanrunworkclient;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;
import com.example.liupan.zanrunworkclient.*;

import com.example.liupan.zanrunworkclient.ConfirmDialog.*;

import Dialog.*;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.example.liupan.zanrunworkclient.*;

import com.example.liupan.zanrunworkclient.EmployeeSimpleAdapter;

import com.example.liupan.zanrunworkclient.entity.*;

import Dialog.QCConfirmDialog;
import Test.DBTest;
import Test.SettingTest;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private PortListenHandler portListenHandler = null;

    private ArrayList<HashMap<String,Object>> employeeList = new ArrayList<HashMap<String, Object>>();

    private ArrayList<EmployeeTask> employeeTasks = new ArrayList<EmployeeTask>();

    private boolean isUpdating =false;

    private enum Mode{
        MODE_DEFAULT,
        MODE_QC_CONFIRM,
        MODE_MANAGER_CONFIRM
    };

    private  Mode currentMode = Mode.MODE_DEFAULT;

    //private Task task = null;

    private FlowCard flowCard = null;

    private Procedure procedure = null;

    private ManagerConfirmDialog mcDialog = null;

    private QCConfirmDialog qcDialog = null;

    private SettingConfirmDialog scDialog = null;

    private ZanRunDBHelper dbHelper = null;

    private Button settingButton = null;

    private Button refreshButton = null;

    private boolean is_refreshing = false;

    EmployeeSimpleAdapter simpleAdapter = null;

    private void processGetNewHuman(Employee employee){
        if(employee == null)
            return;
        int employeeLevel = employee.getEmployeeLevel();
        switch (employeeLevel){
            case Employee.GENERAL_EMPLOYEE:
                processGetNewGeneralEmployee(employee);
                break;
            case Employee.QC_EMPLOYEE:
                processGetNewQC(employee);
                break;
            case Employee.MANAGER_EMPLOYEE:
                processGetNewManager(employee);
                break;
            default:
                break;
        }
    }

    private void processGetNewFlowCard(FlowCard newFlowCard){
        if(newFlowCard == null)
            return;
        flowCard = newFlowCard;
        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        if(!sqlLiteProxy.isAvailable())
            return;
        ArrayList<EmployeeTask> tmpEmployeeTasks =  sqlLiteProxy.employeeTasks(flowCard.getId());
        employeeTasks = tmpEmployeeTasks == null?new ArrayList<EmployeeTask>():tmpEmployeeTasks;
        updateEmployeeList();

        EditText orderNoEditText = (EditText) findViewById(R.id.order_no_Text);
        if(orderNoEditText != null){
            orderNoEditText.setText(String.valueOf(flowCard.getOrderNum()));
        }

        EditText productionNoEditText = (EditText) findViewById(R.id.production_no_text);
        if(productionNoEditText != null){
            productionNoEditText.setText(flowCard.getProductionNo());
        }

        EditText dateEditText = (EditText) findViewById(R.id.date_text);
        if(dateEditText != null){
            dateEditText.setText(flowCard.getTerminalDate());
        }

        EditText numEditText = (EditText) findViewById(R.id.num_text);
        if(numEditText != null){
            int orderNum = flowCard.getOrderNum();
            int mantiNum = flowCard.getMantiNum();
            String numStr = mantiNum+"/"+orderNum;
            numEditText.setText(numStr);
        }

        EditText requestEditText = (EditText) findViewById(R.id.request_text);
        if(requestEditText != null && procedure != null){
            requestEditText.setText(flowCard.getRequest(procedure.getId()));
        }
    }

    private void updateEmployeeList(){
        employeeList.clear();
        //employeeList.add(new HashMap<String,Object>());
        for(int i=0;i<employeeTasks.size();i++){
            EmployeeTask task = employeeTasks.get(i);
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put(EmployeeSimpleAdapter.EMPLOYEE_NAME,task.getEmployeeName());
            map.put(EmployeeSimpleAdapter.PRODUCTION_NUM,task.getProductionNum());
            map.put(EmployeeSimpleAdapter.BAD_NUM,task.getBadProductionNum());
            map.put(EmployeeSimpleAdapter.EMPLOYEE_ID,task.getId());
            map.put(EmployeeSimpleAdapter.EMPLOYEE_STATUS,task.getStatus());
            employeeList.add(0,map);
        }
        refreshEmployeeTaskList();
    }

    private void processGetNewGeneralEmployee(Employee employee){
        if(currentMode != Mode.MODE_DEFAULT)
            return;
        if(procedure == null)
            return;
        EmployeeTask employeeTask = new EmployeeTask(employee,flowCard, procedure);
        SqlLiteProxy sqlLiteProxy =  SqlLiteProxy.getInstance();
        if(!sqlLiteProxy.isAvailable())
            sqlLiteProxy.start(dbHelper);
        try{
            sqlLiteProxy.insertEmployeeTask(employeeTask);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        employeeTasks.add(employeeTask);
        updateEmployeeList();
    }

    private void processGetNewQC(Employee employee){
        if(currentMode != Mode.MODE_QC_CONFIRM)
            return;
        String id = employee.getId();
        if(qcDialog == null)
            return;
        qcDialog.SetConfirmButtonStatus(1,employee);
    }

    private void processGetNewManager(Employee employee){
        if(currentMode != Mode.MODE_MANAGER_CONFIRM)
            return;
        String id = employee.getId();
        if(mcDialog == null)
            return;
        mcDialog.SetConfirmButtonStatus(1,employee);
    }

    private void processGetNewTask(String taskId){
        if(currentMode != Mode.MODE_DEFAULT)
            return;
        FlowCard flowCard = findFlowCard(taskId);
        if(flowCard != null){
            saveCurrentTask();
            changeToTask(flowCard);
        }

    }

    private FlowCard findFlowCard(String flowCardId){
        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        if(!sqlLiteProxy.isAvailable()){
            sqlLiteProxy.start(dbHelper);
        }
        FlowCard flowCard = sqlLiteProxy.findFlowCard(flowCardId);
        return flowCard;
    }

    private void saveCurrentTask(){

    }

    private void changeToTask(FlowCard newFlowCard){
        if(newFlowCard == null)
            return;
        flowCard = newFlowCard;
        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        if(!sqlLiteProxy.isAvailable()){
            sqlLiteProxy.start(dbHelper);
        }
        ArrayList<EmployeeTask> tmpEmployeeLists = sqlLiteProxy.employeeTasks(flowCard.getId());
        if(tmpEmployeeLists != null){
            employeeTasks = tmpEmployeeLists;
            updateEmployeeList();
        }
    }

    private void refreshEmployeeTaskList(){
        if(simpleAdapter != null){
            simpleAdapter.notifyDataSetChanged();
        }
    }

    private void processListenNewHuman(String id){
        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        if(!sqlLiteProxy.isAvailable()){
            sqlLiteProxy.start(dbHelper);
        }
        Employee employee = sqlLiteProxy.findEmployee(id);
        if(employee == null)
           return;
        int level = employee.getEmployeeLevel();
        switch(level){
            case Employee.GENERAL_EMPLOYEE:
                processGetNewGeneralEmployee(employee);
                break;
            case Employee.QC_EMPLOYEE:
                processGetNewQC(employee);
                break;
            case Employee.MANAGER_EMPLOYEE:
                processGetNewManager(employee);
                break;
            default:
                break;

        }
        
        sqlLiteProxy.close();
    }
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ServiceConnection connection = new ServiceConnection() {


        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        private ZanRunService.ZanRunBinder mBinder;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (ZanRunService.ZanRunBinder) service;
            mBinder.StartListenPort();
        }
    };

    private void onSettingButtonClick(){
        if(settingButton == null)
            return;
        scDialog = new SettingConfirmDialog((Context)MainActivity.this);
        scDialog.clif = new SettingConfirmProcess();
        scDialog.show();
    }

    private void onRefreshButtonClick(){
        if(refreshButton == null)
            return;

        //refreshButton.setEnabled(false);
        updateData();
    }
    @Override
    public void onClick(View view){
        if(view == null)
            return;
        if(view == refreshButton)
            onRefreshButtonClick();
        else if(view == settingButton)
            onSettingButtonClick();
    }

    public boolean isUpdating(){
        return isUpdating;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolBar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setTitle("赞润工位机");
        setSupportActionBar(toolBar);

        settingButton = (Button)findViewById(R.id.toolbar_setting_button);
        settingButton.setOnClickListener(this);
        refreshButton = (Button)findViewById(R.id.toolbar_refresh_button);
        refreshButton.setOnClickListener(this);

        dbHelper = new ZanRunDBHelper((Context) this);
        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        sqlLiteProxy.start(dbHelper);

        SettingProxy settingProxy = SettingProxy.getInstance();
        settingProxy.setProcedureId("411c9fc4-1b99-11e7-9736-04de4ce49026");
        String procedureId = settingProxy.getProcedureId();
        procedure = sqlLiteProxy.findProcedure(procedureId);


        ListView listView = (ListView) findViewById(R.id.employees_list);
        String[] strs = {"employee_name","production_num","bad_num"};
        int[] ids = {R.id.employee_name,R.id.production_num,R.id.bad_num};


        int num = 3;
        getData(num);
        simpleAdapter = new EmployeeSimpleAdapter(this,employeeList,R.layout.employee_list_item,strs,ids);

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new EmployeeItemClickListener());


        /*Intent startIntent = new Intent(this, ZanRunService.class);
        startService(startIntent);

        Intent bindIntent = new Intent(this, ZanRunService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        */



        PortListener portListener = new PortListener();
        portListenHandler = new PortListenHandler(getMainLooper());
        portListener.setPortListenHandler(portListenHandler);
        Thread thread = new Thread(portListener);

        thread.start();

        //updateData();


        //SettingTest.settingTest("abcd","dcba");

        Thread thread1=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e("1111", "111111111");
                // TODO Auto-generated method stub
                //DBTest.dbTest();
            }
        });
        thread1.start();


    }

    protected void getData(int num){
        //ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
        //int num = 3;
        for(int i=0;i<num;i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put(EmployeeSimpleAdapter.EMPLOYEE_NAME,"liupan"+i);
            map.put(EmployeeSimpleAdapter.PRODUCTION_NUM,10*i+1);
            map.put(EmployeeSimpleAdapter.BAD_NUM,2*i+3);
            map.put(EmployeeSimpleAdapter.EMPLOYEE_ID,""+i);
            map.put(EmployeeSimpleAdapter.EMPLOYEE_STATUS,i%2);
            map.put(EmployeeSimpleAdapter.EMPLOYEE_ID,"id_liupan"+i);
            map.put(EmployeeSimpleAdapter.EMPLOYEE_TASK_ID,"task_id_liupan"+i);
            employeeList.add(map);
        }
        HashMap<String,Object> emptyMap =  new HashMap<String,Object>();
        employeeList.add(emptyMap);
    }

    protected void insertEmplpoyee(Employee employee){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put(EmployeeSimpleAdapter.EMPLOYEE_NAME,employee.getName());
        map.put(EmployeeSimpleAdapter.PRODUCTION_NUM,0);
        map.put(EmployeeSimpleAdapter.BAD_NUM,0);
        map.put(EmployeeSimpleAdapter.EMPLOYEE_ID,employee.getId());
        map.put(EmployeeSimpleAdapter.EMPLOYEE_STATUS,0);
        employeeList.add(0,map);
    }

    protected void removeEmployee(String employeeId){
        for (HashMap<String,Object> employee: employeeList
             ) {
            if(((String)employee.get(employeeId)) == employeeId){
                employeeList.remove(employee);
                break;
            }
        }
    }

    public class PortListenHandler extends Handler{

        public PortListenHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
            String rfid = (String)msg.obj;
            Employee employee = sqlLiteProxy.findEmployeeWithRfid(rfid);
            if(employee != null){
                processGetNewHuman(employee);
                return;
            }
            FlowCard newFlowCard = sqlLiteProxy.findFlowCardWithRfid(rfid);
            if(newFlowCard != null){
                processGetNewFlowCard(newFlowCard);
                return;
            }

        }
    }

    public class DataUpdaterHandler extends  Handler{
        public DataUpdaterHandler(Looper looper){super(looper);}

        @Override
        public void handleMessage(Message msg){
            int status = msg.what;

        }

        private void startUpdate(){
            isUpdating = true;
            if(MainActivity.this.refreshButton != null)
                MainActivity.this.refreshButton.setEnabled(false);
        }

        private void endUpdate(){
            isUpdating = false;
            if(MainActivity.this.refreshButton != null){
                MainActivity.this.refreshButton.setEnabled(true);
            }
        }
    }

    private class EmployeeItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
            /*String name = (String)(employeeList.get(arg2).get(EmployeeSimpleAdapter.EMPLOYEE_NAME));
            Log.i("tag",name);

            QCConfirmDialog qcConfirmDialog = new QCConfirmDialog((Context)MainActivity.this);
            qcConfirmDialog.show();*/
            int employeeTaskStatus = (int)(employeeList.get(arg2).get(EmployeeSimpleAdapter.EMPLOYEE_STATUS));
            String employeeTaskId = (String)(employeeTasks.get(arg2).getId());
            switch (employeeTaskStatus){
                case EmployeeTask.ET_STATUS_DEFAULT:{
                    qcDialog = new QCConfirmDialog((Context)MainActivity.this,employeeTaskId);
                    qcDialog.clif = new QCConfirmProcess();
                    MainActivity.this.currentMode = MainActivity.Mode.MODE_QC_CONFIRM;
                    qcDialog.show();
                    break;
                }
                    
                case EmployeeTask.ET_STATUS_QC_CONFIRM:{
                    try{
                        mcDialog = new ManagerConfirmDialog((Context)MainActivity.this,employeeTaskId);
                        mcDialog.clif = new ManagerConfirmProcess();
                        MainActivity.this.currentMode = MainActivity.Mode.MODE_MANAGER_CONFIRM;
                        mcDialog.show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                }

                    
                case EmployeeTask.ET_STATUS_MANAGER_CONFIRM:

                    break;
                default:
                    break;
            }
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private class ManagerConfirmProcess implements ManagerConfirmDialog.ClickListenerInterFace{
        public void DoConfirm(int proNum,int badProNum,String employeeTaskId,Dialog dialog,Employee manager){       
            
            for(int i=0;i<employeeTasks.size();i++){
                if(employeeTasks.get(i).getId() == employeeTaskId){
                    employeeTasks.get(i).setStatus(EmployeeTask.ET_STATUS_MANAGER_CONFIRM);
                    employeeTasks.get(i).setManagerId(manager.getId());
                    SqlLiteProxy sqlLiteProxy =  SqlLiteProxy.getInstance();
                    if(!sqlLiteProxy.isAvailable())
                        sqlLiteProxy.start(dbHelper);
                    sqlLiteProxy.updateEmployeeTask(employeeTasks.get(i));
                    updateEmployeeList();
                    break;
                }                
            }
            
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.mcDialog = null;
            MainActivity.this.currentMode = MainActivity.Mode.MODE_DEFAULT;
        }

        public void DoCancel(Dialog dialog){
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.mcDialog = null;
            MainActivity.this.currentMode = MainActivity.Mode.MODE_DEFAULT;
        }
    } 

    private class QCConfirmProcess implements QCConfirmDialog.ClickListenerInterFace{
        public void DoConfirm(String employeeTaskId,Dialog dialog,Employee qc){
            
            for(int i=0;i<employeeTasks.size();i++){
                if(employeeTasks.get(i).getId() == employeeTaskId){
                    employeeTasks.get(i).setStatus(EmployeeTask.ET_STATUS_QC_CONFIRM);
                    employeeTasks.get(i).setManagerId(qc.getId());
                    SqlLiteProxy sqlLiteProxy =  SqlLiteProxy.getInstance();
                    if(!sqlLiteProxy.isAvailable())
                        sqlLiteProxy.start(dbHelper);
                    try{
                        sqlLiteProxy.updateEmployeeTask(employeeTasks.get(i));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }


                    updateEmployeeList();
                    break;
                }                
            }
            
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.qcDialog = null;
            MainActivity.this.currentMode = MainActivity.Mode.MODE_DEFAULT;
        }

        public void DoCancel(Dialog dialog){
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.qcDialog = null;
            MainActivity.this.currentMode = MainActivity.Mode.MODE_DEFAULT;
        }
    }

    private class SettingConfirmProcess implements SettingConfirmDialog.ClickListenerInterFace{
        public void DoConfirm(String type,String Ip,Dialog dialog){
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.scDialog = null;
            SettingProxy settingProxy = SettingProxy.getInstance();
            settingProxy.setServerHost(Ip);
            settingProxy.setProcedureId(type);
            MainActivity.this.currentMode = MainActivity.Mode.MODE_DEFAULT;
        }

        public void DoCancel(Dialog dialog){
            if(dialog != null){
                dialog.dismiss();
            }
            MainActivity.this.scDialog = null;
            MainActivity.this.currentMode = MainActivity.Mode.MODE_DEFAULT;
        }
    }

    private void updateData(){

        DataUpdater tmpDataUpdater = new DataUpdater();
        tmpDataUpdater.setMainActivity(this);
        tmpDataUpdater.setbLoop(false);
        tmpDataUpdater.setHandler(new DataUpdaterHandler(getMainLooper()));
        Thread thread = new Thread(tmpDataUpdater);
        thread.start();

        //thread.start();

    }

    private static String fcIds[] = {"1873e443-21a0-11e7-9c4c-68f728df42b8","18798792-21a0-11e7-9c4c-68f728df42b8"};
    private static String employeeIds[] = {"12bb54af-21a0-11e7-9c4c-68f728df42b8","12bb5967-21a0-11e7-9c4c-68f728df42b8"};
    private static String procedureIds[] = {"748b6b04-21a6-11e7-9c4c-68f728df42b8","748b71a6-21a6-11e7-9c4c-68f728df42b8"};

    private ArrayList<EmployeeTask> createTestTask(int taskNum){
        ArrayList<EmployeeTask> tasks = new ArrayList<EmployeeTask>();
        for(int i=0;i < taskNum;i++){
            EmployeeTask task = new EmployeeTask();
            task.setStartTime("2017-01-01 12:00:00");
            task.setUpdateTime("2017-01-01 16:00:00");
            task.setStatus(0);
            task.setCompanyId("comId"+i);
            task.setBadProductionNum(0);
            task.setEmployeeId(employeeIds[i]);
            task.setEmployeeName("employName" + i);
            task.setFcId(fcIds[i]);
            task.setProcedureId(procedureIds[i]);
            task.setProductionNum(i);
            tasks.add(task);
        }
        return tasks;
    }

    private void uploadLocalData(){
        SettingProxy settingProxy = SettingProxy.getInstance();
        String host = settingProxy.getServerHost();
        String[] tmp = host.split(":");
        //WebRequestProxy.server_ip = "192.168.0.121";
        //WebRequestProxy.server_port = 8080;
        WebRequestProxy.server_ip = tmp[0];
        WebRequestProxy.server_port = tmp.length == 2?Integer.parseInt(tmp[1]):8080;
        //ArrayList<EmployeeTask > tasks = createTestTask(2);
        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        ArrayList<EmployeeTask> finishEmployeeTasks = sqlLiteProxy.finishedEmployeeTasks();
        WebRequestProxy wrp = WebRequestProxy.getInstance();
        wrp.submitTasks(finishEmployeeTasks);
        for(int i=0;i<finishEmployeeTasks.size();i++){
            sqlLiteProxy.deleteEmployeeTask(finishEmployeeTasks.get(i).getId());
        }
    }

    private void pullServerData(){

        //SettingProxy settingProxy = SettingProxy.getInstance();

        //WebRequestProxy.server_ip = "192.168.0.158";
        WebRequestProxy.server_ip = "liugang187.wicp.net";
        WebRequestProxy.server_port = 80;
        /*String host = settingProxy.getServerHost();
        String[] tmp = host.split(":");
        WebRequestProxy.server_ip = tmp[0];
        WebRequestProxy.server_port = tmp.length == 2?Integer.parseInt(tmp[1]):8080;*/
        WebRequestProxy wrp = WebRequestProxy.getInstance();

        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        ArrayList<Employee> employees = wrp.getEmployees();
        try{
            ArrayList<Employee> oldEmployees = sqlLiteProxy.employees();
            for(int i = 0;i<oldEmployees.size();i++){
                sqlLiteProxy.deleteEmployee(employees.get(i));
            }
            for(int i=0;i<employees.size();i++){
                sqlLiteProxy.insertEmployee(employees.get(i));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }



        ArrayList<FlowCard> flowCards = wrp.getFlowCards();
        ArrayList<FlowCard> oldFlowCards = sqlLiteProxy.flowCards();
        for(int i=0;i<oldFlowCards.size();i++){
            sqlLiteProxy.deleteFlowCard(oldFlowCards.get(i).getId());
        }
        for(int i=0;i<flowCards.size();i++){
            sqlLiteProxy.insertFlowCard(flowCards.get(i));
        }


        ArrayList<Procedure> procedures = wrp.getProcedures();
        ArrayList<Procedure> oldProcedures = sqlLiteProxy.procedures();
        for(int i=0;i<oldProcedures.size();i++){
            sqlLiteProxy.deleteProcedure(oldProcedures.get(i).getId());
        }
        for(int i=0;i<procedures.size();i++){
            try{
                sqlLiteProxy.insertProcedure(procedures.get(i));
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
