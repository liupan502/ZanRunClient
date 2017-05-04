package Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.liupan.zanrunworkclient.R;
import com.example.liupan.zanrunworkclient.SettingProxy;
import com.example.liupan.zanrunworkclient.SqlLiteProxy;
import com.example.liupan.zanrunworkclient.entity.Employee;
import com.example.liupan.zanrunworkclient.entity.Procedure;

import java.util.ArrayList;

/**
 * Created by liupan on 2017/3/24.
 */

public class SettingConfirmDialog extends BaseConfirmDialog  implements  AdapterView.OnItemSelectedListener{

    private Context context;

    private TextView ipText = null;

    private TextView procedureText = null;

    private ArrayAdapter<String> adapter = null;

    ArrayList<String> procedureNames = new ArrayList<String>();

    ArrayList<Procedure> procedures = new ArrayList<Procedure>();

    Procedure currentProcedure = null;

    public SettingConfirmDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void SetConfirmButtonStatus(int status, Employee employee) {
        boolean enanbled = status == 0?false:true;
        Button ensureButton = (Button) findViewById(R.id.SettingEnsureButton);
        this.employee = employee;
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
                    //if(ipText == null || currentProcedure == null)
                    //    return;
                    String type = currentProcedure == null?"":currentProcedure.getId();
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

        SettingProxy settingProxy = SettingProxy.getInstance();
        String procedureId = settingProxy.getProcedureId();

        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        procedures = sqlLiteProxy.procedures();
        procedureNames.clear();
        int selectedIndex = -1;
        for(int i=0;i<procedures.size();i++){
            Procedure procedure = procedures.get(i);
            if(procedureId == procedure.getId()){
                currentProcedure = procedure;
                selectedIndex = i;
            }
            procedureNames.add(procedure.getProcedureName());
        }
        Spinner spinner = (Spinner) findViewById(R.id.clientTypeSpinner);
        String[] spinnerContent = {"clientType1","clientType2","clientType3"};
        ArrayList<String> spinnerContentList = new ArrayList<>();
        //spinnerContentList.add("clientType4");
        //spinnerContentList.add("clientType5");
        adapter= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,procedureNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(currentProcedure != null)
            spinner.setSelection(selectedIndex,false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String procedureName = (String) adapter.getItem(position);
        for(int i=0;i<procedures.size();i++){
            if(procedures.get(i).getProcedureName() == procedureName){
                currentProcedure = procedures.get(i);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
