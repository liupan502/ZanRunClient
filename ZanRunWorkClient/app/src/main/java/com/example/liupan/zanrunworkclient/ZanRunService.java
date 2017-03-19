package com.example.liupan.zanrunworkclient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by liupan on 2017/3/19.
 */

public class ZanRunService extends Service {


    public static final String TAG = "ZanRunService";
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return zanRunBinder;
    }

    private ZanRunBinder zanRunBinder = new ZanRunBinder();

    class ZanRunBinder extends Binder{
        public void StartListenPort(){
            Log.d(TAG,"start Listen port");
        }
    }
}
