package com.xiaomi.smarthome.upnp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by swx on 4/25/16.
 * mi wifi download api
 */
public abstract class MiWiFiDownloadApi {
    protected volatile static MiWiFiDownloadApi _instance = null;

    public static MiWiFiDownloadApi instance() {
        return _instance;
    }

    /**
     * ApiLevel:23 开始一个下载
     *
     * @param uri     download uri
     * @param udn     can be @null
     * @param dirType the directory type to pass to {@link Context#getExternalFilesDir(String)}
     * @param subPath the path within the external directory. If subPath is a directory(ending with
     *                "/"), destination filename will be generated.
     * @return download id
     */
    public abstract long startDownload(Uri uri, String udn, String dirType, String subPath);

    /**
     * ApiLevel:23 pause download
     *
     * @param ids the IDs of the downloads
     */
    public abstract void pauseDownload(long... ids);

    /**
     * ApiLevel:23 resume download
     *
     * @param ids the IDs of the downloads
     */
    public abstract void resumeDownload(long... ids);

    /**
     * ApiLevel:23 restart download
     *
     * @param ids the IDs of the downloads
     */
    public abstract void restartDownload(long... ids);

    /**
     * ApiLevel:23 remove download
     *
     * @param ids the IDs of the downloads
     */
    public abstract void removeDownload(long... ids);

    /**
     * ApiLevel:23 query download
     *
     * @param onlyVisibleDownloads hide downloads exclude
     * @param filterIds            the IDs of the downloads
     */
    public abstract Cursor queryDownload(boolean onlyVisibleDownloads, long... filterIds);

    /**
     * ApiLevel:23 notify wifi download manager
     * @param isConnected wifi connected
     */
    public abstract void notifyLocalWifiConnect(boolean isConnected);
}
