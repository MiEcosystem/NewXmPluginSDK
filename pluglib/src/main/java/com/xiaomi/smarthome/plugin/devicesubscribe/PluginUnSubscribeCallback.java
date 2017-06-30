
package com.xiaomi.smarthome.plugin.devicesubscribe;

import com.xiaomi.smarthome.plugin.Error;

/**
 * Created by chenhao on 16/6/15.
 */
public interface PluginUnSubscribeCallback {
    void onSuccess();

    void onFailure(Error error);
}
