package com.jamiahus.ifunieray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditTask extends Activity {
    String taskTitle;
    String monthName;
    String monthNameOld;
    int year;
    int yearOld;
    int dayOfMonth;
    int dayOfMonthOld;
    int taskID;
    DatePicker myDatePicker;
    Button buttonUpdateTaskInfo;
    Button buttonDeleteTask;
    EditText editTextTitle;
    EditText editTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTitle = (EditText) findViewById(R.id.editText_Title);
        editTextDescription = (EditText) findViewById(R.id.editText_TaskDescription);
        myDatePicker = (DatePicker) findViewById(R.id.datePicker_Task_Date);
        buttonUpdateTaskInfo = (Button) findViewById(R.id.button_update_task);
        buttonDeleteTask = (Button) findViewById(R.id.button_Delete);

        Bundle extras = getIntent().getExtras(); //The information about the day selected will be collected here.

        if (extras != null){
            monthName = extras.getString("Month");
            dayOfMonth = extras.getInt("Day");
            year = extras.getInt("Year");
            taskTitle = extras.getString("TastTitle");
            taskID = extras.getInt("TaskID");

            //Set the old Dates
            monthNameOld = monthName;
            dayOfMonthOld = dayOfMonth;
            yearOld = year;
        }

        myDatePicker.updateDate(year,GetNumberForMonth(monthName),dayOfMonth);

        //Set the edit options to the current data so the user knows exactly what they are changing
        editTextTitle.setText(taskTitle);
        String currentTaskDescription = GetTaskDescription(taskID);
        editTextDescription.setText(currentTaskDescription);

        //TODO need to add a way to delete a task
        buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder deleteTaskBuilder = new AlertDialog.Builder(EditTask.this);
                deleteTaskBuilder.setTitle("Deleting Task Warning!");
                deleteTaskBuilder.setMessage("Are you sure you want to delete this task? This cannot be undone!");
                deleteTaskBuilder.setPositiveButton("YES - Delete Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RemoveTaskFromDatabase(taskID);
                    }
                });
                deleteTaskBuilder.setNegativeButton("NO - Return to Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog deleteAlert = deleteTaskBuilder.create();
                deleteAlert.show();

                //RemoveTaskFromDatabase(taskID);

            }
        });


        //Update Button
        buttonUpdateTaskInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputTaskTitle = editTextTitle.getText().toString();
                String userInputTaskDescription = editTextDescription.getText().toString();
                String userInputMonth = getMonthName(myDatePicker.getMonth());
                int userInputYear = myDatePicker.getYear();
                int userInputDayOfMonth = myDatePicker.getDayOfMonth();
                UpdateDatabaseWithNewInfo(userInputTaskTitle,userInputTaskDescription, userInputMonth,
                        userInputYear, userInputDayOfMonth);

                //Notify the user that the information is being updated
                Context context = getApplicationContext();
                CharSequence text = "Updating Task...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Go back to DayViewActivity (Day Should reflect changes
                Intent startDayViewActivity = new Intent(getApplicationContext(), DayViewActivity.class);
                //---- Day Information for the intent

                startDayViewActivity.putExtra("Month", monthNameOld);
                startDayViewActivity.putExtra("Day", dayOfMonthOld);
                startDayViewActivity.putExtra("Year", yearOld);

                startActivity(startDayViewActivity);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
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
        Intent startDayViewActivity = new Intent(getApplicationContext(),DayViewActivity.class);
        startDayViewActivity.putExtra("Month", monthNameOld);
        startDayViewActivity.putExtra("Day", dayOfMonthOld);
        startDayViewActivity.putExtra("Year", yearOld);

        startActivity(startDayViewActivity);
    }

    private void UpdateDatabaseWithNewInfo(String newTitle, String newDescription, String newMonth,
                                           int newYear, int newDayOfMonth){
        //Add data to database ---
        // Gets the data repository in write mode
        DayTasksDBHelper myDbHelpder = new DayTasksDBHelper(getApplicationContext());
        SQLiteDatabase db = myDbHelpder.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues myValues = new ContentValues();
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_TITLE, newTitle);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_DESCRIPTION, newDescription);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_MONTH_TASK, newMonth);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_YEAR_TASK, newYear);
        myValues.put(DayTasksContract.DayTasksContent.COLUMN_NAME_DAY_OF_MONTH_TASK, newDayOfMonth);

        // Insert the new row, returning the primary key value of the new row
        //long newRowId = db.insert(DayTasksContract.DayTasksContent.TABLE_NAME, null, myValues);

        String[] myarg = {String.valueOf(taskID)};
        db.update(DayTasksContract.DayTasksContent.TABLE_NAME,myValues,
                DayTasksContract.DayTasksContent._ID + " =?", myarg); //Need to add ID
    }

    private void RemoveTaskFromDatabase(int inputTaskID){

        //Remove a specific item from database
        DayTasksDBHelper myDbHelpder = new DayTasksDBHelper(getApplicationContext());
        SQLiteDatabase db = myDbHelpder.getWritableDatabase();

        // Define 'where' part of query.
        String selection = DayTasksContract.DayTasksContent._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(inputTaskID) };
        // Issue SQL statement.
        int deletedRows = db.delete(DayTasksContract.DayTasksContent.TABLE_NAME, selection, selectionArgs);

        //Go back to DayViewActivity (Day Should reflect changes
        Intent startDayViewActivity = new Intent(getApplicationContext(), DayViewActivity.class);
        //---- Day Information for the intent

        startDayViewActivity.putExtra("Month", monthNameOld);
        startDayViewActivity.putExtra("Day", dayOfMonthOld);
        startDayViewActivity.putExtra("Year", yearOld);

        startActivity(startDayViewActivity);
    }

    private int GetNumberForMonth (String inputMonth){
        int monthNumber = -1;
        switch (inputMonth){
            case "January":
                monthNumber = 0;
                break;
            case "February":
                monthNumber = 1;
                break;
            case "March":
                monthNumber = 2;
                break;
            case "April":
                monthNumber = 3;
                break;
            case "May":
                monthNumber = 4;
                break;
            case "June":
                monthNumber = 5;
                break;
            case "July":
                monthNumber = 6;
                break;
            case "August":
                monthNumber = 7;
                break;
            case "September":
                monthNumber = 8;
                break;
            case "October":
                monthNumber = 9;
                break;
            case "November":
                monthNumber = 10;
                break;
            case "December":
                monthNumber = 11;
                break;

            default:
                monthNumber = -1;
                break;
        }
        return monthNumber;
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
    private String GetTaskDescription(int taskIdForCurrentItem){
        //Read from database ---
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] myProjection = {BaseColumns._ID,
                DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_DESCRIPTION,};

        // Filter results WHERE "task ID" = 'current task ID'
        String mySelection = DayTasksContract.DayTasksContent._ID + " = ?";
        String[] mySelectionArgs = { String.valueOf(taskIdForCurrentItem)};

        // How you want the results sorted in the resulting Cursor
        //String mySortOrder = DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_TITLE + " DESC";

        DayTasksDBHelper myDbHelpder = new DayTasksDBHelper(getApplicationContext());
        SQLiteDatabase db = myDbHelpder.getWritableDatabase();

        Cursor myCursor = db.query(
                DayTasksContract.DayTasksContent.TABLE_NAME,   // The table to query
                myProjection,             // The array of columns to return (pass null to get all)
                mySelection,              // The columns for the WHERE clause
                mySelectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null//mySortOrder               // The sort order
        );

        //taskTitles.clear(); //Make sure the list is clear
        //listOfTasks.clear(); //Make sure the list is clear
        String taskDescription = "";
        while (myCursor.moveToNext()){
            taskDescription =
                    myCursor.getString(myCursor.getColumnIndexOrThrow(DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_DESCRIPTION));
            //taskTitles.add(taskTitle);
            int currentTaskID = myCursor.getInt(myCursor.getColumnIndexOrThrow(DayTasksContract.DayTasksContent._ID));

            Task currentTask = new Task(currentTaskID);
            //listOfTasks.add(currentTask);
        }

        myCursor.close();
        return taskDescription;
    }
}
