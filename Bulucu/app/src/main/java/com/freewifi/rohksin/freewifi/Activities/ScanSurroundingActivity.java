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
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class ScanSurroundingActivity extends AppCompatActivity {


    private WifiManager manager;

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private StringAdapter adapter;
    private List<String> scanResults;
    private Set<String> uniqueScanResult;

    private TextView scanTime;
    private LottieAnimationView scanLottieButton;
    private TextView wifiNum;
    private RelativeLayout scannerLayout;
    private CollapsingToolbarLayout title;


    private ScanTask scanTask;


    private boolean SCAN_RUNNING = false;
    private int SCAN_NUM =0;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_surrounding_activity_layout);

        scannerLayout = (RelativeLayout)findViewById(R.id.scannerLayout);
        scannerLayout.setPadding(0,getStatusBarHeight(),0,0);

        title= (CollapsingToolbarLayout)findViewById(R.id.title);
        title.setExpandedTitleColor(Color.TRANSPARENT);
        setUpCollapsingToolBar();



        rv = (RecyclerView)findViewById(R.id.rv);

        scanTime = (TextView)findViewById(R.id.scanTime);
        scanLottieButton = (LottieAnimationView)findViewById(R.id.lottieButton);
        scanLottieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startScan();
            }
        });

        wifiNum = (TextView)findViewById(R.id.wifiNum);
        wifiNum.setPadding(0,getStatusBarHeight(),0,0);


        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        scanResults = new ArrayList<String>();
        adapter = new StringAdapter(this, scanResults);
        rv.setAdapter(adapter);
        //rv.setPadding(0, 120, 0, 0);

        manager = WifiUtility.getSingletonWifiManager(this);
        uniqueScanResult = new LinkedHashSet<String>();

        startScan();

    }



    //*******************************************************************************************************//
    //                                          AsncTask                                                     //
    //*******************************************************************************************************//

    private class ScanTask extends AsyncTask<Void, String  , Void >{



        @Override
        public void onPreExecute()
        {

            SCAN_RUNNING = true;
            scanLottieButton.playAnimation();
            Snackbar.make(scanLottieButton, "Scanning...", Snackbar.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            for(int i =0; i<10;i++)
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
                publishProgress((10-i)+"");

            }

            return null;
        }


        public void onProgressUpdate(String ... param)
        {

            Log.d("Log", scanResults.size()+"");

            //adapter.notifyDataSetChanged();     ? Why it is not working

            // TEMP ///////////////////////////////////////////////////////////////////////////////
            scanTime.setText(param[0]);
            SCAN_NUM = scanResults.size();
            wifiNum.setText("Result Found "+SCAN_NUM);
            adapter = new StringAdapter(ScanSurroundingActivity.this, scanResults);
            rv.setAdapter(adapter);
            // TEMP ///////////////////////////////////////////////////////////////////////////////
        }

        @Override
        public void onPostExecute(Void result)
        {
            scanTime.setText("Scan");
            scanLottieButton.pauseAnimation();
            SCAN_RUNNING = false;
            Snackbar.make(scanLottieButton, "Scan Finished", Snackbar.LENGTH_SHORT)
                    .show();

            // Add someAction // Menu item : two dots

        }

    }



    //*******************************************************************************************************//
    //                                     Private Helper methods                                            //
    //*******************************************************************************************************//


    private void startScan()
    {


        if(!SCAN_RUNNING) {
            scanTask = new ScanTask();
            scanTask.execute();
        }


    }



    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
                    title.setTitle("Result Found "+SCAN_NUM);
                    isShow = true;
                } else if(isShow) {
                    title.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



}
