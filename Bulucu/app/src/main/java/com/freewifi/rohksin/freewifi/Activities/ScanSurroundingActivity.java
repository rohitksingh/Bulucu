package com.freewifi.rohksin.freewifi.Activities;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.freewifi.rohksin.freewifi.Adapters.StringAdapter;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class ScanSurroundingActivity extends AppCompatActivity {

    private TextView scanTime;
    private LottieAnimationView scanLottieButton;
    private TextView wifiNum;
    private RelativeLayout scannerLayout;
    private CollapsingToolbarLayout title;

    private WifiManager manager;

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private StringAdapter adapter;
    private List<String> scanResults;
    private Set<String> uniqueScanResult;

    private ScanTask scanTask;
    private boolean SCAN_RUNNING = false;
    private int SCAN_NUM =0;

    private static final String TAG = "ScanSurroundingActivity";


    /***********************************************************************************************
     *                               Activity Life cycle methods                                   *
     /*********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scansurrounding_layout);
        setUpUI();
        startScan();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopScan();
    }



    /***********************************************************************************************
     *                               AsyncTask for scanning surrounding                            *
     /*********************************************************************************************/

    private class ScanTask extends AsyncTask<Void, String  , Void >{

        @Override
        public void onPreExecute()
        {
            Log.d(TAG, "onPreExecute: ");
            SCAN_RUNNING = true;
            scanLottieButton.playAnimation();
            Snackbar.make(scanLottieButton, R.string.scanning, Snackbar.LENGTH_SHORT)
                    .show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(TAG, "doInBackground: ");
            
            if(isCancelled())
            {
                return null;
            }

            for(int i =0; i<10;i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }

                List<ScanResult> results = manager.getScanResults();

                manager.startScan();

                for(ScanResult scanResult: results)
                {
                    uniqueScanResult.add(scanResult.SSID);
                }

                scanResults = new ArrayList<String>(uniqueScanResult);
                publishProgress((10-i)+"");

            }

            return null;
        }


        public void onProgressUpdate(String ... param)
        {

            Log.d(TAG, "scanlist size: "+scanResults.size());

            //adapter.notifyDataSetChanged();     ? Why it is not working

            // TEMP ///////////////////////////////////////////////////////////////////////////////
            scanTime.setText(param[0]);
            SCAN_NUM = scanResults.size();
            wifiNum.setText(AppUtility.getString(R.string.results_found)+": "+SCAN_NUM);
            adapter = new StringAdapter(ScanSurroundingActivity.this, scanResults);
            rv.setAdapter(adapter);
            // TEMP ///////////////////////////////////////////////////////////////////////////////
        }

        @Override
        public void onPostExecute(Void result)
        {
            scanTime.setText(R.string.scan);
            scanLottieButton.pauseAnimation();
            SCAN_RUNNING = false;
            Snackbar.make(scanLottieButton, R.string.scan_finished, Snackbar.LENGTH_SHORT)
                    .show();
            Log.d(TAG, "onPostExecute: ");
            // Add someAction // Menu item : two dots

        }

    }


    /***********************************************************************************************
     *                                 Private Helper methods                                      *
     ***********************************************************************************************/

    private void startScan()
    {

        Log.d(TAG, "Scan running "+SCAN_RUNNING);

        if(!SCAN_RUNNING) {
            scanTask = new ScanTask();
            scanTask.execute();
        }


    }

    private void stopScan()
    {
        scanTask.cancel(true);
    }


    private void setUpCollapsingToolBar()
    {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    title.setTitle(AppUtility.getString(R.string.results_found)+": "+SCAN_NUM);
                    isShow = true;
                } else if(isShow) {
                    title.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



    public void setUpUI()
    {
        scannerLayout = (RelativeLayout)findViewById(R.id.scannerLayout);
        scannerLayout.setPadding(0,AppUtility.getStatusBarHeight(),0,0);
        title= (CollapsingToolbarLayout)findViewById(R.id.title);
        scanTime = (TextView)findViewById(R.id.scanTime);
        scanLottieButton = (LottieAnimationView)findViewById(R.id.lottieButton);
        rv = (RecyclerView)findViewById(R.id.rv);
        wifiNum = (TextView)findViewById(R.id.wifiNum);

        title.setExpandedTitleColor(Color.TRANSPARENT);
        wifiNum.setPadding(0, AppUtility.getStatusBarHeight(),0,0);
        setUpCollapsingToolBar();

        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        scanResults = new ArrayList<String>();
        adapter = new StringAdapter(this, scanResults);
        rv.setAdapter(adapter);

        manager = WifiUtility.getSingletonWifiManager(this);
        uniqueScanResult = new LinkedHashSet<String>();

        scanLottieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startScan();
            }
        });

    }

}
