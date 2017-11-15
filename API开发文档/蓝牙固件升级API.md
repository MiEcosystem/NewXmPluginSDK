# 蓝牙固件升级API
## 读取设备固件版本
```Java
/**
 * ApiLevel: 33
 * 必须与设备处于连接状态的时候才能读取
 */
XmBluetoothManager.instance().getBluetoothFirmwareVersion(mac, new Response.BleReadFirmwareVersionResponse() {
    @Override
    public void onResponse(int code, String version) {
        // version类似1.0.3_2001
    }
});
```
## 升级信息查询
<h5>回调的BtFirmwareUpdateInfo中包括插件管理后台最新蓝牙固件信息，包括最新固件的版本、url、changeLog和md5。</h5>

```Java
/**
 * ApiLevel: 16
 */
XmPluginHostApi.instance().getBluetoothFirmwareUpdateInfo(model, new Callback<BtFirmwareUpdateInfo>() {
    @Override
    public void onSuccess(BtFirmwareUpdateInfo btFirmwareUpdateInfo) {

    }

    @Override
    public void onFailure(int error, String msg) {

    }
});
```

## 固件下载
<h5>可通过回调获取下载进度，如果下载完成可获取本地文件路径。</h5>

```Java
/**
 * ApiLevel: 20
 */
XmPluginHostApi.instance().downloadBleFirmware(url, new Response.BleUpgradeResponse() {
    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onResponse(int code, String filePath) {

    }
});
```

<h5>后来新增下载固件接口可获取文件md5。</h5>

```
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

## 取消固件下载

```Java
/**
 * ApiLevel: 28
 */
XmPluginHostApi.instance().cancelDownloadBleFirmware(url);
```

## 固件升级页面
<h5>蓝牙设备的固件升级页面样式是统一的，但逻辑是不同的。所以打开插件通用设置页时，需要传入自定义的固件升级接口，如下：</h5>

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

<h5>如果插件调用的是带下拉列表的菜单项，没法直接调用通用设置页，则需要使用如下方法传入自定义的固件升级接口(该功能只有在ApiLevel >= 52的插件才能使用)：</h5>

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
可参考[蓝牙Demo工程](../plugProject/xiaomi_bledemo)里的固件升级相关代码。
