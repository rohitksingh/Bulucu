package com.freewifi.rohksin.freewifi.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.freewifi.rohksin.freewifi.Testing.WifiPagerActivity;
import com.freewifi.rohksin.freewifi.R;

/**
 * Created by Illuminati on 5/7/2017.
 */

public class AllWifiListFragment extends WifiFragment {
    @Override
    public View provideYourView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public WifiFragment provideYourFragment() {
        return null;
    }
/*

    @Override
    public View provideYourView(LayoutInflater inflater , ViewGroup parent ,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_main,parent,false);

        RelativeLayout layout = (RelativeLayout)view;
        RecyclerView recyclerView = (RecyclerView)layout.findViewById(R.id.rv);


        if(WifiPagerActivity.scanResults!=null)
        {
            WifiListAdapter adapter = new WifiListAdapter(getContext(),WifiPagerActivity.scanResults);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

    @Override
    public WifiFragment provideYourFragment() {

        return new AllWifiListFragment();
    }

*/


}
