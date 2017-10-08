package sixtysixp.clubwarden;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import sixtysixp.clubwarden.fragments.court123Fragment;
import sixtysixp.clubwarden.fragments.court45Fragment;
import sixtysixp.clubwarden.pojo.BookingTable;
import sixtysixp.clubwarden.staticClass.AlertUtils;
import sixtysixp.clubwarden.staticClass.BookingSlotColor;
import sixtysixp.clubwarden.staticClass.ResRepository;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Author: Ather Iltifat
 */

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "TestActivity";
    private List<Integer> dumpSlotRes = new ArrayList<Integer>();
    private List<Integer> dumpRows = new ArrayList<Integer>();
    private TextView tvBorderedSlot;
    private Calendar cal;
    private DateFormat dateFormat;
    private long minDate, maxDate;
    private int monthDay;
    private static MainActivity activityMain;
    private int spUserID, spCourtTypeID, spClubID, spUserTypeID;
    private String  spFirstName, spLastName;
    private Toast dateBtnsToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String clubName = intent.getExtras().getString("clubName");
        TextView tvClubName = (TextView) findViewById(R.id.tvClubName);
        tvClubName.setText(clubName);
        Log.i(TAG, "On Create .....");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        activityMain = this;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        getSharedPreferences();
        String strVar = String.format("Willkommen %s %s", spFirstName, spLastName);  //Wellcome
        Toast.makeText(this, strVar, Toast.LENGTH_SHORT).show();
        if(spUserTypeID == 1 ){
            LinearLayout infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
            infoLayout.setVisibility(View.VISIBLE);
        }
        init_calendarBtn();
        init_nextDateBtn();
        init_previousDateBtn();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "On Start .....");
    }

    @Override
    protected void onResume(){
        super.onResume();
        cal = Calendar.getInstance();
        monthDay = cal.get(Calendar.DAY_OF_MONTH);
        setDateInstance();
        minDate = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH,180);
        maxDate = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH,-180);

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
        removeSharedPrefInAPIClient();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "On Restart .....");
    }

    /**
     @ brief:  initialize the calendar Button which also sets the minimum and maximum date of calendar
     **/
    private void init_calendarBtn() {
        Button calendarBtn = (Button) findViewById(R.id.calendarBtn);
        calendarBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(dateBtnsToast != null){
                            dateBtnsToast.cancel();
                        }
                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                                AlertDialog.THEME_DEVICE_DEFAULT_DARK,new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal.set(year, monthOfYear, dayOfMonth);
                                chainMethod();
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        try {
                            datePickerDialog.getDatePicker().setMinDate(minDate - 1000);
                            datePickerDialog.getDatePicker().setMaxDate(maxDate);
                        }
                        catch (Exception ex){

                        }
                        datePickerDialog.show();
                    }
                }
        );
    }

    /**
     @ brief:  initialize the nextDate Button which will become unworkable when it reaches the maximum date
     **/
    private void init_nextDateBtn(){
        ImageButton nextDateBtn = (ImageButton) findViewById(R.id.nextDateBtn);
        nextDateBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(dateBtnsToast != null){
                            dateBtnsToast.cancel();
                        }
                        if(maxDate != cal.getTimeInMillis()){
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                            chainMethod();
                        }
                        else{
                            dateBtnsToast = Toast.makeText(MainActivity.this, "Maximal datum begerenzung", Toast.LENGTH_SHORT);
                            dateBtnsToast.show();
                        }
                    }
                }
        );
    }

    /**
     @ brief:  initialize the previousDate Button which will become unworkable when it reaches the minimum date
     **/
    private void init_previousDateBtn(){
        ImageButton previousDateBtn = (ImageButton) findViewById(R.id.previousDateBtn);
        previousDateBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(dateBtnsToast != null){
                            dateBtnsToast.cancel();
                        }
                        if(minDate != cal.getTimeInMillis()){
                            cal.add(Calendar.DAY_OF_MONTH, -1);
                            chainMethod();
                        }
                        else{
                            dateBtnsToast = Toast.makeText(MainActivity.this, "Minimal datum begrenzung", Toast.LENGTH_SHORT);
                            dateBtnsToast.show();
                        }
                    }
                }
        );

    }

    /**
     @ brief:  this method will set the date on calendar button
     **/
    private void set_calendarBtnTxt(){
        Button calendarBtn = (Button) findViewById(R.id.calendarBtn);
        String weekDay = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMAN);
        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.GERMAN);
        DateFormat dF = new SimpleDateFormat("yy");
        String year = dF.format(cal.getTimeInMillis());
        calendarBtn.setText(weekDay +" "+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "/" + monthName+ "/" + year);
    }

    /**
     @ brief:  this method will do several things
     (1) cancels the any alert
     (2) calls removeUserInfo() to remove  user info if notification bar is visible
     (3) calls removeSlotBorder() to removes the border from booking slot
     (4) calls bset_calendarBtnTxt to set the date on calendar Button
     (5) call getRegTblDataByDateFrmClient to get booking Table data by date
     **/
    private void chainMethod(){
        if(AlertUtils.alert != null) {
            AlertUtils.alert.cancel();
        }
        removeUserInfo();
        removeSlotBorder();
        removeRows();
        set_calendarBtnTxt();
        APIClient apiClient = new APIClient();
        apiClient.getRegTblDataByDateFrmClient(dateFormat.format(cal.getTimeInMillis()), spClubID);
    }

    /**
     @ brief:  this method will get the booking table data by date
     **/
    public void getBookingTblData(){
        chainMethod();
    }

    /**
     @ brief:  when the booking has passed, this method will remove the corresponding row
     **/
    public void removeRows() {
        if (monthDay == cal.get(Calendar.DAY_OF_MONTH)) {
            int limitLoop = getRowsLimit();
            for (int i = 0; i < limitLoop; i++) {
                int rowIDFg123 = ResRepository.rowsRes[i][0];
                int rowIDFg45 = ResRepository.rowsRes[i][1];
                TableRow tblRowFg123 = (TableRow) findViewById(rowIDFg123);
                TableRow tblRowFg45 = (TableRow) findViewById(rowIDFg45);
                tblRowFg123.setVisibility(View.GONE);
                tblRowFg45.setVisibility(View.GONE);
                dumpRows.add(rowIDFg123);
                dumpRows.add(rowIDFg45);
            }
        }
        else {
            if(dumpRows.size() > 0) {
                for (Integer item : dumpRows) {
                    TableRow tblRow = (TableRow) findViewById(item);
                    tblRow.setVisibility(View.VISIBLE);
                }
                dumpRows.clear();
            }
        }
    }

    /**
     @ brief:  this method determines how many rows have to be removed
     **/
    private int getRowsLimit(){
        int limitLoop = 0;
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        if(hour.equals("9")){
            limitLoop = 1;
        }
        else if(hour.equals("10")){
            limitLoop = 2;
        }
        else if(hour.equals("11")){
            limitLoop = 3;
        }
        else if(hour.equals("12")){
            limitLoop = 4;
        }
        else if(hour.equals("13")){
            limitLoop = 5;
        }
        else if(hour.equals("14")){
            limitLoop = 6;
        }
        else if(hour.equals("15")){
            limitLoop = 7;
        }
        else if(hour.equals("16")){
            limitLoop = 8;
        }
        else if(hour.equals("17")){
            limitLoop = 9;
        }
        else if(hour.equals("18")){
            limitLoop = 10;
        }
        else if(hour.equals("19")){
            limitLoop = 11;
        }
        return limitLoop;
    }

    /**
     @ brief:  this method will show the booking slot either in green or red color if ot is booked
     green color means slot is booked by ssame person who is using the app
     red color means slot is booked by another person
     **/
    public void fillRegTbl(List<BookingTable> dataList){
        clearRegTable();
        for (BookingTable item  : dataList) {
            int rowNum = getRowNum(item.getTimeSlot()) - 1;
            int courtNumber = item.getCourtNumber() - 1;
            int id = ResRepository.slotRes[rowNum][courtNumber];
            TextView tvBookingSlot = (TextView) findViewById(id);
            if(item.getUserID() == spUserID){
                tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.greenBgColor));
            }
            else{
                tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.redBgColor));
            }
            tvBookingSlot.setTag(item);
            dumpSlotRes.add(id);
        }
    }

    /**
     @ brief:  this method will clear the slots(removed the green or red background color from slot)
     **/
    private void clearRegTable(){
        if(dumpSlotRes.size() > 0) {
            for (Integer item :  dumpSlotRes) {
                TextView tvBookingSlot = (TextView) findViewById(item);
                tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor));
                tvBookingSlot.setTag(null);
            }
            dumpSlotRes.clear();
        }
    }

    /**
     @ brief:  this method will show the date of tommorow when the hor is equal to 20:00 or 21:00 or 22:00 and 23:00
     **/
    private void setDateInstance(){
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        if(hour.equals("20") || hour.equals("21") || hour.equals("22") || hour.equals("23")){
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
    }

    /**
     @ brief:  this method will get the MainActivity instance
     @ return  MainActivity
     **/
    public static MainActivity getMainInstance(){
        return activityMain;
    }

    /**
     @ brief:  this method will get shared preferences which is saved in Login Activity in this method addDataIntoSharedPref()
     **/
    private void getSharedPreferences(){
        SharedPreferences sharedpreferences = this.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        spUserID = sharedpreferences.getInt("userID", -1);
        spCourtTypeID = sharedpreferences.getInt("courtTypeID", -1);
        spClubID = sharedpreferences.getInt("clubID", -1);
        spUserTypeID = sharedpreferences.getInt("userTypeID", -1);
        spFirstName = sharedpreferences.getString("firstName",null);
        spLastName = sharedpreferences.getString("lastName",null);
        if (spUserID == -1 || spCourtTypeID == -1 || spClubID == -1 || spUserTypeID == -1 || spFirstName == null || spLastName == null) {
            AlertUtils.displayAlert1MainAct(MainActivity.this, "Fatal feher!", "Neustart oder.erneut installieren.");  // "Fatal error!", "Restart or reinstall app."
        }
    }

    ///////////////////////////////////////////fragment code////////////////////////////////////////
    /**
     @ brief:  this method will set the ViewPager
     @ Params:  ViewPager viewPager
     **/
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new court123Fragment(), "Platz  1-3");
        adapter.addFragment(new court45Fragment(), "Platz  4-5");
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                removeUserInfo();
                removeSlotBorder();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     @ brief:  this class will implement the ViewPager
     **/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     @ brief:  this method will show the booking table
     **/
    public void showBookingTbl(){
        TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tableLayout1);
        TableLayout tableLayout2 = (TableLayout) findViewById(R.id.tableLayout2);
        tableLayout1.setVisibility(View.VISIBLE);
        tableLayout2.setVisibility(View.VISIBLE);
    }

    /**
     @ brief:  this method will hide the booking table
     **/
    public void hideBookingTbl(){
        TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tableLayout1);
        TableLayout tableLayout2 = (TableLayout) findViewById(R.id.tableLayout2);
        tableLayout1.setVisibility(View.GONE);
        tableLayout2.setVisibility(View.GONE);
    }

    /**
     @ brief:  this method will show the progress bar
     **/
    public void showPBar(){
        LinearLayout layoutFr123PBar = (LinearLayout) findViewById(R.id.layoutFr123PBar);
        LinearLayout layoutFr45PBar = (LinearLayout) findViewById(R.id.layoutFr45PBar);
        ProgressBar fr123PBar = (ProgressBar) findViewById(R.id.fr123PBar);
        ProgressBar fr45PBar = (ProgressBar) findViewById(R.id.fr45PBar);
        layoutFr123PBar.setVisibility(View.VISIBLE);
        layoutFr45PBar.setVisibility(View.VISIBLE);
        fr123PBar.setVisibility(View.VISIBLE);
        fr45PBar.setVisibility(View.VISIBLE);
    }

    /**
     @ brief:  this method will hide the progress bar
     **/
    public void hidePBar(){
        LinearLayout layoutFr123PBar = (LinearLayout) findViewById(R.id.layoutFr123PBar);
        LinearLayout layoutFr45PBar = (LinearLayout) findViewById(R.id.layoutFr45PBar);
        ProgressBar fr123PBar = (ProgressBar) findViewById(R.id.fr123PBar);
        ProgressBar fr45PBar = (ProgressBar) findViewById(R.id.fr45PBar);
        layoutFr123PBar.setVisibility(View.GONE);
        layoutFr45PBar.setVisibility(View.GONE);
        fr123PBar.setVisibility(View.GONE);
        fr45PBar.setVisibility(View.GONE);
    }

    ///////////////////////////////////////Book/Unbook court ///////////////////////////////////////
    /**
     @ brief:  this method will be called when the user long clicks on booking slot
     (1) if the user is coach then this method will remove the text from notification bar and also removes border
     (2) if the user is coach then he can remove and register any booking slot
     (3) if the user is not coach then he can only remove his reserved booking slot and he can only reserve those slots which are
     unreserved
     @ Params:  View v
     **/
    public void onLongClickBookingSlot(View v){
        if(spUserTypeID == 1){
            TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
            TextView tvPhnNumber = (TextView) findViewById(R.id.tvPhnNumber);
            tvUserName.setText("");
            tvPhnNumber.setText("");
            removeSlotBorder();
        }

        TextView tvBookingSlot = (TextView) v;
        ColorDrawable tvColor = (ColorDrawable) tvBookingSlot.getBackground();
        int colorId = tvColor.getColor();
        String IdAsString = getResources().getResourceEntryName(tvBookingSlot.getId());
        String[] parts = IdAsString.split("court");
        String timeSlot = getTimeSlot(Integer.valueOf(parts[0].substring(6)));
        int courtNumber = Integer.valueOf(parts[1]);
        if (colorId == BookingSlotColor.intWhiteBgColor) {
            String msg = "Möchtet Ihr Platz " +courtNumber+ " reservieren von " + timeSlot+".";  // "Do you want to book Court " +courtNumber+ " at " + timeSlot+"."
            AlertUtils.displayAlertForBookingMainAct("Platz reservieren",msg , 1, tvBookingSlot, courtNumber, timeSlot);  // "Book court"
        }
        else if(colorId == BookingSlotColor.intRedBgColor && spUserTypeID == 1){  // coach ID is 1
            String msg = "Möchtet Ihr Platz " +courtNumber+ " befreien  von " + timeSlot+".";  // "Do you want to unbook Court " +courtNumber+ " at " + timeSlot+"."
            AlertUtils.displayAlertForBookingMainAct("Platz befreien",msg , 2, tvBookingSlot, courtNumber, timeSlot);  // "Unbook court"
        }
        else if(colorId == BookingSlotColor.intGreenBgColor){
            String msg = "Möchtet Ihr Platz " +courtNumber+ " befreien  von " + timeSlot+"."; // "Do you want to unbook Court " +courtNumber+ " at " + timeSlot+"."
            AlertUtils.displayAlertForBookingMainAct("Platz befreien",msg , 3, tvBookingSlot, courtNumber, timeSlot);  // "Unbook court"
        }
    }

    /**
     @ brief:  this method will book the court
     @ Params:  int courtNumber, String timeSlot
     **/
    public void bookCourt(int courtNumber, String timeSlot){
        String bookingDate = dateFormat.format(cal.getTimeInMillis());
        Calendar dateTimeNow = Calendar.getInstance();
        String dateTimeOfBooking = dateFormat.format(dateTimeNow.getTimeInMillis());
        APIClient apiClient = new APIClient();
        apiClient.bookCourt(spUserID, spClubID, spCourtTypeID, bookingDate, courtNumber, timeSlot, dateTimeOfBooking);
    }

    /**
     @ brief:  this method will unbook the court only by coach
     @ Params:  TextView tvBookingSlot
     **/
    public void unbookCourtByCoach(TextView tvBookingSlot){
    BookingTable bookingObj = (BookingTable) tvBookingSlot.getTag();
    int coachID = spUserID;
    int coachClubID = spClubID;
    int bookingTblID = bookingObj.getBookingTableID();
    String requestedDate = dateFormat.format(cal.getTimeInMillis());
    APIClient apiClient = new APIClient();
    apiClient.unbookCourtByCoach(coachID, coachClubID, bookingTblID, requestedDate);
}

    /**
     @ brief:  this method will unbook the court only by user
     @ Params:  TextView tvBookingSlot
     **/
    public void unbookCourtByUser(TextView tvBookingSlot){
        BookingTable bookingObj = (BookingTable) tvBookingSlot.getTag();
        int bookingTblID = bookingObj.getBookingTableID();
        String requestedDate = dateFormat.format(cal.getTimeInMillis());
        APIClient apiClient = new APIClient();
        apiClient.unbookCourtByUser(spUserID, spClubID, bookingTblID, requestedDate);
    }

    /**
     @ brief:  this method will get the time slot based on the row number
     @ Params:  int rowNum
     **/
    private String getTimeSlot(int rowNum){
        String timeSlot = null;
        if(rowNum == 1){
            timeSlot = "8:00-9:00";
        }
        else if(rowNum == 2){
            timeSlot = "9:00-10:00";
        }
        else if(rowNum == 3){
            timeSlot = "10:00-11:00";
        }
        else if(rowNum == 4){
            timeSlot = "11:00-12:00";
        }
        else if(rowNum == 5){
            timeSlot = "12:00-13:00";
        }
        else if(rowNum == 6){
            timeSlot = "13:00-14:00";
        }
        else if(rowNum == 7){
            timeSlot = "14:00-15:00";
        }
        else if(rowNum == 8){
            timeSlot = "15:00-16:00";
        }
        else if(rowNum == 9){
            timeSlot = "16:00-17:00";
        }
        else if(rowNum == 10){
            timeSlot = "17:00-18:00";
        }
        else if(rowNum == 11){
            timeSlot = "18:00-19:00";
        }
        else if(rowNum == 12){
            timeSlot = "19:00-20:00";
        }
        return timeSlot;
    }

    /**
     @ brief:  this method will get the row number based on the timeslot
     @ Params:  String timeSlot
     **/
    private int getRowNum(String timeSlot)
    {
        int rowNum = 0;
        if(timeSlot.equals("8:00-9:00")){
            rowNum = 1;
        }
        else if(timeSlot.equals("9:00-10:00")){
            rowNum = 2;
        }
        else if(timeSlot.equals("10:00-11:00")){
            rowNum = 3;
        }
        else if(timeSlot.equals("11:00-12:00")){
            rowNum = 4;
        }
        else if(timeSlot.equals("12:00-13:00")){
            rowNum = 5;
        }
        else if(timeSlot.equals("13:00-14:00")){
            rowNum = 6;
        }
        else if(timeSlot.equals("14:00-15:00")){
            rowNum = 7;
        }
        else if(timeSlot.equals("15:00-16:00")){
            rowNum = 8;
        }
        else if(timeSlot.equals("16:00-17:00")){
            rowNum = 9;
        }
        else if(timeSlot.equals("17:00-18:00")){
            rowNum = 10;
        }
        else if(timeSlot.equals("18:00-19:00")){
            rowNum = 11;
        }
        else if(timeSlot.equals("19:00-20:00")){
            rowNum = 12;
        }
        return rowNum;
    }

    /**
     @ brief:  this method will change the background color of slot to green color when the server response successfully
     @ Params:  BookingTable bookingTbl
     **/
    public void bookCourtOnResponse(BookingTable bookingTbl){
        int rowNum = getRowNum(bookingTbl.getTimeSlot()) - 1;
        int courtNumber = bookingTbl.getCourtNumber() - 1;
        int id = ResRepository.slotRes[rowNum][courtNumber];
        TextView tvBookingSlot = (TextView) findViewById(id);
        tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.greenBgColor));
        dumpSlotRes.add(id);
        tvBookingSlot.setTag(bookingTbl);
    }

    /**
     @ brief:  this method will be called when user clicks on the booking slot, this methis is doing several things.
     (1) it will remove any previous slot border
     (2) if the background of booking slot is white then it will fetch the whiteslotborder from drawable folder
         if the background of booking slot is green then it will fetch the greenslotborder from drawable folder
         if the background of booking slot is red then it will fetch the redslotborder from drawable folder
     @ Params:  TextView txtView
     **/
    public void onClickBookingSlot(TextView txtView){
        removeSlotBorder();
        ColorDrawable tvColor = (ColorDrawable) txtView.getBackground();
        int colorId = tvColor.getColor();
        if (colorId == BookingSlotColor.intWhiteBgColor) {
            //android:background="@drawable/bookinslot_border"
            txtView.setBackgroundResource(R.drawable.whiteslotborder);
        }
        else if(colorId == BookingSlotColor.intGreenBgColor){
            txtView.setBackgroundResource(R.drawable.greenslotborder);
        }
        else if(colorId == BookingSlotColor.intRedBgColor){
            txtView.setBackgroundResource(R.drawable.redslotborder);
        }
        showUserInfo(txtView);
        tvBorderedSlot = txtView;
    }

    /**
     @ brief:  this method will remove the slot border
     if the booking slot background color is white then it will change the background to white and removes its border
     if the booking slot background color is green then it will change the background to green and removes its border
     if the booking slot background color is red then it will change the background to red and removes its border
     **/
    private void removeSlotBorder(){
        try {
            if (tvBorderedSlot != null) {
                GradientDrawable gradientDrawable  = (GradientDrawable) tvBorderedSlot.getBackground();
                Class<? extends GradientDrawable> aClass = gradientDrawable.getClass();
                Field mFillPaint = aClass.getDeclaredField("mFillPaint");
                mFillPaint.setAccessible(true);
                Paint strokePaint= (Paint) mFillPaint.get(gradientDrawable);
                int colorId = strokePaint.getColor();
                //tvBorderedSlot.setBackgroundResource(0);
                if (colorId == BookingSlotColor.intWhiteBgColor) {
                    tvBorderedSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor));
                } else if (colorId == BookingSlotColor.intGreenBgColor) {
                    tvBorderedSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.greenBgColor));
                } else if (colorId == BookingSlotColor.intRedBgColor) {
                    tvBorderedSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.redBgColor));
                }
                tvBorderedSlot = null;
            }
        }
        catch(Exception ex){
        }
    }

    /**
     @ brief:  this method will show the user information on notification bar
     @ Params:  TextView txtView
     **/
    private void showUserInfo(TextView txtView){
        BookingTable bookingObj = (BookingTable) txtView.getTag();
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvPhnNumber = (TextView) findViewById(R.id.tvPhnNumber);
        if(bookingObj != null) {
            String name = bookingObj.getUser().getFirstName() + " " + bookingObj.getUser().getLastName();
            String phnNumbr = bookingObj.getUser().getPhoneNo();
            tvUserName.setText(name);
            tvPhnNumber.setText(phnNumbr);
        }
        else{
            tvUserName.setText("Platz befreit"); // "Unreserved"
            tvPhnNumber.setText("");
        }
    }

    /**
     @ brief:  this method will remove the user information from notification bar
     **/
    private void removeUserInfo(){
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvPhnNumber = (TextView) findViewById(R.id.tvPhnNumber);
        tvUserName.setText("");
        tvPhnNumber.setText("");
    }


    ////This method might not be necessary but just to xtra care we using it here /////////////////////////
    /**
     @ brief:  this method is called only one time from court45Fragment, it removes the shared preferences saved in
     "APIClient class" in mtehod "getRegTblDataByDateFrmClient
     This method might not be necessary but just to take extra care we using it here
     **/
    public void removeSharedPrefInAPIClient(){
        SharedPreferences sharedPref = getSharedPreferences("CountClickAndResponse", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.clear();
        edit.commit();
    }
}
