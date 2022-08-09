package com.theswitchbot.common.ble.dto

import android.os.Parcel
import android.os.Parcelable


data class WoBleDevice(
    var deviceAddress: String,                //设备蓝牙地址
    var scanData: ByteArray?=null,           //手机蓝牙扫描到的数据
    var hubData:ByteArray?=null,        //hub扫描到的数据
    var rssi: Int=0,                    //蓝牙信号强度
    var timestampNanos: Long=0,
    var deviceName:String?=null
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.createByteArray(),
        parcel.createByteArray(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    fun scanRecord()=ScanRecord.parseFromBytes(scanData)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deviceAddress)
        parcel.writeByteArray(scanData)
        parcel.writeByteArray(hubData)
        parcel.writeInt(rssi)
        parcel.writeLong(timestampNanos)
        parcel.writeString(deviceName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WoBleDevice> {
        override fun createFromParcel(parcel: Parcel): WoBleDevice {
            return WoBleDevice(parcel)
        }

        override fun newArray(size: Int): Array<WoBleDevice?> {
            return arrayOfNulls(size)
        }
    }
}