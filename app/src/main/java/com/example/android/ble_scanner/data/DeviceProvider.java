package com.example.android.ble_scanner.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.android.ble_scanner.data.DeviceContract.DeviceEntry;

public class DeviceProvider extends ContentProvider {

    private String LOG_TAG = this.getClass().getSimpleName();

    private DeviceDbHelper mDeviceDbHelper;

    private UriMatcher mUriMatcher;

    private static final int DEVICES = 1;

    private static final int DEVICE_ID = 2;
    @Override
    public boolean onCreate() {
        mDeviceDbHelper = new DeviceDbHelper(getContext());

        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Multiple rows
        mUriMatcher.addURI(DeviceContract.CONTENT_AUTHORITY,DeviceContract.PATH_DEVICES,DEVICES);

        // Single row
        mUriMatcher.addURI(DeviceContract.CONTENT_AUTHORITY,DeviceContract.PATH_DEVICES + "/#",DEVICES);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDeviceDbHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)){
            case DEVICES:
                break;
            case DEVICE_ID:
                selection = DeviceEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
             default:
                 throw new IllegalArgumentException("no match uri");

        }
        Cursor cursor = db.query(DeviceEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        if( cursor!= null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case DEVICES:
                return DeviceEntry.CONTENT_LIST_TYPE;
            case DEVICE_ID:
                return DeviceEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Unknown uri:"+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDeviceDbHelper.getWritableDatabase();
        long rowInserted;
        Uri newUri = null;
        switch (mUriMatcher.match(uri)){
            case DEVICES:
                rowInserted = db.insert(DeviceEntry.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("no match uri");
        }
        if(rowInserted != 0){
           newUri = ContentUris.withAppendedId(DeviceEntry.CONTENT_URI,rowInserted);
           getContext().getContentResolver().notifyChange(newUri,null);
        }
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDeviceDbHelper.getWritableDatabase();
        int rowDeleted;
        switch (mUriMatcher.match(uri)){
            case DEVICES:
                break;
            case DEVICE_ID:
                selection = DeviceEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
                default:
                throw new IllegalArgumentException("no match uri");

        }
        rowDeleted = db.delete(DeviceEntry.TABLE_NAME,selection,selectionArgs);
        if(rowDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowDeleted;
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDeviceDbHelper.getWritableDatabase();
        int rowUpdated = 0;
        switch (mUriMatcher.match(uri)){
            case DEVICES:
                break;
            case DEVICE_ID:
                selection = DeviceEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
            default:
                throw new IllegalArgumentException("no match uri");
        }
        rowUpdated = db.update(DeviceEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowUpdated;
    }
}
