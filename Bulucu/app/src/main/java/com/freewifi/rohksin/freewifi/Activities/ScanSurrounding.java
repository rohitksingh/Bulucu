package com.freewifi.rohksin.freewifi.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.ScanWifiService;
import com.freewifi.rohksin.freewifi.WifiUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Illuminati on 2/17/2018.
 */

public class ScanSurrounding extends AppCompatActivity {


    TextView textView;

    String foundString = "";

    private Map<String, ScanResult> uniqueResult;

    private WifiManager manager;


    private Set<String> uniqueScanResult;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        textView = (TextView)findViewById(R.id.testText);

        manager = WifiUtility.getSingletonWifiManager(this);

        uniqueScanResult = new LinkedHashSet<String>();


        new ScanTask().execute();



    }





    private class ScanTask extends AsyncTask<Void, String  , Void >{


        @Override
        protected Void doInBackground(Void... params) {


            for(int i =0; i<20;i++)
            {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<ScanResult> results = manager.getScanResults();

                manager.startScan();

                for(ScanResult scanResult: results)
                {
                    uniqueScanResult.add(scanResult.SSID);
                }

                Log.d("Result", uniqueScanResult+"");

                /// Have to remoe

                ArrayList<String>  list = new ArrayList<String>(uniqueScanResult);

                ////

                Collections.sort(list);
                publishProgress(list.toString());
            }



            return null;
        }


        public void onProgressUpdate(String ... param)
        {
            textView.setText(param[0]+"");
        }



    }



}
