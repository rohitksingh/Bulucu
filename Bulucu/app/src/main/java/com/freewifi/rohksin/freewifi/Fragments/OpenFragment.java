package com.freewifi.rohksin.freewifi.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freewifi.rohksin.freewifi.R;

/**
 * Created by Illuminati on 2/19/2018.
 */

public class OpenFragment extends Fragment {


    public OpenFragment getInstance()
    {
        OpenFragment fragment = new OpenFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.test, parent, false);
        return view;
    }

}
