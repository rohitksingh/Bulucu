package com.freewifi.rohksin.freewifi.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;

/**
 * Created by Illuminati on 2/22/2018.
 */

public class WifiDetailFragment extends Fragment {




    private TextView textView;

    public WifiDetailFragment getInstance()
    {
        WifiDetailFragment fragment = new WifiDetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.wifi_detail_layout,parent, false);
        textView = (TextView)view.findViewById(R.id.test);
        return view;
    }


    public void update(String value)
    {
        textView.setText(value);
    }
}
