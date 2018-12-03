package com.xiaomi.smarthome.device.api;

import java.io.Serializable;

/**
 * Created by wangchong on 2018/6/26.
 * Api Level: 85
 */
public class BleMeshFirmwareUpdateInfo implements Serializable{

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

    @Override
    public String toString() {
        return "BtFirmwareUpdateInfo{" +
                "version='" + version + '\'' +
                ", safeUrl='" + safeUrl + '\'' +
                ", url='" + url + '\'' +
                ", changeLog='" + changeLog + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
