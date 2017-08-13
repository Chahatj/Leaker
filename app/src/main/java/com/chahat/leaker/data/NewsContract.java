package com.chahat.leaker.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chahat on 10/8/17.
 */

public class NewsContract {

    public static final String CONTENT_AUTHORITY = "com.chahat.leaker";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "path_news";
    public static final String PATH_FAVORITE_NEWS = "path_favorite_news";

    public static final class NewsDetailEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final String NEWS_TABLE_NAME = "NewsDetail";
        public static final String COLUMN_AUTHOR = "news_author";
        public static final String COLUMN_TITLE = "news_title";
        public static final String COLUMN_DESCRIPTION = "news_description";
        public static final String COLUMN_URL = "news_url";
        public static final String COLUMN_URL_IMAGE = "news_url_image";
        public static final String COLUMN_PUBLISHED = "news_published";
    }

    public static final class FavoriteNewsDetailEntry implements BaseColumns{

        public static final Uri FAVORITE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_NEWS).build();
        public static final String FAVORITE_NEWS_TABLE_NAME = "FavoriteNewsDetail";
        public static final String COLUMN_AUTHOR = "news_author";
        public static final String COLUMN_TITLE = "news_title";
        public static final String COLUMN_DESCRIPTION = "news_description";
        public static final String COLUMN_URL = "news_url";
        public static final String COLUMN_URL_IMAGE = "news_url_image";
        public static final String COLUMN_PUBLISHED = "news_published";
    }
}
