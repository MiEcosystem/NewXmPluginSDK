# 通知栏相关API
使用Notification时，icon只能用App Icon，并且Intent需要设置PluginServiceHelper获取的Intent。

还有一点，NotificationBuilder的Context需用activity()，当用户点击通知时，设备在MessageReceiver里面会收到MSG\_NOTIFICATION\_PENDING\_INTENT消息。

需要注意，intent里面要保存did，唤起米家扩展程序时启动activity使用。

## XmPluginHostApi
```
/**
 * ApiLevel:45
 * 获取米家扩展程序notification的icon
 */
public abstract int getMiHomeNotificationIcon();
```

## PluginServiceHelper
```
/**
 * ApiLevel: 27
 * 获取调用App的Intent
 * @param model
 * @param params
 * @return
 */
@Deprecated
public static Intent getNotificationIntent(String model, Intent params) {
    return getNotificationIntent(model, "", params);
}

/**
 * ApiLevel: 48
 * 获取调用App的Intent
 * @param model
 * @param did
 * @param params
 * @return
 */
public static Intent getNotificationIntent(String model, String did, Intent params) {
    Intent intent = new Intent();
    intent.setClassName("com.xiaomi.smarthome",
            "com.xiaomi.smarthome.device.utils.DeviceLauncher2");
    intent.setAction("pluignservice.startForeground.notification.pendingIntent");
    intent.putExtra("params", params);
    intent.putExtra("model", model);
    intent.putExtra("did", did);
    return intent;
}
```
