package com.example.android.ble_scanner.advertiser;


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.android.ble_scanner.R;
import com.example.android.ble_scanner.advertiser.Advertiser_BLE;

import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertiserFragment extends Fragment {

    private Advertiser_BLE mAdvertiser;

    private ListView mSeviceListView;

    private EditText mLocalNameEditText;

    private List<BluetoothGattService> serviceList;

    private ServiceAdapter mServiceAdapter;
    public AdvertiserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advertiser, container, false);

        mLocalNameEditText = view.findViewById(R.id.server_local_name_edit_text);
        mSeviceListView = view.findViewById(R.id.scanner_service_list);

        // Create a advertiser object.
        mAdvertiser = new Advertiser_BLE(this);
        String localName = mAdvertiser.getLocalName();
        mLocalNameEditText.setText(localName);

        // Display for first Service
        serviceList = mAdvertiser.getServicesList();
        mServiceAdapter = new ServiceAdapter(getContext(),serviceList);
        mSeviceListView.setAdapter(mServiceAdapter);
        return view;
    }

    public void displayServersInfo(BluetoothGattServer server){



    }

    //    public void startAdvertise(){
//        // do sth on UI
//
//        mAdvertiser.startServer();
//        Utils.showToast(getContext(),"Start advertising");
//    }
//
//    public void stopAdvertise(){
//        mAdvertiser.stopAdvertise();
//        Utils.showToast(getContext(),"Stop advertising");
//    }

}
