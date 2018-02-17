package com.freewifi.rohksin.freewifi.Activities;

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
import android.util.Log;

import com.freewifi.rohksin.freewifi.Fragments.AllWifiListFragment;
import com.freewifi.rohksin.freewifi.Fragments.OpenWifiListFragment;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Fragments.WifiFragment;
import com.freewifi.rohksin.freewifi.Adapters.WifiPagerAdapter;

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

    public static List<ScanResult> openNetwork;
    public static List<ScanResult> scanResults;

    public  WifiManager wifimanager;


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


    public class WifiList extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //check intent
            //Do work
            Log.d("rohit", "Inside");

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {

                Log.d("rohit", "Inside method");


                scanResults = wifimanager.getScanResults();

                Log.d("rohit", scanResults.size()+"Sizeofresults");


                openNetwork= new ArrayList<ScanResult>();

                for(ScanResult result : scanResults)
                {

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
