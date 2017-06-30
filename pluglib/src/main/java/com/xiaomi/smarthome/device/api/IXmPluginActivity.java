
package com.xiaomi.smarthome.device.api;

import com.xiaomi.plugin.core.XmPluginPackage;

/**
 * activity插件接口
 */
public interface IXmPluginActivity {
    public static final String EXTRA_PACKAGE = "extra_package";
    public static final String EXTRA_CLASS = "extra_class";

    public void attach(IXmPluginHostActivity mainActivity, XmPluginPackage pluginPackage,
                       DeviceStat deviceStat);
}
