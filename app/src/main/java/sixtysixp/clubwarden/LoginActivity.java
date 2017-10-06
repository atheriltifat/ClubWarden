package sixtysixp.clubwarden;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sixtysixp.clubwarden.pojo.User;
import sixtysixp.clubwarden.staticClass.AlertUtils;
import sixtysixp.clubwarden.staticClass.BookingSlotColor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hassan on 7/10/2017.
 */

//////////// set the user joinDate with respect to their System not with "Europe/Berlin" timezone in next update
public class LoginActivity  extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private static LoginActivity activityLogin;
    private EditText firstName, lastName, phoneNo, password;
    private static String clubName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On Create .....");
        activityLogin = this;

        /////////////////////// testing //////////////////////
/*        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        int userId = sharedpreferences.getInt("userID",-1);
        if(userId != -1) {
            editor.remove("userID");
        }*/
/*        editor.putString("firstName", "fvd");
        editor.putString("lastName", "fgdc");
        editor.putString("phoneNo","fgdc");
        editor.putString("joinDate","2017/09/19 05:56:29");*/
        //editor.commit();
        //editor.putInt("userID", 1);
        //editor.commit();

        //////////////////////////////////////////////////////
        getUserIdFromDevice();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "On Start .....");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "On Resume .....");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "On Pause .....");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "On Stop .....");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "On Destroy .....");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "On Restart .....");
    }

    //////////////////////////////////////////// Login //////////////////////////////////////////
    private void onClickloginBtn(){
        try {
            chngBgOfEditView();
            String fName = firstName.getText().toString();
            String lName = lastName.getText().toString();
            String phone = phoneNo.getText().toString();
            String passWord = password.getText().toString();
            String[] inputStrings = trimInputString(fName, lName, phone);  //trim() removes the leading and trailing spaces from string
            fName = inputStrings[0];                                       // trim() will throw error if string is null
            lName = inputStrings[1];
            phone = inputStrings[2];
            if (chkEmptyOrNullStr(fName, lName, phone, passWord)) {
                fName = fName.substring(0, 1).toUpperCase() + fName.substring(1).toLowerCase(); // makes first letter capital and remaining small.
                lName = lName.substring(0, 1).toUpperCase() + lName.substring(1).toLowerCase();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMAN);
                String joinDate = dateFormat.format(calendar.getTimeInMillis());
                APIClient client = new APIClient();
                client.verifyPassAndAddUser(fName, lName, phone, joinDate, passWord);
            }
        }
        catch(Exception ex){
            Toast toast = Toast.makeText(LoginActivity.this,"Feher in Ihre Eingabedaten", Toast.LENGTH_LONG); // "Error, check your credentials"
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }
    public void getUserIdFromDevice(){
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        int userId = sharedpreferences.getInt("userID",-1);
        //checks user exist on device
        if(userId == -1){
            String fname = sharedpreferences.getString("firstName",null);
            String lName = sharedpreferences.getString("lastName",null);
            String phone = sharedpreferences.getString("phoneNo",null);
            String joinDate = sharedpreferences.getString("joinDate",null);
            // checks user exist on device without userID
            if((fname != null && !fname.isEmpty()) &&  (lName != null && !lName.isEmpty()) && (phone != null && !phone.isEmpty())
                    && (joinDate != null && !joinDate.isEmpty())) {
                APIClient client = new APIClient();
                client.getUserByNamePhoneAndDate(fname, lName, phone, joinDate);
            }
            else{
                initLoginForm();
            }
        }
        else{
            APIClient client = new APIClient();
            client.getUserByIdFromClient(userId);
        }
    }

    private void initLoginForm(){
        setContentView(R.layout.activity_login);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        phoneNo = (EditText) findViewById(R.id.phoneNo);
        password = (EditText) findViewById(R.id.password);

        password.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            onClickloginBtn();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        initLoginBtn();
    }
    private void initLoginBtn(){
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        onClickloginBtn();
                    }
                }
        );
    }
    /////////////////////////////////////////// Validation //////////////////////////////////////
    private String[] trimInputString(String fName, String lName, String phone){
        //trim() removes the leading and trailing spaces from string
        // trim() will throw error if string is null
        if(fName!=null){
            fName = fName.trim();
            if(fName.substring(fName.length() - 1).equals("."))
            {
                fName = fName.substring(0, fName.length() - 1);
            }
        }
        if(lName!=null){
            lName = lName.trim();
            if(lName.substring(lName.length() - 1).equals("."))
            {
                lName = lName.substring(0, lName.length() - 1);
            }
        }
        if(phone!=null){
            phone = phone.trim();
        }
        String[] inputStrings = {fName, lName, phone};
        return inputStrings;
    }
    private boolean chkEmptyOrNullStr(String fName, String lName, String phone, String passWord){
        boolean isValid = true;
        String msg = null;
        TextView tv = null;
        if(fName.isEmpty() || fName==null){
            msg = "Bitte geben Sie Ihren 'Vornamen ein'";  //"Please enter 'First Name'"
            tv = firstName;
            isValid = false;
        }
        else if(lName.isEmpty() || lName==null){
            msg = "Bitte geben Sie Ihren 'Nachnamen ein'";  //"Please enter 'Last Name'"
            tv = lastName;
            isValid = false;
        }
        else if(phone.isEmpty() || phone==null){
            msg = "Bitte geben Sie Ihren 'Telefonnummer ein'";  //"Please enter 'Phone Number'"
            tv = phoneNo;
            isValid = false;
        }
        else if(passWord.isEmpty() || passWord==null){
            msg = "Bitte geben Sie Ihren 'Kennwort ein'";  //"Please enter 'Password'"
            tv = password;
            isValid = false;
        }

        if(!isValid){
            tv.setBackgroundResource(R.drawable.redborderlogin);
            tv.requestFocus();
            tv.setText("");
            AlertUtils.displayAlert5LoginAct(msg);
        }
        return isValid;
    }

    private void chngBgOfEditView(){
        firstName.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor));
        lastName.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor));
        phoneNo.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor));
        password.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor));
    }
    private boolean chkUserStatus(){
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        Boolean isAllowed = false;
        Boolean isBan = sharedpreferences.getBoolean("isBanned", false);
        Boolean isApprove = sharedpreferences.getBoolean("isApproved", true);
        Boolean isAct = sharedpreferences.getBoolean("isActive", true);
        if(!isBan) {
            if (isApprove) {
                if(isAct){
                    //jumpToMainActivity();
                    isAllowed = true;
                }
                else{
                    AlertUtils.displayAlert3LoginAct("Inaktiv", "bitte mit Trainer kontaktieren"); // "Inactive", "Contact coach"
                    isAllowed = false;
                }
            }
            else {
                LinearLayout layoutLoginAct1 = (LinearLayout) findViewById(R.id.layoutLoginAct1);
                if(layoutLoginAct1 != null){
                    layoutLoginAct1.setVisibility(View.GONE);
                }
                AlertUtils.displayAlert3LoginAct("Brauchen Sie die Genehmigung", "Ihre Anfrage wurde an Trainer geschickt");  //"Need approval", "Your request has been sent to coach"
                isAllowed = false;
            }
        }
        else{
            AlertUtils.displayAlert3LoginAct("Verboten", "Du wurdest verboten");  //"Banned", "You have been banned"
            isAllowed = false;
        }
        return isAllowed;
    }

    /////////////////////////////////////////// Shared Preference ////////////////////////////////
    private void addDataIntoSharedPref(User user){
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        int userId = sharedpreferences.getInt("userID",-1);
        if(userId == -1) {
            editor.putInt("userID", user.getUserID());
        }
        editor.putInt("userTypeID", user.getUserTypeID());
        editor.putString("firstName", user.getFirstName());
        editor.putString("lastName", user.getLastName());
        editor.putString("phoneNo", user.getPhoneNo());
        editor.putBoolean("isActive", user.getIsActive());
        editor.putBoolean("isBanned", user.getIsBanned());
        editor.putBoolean("isApproved", user.getIsApproved());
        editor.putString("joinDate",user.getJoinDate());
        editor.putInt("courtTypeID", 1);
        editor.putInt("clubID", 1);
        editor.commit();

        clubName = user.getClub().getClubName();
    }
    public void addTempSharedPref(String firstName, String lastName, String phoneNo, String joinDate){
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("phoneNo",phoneNo);
        editor.putString("joinDate",joinDate);
        editor.commit();
    }
    public void removeSharedPref(){
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    ///////////////////////////////////////////// public method //////////////////////////////////
    public static LoginActivity getLoginInstance(){
        return activityLogin;
    }
    public void focusPasswordField(){
        password.requestFocus();
        password.setText("");
        InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
    }
    public void chainMethod(User user){
        addDataIntoSharedPref(user);
        if(chkUserStatus()){
            jumpToMainActivity();
        }
    }

    ///////////////////////////////////////////// private method /////////////////////////////////
    private void jumpToMainActivity(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.putExtra("clubName", clubName);
        startActivity(i);
        finish();
    }




    //////////////////////////////////////////// Do not delete these methods///////////////////////

/*    private boolean chkInputStrLength(String fName, String lName, String phone, String passWord){
        boolean isValid = true;
        int charLimit = 20;
        InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(fName.length() >= charLimit){
            firstName.setText("");
            firstName.requestFocus();
            iMM.showSoftInput(firstName, InputMethodManager.SHOW_IMPLICIT);
            Toast toast = Toast.makeText(LoginActivity.this,"Limit is 20 characters", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            isValid = false;
        }
        else if(lName.length() >= charLimit){
            lastName.setText("");
            lastName.requestFocus();
            iMM.showSoftInput(lastName, InputMethodManager.SHOW_IMPLICIT);
            Toast toast = Toast.makeText(LoginActivity.this,"Limit is 20 characters", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            isValid = false;
        }
        else if(phone.length() >= charLimit){
            phoneNo.setText("");
            phoneNo.requestFocus();
            iMM.showSoftInput(phoneNo, InputMethodManager.SHOW_IMPLICIT);
            Toast toast = Toast.makeText(LoginActivity.this,"Limit is 20 characters", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            isValid = false;
        }
        else if(passWord.length() >= charLimit){
            password.setText("");
            password.requestFocus();
            iMM.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
            Toast toast = Toast.makeText(LoginActivity.this,"Incorrect Password", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            isValid = false;
        }

        return isValid;
    }*/

/*    private boolean chkPhnNoFormat(String phnNumber){
        boolean isValid = true;
        if(phnNumber.matches(".*[a-zA-Z]+.*")){  //return true if it matches atleast one alphabet.
            isValid = false;
        }
        else{

*//*            char[] specialCh = {'\\' , '\'' , '`' , '~' , '!' , '@' , '$' , '%' , '^' , '&' , '*' , '_' , '=' ,
                    '{' , '}' , '[' , ']' , '|' , ';' , ':' , ',' , '.' , '<' , '>' , '"' , '?' , '/' };*//*

            char[] phnNumArray = phnNumber.toCharArray();
            char[] specialCh = {'(' , ')' , '-' , '+' , '#' };
            for(int i = 0; i<phnNumArray.length; i++){
                if( !(phnNumArray[i] >= '0' && phnNumArray[i] <= '9') ) {
                    for (int j = 0; j < specialCh.length; j++) {
                        if (phnNumArray[i] != specialCh[j]) {
                            isValid = false;
                            break;
                        }
                    }
                }
                if (!isValid) {
                    break;
                }
            }
        }
        if(!isValid){
            InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            phoneNo.requestFocus();
            iMM.showSoftInput(phoneNo, InputMethodManager.SHOW_IMPLICIT);
            Toast toast = Toast.makeText(LoginActivity.this,"Phone number is incorrect", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
        return isValid;
    }*/
/*    private void doActionOnInvaildation(View view, String msg){
        //InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //iMM.showSoftInput(firstName, InputMethodManager.SHOW_IMPLICIT);
        view.setBackgroundResource(R.drawable.redborderlogin);
        view.requestFocus();
        AlertUtils.displayAlert5LoginAct(msg);
    }*/



}
