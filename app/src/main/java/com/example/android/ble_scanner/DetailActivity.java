package com.example.android.ble_scanner;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public String LOG_TAG = this.getClass().getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView textView;

    private GATTClient mGattClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Show a back arrow on action bar
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        String address = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        textView = findViewById(R.id.service_text_view);

        if(TextUtils.isEmpty(name)){
            name = "Unknown Device";
        }
        textView.setText("Name: "+ name + "\nAddress: "+ address);

        // Create a GATT client for a remote address
        mGattClient = new GATTClient(this,address);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // if back arrow is pressed, go to scan view
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayGattServices(final List<BluetoothGattService> gattServices){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(BluetoothGattService gattService: gattServices){
                    // Get uuid of this service;
                    textView.append("\n---------------------\n");
                    String serv_uuid = gattService.getUuid().toString();

                    textView.append("Service UUID = "+ serv_uuid + "\nCharacteristics:");
                    // Get all characteristic of this service
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

                    for (BluetoothGattCharacteristic gattCharacteristic: gattCharacteristics){
                        String char_uuid = gattCharacteristic.getUuid().toString();
                        textView.append("\nUUID = " + char_uuid);
                    }
                    textView.append("\n---------------------\n");
                }
            }
        });
    }
}
