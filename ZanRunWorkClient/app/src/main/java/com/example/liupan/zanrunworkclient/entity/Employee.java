package com.example.liupan.zanrunworkclient.entity;

import java.util.HashMap;

/**
 * Created by liupan on 2017/3/8.
 */

public class Employee extends BaseObject {
    public Employee(){
        super();
        setName("");
        setEmployeeNo("");
        setEmployeeLevel(-1);
    }
    public static final int UNKOWN_EMPLOYEE = 0;

    public static final int GENERAL_EMPLOYEE = 1;

    public static final int QC_EMPLOYEE = 2;

    public static final int MANAGER_EMPLOYEE = 3;

    // 雇员编号
    private  String employeeNo;

    // 雇员级别
    private int employeeLevel;

    // 雇员姓名
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmployeeLevel() {
        return employeeLevel;
    }

    public void setEmployeeLevel(int employeeLevel) {
        this.employeeLevel = employeeLevel;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = super.toMap();
        map.put("name",getName());
        map.put("employeeNo",getEmployeeNo());
        map.put("employeeLevel",getEmployeeLevel());
        return map;
    }

    public void initWithMap(HashMap<String,Object> map){
        super.initWithMap(map);
        if(map.containsKey("name"))
            setName((String)map.get("name"));

        if(map.containsKey("employeeNo"))
            setEmployeeNo((String)map.get("employeeNo"));

        if(map.containsKey("employeeLevel"))
            setEmployeeLevel((Integer)map.get("employeeLevel"));
    }



}
