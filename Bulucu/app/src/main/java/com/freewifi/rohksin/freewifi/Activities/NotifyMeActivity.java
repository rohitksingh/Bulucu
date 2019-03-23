package com.freewifi.rohksin.freewifi.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.NotifyMeListAdapter;
import com.freewifi.rohksin.freewifi.Adapters.StringAdapter;
import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeCallback;
import com.freewifi.rohksin.freewifi.Models.WifiResult;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;
import com.freewifi.rohksin.freewifi.Services.TestNotifyMeService;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.List;

public class NotifyMeActivity extends AppCompatActivity implements NotifyMeCallback {


    /**
         TODO Create a utility to sync status of all UI elements
         1) AppUtility.syncUIStatus();
         2) BroadcastReceiver stable unregister call
         3) Notification
     */

    private TextView details;
    private SwitchCompat toggle;
    private RecyclerView notifyMeList;
    private LinearLayoutManager llm;
    private NotifyMeListAdapter adapter;
    private RelativeLayout mainLayout;

    private Intent notifyMeIntent;
    private TestNotifyMeService myService;
    private boolean bound = false;

    private static final String TAG = "NotifyMeActivity";



    /*************************************************************************************************
     *                      Activity Lifecycle methods
     ************************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_me_activity);

        notifyMeIntent = new Intent(this, TestNotifyMeService.class);

        trySetUpUI();

      //  getDetails();

    }


    @Override
    protected void onStart()
    {
        super.onStart();
        bindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService();
    }


    /*************************************************************************************************
     *                         Service Connection
     ************************************************************************************************/

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            TestNotifyMeService.NotifyMeBinder binder = (TestNotifyMeService.NotifyMeBinder) service;
            myService = binder.getService();
            bound = true;
            myService.registerCallBack(NotifyMeActivity.this); // register
            //details.setText(getResultData(myService.getAllwifiResults()));
            setUpList(myService.getAllwifiResults());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
            Log.d("Lifecycle", "onServiceDisConnected: ");
        }
    };


    /*************************************************************************************************
     *                         Callback method
     ************************************************************************************************/

    @Override
    public void notifyResults(List<WifiResult> results) {
        Log.d("NOTIFY_USER_TRACK", "NOTIFY USER ");
        //getDetails(results);
        setUpList(results);
    }


    /*************************************************************************************************
     *                         Private Helper methods
     ************************************************************************************************/


    private void getDetails(List<WifiResult> results)
    {

        if(results.size()!=0)
        {
            Log.d(TAG, "Setting up text" +(results.toString()));
            details.setVisibility(View.GONE);
            setUpList(results);
        }
        else {
            details.setText("No result Found");
        }
    }



    private void bindService()
    {
        Log.d("TestNotifyMeService", "bindService: ");
        bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService()
    {
        if (bound) {
            myService.registerCallBack(null); // unregister
            Log.d("TestNotifyMeService", "unbindService: ");
            unbindService(serviceConnection);
            bound = false;
        }
    }



    private void getDetails()
    {
       // Log.d(TAG, "getDetails: "+bound);

        /*
        boolean result = AppUtility.getNotifyServiceStatus();
        if(result) {
            bindService();
        }
        */

        /*
        bindService();

        if(myService!=null)
            getDetails(myService.getAllwifiResults());
            */


    }




    private void startNotifyMe()
    {
        //AppUtility.setNotifySericeStatus(true);
        startService(notifyMeIntent);
       // bindService();
    }

    private void stopNotifyMe()
    {
        // AppUtility.setNotifySericeStatus(false);
        myService.stopScan();
        stopService(notifyMeIntent);
        //unbindService();
    }


    /*************************************************************************************************
     *                         Setup UI
     ************************************************************************************************/


    private void trySetUpUI()
    {

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainLayout.setPadding(0,AppUtility.getStatusBarHeight(),0,0);
        details = (TextView)findViewById(R.id.detail);
        toggle = (SwitchCompat) findViewById(R.id.chkState);
        Log.d("SERVICE_STATUS", "trySetUpUI: "+AppUtility.getToggleState());
        toggle.setChecked(AppUtility.getToggleState());
        runIntro();

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(notifyMeIntent);
                    AppUtility.setToggleState(true);

                } else {
                    myService.stopScan();
                    stopService(notifyMeIntent);
                    AppUtility.setToggleState(false);

                }
            }
        });

        llm = new LinearLayoutManager(this);
        notifyMeList = (RecyclerView)findViewById(R.id.notifymeList);
        notifyMeList.setLayoutManager(llm);


    }

    private void setupUI()
    {

        details = (TextView)findViewById(R.id.detail);
        toggle = (SwitchCompat) findViewById(R.id.chkState);



    }


    private void runIntro()
    {
          if(!AppUtility.getNotifyMeIntroStatus())
          {
              //addIntroView(R.id.chkState, "Notify Me", "Get notified when a new open network is detected", android.R.color.holo_purple, getResources().getDrawable( R.drawable.scan_now));
          }
    }

    private void setUpList(List<WifiResult> wifiResults)
    {
        Log.d("SettingUPLIST", "setUpList: "+wifiResults.toString());
        adapter = new NotifyMeListAdapter(this, wifiResults);
        notifyMeList.setAdapter(adapter);
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
                    }
                });
    }





}
