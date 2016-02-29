
package com.xiaomi.smarthome.device.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xiaomi.plugin.core.XmPluginPackage;
/**
 * ApiLevel:6
 */
@Deprecated
public class XmPluginBaseService extends Service {

    IXmPluginHostService mHostService;
    XmPluginPackage mPluginPackage;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void attach(IXmPluginHostService mainService, XmPluginPackage pluginPackage) {
        mHostService = mainService;
        mPluginPackage = pluginPackage;
    }

}
