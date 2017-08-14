package com.chahat.leaker;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chahat.leaker.fragment.NewsDetailFragment;
import com.chahat.leaker.object.NewsObject;

public class NewsDetailActivity extends AppCompatActivity {

    private NewsObject newsObject;
    private static final String SAVEINSTANCE_NEWSOBJECT = "NewsObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewUp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (savedInstanceState!=null){
            newsObject =(NewsObject) savedInstanceState.getSerializable(SAVEINSTANCE_NEWSOBJECT);
        }
        if (savedInstanceState==null){
            newsObject = (NewsObject) getIntent().getSerializableExtra(MainActivity.INTENT_OBJECT);
            NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(newsObject);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.news_detail_container,newsDetailFragment).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable(SAVEINSTANCE_NEWSOBJECT,newsObject);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
