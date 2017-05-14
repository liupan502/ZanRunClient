package Dialog;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liupan.zanrunworkclient.R;
import com.example.liupan.zanrunworkclient.entity.Employee;

/**
 * Created by liupan on 2017/3/24.
 */

public class ManagerConfirmDialog extends BaseConfirmDialog {
    private  Context context;

    private String employeeTaskId = null;


    private TextView badProNumText = null;

    private TextView proNumText = null;

    //private Employee manager = null;

    public ManagerConfirmDialog(Context context,String employeeTaskId) {
        super(context);
        this.context = context;
        this.employeeTaskId = employeeTaskId;

    }

    @Override
    public void SetConfirmButtonStatus(int status, Employee employee) {
        boolean enanbled = status == 0?false:true;
        Button ensureButton = (Button) findViewById(R.id.ManagerEnsureButton);
        this.employee = employee;
        ensureButton.setEnabled(enanbled);
    }

    public ClickListenerInterFace clif = null;

    public interface  ClickListenerInterFace{

        public void DoConfirm(int proNum,int badProNum,String employeeTaskId,Dialog dialog,Employee manager);

        public void DoCancel(Dialog dialog);
    }

    @Override
    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_manager_confirm ,null);
        setContentView(view);
        Button ensureButton = (Button) findViewById(R.id.ManagerEnsureButton);
        ensureButton.setEnabled(false);
        ensureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clif != null){
                    if(proNumText == null || badProNumText == null)
                        return;
                    int badProNum = -1,proNum = -1;
                    try{
                        badProNum = Integer.parseInt(badProNumText.getText().toString());
                        proNum = Integer.parseInt(proNumText.getText().toString());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    clif.DoConfirm(proNum,badProNum,employeeTaskId,ManagerConfirmDialog.this,employee);
                }
            }
        });
        Button cancelButton = (Button)findViewById(R.id.ManagerCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clif != null){
                    clif.DoCancel(ManagerConfirmDialog.this);
                }
            }
        });

        proNumText = (EditText) findViewById(R.id.ProductionEdit);
        badProNumText = (EditText) findViewById(R.id.BadProductionEdit);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.4); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

    }
}
