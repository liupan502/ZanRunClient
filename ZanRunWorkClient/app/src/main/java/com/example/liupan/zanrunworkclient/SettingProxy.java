package com.example.liupan.zanrunworkclient;

import android.location.SettingInjectorService;

import com.example.liupan.zanrunworkclient.entity.Procedure;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by liupan on 2017/3/11.
 */

public class SettingProxy {

    private final String SETTING_FILE_PATH = "/assets/ZanRun.properties";

    private final String IP_KEY = "ip";

    private final String PROCEDURE_ID_KEY = "procedure";

    public static SettingProxy getInstance(){
        if(instance == null)
            instance = new SettingProxy();
        return instance;
    }

    private static SettingProxy instance = null;

    private SettingProxy(){
        ;
    }

    private String serverHost ;

    private String  procedureId;

    private String getSettingValue(String key){
        String result = "";

        try{
            String tmp = System.getProperty(key);
            if(tmp != null)
                result = tmp;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return result;
    }

    private void setSetting(String key, String value){
        try{
            System.setProperty(key,value);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {

        }
    }

    public String getServerHost() {
        return getSettingValue(IP_KEY);
    }

    public void setServerHost(String serverHost) {
        setSetting(IP_KEY,serverHost);
    }

    public String getProcedureId() {
        return getSettingValue(PROCEDURE_ID_KEY);
    }

    public void setProcedureId(String procedureId) {
        setSetting(PROCEDURE_ID_KEY,procedureId);
    }




}
