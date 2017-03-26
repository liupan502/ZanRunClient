package com.example.liupan.zanrunworkclient;

import android.os.Looper;
import android.os.Message;


import java.io.File;
import java.util.logging.Handler;

import SerialPort.SerialPort;
import SerialPort.SerialPortFinder;

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

    static public byte HexToByte(String inHex)//Hex�ַ���תbyte
    {
        return (byte)Integer.parseInt(inHex,16);
    }
    @Override
    public void run()  {

        SerialPortFinder finder = new SerialPortFinder();
        String devices[] =  finder.getAllDevices();
        String devicePaths[] = finder.getAllDevicesPath();

        File file = new File("/dev/ttyS2");

        try{
            SerialPort mSerialPort =  new SerialPort(file, 38400, 0);

        }
        catch (Exception e){
            int a = 0;
        }


       /* Looper mainLooper = Looper.getMainLooper();
        PortListenHandler handler = new PortListenHandler(mainLooper);
        handler.removeMessages(0);
        handler.obtainMessage(TASK_TYPE,"123456");*/
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
