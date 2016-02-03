
package com.xiaomi.smarthome.device.api;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.xiaomi.plugin.core.XmPluginPackage;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * ApiLevel: 17 主app渠道
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
     * ApiLevel:1 获取设备列表
     *
     * @return
     */
    public abstract List<DeviceStat> getDeviceList();

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
                            // result.resetFlag = object.optInt("resetFlag");
                            // result.rssi = object.optInt("rssi", 0);
                            // if ((result.bindFlag ==
                            // MiioDBConst.BIND_FLAG_UNSET
                            // && result.authFlag ==
                            // MiioDBConst.AUTH_FLAG_UNSET)) {
                            // result.token = "";
                            // } else {
                            // result.token = object.optString("token");
                            // }
                            result.ip = object.optString("localip");
                            // result.latitude = object.optDouble("latitude");
                            // result.longitude = object.optDouble("longitude");

                            // result.propInfo = object.optJSONObject("prop");
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

    // ApiLevel:1
    // 统计相关接口,参考miui开放平台统计，
    // 不推荐使用，无法把统计数据开放到第三方
    //
    // http://dev.xiaomi.com/doc/p%3D3995/index.html
    // ////////////
    public abstract void recordCountEvent(String category, String key);

    public abstract void recordCountEvent(String category, String key,
                                          Map<String, String> params);

    public abstract void recordCalculateEvent(String category, String key, long value);

    public abstract void recordCalculateEvent(String category, String key, long value,
                                              Map<String, String> params);

    public abstract void recordStringPropertyEvent(String category, String key,
                                                   String value);

    public abstract void recordNumericPropertyEvent(String category, String key,
                                                    long value);

    // ///////////////

    /**
     * ApiLevel:2 智能家庭后台统计(Deprecated)
     *
     * @param appId 第三方插件开放唯一id，定义为厂商名
     * @param value 为Object 可以为int或String或JsonObject
     * @param extra 可以为null
     */
    @Deprecated
    public abstract void addRecord(String appId, String key, Object value, JSONObject extra);

    /**
     * ApiLevel:6 智能家庭后台统计
     *
     * @param loadedInfo 插件上下文
     * @param key
     * @param value
     * @param extra
     */
    public abstract void addRecord(XmPluginPackage loadedInfo, String key, Object value,
                                   JSONObject extra);

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
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("us_id", us_id);
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
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
            dataObj.put("us_id", us_id);
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
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put("identify", did);
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
     * @param loadedInfo 插件上下文
     * @param libName    so库名字
     */
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
     * ApiLevel:4 插件中设备数据属性发生变化，同步数据到智能家庭主app中，比如设备列表中显示某些属性状态
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
    public abstract void checkLocalRouterInfo(String routerId,
                                              Callback<Void> callback);

    /**
     * ApiLevel:7 返回当前连接路由器是否是小米路由器
     *
     * @return
     */
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
            // if (name != null && !name.equals("")) {
            // dataObj.put("name", name);
            // }
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
    public void editScene(String model, int st_id, int us_id, String did, String identify,
                          String name,
                          JSONObject setting,
                          JSONArray authed, final Callback<JSONObject> callback) {
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

    public void delScene(String model, int us_id,
                         final Callback<JSONObject> callback) {
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
     * ApiLevel:14 更新设备的子设备
     */
    public abstract void updateSubDevice(XmPluginPackage xmPluginPackage,String[] didList,Callback<List<DeviceStat>> callback);

    /**
     * ApiLevel:15 异步调用第三方云接口
     */
    public abstract void callRemoteAsync(final String[] dids, final int appId, Object object, Callback<JSONObject> callback);


    /**
     * ApiLevel:16 获取蓝牙设备固件升级信息
     */
    public abstract void getBluetoothFirmwareUpdateInfo(String model, final Callback<BtFirmwareUpdateInfo> callback);
    
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
    
}
