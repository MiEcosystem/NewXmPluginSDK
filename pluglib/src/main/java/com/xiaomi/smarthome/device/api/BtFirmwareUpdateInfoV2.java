package com.xiaomi.smarthome.device.api;

/**
 * Created by wangchong on 2019/2/18.
 * Api Level: 90
 */
public class BtFirmwareUpdateInfoV2 {

    /**
     * 最新固件版本，如1.0.1_0066
     */
    public String version;

    /**
     * 最新固件的下载链接(http)
     */
    public String url;

    /**
     * 最新固件的下载地址(https)
     */
    public String safeUrl;

    /**
     * 最新固件的changeLog
     */
    public String changeLog;

    /**
     * md5
     */
    public String md5;

    /**
     * 插件包上传时间
     */
    public long uploadTime;

    @Override
    public String toString() {
        return "BtFirmwareUpdateInfoV2{" +
                "version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", safeUrl='" + safeUrl + '\'' +
                ", changeLog='" + changeLog + '\'' +
                ", md5='" + md5 + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                '}';
    }
}
