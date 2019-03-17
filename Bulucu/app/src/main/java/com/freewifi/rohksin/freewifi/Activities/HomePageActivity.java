package com.freewifi.rohksin.freewifi.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RohitKSingh on 2/17/2018.
 */

public class HomePageActivity extends AppCompatActivity implements WifiScanInterface{


    private RelativeLayout mainLayout;
    private TextView openNetwork;
    private TextView closedNetwork;
    private TextView openNum;
    private TextView closeNum;
    private TextView scanNow;
    private FrameLayout scan;
    private FrameLayout openWifiContainer;
    private FrameLayout closeWifiContainer;

    private WifiManager manager;
    private List<ScanResult> allScanResults;
    private List<ScanResult> openScanResults;
    private List<ScanResult> closeScanResults;

    private TapTargetView.Listener tapTargetListener;

    private int tapCounter=0;


    private Drawable openWifiLogo;
    private Drawable closeWifiLogo;
    private Drawable scanNowLogo;

    // Privact policy update
    private ImageView privacyPolicy;

    private WifiScanReceiver wifiScanReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_scan_activity_layout);


        setUpUI();


        if(!AppUtility.hasCompletedIntro)
        {
            new DrawableLoader().execute();
        }


    }

    private void setUpUI()
    {

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        openNetwork = (TextView)findViewById(R.id.open);
        closedNetwork = (TextView)findViewById(R.id.close);
        openNum = (TextView)findViewById(R.id.openNum);
        closeNum = (TextView)findViewById(R.id.closeNum);
        openWifiContainer = (FrameLayout)findViewById(R.id.openContainer);
        closeWifiContainer = (FrameLayout)findViewById(R.id.closeContainer);
        scanNow = (TextView)findViewById(R.id.scanNow);

        mainLayout.setPadding(0, AppUtility.getStatusBarHeight(),0,0);

        privacyPolicy = (ImageView)findViewById(R.id.privacyPolicy);
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrivacyPolicyDialog();
            }
        });


        scan = (FrameLayout)findViewById(R.id.scan);


        openWifiContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, WifiListActivity.class);
                intent.setAction("OPEN_NETWORK");
                intent.putExtra("BG_COLOR",android.R.color.holo_green_light);
                startActivity(intent);

            }
        });


        closeWifiContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, WifiListActivity.class);
                intent.setAction("CLOSE_NETWORK");
                intent.putExtra("BG_COLOR",android.R.color.holo_orange_light);
                startActivity(intent);

            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomePageActivity.this, ScanSurroundingActivity.class));
            }
        });
    }


    /***********************************************************************************************************
     *                                  Interface methods                                                      *
     ***********************************************************************************************************/

    @Override
    public void startScan()
    {
        manager = WifiUtility.getSingletonWifiManager(this);
        wifiScanReceiver = new WifiScanReceiver();
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        manager.startScan();
    }


    @Override
    public void stopScan()
    {
        unregisterReceiver(wifiScanReceiver);
    }



    @Override
    public void updateScanUI() {

    }





    /************************************************************************************************************
    *                                    BroadcastReceiver                                                      *
    ************************************************************************************************************/

    private class WifiScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {

                manager.startScan();                                                  // Contineous scan
                scanNow.setText(getNumOfWifi()+"");
                if(openScanResults!=null)
                openNum.setText(openScanResults.size()+"");
                if(closeScanResults!=null)
                closeNum.setText(closeScanResults.size()+"");
                setUpAllList();
            }

        }
    }

    /*************************************************************************************************************
    *                                    Helper Methods                                                          *
    *************************************************************************************************************/


    private int getNumOfWifi()
    {
        return manager.getScanResults().size();
    }

    private void setUpAllList()
    {
        allScanResults = manager.getScanResults();
        openScanResults = new ArrayList<ScanResult>();
        closeScanResults = new ArrayList<ScanResult>();

        for(ScanResult result : allScanResults)
        {
            if(!isProtectedNetwork(result.capabilities))
            {
                openScanResults.add(result);
            }
            else {
                Log.d("CLOSE", result.SSID);
                closeScanResults.add(result);
            }
        }

        if(openScanResults.size()!=0)
        {
            openNetwork.setText(openScanResults.get(0).SSID);

        }else {
            openNetwork.setText(R.string.no_network);
        }

        if(closeScanResults.size()!=0)
        {
            ScanResult scan = closeScanResults.get(0);
            closedNetwork.setText(scan.SSID+"");

        }
        else {
            closedNetwork.setText(R.string.no_network);
        }


    }


    private boolean isProtectedNetwork(String capability)
    {
        return (capability.contains("WPA") || capability.contains("WEP") || capability.contains("WPS"));
    }


    private void addIntroView(int targetId, String msg, String desc, int color, Drawable drawable)
    {
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(targetId), msg, desc)
                        // All options below are optional
                        .outerCircleColor(color)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(android.R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)// Specify the size (in sp) of the title text
                        .titleTextColor(android.R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(android.R.color.white)  // Specify the color of the description text
                        .textColor(android.R.color.white)            // Specify a color for both the title and description text
                        .dimColor(android.R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(drawable)                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        // doSomething();
                        setUpIntroView();
                    }
                });

    }


    private void setUpIntroView()
    {

            switch (tapCounter) {

                case 0:
                    addIntroView(R.id.openContainer, AppUtility.getString(R.string.open_networks), AppUtility.getString(R.string.click_open_networks), android.R.color.holo_green_dark,openWifiLogo);
                    tapCounter++;
                    break;

                case 1:
                    addIntroView(R.id.closeContainer, AppUtility.getString(R.string.close_networks), AppUtility.getString(R.string.click_closed_networks), android.R.color.holo_orange_dark, closeWifiLogo);
                    tapCounter++;
                    break;

                case 2:
                    addIntroView(R.id.scan, AppUtility.getString(R.string.scan_now), AppUtility.getString(R.string.scan_your_surrounding), android.R.color.holo_blue_dark, scanNowLogo);
                    tapCounter++;
                    AppUtility.saveIntoComplete();
                    break;

                default:
                    break;

            }

    }




    /**************************************************************************************************************
    *                     Activity Life cycle methods   && Runtime Permission                                     *
    /*************************************************************************************************************/


    @Override
    public void onResume()
    {
        super.onResume();
        startScan();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
        stopScan();
    }


    /**************************************************************************************************************
     *                                    Private helper  methods                                                 *
     /*************************************************************************************************************/

    private class DrawableLoader extends AsyncTask<Void, Void, Void>{

        @Override
        public Void doInBackground(Void... params)
        {
            openWifiLogo = getResources().getDrawable( R.drawable.open_network);
            closeWifiLogo = getResources().getDrawable( R.drawable.closed_network);
            scanNowLogo = getResources().getDrawable( R.drawable.scan_now);

            return null;
        }

        @Override
        public void onPostExecute(Void result)
        {
            setUpIntroView();
        }

    }


    private void openPrivacyPolicyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.terms)
                .setCancelable(true)
                .setPositiveButton(R.string.read, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = AppUtility.getPrivacyPolicyUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




}

