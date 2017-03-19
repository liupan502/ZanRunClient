package com.example.liupan.zanrunworkclient.entity;

import java.util.HashMap;

/**
 * Created by liupan on 2017/3/11.
 */

public class Task extends BaseObject {
    public Task(){
        super();
    }

    private String flowCardId;

    private String employeeId;

    private String procedureId;

    private String statrTime;

    private String endTime;

    private int status;

    public String getFlowCardId() {
        return flowCardId;
    }

    public void setFlowCardId(String flowCardId) {
        this.flowCardId = flowCardId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getStatrTime() {
        return statrTime;
    }

    public void setStatrTime(String statrTime) {
        this.statrTime = statrTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = super.toMap();
        map.put("flowCardId",getFlowCardId());
        map.put("employeeId",getEmployeeId());
        map.put("procedureId",getProcedureId());
        map.put("statrTime",getStatrTime());
        map.put("endTime",getEndTime());
        map.put("status",getStatus());
        return map;
    }

    public void initWithMap(HashMap<String,Object> map){
        super.initWithMap(map);
        if(map.containsKey("flowCardId"))
            setFlowCardId((String)map.get("flowCardId"));
        if(map.containsKey("employeeId"))
            setEmployeeId((String)map.get("employeeId"));
        if(map.containsKey("procedureId"))
            setProcedureId((String)map.get("procedureId"));
        if(map.containsKey("statrTime"))
            setStatrTime((String)map.get("statrTime"));
        if(map.containsKey("endTime"))
            setEndTime((String)map.get("endTime"));
        if(map.containsKey("status"))
            setStatus((Integer)map.get("status"));

    }



}
