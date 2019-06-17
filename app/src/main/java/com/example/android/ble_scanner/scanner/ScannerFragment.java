package com.example.android.ble_scanner.scanner;

import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.ble_scanner.R;
import com.example.android.ble_scanner.Utils;

import java.util.HashMap;

public class ScannerFragment extends Fragment {

    private Scanner_BLE mScanner;

    private HashMap<String,BLE_Device> mDeviceHashMap;

    private DeviceBaseAdapter mDeviceAdapter;

    private ListView mDeviceListView;

    private Button mScanButton;

    private static int count = 0;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        // Allow user use option scanner_menu
        setHasOptionsMenu(true);

        // Create objects for UI
        mDeviceListView = view.findViewById(R.id.list);

        mDeviceHashMap = new HashMap<>();

        // Create a custom adapter and adapt it to list view
        mDeviceAdapter = new DeviceBaseAdapter(mDeviceHashMap,getContext());
        mDeviceListView.setAdapter(mDeviceAdapter);

        mScanButton = view.findViewById(R.id.scan_button);

        BLE_initialize();
        return view;
    }

    private void BLE_initialize(){

        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.showToast(getContext().getApplicationContext()," BLE not supported");
        }

        // Create a scanner object and give it period and minimal rssi
        mScanner = new Scanner_BLE(this,30000,-75);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scanner_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.scan_menu_item_clear_list:
                // getContext().getContentResolver().delete(DeviceContract.DeviceEntry.CONTENT_URI,null,null);
                mDeviceHashMap.clear();
                mDeviceAdapter.notifyDataSetChanged();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void addDevice(BluetoothDevice device, int new_rssi){

        BLE_Device newDevice = new BLE_Device(device,new_rssi);

//        // Create a content value
//        ContentValues values = new ContentValues();
//        values.put(DeviceContract.DeviceEntry.COLUMN_NAME,newDevice.getName());
//        values.put(DeviceContract.DeviceEntry.COLUMN_ADDRESS,newDevice.getAddress());
//        values.put(DeviceContract.DeviceEntry.COLUMN_RSSI,newDevice.getRssi());

        String address = device.getAddress();
        if(!mDeviceHashMap.containsKey(address)){
            // This device is not on the list, add it to the list

            // Insert the content value to table
           // Uri newUri = getContext().getContentResolver().insert(DeviceContract.DeviceEntry.CONTENT_URI,values);

            mDeviceHashMap.put(address,newDevice);
            mDeviceAdapter.addKey(address);
        }else{
            // This device is already on the list, update its rssi value
            newDevice.setRssi(new_rssi);
            mDeviceHashMap.put(address,newDevice);

//            String selection = DeviceContract.DeviceEntry.COLUMN_ADDRESS + "=?";
//            String selectionArgs[] = { address };

            // Update device with specific address
          //  int rowUpdated = getContext().getContentResolver().update(DeviceContract.DeviceEntry.CONTENT_URI,values,selection,selectionArgs);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    public void startScan(){
        // when scan button is pressed, clear hashmap and list view
        mDeviceHashMap.clear();

        // tart scanning
        Utils.showToast(getContext(),"Start scanning");
        mScanner.start();
    }
    public void stopScan(){
        mScanner.stop();
        Utils.showToast(getContext(),"Stop scanning");
    }

}
