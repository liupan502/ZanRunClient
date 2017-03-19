package com.example.liupan.zanrunworkclient.entity;

import java.util.HashMap;

/**
 * Created by liupan on 2017/3/8.
 */

public class ProcedureInfo extends BaseObject {

    public ProcedureInfo(){
        super();
        setNum(0);
        setProcedureRequest("");
        setQcConfirmStatus(2);
        setFlowCardNo("");
    }

    // 加工要求
    private String procedureRequest;

    // 加工数量
    private  int num;

    public String getFlowCardNo() {
        return flowCardNo;
    }

    public void setFlowCardNo(String flowCardNo) {
        this.flowCardNo = flowCardNo;
    }

    public int getQcConfirmStatus() {
        return qcConfirmStatus;
    }

    public void setQcConfirmStatus(int qcConfirmStatus) {
        this.qcConfirmStatus = qcConfirmStatus;
    }

    // 归属流程卡
    private String flowCardNo;

    private int qcConfirmStatus;

    public String getProcedureRequest() {
        return procedureRequest;
    }

    public void setProcedureRequest(String procedureRequest) {
        this.procedureRequest = procedureRequest;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = super.toMap();
        map.put("qcConfirmStatus",getQcConfirmStatus());
        map.put("procedureRequest",getProcedureRequest());
        map.put("num",getNum());
        return map;
    }

    public void initWithMap(HashMap<String,Object>map){
        super.initWithMap(map);
        if(map.containsKey("qcConfirmStatus"))
            setQcConfirmStatus((Integer)map.get("qcConfirmStatus"));
        if(map.containsKey("procedureRequest"))
            setProcedureRequest((String)map.get("procedureRequest"));
        if(map.containsKey("num"))
            setNum((Integer)map.get("num"));
    }
}
