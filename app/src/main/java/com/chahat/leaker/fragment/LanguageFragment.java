package com.chahat.leaker.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.chahat.leaker.NewsSourceActivity;
import com.chahat.leaker.R;

/**
 * Created by chahat on 9/8/17.
 */

public class LanguageFragment extends Fragment {

    private Spinner countrySpinner;
    private RadioGroup radioGroup;
    private RadioButton radio_english;
    private RadioButton radio_german;
    private RadioButton radio_france;
    private static final String SAVEINSTANCE_COUNTRY = "Country";
    private static final String SAVEINSTANCE_LANGUAGE = "Language";
    public static final String INTENT_LANGUAGE = "intent language";
    public static final String INTENT_COUNTRY = "intent country";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_language,container,false);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        Button buttonContinue = (Button) view.findViewById(R.id.button_continue);
        radio_english = (RadioButton) view.findViewById(R.id.radio_english);
        radio_german = (RadioButton) view.findViewById(R.id.radio_german);
        radio_france = (RadioButton) view.findViewById(R.id.radio_france);

        countrySpinner = (Spinner) view.findViewById(R.id.country_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.country_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(arrayAdapter);

        if (savedInstanceState!=null){
            int country = savedInstanceState.getInt(SAVEINSTANCE_COUNTRY);
            int language = savedInstanceState.getInt(SAVEINSTANCE_LANGUAGE);
            setSaveState(language,country);
        }

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (savedInstanceState==null){
            if (sharedPreferences.contains(getString(R.string.sharedPreference_language))&&sharedPreferences.contains(getString(R.string.sharedPreference_country))){
                setLanguageAndCountry(sharedPreferences);
            }
        }

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                String language = null;

                switch (selectedId){
                    case R.id.radio_english:
                        language = getString(R.string.english_code);
                        break;
                    case R.id.radio_german:
                        language = getString(R.string.german_code);
                        break;
                    case R.id.radio_france:
                        language = getString(R.string.france_code);
                        break;
                }

                String text=null;

                switch (countrySpinner.getSelectedItem().toString()){
                    case "India":
                        text = getString(R.string.india_code);
                        break;
                    case "Australia":
                        text = getString(R.string.australia_code);
                        break;
                    case "Germany":
                        text = getString(R.string.germany_code);
                        break;
                    case "UK":
                        text = getString(R.string.uk_code);
                        break;
                    case "US":
                        text = getString(R.string.us_code);
                        break;
                    case "Italy":
                        text = getString(R.string.italy_code);
                        break;
                }

                Intent intent = new Intent(getContext(), NewsSourceActivity.class);
                intent.putExtra(INTENT_LANGUAGE,language);
                intent.putExtra(INTENT_COUNTRY,text);
                startActivity(intent);
            }
        });


        return view;
    }

    private void setSaveState(int languagePosition, int countryPosition){
        switch (languagePosition){
            case R.id.radio_english:
                radio_english.setChecked(true);
                break;
            case R.id.radio_german:
                radio_german.setChecked(true);
                break;
            case R.id.radio_france:
                radio_france.setChecked(true);
                break;
        }
        countrySpinner.setSelection(countryPosition);

    }

    private void setLanguageAndCountry(SharedPreferences sharedPreferences){
        String language = sharedPreferences.getString(getString(R.string.sharedPreference_language),getString(R.string.english_code));
        switch (language){
            case "en":
                radio_english.setChecked(true);
                break;
            case "de":
                radio_german.setChecked(true);
                break;
            case "fr":
                radio_france.setChecked(true);
                break;
        }
        String country = sharedPreferences.getString(getString(R.string.sharedPreference_country),getString(R.string.india_code));
        switch (country){
            case "in":
                countrySpinner.setSelection(0);
                break;
            case "au":
                countrySpinner.setSelection(1);
                break;
            case "de":
                countrySpinner.setSelection(2);
                break;
            case "gb":
                countrySpinner.setSelection(3);
                break;
            case "it":
                countrySpinner.setSelection(4);
                break;
            case "us":
                countrySpinner.setSelection(5);
                break;
        }
    }

    private void editSharedPreference(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int selectedId = radioGroup.getCheckedRadioButtonId();

        switch (selectedId){
            case R.id.radio_english:
                editor.putString(getString(R.string.sharedPreference_language),getString(R.string.english_code));
                break;
            case R.id.radio_german:
                editor.putString(getString(R.string.sharedPreference_language),getString(R.string.german_code));
                break;
            case R.id.radio_france:
                editor.putString(getString(R.string.sharedPreference_language),getString(R.string.france_code));
                break;
        }

        String text=null;

        switch (countrySpinner.getSelectedItem().toString()){
            case "India":
                text = getString(R.string.india_code);
                break;
            case "Australia":
                text = getString(R.string.australia_code);
                break;
            case "Germany":
                text = getString(R.string.germany_code);
                break;
            case "UK":
                text = getString(R.string.uk_code);
                break;
            case "US":
                text = getString(R.string.us_code);
                break;
            case "Italy":
                text = getString(R.string.italy_code);
                break;
        }
        editor.putString(getString(R.string.sharedPreference_country),text);
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(SAVEINSTANCE_COUNTRY,countrySpinner.getSelectedItemPosition());
        outState.putInt(SAVEINSTANCE_LANGUAGE,radioGroup.getCheckedRadioButtonId());
        super.onSaveInstanceState(outState);
    }
}
