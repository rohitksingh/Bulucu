package com.freewifi.rohksin.freewifi.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Illuminati on 2/17/2018.
 */

public class HomePageActivity extends AppCompatActivity{


    private TextView openNetwork;
    private TextView closedNetwork;
    private TextView openNum;
    private TextView closeNum;
    private TextView scanNow;
    private FrameLayout scan;
    private FrameLayout openWifiContainer;
    private FrameLayout closeWifiContainer;

    private WifiManager manager;
    private List<ScanResult> allScanResults;
    private List<ScanResult> openScanResults;
    private List<ScanResult> closeScanResults;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_scan_activity_layout);

        manager = WifiUtility.getSingletonWifiManager(this);
        registerReceiver(new WifiScanReceiver(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        manager.startScan();

        setUpUI();

    }


    private void setUpUI()
    {
        openNetwork = (TextView)findViewById(R.id.open);
        closedNetwork = (TextView)findViewById(R.id.close);
        openNum = (TextView)findViewById(R.id.openNum);
        closeNum = (TextView)findViewById(R.id.closeNum);
        openWifiContainer = (FrameLayout)findViewById(R.id.openContainer);
        closeWifiContainer = (FrameLayout)findViewById(R.id.closeContainer);
        scanNow = (TextView)findViewById(R.id.scanNow);


        addIntoView(R.id.openContainer,"Open networks", "Click to see list of open networks around you");


        scan = (FrameLayout)findViewById(R.id.scan);


        openWifiContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, WifiListActivity.class);
                intent.setAction("OPEN_NETWORK");
                startActivity(intent);

            }
        });


        closeWifiContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, WifiListActivity.class);
                intent.setAction("CLOSE_NETWORK");
                startActivity(intent);
            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, ScanSurroundingActivity.class));
            }
        });
    }



    //************************************************************************************************************//
    //                                   BroadcastReceiver                                                        //
    //************************************************************************************************************//

    private class WifiScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {

                manager.startScan();                                                  // Contineous scan
                scanNow.setText(getNumOfWifi()+"");
                if(openScanResults!=null)
                openNum.setText(openScanResults.size()+"");
                if(closeScanResults!=null)
                closeNum.setText(closeScanResults.size()+"");
                setUpAllList();
            }

        }
    }

    //*************************************************************************************************************//
    //                                            Helper Methods                                                   //
    //*************************************************************************************************************//


    private int getNumOfWifi()
    {
        return manager.getScanResults().size();
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
            openNetwork.setText(openScanResults.get(0).SSID);

        }else {
            openNetwork.setText("NO Network");
        }

        if(closeScanResults.size()!=0)
        {
            ScanResult scan = closeScanResults.get(0);
            closedNetwork.setText(scan.SSID+"");

        }
        else {
            closedNetwork.setText("NO NETWORK");
        }


    }

    private boolean isProtectedNetwork(String capability)
    {
        return (capability.contains("WPA") || capability.contains("WEP") || capability.contains("WPS"));
    }

    private void addIntoView(int targetId, String msg, String desc)
    {
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(targetId), msg, desc)
                        // All options below are optional
                        .outerCircleColor(android.R.color.holo_red_dark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(android.R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(android.R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(android.R.color.holo_red_dark)  // Specify the color of the description text
                        .textColor(android.R.color.holo_blue_dark)            // Specify a color for both the title and description text
                        .dimColor(android.R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        // doSomething();
                    }
                });

    }



   //**************************************************************************************************************//
    //                         Activity Life cycle methods   && Runtime Permission                                                    //
    //*************************************************************************************************************//


    @Override
    public void onResume()
    {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }


}

