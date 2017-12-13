
package com.xiaomi.xmplugindemo;

import android.util.Log;

import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.Parser;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//设备功能处理
public class DemoDevice extends BaseDevice {
    public static final String MODEL = "xiaomi.demo.v1";

    // /////////////属性定义
    // 相对湿度
    public static final String PROP_HUMIDITY = "humidity";
    // rgb灯的颜色值
    public static final String PROP_RGB = "rgb";
    // 摄氏温度
    public static final String PROP_TEMPERATURE = "temperature";

    public static final String PROPERTIES[] = {
            PROP_HUMIDITY, PROP_RGB, PROP_TEMPERATURE,
    };

    // /////////////事件定义
    // 按钮长按
    public static final String EVENT_BUTTON_LONG_PRESSED = "button_long_pressed";
    // 按钮按下
    public static final String EVENT_BUTTON_PRESSED = "button_pressed";

    public static final String EVENTS[] = {
            EVENT_BUTTON_LONG_PRESSED, EVENT_BUTTON_PRESSED,
    };

    // 相对湿度
    public int getHumidity() {
        return TypeUtil.toInteger(mPropertiesMap.get(PROP_HUMIDITY));
    }

    // rgb灯的颜色值
    public int getRgb() {
        return TypeUtil.toInteger(mPropertiesMap.get(PROP_RGB));
    }

    // 摄氏温度
    public int getTemperature() {
        return TypeUtil.toInteger(mPropertiesMap.get(PROP_TEMPERATURE));
    }

    public int getR() {
        int r = (getRgb() & 0x00FF0000) >> 16;
        return r;
    }

    public int getG() {
        int g = (getRgb() & 0x0000FF00) >> 8;
        return g;
    }

    public int getB() {
        int b = getRgb() & 0x000000FF;
        return b;
    }

    // 获取湿度
    public void get_humidity(Callback<Integer> callback) {
        JSONArray params = new JSONArray();
        callMethod("get_humidity", params, callback, new Parser<Integer>() {
            @Override
            public Integer parse(String result) throws JSONException {
                JSONArray res = new JSONArray(result);
                return res.optInt(0);
            }
        });
    }

    // 获取RGB灯颜色
    public void get_rgb(Callback<Integer> callback) {
        JSONArray params = new JSONArray();
        callMethod("get_rgb", params, callback, new Parser<Integer>() {
            @Override
            public Integer parse(String result) throws JSONException {
                JSONArray res = new JSONArray(result);
                return res.optInt(0);
            }
        });
    }

    // 获取温度
    public void get_temperature(Callback<Integer> callback) {
        JSONArray params = new JSONArray();
        callMethod("get_temperature", params, callback, new Parser<Integer>() {
            @Override
            public Integer parse(String result) throws JSONException {
                JSONArray res = new JSONArray(result);
                return res.optInt(0);
            }
        });
    }

    // 设置rgb灯颜色
    public void set_rgb(int rgb, Callback<Void> callback) {
        JSONArray params = new JSONArray();
        params.put(rgb);
        callMethod("set_rgb", params, callback, new Parser<Void>() {
            @Override
            public Void parse(String result) throws JSONException {
                return null;
            }
        });
    }

    // 缓存设备状态数据，每次进入不需要立即更新数据
    private static ArrayList<DemoDevice> DEVICE_CACHE = new ArrayList<DemoDevice>();

    // 先从缓存中获取Device，并更新DeviceStat
    public static synchronized DemoDevice getDevice(DeviceStat deviceStat) {
        for (DemoDevice device : DEVICE_CACHE) {
            if (deviceStat.did.equals(device.getDid())) {
                device.mDeviceStat = deviceStat;
                return device;
            }
        }

        DemoDevice device = new DemoDevice(deviceStat);
        DEVICE_CACHE.add(device);
        return device;
    }

    // 通过did获取Device
    public static synchronized DemoDevice getDevice(String did) {
        for (DemoDevice device : DEVICE_CACHE) {
            if (did.equals(device.getDid())) {
                return device;
            }
        }
        return null;
    }

    protected HashMap<String, Object> mPropertiesMap = new HashMap<String, Object>();

    public DemoDevice(DeviceStat deviceStat) {
        super(deviceStat);
    }

    // 刷新属性
    public void updateProperty(final String[] properties) {
        if (properties == null || properties.length == 0)
            return;
        JSONArray params = new JSONArray();
        for (String prop : properties) {
            params.put(prop);
        }
        callMethod("get_prop", params, new Callback<JSONArray>() {

            @Override
            public void onSuccess(JSONArray result) {
                if (result == null || result.length() == 0)
                    return;
                for (int i = 0; i < result.length() && i < properties.length; i++) {
                    try {
                        Object value = result.get(i);
                        mPropertiesMap.put(properties[i], value);
                    } catch (JSONException e) {
                    }

                }

                notifyStateChanged();
            }

            @Override
            public void onFailure(int error, String errorInfo) {

            }
        }, new Parser<JSONArray>() {
            @Override
            public JSONArray parse(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray res = jsonObject.optJSONArray("result");
                return res;
            }
        });
    }

    // 订阅属性变化，每次只维持3分钟订阅事件
    public void subscribeProperty(String[] props, Callback<Void> callback) {
        ArrayList<String> propList = new ArrayList<String>();
        for (String prop : props) {
            if (prop.startsWith("prop.")) {
                propList.add(prop);
            } else {
                propList.add("prop." + prop);
            }
        }
        XmPluginHostApi.instance()
                .subscribeDevice(getDid(), mDeviceStat.pid, propList, 3, callback);
    }

    // 订阅事件信息，每次只维持3分钟订阅事件
    public void subscribeEvent(String[] events, Callback<Void> callback) {
        ArrayList<String> eventList = new ArrayList<String>();
        for (String event : events) {
            if (event.startsWith("event.")) {
                eventList.add(event);
            } else {
                eventList.add("event." + event);
            }
        }
        XmPluginHostApi.instance().subscribeDevice(getDid(), mDeviceStat.pid, eventList, 3,
                callback);
    }

    // 收到订阅的信息
    public void onSubscribeData(String data) {
        Log.d(MODEL, "DevicePush :" + data);
        // TODO 处理订阅信息
    }

}
