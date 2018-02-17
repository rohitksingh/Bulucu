package com.freewifi.rohksin.freewifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * Created by Illuminati on 5/6/2017.
 */
public class Connect {

    private WifiManager manager;
    private WifiConfiguration configuration;

    public Connect(Context context,ScanResult scanResult)
    {
        manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        configuration = new WifiConfiguration();
        configuration.SSID = scanResult.SSID;
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int networkId = manager.addNetwork(configuration);

        if(!manager.isWifiEnabled())
            manager.setWifiEnabled(true);

        manager.disconnect();
        manager.enableNetwork(networkId,true);
        manager.reconnect();


    }


}
