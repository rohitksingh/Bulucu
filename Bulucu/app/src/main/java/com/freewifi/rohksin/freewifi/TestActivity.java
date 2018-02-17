package com.freewifi.rohksin.freewifi;

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
import android.widget.TextView;

import java.util.List;

/**
 * Created by Illuminati on 2/15/2018.
 */

public class TestActivity extends AppCompatActivity {

    private TextView textView;
    private WifiManager wifiManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        textView = (TextView)findViewById(R.id.testText);


        wifiManager = (WifiManager)getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        wifiManager.startScan();
        registerReceiver(new WifiScanReceiver(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


    }



    @Override
    public void onResume()
    {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},99);
            }
        }
    }

    private class WifiScanReceiver extends BroadcastReceiver{



        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                List<ScanResult> scanResults = wifiManager.getScanResults();

                Log.d("Scan Result", scanResults.size()+"");

                if(scanResults==null)
                textView.setText("Avail = "+scanResults.size());
                else
                    textView.setText("null");
            }
        }
    }





}
