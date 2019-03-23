package com.freewifi.rohksin.freewifi.Activities;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;

public class SplashActivity extends AppCompatActivity {


    /**
     *
     *    Hotspot Check
     *    Location off then it will not work
     *    Add vibration
     *    Add Stealth mode
     *    Customize Notification
     *
     */



    private LottieAnimationView into;

    private NotifyMeService notifyMeService;
    private Intent checkServiceRunning;
    private boolean bound = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);

        AppUtility.loadAppUtility(this);


        bindService();

        into = (LottieAnimationView)findViewById(R.id.bulucu_into);

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


    @Override
    public void onStop()
    {
        super.onStop();
        unbindService();
    }



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
           // unbindService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

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


}
