package com.freewifi.rohksin.freewifi.Testing;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.freewifi.rohksin.freewifi.Fragments.AllWifiListFragment;
import com.freewifi.rohksin.freewifi.Fragments.OpenWifiListFragment;
import com.freewifi.rohksin.freewifi.Fragments.WifiFragment;
import com.freewifi.rohksin.freewifi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Illuminati on 5/7/2017.
 */
public class WifiPagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager pager;
    private WifiPagerAdapter adapter;

    private FragmentManager manager;
    public static List<ScanResult> openNetwork;
    public static List<ScanResult> scanResults;

    public  WifiManager wifimanager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_pager_activity);

        registerWifiObserver();
        setUpTabs();

    }


    //***************************************************************************************//
    //               Run time permission (Needed for Android.M and Above)                    //
    //***************************************************************************************//

    @Override
    public void onResume()
    {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }

    //******************************************************************************************//
    //                        Private Helper Methods                                            //
    //******************************************************************************************//

    private void registerWifiObserver()
    {
        wifimanager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);   // After Android.N should be called with Application Context to avoid memory leak
        wifimanager.startScan();
        registerReceiver(new WifiList(),new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }


    private void setUpTabs()
    {
        manager = getSupportFragmentManager();
        adapter = new WifiPagerAdapter(manager,WifiPagerActivity.this,getTabFragments());
        pager = (ViewPager)findViewById(R.id.wifiViewPager);
        pager.setAdapter(adapter);
        tabLayout = (TabLayout)findViewById(R.id.swipeTabs);
        tabLayout.setupWithViewPager(pager);
    }

    private List<Fragment> getTabFragments()
    {
        WifiFragment protectedNetworkFragment = new AllWifiListFragment().newInstance();
        WifiFragment openNetworkFragment = new OpenWifiListFragment().newInstance();
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(openNetworkFragment);
        fragments.add(protectedNetworkFragment);
        return fragments;
    }


    private void getScanResults()
    {
        scanResults = wifimanager.getScanResults();
        openNetwork= new ArrayList<ScanResult>();

        for(ScanResult result : scanResults)
        {
            if(!isProtectedNetwork(result.capabilities))
            {
                openNetwork.add(result);
            }
        }
    }

    private boolean isProtectedNetwork(String capability)
    {
        return (capability.contains("WPA") || capability.contains("WEP") || capability.contains("WPS"));
    }


    //************************************************************************************//
    //               BroadCastReceiver                                                    //
    //************************************************************************************//

    public class WifiList extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                getScanResults();

                adapter.notifyDataSetChanged();
                pager.setAdapter(adapter);
            }

        }
    }

}
