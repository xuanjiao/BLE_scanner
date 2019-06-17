package com.example.android.ble_scanner.scanner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.ble_scanner.R;

import java.util.ArrayList;
import java.util.HashMap;


public class DeviceBaseAdapter extends BaseAdapter {

    private HashMap<String,BLE_Device> mDeviceHashmap;

    private ArrayList<String> mKey;

    private Context context;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

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

    public void removeAllKeys(){
        mKey.clear();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.discovered_device_list_item,parent,false);
        }

        TextView nameTextView = convertView.findViewById(R.id.name_text_view);
        TextView addressTextView = convertView.findViewById(R.id.address_text_view);
        TextView rssiTextView = convertView.findViewById(R.id.rssi_text_view);

        BLE_Device device = mDeviceHashmap.get(mKey.get(position));

        final String name = device.getName();
        final String address = device.getAddress();
        int rssi = device.getRssi();

        //write properties on text view
        if(name!=null)
            nameTextView.setText(name);
        else
            nameTextView.setText("Unknown device");

        if(address!= null)
            addressTextView.setText(address);
        else
            addressTextView.setText("Unknown address");

        rssiTextView.setText(String.valueOf(rssi));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // When user click a device, jump to the detail view and pass name and address

                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra(EXTRAS_DEVICE_NAME,name);
                intent.putExtra(EXTRAS_DEVICE_ADDRESS,address);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
