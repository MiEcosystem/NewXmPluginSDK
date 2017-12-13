# 云端相关接口
## XmPluginHostApi
```    
/**
 * ApiLevel:1 调用智能家居后台http服务
 *
 * @param model       米家扩展程序model
 * @param relativeUrl 服务接口url，调用请确认后台支持情况
 * @param params
 * @param callback
 * @param parser
 */
public abstract <T> void callSmartHomeApi(String model, String relativeUrl, JSONObject params,
                                          final Callback<T> callback, final Parser<T> parser);
                                          
/**
 * ApiLevel: 13 调用普通http请求
 *
 * @param model
 * @param url
 * @param method
 * @param params
 * @param callback
 * @param parser
 */
public abstract <T> void callHttpApiV13(String model, String url, String method,
                                        List<KeyValuePair> params, final Callback<T> callback, final Parser<T> parser);
                                        
/**
 * ApiLevel:8 调用智能家居后台http服务
 *
 * @param model       米家扩展程序model
 * @param relativeUrl 服务接口url
 * @param params
 * @param callback
 * @param parser
 */
public abstract <T> void callSmartHomeApi(String model, String relativeUrl, String params,
                                          final Callback<T> callback, final Parser<T> parser);
                                          
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
 * ApiLevel:15 异步调用第三方云接口 需要注意，由于后台返回的数据原因，目前回调接口可能有2种数据。
 * 目前异步接口通过push通知和超时重试2种方式获取数据。其中超时重试可能会比push获取的数据多封装一层
 * 如push获取的数据如果如下"{\"bssid\":\"d0:ee:07:23:22:90\"}"，则超时重试则可能会是{"code":0,"message":"ok","result":"{\"bssid\":\"d0:ee:07:23:22:90\"}"}
 * 有效载荷在result字段中。米家扩展程序解析可能需要对数据做兼容处理
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
 * ApiLevel: 42 创建或修改设置app/米家扩展程序自由存储空间。如果数据超过服务器设置的阈值，自动分段存储到云端。
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
```

## 调用米家云端API
米家扩展程序可以自行调用callSmartHomeApi访问米家云端API，通过不同的relativeUrl和params来访问不同的云端功能。

具体不同设备开放的云端接口请参照与米家云端对接时提供的文档或说明，以云端给出的信息为准。

支持的部分云端API：

>* `/scene/list` 获取设备定时列表
>* `/scene/delete` 删除设备定时
>* `/scene/edit` 创建（编辑）设备定时
>* `/home/latest_version` {"model": model} 获取最新固件版本（蓝牙设备）
>* `/home/checkversion` {"pid":0, "did":did} 获取最新固件版本（WIFI设备）
>米家扩展程序获取设备上报给米家云端的 属性 与 事件 接口（包含蓝牙设备通过蓝牙网关上报的数据）：
>* `/user/get_user_device_data`  读取与时间相关数据，请求参数示例：
>
>	```
>	{
>		"did":"123",   //设备 id
>		"uid":'123',   //要查询的用户 uid 
>		"key":"power", //与上报时一致
>		"type":"prop", //与上报时一致，属性 为 prop ，事件为 event
>		"time_start":"1473841870", //数据起点时间，单位为秒
>		"time_end":"1473841880", //数据终点时间，单位为为秒
>		"group": //返回数据的方式，默认 raw , 可选值为 hour、day、week、 month。
>		"limit": //返回数据的条数，默认 20，最大 1000
>	}
>	```
>* `/device/batchdevicedatas` 读取与时间无关数据，请求参数示例：
>
>	```
>	{
>	    "0":{
>	        "did":"311223", //设备 id
>	        "props":[
>	            "prop.usb_on",
>	            "prop.on"
>	        ]
>	    },
>	    "1":{
>	        "did":"311304",
>	        "props":[
>	            "prop.usb_on",
>	            "prop.on"
>	        ]
>	    }
>	}
>	```
>* `/user/set_user_device_data`   米家扩展程序上报设备数据（属性与事件）至米家云端，支持批量，请求参数示例：
>
>	```
>	{
>		"0": {
>			"uid": "xxx", //用户 uid
>			"did": "123", //设备id
>			"time": "1473841870", //时间戳，单位为秒
>			"type": "prop", // 属性为 prop，事件为 event
>			"key": "power",
>			"value": {} 
>		},
>		"1": {
>			"uid": "xxx",
>			"did": "456",
>			"time": "1473841888",
>			"type": "prop",
>			"key": "power",
>			"value": {}
>		}
>	}
>	```
>*注：米家服务器不解析该 `value` 故可按照自身需要定义内部格式，只要保证 `value` 最终是 >`string` 即可。*

米家扩展程序存取跟设备相关数据，设备解绑（被用户删除）时，数据会被服务器自动清理。

>* `/device/getsetting` 获取数据，参数示例：
>
>	```
>	{
>		"did":xxx,
>		"settings":["keyid_xxx_data"]
>	}
>	```
>* `/device/setsetting` 设置数据，参数示例：
>
>	```
>	{
>		"did":xxx,
>		"settings":{
>			"keyid_xxx_data": "value1"
>		}
>	}
>	```

示例：

>	```
>	// 删除已经设置的定时
>	XmPluginHostApi.instance().callSmartHomeApi(model, "/scene/delete", dataObj, callback, null);
>	// 获取设备上报数据
>	XmPluginHostApi.instance().callSmartHomeApi(model, "/user/get_user_device_data", {"did": did,"uid": ,"key":"power","type":"prop","time_start":"1473841870","time_end":"1473841880"}, callback, parser);
>	```
