package com.example.fa_kaushilprajapati_c0826985_android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table places(id INTEGER primary key AUTOINCREMENT,address TEXT,Latitude Text, Longitude Text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists places");
        onCreate(sqLiteDatabase);
    }

    public DBHelper(Context context) {
        super(context, "favouritePlaces", null, 1);
    }

    public long insertPlaces(String address, Double Latitude, Double Longitude) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("address", address);
        contentValues.put("Latitude", Latitude);
        contentValues.put("Longitude", Longitude);
        long result = sqLiteDatabase.insert("places", null, contentValues);
        sqLiteDatabase.close();

        return result;

//        if (result == -1) {
//            return false;
//        } else
//            return true;
    }

    public int updatePlaces(PlacesModel places, String address, Double Latitude,Double Longitude) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("Latitude", Latitude);
        contentValues.put("Longitude", Longitude);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from places where id = ?", new String[]{address});


            return sqLiteDatabase.update("places", contentValues, "id=?", new String[]{String.valueOf(places.getId())});


    }

    public Boolean deletePlaces(String address) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from places where address = ?", new String[]{address});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.delete("places", "address=?", new String[]{address});
            sqLiteDatabase.close();
            cursor.close();
            if (result == -1) {
                return false;
            } else
                return true;
        } else
            return false;

    }

    public Cursor getData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from places ", null);
        return cursor;
    }

    @SuppressLint("Range")
    public ArrayList<PlacesModel> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<PlacesModel> myRes = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * From places", null);

        if (cursor.moveToFirst()){
            do {
               // PlacesModel placesModel = new PlacesModel(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("address")), cursor.getDouble(cursor.getColumnIndex("Latitude")),cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    PlacesModel placesModel = new PlacesModel();
                    placesModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    placesModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    placesModel.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    placesModel.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                myRes.add(placesModel);
            } while (cursor.moveToNext());
        }
        return myRes;
    }
}

