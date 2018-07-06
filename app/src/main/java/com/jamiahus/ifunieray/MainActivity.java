package com.jamiahus.ifunieray;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    CalendarView mainCalednarView;
    //This button is used to confirm that the user wants to view the events for the currently selected day.
    Button buttonOK;
    private AdView myAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.e("Database Test", "The onUpgrade method was launched.");
        //Log.d("DATABASE DEBUG", "Value: " + checkDataBase());
        if (checkDataBase()){
            DayTasksDBHelper myDbHelpder = new DayTasksDBHelper(getApplicationContext());
            SQLiteDatabase db = myDbHelpder.getWritableDatabase();
        }
        //Log.d("DATABASE DEBUG", "Value(2): " + checkDataBase());

        //Load ad
        myAdView = findViewById(R.id.myAdView);
        AdRequest myAdRequest = new AdRequest.Builder().build();
        myAdView.loadAd(myAdRequest);

        mainCalednarView = findViewById(R.id.mainCalendarView);
        buttonOK = findViewById(R.id.button_OK);



        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdfMonth = new SimpleDateFormat("MM", Locale.US);
                SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.US);
                SimpleDateFormat sdfYear = new SimpleDateFormat("yyy", Locale.US);

                String selectedDateMonth = getMonthName(Integer.valueOf(sdfMonth.format(new Date(mainCalednarView.getDate())))) ;
                int selectedDateDay = Integer.valueOf(sdfDay.format(new Date(mainCalednarView.getDate()))) ;
                int selectedDateYear =Integer.valueOf(sdfYear.format(new Date(mainCalednarView.getDate()))) ;


                //TODO Start Actvity for the day view. In this view we will show the hours of the day.
                //Intent to start Day View Activity
                Intent startDayViewActivity = new Intent(getApplicationContext(), DayViewActivity.class);
                //---- Day Information for the intent

                startDayViewActivity.putExtra("Month", selectedDateMonth);
                startDayViewActivity.putExtra("Day", selectedDateDay);
                startDayViewActivity.putExtra("Year", selectedDateYear);

                startActivity(startDayViewActivity);
            }
        });
    }

    private String getMonthName(int monthNumberInput){
        String monthName;

        switch (monthNumberInput){
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;

            default:
                monthName = "Error with Date Name";
                break;
        }

        return monthName;
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String DB_FULL_PATH = getApplicationContext().getDatabasePath(DayTasksDBHelper.DATABASE_NAME).toString();
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    /*
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    } */
}
