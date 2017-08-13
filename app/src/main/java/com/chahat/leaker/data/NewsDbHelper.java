package com.chahat.leaker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chahat on 10/8/17.
 */

class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;

    public NewsDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String NEWS_TABLE_CREATE = "CREATE TABLE " + NewsContract.NewsDetailEntry.NEWS_TABLE_NAME + " ( " +
                NewsContract.NewsDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsContract.NewsDetailEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                NewsContract.NewsDetailEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsContract.NewsDetailEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.NewsDetailEntry.COLUMN_URL + " TEXT NOT NULL, " +
                NewsContract.NewsDetailEntry.COLUMN_URL_IMAGE + " TEXT NOT NULL, " +
                NewsContract.NewsDetailEntry.COLUMN_PUBLISHED + " TEXT NOT NULL " +
                " );";

        String FAVORITE_NEWS_TABLE_CREATE = "CREATE TABLE " + NewsContract.FavoriteNewsDetailEntry.FAVORITE_NEWS_TABLE_NAME + " ( " +
                NewsContract.FavoriteNewsDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsContract.FavoriteNewsDetailEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                NewsContract.FavoriteNewsDetailEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsContract.FavoriteNewsDetailEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.FavoriteNewsDetailEntry.COLUMN_URL + " TEXT NOT NULL, " +
                NewsContract.FavoriteNewsDetailEntry.COLUMN_URL_IMAGE + " TEXT NOT NULL, " +
                NewsContract.FavoriteNewsDetailEntry.COLUMN_PUBLISHED + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(NEWS_TABLE_CREATE);
        sqLiteDatabase.execSQL(FAVORITE_NEWS_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
