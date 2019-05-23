package com.example.android.ble_scanner;

import android.bluetooth.BluetoothDevice;

public class BLE_Device {
    private BluetoothDevice mBluetoothDevice;

    private int rssi;

    // row on table
    private long row;

    public BLE_Device(BluetoothDevice bluetoothDevice, int new_rssi){
        this.mBluetoothDevice = bluetoothDevice;
        this.rssi = new_rssi;
    }
    public int getRssi() {
        return rssi;
    }

    public void setRssi(int new_rssi) {
        this.rssi = new_rssi;
    }

    public String getName(){
        return mBluetoothDevice.getName();
    }

    public String getAddress(){
        return mBluetoothDevice.getAddress();
    }

    public long getRow(){
        return this.row;
    }

    public void setRow(long row){
        this.row = row;
    }

    public void getInfo(){
    }
}
