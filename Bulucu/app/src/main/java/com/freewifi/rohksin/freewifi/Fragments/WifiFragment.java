package com.freewifi.rohksin.freewifi.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Illuminati on 5/7/2017.
 */
public abstract class WifiFragment extends Fragment {

    public WifiFragment newInstance()
    {
        Bundle bundle = new Bundle();
        WifiFragment fragment = provideYourFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View view = provideYourView(inflater,parent,savedInstanceState);
        return view;
    }

    public abstract View provideYourView(LayoutInflater inflater,ViewGroup group,Bundle savedInstanceState);
    public abstract WifiFragment provideYourFragment();

}
