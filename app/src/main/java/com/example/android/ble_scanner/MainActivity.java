package com.example.android.ble_scanner;

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

        // Find the view pager that allow user 
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(catagoryAdapter);



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
    }
}
