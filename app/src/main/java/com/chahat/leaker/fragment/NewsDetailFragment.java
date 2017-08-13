package com.chahat.leaker.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chahat.leaker.MainActivity;
import com.chahat.leaker.R;
import com.chahat.leaker.data.NewsContract;
import com.chahat.leaker.object.NewsObject;
import com.squareup.picasso.Picasso;

/**
 * Created by chahat on 10/8/17.
 */

public class NewsDetailFragment extends Fragment {

    private NewsObject newsObject;
    private ImageView imageView;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private FloatingActionButton fab;
    private ImageView imageViewDownload;

    public static NewsDetailFragment newInstance(NewsObject newsObject){

        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.INTENT_OBJECT,newsObject);
        newsDetailFragment.setArguments(bundle);

        return newsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null){
            newsObject = (NewsObject) savedInstanceState.getSerializable("NewsObject");
        }else {
            newsObject = (NewsObject) getArguments().getSerializable(MainActivity.INTENT_OBJECT);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("NewsObject",newsObject);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_detail,container,false);

        imageView = (ImageView) view.findViewById(R.id.news_image_view);
        textViewTitle = (TextView) view.findViewById(R.id.news_title_textView);
        textViewDescription = (TextView) view.findViewById(R.id.news_description_textView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        imageViewDownload = (ImageView) view.findViewById(R.id.imageViewDownload);

        Cursor cursor = getContext().getContentResolver().query(NewsContract.FavoriteNewsDetailEntry.FAVORITE_CONTENT_URI,null,
                NewsContract.FavoriteNewsDetailEntry.COLUMN_AUTHOR + " = ?" + " AND " +
                        NewsContract.FavoriteNewsDetailEntry.COLUMN_TITLE + "= ?"+ " AND " +
                        NewsContract.FavoriteNewsDetailEntry.COLUMN_DESCRIPTION + "= ?"+ " AND " +
                        NewsContract.FavoriteNewsDetailEntry.COLUMN_URL + "= ?" + " AND " +
                        NewsContract.FavoriteNewsDetailEntry.COLUMN_URL_IMAGE + "= ?" + " AND " +
                        NewsContract.FavoriteNewsDetailEntry.COLUMN_PUBLISHED + "= ?",
                new String[]{newsObject.getAuthor(),newsObject.getTitle(),newsObject.getDescription(),newsObject.getUrl(),newsObject.getUrlImage(),newsObject.getPublishedAt()},null,null);

        if (cursor!=null){
            if (cursor.getCount()>0){
                imageViewDownload.setSelected(true);
            }else {
                imageViewDownload.setSelected(false);
            }
        }
        cursor.close();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(newsObject.getDescription())
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        imageViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageViewDownload.isSelected()){
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_news_remove);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel_textView);
                    TextView textViewRemove = (TextView) dialog.findViewById(R.id.remove_textView);
                    dialog.show();

                    textViewCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    textViewRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getContext().getContentResolver().delete(NewsContract.FavoriteNewsDetailEntry.FAVORITE_CONTENT_URI,
                                    NewsContract.FavoriteNewsDetailEntry.COLUMN_AUTHOR + "= ?" + " AND " +
                                    NewsContract.FavoriteNewsDetailEntry.COLUMN_TITLE + "= ?" + " AND " +
                                            NewsContract.FavoriteNewsDetailEntry.COLUMN_DESCRIPTION + "= ?"+ " AND " +
                                            NewsContract.FavoriteNewsDetailEntry.COLUMN_URL + "= ?" + " AND " +
                                            NewsContract.FavoriteNewsDetailEntry.COLUMN_URL_IMAGE + "= ?" + " AND " +
                                            NewsContract.FavoriteNewsDetailEntry.COLUMN_PUBLISHED + "= ?",
                                    new String[]{newsObject.getAuthor(),newsObject.getTitle(),newsObject.getDescription(),newsObject.getUrl(),newsObject.getUrlImage(),newsObject.getPublishedAt()});
                            dialog.dismiss();
                            imageViewDownload.setSelected(false);
                            Toast.makeText(getContext(),"Romoved from saved news",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.FavoriteNewsDetailEntry.COLUMN_AUTHOR,newsObject.getAuthor());
                    contentValues.put(NewsContract.FavoriteNewsDetailEntry.COLUMN_TITLE,newsObject.getTitle());
                    contentValues.put(NewsContract.FavoriteNewsDetailEntry.COLUMN_DESCRIPTION,newsObject.getDescription());
                    contentValues.put(NewsContract.FavoriteNewsDetailEntry.COLUMN_URL,newsObject.getUrl());
                    contentValues.put(NewsContract.FavoriteNewsDetailEntry.COLUMN_URL_IMAGE,newsObject.getUrlImage());
                    contentValues.put(NewsContract.FavoriteNewsDetailEntry.COLUMN_PUBLISHED,newsObject.getPublishedAt());

                    getContext().getContentResolver().insert(NewsContract.FavoriteNewsDetailEntry.FAVORITE_CONTENT_URI,contentValues);
                    imageViewDownload.setSelected(true);
                    Toast.makeText(getContext(),"Saved successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean value = sharedPreferences.getBoolean(getString(R.string.pref_check_image),true);
        if (value) {
            Picasso.with(getContext()).load(newsObject.getUrlImage()).into(imageView);
        }
        textViewTitle.setText(newsObject.getTitle());
        textViewDescription.setText(newsObject.getDescription());

        return view;
    }
}
