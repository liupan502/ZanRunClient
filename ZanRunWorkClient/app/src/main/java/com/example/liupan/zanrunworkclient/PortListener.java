package com.example.liupan.zanrunworkclient;

import android.os.Looper;
import android.os.Message;

import java.util.logging.Handler;

/**
 * Created by Panda on 2017/3/20.
 */

public class PortListener implements Runnable {

    public static final int TASK_TYPE = 100;
    public static final int HUNMAN_TYPE = 101;
    void OpenPort(){

    }

    void ClosePort(){
        ;
    }
    @Override
    public void run() {
        Looper mainLooper = Looper.getMainLooper();
        PortListenHandler handler = new PortListenHandler(mainLooper);
        handler.removeMessages(0);
        handler.obtainMessage(TASK_TYPE,"123456");
    }

    private class PortListenHandler extends android.os.Handler{
        public PortListenHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){

        }
    }
}
