package com.edunext.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DataProvider extends ContentProvider {
    private static final String UNKNOWN_URI = "Unknown URI ";
    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper=new DatabaseHelper(getContext());
        dbHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(getContext());
        }

        // getting db in read form
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        return doQuery(db, uri, SchoolTable.TABLE_NAME, projection, selection, selectionArgs, sortOrder);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
       Uri result=null;
        if(dbHelper==null){
            dbHelper=new DatabaseHelper(getContext());
        }
        // getting db in write form
        SQLiteDatabase db= dbHelper.getWritableDatabase();

        result= doInsert(db,SchoolTable.TABLE_NAME, SchoolTable.URI,uri,contentValues);

        if (result == null) {
            System.out.println(">>>> Error Occurred");
            throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(getContext());
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return doUpdate(db, uri, SchoolTable.TABLE_NAME, selection, selectionArgs, values);
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        if(getContext()!=null) {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(getContext());
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();

            // inserting the data into table one by one
            for (ContentValues cv : values) {
                db.insert(SchoolTable.TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
            return values.length;
        }
        return -1;
    }

        /*
         * executes the query for fetching the result of search
         */
    private Cursor doQuery(SQLiteDatabase db, Uri uri, String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(getContext()!=null) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(tableName);
            Cursor result = builder.query(db, projection, selection, selectionArgs, sortOrder, null, null);
            result.setNotificationUri(getContext().getContentResolver(), uri);
            return result;
        }
        return null;
    }

    /*
     * Insert the data into Table
     */
    private Uri doInsert(SQLiteDatabase db, String tableName, Uri contentUri, Uri uri, ContentValues values) {
        if(getContext()!=null) {
            long id = db.insert(tableName, null, values);
            Uri result = contentUri.buildUpon().appendPath(String.valueOf(id)).build();
            getContext().getContentResolver().notifyChange(uri, null);
            return result;
        }
        return null;
    }

    /*
       Update the data in the table
    */
    private int doUpdate(SQLiteDatabase db, Uri uri, String tableName, String selection, String[] selectionArgs, ContentValues values) {
        if(getContext()!=null) {
            int result = db.update(tableName, values, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return result;
        }
        return -1;
    }

}
