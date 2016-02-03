package com.xiaomi.smarthome.bluetooth;

import android.content.Context;
import android.os.Bundle;

import java.util.UUID;
import com.xiaomi.smarthome.bluetooth.Response.BleReadRssiResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleNotifyResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleReadResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleWriteResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleConnectResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleConnectResponse2;

/**
 * Created by liwentian on 2015/10/29.
 */
public abstract class XmBluetoothManager {

    /**
     * ApiLevel: 15
     */
    protected static XmBluetoothManager instance = null;

    /**
     * ApiLevel: 15
     */
    public static XmBluetoothManager getInstance() {
        return instance;
    }

    /**
     * ApiLevel: 15
     * 设备连接
     */
    public abstract void connect(String mac, final BleConnectResponse response);


    /**
     * ApiLevel: 17
     * 设备连接
     */
    public abstract void connect(String mac, final BleConnectResponse2 response);

    /**
     * ApiLevel 15
     * 断开连接
     */
    public abstract void disconnect(String mac);

    /**
     * ApiLevel: 15
     * 读设备
     */
    public abstract void read(String mac, UUID serviceId, UUID characterId, final BleReadResponse response);

    /**
     * ApiLevel: 15
     * 写设备
     */
    public abstract void write(String mac, UUID serviceId, UUID characterId, byte[] bytes, final BleWriteResponse response);

    /**
     * ApiLevel: 15
     * notify
     */
    public abstract void notify(String mac, UUID serviceId, UUID characterId, final BleNotifyResponse response);


    /**
     * ApiLevel: 15
     * 取消notify
     * @param mac
     * @param service
     * @param character
     */
    public abstract void unnotify(String mac, UUID service, UUID character);

    /**
     * ApiLevel: 15
     * 读取已连接设备的rssi
     * @param mac
     */
    public abstract void readRemoteRssi(String mac, BleReadRssiResponse response);

    /**
     * ApiLevel: 15
     * 静默打开蓝牙
     */
    public abstract void openBluetoothSilently();


    /**
     * ApiLevel: 15
     * 打开蓝牙，需要询问用户
     */
    public abstract void openBluetooth(Context context);

    /**
     * ApiLevel: 15
     * @return 判断蓝牙是否打开
     */
    public abstract boolean isBluetoothOpen();

    /**
     * ApiLevel: 15
     */
    public static final String ACTION_CONNECT_STATUS_CHANGED = "com.xiaomi.smarthome.bluetooth.connect_status_changed";
    public static final String ACTION_CHARACTER_CHANGED = "com.xiaomi.smarthome.bluetooth.character_changed";


    /**
     * ApiLevel:15
     */
    public static final int REQUEST_CODE_OPEN_BLUETOOTH = 0x10;

    /**
     * ApiLevel: 15
     */
    public static final String KEY_DEVICE_ADDRESS = "key_device_address";
    public static final String KEY_CONNECT_STATUS = "key_connect_status";
    public static final String KEY_SERVICE_UUID = "key_service_uuid";
    public static final String KEY_CHARACTER_UUID = "key_character_uuid";
    public static final String KEY_CHARACTER_VALUE = "key_character_value";
    public static final String KEY_DEVICES = "devices";

    /**
     * ApiLevel:15
     */
    public static final int STATUS_UNKNOWN = 0x5;
    public static final int STATUS_CONNECTED = 0x10;
    public static final int STATUS_DISCONNECTED = 0x20;

    /**
     * ApiLevel: 15
     */
    public static class Code {
        public static final int REQUEST_SUCCESS = 0;
        public static final int REQUEST_FAILED = -1;
        public static final int REQUEST_CANCELED = -2;
        public static final int ILLEGAL_ARGUMENT = -3;
        public static final int BLE_NOT_SUPPORTED = -4;
        public static final int BLUETOOTH_DISABLED = -5;
        public static final int CONNECTION_NOT_READY = -6;
        public static final int REQUEST_TIMEDOUT = -7;
        public static final int TOKEN_NOT_MATCHED = -10;
    }

    /**
     * ApiLevel: 17
     */
    public static class XmBundle {
        public Bundle bundle;
        public Object obj;
    }

    /**
     * ApiLevel: 17
     * @param mac
     * @param name
     */
    public abstract void deviceRename(String mac, String name);
}
