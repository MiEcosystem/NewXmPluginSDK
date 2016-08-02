
package com.xiaomi.smarthome.plugin.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by chenhao on 16/8/1.
 */
public class PluginBaseService extends Service implements IPluginService {

    Service mPluginHostService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void attach(Context context, Service pluginHostService) {
        attachBaseContext(context);
        mPluginHostService = pluginHostService;
    }

    @Override
    public void stopSelfForPlugin() {
        if (mPluginHostService != null) {
            mPluginHostService.stopSelf();
        }
    }

    @Override
    public void stopSelfForPlugin(int startId) {
        if (mPluginHostService != null) {
            mPluginHostService.stopSelf(startId);
        }
    }

    @Override
    public boolean stopSelfResultForPlugin(int startId) {
        if (mPluginHostService != null) {
            return mPluginHostService.stopSelfResult(startId);
        }

        return false;
    }

    @Override
    public void startForegroundForPlugin(int id, Notification notification) {
        if (mPluginHostService != null) {
            mPluginHostService.startForeground(id, notification);
        }
    }

    @Override
    public void stopForegroundForPlugin(boolean removeNotification) {
        if (mPluginHostService != null) {
            mPluginHostService.stopForeground(removeNotification);
        }
    }
}
