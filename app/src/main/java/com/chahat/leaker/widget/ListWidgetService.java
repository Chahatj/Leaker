package com.chahat.leaker.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.chahat.leaker.MainActivity;
import com.chahat.leaker.R;
import com.chahat.leaker.data.NewsContract;
import com.chahat.leaker.object.NewsObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by chahat on 11/8/17.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this);
    }

    public class ListRemoteViewFactory implements RemoteViewsFactory{

        final Context mContext;
        Cursor mCursor;

        public ListRemoteViewFactory(Context context){
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            if (mCursor!=null) mCursor.close();

            mCursor = mContext.getContentResolver().query(NewsContract.NewsDetailEntry.CONTENT_URI,null,
                    null,null,null);
        }

        @Override
        public void onDestroy() {
            if (mCursor!=null) mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor!=null) return mCursor.getCount();
            else return 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {

            if (mCursor==null || mCursor.getCount()==0) return null;

            mCursor.moveToPosition(i);

            NewsObject newsObject = new NewsObject();
            newsObject.setAuthor(mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_AUTHOR)));
            newsObject.setTitle(mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_TITLE)));
            newsObject.setDescription(mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_DESCRIPTION)));
            newsObject.setUrl(mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_URL)));
            newsObject.setUrlImage(mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_URL_IMAGE)));
            newsObject.setPublishedAt(mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_PUBLISHED)));

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.news_item);

            remoteViews.setTextViewText(R.id.new_title_textView,newsObject.getTitle());
            try {
                Bitmap b = Picasso.with(mContext).load(newsObject.getUrlImage()).get();
                remoteViews.setImageViewBitmap(R.id.news_image_view, b);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.putExtra(MainActivity.INTENT_OBJECT,newsObject);
            remoteViews.setOnClickFillInIntent(R.id.news_container,intent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
