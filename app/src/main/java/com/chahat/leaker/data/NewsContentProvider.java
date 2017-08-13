package com.chahat.leaker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by chahat on 10/8/17.
 */

public class NewsContentProvider extends ContentProvider {

    private NewsDbHelper mDbHelper;
    private static final int NEWS_CODE = 100;
    private static final int FAVORITE_NEWS_CODE = 200;

    private static final UriMatcher sUriMatcher = buildURIMatcher();

    private static UriMatcher buildURIMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = NewsContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority,NewsContract.PATH_NEWS,NEWS_CODE);
        uriMatcher.addURI(authority,NewsContract.PATH_FAVORITE_NEWS,FAVORITE_NEWS_CODE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        mDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)){
            case NEWS_CODE:
                cursor = db.query(NewsContract.NewsDetailEntry.NEWS_TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            case FAVORITE_NEWS_CODE:
                cursor = db.query(NewsContract.FavoriteNewsDetailEntry.FAVORITE_NEWS_TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Uri returnUri=null;

        switch (match){
            case NEWS_CODE:
                try {

                    long id = db.insert(NewsContract.NewsDetailEntry.NEWS_TABLE_NAME,null,contentValues);

                    if ( id > 0 ) {
                        returnUri = ContentUris.withAppendedId(NewsContract.NewsDetailEntry.CONTENT_URI, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }

                }catch (SQLiteAbortException e){
                    e.printStackTrace();
                }
                break;
            case FAVORITE_NEWS_CODE:
                try {

                    long id = db.insert(NewsContract.FavoriteNewsDetailEntry.FAVORITE_NEWS_TABLE_NAME,null,contentValues);

                    if ( id > 0 ) {
                        returnUri = ContentUris.withAppendedId(NewsContract.FavoriteNewsDetailEntry.FAVORITE_CONTENT_URI, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }

                }catch (SQLiteAbortException e){
                    e.printStackTrace();
                }
                break;
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowDeleted;

        if (s==null){
            s="1";
        }

        switch (match){
            case NEWS_CODE:
                rowDeleted = db.delete(NewsContract.NewsDetailEntry.NEWS_TABLE_NAME,s,strings);
                break;
            case FAVORITE_NEWS_CODE:
                rowDeleted = db.delete(NewsContract.FavoriteNewsDetailEntry.FAVORITE_NEWS_TABLE_NAME,s,strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        if (rowDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
