package com.example.android.ble_scanner;

import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Request code
    public static final int REQUEST_ENABLE_BT = 1;

    private HashMap<String,BLE_Device> mDeviceHashMap;

    private Scanner_BLE mScanner;

    private TextView mDeviceTextView;

    private Button mScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.showToast(getApplicationContext()," BLE not supported");
            finish();

        }
        mDeviceHashMap = new HashMap<String,BLE_Device>();
        mScanner = new Scanner_BLE(this,30000,-75);

        mDeviceTextView = findViewById(R.id.device_text_view);
        mScanButton = findViewById(R.id.scan_button);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mScanner.isScanning()){
                    startScan();
                }else{
                    stopScan();
                }
            }
        });


    }

    public void addDevice(BluetoothDevice device, int new_rssi){

        BLE_Device newDevice = new BLE_Device(device,new_rssi);
        String address = device.getAddress();

        if(!mDeviceHashMap.containsKey(address)){
            // This device is not on the list, add it to the list
            mDeviceHashMap.put(address,newDevice);
        }else{
            // This device is already on the list, update its rssi value
            newDevice.setRssi(new_rssi);
            mDeviceHashMap.put(address,newDevice);

            String info = "Name: "+ newDevice.getName() +
                    "\nAddress: "+ newDevice.getAddress() +
                    "\nRSSI: " + newDevice.getRssi() + "\n-----------------\n";

            mDeviceTextView.append(info);
            Utils.showToast(this,"find a device");
        }


    }

    public void startScan(){
        // when scan button is pressed, clear text view and start scanning
        Utils.showToast(this,"Start scanning");
        mDeviceTextView.setText("");
        mDeviceHashMap.clear();
        mScanner.start();
    }
    public void stopScan(){
         mScanner.stop();
        Utils.showToast(this,"Stop scanning");
    }
}
