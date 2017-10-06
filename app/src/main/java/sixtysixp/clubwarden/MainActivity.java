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
import android.os.Handler;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
/*    final int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {}
    else {}*/
    private final static String TAG = "TestActivity";
    private List<Integer> dumpSlotRes = new ArrayList<Integer>();
    private List<Integer> dumpRows = new ArrayList<Integer>();
    private TextView tvBorderedSlot;
    private Calendar cal;
    private DateFormat dateFormat;
    private long minDate, maxDate;
    private int monthDay;
    private static MainActivity activityMain;
    ////////////////////////////////////
    //private final int interval = 1000; // 1 Second
    //private Handler handler = new Handler();
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
       // handler.postDelayed(runnable, interval);
        Log.i(TAG, "On Create .....");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        activityMain = this;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
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
        //////////////////////////testing///////////////////////////////////////////////////////////
        //cal = Calendar.getInstance();
        //cal.set(2017, 8, 21);
        //cal.set(Calendar.HOUR_OF_DAY, 15);
        //cal.set(Calendar.MINUTE, 30);
        //int min = cal.get(Calendar.MINUTE);
        //int hour = cal.get(Calendar.HOUR_OF_DAY);
        //cal.set(Calendar.AM_PM,1);
        ////////////////////////////////////////////////////////////////////////////////////////////
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

    ///////////////////////////////////////////// Calendar Btns ////////////////////////////////////
    private void init_calendarBtn() {
        Button calendarBtn = (Button) findViewById(R.id.calendarBtn);
        calendarBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(dateBtnsToast != null){
                            dateBtnsToast.cancel();
                        }
                        //R.style.GPSTheme, AlertDialog.THEME_TRADITIONAL, AlertDialog.THEME_DEVICE_DEFAULT_DARK
                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                                AlertDialog.THEME_DEVICE_DEFAULT_DARK,new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal.set(year, monthOfYear, dayOfMonth);
                                chainMethod();
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        //datePickerDialog.setCanceledOnTouchOutside(true);

                        /////////////////////  testing  //////////////////////////////
                        //Date date = new Date(minDate);
                        //DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        //formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                        //String dateFormatted = formatter.format(minDate);
                        //Calendar callio = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMAN);
                        //datePickerDialog.getDatePicker().setMinDate(callio.getTimeInMillis() - 1000);
                        ///////////////////////////////////////////////////
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
                            // "Maximum date limit"
                            dateBtnsToast = Toast.makeText(MainActivity.this, "Maximal datum begerenzung", Toast.LENGTH_SHORT);
                            dateBtnsToast.show();
                        }
                    }
                }
        );
    }
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
    private void set_calendarBtnTxt(){
        Button calendarBtn = (Button) findViewById(R.id.calendarBtn);
        String weekDay = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMAN);
        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.GERMAN);
        DateFormat dF = new SimpleDateFormat("yy");
        //dF.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        String year = dF.format(cal.getTimeInMillis());
        calendarBtn.setText(weekDay +" "+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "/" + monthName+ "/" + year);
    }
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
    public void getBookingTblData(){
        chainMethod();
    }

    //////////////////////////////////////////fragment Rows/////////////////////////////////////////
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

    /////////////////////////////////////////Registration table/////////////////////////////////////
    public void fillRegTbl(List<BookingTable> dataList){
        clearRegTable();
        for (BookingTable item  : dataList) {
            int rowNum = getRowNum(item.getTimeSlot()) - 1;
            int courtNumber = item.getCourtNumber() - 1;
            int id = ResRepository.slotRes[rowNum][courtNumber];
            TextView tvBookingSlot = (TextView) findViewById(id);
            if(item.getUserID() == spUserID){
                tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.greenBgColor)); //#008066  green color
            }
            else{
                tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.redBgColor)); //#CD5C5C  red color
            }
            tvBookingSlot.setTag(item);
            dumpSlotRes.add(id);
            //BookingTable aa  = (BookingTable)tv.getTag();
        }
    }
    private void clearRegTable(){
        if(dumpSlotRes.size() > 0) {
            for (Integer item :  dumpSlotRes) {
                TextView tvBookingSlot = (TextView) findViewById(item);
                tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor)); // white color
                tvBookingSlot.setTag(null);
            }
            dumpSlotRes.clear();
        }
    }
    private void setDateInstance(){
        // chks if the time has passed 8AM then it will show the next date
/*        String hour = String.valueOf(cal.get(Calendar.HOUR)) + " " + getCurrentHour();
        if(hour.equals("8 PM") || hour.equals("9 PM") || hour.equals("10 PM") || hour.equals("11 PM")){
            cal.add(Calendar.DAY_OF_MONTH,1);
        }*/
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        if(hour.equals("20") || hour.equals("21") || hour.equals("22") || hour.equals("23")){
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
    }

   ////////////////////////////////////////Getters//////////////////////////////////////////////////
    public static MainActivity getMainInstance(){
        return activityMain;
    }
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

    ///////////////////////////////////////////Progress bar ////////////////////////////////////////
    public void showBookingTbl(){
        TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tableLayout1);
        TableLayout tableLayout2 = (TableLayout) findViewById(R.id.tableLayout2);
        tableLayout1.setVisibility(View.VISIBLE);
        tableLayout2.setVisibility(View.VISIBLE);
    }
    public void hideBookingTbl(){
        TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tableLayout1);
        TableLayout tableLayout2 = (TableLayout) findViewById(R.id.tableLayout2);
        tableLayout1.setVisibility(View.GONE);
        tableLayout2.setVisibility(View.GONE);
    }
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



/*    private Runnable runnable = new Runnable(){
        public void run() {
            Toast.makeText(MainActivity.this, "C'Mom no hands!", Toast.LENGTH_SHORT).show();
        }
    };*/
    ///////////////////////////////////////Book/Unbook court ///////////////////////////////////////
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
        // -591379 -- white color
        if (colorId == BookingSlotColor.intWhiteBgColor) {
            String msg = "Möchtet Ihr Platz " +courtNumber+ " reservieren von " + timeSlot+".";  // "Do you want to book Court " +courtNumber+ " at " + timeSlot+"."
            AlertUtils.displayAlertForBookingMainAct("Platz reservieren",msg , 1, tvBookingSlot, courtNumber, timeSlot);  // "Book court"
        }
        // -3318692 -- red color
        else if(colorId == BookingSlotColor.intRedBgColor && spUserTypeID == 1){  // coach ID is 1
            String msg = "Möchtet Ihr Platz " +courtNumber+ " befreien  von " + timeSlot+".";  // "Do you want to unbook Court " +courtNumber+ " at " + timeSlot+"."
            AlertUtils.displayAlertForBookingMainAct("Platz befreien",msg , 2, tvBookingSlot, courtNumber, timeSlot);  // "Unbook court"
        }
        // green color
        else if(colorId == BookingSlotColor.intGreenBgColor){
            String msg = "Möchtet Ihr Platz " +courtNumber+ " befreien  von " + timeSlot+"."; // "Do you want to unbook Court " +courtNumber+ " at " + timeSlot+"."
            AlertUtils.displayAlertForBookingMainAct("Platz befreien",msg , 3, tvBookingSlot, courtNumber, timeSlot);  // "Unbook court"
        }
    }
    public void bookCourt(int courtNumber, String timeSlot){
        String bookingDate = dateFormat.format(cal.getTimeInMillis());
        //Calendar dateTimeNow = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMAN);
        Calendar dateTimeNow = Calendar.getInstance();
        String dateTimeOfBooking = dateFormat.format(dateTimeNow.getTimeInMillis());
        APIClient apiClient = new APIClient();
        apiClient.bookCourt(spUserID, spClubID, spCourtTypeID, bookingDate, courtNumber, timeSlot, dateTimeOfBooking);
    }

    public void unbookCourtByCoach(TextView tvBookingSlot){
    BookingTable bookingObj = (BookingTable) tvBookingSlot.getTag();
    int coachID = spUserID;
    int coachClubID = spClubID;
    int bookingTblID = bookingObj.getBookingTableID();
    String requestedDate = dateFormat.format(cal.getTimeInMillis());
    APIClient apiClient = new APIClient();
    apiClient.unbookCourtByCoach(coachID, coachClubID, bookingTblID, requestedDate);
}
    public void unbookCourtByUser(TextView tvBookingSlot){
        BookingTable bookingObj = (BookingTable) tvBookingSlot.getTag();
        int bookingTblID = bookingObj.getBookingTableID();
        String requestedDate = dateFormat.format(cal.getTimeInMillis());
        APIClient apiClient = new APIClient();
        apiClient.unbookCourtByUser(spUserID, spClubID, bookingTblID, requestedDate);
    }
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
    public void bookCourtOnResponse(BookingTable bookingTbl){
        int rowNum = getRowNum(bookingTbl.getTimeSlot()) - 1;
        int courtNumber = bookingTbl.getCourtNumber() - 1;
        int id = ResRepository.slotRes[rowNum][courtNumber];
        TextView tvBookingSlot = (TextView) findViewById(id);
        tvBookingSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.greenBgColor));
        dumpSlotRes.add(id);
        tvBookingSlot.setTag(bookingTbl);
    }

   ///////////////////////////////////////// Slot Border //////////////////////////////////////////

    public void onClickBookingSlot(TextView txtView){
        removeSlotBorder();
        ColorDrawable tvColor = (ColorDrawable) txtView.getBackground();
        int colorId = tvColor.getColor();
        if (colorId == BookingSlotColor.intWhiteBgColor) { // white color
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
                if (colorId == BookingSlotColor.intWhiteBgColor) { // white color
                    tvBorderedSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.whiteBgColor)); // white color
                } else if (colorId == BookingSlotColor.intGreenBgColor) {
                    tvBorderedSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.greenBgColor)); // white color
                } else if (colorId == BookingSlotColor.intRedBgColor) {
                    tvBorderedSlot.setBackgroundColor(Color.parseColor(BookingSlotColor.redBgColor)); // white color
                }
                tvBorderedSlot = null;
            }
        }
        catch(Exception ex){
        }
    }
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
    private void removeUserInfo(){
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvPhnNumber = (TextView) findViewById(R.id.tvPhnNumber);
        tvUserName.setText("");
        tvPhnNumber.setText("");
    }


    ////This method might not be necessary but just to xtra care we using it here /////////////////////////
    public void removeSharedPrefInAPIClient(){
        //this method is called only one time from court45Fragment
        //This removes the shared preferences
        //in "APIClient class" mtehod "getRegTblDataByDateFrmClient"
        SharedPreferences sharedPref = getSharedPreferences("CountClickAndResponse", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.clear();
        edit.commit();
    }
}
