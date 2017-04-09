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

import com.example.liupan.zanrunworkclient.R;
import com.example.liupan.zanrunworkclient.entity.Employee;

/**
 * Created by liupan on 2017/3/24.
 */

public class QCConfirmDialog extends BaseConfirmDialog {

    private  Context context;

    private String employeeTaskId = null;

    //private Employee qc = null;
    public QCConfirmDialog(Context context,String employeeTaskId ) {
        super(context);
        this.context = context;
        this.employeeTaskId = employeeTaskId; 

    }

    @Override
    public void SetConfirmButtonStatus(int status,Employee employee) {

        boolean enanbled = status == 0?false:true;
        Button ensureButton = (Button) findViewById(R.id.QCEnsureButton);
        this.employee = employee;
        ensureButton.setEnabled(enanbled);
    }

    public ClickListenerInterFace clif = null;

    @Override
    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_task_start_confirm ,null);
        setContentView(view);
        Button ensureButton = (Button) findViewById(R.id.QCEnsureButton);
        ensureButton.setEnabled(false);
        ensureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clif != null){
                    clif.DoConfirm(employeeTaskId,QCConfirmDialog.this,employee);
                }
            }
        });
        Button cancelButton = (Button)findViewById(R.id.QCCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clif != null){
                    clif.DoCancel(QCConfirmDialog.this);
                }
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public interface  ClickListenerInterFace{
        public void DoConfirm(String employeeTaskId,Dialog dialog,Employee qc);

        public void DoCancel(Dialog dialog);
    }
}
