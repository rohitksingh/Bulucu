package com.freewifi.rohksin.freewifi.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.TrackWifiService;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Illuminati on 3/3/2018.
 */

public class TrackWifiActivity extends AppCompatActivity {

    public static String TARGET_WIFI = "com.freewifi.rohksin.freewifi.Activities.TARGET_WIFI";

    private TextView wifiLevel;
    private ScanResult targetWifi;
    private TextView wifiName;

    private GraphView graphView;
    LineGraphSeries<DataPoint> series;

    int count =0;

    private WifiManager manager;


    private WifiLevelReceiver receiver;
    private Intent startTrackService;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_wifi_activity_layout);
        wifiLevel = (TextView)findViewById(R.id.wifiLevel);
        wifiName = (TextView)findViewById(R.id.wifiName);

        graphView = (GraphView)findViewById(R.id.graph);


        manager = WifiUtility.getSingletonWifiManager(TrackWifiActivity.this);


        series = new LineGraphSeries<DataPoint>();
        series.setColor(Color.WHITE);
        series.setTitle("WifiStatud");


        graphView.addSeries(series);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(10);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(20);

        targetWifi = getIntent().getParcelableExtra(TARGET_WIFI);

        wifiName.setText("Tracking "+targetWifi.SSID);

        startTrackService = new Intent(this, TrackWifiService.class);
        startTrackService.putExtra("SCAN_RESULT", targetWifi);
        startService(startTrackService);

        receiver = new WifiLevelReceiver();
        registerReceiver(receiver, new IntentFilter("LEVEL"));

    }


    @Override
    public void onStop()
    {
        super.onStop();
        unregisterReceiver(receiver);
        stopService(startTrackService);
    }



    class WifiLevelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("LEVEL"))
            {
                int level = intent.getIntExtra("LEVELDATA",0);

                /*
                if(level==-1)
                {
                    wifiLevel.setText("Wifi Lost");
                }
                else {
                    wifiLevel.setText(level+"");
                }

                */


                int relativeLevel  = getLevel(level);

                DataPoint newData = new DataPoint(count, relativeLevel);
                count++;
                series.appendData(newData, true, 12);

                wifiLevel.setText(WifiUtility.getWifiStrengthStatus(relativeLevel));



            }
        }
    }


    private int  getLevel(int level)
    {
        if(level==-1)
            return 0;
        else {
            return (manager.calculateSignalLevel(level, 20));
        }
    }

}
