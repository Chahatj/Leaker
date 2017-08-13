package com.chahat.leaker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chahat.leaker.fragment.AboutUsFragment;
import com.chahat.leaker.fragment.MainFragment;
import com.chahat.leaker.fragment.MyFavoriteFragment;
import com.chahat.leaker.fragment.NavigationDrawerFragment;
import com.chahat.leaker.fragment.NewsDetailFragment;
import com.chahat.leaker.fragment.SettingFragment;
import com.chahat.leaker.object.NewsObject;
import com.chahat.leaker.utils.ReminderUtilities;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentClickListner, NavigationDrawerFragment.OnItemClickListner, MyFavoriteFragment.MyFavoriteClickListner
        , SharedPreferences.OnSharedPreferenceChangeListener {

    private DrawerLayout drawerLayout;
    private boolean isPhone;
    public static final String INTENT_OBJECT = "NewsObject";
    private static final String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);


        isPhone = getResources().getBoolean(R.bool.isPhone);
        // setup drawer view
        if (isPhone){
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.master_fragment_container);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return true;
                }
            });

            // setup menu icon
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        if (savedInstanceState==null){
            FragmentManager fragmentManager = getSupportFragmentManager();

            NavigationDrawerFragment masterFragment = NavigationDrawerFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.master_fragment_container, masterFragment,"NavigationDrawerFragment")
                    .commit();

            if (getIntent().hasExtra(INTENT_OBJECT)){
                NewsObject newsObject = (NewsObject) getIntent().getSerializableExtra(INTENT_OBJECT);
                NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(newsObject);
                fragmentManager.beginTransaction().add(R.id.detail_fragment_container,newsDetailFragment).commit();
            }else {
                MainFragment mainFragment = MainFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_fragment_container,mainFragment,null)
                        .commit();
            }
        }

        ReminderUtilities.scheduleDailyNews(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMainFragmentClick(NewsObject newsObject) {
        if (isPhone){
            Intent intent = new Intent(this,NewsDetailActivity.class);
            intent.putExtra(INTENT_OBJECT,newsObject);
            startActivity(intent);
        }else {
            NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(newsObject);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container,newsDetailFragment).commit();
        }
    }

    @Override
    public void onItemClick(int id) {
        switch (id){
            case R.id.news_textView:
                MainFragment mainFragment = new MainFragment();
                if( !(getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container) instanceof MainFragment) ) {
                    FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.detail_fragment_container,mainFragment);
                    trans.commit();
                }
                if (isPhone){
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.my_favorite_textView:
                MyFavoriteFragment myFavoriteFragment = MyFavoriteFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,myFavoriteFragment
                        ,null).commit();
                if (isPhone){
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.language_textView:
                Intent intent = new Intent(this,LanguageActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT,TAG);
                startActivity(intent);
                if (isPhone){
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.about_textView:
                if (isPhone){
                    Intent intent1 = new Intent(this,AboutUsActivity.class);
                    startActivity(intent1);
                    if (isPhone){
                        drawerLayout.closeDrawers();
                    }
                }else {
                    AboutUsFragment aboutUsFragment = AboutUsFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,aboutUsFragment,null).commit();
                }
                break;
            case R.id.setting_textView:
                if (isPhone){
                    Intent intent1 = new Intent(this,SettingActivity.class);
                    startActivity(intent1);
                    if (isPhone){
                        drawerLayout.closeDrawers();
                    }
                }else {
                    SettingFragment settingFragment = new SettingFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,settingFragment,null).commit();
                }
        }
    }

    @Override
    public void onMyFavoriteClick(NewsObject newsObject) {
        if (isPhone){
            Intent intent = new Intent(this,NewsDetailActivity.class);
            intent.putExtra(MainActivity.INTENT_OBJECT,newsObject);
            startActivity(intent);
        }else {
            NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(newsObject);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container,newsDetailFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (isPhone){
            if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

}
