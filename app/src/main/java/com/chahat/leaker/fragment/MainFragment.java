package com.chahat.leaker.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chahat.leaker.R;
import com.chahat.leaker.adapter.NewsAdapter;
import com.chahat.leaker.data.NewsContract;
import com.chahat.leaker.object.NewsObject;
import com.chahat.leaker.utils.JSONUtils;
import com.chahat.leaker.utils.NetworkUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 9/8/17.
 */

public class MainFragment extends Fragment implements NewsAdapter.NewsItemClickListner, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private final int LOADER_ID = 2;
    private final int NETWORK_LOADER_ID = 3;
    private MainFragmentClickListner mCallback;
    private Parcelable mRecyclerState;
    private LinearLayout emptyView;
    private ProgressBar progressBar;
    private static final String SAVEINSTANCE_RECYCLERSTATE = "RecyclerState";


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MainFragmentClickListner) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnListItemClickListener");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Cursor cursor = getContext().getContentResolver().query(NewsContract.NewsDetailEntry.CONTENT_URI,null,null,null,null);

        if (cursor!=null){
            if (cursor.getCount()>0){
                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null,newsLoader);
            }else {
                getActivity().getSupportLoaderManager().restartLoader(NETWORK_LOADER_ID,null,networkNewsLoader);
            }
        }

        cursor.close();
    }

    public interface MainFragmentClickListner{
        void onMainFragmentClick(NewsObject newsObject);
    }

    @Override
    public void onStart() {
        super.onStart();
        Cursor cursor = getContext().getContentResolver().query(NewsContract.NewsDetailEntry.CONTENT_URI,null,null,null,null);

        if (cursor!=null){
            if (cursor.getCount()>0){
                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null,newsLoader);
            }else {
                getActivity().getSupportLoaderManager().restartLoader(NETWORK_LOADER_ID,null,networkNewsLoader);
            }
        }
        cursor.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState!=null){
            mRecyclerState = savedInstanceState.getParcelable(SAVEINSTANCE_RECYCLERSTATE);
        }

        View view = inflater.inflate(R.layout.fragment_main,container,false);

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsRecyclerView.setLayoutManager(layoutManager);
        emptyView = (LinearLayout) view.findViewById(R.id.empty_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        newsAdapter = new NewsAdapter(getContext(),this);
        newsRecyclerView.setAdapter(newsAdapter);

        Cursor cursor = getContext().getContentResolver().query(NewsContract.NewsDetailEntry.CONTENT_URI,null,null,null,null);

        if (cursor!=null){
            if (cursor.getCount()>0){
                getActivity().getSupportLoaderManager().initLoader(LOADER_ID,null,newsLoader);
            }else {
                getActivity().getSupportLoaderManager().initLoader(NETWORK_LOADER_ID,null,networkNewsLoader);
            }
        }
        cursor.close();

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

        return view;
    }


    private final LoaderManager.LoaderCallbacks<List<NewsObject>> networkNewsLoader = new LoaderManager.LoaderCallbacks<List<NewsObject>>() {
        @Override
        public Loader<List<NewsObject>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<NewsObject>>(getContext()) {

                final List<NewsObject> list = null;

                @Override
                protected void onStartLoading() {

                    if (list!=null){
                        deliverResult(list);
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public List<NewsObject> loadInBackground() {

                    URL url = NetworkUtils.builtArticleURL(getContext());
                    try {
                        String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                        return JSONUtils.getNewsArticle(jsonResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<NewsObject>> loader, List<NewsObject> data) {

            if (data!=null){
                showData();
                for (int i = 0; i < data.size(); i++) {

                    NewsObject newsObject = data.get(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_AUTHOR, newsObject.getAuthor());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_TITLE, newsObject.getTitle());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_DESCRIPTION, newsObject.getDescription());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_URL, newsObject.getUrl());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_URL_IMAGE, newsObject.getUrlImage());
                    contentValues.put(NewsContract.NewsDetailEntry.COLUMN_PUBLISHED, newsObject.getPublishedAt());

                    try {
                        getActivity().getContentResolver().insert(NewsContract.NewsDetailEntry.CONTENT_URI, contentValues);
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }

                }
                newsAdapter.setmList(data);
                if (mRecyclerState!=null){
                    newsRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerState);
                }
            }else {
                showError();
            }

        }

        @Override
        public void onLoaderReset(Loader<List<NewsObject>> loader) {

        }
    };

    private void showData(){
        progressBar.setVisibility(View.GONE);
        emptyView.setVisibility(ViewPager.GONE);
        newsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(){
        progressBar.setVisibility(View.GONE);
        newsRecyclerView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private final LoaderManager.LoaderCallbacks<Cursor> newsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(getContext()) {

                Cursor cursor=null;

                @Override
                protected void onStartLoading() {

                    if (cursor!=null){
                        deliverResult(cursor);
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public void deliverResult(Cursor data) {
                    super.deliverResult(data);
                    cursor = data;
                }

                @Override
                public Cursor loadInBackground() {

                    return getContext().getContentResolver().query(NewsContract.NewsDetailEntry.CONTENT_URI,null,null,null,null,null);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data!=null){
                if (data.getCount()>0){
                    showData();
                    List<NewsObject> list = new ArrayList<>();
                    while (data.moveToNext()){
                        NewsObject newsObject = new NewsObject();
                        newsObject.setAuthor(data.getString(data.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_AUTHOR)));
                        newsObject.setTitle(data.getString(data.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_TITLE)));
                        newsObject.setDescription(data.getString(data.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_DESCRIPTION)));
                        newsObject.setUrl(data.getString(data.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_URL)));
                        newsObject.setUrlImage(data.getString(data.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_URL_IMAGE)));
                        newsObject.setPublishedAt(data.getString(data.getColumnIndex(NewsContract.NewsDetailEntry.COLUMN_PUBLISHED)));
                        list.add(newsObject);
                    }
                    newsAdapter.setmList(list);
                   // newsAdapter.notifyDataSetChanged();
                    if (mRecyclerState!=null){
                        newsRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerState);
                    }
                }else {
                    showError();
                }
            }else {
                showError();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onNewsItemClick(NewsObject newsObject) {
        mCallback.onMainFragmentClick(newsObject);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mRecyclerState = newsRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SAVEINSTANCE_RECYCLERSTATE,mRecyclerState);
        super.onSaveInstanceState(outState);
    }
}
