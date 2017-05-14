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


    private static final int minuteNum = 30;

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
        SettingProxy settingProxy = SettingProxy.getInstance(mainActivity.getApplicationContext());
        String ip = "";
        int port = -1;
        try{
            String host = settingProxy.getServerHost();
            String[] tmpStrs = host.split(":");
            if(tmpStrs.length == 2){
                ip = tmpStrs[0];
                port = Integer.parseInt(tmpStrs[1]);
            }
        }
        catch(Exception e){

        }
        if(port < 0 || ip.isEmpty())
            return;

        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        ArrayList<EmployeeTask> finishEmployeeTasks = sqlLiteProxy.finishedEmployeeTasks();
        WebRequestProxy wrp = WebRequestProxy.getInstance();
        wrp.submitTasks(finishEmployeeTasks);
        for(int i=0;i<finishEmployeeTasks.size();i++){
            sqlLiteProxy.deleteEmployeeTask(finishEmployeeTasks.get(i).getId());
        }
    }

    private void pullServerData(){
        SettingProxy settingProxy = SettingProxy.getInstance(mainActivity.getApplicationContext());
        String ip = "";
        int port = -1;
        try{
            String host = settingProxy.getServerHost();
            String[] tmpStrs = host.split(":");
            if(tmpStrs.length == 2){
                ip = tmpStrs[0];
                port = Integer.parseInt(tmpStrs[1]);
            }
        }
        catch(Exception e){

        }
        if(port < 0 || ip.isEmpty())
            return;

        WebRequestProxy.server_ip = ip;
        WebRequestProxy.server_port = port;

        boolean doPullDataSuccessfully = true;
        WebRequestProxy wrp = WebRequestProxy.getInstance();

        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        ArrayList<Employee> employees = wrp.getEmployees();

        if(employees.size() > 0){

            ArrayList<Employee> oldEmployees = sqlLiteProxy.employees();
            for(int i = 0;i<oldEmployees.size();i++){
                sqlLiteProxy.deleteEmployee(oldEmployees.get(i));
            }
            for(int i=0;i<employees.size();i++){
                sqlLiteProxy.insertEmployee(employees.get(i));
            }
        }
        else{
            doPullDataSuccessfully = false;
        }



        ArrayList<FlowCard> flowCards = wrp.getFlowCards();
        if(flowCards.size() > 0){
            ArrayList<FlowCard> oldFlowCards = sqlLiteProxy.flowCards();
            for(int i=0;i<oldFlowCards.size();i++){
                sqlLiteProxy.deleteFlowCard(oldFlowCards.get(i).getId());
            }
            for(int i=0;i<flowCards.size();i++){
                sqlLiteProxy.insertFlowCard(flowCards.get(i));
            }
        }
        else{
            doPullDataSuccessfully = false;
        }



        ArrayList<Procedure> procedures = wrp.getProcedures();
        if(procedures.size() > 0){
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
        else{
            doPullDataSuccessfully = false;
        }

        if(doPullDataSuccessfully == true && SettingProxy.getInstance(mainActivity.getApplicationContext()).isInit() == false){
            SettingProxy.getInstance(mainActivity.getApplicationContext()).setIsInit(true);
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
                Message startUpdateMsg = handler.obtainMessage(1);
                startUpdateMsg.sendToTarget();

                pullServerData();
                uploadLocalData();

                handler.removeMessages(0);
                Message endUpdateMsg = handler.obtainMessage(2);
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
