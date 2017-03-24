package Dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by liupan on 2017/3/24.
 */

public abstract class BaseConfirmDialog extends Dialog {

    public BaseConfirmDialog(Context context){
        super(context);
    }

    public abstract void SetConfirmButtonStatus(int status);
}
