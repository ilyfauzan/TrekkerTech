package com.example.trekkertech.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "wishlist.db";
    private static final String TABLE_WISHLIST = "wishlist";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_IMAGE = "image";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WISHLIST_TABLE = "CREATE TABLE " + TABLE_WISHLIST + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_COUNTRY + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_WISHLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
        onCreate(db);
    }

    public void addDestination(Destination destination, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, destination.getName());
        values.put(COLUMN_COUNTRY, destination.getCountry());
        values.put(COLUMN_IMAGE, destination.getImage());
        values.put(COLUMN_USERNAME, username);
        db.insert(TABLE_WISHLIST, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<Destination> getAllDestinations(String username) {
        List<Destination> destinations = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WISHLIST, null, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Destination destination = new Destination();
                destination.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                destination.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                destination.setCountry(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY)));
                destination.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                destinations.add(destination);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return destinations;
    }
    public void deleteDestination(Destination destination) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WISHLIST, COLUMN_ID + " = ?", new String[]{String.valueOf(destination.getId())});
        db.close();
    }
    public boolean isDestinationInWishlist(Destination destination, String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WISHLIST + " WHERE " + COLUMN_NAME + " = ? AND " + COLUMN_COUNTRY + " = ? AND " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{destination.getName(), destination.getCountry(), username});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

}
