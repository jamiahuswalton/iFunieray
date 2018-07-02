package com.jamiahus.ifunieray;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jamia on 1/20/2018.
 */

public class ItineraryDBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "itineraryInformation.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_ITINERARY = "ITINERARY";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_NAME = "_NAME";

        private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ITINERARY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT" + ")";


    public ItineraryDBHelper(Context context){
        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITINERARY);
        onCreate(db);
    }
}
