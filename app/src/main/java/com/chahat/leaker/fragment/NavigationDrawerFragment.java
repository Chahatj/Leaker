package com.chahat.leaker.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chahat.leaker.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by chahat on 9/8/17.
 */

public class NavigationDrawerFragment extends Fragment implements View.OnClickListener{

    private TextView myFavoriteTextView;
    private TextView languageTextView;
    private TextView settingTextView;
    private TextView aboutTextView;
    private TextView newsTextView;
    private OnItemClickListner mCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSION_CHECK = 1;
    private TextView textViewLocation;
    private static final String SAVEINSTANCE_LOCATION = "Location";

    public static NavigationDrawerFragment newInstance() {
        return new NavigationDrawerFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_favorite_textView:
                mCallback.onItemClick(R.id.my_favorite_textView);
                myFavoriteTextView.setSelected(true);
                languageTextView.setSelected(false);
                settingTextView.setSelected(false);
                aboutTextView.setSelected(false);
                newsTextView.setSelected(false);
                break;
            case R.id.language_textView:
                mCallback.onItemClick(R.id.language_textView);
                myFavoriteTextView.setSelected(false);
                languageTextView.setSelected(true);
                settingTextView.setSelected(false);
                aboutTextView.setSelected(false);
                newsTextView.setSelected(false);
                break;
            case R.id.setting_textView:
                mCallback.onItemClick(R.id.setting_textView);
                myFavoriteTextView.setSelected(false);
                languageTextView.setSelected(false);
                settingTextView.setSelected(true);
                aboutTextView.setSelected(false);
                newsTextView.setSelected(false);
                break;
            case R.id.about_textView:
                mCallback.onItemClick(R.id.about_textView);
                myFavoriteTextView.setSelected(false);
                languageTextView.setSelected(false);
                settingTextView.setSelected(false);
                aboutTextView.setSelected(true);
                newsTextView.setSelected(false);
                break;
            case R.id.news_textView:
                mCallback.onItemClick(R.id.news_textView);
                myFavoriteTextView.setSelected(false);
                languageTextView.setSelected(false);
                settingTextView.setSelected(false);
                aboutTextView.setSelected(false);
                newsTextView.setSelected(true);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnItemClickListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnListItemClickListener");
        }
    }

    public interface OnItemClickListner {
        void onItemClick(int id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.navigation_drawer_fragment, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        textViewLocation = (TextView) view.findViewById(R.id.textViewLocation);

        if (savedInstanceState != null) {
            textViewLocation.setText(savedInstanceState.getString(SAVEINSTANCE_LOCATION));
        }

        getLocation();

        myFavoriteTextView = (TextView) view.findViewById(R.id.my_favorite_textView);
        myFavoriteTextView.setOnClickListener(this);
        languageTextView = (TextView) view.findViewById(R.id.language_textView);
        languageTextView.setOnClickListener(this);
        settingTextView = (TextView) view.findViewById(R.id.setting_textView);
        settingTextView.setOnClickListener(this);
        aboutTextView = (TextView) view.findViewById(R.id.about_textView);
        aboutTextView.setOnClickListener(this);
        newsTextView = (TextView) view.findViewById(R.id.news_textView);
        newsTextView.setOnClickListener(this);

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CHECK: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                } else {

                    Toast.makeText(getContext(), R.string.permission_required, Toast.LENGTH_SHORT).show();
                    textViewLocation.setVisibility(View.GONE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_CHECK);

        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    String cityName = addresses.get(0).getAddressLine(0);
                                    String stateName = addresses.get(0).getAddressLine(1);
                                    String countryName = addresses.get(0).getAddressLine(2);
                                    textViewLocation.setText(cityName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    textViewLocation.setVisibility(View.GONE);
                                }

                            }else {
                                textViewLocation.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVEINSTANCE_LOCATION, textViewLocation.getText().toString());
        super.onSaveInstanceState(outState);
    }

}
