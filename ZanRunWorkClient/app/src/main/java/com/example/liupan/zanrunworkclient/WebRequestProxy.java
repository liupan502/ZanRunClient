package com.example.liupan.zanrunworkclient;

import com.example.liupan.zanrunworkclient.entity.Employee;
import com.example.liupan.zanrunworkclient.entity.FlowCard;
import com.example.liupan.zanrunworkclient.entity.Procedure;
import com.example.liupan.zanrunworkclient.entity.ProcedureInfo;
import com.example.liupan.zanrunworkclient.EntityJsonExample;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liupan on 2017/3/11.
 */

public class WebRequestProxy {

    public static WebRequestProxy getInstance(){
        if(instance == null)
            instance = new WebRequestProxy();
        return instance;
    }

    private static WebRequestProxy instance = null;

    private boolean bEnabledWeb = false;

    private WebRequestProxy(){
        ;
    }

    public ArrayList<Employee> getEmployees(){
        return bEnabledWeb?getEmployeesFromServer():getEmployeesFromLocal();
    }

    public ArrayList<Procedure> getProcedures(){
        return bEnabledWeb?getProcedureFromServer():getProceduresFromLocal();
    }

    public ArrayList<FlowCard> getFlowCards(){
        return bEnabledWeb?getFlowCardFromServer():getFlowCardFromLocal();
    }

    public void updateProcedureInfos(ArrayList<ProcedureInfo> procedureInfos){
        if (bEnabledWeb)
            updateProcedureInfosToServer(procedureInfos);
        else
            updateProcedureInfosToLocal(procedureInfos);
    }

    private ArrayList<Employee> getEmployeesFromString(String jsonStr){
        ArrayList<Employee> employees = null;
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>) jsonObject.get("data");
            for(int i=0;i<data.size();i++){
                Employee employee = new Employee();
                employee.initWithMap(data.get(i));
                employees.add(employee);
            }
        }
        catch(Exception e){

        }
        return employees;
    }

    private ArrayList<Employee> getEmployeesFromLocal(){
        return getEmployeesFromString(EntityJsonExample.EMPLOYEES);
    }

    private ArrayList<Employee> getEmployeesFromServer(){
        return null;
    }

    private ArrayList<Procedure> getProceduresFromString(String jsonStr){
        ArrayList<Procedure> procedures = null;
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>) jsonObject.get("data");
            for(int i=0;i<data.size();i++){
                Procedure procedure = new Procedure();
                procedure.initWithMap(data.get(i));
                procedures.add(procedure);
            }
        }
        catch (Exception e){

        }
        return procedures;
    }


    private ArrayList<Procedure> getProceduresFromLocal(){
        return getProceduresFromString(EntityJsonExample.PROCEDURES);
    }

    private ArrayList<Procedure> getProcedureFromServer(){
        return null;
    }

    private ArrayList<FlowCard> getFlowCardFromString(String jsonStr){
        ArrayList<FlowCard> flowCards = null;
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>) jsonObject.get("data");
            for(int i=0;i<data.size();i++){
                FlowCard flowCard = new FlowCard();
                flowCard.initWithMap(data.get(i));
                flowCards.add(flowCard);
            }
        }
        catch(Exception e){

        }
        return flowCards;
    }


    private ArrayList<FlowCard> getFlowCardFromLocal(){
        return getFlowCardFromString(EntityJsonExample.FLOWCARDS);
    }

    private ArrayList<FlowCard> getFlowCardFromServer(){
        return null;
    }

    private void updateProcedureInfosToLocal(ArrayList<ProcedureInfo> procedureInfos){

    }

    private void updateProcedureInfosToServer(ArrayList<ProcedureInfo> procedureInfos){

    }
}
