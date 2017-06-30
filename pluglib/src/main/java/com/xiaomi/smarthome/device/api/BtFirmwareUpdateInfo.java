package com.xiaomi.smarthome.device.api;

/**
 * Created by liwentian on 2016/1/6.
 * Api Level: 16
 */
public class BtFirmwareUpdateInfo {

    /**
     * 最新固件版本，如1.1.39
     */
    public String version;

    /**
     * 最新固件的下载链接
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
                ", url='" + url + '\'' +
                ", changeLog='" + changeLog + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
