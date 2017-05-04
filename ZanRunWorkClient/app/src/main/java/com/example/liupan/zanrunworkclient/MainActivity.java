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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.liupan.zanrunworkclient.*;

import com.example.liupan.zanrunworkclient.EmployeeSimpleAdapter;

import com.example.liupan.zanrunworkclient.entity.*;

import Dialog.QCConfirmDialog;
import Test.DBTest;
import Test.SettingTest;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<HashMap<String,Object>> employeeList = new ArrayList<HashMap<String, Object>>();

    private ArrayList<EmployeeTask> employeeTasks = new ArrayList<EmployeeTask>();

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

    private void processGetNewHuman(String humanId){
        SqlLiteProxy sqlLiteProxy =  SqlLiteProxy.getInstance();
        if(!sqlLiteProxy.isAvailable())
            sqlLiteProxy.start(dbHelper);
        Employee employee = sqlLiteProxy.findEmployee(humanId);
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

    private void updateEmployeeList(){
        employeeList.clear();
        employeeList.add(new HashMap<String,Object>());
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
        EmployeeTask employeeTask = new EmployeeTask(employee,null, procedure);
        SqlLiteProxy sqlLiteProxy =  SqlLiteProxy.getInstance();
        if(!sqlLiteProxy.isAvailable())
            sqlLiteProxy.start(dbHelper);
        sqlLiteProxy.insertEmployeeTask(employeeTask);
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

        //employeeTasks = sqlLiteProxy
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
        scDialog.show();
    }

    private void onRefreshButtonClick(){
        if(refreshButton == null)
            return;
        refreshButton.setEnabled(false);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolBar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setTitle("this is a title");
        setSupportActionBar(toolBar);
        toolBar.setTitle("this is a tool bar");
        settingButton = (Button)findViewById(R.id.toolbar_setting_button);
        settingButton.setOnClickListener(this);
        refreshButton = (Button)findViewById(R.id.toolbar_refresh_button);
        refreshButton.setOnClickListener(this);

        dbHelper = new ZanRunDBHelper((Context) this);
        SqlLiteProxy.getInstance().start(dbHelper);
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.order_no);
        tv.setText(stringFromJNI());

        ListView listView = (ListView) findViewById(R.id.employees_list);
        String[] strs = {"employee_name","production_num","bad_num"};
        int[] ids = {R.id.employee_name,R.id.production_num,R.id.bad_num};


        int num = 3;
        getData(num);
        simpleAdapter = new EmployeeSimpleAdapter(this,employeeList,R.layout.employee_list_item,strs,ids);
        //SimpleAdapter simpleAdapter = new SimpleAdapter(this,employeeList,R.layout.employee_list_item,strs,ids);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new EmployeeItemClickListener());


        Intent startIntent = new Intent(this, ZanRunService.class);
        startService(startIntent);

        Intent bindIntent = new Intent(this, ZanRunService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);


        //PortListener portListener = new PortListener();
        //Thread thread = new Thread(portListener);
        //thread.start();

        //updateData();


        //SettingTest.settingTest("abcd","dcba");

        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e("1111", "111111111");
                // TODO Auto-generated method stub
                //DBTest.dbTest();
            }
        });
        thread.start();


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

    private class PortListenHandler extends Handler{

        public PortListenHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case PortListener.HUNMAN_TYPE:{
                    String id = (String)msg.obj;
                    processListenNewHuman(id);
                    break;
                }

                    
                case PortListener.TASK_TYPE:
                    break;
                default:
                    break;
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
            String employeeTaskId = (String)(employeeList.get(arg2).get(EmployeeSimpleAdapter.EMPLOYEE_TASK_ID));
            switch (employeeTaskStatus){
                case EmployeeTask.ET_STATUS_DEFAULT:{
                    qcDialog = new QCConfirmDialog((Context)MainActivity.this,employeeTaskId);
                    qcDialog.clif = new QCConfirmProcess();
                    MainActivity.this.currentMode = MainActivity.Mode.MODE_QC_CONFIRM;
                    qcDialog.show();
                    break;
                }
                    
                case EmployeeTask.ET_STATUS_QC_CONFIRM:{
                    mcDialog = new ManagerConfirmDialog((Context)MainActivity.this,employeeTaskId);
                    mcDialog.clif = new ManagerConfirmProcess();
                    MainActivity.this.currentMode = MainActivity.Mode.MODE_MANAGER_CONFIRM;
                    mcDialog.show();
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
                    sqlLiteProxy.updateEmployeeTask(employeeTasks.get(i));
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

        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                uploadLocalData();
                pullServerData();
            }
        });
        thread.start();

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
        WebRequestProxy.server_ip = "192.168.0.121";
        WebRequestProxy.server_port = 8080;
        ArrayList<EmployeeTask > tasks = createTestTask(2);
        WebRequestProxy wrp = WebRequestProxy.getInstance();
        wrp.submitTasks(tasks);
    }

    private void pullServerData(){

        WebRequestProxy.server_ip = "192.168.0.121";
        WebRequestProxy.server_port = 8080;
        WebRequestProxy wrp = WebRequestProxy.getInstance();

        //ArrayList<Employee> employees = wrp.getEmployees();

        //ArrayList<FlowCard> flowCards = wrp.getFlowCards();

        //ArrayList<Procedure> procedures = wrp.getProcedures();
    }
}
