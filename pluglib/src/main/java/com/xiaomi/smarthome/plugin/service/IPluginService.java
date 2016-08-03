
package com.xiaomi.smarthome.plugin.service;

import android.app.Notification;
import android.app.Service;

/**
 * ApiLevel: 27 Created by chenhao on 16/8/1.
 */
public interface IPluginService {

    /**
     * ApiLevel: 27
     * 
     * @return
     */
    Service getHostService();

    /**
     * ApiLevel: 27
     */
    void stopSelfForPlugin();

    /**
     * ApiLevel: 27
     * 
     * @param startId
     */
    void stopSelfForPlugin(int startId);

    /**
     * ApiLevel: 27
     * 
     * @param startId
     * @return
     */
    boolean stopSelfResultForPlugin(int startId);

    /**
     * ApiLevel: 27
     * 
     * @param id
     * @param notification
     */
    void startForegroundForPlugin(int id, Notification notification);

    /**
     * ApiLevel: 27
     * 
     * @param removeNotification
     */
    void stopForegroundForPlugin(boolean removeNotification);
}
