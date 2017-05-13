package com.example.liupan.zanrunworkclient;

import android.os.Looper;
import android.os.Message;
import android.util.Log;


import java.io.File;
import java.util.logging.Handler;

import SerialPort.SerialPort;
import SerialPort.SerialPortFinder;
import SerialPort.SerialHelper;
import SerialPort.*;

/**
 * Created by Panda on 2017/3/20.
 */

public class PortListener implements Runnable {

    public static final int TASK_TYPE = 100;
    public static final int HUNMAN_TYPE = 101;

    private static final String filePath = "/dev/ttyS2";
    private static final int brate  = 38400;
    private static  int count = 0;
    private static String get_id_msg = "7E5509000001001600007877";
    private static String empty_id_msg = "7E 55 0B 01 00 00 00 1F 16 00 01 01 13 6D ";


    private ZanRunSerialPortHelper helper = null;


    private MainActivity.PortListenHandler portListenHandler = null;

    Message obtainNewCardMessage(String rfid){
        Message msg = null;
        if(portListenHandler == null || rfid.isEmpty())
            return msg;
        portListenHandler.removeMessages(0);
        msg = portListenHandler.obtainMessage(TASK_TYPE,0,0,rfid);
        return msg;
    }

    public void setPortListenHandler(MainActivity.PortListenHandler portListenHandler){
        this.portListenHandler = portListenHandler;
    }

    void OpenPort(){

    }

    void ClosePort(){
        ;
    }

    public class ZanRunSerialPortHelper extends SerialHelper{

        public ZanRunSerialPortHelper(String str,int num){
            super(str,num);
        }
        protected void onDataReceived(ComBean ComRecData){
            String result = MyFunc.ByteArrToHex(ComRecData.bRec);
            Log.i("no_tag", result);
            Log.e("no_tag_error",result);
            String rfid = "";
            if(!result.equals(empty_id_msg)){
                /*int a = 0;
                a++;
                char[] tmp = new char[16];

                for(int i=10;i<26;i++){
                    //  = ComRecData.bRec[i];
                    int tmp_int =  Integer.parseInt(MyFunc.Byte2Hex(ComRecData.bRec[i]), 16);
                    tmp[i-10] = (char)tmp_int;
                }
                int length = tmp.length;*/
                String[] tmpStrs = result.split(" ");
                int count = tmpStrs.length;
                if(count == 28){
                    for(int i=13;i<17;i++){
                        rfid = rfid+tmpStrs[i];
                    }
                }
            }
            Message msg = PortListener.this.obtainNewCardMessage(rfid);
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
            Looper mainLooper = Looper.getMainLooper();
            //portListenHandler = new MainActivity.PortListenHandler(mainLooper);
            helper = new ZanRunSerialPortHelper(filePath,brate);
            helper.open();
            helper.setHexLoopData(get_id_msg);
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

    /*private class PortListenHandler extends android.os.Handler{
        public PortListenHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){

        }
    }*/
}
