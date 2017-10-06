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
 * Created by hassan on 6/22/2017.
 */
// http status codes between 200 - 300 are accepted in retrofit to get the data in response.body()
public class APIClient {
    private ProgressDialog dialog;
    private String authHeader;
    private Retrofit retrofit;

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
                         //.baseUrl("http://192.168.10.2:99/test/api/").client(okHttpClient)
                         .baseUrl("https://bookingappservice.apphb.com/api/").client(okHttpClient)
                         .addConverterFactory(GsonConverterFactory.create());
                 retrofit = builder.build();

    }


    ////////////////////////////////"2016/01/15 00:00:00"///////////////////////////////////////////
    public void getRegTblDataByDateFrmClient(String requestedDate, int clubID){
        //shared pref used in this method to synchronize the no.of click (next and previous date btn) to exact response
        //Retrofit retrofit = builder.build();
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
                            // "Error", "Please try again."
                            AlertUtils.displayAlertForRegTblMainAct("Fehler", "Bitte versuche es erneut.");
                        }
                    }

/*                    editorial.clear();
                    editorial.commit();
                    edit.clear();
                    edit.commit();*/

                    }

                    catch (Exception ex) {
                        mainActInstance.hidePBar();
                        edit.clear();
                        edit.commit();
                        // "Unexpected error", "Please try again."
                        AlertUtils.displayAlertForRegTblMainAct("Unerwarteter Fehler", "Bitte versuche es erneut.");
                    }
                }

                @Override
                public void onFailure(Call<List<BookingTable>> call, Throwable throwable) {
                    ///////////////////// testing /////////////////////
                    SharedPreferences sharedPref = mainActInstance.getSharedPreferences("CountClickAndResponse", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.clear();
                    edit.commit();
                    //////////////////////////////////////////////
                    mainActInstance.hidePBar();
                    // "Network error", "Please try again."
                    AlertUtils.displayAlertForRegTblMainAct("Netzwerkfehler", "Bitte versuche es erneut.");
                }
            });
    }
    public void bookCourt(int userID, int clubID, int courtTypeID, String bookingDate, int courtNumber, String timeSlot,
                          String dateTimeOfBooking){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<BookingTable> call = apiInterface.bookCourtFrmInterface(authHeader, userID, clubID, courtTypeID,
                bookingDate, courtNumber, timeSlot, dateTimeOfBooking);
        Context mainCtxt = MainActivity.getMainInstance();
        final MainActivity mainInstance = MainActivity.getMainInstance();
        //"Booking Court, please wait"
        dialog = ProgressBarUtils.displayProgressBar(mainCtxt,"Platzreservierung im Gange, Bitte warten", false,false);
        call.enqueue(new Callback<BookingTable>(){
            @Override
            public void onResponse(Call<BookingTable> call, Response<BookingTable> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    // "200" OK
                    if (responseCode.equals("200")) {
                        BookingTable bookingTbl = response.body();
                        mainInstance.bookCourtOnResponse(bookingTbl);
                        //"Successfully booked court."
                        Toast.makeText(mainInstance, "Platz erfolgreich reserviert.", Toast.LENGTH_LONG).show();
                    }
                    else if(responseCode.equals("400")){  // "400" Bad request
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("Message");
                        if(errorMsg.equals("alreadyBooked")){
                            // "Failed, court is already booked."
                            Toast.makeText(mainInstance, "Gescheitert, Platz ist bereits reserviert", Toast.LENGTH_LONG).show();
                        }
                        else if(errorMsg.equals("unauthorized")){
                            // "Unauthorized, you are not allowed to book court"
                            Toast.makeText(mainInstance, "Unerlaubt", Toast.LENGTH_LONG).show();
                        }
                    }

                    else{
                        // "Error, please try again."
                        Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception ex){
                    // "Error, please try again."
                    Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BookingTable> call, Throwable throwable) {
                dialog.dismiss();
                // "Network error", "Please try again."
                AlertUtils.displayAlert1MainAct(mainInstance,"Netzwerkfehler", "Bitte versuche es erneut.");
            }
        });
    }
    public void unbookCourtByCoach(int coachID, int coachClubID, int bookingTblID, String requestedDate){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<List<BookingTable>> call = apiInterface.unbookCourtByCoachFrmInterface(authHeader, coachID, coachClubID, bookingTblID, requestedDate);
        Context mainCtxt = MainActivity.getMainInstance();
        final MainActivity mainInstance = MainActivity.getMainInstance();
        // "Unbooking court, please wait"
        dialog = ProgressBarUtils.displayProgressBar(mainCtxt,"Stornierung der Reservierung, bitte warten", false,false);
        call.enqueue(new Callback<List<BookingTable>>(){
            @Override
            public void onResponse(Call<List<BookingTable>> call, Response<List<BookingTable>> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {  // "200" OK
                        List<BookingTable> dataList = response.body();
                        mainInstance.fillRegTbl(dataList);
                        // "Successfully unbooked court."
                        Toast.makeText(mainInstance, "tornierung der Reservierung erfolgreich.", Toast.LENGTH_LONG).show();
                    }

                    else{
                        // "Error, please try again."
                        Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception ex){
                    // "Error, please try again."
                    Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingTable>> call, Throwable throwable) {
                dialog.dismiss();
                // "Network error, please try again."
                Toast.makeText(mainInstance, "Netzwerkfehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void unbookCourtByUser(int userID, int clubID, int bookingTblID, String requestedDate){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<List<BookingTable>> call = apiInterface.unbookCourtByUserFrmInterface(authHeader, userID, clubID, bookingTblID,
                requestedDate);
        Context mainCtxt = MainActivity.getMainInstance();
        final MainActivity mainInstance = MainActivity.getMainInstance();
        // "Unbooking court, please wait."
        dialog = ProgressBarUtils.displayProgressBar(mainCtxt,"Stornierung der Reservierung, bitte warten.", false,false);
        call.enqueue(new Callback<List<BookingTable>>(){
            @Override
            public void onResponse(Call<List<BookingTable>> call, Response<List<BookingTable>> response) {
                //MainActivity mainActInstance = MainActivity.getMainInstance();
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    // "200" OK
                    if (responseCode.equals("200")) {
                        List<BookingTable> dataList = response.body();
                        mainInstance.fillRegTbl(dataList);
                        // "Successfully unbooked court"
                        Toast.makeText(mainInstance, "Stornierung der Reservierung erfolgreich", Toast.LENGTH_LONG).show();
                    }
                    else if(responseCode.equals("400")){  // "400" Bad request
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("Message");
                        if(errorMsg.equals("statusUnauthorized")){
                            // "Unauthorized, you are not allowed to unbook court."
                            Toast.makeText(mainInstance, "Unerlaubt.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            // "Error, please try again."
                            Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        // "Error, please try again."
                        Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
                    }


                }
                catch(Exception ex){
                    // "Error, please try again."
                    Toast.makeText(mainInstance, "Fehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingTable>> call, Throwable throwable) {
                dialog.dismiss();
                // "Network error, please try again."
                Toast.makeText(mainInstance, "Netzwerkfehler, bitte versuche es erneut.", Toast.LENGTH_LONG).show();
            }
        });
    }


    ////////////////////////////  Login Activity ///////////////////////////////////////////////////
    public void verifyPassAndAddUser(String firstName, String lastName, String phoneNo, String joinDate,String password){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<User> call = apiInterface.verifyPassAndAddUserFrmApi(authHeader, firstName, lastName, phoneNo, joinDate, password);
        Context loginCtxt = LoginActivity.getLoginInstance();
        final LoginActivity loginInstance = LoginActivity.getLoginInstance();
        // "Please wait."
        dialog = ProgressBarUtils.displayProgressBar(loginCtxt,"Bitte warten.", false,false);
        loginInstance.addTempSharedPref(firstName, lastName, phoneNo, joinDate);
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User > call, Response<User > response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    if (responseCode.equals("200")) {
                        //JsonObject object = (JsonObject) response.body();
                        //int userTypeID =Integer.valueOf(String.valueOf(object.get("userTypeID")));
                        User user = response.body();
                        loginInstance.chainMethod(user);
                    }
                    else if (responseCode.equals("400")) {  // "400" Bad Request
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("Message");
                        if(errorMsg.equals("incorrectPassword")){
                            loginInstance.removeSharedPref();
                            // "Incorrect Password"
                            AlertUtils.displayAlert4LoginAct("Falsches Passwort");
                        }
                        else{
                            loginInstance.removeSharedPref();
                            // "Error", "Please try again."
                            AlertUtils.displayAlertUnfixed("Fehler", "Bitte versuche es erneut.");
                        }
                    }

                    else{
                        loginInstance.removeSharedPref();
                        // "Error", "Please try again."
                        AlertUtils.displayAlertUnfixed("Fehler", "Bitte versuche es erneut.");
                    }
                }
                catch(Exception ex){
                    // Unexpected error","Please try again.
                    AlertUtils.displayAlertUnfixed("Unerwarteter Fehler", "Bitte versuche es erneut.");
                }
            }

            @Override
            public void onFailure(Call<User > call, Throwable throwable) {
                dialog.dismiss();
                // "Network error", "Please try again."
                AlertUtils.displayAlertUnfixed("Netzwerkfehler","Bitte versuche es erneut.");
            }
        });
    }
    public void getUserByIdFromClient(int userId){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<User> call = apiInterface.getUserByIdFromInterface(authHeader,userId);
        Context loginCtxt = LoginActivity.getLoginInstance();
        final LoginActivity loginInstance = LoginActivity.getLoginInstance();
        // "Please wait."
        dialog = ProgressBarUtils.displayProgressBar(loginCtxt,"Bitte warten.", false,false);
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    // "200" got the user
                    if (responseCode.equals("200")) {
                        User user = response.body();
                        loginInstance.chainMethod(user);
                    }
                    else{
                        // "Error", "Please try again."
                        AlertUtils.displayAlert2LoginAct("Fehler", "Bitte versuche es erneut.");
                    }
                }
                catch(Exception ex){
                    // "Unexpected error", "Please try again."
                    AlertUtils.displayAlert2LoginAct("Unerwarteter Fehler", "Bitte versuche es erneut.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                dialog.dismiss();
                // "Network error", "Please try again."
                AlertUtils.displayAlert2LoginAct("Netzwerkfehler","Bitte versuche es erneut.");
            }
        });
    }
    public void getUserByNamePhoneAndDate(String fname, String lname, String phone, String joinDate){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<User> call = apiInterface.getUserByNamePhoneDateFrmInterface(authHeader,fname, lname, phone, joinDate);
        Context loginCtxt = LoginActivity.getLoginInstance();
        final LoginActivity loginInstance = LoginActivity.getLoginInstance();
        // "Please wait."
        dialog = ProgressBarUtils.displayProgressBar(loginCtxt,"Bitte warten.", false,false);
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                try {
                    String responseCode = String.valueOf(response.code());
                    // "200" got the user
                    if (responseCode.equals("200")) {
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
                            // "Server Error","Please try again."
                            loginInstance.removeSharedPref();
                            AlertUtils.displayAlert2LoginAct("Serverfehler","Bitte versuche es erneut.");
                        }
                    }

                    else{
                        // "Server Error","Please try again."
                        AlertUtils.displayAlert2LoginAct("Serverfehler","Bitte versuche es erneut.");
                    }
                }
                catch(Exception ex){
                    // "Unexpected error", "Please try again."
                    loginInstance.removeSharedPref();
                    AlertUtils.displayAlert2LoginAct("Unerwarteter Fehler", "Bitte versuche es erneut.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                dialog.dismiss();
                // "Network error", "Please try again."
                AlertUtils.displayAlert2LoginAct("Netzwerkfehler","Bitte versuche es erneut.");
            }
        });
    }

    ////////////////////////////////////// getting single string //////////////////
    /*    public void getDbPassword(){
        Retrofit retrofit = builder.build();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<String> call = apiInterface.getPassword();
        call.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String dbPass = response.body().toString();
                LoginActivity.receivePassword(dbPass);
                //Toast.makeText(MainActivity.this, userName, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                LoginActivity.getLoginActivityInstance().displayAlert("Network Error", "Network error, plz try again");
            }

        });

    }*/
    ///////////////////////////////////////////////////////////////////////////

}
