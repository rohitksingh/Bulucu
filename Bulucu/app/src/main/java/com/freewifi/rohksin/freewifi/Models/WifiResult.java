package com.freewifi.rohksin.freewifi.Models;

import android.net.wifi.ScanResult;

public class WifiResult {

    private ScanResult scanResult;
    private String date;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return scanResult.SSID + " at: "+date;
    }

}
