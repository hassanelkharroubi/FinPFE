package com.example.onmyway.Models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;


public class DestinationDB extends SQLiteOpenHelper {

    public DestinationDB(@Nullable Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creer tableau
        String CREATE_DESTINATION_TABLE = "CREATE TABLE IF NOT EXISTS " + Utils.TABLE_NAME + " (" +
                Utils.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Utils.LATITUDE + " VARCHAR(30)," +
                Utils.LONGITUDE + " VARCHAR(30))";

        db.execSQL(CREATE_DESTINATION_TABLE);

    }

    public LatLng getDestination() {
        SQLiteDatabase database = this.getReadableDatabase();

        String destination = "SELECT * FROM " + Utils.TABLE_NAME;
        LatLng latLng = null;

        Cursor cursor = database.rawQuery(destination, null);

        if (cursor.moveToFirst()) {
            do {


                latLng = new LatLng(cursor.getDouble(cursor.getColumnIndex(Utils.LATITUDE)), cursor.getDouble(cursor.getColumnIndex(Utils.LONGITUDE)));

            } while (cursor.moveToNext());
        }
        database.close();
        cursor.close();
        return latLng;
    }//end of getDestination()

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Utils.TABLE_NAME);
        onCreate(db);
    }

    public void addDestination(LatLng latLng) {

        SQLiteDatabase database = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.LATITUDE, latLng.latitude);
        contentValues.put(Utils.LONGITUDE, latLng.longitude);
        database.insert(Utils.TABLE_NAME, null, contentValues);
        database.close();
    }

    private class Utils {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "DESTINATIONDB";
        private static final String TABLE_NAME = "DESTINATION";

        private static final String KEY_ID = "id";
        private static final String LATITUDE = "latitude";
        private static final String LONGITUDE = "longitude";

    }




    public void deleteDestination(){

        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(Utils.TABLE_NAME,null, null);
        Log.i("deleteLocation","delete all geoppoint");

        database.close();
    }//end of deleteDestination;


}
