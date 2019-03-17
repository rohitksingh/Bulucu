package com.freewifi.rohksin.freewifi.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.freewifi.rohksin.freewifi.Activities.HomePageActivity;
import com.freewifi.rohksin.freewifi.R;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;

public class NotifyMeService extends IntentService {

    private int FOREGROUND_ID = 2222;

    private static final String TAG = "NotifyMeService";

    public NotifyMeService()
    {
        super("NotifyMeService");
    }

    @Override
    public void onHandleIntent(@NonNull Intent intent)
    {


        Log.d(TAG, "onHandleIntent: ");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();




        startForeground(FOREGROUND_ID,notification);     //<-- Makes Foreground



        for(int i=0;i<5;i++)
        {
            Log.d(TAG, "onHandleIntent: "+i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        stopForeground(true);


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




}
