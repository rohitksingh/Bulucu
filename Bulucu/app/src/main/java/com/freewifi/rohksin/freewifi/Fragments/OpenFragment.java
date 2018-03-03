package com.freewifi.rohksin.freewifi.Fragments;

import android.animation.Animator;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.freewifi.rohksin.freewifi.Adapters.OpenWifiListAdapter;
import com.freewifi.rohksin.freewifi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Illuminati on 2/19/2018.
 */

public class OpenFragment extends Fragment {


    private RecyclerView rv;
    private LinearLayoutManager llm;
    private OpenWifiListAdapter adapter;

    private Context context;

    private List<ScanResult> scanResults;


    View view;


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
    }



    public OpenFragment getInstance()
    {
        OpenFragment fragment = new OpenFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        //rv = (RecyclerView)
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle)
    {
        view = inflater.inflate(R.layout.wifi_list_activity_layout, parent, false);
        //textView = (TextView)view.findViewById(R.id.testText);

        rv = (RecyclerView)view.findViewById(R.id.rv);
        llm = new LinearLayoutManager(context);

        scanResults = new ArrayList<ScanResult>();

        adapter = new OpenWifiListAdapter(context, scanResults);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //setRevealAnimation(view);
            }
        });

        return view;
    }


    public void setUpList(List<ScanResult> list)
    {


       // adapter.notifyDataSetChanged();

        scanResults = list;
        //adapter.notify();

        Log.d("Scan Result", (scanResults==null)+"");

        adapter = new OpenWifiListAdapter(context, list);
        rv.setAdapter(adapter);

    }





    private void setRevealAnimation(View view )
    {

        int startRadius = 0;
        int  endRadius = (int)Math.hypot(view.getWidth(), view.getHeight());



        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, 0, 0, startRadius, endRadius);
            anim.setDuration(500);
            anim.start();
        }
    }







}
