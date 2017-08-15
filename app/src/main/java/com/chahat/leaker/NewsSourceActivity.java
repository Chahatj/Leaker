package com.chahat.leaker;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chahat.leaker.fragment.LanguageFragment;
import com.chahat.leaker.fragment.NewsSourceFragment;

public class NewsSourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_source);

        Intent intent = getIntent();

        if (intent!=null){
            if (intent.hasExtra(LanguageFragment.INTENT_COUNTRY) && intent.hasExtra(LanguageFragment.INTENT_LANGUAGE)){
                String language = intent.getStringExtra(LanguageFragment.INTENT_LANGUAGE);
                String country = intent.getStringExtra(LanguageFragment.INTENT_COUNTRY);

                FragmentManager fragmentManager = getSupportFragmentManager();

                NewsSourceFragment newsSourceFragment = NewsSourceFragment.newInstance(language,country);

                fragmentManager.beginTransaction().add(R.id.news_source_container,newsSourceFragment,null).commit();
            }
        }


    }
}
