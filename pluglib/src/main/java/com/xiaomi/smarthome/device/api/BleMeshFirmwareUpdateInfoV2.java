package com.xiaomi.smarthome.device.api;

import java.io.Serializable;

/**
 * 批量请求服务端最新固件更新信息所用结构
 * Api Level: 85
 */
public class BleMeshFirmwareUpdateInfoV2 implements Serializable {

    /**
     * 最新固件版本，如1.0.0_0003
     */
    public String version;

    /**
     * 已签名固件的下载链接
     */
    public String safeUrl;

    /**
     * 未签名固件的下载链接
     */
    public String url;

    /**
     * 最新固件的changeLog
     */
    public String changeLog;

    /**
     * md5
     */
    public String md5;

    /**
     * 设备当前的固件版本，如1.0.0_0002
     */
    public String currentVersion;

    /**
     * 最新固件版本的更新时间，如1539140420
     */
    public String uploadTime;

    /**
     * 同model设备对应的最新固件可能不一致
     */
    public String did;

    @Override
    public String toString() {
        return "BtFirmwareUpdateInfo{" +
                "version='" + version + '\'' +
                ", safeUrl='" + safeUrl + '\'' +
                ", url='" + url + '\'' +
                ", changeLog='" + changeLog + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                ", currentVersion='" + currentVersion + '\'' +
                ", did='" + did + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
