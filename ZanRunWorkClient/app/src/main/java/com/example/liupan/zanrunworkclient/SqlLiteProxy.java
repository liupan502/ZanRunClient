package com.example.liupan.zanrunworkclient;

import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;

import com.example.liupan.zanrunworkclient.entity.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.liupan.zanrunworkclient.ZanRunDBHelper;



/**
 * Created by liupan on 2017/3/9.
 */

public class SqlLiteProxy {

    private static final String EMPLOYEE_TABLE = "employee_table";

    private static final String FLOW_CARD_TABLE = "flowcard_table";

    private static final String PROCEDURE_TABLE = "procedure_table";

    private static final String PROCEDURE_INFO_TABLE = "procedureinfo_table";

    private static final String EMPLOYEE_TASK_TABLE = "employee_task_table";

    public static SqlLiteProxy getInstance(){
        if(instance == null){
            instance = new SqlLiteProxy();
        }
        return instance;
    }

    private static SqlLiteProxy instance = null;

    private ZanRunDBHelper dbHelper = null;

    public void start(ZanRunDBHelper dbHelper){
        if(this.dbHelper == null){
            this.dbHelper = dbHelper;
        }
    }

    public boolean isAvailable(){
        return !(dbHelper == null);
    }

    public void close(){
        if(dbHelper != null){
            dbHelper.close();
            dbHelper = null;
        }
    }


    private Employee getEmployeeFromCursor(Cursor cursor){
        int uidIndex = cursor.getColumnIndexOrThrow("uid");
        int cidIndex = cursor.getColumnIndexOrThrow("cid");
        int noIndex = cursor.getColumnIndexOrThrow("no");
        int levelIndex = cursor.getColumnIndexOrThrow("level");
        int nameIndex = cursor.getColumnIndexOrThrow("name");
        int rfidIndex = cursor.getColumnIndex("rfid");

        String uid = cursor.getString(uidIndex);
        String cid = cursor.getString(cidIndex);
        String no = cursor.getString(noIndex);
        int level = cursor.getInt(levelIndex);
        String name = cursor.getString(nameIndex);
        String rfid = cursor.getString(rfidIndex);

        Employee employee = new Employee();
        employee.setId(uid);
        employee.setCompanyId(cid);
        employee.setEmployeeLevel(level);
        employee.setEmployeeNo(no);
        employee.setName(name);
        employee.setRfid(rfid);

        return employee;
    }

    public ArrayList<Employee> employees(){
        if(!isAvailable()){
            return null;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from employee_table", new String[]{});
        ArrayList<Employee> result = new ArrayList<Employee>();
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                Employee emloyee = getEmployeeFromCursor(cursor);
                result.add(emloyee);
            }
        }
        //db.close();
        return result;
    }

    public Employee findEmployee(String employeeId){
        if(!isAvailable())
            return null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from employee_table where uid = ?", new String[]{employeeId});
        Employee result = null;
        if(cursor.moveToFirst()){
            result = getEmployeeFromCursor(cursor);
        }

        //db.close();
        return result;
    }

    public Employee findEmployeeWithRfid(String rfid){
        if(!isAvailable())
            return null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from employee_table where rfid = ?", new String[]{rfid});
        Employee result = null;
        if(cursor.moveToFirst()){
            result = getEmployeeFromCursor(cursor);
        }

        //db.close();
        return result;
    }

    public boolean updateEmployee(Employee employee){
        if(!isAvailable())
            return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String updateEmployeeSql = "update employee_table set cid = ?,no = ?," +
                "level = ?, name = ?, rfid = ? where uid = ?";
        db.execSQL(updateEmployeeSql,new Object[]{employee.getCompanyId(),
        employee.getEmployeeNo(),employee.getEmployeeLevel(),employee.getName(),employee.getRfid(),employee.getId()});
        //db.close();
        return true;
    }

    public boolean insertEmployee(Employee employee){
        if(!isAvailable())
            return false;
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String insertEmployeeSql = "insert into employee_table (uid,cid,no,level,name,rfid) values(?,?,?,?,?,?)";
            db.execSQL(insertEmployeeSql,new Object[]{employee.getId(),employee.getCompanyId(),
                    employee.getEmployeeNo(),employee.getEmployeeLevel(),employee.getName(),employee.getRfid()});
            //db.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public boolean deleteEmployee(Employee employee){
        if(!isAvailable())
            return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteEmployeeSql = "delete from employee_table where uid = ?";
        db.execSQL(deleteEmployeeSql,new Object[]{employee.getId()});
        //db.close();
        return true;
    }

    private ProcedureInfo getProcedureInfoFromCursor(Cursor cursor){
        ProcedureInfo procedureInfo = new ProcedureInfo();
        int uidIndex = cursor.getColumnIndexOrThrow("uid");
        int cidIndex = cursor.getColumnIndexOrThrow("cid");
        int fcidIndex = cursor.getColumnIndex("fcid");
        int qstatusIndex = cursor.getColumnIndexOrThrow("qstatus");
        int numIndex = cursor.getColumnIndexOrThrow("num");
        int reqIndex = cursor.getColumnIndexOrThrow("req");

        String uid = cursor.getString(uidIndex);
        String cid = cursor.getString(cidIndex);
        String fcid = cursor.getString(fcidIndex);
        int qstatus = cursor.getInt(qstatusIndex);
        int num = cursor.getInt(numIndex);
        String req = cursor.getString(reqIndex);

        procedureInfo.setId(uid);
        procedureInfo.setCompanyId(cid);
        procedureInfo.setFlowCardNo(fcid);
        procedureInfo.setNum(num);
        procedureInfo.setQcConfirmStatus(qstatus);
        procedureInfo.setProcedureRequest(req);

        return procedureInfo;
    }

    private Procedure getProcedureFromCursor(Cursor cursor){
        Procedure procedure = new Procedure();

        int uidIndex = cursor.getColumnIndexOrThrow("uid");
        int cidIndex = cursor.getColumnIndexOrThrow("cid");
        int nameIndex = cursor.getColumnIndexOrThrow("name");
        int noIndex = cursor.getColumnIndexOrThrow("no");

        String uid = cursor.getString(uidIndex);
        String cid = cursor.getString(cidIndex);
        String name = cursor.getString(nameIndex);
        String no = cursor.getString(noIndex);

        procedure.setId(uid);
        procedure.setCompanyId(cid);
        procedure.setProcedureNo(no);
        procedure.setProcedureName(name);
        return procedure;
    }

    private FlowCard getFlowCardFromCursor(Cursor cursor){
        FlowCard flowCard = new FlowCard();

        int uidIndex = cursor.getColumnIndexOrThrow("uid");
        int cidIndex = cursor.getColumnIndexOrThrow("cid");
        int noIndex = cursor.getColumnIndexOrThrow("no");
        int pnameIndex = cursor.getColumnIndexOrThrow("pname");
        int pnoIndex = cursor.getColumnIndexOrThrow("pno");
        int mnumIndex = cursor.getColumnIndexOrThrow("mnum");
        int onumIndex = cursor.getColumnIndexOrThrow("onum");
        int tdateIndex = cursor.getColumnIndexOrThrow("tdate");
        int rfidIndex = cursor.getColumnIndexOrThrow("rfid");

        String uid = cursor.getString(uidIndex);
        String cid = cursor.getString(cidIndex);
        String no = cursor.getString(noIndex);
        String pname = cursor.getString(pnameIndex);
        String pno = cursor.getString(pnoIndex);
        int mnum = cursor.getInt(mnumIndex);
        int onum = cursor.getInt(onumIndex);
        String tdate = cursor.getString(tdateIndex);
        String rfid = cursor.getString(rfidIndex);

        flowCard.setId(uid);
        flowCard.setCompanyId(cid);
        flowCard.setCardNo(no);
        flowCard.setProductionNo(pno);
        flowCard.setOrderNum(onum);
        flowCard.setMantiNum(mnum);
        flowCard.setProductionName(pname);
        flowCard.setTerminalDate(tdate);
        flowCard.setRfid(rfid);

        return flowCard;
    }

    public ArrayList<Procedure> procedures(){
        if(!isAvailable()){
            return null;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from procedure_table";
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<Procedure> result = new ArrayList<Procedure>();
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                Procedure procedure = getProcedureFromCursor(cursor);
                result.add(procedure);
            }
        }
        //db.close();
        return result;
    }

    public Procedure findProcedure(String procedureId){
        if(!isAvailable())
            return null;
        String sql = "select * from procedure_table where uid = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{procedureId});
        Procedure result = null;
        if(cursor.moveToFirst()){
            result = getProcedureFromCursor(cursor);
        }
        //db.close();
        return result;
    }

    public boolean updateProcedure(Procedure procedure){
        if(!isAvailable())
            return false;
        String sql = "update procedure_table set cid = ?,name = ?,no = ? where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{procedure.getCompanyId(),procedure.getProcedureName(),
                procedure.getProcedureNo(),procedure.getId()});
        //db.close();
        return true;
    }

    public boolean insertProcedure(Procedure procedure){
        if(!isAvailable())
            return false;
        String sql = "insert into procedure_table (uid,cid,name,no) values(?,?,?,?)";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{procedure.getId(),procedure.getCompanyId(),
        procedure.getProcedureName(),procedure.getProcedureNo()});
        //db.close();
        return true;
    }

    public boolean deleteProcedure(String  procedureId){
        if(!isAvailable())
            return false;
        String sql = "delete from procedure_table where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{procedureId});
        //db.close();
        return true;
    }

    public ArrayList<FlowCard> flowCards(){
        if(!isAvailable())
            return null;
        String sql = "select * from flowcard_table";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<FlowCard> result = new ArrayList<FlowCard>();
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                FlowCard flowCard = getFlowCardFromCursor(cursor);
                result.add(flowCard);
            }
        }
        //db.close();
        return result;
    }

    public FlowCard findFlowCard(String flowCardId){
        if(!isAvailable())
            return null;
        String sql = "select * from flowcard_table where uid = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{flowCardId});
        FlowCard result = null;
        if(cursor.moveToFirst()){
            result = getFlowCardFromCursor(cursor);
        }
        //db.close();
        return result;
    }

    public FlowCard findFlowCardWithRfid(String rfid){
        if(!isAvailable())
            return null;
        String sql = "select * from flowcard_table where rfid = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{rfid});
        FlowCard result = null;
        if(cursor.moveToFirst()){
            result = getFlowCardFromCursor(cursor);
        }
        //db.close();
        return result;
    }

    public boolean updateFlowCard(FlowCard flowCard){
        if(!isAvailable())
            return false;
        String sql = "update flowcard_table set cid = ?, no = ?,pname = ?, pno = ?,mnum = ?," +
                " onum = ?, tdate = ?, rfid = ?  where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{flowCard.getCompanyId(),flowCard.getCardNo(),flowCard.getProductionName(),
                flowCard.getProductionNo(),flowCard.getMantiNum(),flowCard.getOrderNum(),flowCard.getTerminalDate(),
                flowCard.getRfid(),flowCard.getId()});
        //db.close();
        return true;
    }

    public boolean insertFlowCard(FlowCard flowCard){
        if(!isAvailable())
            return false;
        String sql = "insert into flowcard_table (uid,cid,no,pname,pno,mnum,onum,tdate,rfid) values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{flowCard.getId(),flowCard.getCompanyId(),flowCard.getCardNo(),flowCard.getProductionName(),
        flowCard.getProductionNo(),flowCard.getMantiNum(),flowCard.getOrderNum(),flowCard.getTerminalDate(),flowCard.getRfid()});
        //db.close();
        return true;
    }

    public boolean deleteFlowCard(String flowCardId){
        if(!isAvailable())
            return false;
        String sql = "delete from flowcard_table where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{flowCardId});
        //db.close();
        return true;
    }

    public ArrayList<ProcedureInfo> procedureInfos(String flowCardId){
        if(!isAvailable())
            return null;
        String sql = "select * from procedureinfo_table where fcid = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{flowCardId});
        ArrayList<ProcedureInfo> result = new ArrayList<ProcedureInfo>();
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                ProcedureInfo procedureInfo = getProcedureInfoFromCursor(cursor);
                result.add(procedureInfo);
            }
        }
        //db.close();
        return result;
    }

    public ProcedureInfo findProcedureInfo(String procedureInfoId){
        if(!isAvailable())
            return null;
        String sql = "select * from procedureinfo_table where uid = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ProcedureInfo result = null;
        Cursor cursor = db.rawQuery(sql,new String[]{procedureInfoId});
        if(cursor.moveToFirst()){
            result = getProcedureInfoFromCursor(cursor);
        }
        //db.close();
        return result;
    }

    public boolean updateProcedureInfo(ProcedureInfo pi){
        if(!isAvailable())
            return false;
        String sql = "update procedureinfo_table set cid = ?, fcid = ?,qstatus = ?,num = ?, req = ? where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{pi.getCompanyId(),pi.getFlowCardNo(),pi.getQcConfirmStatus(),
                pi.getNum(),pi.getProcedureRequest(),pi.getId()});
        //db.close();
        return true;
    }

    public boolean deleteProcedureInfo(String procedureInfoId){
        if(!isAvailable())
            return false;
        String sql = "delete from procedureinfo_table where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{procedureInfoId});
        //db.close();
        return true;
    }

    public boolean insertProcedureInfo(ProcedureInfo pi){
        if(!isAvailable())
            return false;
        String sql = "insert into procedureinfo_table (uid,cid,fcid,qstatus,num,req) values(?,?,?,?,?,?)";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{pi.getId(),pi.getCompanyId(),pi.getFlowCardNo(),
                pi.getQcConfirmStatus(),pi.getNum(),pi.getProcedureRequest()});
        //db.close();
        return true;
    }

    EmployeeTask getEmployeeTaskFromCursor(Cursor cursor){
        EmployeeTask employeeTask = new EmployeeTask();

        int uidIndex = cursor.getColumnIndexOrThrow("uid");
        int cidIndex = cursor.getColumnIndexOrThrow("cid");
        int employeeNameIndex = cursor.getColumnIndexOrThrow("ename");
        int employeeIdIndex = cursor.getColumnIndexOrThrow("eid");
        int taskIdIndex = cursor.getColumnIndexOrThrow("fid");
        int statusIndex = cursor.getColumnIndexOrThrow("status");
        int productionNum = cursor.getColumnIndexOrThrow("pnum");
        int badProductionNum = cursor.getColumnIndexOrThrow("bpnum");
        int procedureIdIndex = cursor.getColumnIndexOrThrow("pid");
        int managerIdIndex = cursor.getColumnIndexOrThrow("mid");
        int qcIdindex = cursor.getColumnIndexOrThrow("qcid");
        int btimeIndex = cursor.getColumnIndexOrThrow("btime");
        int etimeIndex = cursor.getColumnIndexOrThrow("utime");

        String uid = cursor.getString(uidIndex);
        String cid = cursor.getString(cidIndex);
        String ename = cursor.getString(employeeNameIndex);
        String eid = cursor.getString(employeeIdIndex);
        String tid = cursor.getString(taskIdIndex);
        String pid = cursor.getString(procedureIdIndex);
        int status = cursor.getInt(statusIndex);
        int pnum = cursor.getInt(productionNum);
        int bpnum = cursor.getInt(badProductionNum);
        String mid = cursor.getString(managerIdIndex);
        String qcid = cursor.getString(qcIdindex);
        String btime = cursor.getString(btimeIndex);
        String etime = cursor.getString(etimeIndex);


        employeeTask.setId(uid);
        employeeTask.setCompanyId(cid);
        employeeTask.setProductionNum(pnum);
        employeeTask.setBadProductionNum(bpnum);
        employeeTask.setStatus(status);
        employeeTask.setEmployeeName(ename);
        employeeTask.setEmployeeId(eid);
        employeeTask.setFcId(tid);
        employeeTask.setProcedureId(pid);
        employeeTask.setManagerId(mid);
        employeeTask.setQCId(qcid);
        employeeTask.setStartTime(btime);
        employeeTask.setUpdateTime(etime);
        
        return employeeTask;
    }

    public ArrayList<EmployeeTask> employeeTasks(String flowCardId){
        if(!isAvailable())
            return null;
        String sql = "select * from employee_task_table where fid = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{flowCardId});
        ArrayList<EmployeeTask> result = new ArrayList<EmployeeTask>();
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                try{
                    EmployeeTask employeeTask = getEmployeeTaskFromCursor(cursor);
                    result.add(employeeTask);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        //db.close();
        return result;
    }

    public ArrayList<EmployeeTask> finishedEmployeeTasks(){
        if(!isAvailable())
            return null;
        String sql = "select * from employee_task_table where  status = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{"3"});
        ArrayList<EmployeeTask> result = new ArrayList<EmployeeTask>();
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                EmployeeTask employeeTask = getEmployeeTaskFromCursor(cursor);
                result.add(employeeTask);
            }
        }
        return result;
    }

    public EmployeeTask findEmployeeTask(String employeeTaskId){
        if(!isAvailable())
            return null;
        String sql = "select * from employee_task_table where uid = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        EmployeeTask result = null;
        Cursor cursor = db.rawQuery(sql,new String[]{employeeTaskId});
        if(cursor.moveToFirst()){
            result = getEmployeeTaskFromCursor(cursor);
        }
        //db.close();
        return result;
    }

    public boolean updateEmployeeTask(EmployeeTask et){
        if(!isAvailable())
            return false;
        String sql = "update employee_task_table set cid = ?, ename = ?,eid = ?,"+
        "fid = ?, pid = ?,status = ?, pnum = ?, bpnum = ? ,mid = ? ,qcid = ?,utime = ?  where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{et.getCompanyId(),
            et.getEmployeeName(),
            et.getEmployeeId(),
            et.getFcId(),
            et.getProcedureId(),
            et.getStatus(),
            et.getProductionNum(),
            et.getBadProductionNum(),
            et.getManagerId(),
            et.getQCId(),
            getCurrentTime(),
            et.getId()});
        //db.close();
        return true;
    }

    public boolean deleteEmployeeTask(String employeeTaskId){
        if(!isAvailable())
            return false;
        String sql = "delete from employee_task_table where uid = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{employeeTaskId});
        //db.close();
        return true;
    }

    public boolean insertEmployeeTask(EmployeeTask et){
        if(!isAvailable())
            return false;
        String sql = "insert into employee_task_table (uid,cid,ename,eid,fid,pid,status,pnum,bpnum,mid,qcid,btime,utime) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql,new Object[]{et.getId(),
            et.getCompanyId(),
            et.getEmployeeName(),
            et.getEmployeeId(),
            et.getFcId(),
            et.getProcedureId(),
            et.getStatus(),
            et.getProductionNum(),
            et.getBadProductionNum(),
            et.getManagerId(),
            et.getQCId(),
            getCurrentTime(),
            getCurrentTime()
        });
        //db.close();
        return true;
    }

    // YYYY-MM-DD HH:MM:SS
    public String getCurrentTime(){
        String currentTime = "";
        try{
            SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            currentTime    =    sDateFormat.format(new    java.util.Date());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return currentTime;
    }
}
