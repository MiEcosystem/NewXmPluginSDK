
package com.xiaomi.plugin.core;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.xiaomi.smarthome.device.api.IXmPluginMessageReceiver;

import dalvik.system.DexClassLoader;

import java.util.List;

/**
 * ApiLevel:1 插件包已加载信息
 * 
 * @author chenhao
 */
public class XmPluginPackage {

    /**
     * ApiLevel:1 包名 (请使用线程安全的Getter访问)
     */
    public String packageName;

    /**
     * ApiLevel:1 包路径 (请使用线程安全的Getter访问)
     */
    public String packagePath;

    /**
     * ApiLevel:1 ClassLoader (请使用线程安全的Getter访问)
     */
    public DexClassLoader classLoader;

    /**
     * ApiLevel:1 AssetManager (请使用线程安全的Getter访问)
     */
    public AssetManager assetManager;

    /**
     * ApiLevel:1 Resources (请使用线程安全的Getter访问)
     */
    public Resources resources;

    /**
     * ApiLevel:1 PackageInfo (请使用线程安全的Getter访问)
     */
    public PackageInfo packageInfo;

    /**
     * ApiLevel:1 Model (请使用线程安全的Getter访问)
     */
    @Deprecated
    public String model;

    /**
     * ApiLevel:1 IXmPluginMessageReceiver (请使用线程安全的Getter访问)
     */
    public IXmPluginMessageReceiver xmPluginMessageReceiver;

    /**
     * ApiLevel:1 AppIcon 可能为null (请使用线程安全的Getter访问)
     */
    @Deprecated
    public Drawable appIcon;

    /**
     * ApiLevel:1 AppLabel (请使用线程安全的Getter访问)
     */
    public CharSequence appLabel;

    /**
     * ApiLevel:1 MinApiLevel (请使用线程安全的Getter访问)
     */
    public int miniApiVersion;

    /**
     * ApiLevel:1 VersionCode (请使用线程安全的Getter访问)
     */
    public int packageVersion;

    /**
     * ApiLevel:6
     */
    private List<String> mModelList;

    /**
     * ApiLevel:7
     */
    private long mPluginId;

    /**
     * ApiLevel:6
     */
    private long mPackageId;

    public XmPluginPackage(String packageName, String path,
            DexClassLoader loader, AssetManager assetManager,
            Resources resources, PackageInfo packageInfo, String model,
            IXmPluginMessageReceiver xmPluginMessageReceiver) {
        this.packageName = packageName;
        this.packagePath = path;
        this.classLoader = loader;
        this.assetManager = assetManager;
        this.resources = resources;
        this.packageInfo = packageInfo;

        this.model = model;
        this.xmPluginMessageReceiver = xmPluginMessageReceiver;
    }

    /**
     * @hide 隐藏接口(不要调用)
     */
    public synchronized void setModelList(List<String> modelList) {
        mModelList = modelList;
    }

    /**
     * @hide 隐藏接口(不要调用)
     */
    public synchronized void setPluginId(long pluginId) {
        mPluginId = pluginId;
    }

    /**
     * @hide 隐藏接口(不要调用)
     */
    public synchronized void setPackageId(long packageId) {
        mPackageId = packageId;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized String getPackageName() {
        return packageName;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized String getPackagePath() {
        return packagePath;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized DexClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized Resources getResources() {
        return resources;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized PackageInfo getRawPackageInfo() {
        return packageInfo;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized List<String> getModelList() {
        return mModelList;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized IXmPluginMessageReceiver getMessageReceiver() {
        return xmPluginMessageReceiver;
    }

    /**
     * ApiLevel:6, 可能为null
     * 
     * @return
     */
    @Deprecated
    public synchronized Drawable getAppIcon() {
        return appIcon;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized CharSequence getAppLabel() {
        return appLabel;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized int getMinApiLevel() {
        return miniApiVersion;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized int getVersionCode() {
        return packageVersion;
    }

    /**
     * ApiLevel:7
     * 
     * @return
     */
    public synchronized long getPluginId() {
        return mPluginId;
    }

    /**
     * ApiLevel:6
     * 
     * @return
     */
    public synchronized long getPackageId() {
        return mPackageId;
    }
}
