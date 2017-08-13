package com.chahat.leaker.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.chahat.leaker.R;

/**
 * Created by chahat on 10/8/17.
 */

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_pref);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        PreferenceManager preferenceManager = getPreferenceManager();
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_check_image));

        if (preferenceManager.getSharedPreferences().getBoolean(getString(R.string.pref_check_image), true)){
            checkBoxPreference.setChecked(true);
        } else {
            checkBoxPreference.setChecked(false);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Preference p = findPreference(s);
        if (p!=null){

                boolean value = p.getSharedPreferences().getBoolean(p.getKey(),true);
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) p;
                if (value){
                    checkBoxPreference.setChecked(true);
                }else {
                    checkBoxPreference.setChecked(false);
                }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
