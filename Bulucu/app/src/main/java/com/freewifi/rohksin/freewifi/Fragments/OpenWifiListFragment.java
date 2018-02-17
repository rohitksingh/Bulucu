package com.freewifi.rohksin.freewifi.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.freewifi.rohksin.freewifi.Activities.WifiPagerActivity;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Adapters.WifiListAdapter;

/**
 * Created by Illuminati on 5/7/2017.
 */
public class OpenWifiListFragment extends WifiFragment {



    @Override
    public View provideYourView(LayoutInflater inflater , ViewGroup parent ,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_main,parent,false);

        RelativeLayout layout = (RelativeLayout)view;
        RecyclerView recyclerView = (RecyclerView)layout.findViewById(R.id.rv);

        Log.d("rohit", "inProvideYourView " + WifiPagerActivity.scanResults);


        if( WifiPagerActivity.openNetwork != null && WifiPagerActivity.openNetwork.size()>0)
        {


            Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            WifiListAdapter adapter = new WifiListAdapter(getContext(),WifiPagerActivity.openNetwork);
            // WifiListAdapter adapter = new WifiListAdapter(getContext(),DataProvider.getOpenData());
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

    @Override
    public WifiFragment provideYourFragment() {
        Log.d("rohit", "inProvideYourFragment");

        return new OpenWifiListFragment();
    }

}
