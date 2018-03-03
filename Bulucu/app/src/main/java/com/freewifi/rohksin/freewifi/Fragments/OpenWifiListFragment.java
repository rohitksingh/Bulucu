package com.freewifi.rohksin.freewifi.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Illuminati on 5/7/2017.
 */
public class OpenWifiListFragment extends WifiFragment {
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
*/

}
