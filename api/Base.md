#基本功能接口
##XmPluginHostApi
```

    /**
     * 插件sdk版本号，插件开发如果必须要与之前api兼容，需要通过SDK_VERSION判断兼容性
     */
    public abstract int getApiLevel();
    
    /**
     * ApiLevel: 18 主app渠道，包括Dev,Sdk,Release
     *
     * @return
     */
    public abstract String getChannel();
    
     /**
     * ApiLevel:1 获取当前插件的Application
     */
    public abstract Application application();

    /**
     * ApiLevel:1 获取当前插件的Application Context
     */
    public abstract Context context();
    
    /**
     * ApiLevel:3 获取设备所在位置的天气
     *
     * @param model
     * @param did
     * @param callback
     */
    public void getWeatherInfo(String model, String did,
                               Callback<WeatherInfo> callback)
    /**
     * ApiLevel:3 上报设备的gps信息
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
                              Callback<Void> callback);
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
     * ApiLevel:17 同步设备gps信息
     */
    public void reportGPSInfo(final String did, final Callback<Void> callback) 
    
    /**
     * ApiLevel:6 把某个设备添加桌面快捷方式
     *
     * @param xmPluginPackage
     * @param did
     * @param intent
     */
    public abstract void addToLauncher(XmPluginPackage xmPluginPackage, String did, Intent intent);


    /**
     * ApiLevel:7 获取路由器文件下载地址
     *
     * @param url
     * @return
     */
    public abstract String getRouterFileDownloadUrl(String url);
    
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
     * 在ApiLevel:25后升级了微信sdk，有用到这个接口的必须更新插件sdk适配，发布新版插件并且修改minPluginSdkApiVersion为25 ApiLevel:20
     * 创建米家app注册的微信接口
     */
    public abstract IWXAPI createWXAPI(Context context, boolean bl);
    /**
     * ApiLevel: 22 获取当前服务器
     *
     * @return "cn":中国大陆 "tw":台湾 "sg":新加坡 "in":印度
     */
    public abstract String getGlobalSettingServer();
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

```
##IPluginHostActivity
```    
/**
     * ApiLevel:2 把设备添加到桌面快捷方式
     */
    public abstract void addToLauncher();
```