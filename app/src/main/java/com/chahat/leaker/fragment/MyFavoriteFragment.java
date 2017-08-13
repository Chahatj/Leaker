package com.chahat.leaker.fragment;

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
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chahat.leaker.R;
import com.chahat.leaker.adapter.NewsAdapter;
import com.chahat.leaker.data.NewsContract;
import com.chahat.leaker.object.NewsObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 10/8/17.
 */

public class MyFavoriteFragment extends Fragment implements NewsAdapter.NewsItemClickListner,SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private boolean isPhone;
    private final int LOADER_ID = 4;
    public static final String MYFAVORITE_FRAGMENT_TAG = "myfavoritefragment";
    private MyFavoriteClickListner mCallback;
    private Parcelable mRecyclerState;
    private LinearLayout empty_layout;

    public static MyFavoriteFragment newInstance(){
        return new MyFavoriteFragment();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null,favoriteLoader);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null,favoriteLoader);
    }

    public interface MyFavoriteClickListner{
        void onMyFavoriteClick(NewsObject newsObject);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
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
            mCallback = (MyFavoriteClickListner) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnListItemClickListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState!=null){
            mRecyclerState = savedInstanceState.getParcelable("RecyclerState");
        }

        View view = inflater.inflate(R.layout.fragment_myfavorite,container,false);

        isPhone = getResources().getBoolean(R.bool.isPhone);
        empty_layout = (LinearLayout) view.findViewById(R.id.empty_layout);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_favorite_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter(getContext(),this);
        recyclerView.setAdapter(newsAdapter);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID,null,favoriteLoader);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mRecyclerState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("RecyclerState",mRecyclerState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNewsItemClick(NewsObject newsObject) {
        mCallback.onMyFavoriteClick(newsObject);
    }

    private final LoaderManager.LoaderCallbacks<Cursor> favoriteLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(getContext()) {

                Cursor cursor=null;

                @Override
                protected void onStartLoading() {
                    if (cursor!=null){
                        deliverResult(cursor);
                    }else {
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

                    return getActivity().getContentResolver().query(NewsContract.FavoriteNewsDetailEntry.FAVORITE_CONTENT_URI,
                            null,null,null,null);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data!=null){
                if (data.getCount()>0){
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
                    if (mRecyclerState!=null){
                        recyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerState);
                    }
                }else {
                    empty_layout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }

            }else {
                empty_layout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
}
