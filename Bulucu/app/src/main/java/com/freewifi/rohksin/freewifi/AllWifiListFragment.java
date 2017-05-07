package com.freewifi.rohksin.freewifi;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Illuminati on 5/7/2017.
 */

public class AllWifiListFragment extends WifiFragment {



    @Override
    public View provideYourView(LayoutInflater inflater , ViewGroup parent ,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_main,parent,false);

        RelativeLayout layout = (RelativeLayout)view;
        RecyclerView recyclerView = (RecyclerView)layout.findViewById(R.id.rv);

        Log.d("rohit", "inProvideYourView " + WifiPagerActivity.scanResults);

        if(WifiPagerActivity.scanResults!=null)
        {
            WifiListAdapter adapter = new WifiListAdapter(getContext(),WifiPagerActivity.scanResults);
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

        return new AllWifiListFragment();
    }

}
