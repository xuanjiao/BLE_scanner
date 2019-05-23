package com.example.android.ble_scanner;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.ble_scanner.data.DeviceContract.DeviceEntry;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {

    // Request code
    public static final int REQUEST_ENABLE_BT = 1;

    private Scanner_BLE mScanner;

    private HashMap<String,BLE_Device> mDeviceHashMap;

    //private BLE_DeviceAdapter mDeviceAdapter;
    private DeviceBaseAdapter mDeviceAdapter;

    private ListView mDeviceListView;

    private Button mScanButton;

    private static int count = 0;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create objects for UI
        mDeviceListView = findViewById(R.id.list);

//
//        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
//                intent.putExtra(EXTRAS_DEVICE_NAME,"unknown");
//                intent.putExtra(EXTRAS_DEVICE_ADDRESS,"unknown");
//                stopScan();
//                startActivity(intent);
//            }
//        });

        mDeviceHashMap = new HashMap<>();

       // getSupportLoaderManager().initLoader(0,null,this);
        // Craete a cursor adapter and adapt it to list view
        //DeviceAdapter = new BLE_DeviceAdapter(getApplicationContext(),null);
        mDeviceAdapter = new DeviceBaseAdapter(mDeviceHashMap,this);
        mDeviceListView.setAdapter(mDeviceAdapter);

        //insertDummyDevice();
        //displayDevices();
        BLE_initialize();

//        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // When user click a device, jump to the detail view and pass name and address
//                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
//                intent.putExtra(EXTRAS_DEVICE_NAME,)
//                startActivity(intent);
//            }
//        });
    }


    private void BLE_initialize(){

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.showToast(getApplicationContext()," BLE not supported");
            finish();
        }

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_dummy_device:
                insertDummyDevice();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertDummyDevice(){
        ContentValues values = new ContentValues();
        values.put(DeviceEntry.COLUMN_NAME,"Device "+String.valueOf(count));
        values.put(DeviceEntry.COLUMN_ADDRESS,"12345");
        values.put(DeviceEntry.COLUMN_RSSI,"-75");
        Uri newUri = getContentResolver().insert(DeviceEntry.CONTENT_URI,values);
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

//        int nameColumn = cursor.getColumnIndex(DeviceEntry.COLUMN_NAME);
//        int addressColumn = cursor.getColumnIndex(DeviceEntry.COLUMN_ADDRESS);
//        int rssiColumn = cursor.getColumnIndex(DeviceEntry.COLUMN_RSSI);
//
//        if(cursor.moveToFirst()){
//            String name = cursor.getString(nameColumn);
//            String address = cursor.getString(addressColumn);
//            int rssi = cursor.getInt(rssiColumn);
//        }
        //mDeviceAdapter.swapCursor(cursor);
    }

    public void addDevice(BluetoothDevice device, int new_rssi){

        BLE_Device newDevice = new BLE_Device(device,new_rssi);

        // Create a content value
        ContentValues values = new ContentValues();
        values.put(DeviceEntry.COLUMN_NAME,newDevice.getName());
        values.put(DeviceEntry.COLUMN_ADDRESS,newDevice.getAddress());
        values.put(DeviceEntry.COLUMN_RSSI,newDevice.getRssi());

        String address = device.getAddress();
        if(!mDeviceHashMap.containsKey(address)){
            // This device is not on the list, add it to the list

            // Insert the content value to table
            Uri newUri = getContentResolver().insert(DeviceEntry.CONTENT_URI,values);

            mDeviceHashMap.put(address,newDevice);
            mDeviceAdapter.addKey(address);
        }else{
            // This device is already on the list, update its rssi value
            newDevice.setRssi(new_rssi);
            mDeviceHashMap.put(address,newDevice);

            String selection = DeviceEntry.COLUMN_ADDRESS + "=?";
            String selectionArgs[] = { address };

            // Update device with specific address
            int rowUpdated = getContentResolver().update(DeviceEntry.CONTENT_URI,values,selection,selectionArgs);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    public void startScan(){
        // when scan button is pressed, clear hashmap and list view
        mDeviceHashMap.clear();

        // tart scanning
        Utils.showToast(this,"Start scanning");
        mScanner.start();
    }
    public void stopScan(){
         mScanner.stop();
        Utils.showToast(this,"Stop scanning");
    }

}
