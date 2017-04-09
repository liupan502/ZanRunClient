package com.example.liupan.zanrunworkclient;

import com.example.liupan.zanrunworkclient.entity.Employee;
import com.example.liupan.zanrunworkclient.entity.FlowCard;
import com.example.liupan.zanrunworkclient.entity.Procedure;
import com.example.liupan.zanrunworkclient.entity.ProcedureInfo;
import com.example.liupan.zanrunworkclient.EntityJsonExample;
import org.json.JSONObject;
import org.json.JSONArray;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;  
import org.json.JSONException;  
import org.json.JSONObject;  

/**
 * Created by liupan on 2017/3/11.
 */

public class WebRequestProxy {

    public static final String CODE = "";
    public static final String DATA = "";

    public static final String ID = "";
    public static final String COMPANY_ID = "";

    public static final String EMPLOYEE_NO = "";
    public static final String EMPLOYEE_LEVEL = "";
    public static final String EMPLOYEE_NAME = "";

    public static final String FLOWCARD_NO = "";
    public static final String FLOWCARD_PRODUCTION_NAME = "";
    public static final String FLOWCARD_PRODUCTION_NO = "";
    public static final String FLOWCARD_TERMINAL_DATE = "";
    public static final String FLOWCARD_ORDER_NUM = "";
    public static final String FLOWCARD_MANTI_NUM = "";
    public static final String FLOWCARD_PROCEDURE_INFOS = "";

    public static final String PROCEDURE_NO = "";
    public static final String PROCEDURE_NAME = "";

    public static final String PROCEDURE_INFO_NUM = "";
    public static final String PROCEDURE_INFO_REQUEST = "";
    public static final String PROCEDURE_INFO_QC_CONFIRM_STATUS = "";
    public static final String PROCEDURE_INFO_FLOWCARD_ID = "";

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
        /*ArrayList<Employee> employees = null;
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
        return employees;*/
        //JSONObject jsonOn = new JSONObject();
        ArrayList<Employee> result = new ArrayList<Employee>();
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            int code = jsonObject.getInt(CODE);
            JSONArray data = jsonObject.getJSONArray(DATA);

            for(int i=0;i<data.length();i++){
                Employee ep = new Employee();
                JSONObject employeeJsonObject = data.getJSONObject(i);
                String id = employeeJsonObject.getString(ID);
                String companyId = employeeJsonObject.getString(COMPANY_ID);
                String no = employeeJsonObject.getString(EMPLOYEE_NO);
                int level = employeeJsonObject.getInt(EMPLOYEE_LEVEL);
                String name = employeeJsonObject.getString(EMPLOYEE_NAME);

                ep.setId(id);
                ep.setCompanyId(companyId);
                ep.setEmployeeNo(no);
                ep.setEmployeeLevel(level);
                ep.setName(name);

                result.add(ep);
            }
        }
        catch(Exception e){

        }


        return result;
    }

    private ArrayList<Employee> getEmployeesFromLocal(){
        return getEmployeesFromString(EntityJsonExample.EMPLOYEES);
    }

    private ArrayList<Employee> getEmployeesFromServer(){
        String jsonStr = "";
        ArrayList<Employee> result = getEmployeesFromString(jsonStr);
        return result;
    }

    private ArrayList<Procedure> getProceduresFromString(String jsonStr){
        /*ArrayList<Procedure> procedures = null;
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
        */
        ArrayList<Procedure> result = new ArrayList<Procedure>();
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            int code = jsonObject.getInt(CODE);
            JSONArray data = jsonObject.getJSONArray(DATA);

            for(int i=0;i<data.length();i++){
                Procedure pd = new Procedure();
                JSONObject procedureJsonObject = data.getJSONObject(i);
                String id = procedureJsonObject.getString(ID);
                String companyId = procedureJsonObject.getString(COMPANY_ID);
                String no = procedureJsonObject.getString(PROCEDURE_NO);
                //int level = procedureJsonObject.getInt(PROCEDURE_NAME);
                String name = procedureJsonObject.getString(PROCEDURE_NAME);

                pd.setId(id);
                pd.setCompanyId(companyId);
                pd.setProcedureNo(no);
                //pd.setEmployeeLevel(level);
                pd.setProcedureName(name);

                result.add(pd);
            }
        }
        catch(Exception e){

        }

        return result;

        
    }


    private ArrayList<Procedure> getProceduresFromLocal(){
        return getProceduresFromString(EntityJsonExample.PROCEDURES);
    }

    private ArrayList<Procedure> getProcedureFromServer(){
        return null;
    }

    private ArrayList<FlowCard> getFlowCardFromString(String jsonStr){
        /*ArrayList<FlowCard> flowCards = null;
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
        */

        ArrayList<FlowCard> result = new ArrayList<FlowCard>();
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            int code = jsonObject.getInt(CODE);
            JSONArray data = jsonObject.getJSONArray(DATA);

            for(int i=0;i<data.length();i++){
                FlowCard fc = new FlowCard();
                JSONObject flowcardJsonObject = data.getJSONObject(i);
                String id = flowcardJsonObject.getString(ID);
                String companyId = flowcardJsonObject.getString(COMPANY_ID);
                String no = flowcardJsonObject.getString(FLOWCARD_NO);
                String productionName = flowcardJsonObject.getString(FLOWCARD_PRODUCTION_NAME);
                String productionNo = flowcardJsonObject.getString(FLOWCARD_PRODUCTION_NO);
                String termianlDate = flowcardJsonObject.getString(FLOWCARD_TERMINAL_DATE);
                int  orderNum = flowcardJsonObject.getInt(FLOWCARD_ORDER_NUM);
                int mantiNum = flowcardJsonObject.getInt(FLOWCARD_MANTI_NUM);
                JSONArray procedureInfoArray = flowcardJsonObject.getJSONArray(FLOWCARD_PROCEDURE_INFOS);

                fc.setId(id);
                fc.setCompanyId(companyId);
                fc.setCardNo(no);
                fc.setProductionName(productionName);
                fc.setProductionNo(productionNo);
                fc.setTerminalDate(termianlDate);
                fc.setOrderNum(orderNum);
                fc.setMantiNum(mantiNum);
                ArrayList<ProcedureInfo> procedureInfos = new ArrayList<ProcedureInfo>();
                for(int j=0;j<procedureInfoArray.length();j++){
                    ProcedureInfo pi = new ProcedureInfo();
                    JSONObject procedureJsonObject = procedureInfoArray.getJSONObject(j);
                    String procedureId = procedureJsonObject.getString(ID);
                    String procedureCompanyId = procedureJsonObject.getString(COMPANY_ID);
                    String request = procedureJsonObject.getString(PROCEDURE_INFO_REQUEST);
                    int num = procedureJsonObject.getInt(PROCEDURE_INFO_NUM);
                    int status = procedureJsonObject.getInt(PROCEDURE_INFO_QC_CONFIRM_STATUS);

                    pi.setId(procedureId);
                    pi.setCompanyId(procedureCompanyId);
                    pi.setProcedureRequest(request);
                    pi.setQcConfirmStatus(status);
                    pi.setFlowCardNo(fc.getId());
                    pi.setNum(num);
                    procedureInfos.add(pi);
                }
                fc.setProcedureInfos(procedureInfos);
                //fc.setName(name);

                result.add(fc);
            }
        }
        catch(Exception e){

        }


        return result;

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
