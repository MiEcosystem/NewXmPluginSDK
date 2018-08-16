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
        // 安全芯片：设备已经被重置，没有注册的Key信息，需要用户重新绑定
        public static final int REQUEST_NOT_REGISTERED = -16;
        // 安全芯片：设备已经被绑定，需要用户解除绑定并且按设备的复位键清除绑定
        public static final int REQUEST_REGISTERED = -17;
        // 安全芯片：分享的钥匙已过期
        public static final int REQUEST_SHARED_KEY_EXPIRED = -18;
        // 安全芯片：共享登录时没有获取到共享的Key
        public static final int REQUEST_SC_SHARED_KEY_FAILED = -19;
        // 安全芯片：注册时验证设备返回的证书和设备签名失败
        public static final int REQUEST_SC_REGISTER_FAILED = -20;
        // 安全芯片：Owner登录时解析设备返回的证书和签名失败
        public static final int REQUEST_SC_LOGIN_ENCRYPT_DATA_FAILED = -21;
        // 安全芯片：Owner登录时设备返回失败
        public static final int REQUEST_SC_LOGIN_FAILED = -22;
        // 安全芯片：共享用户登录时解析设备返回的证书和签名失败
        public static final int REQUEST_SC_SHARED_LOGIN_ENCRYPT_DATA_FAILED = -23;
        // 安全芯片：共享用户登录时设备返回失败
        public static final int REQUEST_SC_SHARED_LOGIN_FAILED = -24;
        // 安全芯片：共享用户登录时获取SharedKeyId为空
        public static final int REQUEST_SC_SHARED_LOGIN_KEY_ID_EMPTY = -25;
        // 安全芯片：Owner登录时绑定LTMK到服务器失败
        public static final int REQUEST_SC_BIND_LTMK_FAILED = -26;
        // 连接设备过程中，Notify操作失败
        public static final int REQUEST_NOTIFY_FAILED = -27;
        // 数据传输过程中，数据发送失败
        public static final int REQUEST_WRITE_FAILED = -28;
        // 普通安全：注册时获取did失败
        public static final int REQUEST_GET_DID_FAILED = -29;
        // 普通安全：注册时绑定did失败
        public static final int REQUEST_BIND_DID_FAILED = -30;
        // 普通安全：登录时验证设备返回的token失败
        public static final int REQUEST_TOKEN_VERIFY_FAILED = -31;
        // 蓝牙连接过程中收到连接断开的广播
        public static final int REQUEST_STATUS_DISCONNECTED = -32;
        // 安全芯片：绑定的时候需要用户在设备输入配对码
        public static final int REQUEST_SC_REGISTER_INPUT_PAIR_CODE = -33;
        // 安全芯片：绑定时设备输入的配对码失败
        public static final int REQUEST_SC_REGISTER_PAIR_CODE_FAILED = -34;
        // 安全芯片：绑定时配对码过期
        public static final int REQUEST_SC_REGISTER_PAIR_CODE_EXPIRED = -35;

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
                case REQUEST_NOT_REGISTERED:
                    return "REQUEST_NOT_REGISTERED";
                case REQUEST_REGISTERED:
                    return "REQUEST_REGISTERED";
                case REQUEST_SHARED_KEY_EXPIRED:
                    return "REQUEST_SHARED_KEY_EXPIRED";
                case REQUEST_SC_SHARED_KEY_FAILED:
                    return "REQUEST_SC_SHARED_KEY_FAILED";
                case REQUEST_SC_REGISTER_FAILED:
                    return "REQUEST_SC_REGISTER_FAILED";
                case REQUEST_SC_LOGIN_ENCRYPT_DATA_FAILED:
                    return "REQUEST_SC_LOGIN_ENCRYPT_DATA_FAILED";
                case REQUEST_SC_LOGIN_FAILED:
                    return "REQUEST_SC_LOGIN_FAILED";
                case REQUEST_SC_SHARED_LOGIN_ENCRYPT_DATA_FAILED:
                    return "REQUEST_SC_SHARED_LOGIN_ENCRYPT_DATA_FAILED";
                case REQUEST_SC_SHARED_LOGIN_FAILED:
                    return "REQUEST_SC_SHARED_LOGIN_FAILED";
                case REQUEST_SC_SHARED_LOGIN_KEY_ID_EMPTY:
                    return "REQUEST_SC_SHARED_LOGIN_KEY_ID_EMPTY";
                case REQUEST_SC_BIND_LTMK_FAILED:
                    return "REQUEST_SC_BIND_LTMK_FAILED";
                case REQUEST_NOTIFY_FAILED:
                    return "REQUEST_NOTIFY_FAILED";
                case REQUEST_WRITE_FAILED:
                    return "REQUEST_WRITE_FAILED";
                case REQUEST_GET_DID_FAILED:
                    return "REQUEST_GET_DID_FAILED";
                case REQUEST_BIND_DID_FAILED:
                    return "REQUEST_BIND_DID_FAILED";
                case REQUEST_TOKEN_VERIFY_FAILED:
                    return "REQUEST_TOKEN_VERIFY_FAILED";
                case REQUEST_STATUS_DISCONNECTED:
                    return "REQUEST_STATUS_DISCONNECTED";
                case REQUEST_SC_REGISTER_INPUT_PAIR_CODE:
                    return "REQUEST_SC_REGISTER_INPUT_PAIR_CODE";
                case REQUEST_SC_REGISTER_PAIR_CODE_FAILED:
                    return "REQUEST_SC_REGISTER_PAIR_CODE_FAILED";
                case REQUEST_SC_REGISTER_PAIR_CODE_EXPIRED:
                    return "REQUEST_SC_REGISTER_PAIR_CODE_EXPIRED";
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
     * ApiLevel: 34
     */
    public static final int CODE_REFRESH_DEVICES = 2;

    /**
     * ApiLevel: 35
     */
    public static final int ALERT_INCALL_IN_CONTACTS_ENABLED = 1;
    public static final int ALERT_INCALL_NO_CONTACTS_ENABLED = 2;
    public static final int ALERT_ALARM_ENABLED = 3;
    public static final int ALERT_SMS_IN_CONTACTS_ENABLED = 4;
    public static final int ALERT_SMS_NO_CONTACTS_ENABLED = 5;
    public abstract boolean setAlertConfigs(String mac, int alert, boolean enable);

    /**
     * ApiLevel: 35
     */
    public static final int CODE_REFRESH_CACHE = 3;

    /**
     * ApiLevel: 43
     * 通过蓝牙读写大数据，response只是返回发送状态，通过设置registerBlockListener监听返回的数据
     */
    public abstract void writeBlock(String mac, byte[] data, final BleWriteResponse response);

    /**
     * ApiLevel: 43
     */
    public abstract void registerBlockListener(String mac, final Response.BleReadResponse response);

    /**
     * ApiLevel: 43
     */
    public abstract void unregisterBlockListener(String mac);

    /**
     * ApiLevel: 43
     * 设置指定的设备断开后是否自动重连
     */
    public abstract boolean setAutoReconnect(String mac, boolean enable);
    /**
     * ApiLevel: 43
     * 设备自动重连开关是否打开
     */
    public abstract boolean isAutoReconnect(String mac);

    /**
     * ApiLevel: 45
     * 监听底层ble蓝牙接收到的数据(就算退出插件也可以收到数据)，监听到的数据主进程通过发送{IXmPluginMessageReceiver.MSG_BLE_CHARACTER_CHANGED}给插件
     * registerCharacterChanged不会主动使能Characteristic的notify接口，必须插件主动调用notify
     */
    public abstract void registerCharacterChanged(String mac, UUID serviceId, UUID characterId, final BleWriteResponse response);

    /**
     * ApiLevel: 45
     * 取消监听底层ble蓝牙接收到的数据
     */
    public abstract void unregisterCharacterChanged(String mac, UUID serviceId, UUID characterId);

    /**
     * ApiLevel: 51
     * 安全芯片连接
     */
    public abstract void securityChipConnect(String mac, final BleConnectResponse response);

    /**
     * ApiLevel: 51
     * 安全芯片数据加密
     */
    public abstract void securityChipEncrypt(String mac, byte[] data, final BleReadResponse response);

    /**
     * ApiLevel: 51
     * 安全芯片数据解密
     */
    public abstract void securityChipDecrypt(String mac, byte[] data, final BleReadResponse response);

    /**
     * ApiLevel: 51
     * 锁的分享设备连接
     * @param mac 设备的mac
     */
    public abstract void securityChipSharedDeviceConnect(String mac, final BleConnectResponse response);

    /**
     * ApiLevel: 51
     * 锁设备分享后的KeyId是否过期
     * @return true：钥匙没有过期，false：钥匙已过期
     */
    public abstract boolean isSecurityChipSharedKeyValid(String mac);

    /**
     * ApiLevel: 51
     * 安全芯片操作符 开锁
     */
    public static final int SECURITY_CHIP_UNLOCK_OPERATOR = 0;
    /**
     * ApiLevel: 51
     * 安全芯片操作符 关锁
     */
    public static final int SECURITY_CHIP_LOCK_OPERATOR = 1;
    /**
     * ApiLevel: 51
     * 安全芯片操作符 反锁
     */
    public static final int SECURITY_CHIP_BOLT_OPERATOR = 2;

    /**
     * ApiLevel: 51
     * 开锁成功后，设备通过notify返回的state码
     */
    private static byte[] SECURITY_CHIP_UNLOCK_STATE = new byte[] {0x00};
    /**
     * ApiLevel: 51
     * 关锁成功后，设备通过notify返回的state码
     */
    private static byte[] SECURITY_CHIP_LOCK_STATE = new byte[] {0x01};
    /**
     * ApiLevel: 51
     * 反锁成功后，设备通过notify返回的state码
     */
    private static byte[] SECURITY_CHIP_BOLT_STATE = new byte[] {0x02};

    /**
     * ApiLevel: 51
     * 提供支持安全芯片的锁操作
     * @param mac
     * @param operator 1: 开锁，2：反锁
     */
    public abstract void securityChipOperate(String mac, int operator, final BleReadResponse response);

    /**
     * ApiLevle: 51
     * 广播接收设备是否登录成功
     */
    public static final String ACTION_ONLINE_STATUS_CHANGED = "action.online.status.changed";
    public static final String EXTRA_MAC = "extra_mac";
    public static final String EXTRA_ONLINE_STATUS = "extra_online_status";
    public static final int STATUS_LOGGED_IN = 0x50;

    /**
     * ApiLevel: 51
     * 获取MD5处理过的token
     * @return
     */
    public abstract String getTokenMd5(String mac);

    /**
     * ApiLevel: 55
     * 判断当前设备是否通过蓝牙网关扫描到了
     * response code = 0 ： 网关扫描到了设备
     *          code != 0, 网关没有扫描到设备
     */
    public abstract void isBleGatewayConnected(String mac, Response.BleResponse<Void> response);

    /**
     * ApiLevel: 62
     * 给锁设备提供的获取一次性密码
     * @param mac
     * @param interval 密码的有效时间，单位为分钟，必须 >= 1 并且 <= 60，否则会返回错误。必须与固件中的有效时间保持一致。
     * @param digits 密码的长度，必须 >= 6 并且 <= 8，否则会返回错误。
     * @param response 返回操作成功还是失败，如果操作成功则返回一次性密码
     */
    public abstract void getOneTimePassword(String mac, int interval, int digits, Response.BleResponseV2<int[]> response);

    /**
     * ApiLevel:66
     * 当前设备列表是否有支持蓝牙网关的设备
     *
     * @param response code = 0 ：有支持蓝牙网关的设备
     *                 code != 0, 没有支持蓝牙网关的设备
     */
    public abstract void isBleGatewayExistInDeviceList(Response.BleResponse<Bundle> response);

}
