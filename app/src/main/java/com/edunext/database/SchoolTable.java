package com.edunext.database;

import android.net.Uri;

public class SchoolTable  {

    public static final String TABLE_NAME= "school";
    public static final String TABLE_PATH= "school";
    public static final int PATH_TOKEN= 1;
    public static final Uri URI=ContentDescriptor.BASE_URI.buildUpon().appendPath(TABLE_PATH).build();

    public static class Cols{
        public static final String ID = "id";
        public static final String SCHOOL_CODE = "school_code";
        public static final String URL = "url";
        public static final String ADDED_ON = "addedon";
        public static final String STATUS = "status";
    }
}
