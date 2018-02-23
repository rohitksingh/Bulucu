package com.freewifi.rohksin.freewifi.Fragments;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.OpenWifiListAdapter;
import com.freewifi.rohksin.freewifi.Adapters.StringAdapter;
import com.freewifi.rohksin.freewifi.R;

import java.security.PublicKey;
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
        View view = inflater.inflate(R.layout.scan_list_layout, parent, false);
        //textView = (TextView)view.findViewById(R.id.testText);

        rv = (RecyclerView)view.findViewById(R.id.rv);
        llm = new LinearLayoutManager(context);
        adapter = new OpenWifiListAdapter(context, new ArrayList<ScanResult>());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        return view;
    }


    public void setUpList(List<ScanResult> list)
    {

        adapter = new OpenWifiListAdapter(context, list);
        rv.setAdapter(adapter);
    }



}
