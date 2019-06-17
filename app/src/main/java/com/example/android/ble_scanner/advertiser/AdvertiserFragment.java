package com.example.android.ble_scanner.advertiser;


import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.ble_scanner.R;
import com.example.android.ble_scanner.Utils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertiserFragment extends Fragment {

    private Advertiser_BLE mAdvertiser;

    private ListView mSeviceListView;

    private EditText mLocalNameEditText;

    private TextView mLocalNameLabelTextView;

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

        // Allow user use option advertise_menu
        setHasOptionsMenu(true);

        mLocalNameEditText = view.findViewById(R.id.server_local_name_edit_text);
        mLocalNameLabelTextView = view.findViewById(R.id.server_local_name_label_text_view);
        mSeviceListView = view.findViewById(R.id.scanner_service_list);

        // Create a advertiser object.
        mAdvertiser = new Advertiser_BLE(this);
        serviceList = mAdvertiser.getServicesList();

        mServiceAdapter = new ServiceAdapter(getContext(),serviceList);
        mSeviceListView.setAdapter(mServiceAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.advertiser_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.advertiser_menu_item_advertise:
                if(!mAdvertiser.isAdvertising()){
                    serviceList.clear();
                    startAdvertise();
                    item.setTitle(R.string.stop);
                }else{
                    stopAdvertise();
                    item.setTitle(R.string.advertise);
                }

                break;
                default:
                    return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void startAdvertise(){

        // Display local device name
        mLocalNameLabelTextView.setText("Local Name");
        String localName = mAdvertiser.getLocalName();
        mLocalNameEditText.setText(localName);
        mAdvertiser.startServer();

        // Refresh service list
        mServiceAdapter.notifyDataSetChanged();
        Utils.showToast(getContext(),"Start advertising");
    }

    private void stopAdvertise(){
        Utils.showToast(getContext(),"Stop advertising");
        mAdvertiser.stopAdvertise();

        // Clear all service information
        serviceList.clear();
        mServiceAdapter.notifyDataSetChanged();
    }
    private void advertiseMenuItemSelected(){

        mLocalNameLabelTextView.setText("Local Name");
        String localName = mAdvertiser.getLocalName();
        mLocalNameEditText.setText(localName);

        //  When user click advertise button start advertising. When click again, stop advertising
        if(!mAdvertiser.isAdvertising()){
            Utils.showToast(getContext(),"Start advertising");
            mAdvertiser.startServer();

            // Display services
            serviceList = mAdvertiser.getServicesList();
            mServiceAdapter = new ServiceAdapter(getContext(),serviceList);
            mSeviceListView.setAdapter(mServiceAdapter);

        } else{
            Utils.showToast(getContext(),"Stop advertising");
            mAdvertiser.stopAdvertise();
        }
    }

}
