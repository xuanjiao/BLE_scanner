package com.example.android.ble_scanner;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.ble_scanner.BLE_Device;
import com.example.android.ble_scanner.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceBaseAdapter extends BaseAdapter {

    private HashMap<String,BLE_Device> mDeviceHashmap;

    private ArrayList<String> mKey;
    private Context context;

    public DeviceBaseAdapter(HashMap<String, BLE_Device> mDeviceHashmap, Context context) {
        this.context = context;
        this.mDeviceHashmap = mDeviceHashmap;

        mKey = new ArrayList<String>();
        // Create a set out of the key elements contained in the hash map
        // Transfer this set to key array
        //this.mKey =  mDeviceHashmap.keySet().toArray(new String[mDeviceHashmap.size()]);
    }

    @Override
    public int getCount() {
        return mDeviceHashmap.size();
    }

    public void addKey(String address){
        if(!mKey.contains(address)){
            mKey.add(address);
        }
    }
    @Override
    public Object getItem(int position) {
        return mDeviceHashmap.get(mKey.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        }

        TextView nameTextView = convertView.findViewById(R.id.name_text_view);
        TextView addressTextView = convertView.findViewById(R.id.address_text_view);
        TextView rssiTextView = convertView.findViewById(R.id.rssi_text_view);

        BLE_Device device = mDeviceHashmap.get(mKey.get(position));

        //write properties on text view
        if(device.getName()!=null)
            nameTextView.setText(device.getName());
        else
            nameTextView.setText("Unknown device");


        if(device.getAddress()!= null)
            addressTextView.setText(device.getAddress());
        else
            addressTextView.setText("Unknown address");

        rssiTextView.setText(String.valueOf(device.getRssi()));

        return convertView;
    }
}
