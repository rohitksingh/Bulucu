package com.freewifi.rohksin.freewifi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.LangUtility;

import java.util.Map;

public class SettingActiivity extends AppCompatActivity {

    private TextView german,greek,spanish;
    private Button getStarted;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_layout);

        german = (TextView)findViewById(R.id.german);
        greek = (TextView)findViewById(R.id.greek);
        spanish = (TextView)findViewById(R.id.spanish);
        getStarted = (Button)findViewById(R.id.getStarted);


        german.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(R.string.GERMAN_);
            }
        });

        greek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(R.string.GREEk_);
            }
        });


        spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(R.string.SPANISH_);
            }
        });


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActiivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });




    }


    private void setLanguage(int languageCode)
    {
        AppUtility.setUserLanguage(languageCode);
        enableButton();
    }

    private void enableButton()
    {
        getStarted.setEnabled(true);
        getStarted.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
    }



}
