package com.freewifi.rohksin.freewifi.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeCallback;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

public class TestNotifyMeService extends Service implements WifiScanInterface{

    private static final String TAG = "TestNotifyMeService";
    private IBinder serviceBinder = new NotifyMeBinder();
    private NotifyMeCallback notifyMeCallback;
    private WifiManager wifiManager;
    private WifiScanReceiver wifiScanReceiver;
    
    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate: ");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flag, int flagId)
    {
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


    public String getTestMessage()
    {
        return "TestMessage";
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

                //notifyUser(newResultFound(WifiUtility.getOpenScanResult()));

            }

        }
    }
    
}
