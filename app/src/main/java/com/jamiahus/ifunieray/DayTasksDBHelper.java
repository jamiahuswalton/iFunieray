package com.jamiahus.ifunieray;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jamia on 2/24/2018.
 */

public class DayTasksDBHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DayTask.db";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + DayTasksContract.DayTasksContent.TABLE_NAME + " (" +
                    DayTasksContract.DayTasksContent._ID + " INTEGER PRIMARY KEY," +
                    DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_TITLE + " TEXT," +
                    DayTasksContract.DayTasksContent.COLUMN_NAME_TASK_DESCRIPTION + " TEXT," +
                    //version 2
                    DayTasksContract.DayTasksContent.COLUMN_NAME_MONTH_TASK + " TEXT, " +
                    DayTasksContract.DayTasksContent.COLUMN_NAME_DAY_OF_MONTH_TASK + " INTEGER," +
                    DayTasksContract.DayTasksContent.COLUMN_NAME_YEAR_TASK + " INTEGER " + ")";


    public DayTasksDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
        SQLiteDatabase dbRead = getReadableDatabase();
        Log.d("DATABASE", "Constructor was called");
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        Log.d("DATABASE", "onCreate was called. String: " + TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        Log.d("DATABASE", "The onUpgrade method was launched.");
        if (oldVersion == 1){
            db.execSQL("ALTER TABLE " + DayTasksContract.DayTasksContent.TABLE_NAME + " ADD " +
                    DayTasksContract.DayTasksContent.COLUMN_NAME_MONTH_TASK + " TEXT");
            db.execSQL("ALTER TABLE " + DayTasksContract.DayTasksContent.TABLE_NAME + " ADD " +
                    DayTasksContract.DayTasksContent.COLUMN_NAME_DAY_OF_MONTH_TASK + " INTEGER");
            db.execSQL("ALTER TABLE " + DayTasksContract.DayTasksContent.TABLE_NAME + " ADD " +
                    DayTasksContract.DayTasksContent.COLUMN_NAME_YEAR_TASK + " INTEGER");
        } else if (newVersion == 2){
            db.execSQL(TABLE_CREATE);
            onCreate(db);
        }
        /*
        db.execSQL(TABLE_CREATE);
        onCreate(db);
        */
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DATABASE", "The onDowngrade method was launched.");
        onUpgrade(db, oldVersion, newVersion);
    }
}
