package com.xiaomi.smarthome.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Bundle;

import com.xiaomi.smarthome.bluetooth.Response.BleConnectResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleNotifyResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleReadResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleReadRssiResponse;
import com.xiaomi.smarthome.bluetooth.Response.BleWriteResponse;

import java.util.UUID;

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
    public static final int STATUS_ONLINE = 0x30;
    public static final int STATUS_OFFLINE = 0x40;

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
        public static final int REQUEST_OVERFLOW = -11;
        public static final int CONFIG_UNREADY = -12;
        public static final int REQUEST_ONGOING = -13;
        public static final int REQUEST_DENIED = -14;
        public static final int REQUEST_EXCEPTION = -15;

        public static String toString(int code) {
            switch (code) {
                case REQUEST_SUCCESS:
                    return "REQUEST_SUCCESS";
                case REQUEST_FAILED:
                    return "REQUEST_FAILED";
                case REQUEST_CANCELED:
                    return "REQUEST_CANCELED";
                case ILLEGAL_ARGUMENT:
                    return "ILLEGAL_ARGUMENT";
                case BLE_NOT_SUPPORTED:
                    return "BLE_NOT_SUPPORTED";
                case BLUETOOTH_DISABLED:
                    return "BLUETOOTH_DISABLED";
                case CONNECTION_NOT_READY:
                    return "CONNECTION_NOT_READY";
                case REQUEST_TIMEDOUT:
                    return "REQUEST_TIMEDOUT";
                case TOKEN_NOT_MATCHED:
                    return "TOKEN_NOT_MATCHED";
                case REQUEST_OVERFLOW:
                    return "REQUEST_OVERFLOW";
                case CONFIG_UNREADY:
                    return "CONFIG_UNREADY";
                case REQUEST_ONGOING:
                    return "REQUEST_ONGOING";
                case REQUEST_DENIED:
                    return "REQUEST_DENIED";
                case REQUEST_EXCEPTION:
                    return "REQUEST_EXCEPTION";
                default:
                    return "unknown code: " + code;
            }
        }
    }

    /**
     * ApiLevel: 19
     * @param mac
     * @param name
     */
    @Deprecated
    public abstract void deviceRename(String mac, String name);

    /**
     * ApiLevel: 20
     */
    public static final String ACTION_CHARACTER_WRITE = "com.xiaomi.smarthome.bluetooth.character_write";
    public static final String KEY_CHARACTER_WRITE_STATUS = "key_character_write_status";
    public static final String KEY_MISERVICE_CHARACTERS = "key_miservice_characters";
    public static final String EXTRA_UPGRADE_PROCESS = "extra_upgrade_progress";
    public static final String EXTRA_SCANRECORD = "extra.scanRecord";
    public static final String EXTRA_GATT_PROFILE = "key_gatt_profile";
    public static final String EXTRA_TOKEN = "token";

    /**
     * 正在loading
     */
    public static final int PAGE_LOADING = 0;

    /**
     * 当前固件已是最新页
     */
    public static final int PAGE_CURRENT_LATEST = 1;

    /**
     * 当前固件不是最新
     */
    public static final int PAGE_CURRENT_DEPRECATED = 2;

    /**
     * 固件更新中
     */
    public static final int PAGE_UPGRADING = 3;

    /**
     * 固件更新成功
     */
    public static final int PAGE_UPGRADE_SUCCESS = 4;

    /**
     * 固件更新失败
     */
    public static final int PAGE_UPGRADE_FAILED = 5;

    /**
     * ApiLevel: 21
     * 设备安全长连接
     */
    public abstract void bindDevice(String mac);

    /**
     * ApiLevel: 21
     * 取消设备长连接
     *
     */
    public abstract void unBindDevice(String mac);

    /**
     * ApiLevel: 20
     */
    public static final String KEY_FIRMWARE_CLICK = "key_firmware_click";

    /**
     * ApiLevel: 20
     */
    public abstract void secureConnect(String mac, BleConnectResponse response);

    /**
     * ApiLevel: 22
     * @param mac
     * @param code
     * @param args
     */
    public abstract void call(String mac, int code, Bundle args, Response.BleCallResponse response);

    /**
     * ApiLevel: 22
     * @param mac
     * @param delayMillis
     */
    public abstract void disconnect(String mac, long delayMillis);


    /**
     * ApiLevel: 25
     */
    @Deprecated
    public abstract void refreshDeviceStatus(String mac, long durationInMillis, Response.BleDeviceStatusResponse response);

    /**
     * ApiLevel: 25
     */
    public static final int CODE_REFRESH_DEVICE_STATUS = 1;

    /**
     * ApiLevel: 25
     */
    public abstract void startScan(int durationInMillis, int type, BluetoothSearchResponse response);

    /**
     * ApiLevel: 25
     */
    public abstract void stopScan();

    /**
     * ApiLevel: 25
     */
    public static final int SCAN_CLASSIC = 0;
    public static final int SCAN_BLE = 1;

    /**
     * ApiLevel: 25
     */
    public interface BluetoothSearchResponse {
        void onSearchStarted();

        void onDeviceFounded(XmBluetoothDevice device);

        void onSearchStopped();

        void onSearchCanceled();
    }

    /**
     * ApiLevel: 25
     */
    public abstract void registerMediaButtonReceiver(String model);

    /**
     * ApiLevel: 25
     */
    public abstract void unRegisterMediaButtonReceiver(String model);

    /**
     * ApiLevel: 31
     */
    public abstract void removeToken(String mac);

    /**
     * ApiLevel: 31
     */
    public static final int CODE_REMOVE_TOKEN = 1;

    /**
     * ApiLevel: 31
     * 写设备
     */
    public abstract void writeNoRsp(String mac, UUID serviceId, UUID characterId, byte[] bytes, final BleWriteResponse response);

    /**
     * ApiLevel: 32
     * @param mac
     * @return 获取设备连接状态
     */
    public abstract int getConnectStatus(String mac);

    /**
     * ApiLevel: 32
     */
    public static final int STATE_UNKNOWN = -1;
    public static final int STATE_CONNECTED = BluetoothProfile.STATE_CONNECTED;
    public static final int STATE_CONNECTING = BluetoothProfile.STATE_CONNECTING;
    public static final int STATE_DISCONNECTED = BluetoothProfile.STATE_DISCONNECTED;
    public static final int STATE_DISCONNECTING = BluetoothProfile.STATE_DISCONNECTING;

    /**
     * ApiLevel: 32
     */
    public static final String ACTION_RENAME_NOTIFY = "action.more.rename.notify";
    public static final String EXTRA_NAME = "extra.name";
    public static final String EXTRA_RESULT = "extra.result";
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILED = 0;

    /**
     * ApiLevel: 33
     */
    public abstract void startLeScan(int durationInMillis, UUID[] serviceUuids, BluetoothSearchResponse response);

    /**
     * ApiLevel: 33
     */
    public abstract void getBluetoothFirmwareVersion(String mac, Response.BleReadFirmwareVersionResponse response);

    /**
     * ApiLevel: 35
     */
    public static final int ALERT_INCALL_IN_CONTACTS_ENABLED = 1;
    public static final int ALERT_INCALL_NO_CONTACTS_ENABLED = 2;
    public static final int ALERT_ALARM_ENABLED = 3;
    public static final int ALERT_SMS_IN_CONTACTS_ENABLED = 4;
    public static final int ALERT_SMS_NO_CONTACTS_ENABLED = 5;
    public abstract boolean setAlertConfigs(String mac, int alert, boolean enable);
}
