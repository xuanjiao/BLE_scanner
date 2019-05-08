package com.example.android.ble_scanner;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Utils {
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean checkBluetooth(BluetoothAdapter bluetoothAdapter){
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }else{
            return true;
        }
    }

    public static void requestUserBluetooth(Activity activity){
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, MainActivity.REQUEST_ENABLE_BT);
    }
}
