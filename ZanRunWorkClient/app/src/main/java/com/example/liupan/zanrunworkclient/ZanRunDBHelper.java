package com.example.liupan.zanrunworkclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by liupan on 2017/3/10.
 */

public class ZanRunDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "ZanRunSQLite";

    public ZanRunDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ZanRunDBHelper(Context context){
        super(context,"zanrun.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createEmployeeSql = "create table employee_table(uid varchar(128) , cid varchar(128)," +
                " rfid varchar(128), no varchar(128),level int, name varchar(128))";
        db.execSQL(createEmployeeSql);

        String createFlowCardSql = "create table flowcard_table(uid varchar(128) ,cid varchar(128)," +
                " rfid varchar(128), no varchar(128),pname varchar(128),pno varchar(128),mnum int,onum int,tdate varchar(128))";
        db.execSQL(createFlowCardSql);

        String createProcedureInfoSql = "create table procedureinfo_table(uid varchar(128) ,cid varchar(128)," +
                "fcid varchar(128),qstatus int,num int,req varchar(2048))";
        db.execSQL(createProcedureInfoSql);

        String createProcedureSql = "create table procedure_table(uid varchar(128) ,cid varchar(128)," +
                "name varchar(128),no varchar(128))";
        db.execSQL(createProcedureSql);

        String createEmployeeTaskSql = "create table employee_task_table(uid varchar(128) ,cid varchar(128)," +
                "ename varchar(128),eid varchar(128),fid varchar(128) ,pid varchar(128), mid varchar(128), " +
                "qcid varchar(128),status int,pnum int, bpnum int,btime varchar(128)," +
                "utime varchar(128))";
        db.execSQL(createEmployeeTaskSql);

        Log.i(TAG,"create db");
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"upgrade db");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
