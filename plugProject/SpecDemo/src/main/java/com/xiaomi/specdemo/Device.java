package com.xiaomi.specdemo;

import android.util.Log;

import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.Parser;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

//设备功能处理
public class Device extends BaseDevice {
    public static final String MODEL = "homwee.tv.q5k";

    // 缓存设备状态数据，每次进入不需要立即更新数据
    private static ArrayList<Device> DEVICE_CACHE = new ArrayList<Device>();

    // 先从缓存中获取Device，并更新DeviceStat
    public static synchronized Device getDevice(DeviceStat deviceStat) {
        for (Device device : DEVICE_CACHE) {
            if (deviceStat.did.equals(device.getDid())) {
                device.mDeviceStat = deviceStat;
                return device;
            }
        }

        Device device = new Device(deviceStat);
        DEVICE_CACHE.add(device);
        return device;
    }

    // 通过did获取Device
    public static synchronized Device getDevice(String did) {
        for (Device device : DEVICE_CACHE) {
            if (did.equals(device.getDid())) {
                return device;
            }
        }
        return null;
    }

    public Device(DeviceStat deviceStat) {
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
                //TODO 更新属性

                notifyStateChanged();
            }

            @Override
            public void onFailure(int error, String errorInfo) {

            }
        }, new Parser<JSONArray>() {
            @Override
            public JSONArray parse(String result) throws JSONException {
                JSONArray res = new JSONArray(result);
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
        Log.d(Device.MODEL, "DevicePush :" + data);
        //TODO 处理订阅信息
    }


    // 取消订阅属性，在订阅后，再次订阅前取消，避免重复订阅
    public void unSubscribeProperty(String[] props, Callback<Void> callback) {
        ArrayList<String> propList = new ArrayList<String>();
        for (String prop : props) {
            if (prop.startsWith("prop.")) {
                propList.add(prop);
            } else {
                propList.add("prop." + prop);
            }
        }
        XmPluginHostApi.instance()
                .unsubscribeDevice(getDid(), mDeviceStat.pid, propList, callback);
    }

    // 取消订阅事件消息，在订阅后，再次订阅前取消，避免重复订阅
    public void unSubscribeEvent(String[] events, Callback<Void> callback) {
        ArrayList<String> eventList = new ArrayList<String>();
        for (String event : events) {
            if (event.startsWith("event.")) {
                eventList.add(event);
            } else {
                eventList.add("event." + event);
            }
        }
        XmPluginHostApi.instance().unsubscribeDevice(getDid(), mDeviceStat.pid, eventList,
                callback);
    }

}

