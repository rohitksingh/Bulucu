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
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Activities.HomePageActivity;
import com.freewifi.rohksin.freewifi.Activities.ScanSurroundingActivity;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;

public class NotifyMeService extends Service implements WifiScanInterface{

    /**
        TODO Dynamically register BroadcastReceiver
     */

    private int FOREGROUND_ID = 2222;

    private WifiManager wifiManager;
    private WifiScanReceiver wifiScanReceiver;

    private static final String TAG = "NotifyMeService";


    private List<ScanResult> oldResults;
    private List<ScanResult> newResults;


    @Override
    public void onCreate()
    {
        startScan();
        oldResults = new ArrayList<ScanResult>();
        newResults = new ArrayList<ScanResult>();
    }


    @Override
    public int onStartCommand(Intent intent, int flag, int flagId)
    {
        createNotification("Notify me");  //<-- Makes Foreground
        return flag;
    }



    private void createNotification(String msg)
    {



        Intent yesReceive = new Intent(this, ScanSurroundingActivity.class);
        yesReceive.setAction("STOP_SERVICE");


        PendingIntent pendingIntentYes = PendingIntent.getActivity(this, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);


        Notification notification = notificationBuilder.setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentText(msg)
                .setContentIntent(pendingIntentYes)
                .build();

        startForeground(FOREGROUND_ID,notification);
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
                WifiUtility.updateWifiResult(wifiManager.getScanResults());
                wifiManager.startScan();                                                  // Contineous scan

                newResultFound(WifiUtility.getOpenScanResult());

            }

        }
    }


    private void notifyUser()
    {
        createNotification("New Network found");
    }

    private void newResultFound(List<ScanResult> openNetworks)
    {
        Log.d(TAG, "newResultFound:? ");
        oldResults = newResults;
        newResults = openNetworks;

        if(oldResults.size()!=newResults.size())
        {
            Log.d(TAG, "newResultFound:? YES");
            notifyUser();
        }else {
            //compare logic
        }

    }


}
