package com.example.android.ble_scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import java.util.Iterator;
import java.util.List;
import java.util.logging.LogRecord;

public class Scanner_BLE {

    private MainActivity ma;

    private DetailActivity da;

    private Handler mHandler;

    private BluetoothAdapter mBluetoothAdapter;

    private boolean mScanning;

    private long scanPeriod;

    private int signalStrength;

    public Scanner_BLE(MainActivity mainActivity, long scanPeriod, int signalStrength){

       this.ma = mainActivity;
       this.scanPeriod = scanPeriod;
       this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager =
                (BluetoothManager)ma.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();
    }


    public void start(){
        if(!Utils.checkBluetooth(mBluetoothAdapter)){
            // Allow user enable bluetooth feature
            Utils.requestUserBluetooth(ma);
            ma.stopScan();
        }else{
            // Start scan for one period.
            scanLeDevice(true);
        }
    }

    public void stop(){
        scanLeDevice(false);
    }

    public void scanLeDevice(boolean enable){
        // If it haven started yet, start scan
        if(enable && !mScanning){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Stop scanning after a given scan period
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                    Utils.showToast(ma.getApplicationContext(),"Time out");
                    ma.stopScan();
                }
            },scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(leScanCallback);
            Utils.showToast(ma.getApplicationContext(),"Start scan BLE devices");
        }

        if(!enable){
            mScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
            Utils.showToast(ma.getApplicationContext(),"Stop scan BLE devices");
        }


    }

    public boolean isScanning() {
        return mScanning;
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            // Called when system find a device
            final int new_rssi = rssi;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(new_rssi > signalStrength){
                       ma.addDevice(device,new_rssi);
                    }

                }
            });
        }
    };

}
