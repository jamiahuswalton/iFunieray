package com.jamiahus.ifunieray;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jamia on 1/20/2018.
 */

public class ItineraryData {
    public static final String DEBUG_TAG = "ItineraryData";

    private SQLiteDatabase db;
    private SQLiteOpenHelper itineraryDbHelper;

    private static final String[] ALL_COLUMNS = {
            ItineraryDBHelper.COLUMN_ID,
            ItineraryDBHelper.COLUMN_NAME
    };

    public  ItineraryData (Context context){
        this.itineraryDbHelper = new ItineraryDBHelper(context);
    }

    public void open(){
        db = itineraryDbHelper.getWritableDatabase();
    }

    public void close(){
        if (itineraryDbHelper != null){
            itineraryDbHelper.close();
        }
    }
}
