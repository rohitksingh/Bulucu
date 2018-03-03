package com.freewifi.rohksin.freewifi.Testing;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Illuminati on 5/7/2017.
 */
public class WifiPagerAdapter extends FragmentPagerAdapter {


    private Context context;
    private List<Fragment> fragments;
    private String[] title = new String[]{"Open","Protected"};

    public WifiPagerAdapter(FragmentManager fm,Context context,List<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        Fragment fragment= fragments.get(position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position)
    {
         return title[position];
    }

}
