package com.example.liupan.zanrunworkclient;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by liupan on 2017/3/22.
 */

public class ConfirmDialog extends Dialog {

    private String title ;
    private String confirmButtonText;
    private String cancelButtonText;
    private ClickListenerInterFace clickListenerInterFace;


    public ConfirmDialog(Context context){
        super(context);
    }

    public ConfirmDialog(Context context,int theme){
        super(context,theme);
    }

    public ConfirmDialog(Context context,String title,
                         String confirmText,String cancelText){
        super(context);
        this.title = title;
        this.confirmButtonText = confirmText;
        this.cancelButtonText = cancelText;
    }

    public interface  ClickListenerInterFace{
        public void DoConfirm();

        public void DoCancel();
    }

    @Override
    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    protected void init(){

    }

}
