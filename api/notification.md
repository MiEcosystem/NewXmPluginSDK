#Notification相关
使用Notification时，icon只能用App Icon，并且Intent需要设置PluginServiceHelper获取的Intent，还有一点，NotificationBuilder的Context需用activity()，当用户点击通知时，设备在MessageReceiver里面会收到MSG_NOTIFICATION_PENDING_INTENT消息。需要注意，intent里面要保存did，唤起插件时启动activity使用
```
XmPluginHostApi
    /**
     * ApiLevel:45
     * 获取插件notification的icon
     */
    public abstract int getMiHomeNotificationIcon();
    
PluginServiceHelper
    /**
     * ApiLevel: 27
     * 获取调用App的Intent
     * @param model
     * @param params
     * @return
     */
    public static Intent getNotificationIntent(String model, Intent params);
```