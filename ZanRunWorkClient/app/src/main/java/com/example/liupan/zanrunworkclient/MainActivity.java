package com.example.liupan.zanrunworkclient;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
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
            orderNoEditText.setText(flowCard.getCardNo());
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
            employeeList.add(map);
        }
        refreshEmployeeTaskList();
    }

    private void processGetNewGeneralEmployee(Employee employee){
        if(currentMode != Mode.MODE_DEFAULT)
            return;
        if(procedure == null )
            return;
        for(int i=0;i<employeeTasks.size();i++){
            if(employeeTasks.get(i).getEmployeeId().equals(employee.getId()))
                return;
        }
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
        if(mcDialog != null)
            mcDialog.SetConfirmButtonStatus(1,employee);
        if(scDialog != null)
            scDialog.SetConfirmButtonStatus(1,employee);
    }



    private void refreshEmployeeTaskList(){
        if(simpleAdapter != null){
            simpleAdapter.notifyDataSetChanged();
        }
    }


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }



    private void onSettingButtonClick(){
        if(settingButton == null)
            return;
        boolean ensureButtonEnabled = !SettingProxy.getInstance(getApplicationContext()).isInit();
        scDialog = new SettingConfirmDialog((Context)MainActivity.this,ensureButtonEnabled);
        scDialog.clif = new SettingConfirmProcess();
        currentMode = Mode.MODE_MANAGER_CONFIRM;
        scDialog.show();
    }

    private void onRefreshButtonClick(){
        if(refreshButton == null)
            return;
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

        SettingProxy settingProxy = SettingProxy.getInstance(MainActivity.this.getApplicationContext());
        String procedureId = settingProxy.getProcedureId();
        procedure = sqlLiteProxy.findProcedure(procedureId);

        ListView listView = (ListView) findViewById(R.id.employees_list);
        String[] strs = {"employee_name","production_num","bad_num"};
        int[] ids = {R.id.employee_name,R.id.production_num,R.id.bad_num};

        simpleAdapter = new EmployeeSimpleAdapter(this,employeeList,R.layout.employee_list_item,strs,ids);

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new EmployeeItemClickListener());

        PortListener portListener = new PortListener();
        portListenHandler = new PortListenHandler(getMainLooper());
        portListener.setPortListenHandler(portListenHandler);
        Thread thread = new Thread(portListener);
        thread.start();

        DataUpdater dataUpdater = new DataUpdater();
        dataUpdater.setbLoop(true);
        dataUpdater.setMainActivity(this);
        dataUpdater.setHandler(new DataUpdaterHandler(getMainLooper()));
        Thread thread1 = new Thread(dataUpdater);
        thread1.start();
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
            if(status == 1){
                startUpdate();
            }

            if(status == 2){
                endUpdate();
            }

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
            if(dialog != null)
                dialog.dismiss();
            for(int i=0;i<employeeTasks.size();i++){
                if(employeeTasks.get(i).getId() == employeeTaskId){
                    if(proNum < 0 || badProNum < 0)
                        break;
                    employeeTasks.get(i).setStatus(EmployeeTask.ET_STATUS_MANAGER_CONFIRM);
                    employeeTasks.get(i).setManagerId(manager.getId());
                    employeeTasks.get(i).setProductionNum(proNum);
                    employeeTasks.get(i).setBadProductionNum(badProNum);
                    SqlLiteProxy sqlLiteProxy =  SqlLiteProxy.getInstance();
                    if(!sqlLiteProxy.isAvailable())
                        sqlLiteProxy.start(dbHelper);
                    sqlLiteProxy.updateEmployeeTask(employeeTasks.get(i));
                    updateEmployeeList();
                    break;
                }                
            }
            

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
            SettingProxy settingProxy = SettingProxy.getInstance(MainActivity.this.getApplicationContext());
            settingProxy.setServerHost(Ip);
            settingProxy.setProcedureId(type);
            MainActivity.this.currentMode = MainActivity.Mode.MODE_DEFAULT;
            MainActivity.this.scDialog = null;
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
    }


}
