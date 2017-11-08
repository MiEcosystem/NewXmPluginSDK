# Activity和页面相关API
## XmPluginHostApi    
```
/**
 * ApiLevel:1
 *
 * @param xmPluginPackage 插件包，XmPluginBaseActivity中的mPluginPackage活取
 * @param intent          传入的参数
 * @param did             设备did
 * @param activityClass   启动的activity
 */
public abstract void startActivity(Context context, XmPluginPackage xmPluginPackage,
                                   Intent intent,
                                   String did, Class activityClass);
                                   
/**
 * ApiLevel:8 页面跳转,跳转到一个新页面并加载uri
 *
 * @param xmPluginPackage
 * @param uri
 */
public abstract void gotoPage(Context context, XmPluginPackage xmPluginPackage, Uri uri,
                              Callback<Void> callback);
                              
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
 * @param activity
 * @param did
 */
public abstract void gotoAuthManagerPage(Activity activity, String did);
    
```
## IPluginHostActivity
```
/**
 * ApiLevel:1 打开分享页面
 */
public abstract void openShareActivity();
    
/**
 * ApiLevel:1 进入默认的固件升级页面
 */
public abstract void goUpdateActivity();
    
/**
 * ApiLevel:14 进入某个设备固件更新
 */
public abstract void goUpdateActivity(String did);
    
/**
 * ApiLevel:2 打开反馈页面
 */
public abstract void openFeedbackActivity();
    
/**
 * ApiLevel:4 打开智能场景页
 *
 * @param did 设备id
 */
public abstract void openSceneActivity(String did);
    
/**
 * ApiLevel:22 跳转到帮助页面
 */
public abstract void openHelpActivity();

/**
 * ApiLevel:23 跳转到ble设备升级页面
 * @param intent
 * @param upgrader
 */
public abstract void goBleUpdateActivity(Intent intent, BleUpgrader upgrader);

/**
 * ApiLevel:24 跳转水电燃气缴费页面
 * @param type 0:水电燃气缴费主页面 1:水 2:电 3:燃气
 * @param latitude 纬度
 * @param longitude 经度
 */
public abstract void openRechargePage(int type,double latitude,double longitude);

/**
 * ApiLevel: 9 打开分享页面，分享url或者分享图片
 *
 * @param shareTitle    分享title
 * @param shareContent  分享文字内容
 * @param shareUrl      分享链接
 * @param shareImage    分享图片
 * @param shareThumbUrl 分享缩略图url
 * @param thumb         分享缩略图
 */
public void openShareMediaActivity(String shareTitle,
                                   String shareContent,
                                   String shareUrl,
                                   Bitmap shareImage,
                                   String shareThumbUrl,
                                   Bitmap thumb
);
    
/**
 * ApiLevel: 13 打开分享页面，分享多张图片
 *
 * @param shareTitle        分享title
 * @param shareContent      分享文字内容
 * @param shareImagesZipUrl 分享链接图片zip包，可以是本地文件zip包文件路径，本地单张图片路径，网络zip包url
 */
public void openShareMediaActivity(String shareTitle,
                                   String shareContent,
                                   String shareImagesZipUrl
);

/**
 * ApiLevel: 18 打开分享页面，分享多张图片,添加缩略图
 *
 * @param shareTitle        分享title
 * @param shareContent      分享文字内容
 * @param shareImagesZipUrl 分享链接图片zip包，可以是本地文件zip包文件路径，本地单张图片路径，网络zip包url
 * @param thumb      缩略图，不超过32k大小
 */
public void openShareMediaActivity(String shareTitle,
                                   String shareContent,
                                   String shareImagesZipUrl, Bitmap thumb
);
    
/**
 * ApiLevel: 15 打开分享dialog，分享本地图片
 *
 * @param shareTitle        分享title
 * @param shareContent      分享文字内容
 * @param shareImagesFile 本地单张图片路径
 */
public void openSharePictureActivity(String shareTitle,
                                     String shareContent,
                                     String shareImagesFile
);
    
/**
 * ApiLevel:25 跳转二维码扫描页面
 * @param bundle 请求参数，可以传null即为扫描二维码,或者指定扫描码类型 bundle.putIntArray("barcode_format",new int[]{IXmPluginHostActivity.BarcodeFormat.QR_CODE.ordinal()});
 * 请求逻辑@see #Activity.startActivityForResult(Intent, requestCode)
 * @param requestCode @see #Activity.startActivityForResult(Intent, requestCode)
 *
 * 结果返回到调用Activity的onActivityResult中，调用如下
 * <pre class="prettyprint">
 *    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 *        super.onActivityResult(requestCode, resultCode, data);
 *        if(resultCode==RESULT_OK){
 *            if(requestCode==SCAN_BARCODE){
 *            String result = data.getStringExtra("scan_result");
 *             }
 *         }
 *    }
 * </pre>
 */
public abstract void openScanBarcodePage(Bundle bundle,int requestCode);

/**
 * ApiLevel: 25 新打开更多界面接口，params传递插件自定义参数
 *下面的参数可以禁用某些菜单
 *public static final String ARGS_FIRMWARE_ENABLE = "firmware_enable";
 *public static final String ARGS_SHARE_EBABLE = "share_enable";
 *public static final String ARGS_UNBIND_ENABLE = "unbind_enable";
 *public static final String ARGS_DELETE_ENABLE = "delete_enable";
 * @param menus
 * @param useDefault
 * @param requestCode
 */
public abstract void openMoreMenu(ArrayList<MenuItemBase> menus,
                                  boolean useDefault, int requestCode, Intent params);

/**
 * ApiLevel: 27 更多菜单新标准，从上下拉菜单，默认有
 * 智能 scence_enable
 * 通用设置 common_setting_enable
 * 使用帮助 help_feedback_enable
 *
 * ArrayList<IXmPluginHostActivity.MenuItemBase> menus = new
 ArrayList<>();

 //插件自定义菜单，可以在public void onActivityResult(int requestCode, int resultCode, Intent data) 中接收用户点击的菜单项，String result = data.getStringExtra("menu");
 IXmPluginHostActivity.StringMenuItem stringMenuItem = new
 IXmPluginHostActivity.StringMenuItem();
 stringMenuItem.name = "test string menu";
 menus.add(stringMenuItem);

 //跳转到插件下一个activity的菜单
 IXmPluginHostActivity.IntentMenuItem intentMenuItem = new
 IXmPluginHostActivity.IntentMenuItem();
 intentMenuItem.name = "test intent menu";
 intentMenuItem.intent =
 mHostActivity.getActivityIntent(null,
 ApiDemosActivity.class.getName());
 menus.add(intentMenuItem);

 //带开关按钮的菜单，可以自动调用设备rpc
 IXmPluginHostActivity.SlideBtnMenuItem slideBtnMenuItem = new
 IXmPluginHostActivity.SlideBtnMenuItem();
 slideBtnMenuItem.name = "test slide menu";
 slideBtnMenuItem.isOn = mDevice.getRgb() > 0;
 slideBtnMenuItem.onMethod = "set_rgb";
 JSONArray onparams = new JSONArray();
 onparams.put(0xffffff);
 slideBtnMenuItem.onParams =
 onparams.toString();
 slideBtnMenuItem.offMethod = "set_rgb";
 JSONArray offparams = new JSONArray();
 offparams.put(0);
 slideBtnMenuItem.offParams =
 offparams.toString();
 menus.add(slideBtnMenuItem);

 mHostActivity.openMoreMenu2(menus, true, REQUEST_MENU, null);

 params 设置 security_setting_enable true，添加安全设置接口
 * @param menus
 * @param useDefault
 * @param requestCode
 */
public abstract void openMoreMenu2(ArrayList<MenuItemBase> menus,
                                  boolean useDefault, int requestCode, Intent params);
                                  
/**
 * ApiLevel:2 打开webview
 *
 * @param url
 * @param title
 */
public abstract void loadWebView(String url, String title);
    
/**
 * ApiLevel:6 打开另一个插件设备页面
 *
 * @param did
 */
public abstract void openDevice(String did, Intent intent);
    
/**
 * ApiLevel:1 实现沉浸式标题栏，需要传入当前Activity中的titlebar view
 *
 * @param titleBarView
 */
public abstract void setTitleBarPadding(View titleBarView);
    
/**
 * ApiLevel:4 是否支持状态栏透明显示
 */
public abstract boolean isTranslucentStatusbarEnable();
    
/**
 * ApiLevel:2 设置白色的状态栏，默认
 */
public abstract void enableWhiteTranslucentStatus();
    
/**
 * ApiLevel:15 设置黑色的状态栏，默认是黑色的
 */
public abstract void enableBlackTranslucentStatus();

/**
 * ApiLevel:3 设置插件Activity动画 public final String ANIM_SLIDE_IN_LEFT =
 * "slide_in_left"; public final String ANIM_SLIDE_IN_RIGHT =
 * "slide_in_right"; public final String ANIM_SLIDE_IN_TOP = "slide_in_top";
 * public final String ANIM_SLIDE_IN_BOTTOM = "slide_in_bottom"; public
 * final String ANIM_SLIDE_OUT_LEFT = "slide_out_left"; public final String
 * ANIM_SLIDE_OUT_RIGHT = "slide_out_right"; public final String
 * ANIM_SLIDE_OUT_TOP = "slide_out_top"; public final String
 * ANIM_SLIDE_OUT_BOTTOM = "slide_out_bottom";
 *
 * @param enterAnim
 * @param exitAnim
 */
public void overridePendingTransition(String enterAnim, String exitAnim);
    
/**
 * ApiLevel: 29 打开分享dialog，支持分享视频,必须是mp4文件
 *
 * @param shareTitle        分享title
 * @param shareContent      分享文字内容
 * @param shareImagesFile 本地视频路径
 */
public void openShareVideoActivity(String shareTitle,
                                     String shareContent,
                                     String shareImagesFile
);

/**
 * ApiLevel: 32 开启插件广告的支持
 */
void enableAd();

/**
 * ApiLevel: 32 展示插屏弹窗广告
 */
void showPopAd();

/**
 * ApiLevel: 32 展示页面下方弹窗
 */
void showBottomPopAd();

/**
 * ApiLevel: 32 创建Banner广告
 *
 * @param vAdContainer 用于展示广告View的ViewGroup
 */
void showBannerAd(ViewGroup vAdContainer);

/**
 * ApiLevel: 32 展示通知广告
 *
 * @param vAdContainer 用于展示广告View的ViewGroup
 */
void showNoticeAd(ViewGroup vAdContainer);

/*
 * ApiLevel: 32 打开设备操作历史记录
 *
 */
public void openOpHistoryActivity();

/**
 * ApiLevel: 33
 *
 * 上报热区广告的展示
 *
 * @param gid 商品id
 * @param hotSpotId 热区广告id，用于区分不同的广告
 */
void reportHotSpotAdShow(String gid, String hotSpotId);

/**
 * ApiLevel: 37 打开设备分享页面
 */
void openShareDeviceActivity();
    
/**
 * ApiLevel:1 启动插件中的Activity
 *
 * @param intent
 * @param packageName 插件包名
 * @param className   启动的Activity类名
 * @param requestCode
 */
public abstract void startActivityForResult(Intent intent, String packageName,
                                            String className, int requestCode);
                                            
/**
 * ApiLevel:1 获取启动插件内activity的Intent,用于设备更多页面进入设置页面
 *
 * @param packageName
 * @param className
 * @return
 */
public Intent getActivityIntent(String packageName, String className);

```
