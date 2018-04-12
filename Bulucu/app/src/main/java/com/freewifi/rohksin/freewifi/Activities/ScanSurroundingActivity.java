package com.freewifi.rohksin.freewifi.Activities;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.freewifi.rohksin.freewifi.Adapters.StringAdapter;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Illuminati on 2/17/2018.
 */

public class ScanSurroundingActivity extends AppCompatActivity {


    private WifiManager manager;

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private StringAdapter adapter;
    private List<String> scanResults;
    private Set<String> uniqueScanResult;

    private Menu menu;
    private MenuItem item;
    private MenuItem scanningProgress;

    private boolean SCAN_RUNNING = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_surrounding_activity_layout);

        getSupportActionBar().setTitle("Scan Result");

        rv = (RecyclerView)findViewById(R.id.rv);

        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        scanResults = new ArrayList<String>();
        adapter = new StringAdapter(this, scanResults);
        rv.setAdapter(adapter);
        rv.setPadding(0, 120, 0, 0);

        manager = WifiUtility.getSingletonWifiManager(this);
        uniqueScanResult = new LinkedHashSet<String>();

        startScan();

    }

    //********************************************************************************************//
    //                                      Menu Related                                          //
    //********************************************************************************************//

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.scan_surrounding_menu,menu);
        this.menu = menu;
        item = menu.findItem(R.id.numOfWifi);
        scanningProgress = menu.findItem(R.id.scanning);
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
                //Toast.makeText(this, "Num Of Wifi", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
              //  Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();

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



    //*******************************************************************************************************//
    //                                          AsncTask                                                     //
    //*******************************************************************************************************//

    private class ScanTask extends AsyncTask<Void, String  , Void >{

        @Override
        public void onPreExecute()
        {
            Toast.makeText(ScanSurroundingActivity.this, "Scanning...", Toast.LENGTH_LONG).show();
           // startLoadingAnimation();
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

                scanResults = new ArrayList<String>(uniqueScanResult);
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
            adapter = new StringAdapter(ScanSurroundingActivity.this, scanResults);
            rv.setAdapter(adapter);


            // TEMP
        }

        @Override
        public void onPostExecute(Void result)
        {
            Toast.makeText(ScanSurroundingActivity.this, "Scan Fininshed", Toast.LENGTH_SHORT).show();
        }

    }



    public void startScan()
    {
        ScanTask task = new ScanTask();

        if(!SCAN_RUNNING)
        {
            task.execute();
            SCAN_RUNNING = true;
        }
        else {
            task.cancel(false);
            SCAN_RUNNING = false;
        }

    }


    /*

    public void startLoadingAnimation()
    {

        Log.d("Null", (scanningProgress==null)+""+(item==null));
        View view = (View)scanningProgress;
        Animation loadingAnimation = new RotateAnimation(0.0f,360.0f,view.getPivotX(),view.getPivotY());
        loadingAnimation.setDuration(500);
        loadingAnimation.setRepeatCount(-1);
        view.setAnimation(loadingAnimation);
    }

    */


}
