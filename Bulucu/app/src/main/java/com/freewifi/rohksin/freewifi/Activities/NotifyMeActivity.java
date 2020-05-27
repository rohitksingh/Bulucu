package com.freewifi.rohksin.freewifi.Activities;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Adapters.NotifyMeListAdapter;
import com.freewifi.rohksin.freewifi.Dialogs.NotifyMeDialog;
import com.freewifi.rohksin.freewifi.Interfaces.NotifyMeCallback;
import com.freewifi.rohksin.freewifi.Models.WifiResult;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.List;

public class NotifyMeActivity extends AppCompatActivity implements NotifyMeCallback {


    /***********************************************************************************************
     *                                          TODO
     *    Create a utility to sync status of all UI elements
     *    AppUtility.syncUIStatus();
     *    roadcastReceiver stable unregister call
     *    Create a utility for RunInto AppUtility.ShowIntro("Msg", Key, etc)
     *
     ***********************************************************************************************/

    private FrameLayout mainLayout;
    private TextView details;
    private SwitchCompat toggle;

    private RecyclerView notifyMeList;
    private LinearLayoutManager llm;
    private NotifyMeListAdapter adapter;

    private Intent notifyMeIntent;
    private NotifyMeService myService;
    private boolean bound = false;

    private static final String TAG = "NotifyMeActivity";


    /***********************************************************************************************
     *                               Activity Life cycle methods                                   *
     /*********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifyme_layout);
        notifyMeIntent = new Intent(this, NotifyMeService.class);
        setUpUI();

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


    /***********************************************************************************************
     *                                  Interface methods                                          *
     **********************************************************************************************/

    @Override
    public void notifyResults(List<WifiResult> results) {
        Log.d(TAG, "NOTIFY USER ");
        setUpList(results);
    }


    /***********************************************************************************************
     *                                 Service Connection                                          *
     **********************************************************************************************/

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            NotifyMeService.NotifyMeBinder binder = (NotifyMeService.NotifyMeBinder) service;
            myService = binder.getService();
            bound = true;
            myService.registerCallBack(NotifyMeActivity.this); // register
            setUpList(myService.getAllwifiResults());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };



    /***********************************************************************************************
     *                                 Private Helper methods                                      *
     ***********************************************************************************************/


    private void bindService()
    {
        Log.d("NotifyMeService", "bindService: ");
        bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService()
    {
        if (bound) {
            myService.registerCallBack(null); // unregister
            Log.d("NotifyMeService", "unbindService: ");
            unbindService(serviceConnection);
            bound = false;
        }
    }

    private void syncUI()
    {
        toggle.setChecked(AppUtility.getToggleState());
    }

    private void createInfoDialog()
    {
        Dialog dialog = new NotifyMeDialog(this);
        dialog.show();
    }


    private void runIntro()
    {
          if(!AppUtility.getNotifyMeIntroStatus())
          {
              addIntroView(R.id.chkState, AppUtility.getString(R.string.notify_me), AppUtility.getString(R.string.get_notified_when), android.R.color.holo_purple, getResources().getDrawable( R.drawable.scan_now));
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
                    }
                });
    }

    private void setUpUI()
    {

        details = findViewById(R.id.detail);
        toggle = findViewById(R.id.chkState);
        mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setPadding(0,AppUtility.getStatusBarHeight(),0,0);
        notifyMeList = findViewById(R.id.notifymeList);
        llm = new LinearLayoutManager(this);
        notifyMeList.setLayoutManager(llm);

        syncUI();    //<-- Synv UI with background service

        runIntro();

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(notifyMeIntent);
                    AppUtility.setToggleState(true);
                    createInfoDialog();

                } else {
                    myService.stopScan();
                    stopService(notifyMeIntent);
                    AppUtility.setToggleState(false);

                }
            }
        });

    }

}
