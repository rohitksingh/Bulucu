package com.freewifi.rohksin.freewifi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;

/**
 * Created by Illuminati on 3/3/2018.
 */

public class WifiStatDialog extends Dialog {


    private TextView properties;
    private TextView wifiName;
    private ScanResult scanResult;
    private Context context;
    private TextView securityType;


    public WifiStatDialog(@NonNull Context context, ScanResult scanResult) {
        super(context);
        this.context = context;
        this.scanResult = scanResult;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_wifistat_layout);

        properties =findViewById(R.id.property);
        wifiName = findViewById(R.id.wifiName);
        securityType = findViewById(R.id.securityType);
        securityType.setText(scanResult.capabilities);
        wifiName.setText(scanResult.SSID+"");
        properties.setText("BSSID: "+scanResult.BSSID);

    }

}
