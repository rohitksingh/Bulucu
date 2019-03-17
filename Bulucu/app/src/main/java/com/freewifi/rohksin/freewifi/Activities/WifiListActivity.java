package com.freewifi.rohksin.freewifi.Activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.CloseWifiListAdapter;
import com.freewifi.rohksin.freewifi.Adapters.OpenWifiListAdapter;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.List;

/**
 * Created by RohitKsingh on 2/24/2018.
 */

public class WifiListActivity extends AppCompatActivity implements WifiScanInterface{


    private TextView noNetworkFound;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private RecyclerView.Adapter adapter;

    private WifiManager manager;
    private ScanReceiver scanReceiver;

    private Intent intent;

    private static final String TAG = "WIFI_LIST_ACTIVITY_TAG";

    @Override
    public void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.wifi_list_activity_layout);
        intent = getIntent();
        setBackGroundColor(intent.getIntExtra("BG_COLOR",0));


        noNetworkFound = (TextView)findViewById(R.id.noNetworkAvailable);

        rv = (RecyclerView)findViewById(R.id.rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setPadding(0,getStatusBarHeight(),0,0);


    }



    /**************************************************************************************************
    *                               Private Helper Methods                                            *
    //************************************************************************************************/


    private void setUpAdapters()
    {
        List<ScanResult> scanResults = null;

        if(intent.getAction().equals("OPEN_NETWORK"))
        {
            scanResults = WifiUtility.getOpenScanResult(manager.getScanResults());
            adapter = new OpenWifiListAdapter(this, scanResults);

            Log.d(TAG, "OPEN: "+scanResults.size());
        }
        else if(intent.getAction().equals("CLOSE_NETWORK")){

            scanResults = WifiUtility.getCloseScanResult(manager.getScanResults());
            adapter = new CloseWifiListAdapter(this, WifiUtility.getCloseScanResult(manager.getScanResults()));

            Log.d(TAG, "CLOSED: "+scanResults.size());
        }

        manageListVisibility(scanResults);

    }

    private void manageListVisibility(List<ScanResult> scanResults)
    {
        if(scanResults.size()!=0) {

            noNetworkFound.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(adapter);
        }
        else{
            rv.setVisibility(View.GONE);
            noNetworkFound.setVisibility(View.VISIBLE);
            noNetworkFound.setText(R.string.no_network_available);
        }
    }



    private int getStatusBarHeight()
    {
        int height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId>0)
        {
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }


    private void setBackGroundColor(int color)
    {
        Log.d("Color", color+"");
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(getResources().getColor(color));
    }


    /***********************************************************************************************************
     *                                  Interface methods                                                      *
     ***********************************************************************************************************/

    @Override
    public void startScan() {

        scanReceiver = new ScanReceiver();
        registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        manager = WifiUtility.getSingletonWifiManager(this);
        manager.startScan();
    }

    @Override
    public void stopScan() {
       unregisterReceiver(scanReceiver);
    }

    @Override
    public void updateScanUI() {

    }


    /***********************************************************************************************************
     *                                  Activity Lifecycle methods                                             *
     ***********************************************************************************************************/

    @Override
    public void onResume()
    {
        super.onResume();
        startScan();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        stopScan();
    }



    /***********************************************************************************************************
     *                                  BroadcastReceiver                                                      *
     ************************************************************************************************************/

    class ScanReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                setUpAdapters();
                manager.startScan();
            }
        }
    }




}