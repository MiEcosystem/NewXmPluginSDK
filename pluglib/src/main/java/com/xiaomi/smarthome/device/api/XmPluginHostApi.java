
package com.xiaomi.smarthome.device.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.xiaomi.plugin.core.XmPluginPackage;
import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothRecord;
import com.xiaomi.smarthome.camera.HLSDownloader;
import com.xiaomi.smarthome.camera.XmMp4Record;
import com.xiaomi.smarthome.camera.XmVideoViewGl;
import com.xiaomi.smarthome.device.api.printer.PrinterControl;
import com.xiaomi.smarthome.camera.exopackage.MJExoPlayer;
import com.xiaomi.smarthome.plugin.devicesubscribe.PluginSubscribeCallback;
import com.xiaomi.smarthome.plugin.devicesubscribe.PluginUnSubscribeCallback;
import com.xiaomi.smarthome.plugin.service.HostService;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 插件宿主提供给插件的接口
 */
public abstract class XmPluginHostApi {
    public final static String METHOD_POST = "POST";
    public final static String METHOD_GET = "GET";

    protected static XmPluginHostApi sXmPluginHostApi = null;

    public static XmPluginHostApi instance() {
        return sXmPluginHostApi;
    }

    /**
     * 插件sdk版本号，插件开发如果必须要与之前api兼容，需要通过SDK_VERSION判断兼容性
     */
    public abstract int getApiLevel();

    /**
     * ApiLevel: 18 主app渠道
     *
     * @return
     */
    public abstract String getChannel();

    /**
     * ApiLevel:1
     */
    public abstract Application application();

    /**
     * ApiLevel:1
     */
    public abstract Context context();

    // 启动插件包内部某个activity

    /**
     * ApiLevel:1
     *
     * @param xmPluginPackage 插件包
     * @param intent          传入的参数
     * @param did             设备did
     * @param activityClass   启动的activity
     */
    public abstract void startActivity(Context context, XmPluginPackage xmPluginPackage,
                                       Intent intent,
                                       String did, Class activityClass);

    /**
     * ApiLevel:1 调用智能家居后台http服务
     *
     * @param model       插件model
     * @param relativeUrl 服务接口url
     * @param params
     * @param callback
     * @param parser
     */
    public abstract <T> void callSmartHomeApi(String model, String relativeUrl, JSONObject params,
                                              final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:58 调用智能家居后台http服务
     *
     * @param model       插件model
     * @param hostPrefix  HOST前缀
     * @param relativeUrl 服务接口url
     * @param method      请求方法
     * @param params
     * @param callback
     * @param parser
     */
    public abstract <T> void callSmartHomeApi(String model, String hostPrefix, String relativeUrl, String method, JSONObject params,
                                              final Callback<T> callback, final Parser<T> parser);

    // method POST or GET

    /**
     * ApiLevel:1 调用普通http请求
     *
     * @param model    插件model
     * @param url      请求url
     * @param method   METHOD_POST或METHOD_GET
     * @param params
     * @param callback
     * @param parser
     */
    @Deprecated
    public abstract <T> void callHttpApi(String model, String url, String method,
                                         List<NameValuePair> params, final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel: 13 调用普通http请求
     *
     * @param model
     * @param url
     * @param method
     * @param params
     * @param callback
     * @param parser
     * @param <T>
     */
    public abstract <T> void callHttpApiV13(String model, String url, String method,
                                            List<KeyValuePair> params, final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:1 设备方法调用
     *
     * @param method   方法名
     * @param params   参数，可以是一个集合Collection子类
     * @param callback 回调结果
     * @param parser
     */
    public abstract <T> void callMethod(String did, String method, Object[] params,
                                        final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:1 设备方法调用
     *
     * @param method   方法名
     * @param params
     * @param callback 回调结果
     * @param parser
     */
    public abstract <T> void callMethod(String did, String method, JSONArray params,
                                        final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:59 设备方法调用，强制从云端调用，object为JSONObject或者为JSONArray
     *
     * @param method   方法名
     * @param params
     * @param callback 回调结果
     * @param parser
     */
    public abstract <T> void callMethodFromCloud(String did, String method, Object params,
                                                 final Callback<T> callback, final Parser<T> parser);


    /**
     * ApiLevel:29 设备方法调用
     *
     * @param method   方法名
     * @param params
     * @param callback 回调结果
     * @param parser
     */
    public abstract <T> void callMethod(String did, String method, JSONObject params,
                                        final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:1 获取设备列表
     *
     * @return
     */
    public abstract List<DeviceStat> getDeviceList();

    /**
     * ApiLevel:30 获取设备列表
     *
     * @return 打印机的控制类
     */
    public abstract PrinterControl getPrinterControl();

    /**
     * ApiLevel: 30 获取指定model的设备列表
     *
     * @return
     */
    public abstract List<DeviceStat> getDeviceListV2(List<String> modelList);

    /**
     * ApiLevel: 69 获取当前家庭所有房间
     *
     * @return
     */
    public abstract List<RoomStat> getRoomAll();

    /**
     * ApiLevel: 69 删除房间
     *
     * @param roomIds
     * @param callback
     */
    public abstract void deleteRoom(final List<String> roomIds, final Callback<Void> callback);

    /**
     * ApiLevel: 69 房间重命名
     *
     * @param roomId
     * @param name
     * @param callback
     */
    public abstract void renameRoom(final String roomId, final String name, final Callback<Void> callback);

    /**
     * ApiLevel:1 获取子设备
     *
     * @param did 设备did
     * @return
     */
    public abstract List<DeviceStat> getSubDeviceByParentDid(String did);

    /**
     * ApiLevel:1 获取设备信息
     *
     * @param did
     * @return
     */
    public abstract DeviceStat getDeviceByDid(String did);


    /**
     * ApiLevel:1 获取子设备
     *
     * @param didList
     * @param callback
     */
    public void getSubDevice(String model, String[] didList,
                             final Callback<List<DeviceStat>> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            if (didList != null) {
                JSONArray array = new JSONArray();
                for (String did : didList) {
                    array.put(did);
                }
                dataObj.put("dids", array);
            }

            WifiManager wifi = (WifiManager) context().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String bssid = info != null ? info.getBSSID() : null;

            if (!TextUtils.isEmpty(bssid)) {
                dataObj.put("uid", bssid.toUpperCase());
            }
        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        // [{"uid":103434651,"did":"lumi.158d00005e90f8","mac":"","pd_id":42,"city_id":101010200,"bssid":"","token":"","access_type":3,"localip":"","name":"门窗传感器","ssid":"","longitude":116.330283,"latitude":40.028534,"parent_id":"88292"},{"uid":103434651,"did":"lumi.158d00005e9267","mac":"","pd_id":41,"city_id":0,"bssid":"","token":"","access_type":3,"localip":"","name":"无线开关","ssid":"","longitude":0,"latitude":0,"parent_id":"88292"}]
        callSmartHomeApi(model, "/home/sub_device_list", dataObj, callback,
                new Parser<List<DeviceStat>>() {

                    @Override
                    public List<DeviceStat> parse(String resp) throws JSONException {
                        List<DeviceStat> subDeviceList = new ArrayList<DeviceStat>();
                        JSONObject response = new JSONObject(resp);
                        JSONArray array = response.optJSONArray("list");
                        if (array == null)
                            return subDeviceList;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            DeviceStat result = new DeviceStat();
                            result.did = object.optString("did");
                            result.model = object.optString("model");
                            result.name = object.optString("name");
                            result.bindFlag = object.optInt("adminFlag");
                            result.authFlag = object.optInt("shareFlag");
                            result.ip = object.optString("localip");
                            result.mac = object.optString("mac");
                            result.parentModel = object.optString("parent_model");
                            result.parentId = object.optString("parent_id");
                            // // 对于子设备，一律认为在线
                            // if (!TextUtils.isEmpty(result.parentId)) {
                            // result.isOnline = true;
                            // } else {
                            // result.isOnline = object.optBoolean("isOnline");
                            // }
                            result.isOnline = object.optBoolean("isOnline");
                            subDeviceList.add(result);
                        }
                        return subDeviceList;
                    }
                });

    }

    /**
     * ApiLevel:1 订阅设备属性变化 变化后push消息通知 需要在IXmPluginMessageReceiver里面接收消息通知
     *
     * @param did
     * @param pid
     * @param entryList 属性列表 属性必须加 prop.
     * @param expire    分钟 必须<=3
     * @param callback
     */
    public abstract void subscribeDevice(String did, int pid, List<String> entryList, int expire,
                                         Callback<Void> callback);

    /**
     * ApiLevel: 13 取消订阅
     *
     * @param did
     * @param pid
     * @param entryList
     * @param callback
     */
    public abstract void unsubscribeDevice(String did, int pid, List<String> entryList,
                                           Callback<Void> callback);

    /**
     * ApiLevel: 25 订阅设备属性变化(跟model无关) 变化后push消息通知 需要在IXmPluginMessageReceiver里面接收消息通知
     *
     * @param did
     * @param pid
     * @param entryList 属性列表 属性必须加 prop.
     * @param expire    分钟 必须<=3
     * @param callback
     */
    public abstract void subscribeDeviceV2(String did, int pid, List<String> entryList, int expire,
                                           PluginSubscribeCallback callback);

    /**
     * ApiLevel: 25 取消订阅(跟model无关)
     *
     * @param did
     * @param pid
     * @param entryList
     * @param callback
     */
    public abstract void unsubscribeDeviceV2(String did, int pid, List<String> entryList,
                                             String subId,
                                             PluginUnSubscribeCallback callback);

    // ApiLevel:1
    // 统计相关接口,参考miui开放平台统计，
    // 不推荐使用，无法把统计数据开放到第三方
    //
    // http://dev.xiaomi.com/doc/p%3D3995/index.html
    // ////////////
    @Deprecated
    public abstract void recordCountEvent(String category, String key);

    @Deprecated
    public abstract void recordCountEvent(String category, String key,
                                          Map<String, String> params);

    @Deprecated
    public abstract void recordCalculateEvent(String category, String key, long value);

    @Deprecated
    public abstract void recordCalculateEvent(String category, String key, long value,
                                              Map<String, String> params);

    @Deprecated
    public abstract void recordStringPropertyEvent(String category, String key,
                                                   String value);

    @Deprecated
    public abstract void recordNumericPropertyEvent(String category, String key,
                                                    long value);

    /**
     * ApiLevel:2 米家后台统计(Deprecated)
     *
     * @param appId 第三方插件开放唯一id，定义为厂商名
     * @param value 为Object 可以为int或String或JsonObject
     * @param extra 可以为null
     */
    @Deprecated
    public abstract void addRecord(String appId, String key, Object value, JSONObject extra);

    /**
     * ApiLevel: 6 米家后台统计
     *
     * @param loadedInfo 插件上下文
     * @param key
     * @param value
     * @param extra      添加打点统计新规范，必须按照下面的key value来传参数
     *                   key: PageStart	应用页面打开，自动上报
     *                   value: {
     *                   "name":"页面名称",
     *                   "starttime":"打开时间，时间戳格式"
     *                   }
     *                   key: Task	应用表现，例如等待时间、执行结果等
     *                   value:{
     *                   "name":"任务名称",
     *                   "parent":"任务所在的页面",
     *                   "duration":"任务耗时，以毫秒计，可选",
     *                   "result":"任务结果，默认0为成功，-1为失败，其它自行定义，可选",
     *                   "starttime":"操作时间，时间戳格式"
     *                   }
     *                   key: PageEnd	应用页面关闭，自动上报
     *                   value:{
     *                   "name":"页面名称",
     *                   "starttime":"关闭时间，时间戳格式"
     *                   }
     *                   key: FloatWindow	悬浮窗打开
     *                   value:{
     *                   "name":"悬浮窗名称，可使用悬浮窗标题",
     *                   "parent":"弹出框所在页面名称",
     *                   "starttime":"打开时间，时间戳格式"
     *                   }
     *                   key: RPC	手机向设备发送指令，自动上报
     *                   value:{
     *                   "name":"指令名称",
     *                   "parameter":"指令的参数列表，json格式",
     *                   "web":"使用的网络，0为局域网，1为外网",
     *                   "starttime":"操作时间，时间戳格式"
     *                   }
     *                   key: Operation	用户行为，例如点击按钮
     *                   value:{
     *                   "name":"操作名称",
     *                   "parent":"弹出框所在页面名称",
     *                   "position":"操作所在的位置，格式为x/屏幕宽度&y/屏幕高度，如0.4&0.6",
     *                   "starttime":"操作时间，时间戳格式"
     *                   }
     *                   key:WebEnd	网页关闭，自动上报
     *                   value: {
     *                   "url":"网页连接",
     *                   "starttime":"关闭时间，时间戳格式"
     *                   }
     *                   key:WebStart	网页打开，自动上报
     *                   value:{
     *                   "url":"网页连接",
     *                   "starttime":"打开时间，时间戳格式"
     *                   }
     */
    public abstract void addRecord(XmPluginPackage loadedInfo, String key, Object value,
                                   JSONObject extra);

    public static final String RecordTypeClick = "click";
    public static final String RecordTypeResult = "result";

    public void addRecordV3(XmPluginPackage loadedInfo, String type, String key, Object value,
                            JSONObject extra) {
        if (type != null) {
            type = type.toLowerCase().trim();
            if (!type.isEmpty()) {
                key = type + ":" + key;
            }
        }
        addRecord(loadedInfo, key, value, extra);
    }

    // ///////////////
    // scence

    /**
     * ApiLevel:2 获取场景
     *
     * @param model
     * @param st_id    场景模板id
     * @param did
     * @param name
     * @param callback
     */
    @Deprecated
    public void loadScene(String model, int st_id, String did, String name,
                          final Callback<JSONObject> callback) {

        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("st_id", st_id);
            dataObj.put("identify", did);
            if (name != null && !name.equals("")) {
                dataObj.put("name", name);
            }
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/list", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:2 编辑场景接口
     *
     * @param model
     * @param st_id    场景模板id
     * @param us_id    场景id
     * @param did
     * @param name
     * @param setting
     * @param authed
     * @param callback
     */
    @Deprecated
    public void editScene(String model, int st_id, int us_id, String did, String name,
                          JSONObject setting,
                          JSONArray authed, final Callback<JSONObject> callback) {
        if (us_id < 0) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id + "");
            dataObj.put("identify", did);
            dataObj.put("name", name);
            dataObj.put("st_id", st_id);
            dataObj.put("setting", setting);
            dataObj.put("authed", authed);
        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        callSmartHomeApi(model, "/scene/edit", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });
    }

    /**
     * ApiLevel:3 设置定时场景
     *
     * @param model
     * @param did
     * @param us_id
     * @param name
     * @param setting
     * @param authed
     * @param callback
     */
    @Deprecated
    public void editTimerScene(String model, String did, int us_id, String name,
                               JSONObject setting,
                               JSONArray authed, final Callback<JSONObject> callback) {
        if (us_id < 0) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id + "");
            dataObj.put("identify", did);
            dataObj.put("name", name);
            dataObj.put("st_id", 8);
            dataObj.put("setting", setting);
            dataObj.put("authed", authed);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/edit", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:68 设置定时场景
     *
     * @param model
     * @param did
     * @param us_id
     * @param name
     * @param setting
     * @param authed
     * @param callback
     */
    @Deprecated
    public void editTimerScene(String model, String did, String us_id, String name,
                               JSONObject setting,
                               JSONArray authed, final Callback<JSONObject> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id);
            dataObj.put("identify", did);
            dataObj.put("name", name);
            dataObj.put("st_id", 8);
            dataObj.put("setting", setting);
            dataObj.put("authed", authed);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/edit", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:3 加载所有定时场景
     *
     * @param model
     * @param did
     * @param name
     * @param callback
     */
    @Deprecated
    public void loadTimerScene(String model, String did, String name,
                               final Callback<JSONObject> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
            if (name != null && !name.equals("")) {
                dataObj.put("name", name);
            }
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }
        callSmartHomeApi(model, "/scene/list", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:3 获取定时场景
     *
     * @param model
     * @param did
     * @param us_id
     * @param callback
     */
    @Deprecated
    public void getTimerScene(String model, String did, int us_id,
                              final Callback<JSONObject> callback) {
        if (us_id < 0) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
            dataObj.put("us_id", us_id + "");
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }
        callSmartHomeApi(model, "/scene/get", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:68 获取定时场景
     *
     * @param model
     * @param did
     * @param us_id
     * @param callback
     */
    @Deprecated
    public void getTimerScene(String model, String did, String us_id,
                              final Callback<JSONObject> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
            dataObj.put("us_id", us_id + "");
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }
        callSmartHomeApi(model, "/scene/get", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:3 删除场景
     *
     * @param model
     * @param did
     * @param us_id
     * @param callback
     */
    @Deprecated
    public void delScene(String model, String did, int us_id,
                         final Callback<JSONObject> callback) {
        if (us_id < 0) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
            dataObj.put("us_id", us_id + "");
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/delete", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:3 设置场景
     *
     * @param model
     * @param did
     * @param name
     * @param st_id
     * @param setting
     * @param authed
     * @param callback
     */
    public void setScene(String model, String did, String name, int st_id,
                         JSONObject setting, JSONArray authed,
                         final Callback<Void> callback) {

        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
            dataObj.put("name", name);
            dataObj.put("st_id", st_id);
            dataObj.put("setting", setting);
            dataObj.put("authed", authed);
            dataObj.put("setting", setting);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/idfy_edit", dataObj, callback, null);

    }

    /**
     * ApiLevel:3 获取某个设备场景
     *
     * @param model
     * @param did
     * @param callback
     */
    public void getScene(String model, String did,
                         final Callback<JSONObject> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }
        callSmartHomeApi(model, "/scene/idfy_get", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:3 启动场景
     *
     * @param model
     * @param did
     * @param key
     * @param callback
     */
    public void startScene(String model, String did, String key,
                           final Callback<JSONObject> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
            dataObj.put("key", key);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/idfy_start", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    // ///////////////

    /**
     * ApiLevel:3 上报gps信息
     *
     * @param model
     * @param did
     * @param lng
     * @param lat
     * @param adminArea
     * @param countryCode
     * @param locality
     * @param thoroughfare
     * @param subLocality
     * @param callback
     */
    public void reportGPSInfo(String model, String did, double lng, double lat,
                              String adminArea, String countryCode, String locality,
                              String thoroughfare, String subLocality,
                              Callback<Void> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("did", did);
            dataObj.put("lng", lng);
            dataObj.put("lat", lat);
            dataObj.put("adminArea", adminArea);
            dataObj.put("countryCode", countryCode);
            dataObj.put("locality", locality);
            dataObj.put("thoroughfare", thoroughfare);
            dataObj.put("language", "zh_CN");
            dataObj.put("subLocality", subLocality);

        } catch (JSONException e) {
            callback.onFailure(-1, e.toString());
            return;
        }
        callSmartHomeApi(model, "/location/set", dataObj, callback, null);
    }

    /**
     * ApiLevel:3 获取天气
     *
     * @param model
     * @param did
     * @param callback
     */
    public void getWeatherInfo(String model, String did,
                               Callback<WeatherInfo> callback) {

        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("did", did);

        } catch (JSONException e) {
            callback.onFailure(-1, e.toString());
            return;
        }
        callSmartHomeApi(model, "/location/weather", dataObj, callback, new Parser<WeatherInfo>() {
            @Override
            public WeatherInfo parse(String data) throws JSONException {
                JSONObject response = new JSONObject(data);
                WeatherInfo result = new WeatherInfo();
                JSONObject aqiJsonObj = response.optJSONObject("aqi");
                result.aqi.city = aqiJsonObj.optString("city");
                result.aqi.city_id = aqiJsonObj.optString("city_id");
                result.aqi.pub_time = aqiJsonObj.optString("pub_time");
                result.aqi.aqi = aqiJsonObj.optString("aqi");
                result.aqi.pm25 = aqiJsonObj.optString("pm25");
                result.aqi.pm10 = aqiJsonObj.optString("pm10");
                result.aqi.so2 = aqiJsonObj.optString("so2");
                result.aqi.no2 = aqiJsonObj.optString("no2");
                result.aqi.src = aqiJsonObj.optString("src");
                result.aqi.spot = aqiJsonObj.optString("spot");

                return result;
            }
        });

    }

    /**
     * ApiLevel:65 设备方法调用，完全透明调用，需要自己设置参数
     *
     * @param sid      servicetoken 对应的 sid
     * @param callback 回调结果
     */
    public abstract void getServiceToken(String sid, Callback<JSONObject> callback);


    /**
     * ApiLevel:66
     *
     * @return 红外列表
     */
    public abstract List<DeviceStat> getIrDevList();

    /**
     * ApiLevel:2 设备方法调用，完全透明调用，需要自己设置参数
     *
     * @param did
     * @param params   参数，JsonObject字符串
     * @param callback 回调结果
     * @param parser
     */
    public abstract <T> void callMethod(String did, String params, final Callback<T> callback,
                                        final Parser<T> parser);

    /**
     * ApiLevel:2 访问路由器服务远程接口
     *
     * @param routerId 路由器id
     * @param url
     * @param method
     * @param params
     * @param callback
     * @param parser
     */
    @Deprecated
    public abstract <T> void callRouterRemoteApi(String routerId, String url, String method,
                                                 boolean fullPath, List<NameValuePair> params, final Callback<T> callback,
                                                 final Parser<T> parser);

    /**
     * ApiLevel: 13 访问路由器服务远程接口
     *
     * @param routerId
     * @param url
     * @param method
     * @param fullPath
     * @param params
     * @param callback
     * @param parser
     * @param <T>
     */
    public abstract <T> void callRouterRemoteApiV13(String routerId, String url, String method,
                                                    boolean fullPath, List<KeyValuePair> params, final Callback<T> callback,
                                                    final Parser<T> parser);

    /**
     * ApiLevel:2 获取当前登录的账号id
     *
     * @return
     */
    public abstract String getAccountId();

    /**
     * ApiLevel:2 调用本地局域网普通http请求 延时更低，超时时间为2s
     *
     * @param model    插件model
     * @param url      请求url
     * @param method   METHOD_POST或METHOD_GET
     * @param params
     * @param callback
     * @param parser
     */
    @Deprecated
    public abstract <T> void callLocalHttpApi(String model, String url, String method,
                                              List<NameValuePair> params, final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel: 13 调用本地局域网普通http请求 延时更低，超时时间为2s
     *
     * @param model
     * @param url
     * @param method
     * @param params
     * @param callback
     * @param parser
     * @param <T>
     */
    public abstract <T> void callLocalHttpApiV13(String model, String url, String method,
                                                 List<KeyValuePair> params, final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:2 修改设备名字
     *
     * @param did
     * @param newName
     * @param callback
     */
    public abstract void modDeviceName(String did, String newName,
                                       final Callback<Void> callback);

    /**
     * ApiLevel:2 解绑设备
     *
     * @param did
     * @param callback
     */
    public abstract void unBindDevice(final String did, int pid,
                                      final Callback<Void> callback);

    /**
     * ApiLevel:3 获取最新位置
     *
     * @return
     */
    public abstract Location getLastLocation();

    /**
     * ApiLevel:3 判断是否是网络定位
     *
     * @return
     */
    public abstract boolean isNetworkLocationEnabled();

    /**
     * ApiLevel:3 判断是否gps定位
     *
     * @return
     */
    public abstract boolean isGPSLocationEnable();

    /**
     * ApiLevel:3 获取位置
     *
     * @param callback
     */
    public abstract void requestLocation(Callback<Location> callback);

    /**
     * ApiLevel:3 获取设备固件升级信息
     *
     * @param callback
     */
    public void getUpdateInfo(String model, String did, int pid,
                              final Callback<DeviceUpdateInfo> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("did", did);
            dataObj.put("pid", pid);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/home/checkversion", dataObj, callback,
                new Parser<DeviceUpdateInfo>() {
                    @Override
                    public DeviceUpdateInfo parse(String result) throws JSONException {
                        DeviceUpdateInfo updateInfo = new DeviceUpdateInfo();
                        JSONObject jsonObj = new JSONObject(result);
                        if (jsonObj.optBoolean("updating")) {
                            updateInfo.mHasNewFirmware = false;
                        } else {
                            updateInfo.mHasNewFirmware = !jsonObj.optBoolean("isLatest");
                        }
                        updateInfo.mCurVersion = jsonObj.optString("curr");
                        updateInfo.mNewVersion = jsonObj.optString("latest");
                        updateInfo.mUpdateDes = jsonObj.optString("description");
                        updateInfo.mForce = jsonObj.optInt("force");
                        return updateInfo;
                    }
                });

    }

    /**
     * ApiLevel:3 加载native so
     *
     * @param libName     so库名字
     * @param classLoader 插件的classloader
     */
    @Deprecated
    public abstract void loadLibrary(String model, String libName, ClassLoader classLoader);

    /**
     * ApiLevel:6 加载native so
     *
     * 已经废弃，请使用System.loadLibrary。如果使用的话会抛出异常
     * @param loadedInfo 插件上下文
     * @param libName    so库名字
     */
    @Deprecated
    public abstract void loadLibrary(XmPluginPackage loadedInfo, String libName);

    /**
     * ApiLevel:3 获取app属性
     *
     * @param model
     * @param name  属性名
     * @return 属性值
     */
    public abstract String getProperty(String model, String name);

    /**
     * ApiLevel:3 刷新设备列表
     *
     * @param callback
     * @return
     */
    public abstract void updateDeviceList(Callback<Void> callback);

    /**
     * ApiLevel:4 插件中设备数据属性发生变化，同步数据到米家主app中，比如设备列表中显示某些属性状态
     *
     * @param did
     * @param jsonObject
     * @return
     */
    public abstract void updateDeviceProperties(String did, JSONObject jsonObject);

    /**
     * ApiLevel:4 写log文件，可以从反馈上报到统计平台
     *
     * @param tag
     * @param info
     * @return
     */
    public abstract void log(String tag, String info);

    /**
     * ApiLevel:6
     *
     * @param service
     * @param xmPluginPackage
     * @param activityClass
     */
    @Deprecated
    public abstract void startService(Intent service, XmPluginPackage xmPluginPackage,
                                      Class activityClass);

    /**
     * ApiLevel:6
     *
     * @param service
     * @param xmPluginPackage
     * @param activityClass
     * @return
     */
    @Deprecated
    public abstract boolean stopService(Intent service, XmPluginPackage xmPluginPackage,
                                        Class activityClass);

    /**
     * ApiLevel:6
     *
     * @param service
     * @param xmPluginPackage
     * @param activityClass
     * @param conn
     * @param flags
     * @return
     */
    @Deprecated
    public abstract boolean bindService(Intent service, XmPluginPackage xmPluginPackage,
                                        Class activityClass, ServiceConnection conn,
                                        int flags);

    /**
     * ApiLevel:6 把某个设备添加桌面快捷方式
     *
     * @param xmPluginPackage
     * @param did
     * @param intent
     */
    public abstract void addToLauncher(XmPluginPackage xmPluginPackage, String did, Intent intent);

    /**
     * ApiLevel:6 设置子设备是否在主设备列表显示
     *
     * @param xmPluginPackage
     * @param shownInDeviceList
     * @param did
     * @param context
     * @param callback
     */
    public abstract void setSubDeviceShownMode(XmPluginPackage xmPluginPackage,
                                               boolean shownInDeviceList,
                                               String did,
                                               Context context,
                                               final Callback<Void> callback);

    /**
     * ApiLevel:7 检测当前连接路由器是否是小米路由器
     *
     * @param routerId
     * @param callback
     */
    @Deprecated
    public abstract void checkLocalRouterInfo(String routerId,
                                              Callback<Void> callback);

    /**
     * ApiLevel:7 返回当前连接路由器是否是小米路由器
     *
     * @return
     */
    @Deprecated
    public abstract boolean isLocalMiRouter();

    /**
     * ApiLevel:7 获取路由器文件下载地址
     *
     * @param url
     * @return
     */
    public abstract String getRouterFileDownloadUrl(String url);

    /**
     * ApiLevel:8 页面跳转
     *
     * @param xmPluginPackage
     * @param uri
     */
    public abstract void gotoPage(Context context, XmPluginPackage xmPluginPackage, Uri uri,
                                  Callback<Void> callback);

    /**
     * ApiLevel:8 获取场景
     *
     * @param model
     * @param st_id    场景模板id
     * @param did
     * @param identify
     * @param callback
     */
    public void loadScene(String model, int st_id, String did, String identify, String name,
                          final Callback<JSONObject> callback) {

        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("st_id", st_id);
            dataObj.put("did", did);
            dataObj.put("identify", identify);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/list", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:8 编辑场景接口
     *
     * @param model
     * @param st_id    场景模板id
     * @param us_id    场景id
     * @param did
     * @param name
     * @param setting
     * @param authed
     * @param callback
     */
    @Deprecated
    public void editScene(String model, int st_id, int us_id, String did, String identify,
                          String name,
                          JSONObject setting,
                          JSONArray authed, final Callback<JSONObject> callback) {
        if (us_id < 0) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id + "");
            dataObj.put("identify", identify);
            if (name != null) {
                dataObj.put("name", name);
            }
            dataObj.put("st_id", st_id);
            dataObj.put("setting", setting);
            dataObj.put("authed", authed);
        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        callSmartHomeApi(model, "/scene/edit", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });
    }

    /**
     * ApiLevel:68 编辑场景接口
     *
     * @param model
     * @param st_id    场景模板id
     * @param us_id    场景id
     * @param did
     * @param name
     * @param setting
     * @param authed
     * @param callback
     */
    public void editScene(String model, int st_id, String us_id, String did, String identify,
                          String name,
                          JSONObject setting,
                          JSONArray authed, final Callback<JSONObject> callback) {
        if (TextUtils.isEmpty(us_id)) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id);
            dataObj.put("identify", identify);
            if (name != null) {
                dataObj.put("name", name);
            }
            dataObj.put("st_id", st_id);
            dataObj.put("setting", setting);
            dataObj.put("authed", authed);
        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        callSmartHomeApi(model, "/scene/edit", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });
    }

    /**
     * ApiLevel:8 删除场景接口
     */
    @Deprecated
    public void delScene(String model, int us_id,
                         final Callback<JSONObject> callback) {
        if (us_id < 0) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id + "");
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/delete", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:68 删除场景接口
     */
    public void delScene(String model, String us_id,
                         final Callback<JSONObject> callback) {
        if (TextUtils.isEmpty(us_id)) {
            if (callback != null) {
                callback.onFailure(-1, "us_id is illegal");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }

        callSmartHomeApi(model, "/scene/delete", dataObj, callback, new Parser<JSONObject>() {
            @Override
            public JSONObject parse(String result) throws JSONException {
                return new JSONObject(result);
            }
        });

    }

    /**
     * ApiLevel:8 调用智能家居后台http服务
     *
     * @param model       插件model
     * @param relativeUrl 服务接口url
     * @param params
     * @param callback
     * @param parser
     */
    public abstract <T> void callSmartHomeApi(String model, String relativeUrl, String params,
                                              final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:58 调用智能家居后台http服务
     *
     * @param model       插件model
     * @param hostPrefix  host前缀
     * @param relativeUrl 服务接口url
     * @param method      请求方法
     * @param params
     * @param callback
     * @param parser
     */
    public abstract <T> void callSmartHomeApi(String model, String hostPrefix, String relativeUrl, String method, String params,
                                              final Callback<T> callback, final Parser<T> parser);

    /**
     * ApiLevel:8 向某个设备的插件发送消息
     *
     * @param did
     * @param msgType
     * @param msgArg
     * @param deviceStat
     * @param msgCallback
     */
    public abstract void sendMessage(String did, int msgType, Intent msgArg,
                                     DeviceStat deviceStat, MessageCallback msgCallback);

    /**
     * ApiLevel:10 通知蓝牙设备已绑定
     *
     * @param mac   绑定的设备mac
     * @param token 设备token
     */
    public abstract void notifyBluetoothBinded(String mac, String token);

    /**
     * ApiLevel:10 获取小米用户信息
     *
     * @param userid 小米账号
     */
    public abstract void getUserInfo(String userid, final Callback<UserInfo> callback);

    /**
     * ApiLevel:10 开始一个下载
     *
     * @param uri     download uri
     * @param udn     can be @null
     * @param dirType the directory type to pass to {@link Context#getExternalFilesDir(String)}
     * @param subPath the path within the external directory. If subPath is a directory(ending with
     *                "/"), destination filename will be generated.
     * @return download id
     */
    @Deprecated
    public abstract long startDownload(Uri uri, String udn, String dirType, String subPath);

    /**
     * ApiLevel:10 pause download
     *
     * @param ids the IDs of the downloads
     */
    @Deprecated
    public abstract void pauseDownload(long... ids);

    /**
     * ApiLevel:10 resume download
     *
     * @param ids the IDs of the downloads
     */
    @Deprecated
    public abstract void resumeDownload(long... ids);

    /**
     * ApiLevel:10 restart download
     *
     * @param ids the IDs of the downloads
     */
    @Deprecated
    public abstract void restartDownload(long... ids);

    /**
     * ApiLevel:10 remove download
     *
     * @param ids the IDs of the downloads
     */
    @Deprecated
    public abstract void removeDownload(long... ids);

    /**
     * ApiLevel:10 query download
     *
     * @param onlyVisibleDownloads hide downloads exclude
     * @param filterIds            the IDs of the downloads
     */
    @Deprecated
    public abstract Cursor queryDownload(boolean onlyVisibleDownloads, long... filterIds);

    /**
     * ApiLevel:10 notify wifi download manager
     *
     * @param isConnected
     */
    @Deprecated

    public abstract void notifyLocalWifiConnect(boolean isConnected);

    /**
     * ApiLevel:14 更新设备的子设备
     */
    public abstract void updateSubDevice(XmPluginPackage xmPluginPackage, String[] didList,
                                         Callback<List<DeviceStat>> callback);

    /**
     * ApiLevel:15 异步调用第三方云接口 需要注意，由于后台返回的数据原因，目前回调接口可能有2种数据。
     * 目前异步接口通过push通知和超时重试2种方式获取数据。其中超时重试可能会比push获取的数据多封装一层
     * 如push获取的数据如果如下"{\"bssid\":\"d0:ee:07:23:22:90\"}"，则超时重试则可能会是{"code":0,"message":"ok","result":"{\"bssid\":\"d0:ee:07:23:22:90\"}"}
     * 有效载荷在result字段中。插件解析可能需要对数据做兼容处理
     */
    public abstract void callRemoteAsync(final String[] dids, final int appId, Object object,
                                         Callback<JSONObject> callback);

    /**
     * ApiLevel:23 异步调用第三方云接口 finalCallback: 异步请求最终结果回调，其回调表明最终结果（如果成功）或是失败 directCallback:
     * 异步请求中间回调,用于通知后台关于异步请求的配置.目前返回结果如下{"sid": 32323,"interval": 3,"max_retry":
     * 3}，意思是最多重试三次，每次请求间隔３秒 如果请求提交失败，则直接调用finalCallback的回调，表明callRemoteAsync失败
     */
    public abstract void callRemoteAsync(final String[] dids, final int appId, Object object,
                                         Callback<JSONObject> finalCallback, Callback<JSONObject> directCallback);

    /**
     * ApiLevel:16 获取蓝牙设备固件升级信息
     */
    public abstract void getBluetoothFirmwareUpdateInfo(String model,
                                                        final Callback<BtFirmwareUpdateInfo> callback);

    /**
     * ApiLevel:17 同步设备gps信息
     */
    public void reportGPSInfo(final String did, final Callback<Void> callback) {
        if (TextUtils.isEmpty(did)) {
            if (callback != null) {
                callback.onFailure(-1, "");
            }
            return;
        }
        final DeviceStat deviceStat = getDeviceByDid(did);
        if (deviceStat == null) {
            if (callback != null) {
                callback.onFailure(-1, "");
            }
            return;
        }
        requestLocation(new Callback<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    if (callback != null) {
                        callback.onFailure(-1, "");
                    }
                    return;
                }
                Address lastAddress = null;
                Geocoder geoCoder = new Geocoder(context());
                try {
                    List<Address> addressList = geoCoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(), 1);
                    if (addressList != null && addressList.size() > 0) {
                        lastAddress = addressList.get(0);
                    }
                } catch (IOException e) {
                }
                if (lastAddress == null) {
                    if (callback != null) {
                        callback.onFailure(-1, "");
                    }
                    return;
                }

                String adminArea = "";
                String countryCode = "";
                String locality = "";
                String thoroughfare = "";
                String subLocality = "";

                adminArea = lastAddress.getAdminArea();
                countryCode = lastAddress.getCountryCode();
                locality = lastAddress.getLocality();
                thoroughfare = lastAddress.getThoroughfare();
                subLocality = lastAddress.getSubLocality();

                reportGPSInfo(deviceStat.model, did,
                        location.getLongitude(), location.getLatitude(),
                        adminArea, countryCode, locality, thoroughfare,
                        subLocality, callback);
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                if (callback != null) {
                    callback.onFailure(error, errorInfo);
                }
            }
        });
    }

    /**
     * 在ApiLevel:25后升级了微信sdk，有用到这个接口的必须更新插件sdk适配，发布新版插件并且修改minPluginSdkApiVersion为25 ApiLevel:20
     * 创建米家app注册的微信接口
     */
    public abstract IWXAPI createWXAPI(Context context, boolean bl);

    /**
     * ApiLevel: 20 蓝牙数据上报
     */
    public abstract void reportBluetoothRecords(String did, String model,
                                                List<XmBluetoothRecord> records, final Callback<List<Boolean>> callback);

    /**
     * ApiLevel: 20 下载蓝牙固件
     */
    public abstract void downloadBleFirmware(String url, Response.BleUpgradeResponse response);

    /**
     * ApiLevel: 28 取消下载蓝牙固件
     *
     * @param url
     */
    public abstract void cancelDownloadBleFirmware(String url);

    /**
     * ApiLevel: 20 设置蓝牙设备副标题
     */
    public abstract void setBleDeviceSubtitle(String mac, String subtitle);

    /**
     * ApiLevel: 21 设置蓝牙设备名称
     */
    public abstract void deviceRename(String mac, final String name);

    /**
     * ApiLevel: 22 获取当前服务器
     *
     * @return "cn":中国大陆 "tw":台湾 "sg":新加坡 "in":印度
     */
    public abstract String getGlobalSettingServer();

    /**
     * ApiLevel: 60 获取当前服务器
     *
     * @param changeServer true: 获取到的不一定是用户当前选择的服务器（比如香港共用新加坡服务器），false：获取到的是用户当前选择的服务器
     * @return "cn":中国大陆 "tw":台湾 "sg":新加坡 "in":印度
     */
    public abstract String getGlobalSettingServer(boolean changeServer);

    /**
     * ApiLevel: 22 给设备发送broadcast，会发送给IXmPluginMessageReceiver.handleMessage
     */
    public Intent getBroadCastIntent(DeviceStat deviceStat) {
        Intent intent = new Intent("com.xiaomi.smarthome.RECEIVE_MESSAGE");
        if (deviceStat != null) {
            intent.putExtra("device_id", deviceStat.did);
            intent.putExtra("device_mac", deviceStat.mac);
            intent.putExtra("user_model", deviceStat.model);
        }
        return intent;
    }

    /**
     * ApiLevel: 22 获取设备属性和事件历史记录
     *
     * @param model
     * @param did       设备did
     * @param type      属性为prop,事件为event
     * @param key       属性名，不需要prop或者event前缀
     * @param timeStart 起始时间单位为秒
     * @param timeEnd   结束事件，单位为秒
     * @param callback  回调
     */
    public void getUserDeviceData(String model, String did, String type, String key, long timeStart,
                                  long timeEnd, Callback<JSONArray> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("did", did);
            dataObj.put("type", type);
            dataObj.put("key", key);
            dataObj.put("time_start", timeStart);
            dataObj.put("time_end", timeEnd);
//            dataObj.put("limit",500);
        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        callSmartHomeApi(model, "/user/get_user_device_data", dataObj, callback,
                new Parser<JSONArray>() {
                    @Override
                    public JSONArray parse(String result) throws JSONException {
                        JSONObject response = new JSONObject(result);
                        return response.getJSONArray("result");
                    }
                });
    }

    /**
     * ApiLevel: 22 创建或修改设置app/插件自由存储空间,最大4k
     *
     * @param app_id 厂商APP_ID，需要向小米申请
     * @param key    索引，从0开始
     * @param data   key，value结构数据
     */

    @Deprecated
    public void setUserConfig(String model, String app_id, int key, Map<String, Object> data,
                              Callback<Boolean> callback) {
        if (callback != null) {
            callback.onFailure(-1, "This API is forbidden, please use setUserConfigV2 instead");
        }
    }

    /**
     * ApiLevel: 22 拉取设置app/插件自由存储空间
     *
     * @param model
     * @param app_id   厂商APP_ID，需要向小米申请
     * @param keys     索引，从0开始
     * @param callback key，value结构数据
     */
    @Deprecated
    public void getUserConfig(String model, String app_id, int[] keys,
                              Callback<Map<String, Object>> callback) {
        if (callback != null) {
            callback.onFailure(-1, "API forbidden, please use getUserConfigV2 instead!");
        }
    }

    /**
     * ApiLevel: 24 查询水电燃气余额
     *
     * @param type      1:水 2:电 3:燃气
     * @param latitude  纬度
     * @param longitude 经度
     * @param callback  返回查询余额Json
     *                  //{"balance":300,"updateTime":1465781516,"rechargeItemName":"郑州市燃气费"}
     *                  返回null时表示没有绑定机表号
     */
    public abstract void getRechargeBalances(int type, double latitude, double longitude,
                                             Callback<JSONObject> callback);

    /**
     * ApiLevel: 26 编码生成二维码图片
     *
     * @param barcode 二维码信息
     * @param width   图片宽度
     * @param height  图片高度
     * @return 返回二维码图片, 为ARGB_8888格式
     */
    public abstract Bitmap encodeBarcode(String barcode, int width, int height);

    /**
     * ApiLevel: 26 解码二维码图片
     *
     * @param bitmap 二维码图片,必须为ARGB_8888格式
     * @return 返回二维码信息
     */
    public abstract String decodeBarcode(Bitmap bitmap);

    /**
     * ApiLevel: 27 从服务器更新设备信息
     *
     * @param didList
     * @param callback
     */
    public abstract void updateDevice(List<String> didList, Callback<List<DeviceStat>> callback);

    /**
     * ApiLevel: 27
     *
     * @param context
     * @param loadedInfo
     * @param hostService
     * @param startIntent
     * @param serviceClass
     * @param callback
     */
    public abstract void startService(Context context, XmPluginPackage loadedInfo,
                                      HostService hostService, Intent startIntent, Class serviceClass,
                                      Callback<Bundle> callback);

    /**
     * ApiLevel: 27
     *
     * @param context
     * @param loadedInfo
     * @param hostService
     * @param startIntent
     * @param serviceClass
     * @param callback
     */
    public abstract void stopService(Context context, XmPluginPackage loadedInfo,
                                     HostService hostService, Intent startIntent, Class serviceClass,
                                     Callback<Bundle> callback);

    public abstract int getDrawableResIdByName(XmPluginPackage loadedInfo, String resName);

    /**
     * ApiLevel: 30 获取组设备中的设备信息
     *
     * @param did 组设备id
     */
    public void getVirtualDevicesByDid(String model, String did,
                                       final Callback<List<DeviceStat>> callback) {

        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("type", "get");
            dataObj.put("masterDid", did);
        } catch (JSONException e) {
            if (callback != null)
                callback.onFailure(-1, e.toString());
            return;
        }
        callSmartHomeApi(model, "/home/virtualdevicectr", dataObj, callback,
                new Parser<List<DeviceStat>>() {
                    @Override
                    public List<DeviceStat> parse(String result) throws JSONException {
                        JSONObject jsonObject = new JSONObject(result);
                        ArrayList<DeviceStat> deviceStatArrayList = new ArrayList<DeviceStat>();
                        JSONArray membersJson = jsonObject.optJSONArray("members");
                        if (membersJson != null && membersJson.length() > 0) {
                            for (int i = 0; i < membersJson.length(); i++) {
                                deviceStatArrayList.add(getDeviceByDid(membersJson.optString(i)));
                            }
                        }
                        return deviceStatArrayList;
                    }
                });
    }


    /**
     * ApiLevel: 30 创建或修改设置app/插件自由存储空间,最大4k
     *
     * @param xmPluginPackage 插件上下文
     * @param model           设备Model
     * @param app_id          厂商APP_ID，需要向小米申请，0和1预留
     * @param key             索引，从0开始
     * @param data            key，value结构数据
     * @param callback
     */
    public void setUserConfigV2(XmPluginPackage xmPluginPackage, String model, int app_id, int key,
                                Map<String, Object> data, Callback<Boolean> callback) {
        if (app_id == 0 || app_id == 1) {
            if (callback != null) {
                callback.onFailure(-1, "App id invalid, value 0 and 1 are reserved.");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("component_id", app_id);
            dataObj.put("key", key);
            JSONObject attris = new JSONObject();
            Set<Map.Entry<String, Object>> entrys = data.entrySet();
            for (Map.Entry<String, Object> entry : entrys) {
                attris.put(entry.getKey(), entry.getValue());
            }
            dataObj.put("data", attris);

        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
            }
            return;
        }
        callSmartHomeApi(model, "/user/set_user_config", dataObj, callback, new Parser<Boolean>() {
            @Override
            public Boolean parse(String result) throws JSONException {
                JSONObject response = new JSONObject(result);
                int res = response.optInt("result");
                return res != 0;
            }
        });
    }

    /**
     * ApiLevel: 41 创建或修改设置app/插件自由存储空间,最大4k
     *
     * @param xmPluginPackage 插件上下文
     * @param model           设备Model
     * @param data            索引，从0开始
     * @param callback
     */
    public void setUserConfigV3(XmPluginPackage xmPluginPackage, String model, int key,
                                Map<String, Object> data, Callback<Boolean> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("key", key);
            dataObj.put("model", model);
            JSONObject attris = new JSONObject();
            Set<Map.Entry<String, Object>> entrys = data.entrySet();
            for (Map.Entry<String, Object> entry : entrys) {
                attris.put(entry.getKey(), entry.getValue());
            }
            dataObj.put("data", attris);

        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
            }
            return;
        }
        callSmartHomeApi(model, "/user/set_third_user_config", dataObj, callback, new Parser<Boolean>() {
            @Override
            public Boolean parse(String result) throws JSONException {
                JSONObject response = new JSONObject(result);
                int res = response.optInt("result");
                return res != 0;
            }
        });
    }

    /**
     * ApiLevel: 42 创建或修改设置app/插件自由存储空间。如果数据超过服务器设置的阈值，自动分段存储到云端。
     * 但是分段存储会占用额外的key，比如key=100时，分出的新段会存储在101,102,103...等后续相邻的key上，
     * 所以：若调用者打算用到多个key,每2个key之间需要留出足够的间隔以供分段用，推荐>100，比如100,200,300...
     *
     * @param xmPluginPackage
     * @param model
     * @param key
     * @param data
     * @param callback        返回存储这条数据占用的key
     */
    public abstract void setUserConfigV5(XmPluginPackage xmPluginPackage, String model, int key,
                                         Map<String, Object> data, Callback<int[]> callback);

    /**
     * ApiLevel: 30 拉取设置app/插件自由存储空间
     *
     * @param xmPluginPackage 插件上下文
     * @param model           设备Model
     * @param app_id          厂商APP_ID，需要向小米申请, 0 和 1 预留
     * @param keys            索引，从0开始
     * @param callback        key，value结构数据
     */
    public void getUserConfigV2(XmPluginPackage xmPluginPackage, String model, int app_id,
                                int[] keys, Callback<Map<String, Object>> callback) {
        if (app_id == 0 || app_id == 1) {
            if (callback != null) {
                callback.onFailure(-1, "App id invalid, 0 and 1 are reserved.");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("component_id", app_id);
            JSONArray keysArray = new JSONArray();
            for (int i = 0; i < keys.length; i++) {
                keysArray.put(keys[i]);
            }
            dataObj.put("keys", keysArray);

        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        callSmartHomeApi(model, "/user/get_user_config", dataObj, callback,
                new Parser<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> parse(String result) throws JSONException {
                        JSONObject response = new JSONObject(result);
                        Map<String, Object> map = new HashMap<String, Object>();
                        JSONObject resultObj = response.optJSONObject("result");
                        if (resultObj != null) {
                            Iterator<String> iterator = resultObj.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                map.put(key, resultObj.get(key));
                            }
                        }
                        return map;
                    }
                });
    }

    /**
     * ApiLevel: 40 拉取设置app/插件自由存储空间
     *
     * @param xmPluginPackage 插件上下文
     * @param model           设备Model
     * @param app_id          厂商APP_ID，需要向小米申请, 0 和 1 预留
     * @param keys            索引，从0开始
     * @param callback        key，value结构数据，key为传入的int[]的各个元素，value为对应的JSONObject，例如"3" -> "{"uid":"923000000","data":{"data":"i am test data"},"component_id":"10000","update_time":"1492657366","key":"3"}"
     */
    public void getUserConfigV3(XmPluginPackage xmPluginPackage, String model, int app_id,
                                int[] keys, Callback<Map<String, Object>> callback) {
        if (app_id == 0 || app_id == 1) {
            if (callback != null) {
                callback.onFailure(-1, "App id invalid, 0 and 1 are reserved.");
            }
            return;
        }
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("component_id", app_id);
            JSONArray keysArray = new JSONArray();
            for (int i = 0; i < keys.length; i++) {
                keysArray.put(keys[i]);
            }
            dataObj.put("keys", keysArray);

        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        callSmartHomeApi(model, "/user/get_user_config", dataObj, callback,
                new Parser<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> parse(String result) throws JSONException {
                        JSONObject response = new JSONObject(result);
                        Map<String, Object> map = new HashMap<String, Object>();
                        JSONArray resultObj = response.optJSONArray("result");
                        for (int i = 0; i < resultObj.length(); i++) {
                            JSONObject jsonObject = resultObj.getJSONObject(i);
                            String key = (String) jsonObject.get("key");
                            map.put(key, jsonObject);
                        }
                        return map;
                    }
                });
    }

    /**
     * ApiLevel: 41 拉取设置app/插件自由存储空间
     *
     * @param xmPluginPackage 插件上下文
     * @param model           设备Model
     * @param keys            索引，从0开始
     * @param callback        key，value结构数据，key为传入的int[]的各个元素，value为对应的JSONObject，例如"3" -> "{"uid":"923000000","data":{"data":"i am test data"},"component_id":"10000","update_time":"1492657366","key":"3"}"
     */
    public void getUserConfigV4(XmPluginPackage xmPluginPackage, String model,
                                int[] keys, Callback<Map<String, Object>> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            JSONArray keysArray = new JSONArray();
            for (int i = 0; i < keys.length; i++) {
                keysArray.put(keys[i]);
            }
            dataObj.put("keys", keysArray);
            dataObj.put("model", model);

        } catch (JSONException e) {
            if (callback != null) {
                callback.onFailure(-1, e.toString());
                return;
            }
        }
        callSmartHomeApi(model, "/user/get_third_user_config", dataObj, callback,
                new Parser<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> parse(String result) throws JSONException {
                        JSONObject response = new JSONObject(result);
                        Map<String, Object> map = new HashMap<>();
                        JSONArray resultObj = response.optJSONArray("result");
                        for (int i = 0; i < resultObj.length(); i++) {
                            JSONObject jsonObject = resultObj.getJSONObject(i);
                            Object key = jsonObject.get("key");
                            if (key.equals(JSONObject.NULL)) continue;
                            map.put((String) key, jsonObject);
                        }
                        return map;
                    }
                });
    }

    /**
     * ApiLevel: 42
     * 与setUserConfigV5配套使用，会把分段的数据自动合并后返回，使得分段行为对调用者透明
     *
     * @param xmPluginPackage
     * @param model
     * @param keys
     * @param callback
     */
    public abstract void getUserConfigV5(XmPluginPackage xmPluginPackage, String model,
                                         int[] keys, Callback<Map<String, Object>> callback);

    /**
     * ApiLevel: 32 打开插件安全验证通过后，可以获取设备pincode
     *
     * @param did
     * @return
     */
    public abstract String getDevicePincode(String did);

    /**
     * ApiLevel: 32,本地ping设备，查看设备是否是本地设备
     *
     * @param did
     * @param callback
     */
    public abstract void localPing(String did, Callback<Void> callback);

    /**
     * ApiLevel: 32,从服务器批量获取设备属性
     *
     * @param jsonArray [{"did":"aaa", "props":["prop.aaa","prop.bbb"]},{"did":"123", "props":["prop.jjjj","prop.777"]}]
     * @param callback
     */
    public void batchGetDeviceProps(String model, JSONArray jsonArray, Callback<String> callback) {
        callSmartHomeApi(model, "/device/batchdevicedatas", jsonArray.toString(), callback, new Parser<String>() {
            @Override
            public String parse(String result) throws JSONException {
                return result;
            }
        });
    }

    /**
     * ApiLevel: 32
     * 打开语音授权页面。
     * 使用startActivityForResult方法，回调根据resultCode是RESULT_CANCELED或者RESULT_OK判定操作是否成功
     * 返回true,说明打开了授权页面,否则不需要打开授权页面
     *
     * @param did
     * @param activity:
     */
    public abstract boolean checkAndShowVoiceCtrlAuthorizePageIfNeed(Activity activity, String did, int requestCode);

    /**
     * ApiLevel: 31
     *
     * @param did
     */
    public abstract void visualSecureBind(String did);

    /**
     * ApiLevel: 31
     *
     * @param model 设备model
     */
    public abstract void getFirmwareUpdateInfoCommon(String model, final Callback<FirmwareUpdateInfo> callback);

    /**
     * ApiLevel: 32 下载固件
     */
    public abstract void downloadFirmware(String url, Response.FirmwareUpgradeResponse response);

    /**
     * ApiLevel: 33
     * 获取网络链接对应的图片资源
     */
    public abstract void loadBitmap(String imageUrl, Callback<Bitmap> callback);

    /**
     * ApiLevel:34 获取设备标签
     *
     * @param did
     * @return
     */
    public abstract DeviceTag getDeviceTagByDid(String did);

    /**
     * ApiLevel:34 添加标签,若did不为空，则同时为此设备设置该标签
     *
     * @param tag
     * @param did
     */
    public abstract void addTag(String tag, String did);

    /**
     * ApiLevel:34 删除标签
     *
     * @param tag
     */
    public abstract void removeTag(String tag);

    /**
     * ApiLevel: 34
     * 根据设备的model获取设备实物图
     */
    public abstract void getDeviceRealIconByModel(String model, Callback<Bitmap> callback);

    /**
     * ApiLevel:34 获取建议标签
     *
     * @param did
     * @return
     */
    public abstract List<String> getRecommendTags(String did);

    /**
     * ApiLevel: 36
     * 初始化相机发送通道
     */
    public abstract void initCameraFrameSender(String did);

    /**
     * ApiLevel: 36
     * 摄像机设备发送video接口
     */
    public abstract void sendCameraFrame(String did, byte[] data, long seq, int frameSize, long timestamp, boolean isIFrame, int width, int height);


    /**
     * ApiLevel: 57
     * 摄像机设备发送video接口
     * videoType 发送的video格式
     * 1 - H264
     * 2 - H265
     */
    public abstract void sendCameraFrame(String did, byte[] data, long seq, int frameSize, long timestamp, int videoType, boolean isIFrame, int width, int height);

    /**
     * ApiLevel: 36
     * 关闭发送通道
     */
    public abstract void closeCameraFrameSender(String did);

    /**
     * ApiLevel: 36
     * 开启摄像头悬浮窗（须支持发送摄像头视频数据）
     */
    public abstract void openCameraFloatingWindow(String did);

    /**
     * ApiLevel: 36
     * 关闭摄像头悬浮窗（须支持发送摄像头视频数据）
     */
    public abstract void closeCameraFloatingWindow(String did);

    /**
     * ApiLevel: 35
     */
    public abstract Typeface getFont(String name);

    /**
     * ApiLevel: 37
     *
     * @param context
     * @param loadedInfo
     * @param hostService
     * @param serviceClass
     * @param connection
     * @param flags
     * @param callback
     */
    public abstract void bindService(Context context, XmPluginPackage loadedInfo,
                                     HostService hostService, Class serviceClass, ServiceConnection connection, int flags,
                                     Callback<Bundle> callback);

    /**
     * ApiLevel: 37
     *
     * @param context
     * @param loadedInfo
     * @param hostService
     * @param serviceClass
     * @param connection
     * @param callback
     */
    public abstract void unbindService(Context context, XmPluginPackage loadedInfo,
                                       HostService hostService, Class serviceClass, ServiceConnection connection,
                                       Callback<Bundle> callback);

    /**
     * ApiLevel: 38
     * 根据did获取设备的订阅属性
     */
    public abstract JSONArray getDeviceProp(String did);

    /**
     * ApiLevel:39 米家后台统计.该打点同时也更新到小米开放平台上.
     *
     * @param loadedInfo 插件上下文
     * @param model      当前设备model
     * @param value      为Object 可以为int或String或JsonObject
     * @param extra      可以为null
     */
    @Deprecated
    public abstract void addRecordV2(XmPluginPackage loadedInfo, String model, String key, Object value, JSONObject extra);

    /**
     * ApiLevel: 41
     * 设备列表过滤能控制的设备
     */
    public abstract void getControllableDevices(String model, Callback<JSONObject> callback);

    /**
     * ApiLevel:43
     * 跳转到意见反馈页面
     *
     * @param activity
     * @param model
     * @param did
     * @param pluginPackage
     */
    public abstract void gotoFeedback(Activity activity, String model, String did, XmPluginPackage pluginPackage);

    /**
     * ApiLevel:44
     * 跳转到授权管理页
     *
     * @param activity
     * @param did
     */
    public abstract void gotoAuthManagerPage(Activity activity, String did);

    /**
     * ApiLevel:45
     * 获取插件notification的icon
     */
    public abstract int getMiHomeNotificationIcon();

    /**
     * ApiLevel:51
     * 分享电子钥匙
     *
     * @param model      设备model
     * @param did        分享者的did
     * @param shareUid   分享目标的uid
     * @param status     分享类别，1：暂时，2：周期，3：永久
     * @param activeTime 生效时间 UTC时间戳，单位为s
     * @param expireTime 过期时间 UTC时间戳，单位为s
     * @param weekdays   生效日期（星期几，例如周一和周三对应1和3，[1, 3]，星期天对应0），仅在status=2时不可为空
     * @param readonly   true：被分享人不可接收锁push，false：被分享人可接收锁push，（family关系用户不受这个字段影响）
     * @param callback
     */
    public void shareSecurityKey(final String model, final String did, String shareUid, final int status, final long activeTime, final long expireTime,
                                 final List<Integer> weekdays, final boolean readonly, final Callback<Void> callback) {
        Callback<String> userInfoCallback = new Callback<String>() {
            @Override
            public void onSuccess(String userId) {
                if (TextUtils.isEmpty(userId)
                        || userId.equalsIgnoreCase("-1")
                        || userId.equalsIgnoreCase("0")) {
                    if (callback != null) {
                        callback.onFailure(INVALID, "shareUid is invalid");
                    }
                } else {
                    JSONObject dataObj = new JSONObject();
                    try {
                        dataObj.put("type", "bleshare");
                        dataObj.put("did", did);
                        dataObj.put("userid", userId);
                        dataObj.put("status", status);
                        dataObj.put("active_time", activeTime);
                        dataObj.put("expire_time", expireTime);
                        if (weekdays != null && weekdays.size() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < weekdays.size(); i++) {
                                if (i == 0) {
                                    sb.append(weekdays.get(i));
                                } else {
                                    sb.append(",");
                                    sb.append(weekdays.get(i));
                                }
                            }
                            dataObj.put("weekdays", sb.toString());
                        }
                        dataObj.put("readonly", readonly);
                    } catch (JSONException e) {
                        if (callback != null)
                            callback.onFailure(-1, e.toString());
                        return;
                    }

                    callSmartHomeApi(model, "/share/bluetoothkeyshare", dataObj, callback, null);
                }
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                if (callback != null) {
                    callback.onFailure(error, errorInfo);
                }
            }
        };

        JSONObject userInfoObj = new JSONObject();
        try {
            userInfoObj.put("id", shareUid);
        } catch (JSONException e) {
        }

        Parser<String> parser = new Parser<String>() {
            @Override
            public String parse(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.optString("userid");
            }
        };

        callSmartHomeApi(model, "/home/profile", userInfoObj, userInfoCallback, parser);
    }

    /**
     * ApiLevel:51
     * 更新分享的电子钥匙信息
     *
     * @param model      设备的model
     * @param did        分享者的did
     * @param keyId      电子钥匙的keyId
     * @param status     分享类别，1：暂时，2：周期，3：永久
     * @param activeTime 生效时间 UTC时间戳，单位为s
     * @param expireTime 过期时间 UTC时间戳，单位为s
     * @param weekdays   生效日期（星期几，例如周一和周三对应1和3，[1, 3]），仅在status=2时不可为空
     * @param callback
     */
    public void updateSecurityKey(String model, String did, String keyId, int status, long activeTime, long expireTime, List<Integer> weekdays, Callback<Void> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("type", "update");
            dataObj.put("did", did);
            dataObj.put("keyid", keyId);
            dataObj.put("status", status);
            dataObj.put("active_time", activeTime);
            dataObj.put("expire_time", expireTime);
            if (weekdays != null && weekdays.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < weekdays.size(); i++) {
                    if (i == 0) {
                        sb.append(weekdays.get(i));
                    } else {
                        sb.append(",");
                        sb.append(weekdays.get(i));
                    }
                }
                dataObj.put("weekdays", sb.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callSmartHomeApi(model, "/share/bluetoothkeyshare", dataObj, callback, null);
    }

    /**
     * ApiLevel:51
     * 删除共享的电子钥匙
     *
     * @param model    设备的model
     * @param did      分享者的did
     * @param keyId    分享电子钥匙的KeyId
     * @param callback
     */
    public void deleteSecurityKey(String model, String did, String keyId, final Callback<Void> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("type", "bledelete");
            dataObj.put("did", did);
            dataObj.put("keyid", keyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callSmartHomeApi(model, "/share/bluetoothkeyshare", dataObj, callback, null);
    }

    /**
     * ApiLevel:51
     * 获取所有分享的电子钥匙信息
     *
     * @param model    设备model
     * @param did      分享者的did
     * @param callback
     */
    public void getSecurityKey(String model, String did, final Callback<List<SecurityKeyInfo>> callback) {
        final JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("type", "get");
            dataObj.put("did", did);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callSmartHomeApi(model, "/share/bluetoothkeyshare", dataObj, callback, new Parser<List<SecurityKeyInfo>>() {
            @Override
            public List<SecurityKeyInfo> parse(String result) throws JSONException {
                /**
                 * result：格式{"bleshare":[{"keyid":183038048,"shareuid":"23912868","status":"3","active_time":"1499997061","expire_time":"1531446661","isoutofdate":0}]}
                 */
                List<SecurityKeyInfo> infos = new ArrayList<SecurityKeyInfo>();
                try {
                    JSONObject resultObject = new JSONObject(result);
                    JSONArray jsonArray = resultObject.optJSONArray("bleshare");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            if (jsonObject != null) {
                                SecurityKeyInfo info = new SecurityKeyInfo();
                                info.keyId = jsonObject.optString("keyid");
                                info.shareUid = jsonObject.optString("shareuid");
                                info.status = jsonObject.optInt("status");
                                info.activeTime = jsonObject.optLong("active_time");
                                info.expireTime = jsonObject.optLong("expire_time");
                                info.isoutofdate = jsonObject.optInt("isoutofdate");
                                String weekdays = jsonObject.optString("weekdays");
                                if (!TextUtils.isEmpty(weekdays)) {
                                    String[] days = weekdays.split(",");
                                    if (days != null && days.length > 0) {
                                        info.weekdays = new ArrayList<Integer>();
                                        for (int tmp = 0; tmp < days.length; tmp++) {
                                            info.weekdays.add(Integer.valueOf(days[tmp]));
                                        }
                                    }
                                }

                                infos.add(info);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return infos;
            }
        });
    }

    /**
     * ApiLevel:51
     * 获取UTC时间，单位为ms
     * 被废弃了，使用getUTCFromServer接口
     */
    @Deprecated
    public abstract long getUTCTimeInMillis();

    /**
     * ApiLevel:51
     * 从服务器获取UTC时间，单位为秒（返回-1，说明解析出现异常，当做错误处理）
     *
     * @param callback
     */
    public void getUTCFromServer(String model, Callback<Long> callback) {
        JSONObject dataObj = new JSONObject();
        Parser<Long> parser = new Parser<Long>() {
            @Override
            public Long parse(String response) throws JSONException {
                /**
                 * {"code":0,"message":"ok","result":1502874040}
                 */
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.optLong("result", -1);
            }
        };

        callSmartHomeApi(model, "/device/get_utc_time", dataObj, callback, parser);
    }

    /**
     * ApiLevel: 51
     * 获取蓝牙锁绑定的时间
     *
     * @param model
     * @param did
     * @param callback
     */
    public void getBleLockBindInfo(String model, String did, Callback<String> callback) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("did", did);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Parser<String> parser = new Parser<String>() {
            @Override
            public String parse(String response) throws JSONException {
                /**
                 * {"code":0,"message":"ok","result":{"bindtime":1505180216}}
                 * 返回数据格式：{"bindtime":1505180216}
                 */
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.optString("bindtime");
            }
        };

        callSmartHomeApi(model, "/device/blelockbindinfo", dataObj, callback, parser);
    }

    /**
     * ApiLevel: 51
     * 请求app config
     *
     * @param name
     * @param lang
     * @param version
     */
    public abstract void getAppConfig(String name, String lang, String version, Callback<String> callback);


    /**
     * ApiLevel: 51
     * 获取app语言信息
     */
    public abstract Locale getSettingLocale();

    /**
     * ApiLevel: 53
     * 判断当前app是否支持当前model
     */
    public abstract boolean isModelSupport(String model);

    /**
     * ApiLevel: 54
     * 提供给插件记录日志保存在本地，用户反馈时可以提交到服务器，
     *
     * @param model
     * @param logMessage
     */
    public abstract void logByModel(String model, String logMessage);

    /**
     * ApiLevel: 55
     * 反地理解析获取经纬度信息获取位置信息
     * <p>
     * 注意事项：
     * 1. 插件要缓存相应的结果，避免频繁调用该接口，每天配额是有限的，超过后当天该接口将无法工作
     * 2. 目前使用Android系统和高德进行反地理解析，而高德并不支持海外反地理解析
     * 3. 反地理解析结果语言一般是米家设置的语言，如果米家跟随系统，则是手机系统的语言，但是不能保证一定会有相应的语言版本
     */
    public abstract void reverseGeo(double latitude, double longitude, Callback<Address> callback);

    /**
     * ApiLevel: 58
     * 提供给插件记录日志保存在本地，用户反馈时可以提交到服务器
     *
     * @param model
     * @param logMessage
     */
    public abstract void logForModel(String model, String logMessage);

    /**
     * ApiLevel: 64
     * 创建一个播放视频流的播放视图
     *
     * @param context
     * @param original 父容器
     * @param useHard  是否优先使用硬解码
     * @param type     视频流编码类型 1==h264 2==h265
     * @return
     * @see com.xiaomi.smarthome.camera.VideoFrame
     */
    public abstract XmVideoViewGl createVideoView(Context context, FrameLayout original, boolean useHard, int type);

    /**
     * ApiLevel: 64
     * 创建一个用来播放本地Mp4的视图
     *
     * @param context
     * @param original 父容器
     * @param useHard  true MediaPlayer播放mp4 // false 使用ffmpeg 播放mp4
     * @return
     */
    public abstract XmVideoViewGl createMp4View(Context context, FrameLayout original, boolean useHard);

    /**
     * ApiLevel: 64
     * 创建一个可以用来合成Mp4的接口
     *
     * @return Mp4音视频流合成器的接口
     */
    public abstract XmMp4Record createMp4Record();

    /**
     * ApiLevel: 64
     * 插件获取bindkey后传给设备，然后设备再传给MIOT后台，完成设备与MIOT的绑定
     */
    public abstract void getBindKey(String model, Callback<String> callback);

    /**
     * ApiLevel: 75
     * 检查/请求权限
     * 米家将targetSdkVersion升级到>=23之后,需要适配全新的权限机制。
     * ps：6.0之前的安卓版本上，申请的权限在app安装时就被授予。
     * 6.0之后为了更好的保护用户隐私，部分涉及隐私的权限被定义为危险权限（CAMERA、麦克风等），需要用户主动授权后才能使用。
     * 针对这种情况，凡涉及到危险权限的功能，都应该在使用前动态的检查是否已被用户授权。
     * 否则可能会出现java.lang.SecurityException: Permission Denial从而导致应用崩溃。
     * 危险权限列表可以参考Android开发文档https://developer.android.com/guide/topics/permissions/overview#perm-groups
     *
     * @param activity
     * @param requestPermission 是否发起请求权限流程
     * @param callback 授权结果回调
     * @param permissions 申请的权限或权限组(常见危险权限已在Permission这个类中定义，可直接作为参数传入该方法使用。)
     * @return 权限都已经授予/功能正常，返回true，否则，返回false
     */
    public abstract boolean checkAndRequestPermisson(Activity activity, boolean requestPermission, Callback<List<String>> callback, String... permissions);

    /*
     * ApiLevel: 65
     * 插件获取云存储报警视频, mp4格式
     * @param context 不能为null
     * @param params 包括did，fileId，stoId的json格式string
     * @param callback 回调函数，获取成功或者失败的结果
     */
    public abstract void getCloudVideoFile(Context context, String params, ICloudDataCallback callback);

    /*
     * ApiLevel: 65
     * 插件获取图片url的api，只是获取url，不真正下载图片
     * @param did 设备id
     * @param fileId 文件id
     * @param stoId 存储id
     * @return 图片url
     */
    public abstract String getCloudImageUrl(String did, String fileId, String stoId);

    /*
     * ApiLevel: 65
     * 插件获取加密的图片
     * @param context 不能为null
     * @param imageUri 图片的uri
     * @return 图片数据
     */
    public abstract byte[] sendImageDownloadRequest(Context context, String imageUri);

    /**
     * ApiLevel: 66
     * 打开创建组设备界面
     *
     * @param groupModel
     */
    public abstract void createDeviceGroup(Context context, String groupModel);

    /**
     * ApiLevel: 68
     * 查询did对应的设备是否开启的用户体验计划
     *
     * @param did
     */
    public abstract boolean isUsrExpPlanEnabled(String did);

    /**
     * ApiLevel: 68
     * 设置did对应的设备是否开启的用户体验计划
     * 只保存本地，目前清除数据后恢复默认值
     *
     * @param did
     */
    public abstract void setUsrExpPlanEnabled(String did, boolean enabled);

    /**
     * ApiLevel: 69
     * 获取特定Model蓝牙设备列表信息(在后台配置具体哪个model可以获取哪些设备列表)
     *
     * @param requestModel 要获取设备列表的model
     * @return 返回的设备列表只包含：mac地址、did、model、设备名称（用户自定义的）、产品名称、设备实物图
     */
    public abstract List<DeviceStat> getFilterBluetoothDeviceList(String requestModel);

    /**
     * ApiLevel: 69
     * 获取model对应的产品基本信息，比如产品名称、产品icon等
     *
     * @param model
     * @return
     */
    public abstract ProductInfo getProductInfo(String model);

    /*
     * ApiLevel: 69
     * @param context 不能为null
     * @param viewGroup parent layout
     * @param attrs
     * @param defStyleAttr
     *
     * @return MJExoPlayer 创建的一个父view为参数viewGroup中的播放器
     */
    public abstract MJExoPlayer createExoPlayer(Context context, ViewGroup viewGroup, AttributeSet attrs, int defStyleAttr);

    /*
     * ApiLevel: 69
     * 根据参数生成请求url，会添加加密信息(segmentIv)和地区(region)等参数进url，然后将参数进行加密
     * @param hostParams 请求host的前缀(prefix)和路径(path)变量，包括请求方法(post, get)
     * @param pathParams 请求路径后面跟的变量数据
     *
     * @return 返回一个请求的url
     */
    public abstract String generateRequestUrl(String model, JSONObject hostParams, JSONObject pathParams);


    /*
     *
     *ApiLevel: 69
     *创建一个从m3u8生成mp4的类，通过这个类可以将流数据转化为mp4文件
     * @param model 设备的model
     *
     * @return 返回一个HLSDownloader实例
     */
    public abstract HLSDownloader getHLSDownloader(String model);

    /**
     * ApiLevel: 76
     * 获取当前用户设备列表所有的蓝牙网关设备
     *
     */
    public abstract List<DeviceStat> getBleGatewayDeviceList();
}
