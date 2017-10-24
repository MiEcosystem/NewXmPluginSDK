
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
    @Deprecated
    public static Intent getNotificationIntent(String model, Intent params) {
        return getNotificationIntent(model, "", params);
    }

    /**
     * ApiLevel: 48
     *
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
}
