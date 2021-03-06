package com.freewifi.rohksin.freewifi.Activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.CloseWifiListAdapter;
import com.freewifi.rohksin.freewifi.Adapters.OpenWifiListAdapter;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.ArrayList;
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
    private List<ScanResult> scanResults;

    private List<ScanResult> oldResults = new ArrayList<>();

    private Intent intent;

    private static final String TAG = "WIFI_LIST_ACTIVITY_TAG";


    /***********************************************************************************************
     *                           Activity Life cycle methods                                       *
     /*********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_wifilist_layout);
        setUpUI();
    }

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

    /***********************************************************************************************
     *                                  Interface methods                                          *
     **********************************************************************************************/

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


    /***********************************************************************************************
     *                                    BroadcastReceiver                                         *
     ***********************************************************************************************/

    class ScanReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                WifiUtility.updateWifiResult(manager.getScanResults());
                setUpAdapters();
                manager.startScan();
            }
        }
    }


    /**************************************************************************************************
    *                               Private Helper Methods                                            *
    //************************************************************************************************/



    private List<ScanResult> getResult(){

        List<ScanResult> scanResults;

        if(intent.getAction().equals("OPEN_NETWORK")){
            scanResults = WifiUtility.getOpenScanResult();
        }else{
            scanResults = WifiUtility.getCloseScanResult();
        }

        return scanResults;

    }

    private void setUpAdapters()
    {

        scanResults = getResult();

        if(oldResults.size()==scanResults.size()){

            boolean nochange = true;

            for(int i=0;i<oldResults.size();i++){
                if(! AppUtility.ifSame(oldResults.get(i).SSID, scanResults.get(i).SSID)){
                    oldResults = scanResults;
                    nochange = false;
                    break;
                }
            }

            if(!nochange){
                manageListVisibility(scanResults);
            }

        }else {
            oldResults = scanResults;
            manageListVisibility(scanResults);
        }

    }

    private void manageListVisibility(List<ScanResult> scanResults)
    {

        if(intent.getAction().equals("OPEN_NETWORK")){
            adapter = new OpenWifiListAdapter(this, scanResults);
        }else{
            adapter = new CloseWifiListAdapter(this, scanResults);
        }

        if(scanResults.size()!=0) {
            noNetworkFound.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(adapter);
        }
        else{
            rv.setVisibility(View.GONE);
            noNetworkFound.setVisibility(View.VISIBLE);
        }
    }


    private void setBackGroundColor(int color)
    {
        Log.d("Color", color+"");
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(getResources().getColor(color));
    }


    private void setUpUI()
    {
        intent = getIntent();
        setBackGroundColor(intent.getIntExtra("BG_COLOR",0));

        noNetworkFound = findViewById(R.id.noNetworkAvailable);
        rv = findViewById(R.id.rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setPadding(0, AppUtility.getStatusBarHeight(),0,0);
        setUpAdapters();
    }


}