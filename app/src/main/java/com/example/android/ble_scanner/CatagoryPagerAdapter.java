package com.example.android.ble_scanner;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CatagoryPagerAdapter extends FragmentPagerAdapter {

    private String [] pageTitles = new String[]{"Advertiser", "Scanner"};

    public CatagoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new ScannerFragment();
        else
            return new AdvertiserFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
