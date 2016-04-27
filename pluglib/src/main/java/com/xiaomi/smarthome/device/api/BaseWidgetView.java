
package com.xiaomi.smarthome.device.api;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.xiaomi.plugin.core.XmPluginPackage;

/**
 * ApiLevel:8
 * 获取插件View
 */
public abstract class BaseWidgetView {
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    // 当前插件package信息
    protected XmPluginPackage mPluginPackage;
    // 提供设备Device数据
    public DeviceStat mDeviceStat;
    protected Intent mIntent;
    protected View mView;

    public BaseWidgetView(Context context, LayoutInflater layoutInflater,
            XmPluginPackage xmPluginPackage,
            Intent intent, DeviceStat deviceStat) {
        mContext = context;
        mLayoutInflater = layoutInflater;
        mPluginPackage = xmPluginPackage;
        mDeviceStat = deviceStat;
        mIntent = intent;
    }

    public View getView() {
        if (mView == null) {
            mView = onCreateView(mIntent);
        }
        mDeviceStat = XmPluginHostApi.instance().getDeviceByDid(mDeviceStat.did);
        onRefresh();
        return mView;
    }

    public abstract View onCreateView(Intent intent);

    public abstract void onRefresh();

    public abstract void onPause();

    public abstract void onResume();

    public abstract void onDestory();

}
