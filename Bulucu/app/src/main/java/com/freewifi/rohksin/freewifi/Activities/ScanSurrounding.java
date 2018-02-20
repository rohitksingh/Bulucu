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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.StringAdapter;
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


    //TextView textView;

    String foundString = "";

    private Map<String, ScanResult> uniqueResult;

    private WifiManager manager;


    private Set<String> uniqueScanResult;


    private RecyclerView rv;
    private LinearLayoutManager llm;
    private StringAdapter adapter;
    private List<String> scanResults;

    private Button scanButton;




    private boolean SCAN_RUNNING = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scan);
        scanButton = (Button)findViewById(R.id.scanButton);
        rv = (RecyclerView)findViewById(R.id.rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        scanResults = new ArrayList<String>();
        adapter = new StringAdapter(this, scanResults);
        rv.setAdapter(adapter);


        scanButton.setText("START");


        manager = WifiUtility.getSingletonWifiManager(this);

        uniqueScanResult = new LinkedHashSet<String>();


        //final ScanTask task = new ScanTask();


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //task.execute();

                ScanTask task = new ScanTask();

                if(!SCAN_RUNNING)
                {
                    task.execute();
                    SCAN_RUNNING = true;
                    scanButton.setText("STOP");
                }
                else {
                    task.cancel(true);
                    SCAN_RUNNING = false;
                    scanButton.setText("START");
                }


            }
        });





       // task.cancel(true);





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

               // ArrayList<String>  list = new ArrayList<String>(uniqueScanResult);

                scanResults = new ArrayList<String>(uniqueScanResult);

                ////

                Collections.sort(scanResults);
                publishProgress("");
            }



            return null;
        }


        public void onProgressUpdate(String ... param)
        {
            //textView.setText(param[0]+"");


            Log.d("Log", scanResults.size()+"");

            //adapter.notifyDataSetChanged();     ? Why it is not working

            // TEMP

            adapter = new StringAdapter(ScanSurrounding.this, scanResults);
            rv.setAdapter(adapter);


            // TEMP
        }



    }



}
