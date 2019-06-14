package com.example.android.ble_scanner.advertiser;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.ble_scanner.R;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE;

public class ServiceAdapter extends ArrayAdapter<BluetoothGattService> {

    public ServiceAdapter(Context context,List<BluetoothGattService> objects) {
        super(context,0, objects);
    }


    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        // If there is no view to convert, generate a new view.
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_list_item,parent,false);


//
        EditText uuidEditText = convertView.findViewById(R.id.service_uuid_edit_text);
        EditText characteristicEditText = convertView.findViewById(R.id.characteristic_edit_text);

        BluetoothGattService service = getItem(position);
        UUID uuid = service.getUuid();

        // Display service uuid
        uuidEditText.setText(uuid.toString());

        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristicList){

            // Display information for each characteristic.
            String text = composeCharacteristicInfo(characteristic);
            characteristicEditText.append(text);
        }

        return convertView;
    }

    private String composeCharacteristicInfo(BluetoothGattCharacteristic characteristic){
        StringBuilder builder = new StringBuilder();

        // Compose characteristic UUID.
        UUID uuid = characteristic.getUuid();
        builder.append("\nUUID: ").append(uuid.toString()).append("\n");

        // Compose characteristic properties.
        builder.append("\nProperties: ");
        if((characteristic.getProperties() & PROPERTY_READ)!=0){
            builder.append("Read ");
        }

        if((characteristic.getProperties() & PROPERTY_WRITE_NO_RESPONSE)!=0){
            builder.append("Write without response ");
        }

        if((characteristic.getProperties() & PROPERTY_NOTIFY)!=0){
            builder.append("Notify ");
        }

        if((characteristic.getProperties() & PROPERTY_WRITE)!=0){
            builder.append("Write ");
        }

    //    characteristic.getValue();
    //    Integer value = characteristic.getIntValue(FORMAT_UINT16,0);


        // Compose characteristic value
        byte[] value = characteristic.getValue();
        builder.append("\nValue: ").append(value);


        // Compose characteristic descriptors
        builder.append("\nDescriptor: ");
        for( BluetoothGattDescriptor descriptor: characteristic.getDescriptors()){
            builder.append(descriptor.toString()).append(" ");
        }
        builder.append("\n");

        return  builder.toString();
    }
}
