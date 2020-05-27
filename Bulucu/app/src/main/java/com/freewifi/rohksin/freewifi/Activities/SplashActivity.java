package com.freewifi.rohksin.freewifi.Activities;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;

public class SplashActivity extends AppCompatActivity {

    /***********************************************************************************************
     *                                          TODO
     *    Hotspot Check
     *    Location off then it will not work, Add a dialog to tell user
     *    Add Stealth mode
     *    Customize Notification  How to update Notification, Noti channel?
     *    Do we need a class for BasicSplashActivity which calls AppUtility?
     *
     ***********************************************************************************************/


    private LottieAnimationView into;

    private NotifyMeService notifyMeService;
    private Intent checkServiceRunning;
    private boolean bound = false;

    /***********************************************************************************************
     *                     Activity Life cycle methods   && Runtime Permission                     *
     /*********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        setUpUI();
        AppUtility.loadAppUtility(this);           //<-- App AppUtility should be called here
        bindService();

    }

    @Override
    public void onStop()
    {
        super.onStop();
        unbindService();
    }


    /***********************************************************************************************
     *                                 Service Connection                                          *
     **********************************************************************************************/

    public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bound = true;
            NotifyMeService.NotifyMeBinder binder = (NotifyMeService.NotifyMeBinder)service;
            notifyMeService = binder.getService();
            int counter = notifyMeService.getServiceStartedCounter();
            boolean isSerRunning = AppUtility.isServiceRunning(counter);
            Log.d("SERVICE_STATUS", "onServiceConnected: "+isSerRunning);
            AppUtility.setToggleState(isSerRunning);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };


    /***********************************************************************************************
     *                                 Private Helper methods                                      *
     ***********************************************************************************************/

    private void bindService()
    {
        checkServiceRunning = new Intent(this, NotifyMeService.class);
        bindService(checkServiceRunning, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService()
    {
        if (bound) {
            bound = false;
            unbindService(serviceConnection);
        }
    }


    private void setUpUI()
    {
        into = findViewById(R.id.bulucu_into);

        into.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


}
