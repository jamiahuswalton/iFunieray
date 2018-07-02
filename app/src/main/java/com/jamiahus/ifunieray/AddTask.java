package com.jamiahus.ifunieray;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

import java.util.Date;

public class AddTask extends Activity {

    private AdView myAdView;
    Button buttonSaveTask;
    DatePicker taskDatePicker;
    TextView taskTitle;
    TextView taskDescription;
    String monthNameOld;
    int yearOld;
    int dayOfMonthOld;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Bundle extras = getIntent().getExtras(); //The information about the day selected will be collected here.

        if (extras != null){
            monthNameOld = extras.getString("Month");
            dayOfMonthOld = extras.getInt("Day");
            yearOld = extras.getInt("Year");
        }

        taskDatePicker = (DatePicker) findViewById(R.id.datePicker_Task_Date);
        taskTitle = (TextView) findViewById(R.id.editText_Title);
        taskDescription = (TextView) findViewById(R.id.editText_TaskDescription);

        //This is the save button. The user will click this when they are ready to save the task
        buttonSaveTask = (Button) findViewById(R.id.button_Save_task);
        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int myMonth = taskDatePicker.getMonth(); //January = 0, this means that the months will be off by 1
                int day = taskDatePicker.getDayOfMonth();
                int year = taskDatePicker.getYear();

                /*
                Context context = getApplicationContext();
                //CharSequence text = "Month: " + myMonth + ", " + "Day: " + day + ", " + "Year: " + year;
                CharSequence text = "Title: " + taskTitle.getText() + ", Description: " + taskDescription.getText();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                */

                String inputTitle = taskTitle.getText().toString();
                String inputDescription = taskDescription.getText().toString();
                String inputMonth = getMonthName(myMonth);
                addItemActivityToDatabase(inputTitle,inputDescription,inputMonth,year,day);
            }
        });
    }

    private void addItemActivityToDatabase(String inputTaskTitle, String inputTaskDescription,
                                           String inputTaskMonth, int inputTaskYear, int inputTaskDayOfMonth){
        //Add data to database ---
        // Gets the data repository in write mode
        DayTasksDBHelper myDbHelpder = new DayTasksDBHelper(getApplicationContext());
        SQLiteDatabase db = myDbHelpder.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues myValues = new ContentValues();
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_TITLE, inputTaskTitle);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_DESCRIPTION, inputTaskDescription);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_MONTH_TASK, inputTaskMonth);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_YEAR_TASK, inputTaskYear);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_DAY_OF_MONTH_TASK, inputTaskDayOfMonth);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DayTasksContract.DayTasksContent.TABLE_NAME, null, myValues);

        //Go back to DayViewActivity (Day Should reflect changes
        Intent startDayViewActivity = new Intent(getApplicationContext(), DayViewActivity.class);
        //---- Day Information for the intent

        startDayViewActivity.putExtra("Month", monthNameOld);
        startDayViewActivity.putExtra("Day", dayOfMonthOld);
        startDayViewActivity.putExtra("Year", yearOld);

        startActivity(startDayViewActivity);
    }

    private String getMonthName(int monthNumberInput){
        String monthName ="";

        switch (monthNumberInput){
            case 0:
                monthName = "January";
                break;
            case 1:
                monthName = "February";
                break;
            case 2:
                monthName = "March";
                break;
            case 3:
                monthName = "April";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "June";
                break;
            case 6:
                monthName = "July";
                break;
            case 7:
                monthName = "August";
                break;
            case 8:
                monthName = "September";
                break;
            case 9:
                monthName = "October";
                break;
            case 10:
                monthName = "November";
                break;
            case 11:
                monthName = "December";
                break;

            default:
                monthName = "Error with Date Name";
                break;
        }
        return monthName;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        /*
        Context context = getApplicationContext();
        CharSequence text = "Back button was pressed";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();*/

        //Return to the Day Activity View
        //Intent startMainActivityActivity = new Intent(getApplicationContext(), MainActivity.class);
        Intent startDayViewActivity = new Intent(getApplicationContext(), DayViewActivity.class);
        startDayViewActivity.putExtra("Month", monthNameOld);
        startDayViewActivity.putExtra("Day", dayOfMonthOld);
        startDayViewActivity.putExtra("Year", yearOld);

        startActivity(startDayViewActivity);
    }
}
