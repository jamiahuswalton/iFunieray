package com.jamiahus.ifunieray;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;


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

        //TODO: Set on date change listner to check the number of task on the day that is currently selected.
        //mainCalednarView.setOnDateChangeListener(new View.OnClickListener());

        //Count the number of tasks that are scheduled for this day. - Testing
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM", Locale.US);
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.US);
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyy", Locale.US);

        String selectedDateMonth = getMonthName(Integer.valueOf(sdfMonth.format(new Date(mainCalednarView.getDate())))) ;
        int selectedDateDay = Integer.valueOf(sdfDay.format(new Date(mainCalednarView.getDate()))) ;
        int selectedDateYear =Integer.valueOf(sdfYear.format(new Date(mainCalednarView.getDate()))) ;
        int test = CountTasksForGivenDay(selectedDateYear,selectedDateDay,selectedDateMonth);
        Toast.makeText(this, String.valueOf(test), Toast.LENGTH_SHORT).show();
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

    private int CountTasksForGivenDay(int currentYear, int currentDayOfMonth, String currentMonthName){
        int taskCount = 0;

        //Read from database ---

        DayTasksDBHelper myDbHelpder = new DayTasksDBHelper(getApplicationContext());
        SQLiteDatabase db = myDbHelpder.getWritableDatabase();
        /*
        * Define a projection that specifies which columns from the database you will actually use
        * after this query.  */
        String[] myProjection = {BaseColumns._ID,
                DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_TITLE,
                DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_DESCRIPTION,
                DayTasksContract.DayTasksContent.COLUMN_NAME_YEAR_TASK};

        /* Pick the columns that I want to set a WHERE clause for. Ultimately, I want to pick all
        of the tasks for a particular month, day, and year. The day that the user selected.*/
        String mySelection = DayTasksContract.DayTasksContent.COLUMN_NAME_YEAR_TASK + " = ? AND " +
                DayTasksContract.DayTasksContent.COLUMN_NAME_DAY_OF_MONTH_TASK + " = ? AND " +
                DayTasksContract.DayTasksContent.COLUMN_NAME_MONTH_TASK + " = ? ";
        //Pick what is the particular month day day year? This should be passed through the EXTRAs
        String[] mySelectionArgs = {
                String.valueOf(currentYear),
                String.valueOf(currentDayOfMonth),
                currentMonthName};

        // How you want the results sorted in the resulting Cursor?
        String mySortOrder = DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_TITLE + " DESC";

        //DayTasksDBHelper myDbHelpder = new DayTasksDBHelper(getApplicationContext());
        //SQLiteDatabase db = myDbHelpder.getWritableDatabase();

        Cursor myCursor = db.query(
                DayTasksContract.DayTasksContent.TABLE_NAME,   // The table to query
                myProjection,             // The array of columns to return (pass null to get all)
                mySelection,              // The columns for the WHERE clause
                mySelectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                mySortOrder               // The sort order
        );

        while (myCursor.moveToNext()){
            //Count the number of tasks in this day.
            taskCount++;
        }

        myCursor.close();

        return taskCount;
    }
}
