package com.example.whatsappchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SafetyDatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="whatsappsafety.db";
    public static final String TABLE_NAME="list_data";
    public static final String COL1="ID";
    public static final String COL2="ITEM1";
    public SafetyDatabaseHandler(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable="CREATE TABLE " + TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+"ITEM1 TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String a="DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(a);
        onCreate(sqLiteDatabase);
    }
    public boolean addData(String item1){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(COL2,item1);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getListContents(){
        SQLiteDatabase db=this.getWritableDatabase();
        //cursor class store the result of database done in sql query
        Cursor data=db.rawQuery("SELECT * FROM " + TABLE_NAME,null );
        return  data;
    }
}
