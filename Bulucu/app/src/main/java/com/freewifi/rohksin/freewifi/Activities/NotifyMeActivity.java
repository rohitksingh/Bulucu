package com.freewifi.rohksin.freewifi.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeCallback;
import com.freewifi.rohksin.freewifi.Models.WifiResult;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.List;

public class NotifyMeActivity extends AppCompatActivity implements NotifyMeCallback {


    /*
         TODO kEEP a static variable in to know if the service is running
     */

    private TextView details;
    private SwitchCompat toggle;

    private Intent notifyMeIntent;
    private NotifyMeService myService;
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

        notifyMeIntent = new Intent(this, NotifyMeService.class);

        setupUI();

        getDetails();

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
            NotifyMeService.LocalBinder binder = (NotifyMeService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.registerCallBack(NotifyMeActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };


    /*************************************************************************************************
     *                         Callback method
     ************************************************************************************************/

    @Override
    public void notifyResults(List<WifiResult> results) {
        Log.d(TAG, "NOTIFY USER ");
        getDetails(results);
    }


    /*************************************************************************************************
     *                         Private Helper methods
     ************************************************************************************************/


    private void getDetails(List<WifiResult> results)
    {

        if(results.size()!=0)
        {
            details.setText(getResultData(results));
        }
        else {
            details.setText("No result Found");
        }
    }



    private void bindService()
    {
        bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService()
    {
        if (bound) {
            myService.registerCallBack(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

    private void getDetails()
    {
        Log.d(TAG, "getDetails: "+bound);

        boolean result = AppUtility.getNotifyServiceStatus();
        if(result) {
            bindService();
        }

        if(myService!=null)
            getDetails(myService.getAllwifiResults());


    }


    private void startNotifyMe()
    {
        AppUtility.setNotifySericeStatus(true);
        startService(notifyMeIntent);
        bindService();
    }

    private void stopNotifyMe()
    {
        AppUtility.setNotifySericeStatus(false);
        stopService(notifyMeIntent);
        unbindService();
    }


    /*************************************************************************************************
     *                         Setup UI
     ************************************************************************************************/

    private void setupUI()
    {

        details = (TextView)findViewById(R.id.detail);
        toggle = (SwitchCompat) findViewById(R.id.chkState);

        runIntro();

        if(AppUtility.getNotifyServiceStatus())
        {
            toggle.setChecked(true);
        }

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startNotifyMe();
                } else {
                    stopNotifyMe();
                }
            }
        });

    }


    private void runIntro()
    {
          if(!AppUtility.getNotifyMeIntroStatus())
          {
              addIntroView(R.id.chkState, "Notify Me", "Get notified when a new open network is detected", android.R.color.holo_purple, getResources().getDrawable( R.drawable.scan_now));
          }
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


    /**
     *    Temp methods, it will be removed once layout is decided
     *
     */

    public String getResultData(List<WifiResult> wifiResults)
    {
        String result="";

        for(WifiResult wifiResult: wifiResults)
        {
            result = result +"\n"+wifiResult.toString();
        }

        return result;
    }


}
