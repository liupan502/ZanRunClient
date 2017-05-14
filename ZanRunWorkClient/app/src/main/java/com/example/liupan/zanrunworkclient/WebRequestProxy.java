package com.example.liupan.zanrunworkclient;

import com.example.liupan.zanrunworkclient.entity.Employee;
import com.example.liupan.zanrunworkclient.entity.EmployeeTask;
import com.example.liupan.zanrunworkclient.entity.FlowCard;
import com.example.liupan.zanrunworkclient.entity.Procedure;
import com.example.liupan.zanrunworkclient.entity.ProcedureInfo;
import com.example.liupan.zanrunworkclient.EntityJsonExample;
import org.json.JSONObject;
import org.json.JSONArray;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;  
import org.json.JSONException;  
import org.json.JSONObject;  

/**
 * Created by liupan on 2017/3/11.
 */

public class WebRequestProxy {

    public static  String server_ip = "";
    public static  int server_port = 8080;
    public static final String BASE_PATH = "/api/";
    public static final String GET_EMPLOYEE_LIST = "emplist";
    public static final String GET_TECHLIST = "techlist";
    public static final String GET_CARDLIST = "cardlist";
    public static final String UPLOAD_DATA = "upWorkload";

    public static final String CODE = "code";
    public static final String DATA = "data";

    public static final String ID = "";
    public static final String COMPANY_ID = "";

    public static final String EMPLOYE_ID = "empuuid";
    public static final String EMPLOYEE_NO = "empNo";
    public static final String EMPLOYEE_LEVEL = "empLev";
    public static final String EMPLOYEE_NAME = "empName";
    public static final String EMPLOYEE_RFID = "rfid";

    public static final String FLOWCARD_ID = "carduuid";
    public static final String FLOWCARD_NO = "cardNo";
    public static final String FLOWCARD_PRODUCTION_NAME = "productName";
    public static final String FLOWCARD_PRODUCTION_NO = "productNo";
    public static final String FLOWCARD_TERMINAL_DATE = "compDate";
    public static final String FLOWCARD_ORDER_NUM = "orderNum";
    public static final String FLOWCARD_MANTI_NUM = "mantiNum";
    public static final String FLOWCARD_PROCEDURE_INFOS = "techList";
    public static final String FLOWCARD_RFID = "rfid";

    public static final String PROCEDURE_ID = "techuuid";
    public static final String PROCEDURE_NO = "techNo";
    public static final String PROCEDURE_NAME = "techName";

    public static final String PROCEDURE_INFO_ID = "techuuid";
    public static final String PROCEDURE_INFO_NO = "techNo";
    public static final String PROCEDURE_INFO_NUM = "techNum";
    public static final String PROCEDURE_INFO_REQUEST = "techReq";
    public static final String PROCEDURE_INFO_QC_CONFIRM_STATUS = "qcConfirm";
    public static final String PROCEDURE_INFO_FLOWCARD_ID = "";

    public static WebRequestProxy getInstance(){
        if(instance == null)
            instance = new WebRequestProxy();
        return instance;
    }

    private static WebRequestProxy instance = null;

    private boolean bEnabledWeb = true;

    private WebRequestProxy(){
        ;
    }

    public void submitTasks(ArrayList<EmployeeTask> tasks){
        try{
            JSONArray jsonArray = new JSONArray();
            for(int i=0;i<tasks.size();i++){
                EmployeeTask task = tasks.get(i);

                JSONObject json = new JSONObject();
                json.put("carduuid",task.getFcId());
                json.put("techuuid",task.getProcedureId());
                json.put("empuuid",task.getEmployeeId());
                json.put("beginTime",task.getStartTime());
                json.put("endTime",task.getUpdateTime());
                json.put("compNum",task.getProductionNum());
                json.put("badNum",task.getBadProductionNum());
                jsonArray.put(i,json);
            }
            //JSONObject jsonObject = new JSONObject();
            //jsonObject.put("param",jsonArray);
            String jsonStr = jsonArray.toString();
            String hostName = getHost(WebRequestProxy.server_ip,WebRequestProxy.server_port);
            String urlStr = hostName + BASE_PATH + UPLOAD_DATA;
            String result = getConntentFromServer(urlStr,jsonStr);
            //String result = getConntentFromServer(urlStr,"abcd");

            int a = 0;
        }
        catch (Exception e){
            int a = 0;
        }

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

        ArrayList<Employee> result = new ArrayList<Employee>();
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            int code = jsonObject.getInt(CODE);
            JSONArray data = jsonObject.getJSONArray(DATA);

            for(int i=0;i<data.length();i++){
                Employee ep = new Employee();
                JSONObject employeeJsonObject = data.getJSONObject(i);
                String id = employeeJsonObject.getString(EMPLOYE_ID);
                //String companyId = employeeJsonObject.getString(COMPANY_ID);
                String no = employeeJsonObject.getString(EMPLOYEE_NO);
                int level = employeeJsonObject.getInt(EMPLOYEE_LEVEL);
                String name = employeeJsonObject.getString(EMPLOYEE_NAME);
                String rfid = employeeJsonObject.getString(EMPLOYEE_RFID);

                ep.setId(id);
                //ep.setCompanyId(companyId);
                ep.setEmployeeNo(no);
                ep.setEmployeeLevel(level);
                ep.setName(name);
                ep.setRfid(rfid);

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
        String hostName = getHost(WebRequestProxy.server_ip,WebRequestProxy.server_port);
        String urlStr = hostName + BASE_PATH + GET_EMPLOYEE_LIST;
        String jsonStr = getConntentFromServer(urlStr,null);
        ArrayList<Employee> result = new ArrayList<Employee> ();
        if(jsonStr.length() > 0)
            result = getEmployeesFromString(jsonStr);
        return result;
    }

    private ArrayList<Procedure> getProceduresFromString(String jsonStr){

        ArrayList<Procedure> result = new ArrayList<Procedure>();
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            int code = jsonObject.getInt(CODE);
            JSONArray data = jsonObject.getJSONArray(DATA);

            for(int i=0;i<data.length();i++){
                Procedure pd = new Procedure();
                JSONObject procedureJsonObject = data.getJSONObject(i);
                String id = procedureJsonObject.getString(PROCEDURE_ID);
                //String companyId = procedureJsonObject.getString(COMPANY_ID);
                String no = procedureJsonObject.getString(PROCEDURE_NO);
                //int level = procedureJsonObject.getInt(PROCEDURE_NAME);
                String name = procedureJsonObject.getString(PROCEDURE_NAME);

                pd.setId(id);
                //pd.setCompanyId(companyId);
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
        String hostName = getHost(WebRequestProxy.server_ip,WebRequestProxy.server_port);
        String urlStr = hostName + BASE_PATH + GET_TECHLIST;
        String jsonStr = getConntentFromServer(urlStr,null);
        ArrayList<Procedure> result = new ArrayList<Procedure> ();
        if(jsonStr.length() > 0)
            result = getProceduresFromString(jsonStr);
        return result;
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
                String id = flowcardJsonObject.getString(FLOWCARD_ID);
                //String companyId = flowcardJsonObject.getString(COMPANY_ID);
                String no = flowcardJsonObject.getString(FLOWCARD_NO);
                String productionName = flowcardJsonObject.getString(FLOWCARD_PRODUCTION_NAME);
                String productionNo = flowcardJsonObject.getString(FLOWCARD_PRODUCTION_NO);
                String termianlDate = flowcardJsonObject.getString(FLOWCARD_TERMINAL_DATE);
                int  orderNum = flowcardJsonObject.getInt(FLOWCARD_ORDER_NUM);
                int mantiNum = flowcardJsonObject.getInt(FLOWCARD_MANTI_NUM);
                String rfid = flowcardJsonObject.getString(FLOWCARD_RFID);
                JSONArray procedureInfoArray = flowcardJsonObject.getJSONArray(FLOWCARD_PROCEDURE_INFOS);

                fc.setId(id);
                //fc.setCompanyId(companyId);
                fc.setCardNo(no);
                fc.setProductionName(productionName);
                fc.setProductionNo(productionNo);
                fc.setTerminalDate(termianlDate);
                fc.setOrderNum(orderNum);
                fc.setMantiNum(mantiNum);
                fc.setRfid(rfid);
                ArrayList<ProcedureInfo> procedureInfos = new ArrayList<ProcedureInfo>();
                for(int j=0;j<procedureInfoArray.length();j++){
                    ProcedureInfo pi = new ProcedureInfo();
                    JSONObject procedureJsonObject = procedureInfoArray.getJSONObject(j);
                    String procedureId = procedureJsonObject.getString(PROCEDURE_INFO_ID);
                    //String procedureCompanyId = procedureJsonObject.getString(COMPANY_ID);
                    String request = procedureJsonObject.getString(PROCEDURE_INFO_REQUEST);
                    String procedureNo = procedureJsonObject.getString(PROCEDURE_INFO_NO);
                    int num = procedureJsonObject.getInt(PROCEDURE_INFO_NUM);
                    String statusStr = procedureJsonObject.getString(PROCEDURE_INFO_QC_CONFIRM_STATUS);
                    int status = Integer.parseInt(statusStr);

                    pi.setId(procedureId);
                    //pi.setCompanyId(procedureCompanyId);
                    pi.setProcedureRequest(request);
                    pi.setQcConfirmStatus(status);
                    pi.setProcedureNo(procedureNo);
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
        String hostName = getHost(WebRequestProxy.server_ip,WebRequestProxy.server_port);
        String urlStr = hostName + BASE_PATH + GET_CARDLIST;
        String jsonStr = getConntentFromServer(urlStr,null);
        ArrayList<FlowCard> result = new ArrayList<FlowCard> ();
        if(jsonStr.length() > 0)
            result = getFlowCardFromString(jsonStr);
        return result;
    }

    private void updateProcedureInfosToLocal(ArrayList<ProcedureInfo> procedureInfos){

    }

    private void updateProcedureInfosToServer(ArrayList<ProcedureInfo> procedureInfos){

    }

    private String getHost(String ip,int port){
        String host = "http://"+ip+":"+port ;
        return host;
    }

    private String getConntentFromServer(String urlStr,String params ){

        String result = "";
        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            //connection.setConnectTimeout(5*1000);
            connection.setReadTimeout(5*1000);
            if(params != null){
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Charset", "UTF-8");


                OutputStream  out = connection.getOutputStream();
                // 写入请求的字符串
                out.write(params.getBytes());
                out.flush();
                out.close();
                int statusCode = connection.getResponseCode();
            }
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String readLine = "";
            while ((readLine = br.readLine()) != null){
                result += readLine;
            }
            in.close();
            connection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
