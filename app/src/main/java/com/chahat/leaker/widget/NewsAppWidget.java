package com.chahat.leaker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.chahat.leaker.MainActivity;
import com.chahat.leaker.NewsDetailActivity;
import com.chahat.leaker.R;

/**
 * Implementation of App Widget functionality.
 */
public class NewsAppWidget extends AppWidgetProvider {


    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_widget);

        Intent intent = new Intent(context,ListWidgetService.class);
        views.setRemoteAdapter(R.id.news_widget_listView,intent);

        boolean isPhone = context.getResources().getBoolean(R.bool.isPhone);
        Intent appIntent = null;
        if (isPhone){
            appIntent = new Intent(context,NewsDetailActivity.class);
        }else {
            appIntent = new Intent(context,MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.news_widget_listView,pendingIntent);

        views.setEmptyView(R.id.news_widget_listView,R.id.empty_view);

        Intent intent1 = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context,0,intent1,0);
        views.setOnClickPendingIntent(R.id.empty_view,pendingIntent1);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

