package com.edunext.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.edunext.model.SchoolModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String LOGTAG = DatabaseManager.class.getSimpleName();



    /*
     * Saving complete list into table
     */
    public static void savingCompleteSchoolData(Context context, List<SchoolModel> schoolDataList) {
        ArrayList<ContentValues> cvList = new ArrayList<>();
        for (int i = 0; i < schoolDataList.size(); i++) {
            ContentValues schoolItem = new ContentValues();
            schoolItem.put(SchoolTable.Cols.ID, schoolDataList.get(i).getId());
            schoolItem.put(SchoolTable.Cols.SCHOOL_CODE, schoolDataList.get(i).getSchool_code());
            schoolItem.put(SchoolTable.Cols.URL, schoolDataList.get(i).getUrl());
            schoolItem.put(SchoolTable.Cols.ADDED_ON, schoolDataList.get(i).getAddedon());
            schoolItem.put(SchoolTable.Cols.STATUS, schoolDataList.get(i).getStatus());
            cvList.add(schoolItem);
        }
        context.getContentResolver().bulkInsert(SchoolTable.URI, cvList.toArray(new ContentValues[cvList.size()]));
    }

    /**
     * Save or Update data
     * @param context - context of activity

     */
    public static void saveOrUpdateSchoolData(Context context,  List<SchoolModel> schoolDataList) {

        if(schoolDataList!=null && schoolDataList.size()>0) {
            for (SchoolModel schoolModel : schoolDataList) {

                ContentResolver resolver;
                resolver = context.getContentResolver();
                ContentValues values = prepareData(schoolModel);

                Cursor cursor;
                String condition = SchoolTable.Cols.ID + "= '" + schoolModel.getId() + "'";
                cursor = context.getContentResolver().query(SchoolTable.URI, null, condition, null, null);

                if (cursor != null && cursor.getCount() != 0) {
                    resolver.update(SchoolTable.URI, values, condition, null);
                } else {
                    resolver.insert(SchoolTable.URI, values);
                }
            }
        }
    }

    private static ContentValues prepareData(SchoolModel dataModel) {
        ContentValues values = new ContentValues();

        values.put(SchoolTable.Cols.ID, dataModel.getId());

        if (dataModel.getSchool_code() != null && dataModel.getSchool_code().length() > 0)
            values.put(SchoolTable.Cols.SCHOOL_CODE, dataModel.getSchool_code());

        if (dataModel.getAddedon() != null && dataModel.getAddedon().length() > 0)
            values.put(SchoolTable.Cols.ADDED_ON, dataModel.getAddedon());

        if (dataModel.getUrl() != null && dataModel.getUrl().length() > 0)
            values.put(SchoolTable.Cols.URL, dataModel.getUrl());

        if (dataModel.getStatus() != null && dataModel.getStatus().length() > 0)
            values.put(SchoolTable.Cols.STATUS, dataModel.getStatus());


        return values;
    }



    /*
     * fetching data from Table
     */
    public static List<SchoolModel> getSchoolData(Activity context) {
        Cursor cursor;
        cursor = context.getContentResolver().query(SchoolTable.URI, null, null, null, null);
        List<SchoolModel> schoolDataList = new ArrayList<>();
        if (cursor != null && !cursor.isClosed()) {
            while (cursor.moveToNext()) {
                schoolDataList.add(fetchSchoolDataFromCursor(cursor));
            }
            cursor.close();
        }

        return schoolDataList;
    }

    private static SchoolModel fetchSchoolDataFromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(SchoolTable.Cols.ID));
        String school_code = cursor.getString(cursor.getColumnIndex(SchoolTable.Cols.SCHOOL_CODE));
        String addedOn = cursor.getString(cursor.getColumnIndex(SchoolTable.Cols.ADDED_ON));
        String status = cursor.getString(cursor.getColumnIndex(SchoolTable.Cols.STATUS));
        String url = cursor.getString(cursor.getColumnIndex(SchoolTable.Cols.URL));
        return new SchoolModel(id,school_code,url,addedOn,status);
    }



}
