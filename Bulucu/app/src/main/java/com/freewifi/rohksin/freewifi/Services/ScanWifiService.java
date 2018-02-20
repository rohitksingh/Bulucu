package com.freewifi.rohksin.freewifi.Services;

import android.app.IntentService;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.freewifi.rohksin.freewifi.WifiUtility;

/**
 * Created by Illuminati on 2/18/2018.
 */

public class ScanWifiService extends IntentService {

    private WifiManager manager;


    public ScanWifiService() {
        super("ScanWifi");
        manager = WifiUtility.getSingletonWifiManager(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        for(int i =0;i<10;i++)
        {
            try {
                Thread.sleep(1000);



            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Intent myIntent = new Intent();
            myIntent.setAction("ABC");
            Log.d("SEND","MyIntent");
            myIntent.putExtra("ZZZ", i);
            sendBroadcast(myIntent);

        }

    }




}
