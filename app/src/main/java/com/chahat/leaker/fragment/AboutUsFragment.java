package com.chahat.leaker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chahat.leaker.R;

/**
 * Created by chahat on 10/8/17.
 */

public class AboutUsFragment extends Fragment {

    public static AboutUsFragment newInstance(){
        return new AboutUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_aboutus,container,false);
    }
}
