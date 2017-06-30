
package com.xiaomi.smarthome.device.api;

import android.content.Intent;

/**
 * 插件消息回调 <br/>
 * <br/>
 * ApiLevel:2
 * 
 * @author chenhao
 */
public interface MessageCallback  {
    /**
     * ApiLevel:2
     */
    public void onSuccess(Intent result);

    /**
     * ApiLevel:2
     */
    public void onFailure(int errorCode, String errorInfo);

    
}
