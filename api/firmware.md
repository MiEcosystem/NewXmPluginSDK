#固件升级相关
```
XmPluginHostApi
    /**
     * ApiLevel:3 获取设备固件升级信息
     *
     * @param callback
     */
    public void getUpdateInfo(String model, String did, int pid,
                              final Callback<DeviceUpdateInfo> callback)
    /**
     * ApiLevel:16 获取蓝牙设备固件升级信息
     */
    public abstract void getBluetoothFirmwareUpdateInfo(String model,
                                                        final Callback<BtFirmwareUpdateInfo> callback);
    /**
     * ApiLevel: 20 下载蓝牙固件
     */
    public abstract void downloadBleFirmware(String url, Response.BleUpgradeResponse response);

    /**
     * ApiLevel: 28 取消下载蓝牙固件
     *
     * @param url
     */
    public abstract void cancelDownloadBleFirmware(String url);
```