package com.chahat.leaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();

        if (!intent.hasExtra(Intent.EXTRA_TEXT)){
            if (sharedPreferences.contains(getString(R.string.sharedPreference_country)) && sharedPreferences.contains(getString(R.string.sharedPreference_language))
                    && sharedPreferences.contains(getString(R.string.news_source_select_id))){
                Intent intent1 = new Intent(this,MainActivity.class);
                startActivity(intent1);
                finish();
            }
        }
    }
}
