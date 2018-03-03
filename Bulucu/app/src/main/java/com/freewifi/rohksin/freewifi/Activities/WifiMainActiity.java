package com.freewifi.rohksin.freewifi.Activities;

import android.animation.Animator;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TabHost;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.CallbackListeners.ListItemListener;
import com.freewifi.rohksin.freewifi.Fragments.CloseFragment;
import com.freewifi.rohksin.freewifi.Fragments.OpenFragment;
import com.freewifi.rohksin.freewifi.Fragments.OpenWifiListFragment;
import com.freewifi.rohksin.freewifi.Fragments.WifiDetailFragment;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Testing.MyFragment;
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
    private TextView openNum;
    private TextView closeNum;
    private TextView scanNow;
    private TextView scan;



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

        openNetwork = (TextView)findViewById(R.id.open);
        closedNetwork = (TextView)findViewById(R.id.close);
        openNum = (TextView)findViewById(R.id.openNum);
        closeNum = (TextView)findViewById(R.id.closeNum);

        openNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(WifiMainActiity.this, OpenWifiActivity.class);
                intent.setAction("OPEN_NETWORK");
                startActivity(intent);



            }
        });

        closedNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WifiMainActiity.this, OpenWifiActivity.class);
                intent.setAction("CLOSE_NETWORK");
                startActivity(intent);
            }
        });


        scanNow = (TextView)findViewById(R.id.scanNow);
        scan = (TextView)findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.startScan();
                scanNow.setText(getNumOfWifi()+"");


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
                scanNow.setText(getNumOfWifi()+"");
                if(openScanResults!=null)
                openNum.setText(openScanResults.size()+"");
                if(closeScanResults!=null)
                closeNum.setText(closeScanResults.size()+"");
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


}









