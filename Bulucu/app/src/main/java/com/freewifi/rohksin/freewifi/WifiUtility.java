package com.freewifi.rohksin.freewifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * Created by Illuminati on 5/6/2017.
 */
public class WifiUtility {

    private static WifiManager manager;
    private static WifiConfiguration configuration;
    private static ScanResult inspectWifi;


    public static void connect(Context context, ScanResult scanResult)
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


    public static void setInspectWifi(ScanResult scanResult)
    {
        inspectWifi = scanResult;
    }

    public static ScanResult getInspectWifi()
    {

        // Temp until a POJO is created
        // set null after return One-Tiime Usage only

        return inspectWifi;
    }


    public static String getWifiDetail(ScanResult scanResult)
    {
        String details = "WIFI NAME :" + scanResult.SSID +"\n"+
                "BSSD :" + scanResult.BSSID +"\n"+
                "capability :" + scanResult.capabilities +"\n"+
                "Freq :"+scanResult.frequency+"\n"+
                "Freq0 :"+scanResult.centerFreq0+"\n"+
                "Freq1 :"+scanResult.centerFreq1+"\n"+
                "Channel Width :"+scanResult.channelWidth+"\n"+
                "Desc Content :"+scanResult.describeContents()+"\n"+
                "Level :"+scanResult.level+"\n"
                ;

        return details;
    }





}
