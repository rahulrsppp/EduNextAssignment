package com.edunext.database;

import android.content.UriMatcher;
import android.net.Uri;

public class ContentDescriptor  {
    public static final String AUTHORITY="com.edunext";
    public static final Uri BASE_URI=Uri.parse("content://"+AUTHORITY);
    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, SchoolTable.TABLE_PATH, SchoolTable.PATH_TOKEN);
        return matcher;
    }



}
