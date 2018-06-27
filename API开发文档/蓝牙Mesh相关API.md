# 蓝牙Mesh相关API
## 连接附近的蓝牙Mesh设备

```Java
/**
 * ApiLevel: 70
 * 安全连接蓝牙Mesh设备
 */
XmBluetoothManager.getInstance().bleMeshConnect(mac, new Response.BleConnectResponse() {
    @Override
    public void onResponse(int code, Bundle bundle) {
        if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
            // 连接成功
        }
    }
});
```

## 读取附近蓝牙Mesh设备的固件版本号

```Java
/**
 * ApiLevel: 70
 * 获取蓝牙Mesh设备当前固件版本号（只有连接设备成功后才能获取固件版本号）
 */
XmBluetoothManager.getInstance().getBleMeshFirmwareVersion(mMac, new Response.BleReadFirmwareVersionResponse() {
    @Override
    public void onResponse(int code, String version) {
        if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
            // version类似1.0.3_2001
        }
    }
});
```

## 通过Ble蓝牙接口操作蓝牙Mesh设备
参考[蓝牙基础API](蓝牙基础API.md)文档，里面包含读、写、接收等Ble蓝牙相关接口的操作

## 通过MiotSpec协议发送远程控制命令给Mesh网关

### 远程读取设备属性信息

```Java
/**
 * ApiLevel: 70
 * 根据miotspec协议，读取设备属性
 *
 * @param model 当前设备model
 * @param params 要读取的设备信息，可以一次读取设备的多个属性，格式如下：[{"did": "123", "siid": 1, "piid": 2}, {"did": "124", "siid": 1, "piid": 3}]
 * @param callback 返回读取的多个设备属性信息，"code"为0表示成功，其他表示失败，格式如下：[{"did": "123", "siid": 1, "piid": 2， "value": 10, "code": 0}, {"did": "124", "siid": 1, "piid": 3, "value": "hello", "code": 0}]
 */
XmPluginHostApi.instance().getMiotSpecProp(String model, JSONArray params, Callback<JSONArray> callback);
```

### 远程写设备属性信息

```Java
/**
 * ApiLevel: 70
 * 根据miotspec协议，写设备属性
 *
 * @param model 当前设备model
 * @param params 需要设置的设备属性，可以一次设备设备的多个属性，格式如下：[{"did": "1234", "siid": 1, "piid": 1, "value": 10}, {"did": "1234", "siid": 1, "piid": 88, "value": "hello"}]
 * @param callback 返回写的结果，"code"为0表示成功，其他表示失败，格式如下：[{"did": "1234", "siid": 1, "piid": 1, "code": 0}, {"did": "1234", "siid": 1, "piid": 88, "code": -4003}]
 */
XmPluginHostApi.instance().setMiotSpecProp(String model, JSONArray params, Callback<JSONArray> callback);
```

### 远程执行设备的action方法

```Java
/**
 * ApiLevel: 70
 * 根据miotspec协议，执行设备支持的方法
 *
 * @param model 当前设备model
 * @param did 当前设备did
 * @param siid 设备的service id
 * @param aiid 设备的action id
 * @param in 要执行的action信息，包含property id和要设备的value，格式如下：[{"piid": 1, "vaule": 10}]
 * @param callback 返回action的执行结果，"code"为0表示成功，其他表示失败，格式如下：["did": "1234", "siid": 1, "aiid": 1, "code": 0, "out": ["piid": 3, "value": 10]]
 */
XmPluginHostApi.instance().callMiotSpecAction(String model, String did, int siid, int aiid, int piid, JSONArray in, Callback<String> callback);
```

## 蓝牙Mesh设备OTA升级固件
手机蓝牙连接上蓝牙Mesh设备后，先读取设备的固件版本号，再与从服务端获取的最新固件版本号做比较，如果有新的固件，则需要在通用设置中提示用户升级固件。

### 获取设备固件版本号
调用如上描述的XmBluetoothManager.getInstance().getBleMeshFirmwareVersion()接口。

### 获取服务端最新的固件信息
<h5>回调的BleMeshFirmwareUpdateInfo中包含米家扩展程序管理后台最新蓝牙固件信息，包括最新固件的版本version、safeUrl、url、changeLog和md5。safeUrl是已签名固件的下载链接，url是未签名固件的下载链接，需要下载safeUrl对应的固件地址</h5>

```Java
/**
 * ApiLevel: 70
 * 获取蓝牙设备固件升级信息
 */
XmPluginHostApi.instance().getBleMeshFirmwareUpdateInfo(String model, String did, Callback<BleMeshFirmwareUpdateInfo> callback);
```

### 固件下载
<h5>可通过回调获取下载进度，如果下载完成可获取本地文件路径。</h5>

```Java
/**
 * ApiLevel: 32
 */
XmPluginHostApi.instance().downloadFirmware(url, new Response.FirmwareUpgradeResponse() {
   @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onResponse(int code, String filePath, String md5) {

    }
});
```

### 取消固件下载

```Java
/**
 * ApiLevel: 28
 */
XmPluginHostApi.instance().cancelDownloadBleFirmware(url);
```

### 把下载好的固件传输给蓝牙Mesh设备

```Java
/**
 * ApiLevel: 70
 * 统一的蓝牙Mesh设备固件升级（只有连接设备成功后才能调用升级），非mesh设备不能调用这个升级接口
 * @param mac
 * @param filePath 已下载到本地的固件文件路径
 * @param response 返回固件升级进度，以及升级结果
 */        
XmBluetoothManager.getInstance().startBleMeshUpgrade(mMac, filePath, new Response.BleUpgradeResponse() {
    @Override
    public void onProgress(int progress) {
        // progress下载OTA升级进度
    }

    @Override
    public void onResponse(int code, String errorMsg) {
        if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
            // OTA升级成功
        } else {
            // OTA升级失败                
        }
    }
});
```

### 取消蓝牙Mesh失败的固件升级

```Java
/**
 * ApiLevel: 65+
 * 取消正在执行的蓝牙Mesh固件升级
 * @param mac
 */
XmPluginHostApi.instance().cancelDownloadBleFirmware(String mac);
```

## 固件升级页面
<h5>蓝牙设备的固件升级页面样式是统一的，但逻辑是不同的。所以打开米家扩展程序通用设置页时，需要传入自定义的固件升级接口，如下：</h5>

```Java
findViewById(R.id.title_bar_more).setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        List<MenuItemBase> menus = new ArrayList<MenuItemBase>();
        menus.add(IXmPluginHostActivity.BleMenuItem.newUpgraderItem(new MyUpgrader()));
        hostActivity().openMoreMenu((ArrayList<MenuItemBase>) menus, true, 0);
    }
});
```

<h5>如果米家扩展程序调用的是带下拉列表的菜单项，没法直接调用通用设置页，则需要使用如下方法传入自定义的固件升级接口(该功能只有在ApiLevel >= 52的米家扩展程序才能使用)：</h5>

```Java
findViewById(R.id.title_bar_more).setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        List<MenuItemBase> menus = new ArrayList<MenuItemBase>();
        menus.add(IXmPluginHostActivity.BleMenuItem.newUpgraderItem(new MyUpgrader()));
        hostActivity().openMoreMenu2((ArrayList<MenuItemBase>) menus, true, 0, null);
    }
});
```

<h5>固件升级接口需要继承自BleUpgrader，如下：</h5>

```Java
public class MyUpgrader extends  BleUpgrader  {

    @Override
    public String getCurrentVersion() {
        // 返回当前固件版本
    }

    @Override
    public String getLatestVersion() {
        // 返回最新固件版本
    }

    @Override
    public String getUpgradeDescription() {
        // 返回最新固件升级描述
    }   

    @Override
    public void startUpgrade() {
        // 开始固件升级时回调
    }   

    @Override
    public void onActivityCreated(Bundle bundle) throws RemoteException {
        // 必须得在onActivityCreated调用showPage告诉米家app当前升级状态，不然的话会一直显示Loading页
        // 通知米家app升级页面，当前固件不是最新版本，需要提示用户升级
        showPage(XmBluetoothManager.PAGE_CURRENT_DEPRECATED, null);
    }
}
```

<h5>固件升级过程中，根据状态不同，可以有如下几个页面：</h5>

```Java
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
```

<h5>需要展示某个页面时，只需调用</h5>

```Java
// 必须得在onActivityCreated调用showPage告诉米家app当前升级状态，不然的话会一直显示Loading页
// 如果当前固件已经是最新版本了，调用showPage(XmBluetoothManager. PAGE_CURRENT_LATEST, null);
// 如果当前固件不是最新，需要升级，调用showPage(XmBluetoothManager.PAGE_CURRENT_DEPRECATED, null);
showPage(int pageIndex, Bundle data);
```

<h5>如果要带上进度，则需要赋给Bundle中，如下：</h5>

```Java
Bundle bundle = new Bundle();
bundle.putInt(XmBluetoothManager.EXTRA_UPGRADE_PROCESS, mProgress++);
showPage(XmBluetoothManager.PAGE_UPGRADING, bundle);
```

## 参考Demo
可参考[蓝牙Mesh Demo工程](../plugProject/BleMeshDemo)里的相关代码。
