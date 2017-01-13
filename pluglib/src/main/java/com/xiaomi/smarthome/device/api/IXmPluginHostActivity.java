
package com.xiaomi.smarthome.device.api;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomi.smarthome.bluetooth.BleUpgrader;
import com.xiaomi.smarthome.bluetooth.IBleUpgradeController;
import com.xiaomi.smarthome.bluetooth.ISlideBtnController;
import com.xiaomi.smarthome.bluetooth.SlideBtnController;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;

import java.util.ArrayList;
import java.util.List;

public interface IXmPluginHostActivity {
    public FragmentActivity activity();

    /**
     * ApiLevel:5 在启动设备更多页时，可以告知更多页该Item是调起Host程序的activity
     * 具体哪个activity由TARGET_ACTIVITY_IN_HOST_XXX指派
     */
    public static final String KEY_INTENT_TARGET_ACTIVITY_IN_HOST = "target_activity";
    /**
     * ApiLevel:5
     */
    public static final int TARGET_ACTIVITY_IN_HOST_DEVICE_SCENE = 1;

    /**
     * ApiLevel:32
     */
    public static final int TARGET_ACTIVITY_IN_HOST_DEVICE_OP_HISTORY = 2;


    // 动画类型
    public final String ANIM_SLIDE_IN_LEFT = "slide_in_left";
    public final String ANIM_SLIDE_IN_RIGHT = "slide_in_right";
    public final String ANIM_SLIDE_IN_TOP = "slide_in_top";
    public final String ANIM_SLIDE_IN_BOTTOM = "slide_in_bottom";
    public final String ANIM_SLIDE_OUT_LEFT = "slide_out_left";
    public final String ANIM_SLIDE_OUT_RIGHT = "slide_out_right";
    public final String ANIM_SLIDE_OUT_TOP = "slide_out_top";
    public final String ANIM_SLIDE_OUT_BOTTOM = "slide_out_bottom";
    public final String ANIM_FADE_IN_LEFT = "fade_in_left";
    public final String ANIM_FADE_IN_RIGHT = "fade_in_right";
    public final String ANIM_FADE_OUT_LEFT = "fade_out_left";
    public final String ANIM_FADE_OUT_RIGHT = "fade_out_right";

    /**@deprecated
     * @see MenuItemBase
     *
     *  ApiLevel:1 打开菜单，onActivityResult()返回用户点击结果 String selectMenu =
     * data.getStringExtra("menu");
     *
     * @param menus       自定义菜单列表，在默认菜单之上,点击后推出菜单项
     * @param intents     自定义菜单列表，在默认菜单之上，点击后打开Intent
     * @param useDefault  true使用默认菜单，false不使用默认菜单
     * @param requestCode requestCode If >= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public abstract void openMoreMenu(ArrayList<String> menus, ArrayList<Intent> intents,
                                      boolean useDefault, int requestCode);

    /**
     * ApiLevel:1 打开分享页面
     */
    public abstract void openShareActivity();

    /**
     * ApiLevel:1 实现沉浸式标题栏，需要传入当前Activity中的titlebar view
     *
     * @param titleBarView
     */
    public abstract void setTitleBarPadding(View titleBarView);

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

    /**
     * ApiLevel:1 进入默认的固件升级页面
     */
    public abstract void goUpdateActivity();

    /**
     * ApiLevel:1 开始加载场景
     */
    public abstract void startLoadScene();

    /**
     * ApiLevel:7 开始设置智能设备的场景
     *
     * @param deviceId
     */
    public abstract void startCreateSceneByDid(String deviceId);

    /**
     * ApiLevel:1 编辑当前已有智能场景
     *
     * @param sceneId
     */
    public abstract void startEditScene(int sceneId);

    /**
     * ApiLevel:2 增加通用时间设置接口
     *
     * @param deviceId   device id
     * @param onMethod   当on timer时间到的时候执行的action
     * @param onParams   on action的参数
     * @param offMethod  当off timer时间到的时候执行的action
     * @param offParams  off action参数
     * @param timerName  定时器名称
     * @param timerTitle 定时器标题
     */
    @Deprecated
    public abstract void startSetTimerList(String deviceId, String onMethod, String onParams,
                                           String offMethod, String offParams, String timerName, String timerTitle);

    /**
     * ApiLevel:2 设置白色的状态栏，默认
     */
    public abstract void enableWhiteTranslucentStatus();

    /**
     * ApiLevel:2 打开反馈页面
     */
    public abstract void openFeedbackActivity();

    /**
     * ApiLevel:2 把设备添加到桌面快捷方式
     */
    public abstract void addToLauncher();

    /**
     * ApiLevel:2 打开webview
     *
     * @param url
     * @param title
     */
    public abstract void loadWebView(String url, String title);

    /**
     * ApiLevel:3 打开商城页面
     *
     * @param gid
     */
    public abstract void openShopActivity(String gid);

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
     * @see #openShareMediaActivity() ApiLevel:3 分享米聊，微信，朋友圈，sina微博，
     * @deprecated
     */
    public void share(String shareTitle,
                      String shareContent,
                      String shareUrl,
                      String shareImageUrl,
                      String shareThumbUrl,
                      Bitmap thumb);

    /**
     * ApiLevel:4 刷新新连接进入的特定子设备
     *
     * @param targetModel
     * @param callback
     */
    public void startSearchNewDevice(String targetModel, String did,
                                     DeviceFindCallback callback);

    public interface DeviceFindCallback {
        void onDeviceFind(List<DeviceStat> deviceStatList);
    }

    /**
     * ApiLevel:4 打开智能场景页
     *
     * @param did 设备id
     */
    public abstract void openSceneActivity(String did);

    /**
     * ApiLevel:4 是否支持状态栏透明显示
     */
    public abstract boolean isTranslucentStatusbarEnable();

    /**
     * ApiLevel:6 打开一个设备
     *
     * @param did
     */
    public abstract void openDevice(String did, Intent intent);

    /**
     * ApiLevel:8 异步请求回调
     */
    public abstract class AsyncCallback<T> {
        public abstract void onSuccess(T result);

        public abstract void onFailure(int shError, Object errorInfo);
    }

    /**
     * ApiLevel:8 获取当前设备所支持的推荐场景
     */
    public abstract void getDeviceRecommendScenes(String did,
                                                  AsyncCallback<List<RecommendSceneItem>> callback);

    /**
     * ApiLevel:8 开始编辑推荐场景
     *
     * @param model 当前设备的model
     * @param did   当前设备的did
     */
    public abstract void startEditRecommendScenes(RecommendSceneItem item, String model, String did);

    /**
     * ApiLevel:8 根据did获取场景
     *
     * @param did 当前设备的did
     */
    public abstract List<SceneInfo> getSceneByDid(String did);

    /**
     * ApiLevel:8 enable or disable the specific scene
     */
    public abstract void setSceneEnabled(SceneInfo info, boolean enable,
                                         AsyncCallback<Void> callback);

    /**
     * ApiLevel:8 modeify scene name
     */
    public abstract void modifySceneName(SceneInfo info, AsyncCallback<Void> callback);

    /**@deprecated
     * @see MenuItemBase
     * 
     * ApiLevel:8 打开菜单,添加传设备did参数，onActivityResult()返回用户点击结果 String
     * selectMenu = data.getStringExtra("menu");
     *
     * @param menus       自定义菜单列表，在默认菜单之上,点击后推出菜单项
     * @param intents     自定义菜单列表，在默认菜单之上，点击后打开Intent
     * @param useDefault  true使用默认菜单，false不使用默认菜单
     * @param requestCode requestCode If >= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public abstract void openMoreMenu(String did, ArrayList<String> menus,
                                      ArrayList<Intent> intents,
                                      boolean useDefault, int requestCode);

    /**
     * ApiLevel: 8 startSetTimerList(mDeviceStat.did, "set_rgb",
     * String.valueOf(0x00ffffff), "set_rgb", String.valueOf(0x00000000),
     * mDeviceStat.did, "RGB灯定时器", "RGB灯定时器");
     *
     * @param deviceId    device id
     * @param onMethod    当on timer时间到的时候执行的action，执行原始数据
     * @param onParams    on action的参数
     * @param offMethod   当off timer时间到的时候执行的action，执行原始数据
     * @param offParams   off action参数
     * @param identify    定时器的identify，使用device id
     * @param displayName 定时的名称,在场景执行日志中显示
     * @param timerTitle  定时器标题
     */
    public void startSetTimerList(String deviceId, String onMethod, String onParams,
                                  String offMethod, String offParams, String identify, String displayName,
                                  String timerTitle);

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
     * ApiLevel: 13 更多菜单 菜单项基类
     **/
    public static abstract class MenuItemBase implements Parcelable {

    }

    /**
     * ApiLevel: 13 onActivityResult()返回用户点击结果 String selectMenu =
     * data.getStringExtra("menu");
     */
    public static class StringMenuItem extends MenuItemBase {
        public String name;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
        }

        public static final Creator<StringMenuItem> CREATOR = new Creator<StringMenuItem>() {
            public StringMenuItem createFromParcel(Parcel in) {
                return new StringMenuItem(in);
            }

            public StringMenuItem[] newArray(int size) {
                return new StringMenuItem[size];
            }

        };

        public StringMenuItem(Parcel in) {
            name = in.readString();
        }

        public StringMenuItem() {
        }
    }

    /**
     * ApiLevel: 29 只用于信息显示，不会有任何操作
     */
    public static class InfoMenuItem extends MenuItemBase {
        public String name;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
        }

        public static final Creator<InfoMenuItem> CREATOR = new Creator<InfoMenuItem>() {
            public InfoMenuItem createFromParcel(Parcel in) {
                return new InfoMenuItem(in);
            }

            public InfoMenuItem[] newArray(int size) {
                return new InfoMenuItem[size];
            }

        };

        public InfoMenuItem(Parcel in) {
            name = in.readString();
        }

        public InfoMenuItem() {
        }
    }

    /**
     * ApiLevel: 13 通过Intent跳到下一页
     */
    public static class IntentMenuItem extends MenuItemBase {
        public String name;
        public Intent intent;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeParcelable(intent, flags);
        }

        public static final Creator<IntentMenuItem> CREATOR = new Creator<IntentMenuItem>() {
            public IntentMenuItem createFromParcel(Parcel in) {
                return new IntentMenuItem(in);
            }

            public IntentMenuItem[] newArray(int size) {
                return new IntentMenuItem[size];
            }

        };

        public IntentMenuItem(Parcel in) {
            name = in.readString();
            intent = in.readParcelable(Intent.class.getClassLoader());
        }

        public IntentMenuItem() {
        }
    }

    /**
     * ApiLevel: 13 开关按钮
     */
    public static class SlideBtnMenuItem extends MenuItemBase {
        public String name;
        //ApiLevel: 20 支持副标题
        public String subName;
        
        public String onMethod;// 开启时rpc调用
        public String onParams;
        public String offMethod;// 关闭rpc调用
        public String offParams;
        public boolean isOn;

        public ISlideBtnController controller;

        //内部使用
        public boolean isClicked = false;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(onMethod);
            dest.writeString(onParams);
            dest.writeString(offMethod);
            dest.writeString(offParams);
            dest.writeInt(isOn ? 1 : 0);
            dest.writeString(subName);

            if (controller != null) {
                dest.writeInt(1);
                dest.writeStrongBinder(controller.asBinder());
            } else {
                dest.writeInt(0);
            }
        }

        public static final Creator<SlideBtnMenuItem> CREATOR = new Creator<SlideBtnMenuItem>() {
            public SlideBtnMenuItem createFromParcel(Parcel in) {
                return new SlideBtnMenuItem(in);
            }

            public SlideBtnMenuItem[] newArray(int size) {
                return new SlideBtnMenuItem[size];
            }

        };

        public SlideBtnMenuItem(Parcel in) {
            name = in.readString();
            onMethod = in.readString();
            onParams = in.readString();
            offMethod = in.readString();
            offParams = in.readString();
            isOn = in.readInt() == 1;
            subName = in.readString();

            int hasBinder = in.readInt();
            if (hasBinder != 0) {
                IBinder binder = in.readStrongBinder();
                controller = ISlideBtnController.Stub.asInterface(binder);
            }
        }

        public SlideBtnMenuItem() {
        }
    }

    /**
     * ApiLevel: 13 新打开更多界面接口，加入开关项
     *
     * @param menus
     * @param useDefault
     * @param requestCode
     */
    public abstract void openMoreMenu(ArrayList<MenuItemBase> menus,
                                      boolean useDefault, int requestCode);

    /**
     * ApiLevel: 13 打开自定义场景创建页面
     *
     */
    public abstract void startEditCustomScene();
    
    /**
     * ApiLevel:14 进入某个设备固件更新
     */
    public abstract void goUpdateActivity(String did);
    
    /**
     * ApiLevel:15 设置黑色的状态栏，默认是黑色的
     */
    public abstract void enableBlackTranslucentStatus();
    /**
     * ApiLevel:19 开始加载场景
     */
    public abstract void startLoadScene(AsyncCallback callback);

    /**
     * ApiLevel:20 蓝牙设备的设置项
     */
    public static class BleMenuItem extends MenuItemBase {

        public String key;
        public Intent intent;

        public static final String EXTRA_HAS_NEWER = "extra_has_newer";
        public static final String EXTRA_UPGRADE_CONTROLLER = "extra_upgrade_controller";

        public BleMenuItem() {
            intent = new Intent();
        }

        public static BleMenuItem newUpgraderItem(BleUpgrader upgrader) {
            BleMenuItem item = new BleMenuItem();
            item.key = XmBluetoothManager.KEY_FIRMWARE_CLICK;
            item.setUpgrader(upgrader);
            return item;
        }

        private void setUpgrader(BleUpgrader upgrader) {
            Bundle bundle = new Bundle();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bundle.putBinder(EXTRA_UPGRADE_CONTROLLER, upgrader);
            }

            intent.putExtras(bundle);
        }

        public IBleUpgradeController getBleUpgrader() {
            IBinder binder = null;

            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    binder = bundle.getBinder(IXmPluginHostActivity.BleMenuItem.EXTRA_UPGRADE_CONTROLLER);
                }
            }

            if (binder != null) {
                return IBleUpgradeController.Stub.asInterface(binder);
            }

            return null;
        }

        @Deprecated
        public void setHasNewerVersion(boolean flag) {
//            intent.putExtra(EXTRA_HAS_NEWER, flag);
        }

        public BleMenuItem(Parcel in) {
            key = in.readString();
            intent = in.readParcelable(Intent.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(key);
            dest.writeParcelable(intent, flags);
        }

        public static final Creator<BleMenuItem> CREATOR = new Creator<BleMenuItem>() {
            public BleMenuItem createFromParcel(Parcel in) {
                return new BleMenuItem(in);
            }

            public BleMenuItem[] newArray(int size) {
                return new BleMenuItem[size];
            }

        };
    }

    /**
     * ApiLevel:22 跳转到帮助页面
     */
    public abstract void openHelpActivity();


    /**
     * ApiLevel:23
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

     public enum BarcodeFormat {

        /** Aztec 2D barcode format. */
        AZTEC,

        /** CODABAR 1D format. */
        CODABAR,

        /** Code 39 1D format. */
        CODE_39,

        /** Code 93 1D format. */
        CODE_93,

        /** Code 128 1D format. */
        CODE_128,

        /** Data Matrix 2D barcode format. */
        DATA_MATRIX,

        /** EAN-8 1D format. */
        EAN_8,

        /** EAN-13 1D format. */
        EAN_13,

        /** ITF (Interleaved Two of Five) 1D format. */
        ITF,

        /** MaxiCode 2D barcode format. */
        MAXICODE,

        /** PDF417 format. */
        PDF_417,

        /** QR Code 2D barcode format. */
        QR_CODE,

        /** RSS 14 */
        RSS_14,

        /** RSS EXPANDED */
        RSS_EXPANDED,

        /** UPC-A 1D format. */
        UPC_A,

        /** UPC-E 1D format. */
        UPC_E,

        /** UPC/EAN extension format. Not a stand-alone format. */
        UPC_EAN_EXTENSION

    }

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
     * 智能场景 scence_enable
     * 通用设置 common_setting_enable
     * 帮助与反馈 help_feedback_enable
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
     *  ApiLevel: 29 需要验证pincode，如果设置pincode，则每次打开页面自动跳到验证pincode页面
     *  设置里边需要打开安全设置选项，参考openMoreMenu2接口
     *
     */
    public abstract void enableVerifyPincode();

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
     *
     *
     *
     */
    public void openOpHistoryActivity(
    );

    /**
     * ApiLevel: 33
     *
     * 上报热区广告的展示
     *
     * @param gid 商品id
     * @param hotSpotId 热区广告id，用于区分不同的广告
     */
    void reportHotSpotAdShow(String gid, String hotSpotId);
}
