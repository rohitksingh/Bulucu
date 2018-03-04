package com.freewifi.rohksin.freewifi.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Testing.TestService;

/**
 * Created by Illuminati on 3/3/2018.
 */

public class TrackWifiActivity extends AppCompatActivity {

    public static String TARGET_WIFI = "com.freewifi.rohksin.freewifi.Activities.TARGET_WIFI";

    private TextView wifiLevel;
    private ScanResult targetWifi;
    private TextView wifiName;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_wifi_activity_layout);
        wifiLevel = (TextView)findViewById(R.id.wifiLevel);
        wifiName = (TextView)findViewById(R.id.wifiName);


        targetWifi = getIntent().getParcelableExtra(TARGET_WIFI);

        wifiName.setText("Tracking "+targetWifi.SSID);

        Intent startTrackService = new Intent(this, TestService.class);
        startTrackService.putExtra("SCAN_RESULT", targetWifi);
        startService(startTrackService);

        registerReceiver(new LevelReceiver(), new IntentFilter("LEVEL"));


    }



    class LevelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("LEVEL"))
            {
                int level = intent.getIntExtra("LEVELDATA",0);

                if(level==-1)
                {
                    wifiLevel.setText("Wifi Lost");
                }
                else {
                    wifiLevel.setText(level+"");
                }

            }
        }
    }

}
