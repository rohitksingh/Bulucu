package com.freewifi.rohksin.freewifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView wifiListView;
    private  WifiListAdapter adapter;
    private LinearLayoutManager llm;
    private WifiManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wifiListView = (RecyclerView)findViewById(R.id.rv);
        manager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        WifiList receiver = new WifiList();
        registerReceiver(receiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        manager.startScan();


    }


    public class WifiList extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //check intent
            //Do work

            List<ScanResult> wifiList;


            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                wifiList = new ArrayList<>();

                List<ScanResult> openNetwork =  new ArrayList<ScanResult>();

                List<ScanResult> scanResults = manager.getScanResults();

                wifiList = new ArrayList<ScanResult>();

                for(ScanResult result : scanResults)
                {
                    //ScanResult wifi = new Wifi();
                    //String capability = result.capabilities;
                    if(!isProtectedNetwork(result.capabilities))
                    {
                        openNetwork.add(result);
                    }


                   // wifi.setName(result.SSID);
                  //  wifi.setType(result.capabilities);
                   // wifiList.add(wifi);
                }

                adapter = new WifiListAdapter(MainActivity.this,null);
                llm = new LinearLayoutManager(MainActivity.this);
                wifiListView.setLayoutManager(llm);
                wifiListView.setAdapter(adapter);
            }

        }
    }

    public boolean isProtectedNetwork(String capability)
    {
        return (capability.contains("WPA") || capability.contains("WEP") || capability.contains("WPS"));
    }


}