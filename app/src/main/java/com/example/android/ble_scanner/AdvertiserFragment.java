package com.example.android.ble_scanner;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertiserFragment extends Fragment {

    private Advertiser_BLE mAdvertiser;

    public AdvertiserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advertiser, container, false);

        // Create a advertiser object.
        mAdvertiser = new Advertiser_BLE(this);

        // When user click advertise button start advertising. When click again, stop advertising
        Button button = view.findViewById(R.id.start_advertising_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAdvertiser.ismAdvertising())
                    startAdvertise();
                else
                    stopAdvertise();
            }
        });
        return view;
    }

    public void startAdvertise(){
        // do sth on UI


        mAdvertiser.start();
        Utils.showToast(getContext(),"Start advertising");
    }

    public void stopAdvertise(){
        mAdvertiser.stop();
        Utils.showToast(getContext(),"Stop advertising");
    }

}
