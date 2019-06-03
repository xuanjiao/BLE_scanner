package com.example.android.ble_scanner;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        CatagoryPagerAdapter catagoryAdapter = new CatagoryPagerAdapter(getSupportFragmentManager());

        // Get view pager and set PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(catagoryAdapter);

        // Get Tablayout and set up with view pager
        TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tab);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
    }
}
