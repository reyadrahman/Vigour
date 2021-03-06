package com.sp.vigour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Addhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StepsList.db";
    private static final int SCHEMA_VERSION = 2;

    public Addhelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Steps_table( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usersteps TEXT,userdate TEXT,usertime  TEXT,usercrypto TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String usersteps, String userdate,
                       String usertime, String usercrypto){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("usersteps",usersteps);
        cv.put("userdate",userdate);
        cv.put("usertime",usertime);
        cv.put("usercrypto",usercrypto);

        db.insert("Steps_table", "usersteps", cv);
        Log.d("Actlife","db inserted");

    }

    Cursor getdata() {
        String query = "Select * " + " From Steps_table";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }

    public Boolean delete(String historyID) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select *from Steps_table where _id = ?", new String[]{historyID});
        if (cursor.getCount() > 0) {
            long result = db.delete("Steps_table", "_id=?", new String[]{historyID});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
