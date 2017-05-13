package com.example.liupan.zanrunworkclient;

import android.os.Message;

import com.example.liupan.zanrunworkclient.entity.Employee;
import com.example.liupan.zanrunworkclient.entity.EmployeeTask;
import com.example.liupan.zanrunworkclient.entity.FlowCard;
import com.example.liupan.zanrunworkclient.entity.Procedure;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by liupan on 2017/5/13.
 */

public class DataUpdater implements Runnable {


    private static final int minuteNum = 1;

    private static final int minuteToSecondFactor = 60;

    private static final int secondToMsFactor = 1000;

    private boolean bLoop = false;

    private MainActivity.DataUpdaterHandler handler = null;

    private MainActivity mainActivity = null;

    public void setbLoop(boolean bLoop){
        this.bLoop = bLoop;
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setHandler(MainActivity.DataUpdaterHandler handler){
        this.handler = handler;
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

    @Override
    public void run(){
        while(true){
            if(mainActivity == null || handler == null)
                break;
            boolean isUpdating = mainActivity.isUpdating();
            if(!isUpdating){
                handler.removeMessages(0);
                Message startUpdateMsg = handler.obtainMessage(0);
                startUpdateMsg.sendToTarget();

                pullServerData();
                uploadLocalData();

                handler.removeMessages(0);
                Message endUpdateMsg = handler.obtainMessage(1);
                endUpdateMsg.sendToTarget();
            }
            try{
                if(bLoop)
                    sleep(minuteNum*minuteToSecondFactor*secondToMsFactor);
                else
                    break;
            }
            catch (Exception e){
                e.printStackTrace();
                break;
            }
        }


    }
}
