package com.example.android.ble_scanner.scanner;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.ble_scanner.R;
import com.example.android.ble_scanner.data.DeviceContract;

public class BLE_DeviceAdapter extends CursorAdapter {

    public BLE_DeviceAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.discovered_device_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get Column index for interest device properties
        int nameColumn = cursor.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_NAME);
        int addressColumn = cursor.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_ADDRESS);
        int rssiColumn = cursor.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_RSSI);

        // Get device properties from cursor
         String name = cursor.getString(nameColumn);
         String address = cursor.getString(addressColumn);
         int rssi = cursor.getInt(rssiColumn);

         TextView nameTextView = view.findViewById(R.id.name_text_view);
         TextView addressTextView = view.findViewById(R.id.address_text_view);
         TextView rssiTextView = view.findViewById(R.id.rssi_text_view);

         //write properties on text view
         nameTextView.setText(name);
         addressTextView.setText(address);
         rssiTextView.setText(String.valueOf(rssi));


    }
}
