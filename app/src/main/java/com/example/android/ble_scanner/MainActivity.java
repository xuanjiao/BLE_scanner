package com.example.android.ble_scanner;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.ble_scanner.data.DeviceContract.DeviceEntry;
import com.example.android.ble_scanner.data.DeviceDbHelper;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Request code
    public static final int REQUEST_ENABLE_BT = 1;

    private HashMap<String,BLE_Device> mDeviceHashMap;

    private Scanner_BLE mScanner;

    private ListView listView;

    private BLE_DeviceAdapter mDeviceAdapter;
//    private TextView mDeviceTextView;

    private Button mScanButton;

    private static int count = 0;

//    private DeviceDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Craete a cursor adapter and adapt it to list view
        mDeviceAdapter = new BLE_DeviceAdapter(getApplicationContext(),null);
        listView = findViewById(R.id.list);
        listView.setAdapter(mDeviceAdapter);

        // Create objects for UI
//        mDeviceTextView = findViewById(R.id.device_text_view);

//        mDbHelper = new DeviceDbHelper(this);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.showToast(getApplicationContext()," BLE not supported");
            finish();

        }
        mDeviceHashMap = new HashMap<String,BLE_Device>();

        // Create a scanner object and give it period and minimal rssi
        mScanner = new Scanner_BLE(this,30000,-75);

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

        insertDummyDevice();
        //displayDevices();
    }

    private void insertDummyDevice(){
        ContentValues values = new ContentValues();
        values.put(DeviceEntry.COLUMN_NAME,"Device "+String.valueOf(count));
        values.put(DeviceEntry.COLUMN_ADDRESS,"12345");
        values.put(DeviceEntry.COLUMN_RSSI,"-75");
        getContentResolver().insert(DeviceEntry.CONTENT_URI,values);
        count++;
    }

    private void displayDevices(){
        String projection[] = {
                DeviceEntry.COLUMN_ID,
                DeviceEntry.COLUMN_NAME,
                DeviceEntry.COLUMN_ADDRESS,
                DeviceEntry.COLUMN_RSSI
        };
        Cursor cursor = getContentResolver().query(DeviceEntry.CONTENT_URI,projection,null,null,null);

        int nameColumn = cursor.getColumnIndex(DeviceEntry.COLUMN_NAME);
        int addressColumn = cursor.getColumnIndex(DeviceEntry.COLUMN_ADDRESS);
        int rssiColumn = cursor.getColumnIndex(DeviceEntry.COLUMN_RSSI);

        if(cursor.moveToFirst()){
            String name = cursor.getString(nameColumn);
            String address = cursor.getString(addressColumn);
            int rssi = cursor.getInt(rssiColumn);
//            mDeviceTextView.append(name + " - " + address + " - "+String.valueOf(rssi));
        }
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

//            mDeviceTextView.append(info);
            Utils.showToast(this,"find a device");
        }


    }

    public void startScan(){
        // when scan button is pressed, clear text view and start scanning
        Utils.showToast(this,"Start scanning");
//        mDeviceTextView.setText("");
        mDeviceHashMap.clear();
        mScanner.start();
    }
    public void stopScan(){
         mScanner.stop();
        Utils.showToast(this,"Stop scanning");
    }
}
