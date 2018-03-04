package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

import com.freewifi.rohksin.freewifi.Activities.TrackWifiActivity;

/**
 * Created by Illuminati on 3/3/2018.
 */

public class IntentUtility {


    public static void startTrackWifiActivity(Context context, ScanResult scanResult)
    {
        Intent intent = new Intent(context, TrackWifiActivity.class);
        intent.putExtra(TrackWifiActivity.TARGET_WIFI,scanResult);
        context.startActivity(intent);
    }

}
