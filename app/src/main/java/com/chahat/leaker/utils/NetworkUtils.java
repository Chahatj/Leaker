package com.chahat.leaker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.chahat.leaker.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by chahat on 9/8/17.
 */

public class NetworkUtils {

    private static final String SOURCE_URL = "https://newsapi.org/v1/sources";
    private static final String ARTICLE_URL = "https://newsapi.org/v1/articles";
    private static final String LANGUAGE_QUERY_PARAM = "language";
    private static final String COUNTRY_QUERY_PARAM = "country";
    private static final String CHANNEL_QUERY_PARAM = "source";
    private static final String APIKEY_QUERY_PARAM = "apiKey";

    public static URL builtSourceURL(String language,String country){

        Uri uri = Uri.parse(SOURCE_URL).buildUpon().
                appendQueryParameter(LANGUAGE_QUERY_PARAM,language).
                appendQueryParameter(COUNTRY_QUERY_PARAM,country).
                build();

        URL url = null;

        try {
            url = new URL(uri.toString());
            Log.d("NetworkUtils",url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL builtArticleURL(Context context){

        String apiKey = context.getString(R.string.News_API_Key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String channelId = sharedPreferences.getString(context.getString(R.string.news_source_select_id),null);

        Uri uri = Uri.parse(ARTICLE_URL).buildUpon().
                appendQueryParameter(CHANNEL_QUERY_PARAM,channelId).
                appendQueryParameter(APIKEY_QUERY_PARAM,apiKey).
                build();

        URL url = null;

        try {
            url = new URL(uri.toString());
            Log.d("NetworkUtils",url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasNext = scanner.hasNext();
            if (hasNext) {
                return scanner.next();
            } else {
                return null;
            }
        }finally {
            httpURLConnection.disconnect();
        }

    }


}
