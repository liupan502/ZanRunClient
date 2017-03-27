package com.example.liupan.zanrunworkclient;

import android.os.Looper;
import android.os.Message;


import java.io.File;
import java.util.logging.Handler;

import SerialPort.SerialPort;
import SerialPort.SerialPortFinder;
import SerialPort.SerialPortHelper;

/**
 * Created by Panda on 2017/3/20.
 */

public class PortListener implements Runnable {

    public static final int TASK_TYPE = 100;
    public static final int HUNMAN_TYPE = 101;

    private static final String filePath = ""/dev/ttyS2"";
    private static final int brate  = 38400;
    private static  int count = 0;

    private ZanRunSerialPortHelper helper = null;


    private PortListenHandler portListenHandler = null;

    Message obtainNewCardMessage(byte[] content){
        Message msg = null;
        if(portListenHandler == null)
            return msg;
        portListenHandler.removeMessages(0);
        msg = portListenHandler.obtainMessage(TASK_TYPE,0,0,("this is a task"+cout++);       
        return msg;
    }


    void OpenPort(){

    }

    void ClosePort(){
        ;
    }

    public class ZanRunSerialPortHelper extends SerialPortHelper{
        protected void onDataReceived(ComBean ComRecData){
            Message msg = PortListener.this.obtainNewCardMessage(ComRecData.bRec);
            if(msg == null || portListenHandler ==null)
                return;
            msg.sendToTarget();
        }
    }

    static public byte HexToByte(String inHex)//Hex�ַ���תbyte
    {
        return (byte)Integer.parseInt(inHex,16);
    }
    @Override
    public void run()  {

        try{
            helper = new ZanRunSerialPortHelper(filePath,brate);
            helper.setbLoopData(new byte[]{}); 
            helper.startSend();          
        }
        catch(Exception e){

        }
        /*SerialPortFinder finder = new SerialPortFinder();
        String devices[] =  finder.getAllDevices();
        String devicePaths[] = finder.getAllDevicesPath();

        File file = new File(filePath);

        try{
            SerialPort mSerialPort =  new SerialPort(file, brate, 0);

        }
        catch (Exception e){
            int a = 0;
        }*/


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
