
package com.xiaomi.smarthome.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * ApiLevel:5 蓝牙设备
 */
public class XmBluetoothDevice implements Parcelable {

    /**
     * ApiLevel:5
     */
    public BluetoothDevice device;
    /**
     * ApiLevel:5
     */
    public int rssi;
    /**
     * ApiLevel:8
     */
    public boolean isConnected;
    /**
     * ApiLevel:8
     */
    public byte[] scanRecord;
    /**
     * ApiLevel:10
     */
    public int deviceType;

    /**
     * ApiLevel:11
     */
    public String name;

    /**
     * ApiLevel:10
     */
    public static final int DEVICE_TYPE_CLASSIC = 1;

    /**
     * ApiLevel:10
     */
    public static final int DEVICE_TYPE_BLE = 2;

    /**
     * ApiLevel:10
     */
    public XmBluetoothDevice() {

    }

    /**
     * ApiLevel:10
     */
    public XmBluetoothDevice(BluetoothDevice device, int deviceType) {
        this.device = device;
        this.deviceType = deviceType;
    }

    /**
     * ApiLevel:10
     */
    public XmBluetoothDevice(BluetoothDevice device, int rssi, byte[] scanRecord, int deviceType) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
        this.deviceType = deviceType;
    }

    public String getAddress() {
        return device != null ? device.getAddress() : "";
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        sb.append("name = " + name);
        sb.append(", mac = " + device.getAddress());
        sb.append(", connected = " + isConnected);
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.device, 0);
        dest.writeInt(this.rssi);
        dest.writeByte(isConnected ? (byte) 1 : (byte) 0);
        dest.writeByteArray(this.scanRecord);
        dest.writeInt(this.deviceType);
        dest.writeString(this.name);
    }

    public XmBluetoothDevice(Parcel in) {
        this.device = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.rssi = in.readInt();
        this.isConnected = in.readByte() != 0;
        this.scanRecord = in.createByteArray();
        this.deviceType = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<XmBluetoothDevice> CREATOR = new Creator<XmBluetoothDevice>() {
        public XmBluetoothDevice createFromParcel(Parcel source) {
            return new XmBluetoothDevice(source);
        }

        public XmBluetoothDevice[] newArray(int size) {
            return new XmBluetoothDevice[size];
        }
    };
}
