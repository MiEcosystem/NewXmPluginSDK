
package com.xiaomi.smarthome.plugin.devicesubscribe;

import com.xiaomi.smarthome.plugin.Error;

import org.json.JSONArray;

/**
 * Created by chenhao on 16/6/15.
 */
public interface PluginSubscribeCallback {

    void onSuccess(String subId);

    void onFailure(Error error);

    void onReceive(String did, String model, JSONArray prop);
}
