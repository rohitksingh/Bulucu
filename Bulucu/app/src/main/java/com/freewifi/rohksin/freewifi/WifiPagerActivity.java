package com.freewifi.rohksin.freewifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Illuminati on 5/7/2017.
 */
public class WifiPagerActivity extends AppCompatActivity {

    private WifiPagerAdapter adapter;
    private ViewPager pager;
    private FragmentManager manager;
    private TabLayout tabLayout;

    static List<ScanResult> openNetwork;
    static List<ScanResult> scanResults;

    private WifiManager wifimanager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_pager_activity);

        pager = (ViewPager)findViewById(R.id.wifiViewPager);

        wifimanager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        manager = getSupportFragmentManager();

        registerReceiver(new WifiList(),new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifimanager.startScan();

        WifiFragment protectedNetworkFragment = new AllWifiListFragment().newInstance();

        WifiFragment openNetworkFragment = new OpenWifiListFragment().newInstance();


        List<Fragment> fragments = new ArrayList<Fragment>();

        // if(openNetwork!=null)


        fragments.add(openNetworkFragment);
        fragments.add(protectedNetworkFragment);



        adapter = new WifiPagerAdapter(manager,WifiPagerActivity.this,fragments);
        pager.setAdapter(adapter);
        tabLayout = (TabLayout)findViewById(R.id.swipeTabs);
        tabLayout.setupWithViewPager(pager);

    }


    public class WifiList extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //check intent
            //Do work
            Log.d("rohit", "Inside");

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {

                Log.d("rohit", "Inside method");

                //openNetwork =  new ArrayList<ScanResult>();

                scanResults = wifimanager.getScanResults();


               // Log.d("rohit", "ScanResult empty?"+(scanResults==null));


                openNetwork= new ArrayList<ScanResult>();

                for(ScanResult result : scanResults)
                {
                    //ScanResult wifi = new Wifi();
                    //String capability = result.capabilities;
                    if(!isProtectedNetwork(result.capabilities))
                    {
                        openNetwork.add(result);
                    }

                }


                WifiFragment protectedNetworkFragment = new AllWifiListFragment().newInstance();

                WifiFragment openNetworkFragment = new OpenWifiListFragment().newInstance();


                List<Fragment> fragments = new ArrayList<Fragment>();

               // if(openNetwork!=null)


                fragments.add(openNetworkFragment);
                fragments.add(protectedNetworkFragment);



                adapter = new WifiPagerAdapter(manager,WifiPagerActivity.this,fragments);
                pager.setAdapter(adapter);
                tabLayout = (TabLayout)findViewById(R.id.swipeTabs);
                tabLayout.setupWithViewPager(pager);


            }

        }
    }

    public boolean isProtectedNetwork(String capability)
    {
        return (capability.contains("WPA") || capability.contains("WEP") || capability.contains("WPS"));
    }

}
