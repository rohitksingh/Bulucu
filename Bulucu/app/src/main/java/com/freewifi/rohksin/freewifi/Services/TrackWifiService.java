package com.freewifi.rohksin.freewifi.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.List;

/**
 * Created by RohitKsingh on 3/3/2018.
 */

public class TrackWifiService extends IntentService {


    private WifiManager wifiManager;
    private ScanResult targetScan;

    public TrackWifiService()
    {
        super("estService");
        wifiManager = WifiUtility.getSingletonWifiManager(TrackWifiService.this);

        Log.d("NAME", "service started");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //ScanResult scanResult

        targetScan = intent.getParcelableExtra("SCAN_RESULT");

        Log.d("NAME", targetScan.SSID);

        registerReceiver(new ScanResultReceiver(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        while (true)
        {

        }


    }




    public class ScanResultReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                List<ScanResult> scanResults = wifiManager.getScanResults();

                ScanResult scanResult = WifiUtility.getThisWifi(targetScan, scanResults);


                Intent sendLevel = new Intent();
                sendLevel.setAction("LEVEL");

                if(scanResult!=null) {
                    sendLevel.putExtra( "LEVELDATA", scanResult.level);
                }else {
                    sendLevel.putExtra( "LEVELDATA", -1);
                }

                sendBroadcast(sendLevel);
            }


        }
    }


}
