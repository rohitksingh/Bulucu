package com.freewifi.rohksin.freewifi;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Illuminati on 5/7/2017.
 */

public class DataProvider {

    private static List<String> open;

    public static List<String > getOpenData()
    {
        open = new ArrayList<String>();
        for(int i=0;i<10;i++)
        {
            open.add("Rohit "+i);
        }

        return open;
    }

    public static void setOpenData(List<ScanResult> results)
    {

    }

}
