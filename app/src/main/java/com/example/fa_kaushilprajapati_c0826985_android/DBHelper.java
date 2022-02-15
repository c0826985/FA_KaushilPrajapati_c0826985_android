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
        sqLiteDatabase.execSQL("Create Table places(address TEXT primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists places");
        onCreate(sqLiteDatabase);
    }

    public DBHelper(Context context) {
        super(context, "favouritePlaces", null, 1);
    }

    public boolean insertPlaces(String address) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        long result = sqLiteDatabase.insert("places", null, contentValues);
        sqLiteDatabase.close();
        if (result == -1) {
            return false;
        } else
            return true;
    }

    public Boolean updatePlaces(String address, String updateAddress) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", updateAddress);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from places where address = ?", new String[]{address});

        if (cursor.getCount() > 0) {

            long result = sqLiteDatabase.update("places", contentValues, "address=?", new String[]{updateAddress});
            sqLiteDatabase.close();
            cursor.close();
            if (result == -1) {
                return false;
            } else
                return true;
        } else
            return false;
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

    public ArrayList<PlacesModel> getAll() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<PlacesModel> myRes = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from places", null);
        if (cursor.moveToNext()) {
            do {
                myRes.add(new PlacesModel(cursor.getString(0), false));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return myRes;
    }
}

