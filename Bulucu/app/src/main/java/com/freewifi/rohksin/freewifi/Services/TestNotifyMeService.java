package com.freewifi.rohksin.freewifi.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeCallback;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.Models.WifiResult;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TestNotifyMeService extends Service implements WifiScanInterface{

    private static final String TAG = "TestNotifyMeService";
    private IBinder serviceBinder = new NotifyMeBinder();
    private NotifyMeCallback notifyMeCallback;
    private WifiManager wifiManager;
    private WifiScanReceiver wifiScanReceiver;

    private Set<String> allScanNames;
    private List<WifiResult> allwifiResults;

    private int SERVICE_STARTED_COUNTER=0;
    
    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate: ");
        allScanNames = new TreeSet<String>();
        allwifiResults = new ArrayList<WifiResult>();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flag, int flagId)
    {
        SERVICE_STARTED_COUNTER++;
        Log.d(TAG, "onStartCommand: ");
        startScan();
        return START_STICKY;
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d(TAG, "onBind: ");
         return serviceBinder;
    }
    
    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void startScan() {
        Log.d(TAG, "startScan: ");

        wifiScanReceiver = new WifiScanReceiver();
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager = WifiUtility.getSingletonWifiManager(this);
        wifiManager.startScan();

    }

    @Override
    public void stopScan() {
        Log.d(TAG, "stopScan: ");
        if(AppUtility.isServiceRunning(SERVICE_STARTED_COUNTER))
        unregisterReceiver(wifiScanReceiver);
    }

    @Override
    public void updateScanUI() {

    }

    public class NotifyMeBinder extends Binder{
        
        public TestNotifyMeService getService()
        {
            return TestNotifyMeService.this;
        }
    }


    public List<WifiResult> getAllwifiResults()
    {
        return allwifiResults;
    }

    public int getServiceStartedCounter()
    {
        return SERVICE_STARTED_COUNTER;
    }

    public void registerCallBack(NotifyMeCallback notifyMeCallback)
    {
        this.notifyMeCallback = notifyMeCallback;
    }


    private class WifiScanReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {

                Log.d(TAG, "onReceive: ");
                WifiUtility.updateWifiResult(wifiManager.getScanResults());
                wifiManager.startScan();                                                  // Contineous scan

                notifyUser(newResultFound(WifiUtility.getOpenScanResult()));

            }

        }
    }



    private void notifyUser(boolean newResultsFound)
    {
        Log.d(TAG, "notifyUser: "+newResultsFound + ": "+allScanNames.toString());
        if(newResultsFound) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();

            if(notifyMeCallback !=null) {
                Log.d("NOTIFY_USER_TRACK", "inside service notify user: ");
                notifyMeCallback.notifyResults(allwifiResults);

            }

        }
    }

    private boolean newResultFound(List<ScanResult> openNetworks)
    {
        boolean newResultFound = false;

        for(int i=0;i<openNetworks.size();i++)
        {
            ScanResult openNetwork = openNetworks.get(i);
            String name= openNetwork.SSID;

            if(!allScanNames.contains(name))
            {
                newResultFound = true;
                allScanNames.add(name);
                allwifiResults.add(createWifiResult(openNetwork));
            }

        }

        return newResultFound;

    }

    private WifiResult createWifiResult(ScanResult scanResult)
    {

        WifiResult wifiResult = new WifiResult();
        wifiResult.setScanResult(scanResult);
        wifiResult.setDate(AppUtility.getCurrentDate());

        return wifiResult;
    }


}
