package com.freewifi.rohksin.freewifi.Activities;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.CloseWifiListAdapter;
import com.freewifi.rohksin.freewifi.Adapters.OpenWifiListAdapter;
import com.freewifi.rohksin.freewifi.R;

import java.util.List;

/**
 * Created by Illuminati on 2/24/2018.
 */

public class OpenWifiActivity extends AppCompatActivity{


    private TextView textView;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private List<ScanResult> scanResults;

    private OpenWifiListAdapter openWifiListAdapter;
    private CloseWifiListAdapter closeWifiListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.scan_list_layout);
        textView = (TextView)findViewById(R.id.noNetworkAvailable);
        rv = (RecyclerView)findViewById(R.id.rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        Intent intent = getIntent();

        scanResults =  intent.getParcelableArrayListExtra("LIST");

        if(intent.getAction().equals("OPEN_NETWORK"))
        {
            int listSize = intent.getIntExtra("SIZE", 0);
            textView.setText("OPEN "+ listSize);
            openWifiListAdapter = new OpenWifiListAdapter(this, scanResults);
        }
        else if(intent.getAction().equals("CLOSE_NETWORK")){

            int listSize = intent.getIntExtra("SIZE", 0);
            textView.setText("CLOSE "+ listSize);

            closeWifiListAdapter = new CloseWifiListAdapter(this, scanResults);

        }


    }
}
