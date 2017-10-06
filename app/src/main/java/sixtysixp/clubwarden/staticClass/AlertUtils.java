package sixtysixp.clubwarden.staticClass;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import sixtysixp.clubwarden.LoginActivity;
import sixtysixp.clubwarden.MainActivity;
import sixtysixp.clubwarden.R;

/**
 * Created by hassan on 7/22/2017.
 */

public class AlertUtils {
    public static AlertDialog alert;
    ////////////////////////////////////////// APIClient ///////////////////////////////////////
    public static void displayAlertUnfixed(String title,String msg){
        AlertDialog.Builder builder;
        //builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder = new AlertDialog.Builder(LoginActivity.getLoginInstance(), R.style.MyDialogTheme);
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }*/
        builder.setTitle(title)
                .setMessage(msg).setView(R.layout.dialog_divider)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                });
/*                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })*/
        //.setIcon(android.R.drawable.ic_dialog_alert)
        //.show();
        alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
        //alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.WHITE);
    }

    public static void displayAlert2LoginAct(String title,String msg){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.getLoginInstance(), R.style.MyDialogTheme);
        builder.setTitle(title)
                .setMessage(msg).setView(R.layout.dialog_divider)
                // TRY AGAIN
                .setPositiveButton("VERSUCH NOCHMAL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.getLoginInstance().getUserIdFromDevice();
                    }
                })
                //QUIT
                .setNegativeButton("BEENDEN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.getLoginInstance().finish();
                    }
                });
        //.setIcon(android.R.drawable.ic_dialog_alert)
        //.show();
        alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
        nbutton.setBackgroundColor(Color.parseColor("#1B5E20"));
        //alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,20,0);
        nbutton.setLayoutParams(params);
    }

    public static void displayAlert3LoginAct(String title,String msg){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.getLoginInstance(), R.style.MyDialogTheme);
        builder.setTitle(title)
                .setMessage(msg).setView(R.layout.dialog_divider)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.getLoginInstance().finish();
                    }
                });
        alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
    }

    public static void displayAlert4LoginAct(String title){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.getLoginInstance(), R.style.MyDialogTheme);
        builder.setTitle(title)
                .setView(R.layout.dialog_divider)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.getLoginInstance().focusPasswordField();
                    }
                });
        alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
    }
    public static void displayAlert5LoginAct(String msg){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.getLoginInstance(), R.style.MyDialogTheme);
        builder.setMessage(msg)
                .setView(R.layout.dialog_divider)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
    }
    //////////////////////////////////////////////////// Main activity /////////////////////////////

    public static void displayAlert1MainAct(Context context, String title, String msg){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(title)
                .setMessage(msg).setView(R.layout.dialog_divider)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                });

/*                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })*/
        alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
        //alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.WHITE);
    }

    public static void displayAlertForBookingMainAct(String title, String msg, final int selector,final TextView tv,
                                                     final int courtNumber, final String timeSlot){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getMainInstance(), R.style.MyDialogTheme);
        builder.setTitle(title).setMessage(msg).setView(R.layout.dialog_divider)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(selector == 1){
                            MainActivity.getMainInstance().bookCourt(courtNumber, timeSlot);
                        }
                        else if(selector == 2){
                            MainActivity.getMainInstance().unbookCourtByCoach(tv);
                        }
                        else{
                            MainActivity.getMainInstance().unbookCourtByUser(tv);
                        }
                    }
                })
                // CANCEL
                .setNegativeButton("ABBRUCH", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setBackgroundColor(Color.parseColor("#1B5E20"));
        //alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,20,0);
        nbutton.setLayoutParams(params);
    }

    public static void displayAlertForRegTblMainAct(String title,String msg){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.getMainInstance(), R.style.MyDialogTheme);
        builder.setTitle(title)
                .setMessage(msg).setView(R.layout.dialog_divider)
                // TRY AGAIN
                .setPositiveButton("VERSUCH NOCHMAL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.getMainInstance().getBookingTblData();
                    }
                })
                //CANCEL
                .setNegativeButton("ABBRUCH", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //.setIcon(android.R.drawable.ic_dialog_alert)
        //.show();
        alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
        nbutton.setBackgroundColor(Color.parseColor("#1B5E20"));
        //alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,20,0);
        nbutton.setLayoutParams(params);
    }

}
