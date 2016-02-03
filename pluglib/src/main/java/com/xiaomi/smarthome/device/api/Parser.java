
package com.xiaomi.smarthome.device.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 调用设备的方法结果解析
 */
public interface Parser<T> {
    /**
     * 解析方法
     * 
     * @param result
     * @return
     * @throws JSONException
     */
    public T parse(String result) throws JSONException;

    public static final Parser<JSONObject> DEFAULT_PARSER = new Parser<JSONObject>() {
        @Override
        public JSONObject parse(String result) throws JSONException {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject;
        }
    };
}
