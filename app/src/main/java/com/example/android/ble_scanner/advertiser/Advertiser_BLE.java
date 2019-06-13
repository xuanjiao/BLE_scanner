package com.example.android.ble_scanner.advertiser;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_WRITE;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE;
import static android.bluetooth.BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
import static android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_READ;
import static android.bluetooth.BluetoothGattService.SERVICE_TYPE_PRIMARY;
import static com.example.android.ble_scanner.advertiser.BLEServerConstants.*;

public class Advertiser_BLE {

    private String LOG_TAG = this.getClass().getSimpleName();

    private AdvertiserFragment af;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    private boolean mAdvertising;

    private BluetoothGattServer mBluetoothGattServer;

    private int currentCounterValue = 0;

    private List<BluetoothDevice> mRegisteredDevices;

    public Advertiser_BLE(AdvertiserFragment af){

        this.af = af;

        // The BluetoothAdapter is required for any and all Bluetooth activity.
        final BluetoothManager bluetoothManager =
                (BluetoothManager)af.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mBluetoothGattServer = bluetoothManager.openGattServer(af.getContext(),mGattServerCallback);

        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        mAdvertising = false;

        startServer();
    }


    public void startAdvertise(AdvertiseSettings settings, AdvertiseData data){
        mBluetoothLeAdvertiser.startAdvertising(settings,data,mAdvertiseCallback);
        mAdvertising = true;
    }

    public void stopAdvertise(){
        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        mAdvertising = false;
    }

    public void startServer(){
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                // Perform Bluetooth LE advertising in balanced power mode.
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setTimeout(0)
                .build();

        AdvertiseData data = new AdvertiseData.Builder()
                // Add a service UUID to advertise data.
                .addServiceUuid(new ParcelUuid(SERVICE_UUID))
                // Set whether the device name should be included in advertise packet.
                .setIncludeDeviceName(true)
                // Whether the transmission power level should be included in the advertise packet.
                .setIncludeTxPowerLevel(false)
                .build();

        startAdvertise(settings,data);

        mBluetoothGattServer.addService(createService());
    }

    public boolean isAdvertising() {
        return mAdvertising;
    }

    public BluetoothGattService createService(){
        BluetoothGattService service = new BluetoothGattService(SERVICE_UUID,SERVICE_TYPE_PRIMARY);

        // Create a counter characteristic, characteristic supports notification and is readable.
        BluetoothGattCharacteristic counter = new BluetoothGattCharacteristic(
                CHARACTERISTIC_COUNTER_UUID,
                PROPERTY_READ | PROPERTY_NOTIFY,
                PERMISSION_READ);

        // Support subscribtion
        BluetoothGattDescriptor counterConfig = new BluetoothGattDescriptor(
                DESCRIPTOR_CONFIG_UUID,
                PERMISSION_READ | PERMISSION_WRITE
        );
        counter.addDescriptor(counterConfig);

        // Create a interactor notification, characteristic can be written without response.
        BluetoothGattCharacteristic interactor = new BluetoothGattCharacteristic(
                CHARACTERISTIC_INTERACTOR_UUID,
                PROPERTY_WRITE_NO_RESPONSE,
                PERMISSION_WRITE);

        if( !service.addCharacteristic(counter) ||
            !service.addCharacteristic(interactor)){
            Log.w(LOG_TAG,"add characteristic failed");
        }

        return service;
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

    private BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            if(newState == BluetoothProfile.STATE_CONNECTED){
                Log.i(LOG_TAG,"Connected to GATT client");

                // Upon connection establish, show

            }else if (newState == BluetoothProfile.STATE_DISCONNECTED){
                Log.i(LOG_TAG,"Disconnected to GATT client");
            }
        }

//        A remote client has requested to read a local characteristic.
//        BluetoothDevice: The remote device that has requested the read operation
//        requestId	int: The Id of the request
//        offset	int: Offset into the value of the characteristic
//        descriptor	BluetoothGattDescriptor: Descriptor to be read
        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            if(characteristic.getUuid() == CHARACTERISTIC_COUNTER_UUID){
                byte [] value = ByteBuffer.allocate(4).putInt(currentCounterValue).array();
                mBluetoothGattServer.sendResponse(device,requestId,GATT_SUCCESS,0,value);
            }
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            if(characteristic.getUuid() == CHARACTERISTIC_INTERACTOR_UUID){
                currentCounterValue++;

                // When counter value increases, notify all listeners.
                notifyAllRegistedDevices(characteristic);
            }
        }

        // If a device wants to be notified when counter increase, it should write to descriptor.
        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            if(descriptor.getUuid() == DESCRIPTOR_CONFIG_UUID){
                if(Arrays.equals(ENABLE_NOTIFICATION_VALUE,value)){
                    mRegisteredDevices.add(device);
                }else if(Arrays.equals(DISABLE_NOTIFICATION_VALUE,value)){
                    mRegisteredDevices.remove(device);
                }
            }
            if(responseNeeded){
                mBluetoothGattServer.sendResponse(device,requestId,GATT_SUCCESS,0,value);
            }
        }

        private void notifyAllRegistedDevices(BluetoothGattCharacteristic characteristic){
            for(BluetoothDevice device : mRegisteredDevices){

                // Send a notification or indication that a local characteristic has been updated.
                // This function should be invoked for every client that requests notifications/indications
                mBluetoothGattServer.notifyCharacteristicChanged(device,characteristic,false);
            }
        }
        };
}
