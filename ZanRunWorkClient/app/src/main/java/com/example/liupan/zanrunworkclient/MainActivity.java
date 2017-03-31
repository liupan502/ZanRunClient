package com.example.liupan.zanrunworkclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;
import com.example.liupan.zanrunworkclient.*;

import com.example.liupan.zanrunworkclient.ConfirmDialog.*;

import Dialog.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.liupan.zanrunworkclient.*;

import com.example.liupan.zanrunworkclient.EmployeeSimpleAdapter;

import com.example.liupan.zanrunworkclient.entity.*;

import Dialog.QCConfirmDialog;


public class MainActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,Object>> employeeList = new ArrayList<HashMap<String, Object>>();

    private enum Mode{
        MODE_DEFAULT,
        MODE_QC_CONFIRM,
        MODE_MANAGER_CONFIRM
    };

    private  Mode currentMode = Mode.MODE_DEFAULT;

    private Task task = null;

    private ManagerConfirmDialog mcDialog = null;

    private QCConfirmDialog qcDialog = null;

    private SettingConfirmDialog scDialog = null;

    private ZanRunDBHelper dbHelper = null;

    private void processGetNewHuman(String humanId){
        SqlLiteProxy sqlLiteProxy =  SqlLiteProxy.getInstance();
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

    private void processGetNewGeneralEmployee(Employee employee){
        if(currentMode != Mode.MODE_DEFAULT)
            return;
        insertEmplpoyee(employee);
    }

    private void processGetNewQC(Employee employee){
        if(currentMode != Mode.MODE_QC_CONFIRM)
            return;
        String id = employee.getId();
        if(qcDialog == null)
            return;
        qcDialog.set
    }

    private void processGetNewManager(Employee employee){
        if(currentMode != Mode.MODE_MANAGER_CONFIRM)
            return;
    }

    private void processGetNewTask(String taskId){
        if(currentMode != Mode.MODE_DEFAULT)
            return;
        saveCurrentTask();
        changeToTask(taskId);
    }

    private void saveCurrentTask(){

    }

    private void changeToTask(String taskId){

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ZanRunDBHelper((Context)this);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.order_no);
        tv.setText(stringFromJNI());

        ListView listView = (ListView) findViewById(R.id.employees_list);
        String[] strs = {"employee_name","production_num","bad_num"};
        int[] ids = {R.id.employee_name,R.id.production_num,R.id.bad_num};
        getData();
        EmployeeSimpleAdapter simpleAdapter = new EmployeeSimpleAdapter(this,employeeList,R.layout.employee_list_item,strs,ids);
        //SimpleAdapter simpleAdapter = new SimpleAdapter(this,employeeList,R.layout.employee_list_item,strs,ids);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new EmployeeItemClickListener());


        Intent startIntent = new Intent(this, ZanRunService.class);
        startService(startIntent);

        Intent bindIntent = new Intent(this, ZanRunService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);


        PortListener portListener = new PortListener();
        Thread thread = new Thread(portListener);
        thread.start();
    }

    protected void getData(){
        //ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
        int num = 3;
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
                    qcDialog.show();
                    break;
                }
                    
                case EmployeeTask.ET_STATUS_QC_CONFIRM:{
                    mcDialog = new ManagerConfirmDialog((Context)MainActivity.this,employeeTaskId);
                    mcDialog.clif = new ManagerConfirmProcess();
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
        public void DoConfirm(int proNum,int badProNum,String employeeTaskId,Dialog dialog){
            
            for(int i=0;i<employeeList.count();i++){
                HashMap<String,Object> map = (HashMap<String,Object>)employeeList.get(i);
                if(map.get(EmployeeSimpleAdapter.EMPLOYEE_TASK_ID) != employeeTaskId)
                    continue;

                map.put(EmployeeSimpleAdapter.EMPLOYEE_STATUS,EmployeeTask.ET_STATUS_MANAGER_CONFIRM);
                map.put(EmployeeSimpleAdapter.BAD_NUM,badProNum);
                map.put(EmployeeSimpleAdapter.PRODUCTION_NUM,proNum);
                break;
            }

            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.mcDialog = null;
        }

        public void DoCancel(Dialog dialog){
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.mcDialog = null;
        }
    } 

    private class QCConfirmProcess implements QCConfirmDialog.ClickListenerInterFace{
        public void DoConfirm(String employeeTaskId,Dialog dialog){
            
            for(int i=0;i<employeeList.count();i++){
                HashMap<String,Object> map = (HashMap<String,Object>)employeeList.get(i);
                if(map.get(EmployeeSimpleAdapter.EMPLOYEE_TASK_ID) != employeeTaskId)
                    continue;

                map.put(EmployeeSimpleAdapter.EMPLOYEE_STATUS,EmployeeTask.ET_STATUS_QC_CONFIRM);
                break;
            }
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.qcDialog = null;
        }

        public void DoCancel(Dialog dialog){
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.qcDialog = null;
        }
    }

    private class SettingConfirmProcess implements SettingConfirmDialog.ClickListenerInterFace{
        public void DoConfirm(String type,String Ip,Dialog dialog){
            if(dialog != null)
                dialog.dismiss();
            MainActivity.this.scDialog = null;
        }

        public void DoCancel(Dialog dialog){
            if(dialog != null){
                dialog.dismiss();
            }
            MainActivity.this.scDialog = null;
        }
    }


}
