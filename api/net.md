#云端相关接口
```
XmPluginHostApi    
    /**
     * ApiLevel:1 调用智能家居后台http服务
     *
     * @param model       插件model
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
     * @param model       插件model
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