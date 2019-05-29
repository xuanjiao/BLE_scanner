package com.example.android.ble_scanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CatagoryPagerAdapter extends FragmentPagerAdapter {

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
}
