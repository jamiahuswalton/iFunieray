package com.jamiahus.ifunieray;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.ListActivity;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class DayViewActivity extends Activity {


    TextView initialTextView;
    String monthName;
    //String[] listContent = {"Pray1", "Bless", "Savior", "Jesus"};;
    List taskTitles = new ArrayList<>();
    List<Task> listOfTasks = new ArrayList<Task>();
    String taskTitle;
    int year;
    int dayOfMonth;
    Button buttonAddNewTask;
    private AdView myAdView;
    ListView myListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        myListView = (ListView) findViewById(R.id.listView);

        //Load ad
        myAdView = (AdView)findViewById(R.id.myAdView);
        AdRequest myAdRequest = new AdRequest.Builder().build();
        myAdView.loadAd(myAdRequest);

        Bundle extras = getIntent().getExtras(); //The information about the day selected will be collected here.

        //TODO Need to create a list of activities that are on the day that is selected.
        initialTextView = (TextView) findViewById(R.id.Date_Display); //This is used to debug

        if (extras != null){
            monthName = extras.getString("Month");
            dayOfMonth = extras.getInt("Day");
            year = extras.getInt("Year");
        }
        initialTextView.setText(monthName + ", " + dayOfMonth + ", " + year);

        ReadFromDatabseAndUpdateListToReflectAnyChanges();


        //TODO Create a button listener so something can happen when an item is clicked


        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                //TODO Start an activity that will open the selected item and then allow the user to edit it

                Intent startEditTastActivity = new Intent(getApplicationContext(), EditTask.class);
                //---- Day Information for the intent
                //TODO Send the ID of the item selected
                Task temp = listOfTasks.get(position);
                int selectedItemID = temp.GetTaskID();
                startEditTastActivity.putExtra("Month", monthName);
                startEditTastActivity.putExtra("Day", dayOfMonth);
                startEditTastActivity.putExtra("Year", year);
                startEditTastActivity.putExtra("TastTitle", selectedItem);
                startEditTastActivity.putExtra("TaskID", selectedItemID);

                startActivity(startEditTastActivity);
            }
        });

        //TODO Start "Add Activity" view when button clicked
        buttonAddNewTask = (Button) findViewById(R.id.button_addTask);
        buttonAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to start Day View Activity
                Intent startAddTaskActivity = new Intent(getApplicationContext(), AddTask.class);

                startAddTaskActivity.putExtra("Month", monthName);
                startAddTaskActivity.putExtra("Day", dayOfMonth);
                startAddTaskActivity.putExtra("Year", year);

                startActivity(startAddTaskActivity);
            }
        });

    }


    @Override
    protected void onResume(){
        super.onResume();
        //ReadFromDatabseAndUpdateListToReflectAnyChanges();
    }

    //@Override
    /*
    public void onBackPressed(){
        super.onBackPressed();
        //Notify the user that the information is being updated
        Context context = getApplicationContext();
        CharSequence text = "Back button was pressed";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //Intent startMainActivityActivity = new Intent(getApplicationContext(), MainActivity.class);
        Intent openMainActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(startMainActivityActivity);
    }*/

    private void ReadFromDatabseAndUpdateListToReflectAnyChanges(){
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
        String[] mySelectionArgs = {String.valueOf(year), String.valueOf(dayOfMonth), monthName};

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

        taskTitles.clear(); //Make sure the list is clear
        listOfTasks.clear(); //Make sure the list is clear
        while (myCursor.moveToNext()){
            taskTitle =
                    myCursor.getString(myCursor.getColumnIndexOrThrow(DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_TITLE));
            taskTitles.add(taskTitle);
            int currentTaskID = myCursor.getInt(myCursor.getColumnIndexOrThrow(DayTasksContract.DayTasksContent._ID));

            Task currentTask = new Task(currentTaskID);
            listOfTasks.add(currentTask);
        }

        myCursor.close();

        //---
        //In this section I am learning how to populate the List View
        //taskTitles.set(0,"This is a very very long string and I am going to see what happens when it is added to the list.");
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,taskTitles);
        //myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(myAdapter);

        /*
        Context context = getApplicationContext();
        CharSequence text = Integer.toString(myCursor.getCount());
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        */
    }


}
