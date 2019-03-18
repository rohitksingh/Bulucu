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
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Activities.HomePageActivity;
import com.freewifi.rohksin.freewifi.Activities.NotifyMeActivity;
import com.freewifi.rohksin.freewifi.Activities.ScanSurroundingActivity;
import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeResults;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;

public class NotifyMeService extends Service implements WifiScanInterface {

    /**
        TODO Dynamically register BroadcastReceiver
     */

    private int FOREGROUND_ID = 2222;

    private WifiManager wifiManager;
    private WifiScanReceiver wifiScanReceiver;

    private static final String TAG = "NotifyMeService";


    private Set<String> allScanNames;

    private NotifyMeResults notifyMeResults;;
    public IBinder localBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        allScanNames = new TreeSet<String>();
        startScan();

    }


    @Override
    public int onStartCommand(Intent intent, int flag, int flagId)
    {
        createNotification("Notify me");  //<-- Makes Foreground
        return flag;
    }



    public class LocalBinder extends Binder {
        public NotifyMeService getService() {
            return NotifyMeService.this;
        }
    }



    private void createNotification(String msg)
    {


        Log.d(TAG, "createNotification: "+allScanNames.size());

        Intent notifyMeIntent = new Intent(this, NotifyMeActivity.class);
        notifyMeIntent.putExtra("startedByNotification", true);

        PendingIntent pendingIntentYes = PendingIntent.getActivity(this, 12345,notifyMeIntent,  PendingIntent.FLAG_UPDATE_CURRENT);



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
        return localBinder;
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

    }

    @Override
    public void stopScan() {

        unregisterReceiver(wifiScanReceiver);
    }

    @Override
    public void updateScanUI() {

    }


    public void registerCallBack(NotifyMeResults notifyMeResults)
    {
        this.notifyMeResults = notifyMeResults;
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

                if(notifyMeResults!=null)
                notifyMeResults.notifyResults(new ArrayList<String>(allScanNames));

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
        }
    }

    private boolean newResultFound(List<ScanResult> openNetworks)
    {
        boolean newResultFound = false;

        for(int i=0;i<openNetworks.size();i++)
        {
            String name= openNetworks.get(i).SSID;
            if(!allScanNames.contains(name))
            {

                newResultFound = true;
                allScanNames.add(name);
            }

        }

        return newResultFound;

    }


}
