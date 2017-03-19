package com.example.liupan.zanrunworkclient.entity;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by liupan on 2017/3/8.
 */

public  class BaseObject extends Object {
    public BaseObject() {
        super();
        setId("");
        setCompanyId("");
    }

    // 公司id
    private  String companyId;

    // id
    private  String id;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  HashMap<String,Object> toMap(){
        HashMap map = new HashMap<String,Object>();
        map.put("id",getId());
        map.put("companyId",getCompanyId());
        return map;
    }

    public  void initWithMap(HashMap<String,Object> map){
        if(map.containsKey("id"))
            setId((String)map.get("id"));
        if(map.containsKey("companyKey"))
            setCompanyId((String)map.get("companyId"));
    }


}
