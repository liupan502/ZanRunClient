package com.example.liupan.zanrunworkclient.entity;

import java.util.HashMap;

/**
 * Created by liupan on 2017/3/8.
 */

public class Procedure extends BaseObject {

    public Procedure(){
        super();
        setProcedureName("");
        setProcedureNo("");
    }

    // 工序编号
    private String procedureNo;

    // 工序名称
    private String procedureName;

    public String getProcedureNo() {
        return procedureNo;
    }

    public void setProcedureNo(String procedureNo) {
        this.procedureNo = procedureNo;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = super.toMap();
        map.put("procedureNo",getProcedureNo());
        map.put("procedureName",getProcedureName());
        return map;
    }

    public void initWithMap(HashMap<String,Object> map){
        super.initWithMap(map);
        if(map.containsKey("procedureNo"))
            setProcedureNo((String)map.get("procedureNo"));
        if(map.containsKey("procedureName"))
            setProcedureNo((String)map.get("procedureName"));
    }
}
