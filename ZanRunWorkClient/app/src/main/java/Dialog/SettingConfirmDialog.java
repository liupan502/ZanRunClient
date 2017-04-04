package Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.liupan.zanrunworkclient.R;

/**
 * Created by liupan on 2017/3/24.
 */

public class SettingConfirmDialog extends BaseConfirmDialog {

    private Context context;

    private TextView ipText = null;

    private TextView procedureText = null;
    public SettingConfirmDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void SetConfirmButtonStatus(int status) {
        boolean enanbled = status == 0?false:true;
        Button ensureButton = (Button) findViewById(R.id.SettingEnsureButton);
        ensureButton.setEnabled(enanbled);
    }

    public ClickListenerInterFace clif = null;

    public interface  ClickListenerInterFace{
        public void DoConfirm(String type,String Ip,Dialog dialog);

        public void DoCancel(Dialog dialog);
    }

    @Override
    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_setting_confirm ,null);
        setContentView(view);
        Button ensureButton = (Button) findViewById(R.id.SettingEnsureButton);
        ensureButton.setEnabled(false);
        ensureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clif != null){
                    if(ipText == null || procedureText == null)
                        return;
                    String type = procedureText.getText().toString();
                    String ip = ipText.getText().toString();
                    clif.DoConfirm(type,ip,SettingConfirmDialog.this);
                }
            }
        });
        Button cancelButton = (Button)findViewById(R.id.SettingCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clif != null){
                    clif.DoCancel(SettingConfirmDialog.this);
                }
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
}
