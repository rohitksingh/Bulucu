package com.freewifi.rohksin.freewifi.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.freewifi.rohksin.freewifi.R;

public class NotifyMeActivity extends AppCompatActivity {

    private Button start, stop;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_me_activity);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}
