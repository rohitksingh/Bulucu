package com.freewifi.rohksin.freewifi;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Activities.WifiDetailActivity;

/**
 * Created by Illuminati on 2/17/2018.
 */

public class WifiSignalService extends IntentService {


    public static String SIGNAL_ACTION = "com.freewifi.rohksin.freewifi.signal";
    public static String SIGNAL_STRENGTH = "com.freewifi.rohksin.freewifi.signalStrength";

    public WifiSignalService()
    {
        super("WifiSignalService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        while (true)
        {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);


            Intent resultBack = new Intent(this, WifiDetailActivity.class);
            resultBack.setAction(SIGNAL_ACTION);
            resultBack.putExtra(SIGNAL_STRENGTH, level);
            Log.d("SIG","SEND "+level);
            sendBroadcast(resultBack);
        }

    }

}
