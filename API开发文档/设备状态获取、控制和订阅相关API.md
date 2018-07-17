# 设备获取控制及状态获取相关API
## XmPluginHostApi
```   
/**
 * ApiLevel:1 获取设备列表
 *
 * @return
 */
public abstract List<DeviceStat> getDeviceList();

/**
 * ApiLevel:3 主动触发刷新设备列表
 *
 * @param callback
 * @return
 */
public abstract void updateDeviceList(Callback<Void> callback);

/**
 * ApiLevel: 30 获取指定model的设备列表
 *
 * @return
 */
public abstract List<DeviceStat> getDeviceListV2(List<String> modelList);

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
                         final Callback<List<DeviceStat>> callback);
                         
/**
 * ApiLevel: 59 设备方法调用，强制从云端调用，object为JSONObject或者为JSONArray
 *
 * @param method   方法名，在开放平台上配置，需要固件支持
 * @param params
 * @param callback 回调结果
 * @param parser
 */
public abstract <T> void callMethodFromCloud(String did, String method, Object params,
                                             final Callback<T> callback, final Parser<T> parser);
                                             
/**
 * ApiLevel:4 米家扩展程序中设备数据属性发生变化，同步数据到米家主app中，比如设备列表中显示某些属性状态
 *
 * @param did
 * @param jsonObject
 * @return
 */
public abstract void updateDeviceProperties(String did, JSONObject jsonObject);
    
/**
 * ApiLevel: 32,从服务器批量获取设备属性
 *
 * @param jsonArray [{"did":"aaa", "props":["prop.aaa","prop.bbb"]},{"did":"123", "props":["prop.jjjj","prop.777"]}]
 * @param callback
 */
public void batchGetDeviceProps(String model, JSONArray jsonArray, Callback<String> callback);

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
                              long timeEnd, Callback<JSONArray> callback);
                              
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
                                         
/**
 * ApiLevel: 38
 * 根据did获取设备的订阅属性，此接口是直接获取app已经获取到的属性
 */
public abstract JSONArray getDeviceProp(String did);

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
 * ApiLevel:14 更新设备的子设备
 */
public abstract void updateSubDevice(XmPluginPackage xmPluginPackage, String[] didList,
                                     Callback<List<DeviceStat>> callback);
```
## IXmPluginHostActivity
```
/**
 * ApiLevel:4 刷新新连接进入的特定子设备
 *
 * @param targetModel
 * @param callback
 */
public void startSearchNewDevice(String targetModel, String did,
                                 DeviceFindCallback callback);
```
