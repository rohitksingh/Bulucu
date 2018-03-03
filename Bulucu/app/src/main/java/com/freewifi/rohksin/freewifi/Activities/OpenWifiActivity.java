package com.freewifi.rohksin.freewifi.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.CloseWifiListAdapter;
import com.freewifi.rohksin.freewifi.Adapters.OpenWifiListAdapter;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.WifiUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Illuminati on 2/24/2018.
 */

public class OpenWifiActivity extends AppCompatActivity{


    private TextView textView;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private List<ScanResult> scanResults = new ArrayList<ScanResult>();


    private RecyclerView.Adapter adapter;

    private WifiManager manager;

    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceBundle)
     {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.scan_list_layout);
        textView = (TextView)findViewById(R.id.noNetworkAvailable);



        rv = (RecyclerView)findViewById(R.id.rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
         rv.setPadding(0,getStatusBarHeight(),0,0);

         intent = getIntent();


         //setUpAdapters();

         registerReceiver(new ScanReceiver(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
         manager = WifiUtility.getSingletonWifiManager(this);
         manager.startScan();

    }



    private void setUpAdapters()
    {
        List<ScanResult> scanResults = null;

        if(intent.getAction().equals("OPEN_NETWORK"))
        {
            scanResults = WifiUtility.getOpenScanResult(manager.getScanResults());
            adapter = new OpenWifiListAdapter(this, scanResults);

        }
        else if(intent.getAction().equals("CLOSE_NETWORK")){

            scanResults = WifiUtility.getCloseScanResult(manager.getScanResults());
            adapter = new CloseWifiListAdapter(this, WifiUtility.getCloseScanResult(manager.getScanResults()));
        }

        if(scanResults.size()!=0) {

            Log.d("Size", WifiUtility.getOpenScanResult(manager.getScanResults()).size()+"");
            textView.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(adapter);
        }
        else{
            rv.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("NO NETWORK AVAILABLE");
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

    class ScanReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
               // scanResults = manager.getScanResults();

                setUpAdapters();
                manager.startScan();
            }
        }
    }

}