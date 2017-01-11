
package com.xiaomi.smarthome.device.api;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import com.xiaomi.plugin.core.XmPluginPackage;

public interface IXmPluginMessageReceiver {
    /**
     * ApiLevel:1
     */
    public static final int LAUNCHER = 1;// 入口消息

    // push消息类型 数据为Json字符串，JSONObject msgData = new
    // JSONObject(intent.getStringExtra("data"));
    /**
     * ApiLevel:1
     */
    public static final int PUSH_MESSAGE = 2;

    /**
     * ApiLevel:6
     */
    public static final int MSG_LAUNCHER = 1;

    /**
     * ApiLevel:6
     */
    public static final int MSG_PUSH_MESSAGE = 2;

    /**
     * ApiLevel:6
     */
    public static final int MSG_GET_SCENE_VALUE = 3;

    /**
     * ApiLevel:8 页面跳转
     */
    public static final int MSG_PAGE_NAVIGATE = 4;

    /**
     * ApiLevel:8 设备列表状态栏
     */
    public static final int MSG_DEVICE_LIST_STATUS = 5;

    /**
     * ApiLevel:10 系统蓝牙广播ACL_CONNECTED
     */
    public static final int MSG_BROADCAST_BLUETOOTH_DEVICE_ACTION_ACL_CONNECTED = 6;

    /**
     * ApiLevel:10 系统蓝牙广播ACL_DISCONNECTED
     */
    public static final int MSG_BROADCAST_BLUETOOTH_DEVICE_ACTION_ACL_DISCONNECTED = 7;

    /**
     * ApiLevel:10 蓝牙配对
     */
    public static final int MSG_BLUETOOTH_PAIRING = 8;

    /**
     * ApiLevel:10 蓝牙断开连接
     */
    public static final int MSG_BLUETOOTH_DISCONNECT = 9;

    /**
     * ApiLevel:16 获取场景自定义条件中的extra字段
     */
    public static final int MSG_SCENE_GET_CONDITION_EXTRA = 10;

    /**
     * ApiLevel:20 设备删除
     */
    public static final int MSG_DEVICE_DELETED = 11;

    /**
     * ApiLevel:21 UPNP设备已连接
     */
    public static final int MSG_UPNP_CONNECT = 12;

    /**
     * ApiLevel:21 UPNP设备连接断开
     */
    public static final int MSG_UPNP_DISCONNECT = 13;

    /**
     * ApiLevel:22 设置超长场景extra
     */
    public static final int MSG_SET_SCENE_LARGE_EXTRA = 14;

    /**
     * ApiLevel:22 接收broadcast消息
     */
    public static final int MSG_BROADCAST = 15;

    /**
     * ApiLevel:23 UPNP设备事件
     */
    public static final int MSG_UPNP_EVENT = 16;

    /**
     * ApiLevel:27
     */
    public static final int MSG_NOTIFICATION_PENDING_INTENT = 17;

    /**
     * ApiLevel:6
     */
    public static final int MSG_CUSTOM_START = 10000;

    /**
     * ApiLevel:8
     */
    public static final int DEVICE_LIST_MAIN_VIEW = 1;// 设备列表界面

    /**
     * ApiLevel:34 请求相机数据
     * intent里面带有请求数据信息
     * key:request_frame_rate
     * value: 0代表自动，1代表480p，2代表720p，3代表1080p
     */
    public static final int MSG_REQUEST_CAMERA_FRAME = 18;

    /**
     * ApiLevel:34 停止发送数据
     */
    public static final int MSG_STOP_CAMERA_FRAME = 19;

    /**
     * 所有插件必须实现该接口，并且在type=LAUNCHER时，启动入口页面 比如下MiTVPageActivity为入口activity switch (type) { case
     * LAUNCHER: {// 启动入口 XmPluginHostApi.instance().startActivity(xmPluginPackage, intent,
     * deviceStat.did, MiTVPageActivity.class); return true; } default: break; } 且
     * 必须在AndroidManifest.xml文件中application下指定
     * <meta-data android:name="model" android:value="xiaomi.tvbox.v1"/> <meta-data android:name=
     * "message_handler" android:value="com.xiaomi.plug.mitv.MitvMessageReceiver"/> 消息处理接口，
     * 处理外部传递过来的消息 启动入口必须在这里指定<br/>
     * <br/>
     * ApiLevel:1
     */
    public boolean handleMessage(Context context, XmPluginPackage xmPluginPackage, int type,
            Intent intent, DeviceStat deviceStat);

    /**
     * ApiLevel:2 功能同上，异步调用完成后，通过callback 返回结果
     */
    public boolean handleMessage(Context context, XmPluginPackage xmPluginPackage, int type,
            Intent intent, DeviceStat deviceStat, MessageCallback callback);

    /**
     * ApiLevel:7 创建View给外边使用，如果需要在设备列表中的卡片模式中显示设备view，需要实现，否则返回null
     * 
     * @param context
     * @param layoutInflater
     * @param xmPluginPackage
     * @param type
     * @param intent
     * @param deviceStat
     * @return
     */
    public BaseWidgetView createWidgetView(Context context, LayoutInflater layoutInflater,
            XmPluginPackage xmPluginPackage, int type,
            Intent intent, DeviceStat deviceStat);

}
