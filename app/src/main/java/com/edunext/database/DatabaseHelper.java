package com.edunext.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.MessageFormat;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "eduNext.db";
    private static final int DB_VERSION = 1;
    private static final String KEY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS {0} ({1})";
    private static final String KEY_DELETE_TABLE = "DROP TABLE IF EXISTS {0}";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createSchoolTable(sqLiteDatabase);
    }

    @Override
    public synchronized void close() {
        super.close();
    }


    private void createSchoolTable(SQLiteDatabase db){
        String builder = SchoolTable.Cols.ID + " PRIMARY KEY, " +
                SchoolTable.Cols.SCHOOL_CODE + " TEXT, " +
                SchoolTable.Cols.URL + " TEXT, " +
                SchoolTable.Cols.ADDED_ON + " TEXT, " +
                SchoolTable.Cols.STATUS + " TEXT ";

        createTable(builder,db,SchoolTable.TABLE_NAME);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        dropTable(db, SchoolTable.TABLE_NAME);

        onCreate(db);
    }

    private void createTable(String fields, SQLiteDatabase db, String tableName) {
        String query = MessageFormat.format(DatabaseHelper.KEY_CREATE_TABLE, tableName, fields);
        db.execSQL(query);
    }
    private void dropTable(SQLiteDatabase db, String tableName) {
        String query = MessageFormat.format(DatabaseHelper.KEY_DELETE_TABLE, tableName);
        db.execSQL(query);
    }
}

