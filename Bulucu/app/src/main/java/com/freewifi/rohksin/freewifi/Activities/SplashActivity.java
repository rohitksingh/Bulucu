package com.freewifi.rohksin.freewifi.Activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.LangUtility;

public class SplashActivity extends AppCompatActivity {


    private LottieAnimationView into;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);

        setUpUtility();

        into = (LottieAnimationView)findViewById(R.id.bulucu_into);

        into.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                startActivity(new Intent(SplashActivity.this, SettingActiivity.class));
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


    private void setUpUtility()
    {
        AppUtility.loadAppUtility(this);
        LangUtility.setUpAllLanguage(this);
    }

}
