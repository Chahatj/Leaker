package com.chahat.leaker;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chahat.leaker.fragment.NewsSourceFragment;

public class NewsSourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_source);

        FragmentManager fragmentManager = getSupportFragmentManager();

        NewsSourceFragment newsSourceFragment = NewsSourceFragment.newInstance();

        fragmentManager.beginTransaction().add(R.id.news_source_container,newsSourceFragment,null).commit();
    }
}
