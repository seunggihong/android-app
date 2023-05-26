package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "grade_db";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TableInfo.TABLE_NAME + " (" +
                    TableInfo.COLUMN_NAME_CREDIT + " INTEGER," +
                    TableInfo.COLUMN_NAME_GRADE + " INTEGER," +
                    TableInfo.COLUMN_NAME_SUBJECT + " TEXT," +
                    TableInfo.COLUMN_NAME_SCORE + " TEXT," +
                    TableInfo.COLUMN_NAME_MULTIPLE + " REAL) ";

    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TableInfo.TABLE_NAME;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
