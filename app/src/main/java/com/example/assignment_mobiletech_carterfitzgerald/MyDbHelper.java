package com.example.assignment_mobiletech_carterfitzgerald;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "EventNames";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";


    public MyDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME + "(" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_NAME + " text" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " +
                TABLE_NAME);
        onCreate(db);
    }

    public long create(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                COLUMN_NAME,name);
        long id = db.insert(
                TABLE_NAME, null, contentValues);
        return id;
    }
    @SuppressLint("Range")
    public ArrayList<String> readAll() {
        ArrayList<String> all = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " +
                TABLE_NAME, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex(
                    COLUMN_NAME));
            cursor.moveToNext();
            all.add("Name: " + name);
        }
        return all;
    }
    public void update(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.update(
                TABLE_NAME, contentValues, "name = ? ", new String[]{name});
    }
    public void delete(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(
                TABLE_NAME, "name = ? ", new String[]{name});
    }

    public String getName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;

        // Define a projection that specifies which columns from the database you will actually use after this query
        String[] projection = {COLUMN_NAME};

        // Filter results WHERE "id" = '1'
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {"1"};

        String sortOrder = null; // You can specify the sorting order here if needed

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,   // The array of columns to return (pass null to get all)
                selection,    // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null,         // don't group the rows
                null,         // don't filter by row groups
                sortOrder     // The sort order
        );

        // Extract data from the cursor
        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            cursor.close();
            cursor.close();
        }

        return name;
    }

    public void updateOrInsertActivityNameInDatabase(String activityName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, activityName);

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{"1"});
        if (cursor != null && cursor.getCount() > 0) {
            // Database is not empty, update existing entry
            db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{"1"});
        } else {
            // Database is empty, create a new entry
            db.insert(TABLE_NAME, null, contentValues);
        }

        // Close cursor
        if (cursor != null) {
            cursor.close();
        }
    }
}
