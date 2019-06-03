package com.example.android.ble_scanner;

import java.util.UUID;

public class BLEServerConstants {
     public static final UUID SERVICE_UUID = UUID.fromString("e5670fa6-57c2-43b9-bf95-796a2c0893cd");

     public static final UUID CHARACTERISTIC_COUNTER_UUID = UUID.fromString("fe538a2f-34b6-496f-8682-f19ce5040ca9");

     public static final UUID CHARACTERISTIC_INTERACTOR_UUID = UUID.fromString("cf64bc6a-7e85-4516-b652-97ca72c690fe");

     // Descriptors define metadata such as description and presentation information.
     public static final UUID DESCRIPTOR_CONFIG_UUID = UUID.fromString("3b926a33-a19b-4d1f-91be-816f45c7d994");

}
