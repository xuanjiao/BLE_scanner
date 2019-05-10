package com.example.android.ble_scanner.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.android.ble_scanner.data.DeviceContract.DeviceEntry;

public class DeviceProvider extends ContentProvider {

    private DeviceDbHelper mDeviceDbHelper;

    @Override
    public boolean onCreate() {
        mDeviceDbHelper = new DeviceDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDeviceDbHelper.getReadableDatabase();
        db.query(DeviceEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
