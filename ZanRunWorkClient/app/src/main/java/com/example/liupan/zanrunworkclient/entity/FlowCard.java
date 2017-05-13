package com.example.liupan.zanrunworkclient.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liupan on 2017/3/8.
 */

public class FlowCard extends BaseObject {

    public FlowCard(){
        super();
        setCardNo("");
        setProductionName("");
        setProductionNo("");
        setMantiNum(0);
        setOrderNum(0);
        //setProductionNo("");
        setProcedureInfos(new ArrayList<ProcedureInfo>());
    }

      // 流程卡编号
      private String cardNo;

      // 产品名称
      private String ProductionName;

      // 产品编号
      private String ProductionNo;

      // 终止日期
      private String terminalDate;

      // 订单数量
      private int orderNum;

      // 已生产数量
      private int mantiNum;

      // 涉及工序
      private ArrayList<ProcedureInfo> procedureInfos;

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    private String rfid;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getProductionName() {
        return ProductionName;
    }

    public void setProductionName(String productionName) {
        ProductionName = productionName;
    }

    public String getProductionNo() {
        return ProductionNo;
    }

    public void setProductionNo(String productionNo) {
        ProductionNo = productionNo;
    }

    public String getTerminalDate() {
        return terminalDate;
    }

    public void setTerminalDate(String terminalDate) {
        this.terminalDate = terminalDate;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getMantiNum() {
        return mantiNum;
    }

    public void setMantiNum(int mantiNum) {
        this.mantiNum = mantiNum;
    }

    public ArrayList<ProcedureInfo> getProcedureInfos() {
        return procedureInfos;
    }

    public void setProcedureInfos(ArrayList<ProcedureInfo> procedureInfos) {
        this.procedureInfos = procedureInfos;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = super.toMap();
        map.put("cardNo",getCardNo());
        map.put("productionName",getProductionName());
        map.put("productionNo",getProductionNo());
        ArrayList<HashMap<String,Object>> procedureInfoMaps = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<procedureInfos.size();i++){
            HashMap<String,Object> procedureInfoMap = procedureInfos.get(i).toMap();
            procedureInfoMaps.add(procedureInfoMap);
        }
        map.put("procedureInfos",procedureInfoMaps);
        map.put("terminalDate",getTerminalDate());
        map.put("orderNum",getOrderNum());
        map.put("mantiNum",getMantiNum());
        return map;
    }

    public void initWithMap(HashMap<String,Object> map){
        super.initWithMap(map);
        if(map.containsKey("cardNo"))
            setCardNo((String)map.get("cardNo"));
        if(map.containsKey("productionName"))
            setProductionName((String)map.get("productionName"));
        if(map.containsKey("productionNo"))
            setProductionNo((String)map.get("productionNo"));
        if(map.containsKey("terminalDate"))
            setTerminalDate((String)map.get("terminalDate"));
        if(map.containsKey("orderNum"))
            setOrderNum((Integer)map.get("orderNum"));
        if(map.containsKey("mantiNum"))
            setMantiNum((int)map.get("mantiNum"));
        if(map.containsKey("procedureInfos")){
            ArrayList<HashMap<String,Object>> procedureInfoMaps =
                    (ArrayList<HashMap<String,Object>>)map.get("procedureInfos");
            procedureInfos.clear();
            for(int i=0;i<procedureInfoMaps.size();i++){
                ProcedureInfo procedureInfo = new ProcedureInfo();
                procedureInfo.initWithMap(procedureInfoMaps.get(i));
                procedureInfos.add(procedureInfo);
            }
        }
    }

    public String getRequest(String procedureId){
        String request = "";
        for(int i=0;i<procedureInfos.size();i++){
            ProcedureInfo procedureInfo = procedureInfos.get(i);
            if(procedureInfo.getId() == procedureId){
                request = procedureInfo.getProcedureRequest();
            }
        }
        return request;
    }
}
