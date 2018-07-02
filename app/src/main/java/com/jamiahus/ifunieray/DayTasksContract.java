package com.jamiahus.ifunieray;

import android.provider.BaseColumns;

/**
 * Created by jamia on 2/24/2018.
 */

public final class DayTasksContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DayTasksContract (){};
    /*
    private static final String SQL_CREAT_TASK_TABLE = "CREATE TABLE " + DayTasksContent.TABLE_NAME + " (" +
            DayTasksContent._ID + " INTEGER PRIMARY KEY," + DayTasksContent.COLUMN_NAME_TASK_TITLE + " TEXT," +
            DayTasksContent.COLUMN_NAME_TASK_DESCRIPTION + " TEXT)";
    */

    /* TODO Need to add more columns to a task. For exmaple, time range for the task to take place.
        I am thinking that we would have the user add specific how the day would be broken down
        (e.g., in increments of 30 minutes).
    */
    /* Inner class that defines the table contents */
    public static class DayTasksContent implements BaseColumns{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TASK_TITLE = "title";
        public static final String COLUMN_NAME_TASK_DESCRIPTION = "Task_Description";
        //Version 2
        public static final String COLUMN_NAME_MONTH_TASK = "Task_Month";
        public static final String COLUMN_NAME_DAY_OF_MONTH_TASK = "Task_Day_Of_Month";
        public static final String COLUMN_NAME_YEAR_TASK = "Task_Year";
    }
}
