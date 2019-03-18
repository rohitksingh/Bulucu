package com.freewifi.rohksin.freewifi.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeCallback;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;

import java.util.List;

public class NotifyMeActivity extends AppCompatActivity implements NotifyMeCallback {

    private Button start, stop;
    private TextView details;

    private Intent notifyMeIntent;

    private NotifyMeService myService;
    private boolean bound = false;

    /*************************************************************************************************
     *                      Activity Lifecycle methods
     ************************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_me_activity);

        setupUI();

        notifyMeIntent = new Intent(this, NotifyMeService.class);
        getDetails();




    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            myService.registerCallBack(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
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


    private void getDetails()
    {
        boolean result = getIntent().getBooleanExtra("startedByNotification", false);
        if(result) {
            bindService();
        }
    }



    /*************************************************************************************************
     *                         Setup UI
     ************************************************************************************************/

    private void setupUI()
    {
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        details = (TextView)findViewById(R.id.detail);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(notifyMeIntent);
                bindService();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(notifyMeIntent);
            }
        });
    }

}
