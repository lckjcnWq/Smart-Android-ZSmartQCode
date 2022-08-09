package com.theswitchbot.common.ble


object BleUuid {
    const val WOAN_UUID=2409

    const val WOFINGER_SERVICE_UUID = "cba20d00-224d-11e6-9fb8-0002a5d5c51b"
    const val WOFINGER_RX_CHAR_UUID = "cba20002-224d-11e6-9fb8-0002a5d5c51b"
    const val WOFINGER_TX_CHAR_UUID = "cba20003-224d-11e6-9fb8-0002a5d5c51b"
    const val WOFINGER_SERDATA_UUID = "00000d00-0000-1000-8000-00805f9b34fb" //广播包中的
    const val NEW_WOFINGER_SERVICE_UUID = "0000fd3d-0000-1000-8000-00805f9b34fb"
    const val NEW_WOFINGER_SERDATA_UUID = "0000fd3d-0000-1000-8000-00805f9b34fb" //广播包中的
    const val WOFINGER_DFU_SERVICE_PARCEL_UUID = "00001530-1212-efde-1523-785feabcd123" //Nordic DFU官方广播
    const val WOFINGER_NEW_DFU_SERVICE_PARCEL_UUID = "0000fe59-0000-1000-8000-00805f9b34fb" //Nordic DFU新官方广播
    const val CCCD = "00002902-0000-1000-8000-00805f9b34fb"
    const val OTA_WOFINGER_SERVICE_UUID = "0000d0ff-3c17-d293-8e48-14fe2e4da212"
}