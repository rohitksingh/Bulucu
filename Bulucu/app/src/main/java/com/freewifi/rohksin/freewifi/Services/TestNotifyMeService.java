package com.freewifi.rohksin.freewifi.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;

public class TestNotifyMeService extends Service implements WifiScanInterface{

    private static final String TAG = "TestNotifyMeService";
    
    private IBinder serviceBinder = new NotifyMeBinder();
    
    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate: ");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flag, int flagId)
    {
        Log.d(TAG, "onStartCommand: ");
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
    }

    @Override
    public void stopScan() {
        Log.d(TAG, "stopScan: ");
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
    
}
