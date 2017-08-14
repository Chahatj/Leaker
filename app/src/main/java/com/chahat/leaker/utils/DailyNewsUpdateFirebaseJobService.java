package com.chahat.leaker.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.chahat.leaker.R;
import com.chahat.leaker.data.NewsContract;
import com.chahat.leaker.object.NewsObject;
import com.chahat.leaker.widget.NewsAppWidget;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by chahat on 10/8/17.
 */

public class DailyNewsUpdateFirebaseJobService extends JobService {

    private AsyncTask<Void,Void,List<NewsObject>> jobAsyncTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        jobAsyncTask = new AsyncTask<Void, Void, List<NewsObject>>() {
            @Override
            protected List<NewsObject> doInBackground(Void... voids) {
                Context context = DailyNewsUpdateFirebaseJobService.this;
                URL url = NetworkUtils.builtArticleURL(context);
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    return JSONUtils.getNewsArticle(jsonResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(List<NewsObject> newsObjects) {
                changeDatabase(newsObjects);
                jobFinished(job,false);
            }
        };
        jobAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (jobAsyncTask!=null){
            jobAsyncTask.cancel(true);
        }
        return true;
    }

    private void changeDatabase(List<NewsObject> list) {

        Cursor cursor = getContentResolver().query(NewsContract.NewsDetailEntry.CONTENT_URI, null, null, null, null, null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {

                getContentResolver().delete(NewsContract.NewsDetailEntry.CONTENT_URI, null, null);

                for (int i = 0; i < list.size(); i++) {

                    NewsObject newsObject = list.get(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_AUTHOR, newsObject.getAuthor());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_TITLE, newsObject.getTitle());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_DESCRIPTION, newsObject.getDescription());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_URL, newsObject.getUrl());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_URL_IMAGE, newsObject.getUrlImage());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_PUBLISHED, newsObject.getPublishedAt());

                    getContentResolver().insert(NewsContract.NewsDetailEntry.CONTENT_URI, contentValues);
                }
            } else {
                for (int i = 0; i < list.size(); i++) {

                    NewsObject newsObject = list.get(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_AUTHOR, newsObject.getAuthor());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_TITLE, newsObject.getTitle());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_DESCRIPTION, newsObject.getDescription());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_URL, newsObject.getUrl());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_URL_IMAGE, newsObject.getUrlImage());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_PUBLISHED, newsObject.getPublishedAt());

                    getContentResolver().insert(NewsContract.NewsDetailEntry.CONTENT_URI, contentValues);
                }
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(DailyNewsUpdateFirebaseJobService.this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), NewsAppWidget.class));
            //Now update all widgets
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.news_widget_listView);
        }
        cursor.close();
    }
}
