package com.example.android.ble_scanner.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DeviceContract {

    public static final String PATH_DEVICES = "devices";

    public static final String CONTENT_AUTHORITY = "com.example.android.ble_scanner";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final class DeviceEntry implements BaseColumns{

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEVICES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEVICES;

        public static final String TABLE_NAME ="devices";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_DEVICES);

        public static final String COLUMN_ID = BaseColumns._ID;

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_RSSI = "rssi";
    }

}
