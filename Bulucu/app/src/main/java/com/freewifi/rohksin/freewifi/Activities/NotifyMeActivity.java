package com.freewifi.rohksin.freewifi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Services.NotifyMeService;

public class NotifyMeActivity extends AppCompatActivity {

    private Button start, stop;

    Intent notifyMeIntent;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_me_activity);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);


        notifyMeIntent = new Intent(this, NotifyMeService.class);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(notifyMeIntent);
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
