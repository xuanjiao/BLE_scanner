package com.example.android.ble_scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.example.android.ble_scanner.BLEServerConstants;

import java.util.UUID;

import static android.bluetooth.le.AdvertiseSettings.ADVERTISE_MODE_BALANCED;
import static android.bluetooth.le.AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW;

public class Advertiser_BLE {

    private String LOG_TAG = this.getClass().getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    private AdvertiseSettings settings;

    private AdvertiseData data;

    private boolean mAdvertising;

    public Advertiser_BLE(AdvertiserFragment af){

        // The BluetoothAdapter is required for any and all Bluetooth activity.
        final BluetoothManager bluetoothManager =
                (BluetoothManager)af.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        settings = new AdvertiseSettings.Builder()
                // Perform Bluetooth LE advertising in balanced power mode.
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setTimeout(0)
                .build();

        data = new AdvertiseData.Builder()
                // Add a service UUID to advertise data.
                .addServiceUuid(new ParcelUuid(BLEServerConstants.SERVICE_UUID))
                // Set whether the device name should be included in advertise packet.
                .setIncludeDeviceName(true)
                // Whether the transmission power level should be included in the advertise packet.
                .setIncludeTxPowerLevel(false)
                .build();

        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        mAdvertising = false;
    }


    public void start(){
        mBluetoothLeAdvertiser.startAdvertising(settings,data,mAdvertiseCallback);
        mAdvertising = true;
    }

    public void stop(){
        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        mAdvertising = false;
    }

    public boolean ismAdvertising() {
        return mAdvertising;
    }

    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i(LOG_TAG,"LE Advertise Started");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.w(LOG_TAG,"LE Advertise failed: " + errorCode);
        }


    };
}
