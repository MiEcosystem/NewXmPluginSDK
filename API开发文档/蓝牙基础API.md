# 蓝牙基础API

* [插件开发注意事项](#插件开发注意事项)
* [插件安全连接注意事项](#插件安全连接注意事项)
* [蓝牙插件Demo](#蓝牙插件demo)
* [1、设备连接](#1设备连接)
	* [普通连接](#普通连接)
	* [安全连接](#安全连接)
	* [长连接](#长连接)
	* [断开连接](#断开连接)
	* [获取连接状态](#获取连接状态)
* [2、读取设备](#2读取设备)
* [3、写设备](#3写设备)
* [4、设备通知](#4设备通知)
* [5、读取RSSI](#5读取rssi)
* [6、状态通知](#6状态通知)
* [7、设备更名](#7设备更名)
* [8、数据上报](#8数据上报)
* [9、更改副标题](#9更改副标题)
* [10、开关蓝牙](#10开关蓝牙)
	* [打开蓝牙，需要询问用户](#打开蓝牙需要询问用户)
	* [静默打开蓝牙](#静默打开蓝牙)
	* [判断蓝牙是否打开](#判断蓝牙是否打开)
* [11、扫描蓝牙设备](#11扫描蓝牙设备)
	* [扫描经典蓝牙或BLE蓝牙](#扫描经典蓝牙或ble蓝牙)
	* [扫描BLE蓝牙](#扫描ble蓝牙)
	* [停止蓝牙扫描](#停止蓝牙扫描)
* [12、注册MediaButton广播](#12注册mediabutton广播)
	* [注册MediaButton广播接收器](#注册mediabutton广播接收器)
	* [取消注册MediaButton广播接收器](#取消注册mediabutton广播接收器)
* [13、后台接收蓝牙数据](#13后台接收蓝牙数据)
	* [注册蓝牙数据监听器](#注册蓝牙数据监听器)
	* [取消蓝牙数据监听器](#取消蓝牙数据监听器)
* [14、判断设备是否通过蓝牙网关扫描到](#14判断设备是否通过蓝牙网关扫描到)

------

## 插件开发注意事项
 * 一定要在后台配置好设备的配对方式和绑定关系，有必要的需要上传配对引导图。
 * minApiLevel不要设置得过低，插件如果调用了某个高版本的API，但是minApiLevel设置的低于这个版本，则在低版本米家APP中会崩溃
 * 建议插件中使用米家提供的接口与设备通信，不要使用Android原生蓝牙接口

## 插件安全连接注意事项
 - 不要重复多次调用安全连接，在上一次回调还没回来之前不要再次调用
 - 如果正在连接的时候退出插件了，要立即断开连接，不要设置保持时间
 - 建议插件中要断线自动重连，只有重连三次失败时才提示用户
 - 当token不匹配时调用removeToken清除token
 - 退出插件时要断开连接，可指定一个延时

### 常见问题
 - 如果插件下载到100%后没反应，可能是插件包有问题，检查minSdkLevel是否过高，高于当前手机level，导致getPackageArchiveInfo时返回null

## 蓝牙插件Demo
参考[蓝牙Demo工程](https://github.com/MiEcosystem/NewXmPluginSDK/tree/master/plugProject/xiaomi_bledemo)

## 1、设备连接
<h5>符合MiService协议的设备要走安全连接，如果走普通连接会超时自动断开。</h5>

### 普通连接

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().connect(mac, new Response.BleConnectResponse() {
    @Override
    public void onResponse(int code, Bundle data) {
        if (code == Code.REQUEST_SUCCESS) {
            
        } else {
            
        }
    }
});
```

### 安全连接
<h5>回调中会带上本地设备的token。</h5>

```Java
/**
 * ApiLevel: 20
 */
XmBluetoothManager.getInstance().secureConnect(mac, new Response.BleConnectResponse() {
    @Override
    public void onResponse(int code, Bundle data) {
        if (code == Code.REQUEST_SUCCESS) {
            byte[] token = data.getByteArray(XmBluetoothManager.EXTRA_TOKEN);
        } else {
            
        }
        
    }
});
```

<h5>如果安全连接返回的code为TOKEN_NOT_MATCHED，则调用以下接口清除本地token。</h5>

```
/**
 * ApiLevel: 31
 */
XmBluetoothManager.getInstance().removeToken(mac)
```

### 长连接
<h5>只在MIUI上支持，如果连接失败，则会隔一段时间尝试重连，如果继续失败，则重连间隔会翻倍，直到上限。</h5>

```
/**
 * ApiLevel: 21
 */
void bindDevice(String mac); // 维持长连接
/**
 * ApiLevel: 21
 */
void unBindDevice(String mac); // 解除长连接
```

<h5>支持提醒功能，包括来电，短信或闹钟时通知设备。</h5>

```
/**
 * ApiLevel: 35
 */
public static final int ALERT_INCALL_IN_CONTACTS_ENABLED = 1; // 联系人来电提醒
public static final int ALERT_INCALL_NO_CONTACTS_ENABLED = 2; // 非联系人来电提醒
public static final int ALERT_ALARM_ENABLED = 3;   // 闹钟提醒
public static final int ALERT_SMS_IN_CONTACTS_ENABLED = 4; // 联系人短信提醒
public static final int ALERT_SMS_NO_CONTACTS_ENABLED = 5; // 非联系人短信提醒

public abstract boolean setAlertConfigs(String mac, int alert, boolean enable);
```

### 断开连接
<h5>退出插件时要主动断开设备连接，可以指定一个延时。</h5>

```Java
/**
 * ApiLevel:15
 */
XmBluetoothManager.getInstance().disconnect(mac);
/**
 * ApiLevel:22
 */
XmBluetoothManager.getInstance().disconnect(mac, 10000);
```

### 获取连接状态

```Java
/**
 * ApiLevel: 32
 */
int connState = XmBluetoothManager.getInstance().getConnectStatus(mac);
// XmBluetoothManager.STATE_UNKNOWN
// XmBluetoothManager.STATE_CONNECTED
// XmBluetoothManager.STATE_CONNECTING
// XmBluetoothManager.STATE_DISCONNECTED
// XmBluetoothManager.STATE_DISCONNECTING
```

## 2、读取设备

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().read(mac, serviceUUID, characterUUID, new Response.BleReadResponse() {
    @Override
    public void onResponse(int code, byte[] bytes) {
        if (code == Code.REQUEST_SUCCESS) {

        } else {

        }
    }
});
```

## 3、写设备

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().write(mac, serviceUUID, characterUUID, bytes, new Response.BleWriteResponse() {

    @Override
    public void onResponse(int code, Void data) {

    }
});
```

<h5>如果希望蓝牙写时带WRITE_TYPE_NO_RESPONSE标志，则用如下接口</h5>

```
/**
 * ApiLevel: 31
 */
XmBluetoothManager.getInstance().writeNoRsp(mac, serviceUUID, characterUUID, bytes, new Response.BleWriteResponse() {

    @Override
    public void onResponse(int code, Void data) {

    }
});
```

## 4、设备通知
<h5>打开notify成功后，参考第六条监听notify广播。</h5>

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().notify(mac, serviceUUID, characterUUID, new Response.BleNotifyResponse() {
    @Override
    public void onResponse(int code, Void data) {
        
    }
});

/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().unnotify(mac, serviceUUID, characterUUID);
```

## 5、读取RSSI

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().readRemoteRssi(mac, new Response.BleReadRssiResponse() {
    @Override
    public void onResponse(int code, Integer rssi) {
        
    }
});
```

## 6、状态通知
<h5>这里可监听连接、notify和写状态。</h5>

```Java
IntentFilter filter = new IntentFilter(XmBluetoothManager.ACTION_CHARACTER_CHANGED);
filter.addAction(XmBluetoothManager.ACTION_CONNECT_STATUS_CHANGED);
filter.addAction(XmBluetoothManager.ACTION_CHARACTER_WRITE);
registerReceiver(mReceiver, filter);

private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        String mac = intent.getStringExtra(XmBluetoothManager.KEY_DEVICE_ADDRESS);
        // 收到数据后需要先判断下是不是自己设备发送过来的
        if (TextUtils.equals(mac, mDevice.getMac())) {
            if (XmBluetoothManager.ACTION_CHARACTER_CHANGED.equalsIgnoreCase(action)) {
                UUID service = (UUID) intent.getSerializableExtra(XmBluetoothManager.KEY_SERVICE_UUID);
                UUID character = (UUID) intent.getSerializableExtra(XmBluetoothManager.KEY_CHARACTER_UUID);
                byte[] value = intent.getByteArrayExtra(XmBluetoothManager.KEY_CHARACTER_VALUE);
                processNotify(service, character, value);
            } else if (XmBluetoothManager.ACTION_CONNECT_STATUS_CHANGED.equalsIgnoreCase(action)) {
                int status = intent.getIntExtra(XmBluetoothManager.KEY_CONNECT_STATUS, XmBluetoothManager.STATUS_UNKNOWN);
                if (status == XmBluetoothManager.STATUS_CONNECTED) {

                } else if (status == XmBluetoothManager.STATUS_DISCONNECTED) {

                }
                processConnectStatusChanged(status);
            } else if (XmBluetoothManager.ACTION_CHARACTER_WRITE.equalsIgnoreCase(action)) {
                UUID service = (UUID) intent.getSerializableExtra(XmBluetoothManager.KEY_SERVICE_UUID);
                UUID character = (UUID) intent.getSerializableExtra(XmBluetoothManager.KEY_CHARACTER_UUID);
                byte[] value = intent.getByteArrayExtra(XmBluetoothManager.KEY_CHARACTER_VALUE);
                int status = intent.getIntExtra(XmBluetoothManager.KEY_CHARACTER_WRITE_STATUS, XmBluetoothManager.STATUS_UNKNOWN);
                processCharacterWrited(status);
            }
        }
    }
};
```

## 7、设备更名
<h5>支持同步的设备更名后会同步到云端，否则只是保存在本地。传入设备mac或did皆可。</h5>

```Java
/**
 * ApiLevel: 21
 */
XmBluetoothManager.getInstance().deviceRename(mac, name);
```

<h5>插件中设置页中重命名后会发送广播通知结果，插件可监听如下广播：</h5>

```Java
private class PluginReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "action.more.rename".equals(intent.getAction())) {
            String name = intent.getStringExtra("name");
            int result = intent.getIntExtra("result", 0);
        }
    }
}
```

## 8、数据上报
<h5>上报数据都是封装成XmBluetoothRecord，里面包括type, key, value。其中type只能为TYPE_PROP或TYPE_EVENT。支持多条数据同时上报，回调每条数据的上报结果。</h5>

```Java
List<XmBluetoothRecord> records = new ArrayList<XmBluetoothRecord>();
XmBluetoothRecord record = new XmBluetoothRecord();
record.type = XmBluetoothRecord.TYPE_PROP;
record.key = "color";
record.value = "red";
record.trigger = null;
records.add(record);

/**
 * ApiLevel: 20
 */
XmPluginHostApi.instance().reportBluetoothRecords(did, model, records, new Callback<List<Boolean>>() {

    @Override
    public void onSuccess(List<Boolean> booleans) {

    }

    @Override
    public void onFailure(int i, String s) {

    }
};
```

## 9、更改副标题

```Java
/**
 * ApiLevel: 20
 */
XmPluginHostApi.instance().setBleDeviceSubtitle(mac, subtitle);
```

## 10、开关蓝牙

### 打开蓝牙，需要询问用户

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().openBluetooth(context);
```

### 静默打开蓝牙

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().openBluetoothSilently();
```

### 判断蓝牙是否打开

```Java
/**
 * ApiLevel: 15
 */
XmBluetoothManager.getInstance().isBluetoothOpen();
```

## 11、扫描蓝牙设备

### 扫描经典蓝牙或BLE蓝牙

```Java
/**
 * ApiLevel: 25
 * durationInMillis为扫描持续时间
 * type: XmBluetoothManager.SCAN_CLASSIC 扫描经典蓝牙，type: XmBluetoothManager.SCAN_BLE 扫描BLE蓝牙
 */
XmBluetoothManager.getInstance().startScan(durationInMillis, type, new XmBluetoothManager.BluetoothSearchResponse() {
	@Override
	public void onSearchStarted() {

	}

	@Override
	public void onDeviceFounded(XmBluetoothDevice xmBluetoothDevice) {

	}

	@Override
	public void onSearchStopped() {

	}

	@Override
	public void onSearchCanceled() {

	}
});
```

### 扫描BLE蓝牙
```Java
/**
 * ApiLevel: 33
 * durationInMillis为扫描持续时间
 * serviceUuids为要过滤的service uuid
 */
XmBluetoothManager.getInstance().startLeScan(durationInMillis, serviceUuids, new XmBluetoothManager.BluetoothSearchResponse() {
	@Override
	public void onSearchStarted() {

	}

	@Override
	public void onDeviceFounded(XmBluetoothDevice xmBluetoothDevice) {

	}

	@Override
	public void onSearchStopped() {

	}

	@Override
	public void onSearchCanceled() {

	}
});
```

### 停止蓝牙扫描

```Java
/**
 * ApiLevel: 25
 */
XmBluetoothManager.getInstance().stopScan();
```

## 12、注册MediaButton广播

### 注册MediaButton广播接收器

```Java
/**
 * ApiLevel: 25
 * IXmPluginMessageReceiver中，MSG_BROADCAST返回MediaButton事件
 */
XmBluetoothManager.getInstance().registerMediaButtonReceiver(model);
```

### 取消注册MediaButton广播接收器

```Java
/**
 * ApiLevel: 25
 */
XmBluetoothManager.getInstance().unRegisterMediaButtonReceiver(model);
```

## 13、后台接收蓝牙数据

### 注册蓝牙数据监听器
```Java
/**
 * ApiLevel: 45
 * 监听底层ble蓝牙接收到的数据(就算退出插件也可以收到数据)，监听到的数据主进程通过发送{IXmPluginMessageReceiver.MSG_BLE_CHARACTER_CHANGED}给插件
 * registerCharacterChanged不会主动使能Characteristic的notify接口，必须插件主动调用notify
 */
XmBluetoothManager.getInstance().registerCharacterChanged(mac, serviceId, characterId, new Response.BleWriteResponse() {

	@Override
	public void onResponse(int code, Void aVoid) {
		
	}
});
```

### 取消蓝牙数据监听器
```Java
/**
 * ApiLevel: 45
 * 取消registerCharacterChanged注册的监听器
 */
XmBluetoothManager.getInstance().unregisterCharacterChanged(mac);
```

## 14、判断设备是否通过蓝牙网关扫描到

```Java
/**
 * ApiLevel: 55
 * 判断当前设备是否通过蓝牙网关扫描到了
 * response code = 0 ： 网关扫描到了设备
 *          code != 0, 网关没有扫描到设备
 */
public abstract void isBleGatewayConnected(String mac, Response.BleResponse<Void> response);
```

