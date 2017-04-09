package Dialog;

import android.app.Dialog;
import android.content.Context;
import com.example.liupan.zanrunworkclient.entity.Employee;

/**
 * Created by liupan on 2017/3/24.
 */

public abstract class BaseConfirmDialog extends Dialog {

    protected Employee employee = null;

    public BaseConfirmDialog(Context context){
        super(context);
    }

    public abstract void SetConfirmButtonStatus(int status,Employee employee);
}
