package com.freewifi.rohksin.freewifi.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Fragments.OpenFragment;
import com.freewifi.rohksin.freewifi.Fragments.OpenWifiListFragment;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.WifiUtility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Illuminati on 2/17/2018.
 */

public class WifiMainActiity extends AppCompatActivity{


    private TextView openNetwork;
    private TextView closedNetwork;
    private TextView scanNow;
    private TextView scan;

    private FragmentManager fragmentManager;

    private WifiManager manager;
    private List<ScanResult> allScanResults;
    private List<ScanResult> openScanResults;
    private List<ScanResult> closeScanResults;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_main_activity);


        registerReceiver(new WifiScanReceiver(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        manager = WifiUtility.getSingletonWifiManager(this);
        //allScanResults = new ArrayList<ScanResult>();



        fragmentManager = getSupportFragmentManager();


        openNetwork = (TextView)findViewById(R.id.open);
        closedNetwork = (TextView)findViewById(R.id.close);

        openNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction()
                        .add(R.id.parentPanel, new OpenFragment().getInstance())
                        .commit();

            }
        });


        scanNow = (TextView)findViewById(R.id.scanNow);
        scan = (TextView)findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.startScan();
                scanNow.setText("Result: "+getNumOfWifi());

            }
        });


        scanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WifiMainActiity.this, ScanSurrounding.class));
            }
        });



    }





    private int getNumOfWifi()
    {
        return manager.getScanResults().size();
    }



    private class WifiScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                Log.d("Scan","XXX");
                manager.startScan();// Contineous scan
                scanNow.setText("Result: "+getNumOfWifi());
                setUpAllList();
            }

        }
    }


    private void setUpAllList()
    {
        allScanResults = manager.getScanResults();
        openScanResults = new ArrayList<ScanResult>();
        closeScanResults = new ArrayList<ScanResult>();


        for(ScanResult result : allScanResults)
        {
            if(!isProtectedNetwork(result.capabilities))
            {
                openScanResults.add(result);
            }
            else {
                Log.d("CLOSE", result.SSID);
                closeScanResults.add(result);
            }
        }

        if(openScanResults.size()!=0)
        {
            openNetwork.setText(openScanResults.size()+"+");
        }else {
            openNetwork.setText("NO Network");
        }

        if(closeScanResults.size()!=0)
        {
             ScanResult scan = closeScanResults.get(0);

            if(scan == null)
            {
                Log.d("ISNULL", "YES");
            }
            else {
                Log.d("ISNOTNULL", "NO");
            }

            closedNetwork.setText(scan.SSID+"");
            Log.d("SIZE", closeScanResults.size()+" ");
        }
        else {
            closedNetwork.setText("NO NETWORK");
        }


    }

    private boolean isProtectedNetwork(String capability)
    {
        return (capability.contains("WPA") || capability.contains("WEP") || capability.contains("WPS"));
    }








}





