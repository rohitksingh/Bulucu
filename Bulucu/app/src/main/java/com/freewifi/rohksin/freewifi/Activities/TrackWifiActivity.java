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
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.TrackWifiService;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by RohitKsingh on 3/3/2018.
 */

public class TrackWifiActivity extends AppCompatActivity {

    private TextView wifiLevel;
    private ScanResult targetWifi;
    private TextView wifiName;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;

    private WifiManager manager;

    private WifiLevelReceiver receiver;
    private Intent startTrackService;

    private int count =0;
    public static String TARGET_WIFI = "com.freewifi.rohksin.freewifi.Activities.TARGET_WIFI";

    /***********************************************************************************************
     *                               Activity Life cycle methods                                   *
     /*********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackwifi_layout);
        setUpUI();
    }


    @Override
    public void onStop()
    {
        super.onStop();
        unregisterReceiver(receiver);
        stopService(startTrackService);
    }


    /***********************************************************************************************
     *                                    BroadcastReceiver                                        *
     ***********************************************************************************************/

    private class WifiLevelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("LEVEL"))
            {
                int level = intent.getIntExtra("LEVELDATA",0);

                int relativeLevel  = getLevel(level);

                DataPoint newData = new DataPoint(count, relativeLevel);
                count++;
                series.appendData(newData, true, 12);

                wifiLevel.setText(WifiUtility.getWifiStrengthStatus(relativeLevel));

            }
        }
    }


    /***************************************************************************************************
     *                                     Private Helper mrthods                                      *
     ***************************************************************************************************/

    private int  getLevel(int level)
    {
        if(level==-1)
            return 0;
        else {
            return (manager.calculateSignalLevel(level, 20));
        }
    }


    private void setUpUI(){

        manager = WifiUtility.getSingletonWifiManager(TrackWifiActivity.this);
        targetWifi = getIntent().getParcelableExtra(TARGET_WIFI);
        receiver = new WifiLevelReceiver();
        registerReceiver(receiver, new IntentFilter("LEVEL"));

        wifiLevel = (TextView)findViewById(R.id.wifiLevel);
        wifiName = (TextView)findViewById(R.id.wifiName);
        graphView = (GraphView)findViewById(R.id.graph);

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

        wifiName.setText(AppUtility.getString(R.string.tracking)+" "+targetWifi.SSID);

        startTrackService = new Intent(this, TrackWifiService.class);
        startTrackService.putExtra("SCAN_RESULT", targetWifi);
        startService(startTrackService);

    }

}
