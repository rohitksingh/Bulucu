package com.freewifi.rohksin.freewifi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;

/**
 * Created by Illuminati on 3/3/2018.
 */

public class WifiLevelDialog extends Dialog {

    private Context context;
    private TextView textView;

    public WifiLevelDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.wifi_detail_layout);
        textView = (TextView)findViewById(R.id.wifiName);
    }


    public void updateLevel(int level)
    {
        textView.setText(level+"");
    }

}
