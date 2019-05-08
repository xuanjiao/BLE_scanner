package com.example.android.ble_scanner;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class BLE_Device_Adapter extends CursorAdapter {

    public BLE_Device_Adapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
