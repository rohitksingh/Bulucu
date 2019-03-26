package com.freewifi.rohksin.freewifi.Activities;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

public class CreateHotspotActivity extends AppCompatActivity {

    private Button button;
    private static final String TAG = "CreateHotspotActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_hotspot_activity_layout);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHotspot();
            }
        });
    }


    public void toggleHotspot()
    {
        WifiManager wifiManager = WifiUtility.getSingletonWifiManager(this);
        Log.d(TAG, "isWifiEnabled: "+wifiManager.isWifiEnabled());
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        wifiManager.setWifiEnabled(!isWifiEnabled);
    }

}
