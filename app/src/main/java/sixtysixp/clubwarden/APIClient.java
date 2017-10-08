package sixtysixp.clubwarden;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import sixtysixp.clubwarden.pojo.BookingTable;
import sixtysixp.clubwarden.pojo.User;
import sixtysixp.clubwarden.staticClass.AlertUtils;
import sixtysixp.clubwarden.staticClass.ProgressBarUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

/**
 * Author: Ather Iltifat
 */
// http status codes between 200 - 300 are accepted in retrofit to get the data in response.body()
public class APIClient {
    private ProgressDialog dialog;
    private String authHeader;
    private Retrofit retrofit;

    /**
     @ brief:  the constructor will set the authHeader which is used by Web API without it, it will not process the requet.
     the constructor will also set the readtime out and connection time out, it also saves the main URL of web api
     **/
    public APIClient(){
        String userName = "ather";
        String password = "abc";
        String base = userName + ":" + password;
        authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        ////////////// prevent the timeout error for 20 seconds //////////////////
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        //////////////////////////////////////////////////////////////////////////
        Retrofit.Builder builder = new Retrofit.Builder()
                         .baseUrl("https://bookingappservice.apphb.com/api/").client(okHttpClient)
                         .addConverterFactory(GsonConverterFactory.create());
                 retrofit = builder.build();

    }


    ////////////////////////////////"2016/01/15 00:00:00"///////////////////////////////////////////
    /**
     @ brief:  this method will get the registraion or booking table data by date.
     it hides the booking table and shows the progress bar when the request is made to server and shows the booking table and hide
     the progress bar on response from server.
     It also uses the shared pref used in this method to synchronize the no.of click (next and previous date btn)
     to exact response i.e when the user fastly clicks the next or previous Button it will only show the booking table data
     of the date on which the user stops.
     @ Params:  String requestedDate, int clubID
     **/
    public void getRegTblDataByDateFrmClient(String requestedDate, int clubID){
        //
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<List<BookingTable>> call = apiInterface.getRegTblDataByDateFrmInterface(authHeader, requestedDate, clubID);
        final MainActivity mainActInstance = MainActivity.getMainInstance();
        mainActInstance.hideBookingTbl();
        mainActInstance.showPBar();
        SharedPreferences sharedPref = mainActInstance.getSharedPreferences("CountClickAndResponse", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        int countClick = sharedPref.getInt("countClick",-1);
        if(countClick == -1){
            edit.putInt("countClick", 1);
        }
        else{
            countClick = countClick + 1;
            edit.putInt("countClick", countClick);
        }
        edit.commit();
        call.enqueue(new Callback<List<BookingTable>>() {
            @Override
            public void onResponse(Call<List<BookingTable>> call, Response<List<BookingTable>> response) {
                SharedPreferences sharedPref = mainActInstance.getSharedPreferences("CountClickAndResponse", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPref.edit();
                try {
                    int countResponse = sharedPref.getInt("countResponse",-1);
                    if(countResponse == -1){
                        edit.putInt("countResponse", 1);
                    }
                    else{
                        countResponse = countResponse+1;
                        edit.putInt("countResponse", countResponse);
                    }
                    edit.commit();
                    int countClick = sharedPref.getInt("countClick",-1);
                    countResponse = sharedPref.getInt("countResponse",-1);
                    if(countClick == countResponse) {
                        edit.clear();
                        edit.commit();


                        String responseCode = String.valueOf(response.code());
                        // "200" got the user
                        if (responseCode.equals("200")) {
                            mainActInstance.hidePBar();
                            mainActInstance.showBookingTbl();
                            List<BookingTable> dataList = response.body();
                            mainActInstance.fillRegTbl(dataList);
                        } else {
                            mainActInstance.hidePBar();
                            AlertUtils.displayAlertForRegTblMainAct("Fehler", "Bitte versuche es erneut.");  // "Error", "Please try again."
                        }
                    }

                    }

                    catch (Exception ex) {
                        mainActInstance.hidePBar();
                        edit.clear();
                        edit.commit();
                        AlertUtils.displayAlertForRegTblMainAct("Unerwarteter Fehler", "Bitte versuche es erneut.");  // "Unexpected error", "Please try again."
                    }
                }

                @Override
                public void onFailure(Call<List<BookingTable>> call, Throwable throwable) {
                    SharedPreferences sharedPref = mainActInstance.getSharedPreferences("CountClickAndResponse", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.clear();
                    edit.commit();
                    mainActInstance.hidePBar();
                    AlertUtils.displayAlertForRegTblMainAct("Netzwerkfehler", "Bitte versuche es erneut.");  // "Network error", "Please try again."
                }
            });
    }


    /**
     @ brief:  this method will send the request to server to book court.
     @ Params:  int userID, int clubID, int courtTypeID, String bookingDate, int courtNumber, String timeSlot,
     String dateTimeOfBooking
     **/
    public void bookCourt(int userID, int clubID, int courtTypeID, String bookingDate, int courtNumber, String timeSlot,
                          String dateTimeOfBooking){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<BookingTable> call = apiInterface.bookCourtFrmInterface(authHeader, userID, clubID, courtTypeID,
                bookingDate, courtNumber, timeSlot, dateTimeOfBooking);
        Context mainCtxt = MainActivity.getMainInstance();
        final MainActivity mainInstance = MainActivity.getMainInstance();
        dialog = ProgressBarUtils.displayProgressBar(mainCtxt,"Platzreservierung im Gange, Bitte warten", false,false);  //"Booking Court, please wait"
        call.enqueue(new Callback<BookingTable>(){
            @Override
            public void onResponse(Call<BookingTable> call, Response<BookingTable> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {  // "200" OK
                        BookingTable bookingTbl = response.body();
                        mainInstance.bookCourtOnResponse(bookingTbl);
                        Toast.makeText(mainInstance, "Platz erfolgreich reserviert.", Toast.LENGTH_LONG).show();  //"Successfully booked court."
                    }
                    else if(responseCode.equals("400")){  // "400" Bad request
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("Message");
                        if(errorMsg.equals("alreadyBooked")){
                            Toast.makeText(mainInstance, "Gescheitert, Platz ist bereits reserviert", Toast.LENGTH_LONG).show();  // "Failed, court is already booked."
                        }
                        else if(errorMsg.equals("unauthorized")){
                            Toast.makeText(mainInstance, "Unerlaubt", Toast.LENGTH_LONG).show();  // "Unauthorized, you are not allowed to book court"
                        }
                    }

                    else{
                        Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Error, please try again."
                    }
                }
                catch(Exception ex){
                    Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Error, please try again."
                }
            }

            @Override
            public void onFailure(Call<BookingTable> call, Throwable throwable) {
                dialog.dismiss();
                AlertUtils.displayAlert1MainAct(mainInstance,"Netzwerkfehler", "Bitte versuche es erneut.");  // "Network error", "Please try again."
            }
        });
    }

    /**
     @ brief:  this method will send the request to server to unbook court by coach.
     @ Params:  int coachID, int coachClubID, int bookingTblID, String requestedDate
     **/
    public void unbookCourtByCoach(int coachID, int coachClubID, int bookingTblID, String requestedDate){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<List<BookingTable>> call = apiInterface.unbookCourtByCoachFrmInterface(authHeader, coachID, coachClubID, bookingTblID, requestedDate);
        Context mainCtxt = MainActivity.getMainInstance();
        final MainActivity mainInstance = MainActivity.getMainInstance();
        dialog = ProgressBarUtils.displayProgressBar(mainCtxt,"Stornierung der Reservierung, bitte warten", false,false);  // "Unbooking court, please wait"
        call.enqueue(new Callback<List<BookingTable>>(){
            @Override
            public void onResponse(Call<List<BookingTable>> call, Response<List<BookingTable>> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {  // "200" OK
                        List<BookingTable> dataList = response.body();
                        mainInstance.fillRegTbl(dataList);
                        Toast.makeText(mainInstance, "tornierung der Reservierung erfolgreich.", Toast.LENGTH_LONG).show();  // "Successfully unbooked court."
                    }

                    else{
                        Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Error, please try again."
                    }
                }
                catch(Exception ex){
                    Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Error, please try again."
                }
            }

            @Override
            public void onFailure(Call<List<BookingTable>> call, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(mainInstance, "Netzwerkfehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Network error, please try again."
            }
        });
    }

    /**
     @ brief:  this method will send the request to server to unbook court by user.
     @ Params:  int userID, int clubID, int bookingTblID, String requestedDate
     **/
    public void unbookCourtByUser(int userID, int clubID, int bookingTblID, String requestedDate){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<List<BookingTable>> call = apiInterface.unbookCourtByUserFrmInterface(authHeader, userID, clubID, bookingTblID,
                requestedDate);
        Context mainCtxt = MainActivity.getMainInstance();
        final MainActivity mainInstance = MainActivity.getMainInstance();
        dialog = ProgressBarUtils.displayProgressBar(mainCtxt,"Stornierung der Reservierung, bitte warten.", false,false);  // "Unbooking court, please wait."
        call.enqueue(new Callback<List<BookingTable>>(){
            @Override
            public void onResponse(Call<List<BookingTable>> call, Response<List<BookingTable>> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {  // "200" OK
                        List<BookingTable> dataList = response.body();
                        mainInstance.fillRegTbl(dataList);
                        Toast.makeText(mainInstance, "Stornierung der Reservierung erfolgreich", Toast.LENGTH_LONG).show();  // "Successfully unbooked court"
                    }
                    else if(responseCode.equals("400")){  // "400" Bad request
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("Message");
                        if(errorMsg.equals("statusUnauthorized")){
                            Toast.makeText(mainInstance, "Unerlaubt.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Error, please try again."
                        }
                    }
                    else {
                        Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Error, please try again."
                    }


                }
                catch(Exception ex){
                    Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Error, please try again."
                }
            }

            @Override
            public void onFailure(Call<List<BookingTable>> call, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(mainInstance, "Netzwerkfehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();  // "Network error, please try again."
            }
        });
    }


    ////////////////////////////  Login Activity ///////////////////////////////////////////////////
    /**
     @ brief:  this method will send the request to server of user date and password to register into the database if the credentials
     and password is correct then the server will accept the request
     @ Params:  String firstName, String lastName, String phoneNo, String joinDate,String password
     **/
    public void verifyPassAndAddUser(String firstName, String lastName, String phoneNo, String joinDate,String password){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<User> call = apiInterface.verifyPassAndAddUserFrmApi(authHeader, firstName, lastName, phoneNo, joinDate, password);
        Context loginCtxt = LoginActivity.getLoginInstance();
        final LoginActivity loginInstance = LoginActivity.getLoginInstance();
        dialog = ProgressBarUtils.displayProgressBar(loginCtxt,"Bitte warten.", false,false);  // "Please wait."
        loginInstance.addTempSharedPref(firstName, lastName, phoneNo, joinDate);
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User > call, Response<User > response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {
                        User user = response.body();
                        loginInstance.chainMethod(user);
                    }
                    else if (responseCode.equals("400")) {  // "400" Bad Request
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("Message");
                        if(errorMsg.equals("incorrectPassword")){
                            loginInstance.removeSharedPref();
                            AlertUtils.displayAlert4LoginAct("Falsches Passwort");  // "Incorrect Password"
                        }
                        else{
                            loginInstance.removeSharedPref();
                            AlertUtils.displayAlertUnfixed("Fehler", "Bitte versuche es erneut.");  // "Error", "Please try again."
                        }
                    }

                    else{
                        loginInstance.removeSharedPref();
                        AlertUtils.displayAlertUnfixed("Fehler", "Bitte versuche es erneut.");  // "Error", "Please try again."
                    }
                }
                catch(Exception ex){
                    AlertUtils.displayAlertUnfixed("Unerwarteter Fehler", "Bitte versuche es erneut.");  // Unexpected error","Please try again.
                }
            }

            @Override
            public void onFailure(Call<User > call, Throwable throwable) {
                dialog.dismiss();

                AlertUtils.displayAlertUnfixed("Netzwerkfehler","Bitte versuche es erneut.");  // "Network error", "Please try again."
            }
        });
    }

    /**
     @ brief:  this method will get the user by ID from server
     and password is correct then the server will accept the request
     @ Params:  int userId
     **/
    public void getUserByIdFromClient(int userId){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<User> call = apiInterface.getUserByIdFromInterface(authHeader,userId);
        Context loginCtxt = LoginActivity.getLoginInstance();
        final LoginActivity loginInstance = LoginActivity.getLoginInstance();
        dialog = ProgressBarUtils.displayProgressBar(loginCtxt,"Bitte warten.", false,false);  // "Please wait."
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {  // "200" got the user
                        User user = response.body();
                        loginInstance.chainMethod(user);
                    }
                    else{
                        AlertUtils.displayAlert2LoginAct("Fehler", "Bitte versuche es erneut.");  // "Error", "Please try again."
                    }
                }
                catch(Exception ex){
                    AlertUtils.displayAlert2LoginAct("Unerwarteter Fehler", "Bitte versuche es erneut.");  // "Unexpected error", "Please try again."
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                dialog.dismiss();
                AlertUtils.displayAlert2LoginAct("Netzwerkfehler","Bitte versuche es erneut.");  // "Network error", "Please try again."
            }
        });
    }

    /**
     @ brief:  this method will get the user by name, last name, phone and join date, if it receives the message of multiple user
     or not found then it will remove the sa=hared pref
     @ Params:  String fname, String lname, String phone, String joinDate
     **/
    public void getUserByNamePhoneAndDate(String fname, String lname, String phone, String joinDate){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<User> call = apiInterface.getUserByNamePhoneDateFrmInterface(authHeader,fname, lname, phone, joinDate);
        Context loginCtxt = LoginActivity.getLoginInstance();
        final LoginActivity loginInstance = LoginActivity.getLoginInstance();
        dialog = ProgressBarUtils.displayProgressBar(loginCtxt,"Bitte warten.", false,false);  // "Please wait."
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {  // "200" got the user
                        User user = response.body();
                        loginInstance.chainMethod(user);
                    }

                    else if (responseCode.equals("400")) {  // "400" Bad Request
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("Message");
                        //found multiple users in DB   // user not found in DB
                        if(errorMsg.equals("multipleUsers") || errorMsg.equals("notFound")){
                            loginInstance.removeSharedPref();
                            loginInstance.getUserIdFromDevice();
                        }
                        else {
                            loginInstance.removeSharedPref();
                            AlertUtils.displayAlert2LoginAct("Serverfehler","Bitte versuche es erneut.");  // "Server Error","Please try again."
                        }
                    }

                    else{
                        AlertUtils.displayAlert2LoginAct("Serverfehler","Bitte versuche es erneut.");  // "Server Error","Please try again."
                    }
                }
                catch(Exception ex){
                    loginInstance.removeSharedPref();
                    AlertUtils.displayAlert2LoginAct("Unerwarteter Fehler", "Bitte versuche es erneut.");  // "Unexpected error", "Please try again."
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                dialog.dismiss();
                AlertUtils.displayAlert2LoginAct("Netzwerkfehler","Bitte versuche es erneut.");  // "Network error", "Please try again."
            }
        });
    }

}
