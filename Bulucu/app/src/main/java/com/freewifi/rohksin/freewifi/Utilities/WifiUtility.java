package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.freewifi.rohksin.freewifi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RohitKsingh on 5/6/2017.
 */
public class WifiUtility {

    private static WifiConfiguration configuration;
    private static WifiManager singletonWifiManager;

    private static List<ScanResult> allScanResults;
    private static List<ScanResult> openScans;
    private static List<ScanResult> closeScans;


    public static WifiManager getSingletonWifiManager(Context context)
    {
        if(singletonWifiManager !=null)
        {
            return singletonWifiManager;
        }
        else {
            singletonWifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return singletonWifiManager;
        }
    }


    public static void connect(Context context, ScanResult scanResult)
    {

        singletonWifiManager = getSingletonWifiManager(context);
        configuration = new WifiConfiguration();
        configuration.SSID = scanResult.SSID;
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int networkId = singletonWifiManager.addNetwork(configuration);

        if(!singletonWifiManager.isWifiEnabled())
            singletonWifiManager.setWifiEnabled(true);

        singletonWifiManager.disconnect();
        singletonWifiManager.enableNetwork(networkId,true);
        singletonWifiManager.reconnect();
    }


    public static List<ScanResult> getOpenScanResult(List<ScanResult> allScan)
    {
        filterScan(allScan);
        return openScans;
    }

    public static List<ScanResult> getCloseScanResult(List<ScanResult> allScan)
    {
        filterScan(allScan);
        return closeScans;
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

    public static ScanResult getThisWifi(ScanResult scanResult, List<ScanResult> scanResults)
    {
        String ssid = scanResult.SSID;

        for(ScanResult listItem : scanResults)
        {
            if(listItem.SSID.equals(ssid))
            {
                return listItem;
            }
        }

        return null;
    }


    public static String getWifiStrengthStatus(int level)
    {
        if(level==0)
            return AppUtility.getString(R.string.signal_lost);
        else if(level <= 5)
            return AppUtility.getString(R.string.weak_signal);
        else if(level > 5 && level <=10)
            return AppUtility.getString(R.string.fair_signal);
        else if(level > 10 && level <=15)
            return AppUtility.getString(R.string.good_signal);
        else
            return AppUtility.getString(R.string.excellent_signal);
    }


    //**************************************************************************************************//
    //                                 Private Helper Methods                                           //
    //**************************************************************************************************//

    private static void filterScan(List<ScanResult> scanResult)
    {
        allScanResults = scanResult;
        openScans = new ArrayList<ScanResult>();
        closeScans = new ArrayList<ScanResult>();

        for(ScanResult result : allScanResults)
        {
            if(!isProtectedNetwork(result.capabilities))
            {
                openScans.add(result);
            }
            else {
                closeScans.add(result);
            }
        }

    }

    private static boolean isProtectedNetwork(String capability)
    {
        return (capability.contains("WPA") || capability.contains("WEP") || capability.contains("WPS"));
    }

}
