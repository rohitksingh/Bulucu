package com.freewifi.rohksin.freewifi.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeCallback;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;

import java.util.List;

public class NotifyMeActivity extends AppCompatActivity implements NotifyMeCallback {


    /*
         TODO kEEP a static variable in to know if the service is running
     */

    private TextView details;
    private ToggleButton toggle;

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
    public void notifyResults(List<String> results) {
        getDetails(results);
    }


    /*************************************************************************************************
     *                         Private Helper methods
     ************************************************************************************************/


    private void getDetails(List<String> results)
    {

        if(results!=null)
        {
            details.setText(results.toString());
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
        toggle = (ToggleButton) findViewById(R.id.chkState);

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

}
