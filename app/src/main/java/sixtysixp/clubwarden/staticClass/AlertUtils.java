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
 * Author: Ather Iltifat
 */

public class AlertUtils {
    public static AlertDialog alert;

    public static void displayAlertUnfixed(String title,String msg){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.getLoginInstance(), R.style.MyDialogTheme);
        builder.setTitle(title)
                .setMessage(msg).setView(R.layout.dialog_divider)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
    }
    public static void displayAlert2LoginAct(String title,String msg){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.getLoginInstance(), R.style.MyDialogTheme);
        builder.setTitle(title)
                .setMessage(msg).setView(R.layout.dialog_divider)
                .setPositiveButton("VERSUCH NOCHMAL", new DialogInterface.OnClickListener() {  // TRY AGAIN
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.getLoginInstance().getUserIdFromDevice();
                    }
                })
                .setNegativeButton("BEENDEN", new DialogInterface.OnClickListener() {  //QUIT
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.getLoginInstance().finish();
                    }
                });
        alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
        nbutton.setBackgroundColor(Color.parseColor("#1B5E20"));
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
        alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
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

                .setNegativeButton("ABBRUCH", new DialogInterface.OnClickListener() {  // CANCEL
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
                .setPositiveButton("VERSUCH NOCHMAL", new DialogInterface.OnClickListener() {  // TRY AGAIN
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.getMainInstance().getBookingTblData();
                    }
                })
                .setNegativeButton("ABBRUCH", new DialogInterface.OnClickListener() { //CANCEL
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        pbutton.setBackgroundColor(Color.parseColor("#558B2F"));
        nbutton.setBackgroundColor(Color.parseColor("#1B5E20"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,20,0);
        nbutton.setLayoutParams(params);
    }

}
