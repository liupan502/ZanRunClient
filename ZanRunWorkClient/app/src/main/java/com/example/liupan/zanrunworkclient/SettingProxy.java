package com.example.liupan.zanrunworkclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.location.SettingInjectorService;

import com.example.liupan.zanrunworkclient.entity.Procedure;

import java.io.File;
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

    private final String NAME = "ZAN_RUN";

    private final String IP_KEY = "ip";

    private final String PROCEDURE_ID_KEY = "procedure";

    private final String IS_INIT_KEY = "isInit";

    public static SettingProxy getInstance(Context context){
        if(instance == null)
            instance = new SettingProxy(context);
        else
            instance.context = context;
        return instance;
    }

    private static SettingProxy instance = null;

    private Context context = null;

    private SettingProxy(Context context){
        this.context = context;
    }

    private String serverHost ;

    private String  procedureId;

    private String getSettingValue(String key){
        String result = "";

        try{
            SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
            result = sp.getString(key,null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    private void setSetting(String key, String value){
        try{
            SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key,value);
            editor.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {

        }
    }

    public String getServerHost() {
        String serverHost = getSettingValue(IP_KEY);
        return serverHost == null?"":serverHost;
    }

    public void setServerHost(String serverHost) {
        setSetting(IP_KEY,serverHost);
    }

    public String getProcedureId() {
        String procedureId = getSettingValue(PROCEDURE_ID_KEY);
        return procedureId == null?"":procedureId;
    }

    public void setProcedureId(String procedureId) {
        setSetting(PROCEDURE_ID_KEY,procedureId);
    }

    public boolean isInit(){
        boolean result = false;
        try{
            String isInitStr = getSettingValue(IS_INIT_KEY);
            if(isInitStr != null){
                int valInt = Integer.parseInt(isInitStr);
                if(valInt == 0)
                    result = false;
                else
                    result = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void setIsInit(boolean isInit){
        int valInt = isInit?1:0;
        String valStr = String.valueOf(valInt);
        setSetting(IS_INIT_KEY,valStr);
    }

}
