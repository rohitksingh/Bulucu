package com.freewifi.rohksin.freewifi.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Activities.HomePageActivity;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;

public class NotifyMeService extends Service implements WifiScanInterface{

    /**
        TODO Dynamically register BroadcastReceiver
     */

    private int FOREGROUND_ID = 2222;

    private WifiManager wifiManager;
    private WifiScanReceiver wifiScanReceiver;

    private static final String TAG = "NotifyMeService";


    @Override
    public void onCreate()
    {
        startScan();
    }


    @Override
    public int onStartCommand(Intent intent, int flag, int flagId)
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();

        startForeground(FOREGROUND_ID,notification);     //<-- Makes Foreground

        for(int i=0;i<10;i++)
        {
            Log.d(TAG, "onHandleIntent: "+i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stopSelf();
        return flag;
    }




    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy: ");
        stopScan();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager){
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    @Override
    public void startScan() {

        wifiScanReceiver = new WifiScanReceiver();
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager = WifiUtility.getSingletonWifiManager(this);
        wifiManager.startScan();
        wifiScanReceiver = new WifiScanReceiver();

    }

    @Override
    public void stopScan() {

        unregisterReceiver(wifiScanReceiver);
    }

    @Override
    public void updateScanUI() {

    }


    private class WifiScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {

                Log.d(TAG, "onReceive: ");
                wifiManager.startScan();                                                  // Contineous scan

            }

        }
    }


    private void notifyUser()
    {

    }

    private boolean newResultFound()
    {
        return true;
    }


}
