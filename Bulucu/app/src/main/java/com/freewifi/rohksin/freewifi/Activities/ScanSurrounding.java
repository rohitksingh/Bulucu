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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.freewifi.rohksin.freewifi.Adapters.StringAdapter;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.ScanWifiService;
import com.freewifi.rohksin.freewifi.WifiUtility;

import java.security.PublicKey;
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


    private Menu menu;

    MenuItem item;




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
                    task.cancel(false);
                    SCAN_RUNNING = false;
                    scanButton.setText("START");
                }


            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.scan_surrounding_menu,menu);
        this.menu = menu;
        item = menu.findItem(R.id.numOfWifi);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        switch (id)
        {
            case R.id.numOfWifi:
            {
                Toast.makeText(this, "Num Of Wifi", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.addToDataBase:
            {
                Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();

        }


        return super.onOptionsItemSelected(item);
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
                // Sorting disabled
               // Collections.sort(scanResults);
                publishProgress("");
            }



            return null;
        }


        public void onProgressUpdate(String ... param)
        {



            Log.d("Log", scanResults.size()+"");

            //adapter.notifyDataSetChanged();     ? Why it is not working

            // TEMP

            item.setTitle(scanResults.size()+"");

            adapter = new StringAdapter(ScanSurrounding.this, scanResults);
            rv.setAdapter(adapter);


            // TEMP
        }



    }



}
