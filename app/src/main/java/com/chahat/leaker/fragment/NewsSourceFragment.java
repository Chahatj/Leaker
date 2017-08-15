package com.chahat.leaker.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.chahat.leaker.MainActivity;
import com.chahat.leaker.R;
import com.chahat.leaker.adapter.NewsSourceAdapter;
import com.chahat.leaker.data.NewsContract;
import com.chahat.leaker.object.NewsSourceObject;
import com.chahat.leaker.utils.JSONUtils;
import com.chahat.leaker.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by chahat on 9/8/17.
 */

public class NewsSourceFragment extends Fragment implements NewsSourceAdapter.OnNewsSourceClick{

    private RecyclerView recyclerView;
    private NewsSourceAdapter newsSourceAdapter;
    private final int LOADER_ID =1;
    private Parcelable mRecyclerState;
    private LinearLayout emptyView;
    private ProgressBar progressBar;
    private static final String SAVEINSTANCE_RECYCLERSTATE = "RecyclerViewState";
    private static final String SAVEINSTANCE_LANGUAGE = "language";
    private static final String SAVEINSTANCE_COUNTRY = "country";
    private String language,country;

    public static NewsSourceFragment newInstance(String language,String country){
        NewsSourceFragment newsSourceFragment =  new NewsSourceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LanguageFragment.INTENT_LANGUAGE,language);
        bundle.putString(LanguageFragment.INTENT_COUNTRY,country);
        newsSourceFragment.setArguments(bundle);

        return newsSourceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState!=null){
            mRecyclerState = savedInstanceState.getParcelable(SAVEINSTANCE_RECYCLERSTATE);
            language = savedInstanceState.getString(SAVEINSTANCE_LANGUAGE);
            country = savedInstanceState.getString(SAVEINSTANCE_COUNTRY);
        }else {
            language = getArguments().getString(LanguageFragment.INTENT_LANGUAGE);
            country = getArguments().getString(LanguageFragment.INTENT_COUNTRY);
        }


        View view = inflater.inflate(R.layout.fragment_news_source,container,false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_channel);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.news_source_recycler_view);
        emptyView = (LinearLayout) view.findViewById(R.id.empty_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        newsSourceAdapter = new NewsSourceAdapter(this);
        recyclerView.setAdapter(newsSourceAdapter);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID,null,newsSourceLoader);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null,newsSourceLoader);
    }

    private final LoaderManager.LoaderCallbacks<List<NewsSourceObject>> newsSourceLoader = new LoaderManager.LoaderCallbacks<List<NewsSourceObject>>() {
        @Override
        public Loader<List<NewsSourceObject>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<NewsSourceObject>>(getContext()) {

                List<NewsSourceObject> mList = null;

                @Override
                protected void onStartLoading() {

                    if (mList!=null){
                        deliverResult(mList);
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public List<NewsSourceObject> loadInBackground() {

                    URL url = NetworkUtils.builtSourceURL(language,country);

                    try {
                        String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                        return JSONUtils.getNewsSourceList(jsonResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<NewsSourceObject>> loader, List<NewsSourceObject> data) {
            if (data!=null){
                showChannel();
                newsSourceAdapter.setSourceList(data);
                if (mRecyclerState!=null){
                    recyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerState);
                }
            }else {
                showError();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<NewsSourceObject>> loader) {

        }
    };

    private void showChannel(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void showError(){
        progressBar.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void newSourceClickHandler(String channelName,String channelId) {
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(getString(R.string.sharedPreference_language),language);
        editor.putString(getString(R.string.sharedPreference_country),country);
        editor.putString(getString(R.string.selection_news_source),channelName);
        editor.putString(getString(R.string.news_source_select_id),channelId);
        editor.apply();
        getActivity().getContentResolver().delete(NewsContract.NewsDetailEntry.CONTENT_URI,null,null);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mRecyclerState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SAVEINSTANCE_RECYCLERSTATE,mRecyclerState);
        outState.putString(SAVEINSTANCE_LANGUAGE,language);
        outState.putString(SAVEINSTANCE_COUNTRY,country);
        super.onSaveInstanceState(outState);
    }
}
