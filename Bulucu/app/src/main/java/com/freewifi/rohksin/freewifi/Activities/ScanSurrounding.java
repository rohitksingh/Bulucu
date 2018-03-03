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
import android.support.v7.widget.Toolbar;
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


   // private Toolbar toolbar;
    private Menu menu;

    MenuItem item;




    private boolean SCAN_RUNNING = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scan);
        scanButton = (Button)findViewById(R.id.scanButton);
        //toolbar = (Toolbar)findViewById(R.id.scanToolBar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Scan Result");


        rv = (RecyclerView)findViewById(R.id.rv);




        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        scanResults = new ArrayList<String>();
        adapter = new StringAdapter(this, scanResults);
        rv.setAdapter(adapter);

        Log.d("Status Bar Height", getSupportActionBar().getHeight()+"");

        rv.setPadding(0, 120, 0, 0);
        scanButton.setText("START");
        scanButton.setVisibility(View.GONE);


        manager = WifiUtility.getSingletonWifiManager(this);

        uniqueScanResult = new LinkedHashSet<String>();


        //final ScanTask task = new ScanTask();

        startScan();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //task.execute();



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
            default:
                Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();

        }


        return super.onOptionsItemSelected(item);
    }




    private int getTopPadding()
    {

        int height=0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId>0)
        {
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }





    private class ScanTask extends AsyncTask<Void, String  , Void >{


        @Override
        public void onPreExecute()
        {
            Toast.makeText(ScanSurrounding.this, "Scanning...", Toast.LENGTH_LONG).show();
        }

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

        @Override
        public void onPostExecute(Void result)
        {
            Toast.makeText(ScanSurrounding.this, "Scan Fininshed", Toast.LENGTH_SHORT).show();
        }


    }




    public void startScan()
    {
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


}
