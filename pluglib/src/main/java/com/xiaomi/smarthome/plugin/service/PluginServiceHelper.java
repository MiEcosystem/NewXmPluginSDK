
package com.xiaomi.smarthome.plugin.service;

import android.content.Intent;

/**
 * ApiLevel: 27 Created by chenhao on 16/8/23.
 */
public class PluginServiceHelper {

    /**
     * ApiLevel: 27
     * 
     * @param model
     * @param params
     * @return
     */
    public static Intent getNotificationIntent(String model, Intent params) {
        Intent intent = new Intent();
        intent.setClassName("com.xiaomi.smarthome",
                "com.xiaomi.smarthome.framework.navigate.SmartHomeLauncher");
        intent.setAction("pluignservice.startForeground.notification.pendingIntent");
        intent.putExtra("params", params);
        intent.putExtra("model", model);
        return intent;
    }
}
