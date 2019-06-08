package com.example.android.ble_scanner;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class GATTClient {

    public String LOG_TAG = this.getClass().getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothDevice device;

    private BluetoothGatt mGatt;

    private Context context;

    private DetailActivity da;


    public GATTClient(DetailActivity da, String address){
        this.da = da;
        context = da.getApplicationContext();

        final BluetoothManager bluetoothManager = (BluetoothManager)da.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        device = mBluetoothAdapter.getRemoteDevice(address);
        // Connect to a GATT server
        mGatt = device.connectGatt(context,false,gattCallback);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED){
                // Called when the GATT connection succeeds
                Log.i(LOG_TAG,"Connected to GATT server. Attempting to start service discovery");
                mGatt.discoverServices();
            }else if (newState == BluetoothProfile.STATE_DISCONNECTED){
                Log.i(LOG_TAG,"Disconnected from GATT server");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if(status != BluetoothGatt.GATT_SUCCESS){
                Log.v(LOG_TAG,"Service discovery failed. Status = " + status);
                return;
            }
            da.displayGattServices(gatt.getServices());
        }
    };
}
