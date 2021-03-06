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

/***********************************************************************************************
 *                                          TODO
 *    Add load Utility and load it into SplashActivity
 *
 ***********************************************************************************************/

public class WifiUtility {

    private static WifiConfiguration configuration;
    private static WifiManager singletonWifiManager;

    private static List<ScanResult> allScanResults = new ArrayList<>();
    private static List<ScanResult> openScans = new ArrayList<>();
    private static List<ScanResult> closeScans = new ArrayList<>();


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

    public static void updateWifiResult(List<ScanResult> _allScanResults)
    {
        allScanResults = _allScanResults;
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


    public static List<ScanResult> getOpenScanResult()
    {
        filterScan();
        return openScans;
    }

    public static List<ScanResult> getCloseScanResult()
    {
        filterScan();
        return closeScans;
    }


    public static String getWifiDetail(ScanResult scanResult)
    {

        String details = "WIFI NAME :" + scanResult.SSID +"\n"+
                "BSSD :" + scanResult.BSSID +"\n"+
                "capability :" + scanResult.capabilities +"\n"+
                "Freq :"+scanResult.frequency+"\n"+
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

    private static void filterScan()
    {
        allScanResults = getAllScanResults();
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


    private static List<ScanResult> getAllScanResults()
    {
        return allScanResults;
    }


}
