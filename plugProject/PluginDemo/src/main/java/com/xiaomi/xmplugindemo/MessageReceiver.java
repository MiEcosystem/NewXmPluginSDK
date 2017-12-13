
package com.xiaomi.xmplugindemo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;

import com.xiaomi.plugin.core.XmPluginPackage;
import com.xiaomi.smarthome.device.api.BaseWidgetView;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.IXmPluginMessageReceiver;
import com.xiaomi.smarthome.device.api.MessageCallback;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

/**
 * 所有插件入口函数，必须实现
 */
public class MessageReceiver implements IXmPluginMessageReceiver {

    @Override
    public boolean handleMessage(Context context, XmPluginPackage xmPluginPackage, int type,
            Intent intent,
            DeviceStat deviceStat) {
        switch (type) {
            case MSG_BROADCAST:
            case LAUNCHER: {// 启动入口
                if (deviceStat.model.startsWith("xiaomi.bledemo")) {
                    XmPluginHostApi.instance().startActivity(context, xmPluginPackage, intent,
                            deviceStat.did, BlueDemoMainActivity.class);
                } else {
                    XmPluginHostApi.instance().startActivity(context, xmPluginPackage, intent,
                            deviceStat.did, MainActivity.class);
                }
                return true;
            }
            case PUSH_MESSAGE: {
                // 订阅消息push通知
                if (intent == null)
                    return false;
                String msgType = intent.getStringExtra("type");
                if (TextUtils.isEmpty(msgType))
                    return false;
                if ("DevicePush".equals(msgType)) {// 订阅的消息
                    String data = intent.getStringExtra("data");
                    DemoDevice device = DemoDevice.getDevice(deviceStat);
                    device.onSubscribeData(data);

                } else if ("ScenePush".equals(msgType)) {// 场景消息
                    String event = intent.getStringExtra("event");
                    String extra = intent.getStringExtra("extra");
                    long time = intent.getLongExtra("time", 0);
                    boolean isNotified = intent.getBooleanExtra("isNotified", false);
                    Log.d(DemoDevice.MODEL, "ScenePush :" + event + "  " + extra);
                    // TODO 处理场景通知
                }
            }
            default:
                break;
        }
        return false;
    }

    // 智能家庭app主动插件，获取插件数据,建议定义的type>=100
    @Override
    public boolean handleMessage(Context context, XmPluginPackage xmPluginPackage, int type,
            Intent intent, DeviceStat deviceStat, MessageCallback callback) {
        if(callback!=null){
            callback.onSuccess(intent);
        }
        return false;
    }

    @Override
    public BaseWidgetView createWidgetView(Context context, LayoutInflater layoutInflater,
            XmPluginPackage xmPluginPackage, int type, Intent intent, DeviceStat deviceStat) {
        // TODO Auto-generated method stub
        return null;
    }
}
