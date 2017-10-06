package sixtysixp.clubwarden.staticClass;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by hassan on 7/22/2017.
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


/*    public static ProgressDialog displayFixedProgressBar123(Context context){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }*/

}
