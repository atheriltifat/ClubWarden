package sixtysixp.clubwarden.staticClass;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Author: Ather Iltifat
 */

public class ProgressBarUtils {

    public static ProgressDialog displayProgressBar(Context context, String msg, Boolean isCanceledOnTouchOutside, Boolean isCancelable){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.show();
        if(isCanceledOnTouchOutside) {
            dialog.setCanceledOnTouchOutside(true);
        }
        else{
            dialog.setCanceledOnTouchOutside(false);
        }
        if(isCancelable){
            dialog.setCancelable(true);
        }
        else{
            dialog.setCancelable(false);
        }
        return dialog;
    }

}
