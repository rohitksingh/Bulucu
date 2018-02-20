package com.freewifi.rohksin.freewifi.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.WifiSignalService;

/**
 * Created by Illuminati on 2/17/2018.
 */

public class WifiDetailActivity extends AppCompatActivity {

    /*******************
     *
     *    How to get Distance in cms ?  // Using Service is it possible
     *
     *
     *    Feature need to be added : Scan Wifi
     *    SQLITE DATA BASE
     *    SCAN AN AREA
     *    SCAN WITH TIME
     *
     *
     */





    private TextView text;
    private ScanResult scanResult;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        text = (TextView)findViewById(R.id.testText);


       // registerReceiver(new WifiSignalReceiver(), new IntentFilter("HI"));
       // startService(new Intent(this, WifiSignalService.class));




    }


    public class WifiSignalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {

            Log.d("Sig", "inside intent");
            if(intent.getAction().equals("HI"))
            {
                Log.d("Sig", "inside action");
                int level = intent.getIntExtra(WifiSignalService.SIGNAL_STRENGTH,0);
                text.setText("SIGNAL STRENTH = "+level);
            }
        }

    }

}
