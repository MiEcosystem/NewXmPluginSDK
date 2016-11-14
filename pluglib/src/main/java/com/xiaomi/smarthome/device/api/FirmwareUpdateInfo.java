package com.xiaomi.smarthome.device.api;

/**
 * Created by zhaoming on 16-11-14.
 * ApiLevel:31
 */

public class FirmwareUpdateInfo {

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

    @Override
    public String toString() {
        return "BtFirmwareUpdateInfo{" +
                "version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", changeLog='" + changeLog + '\'' +
                '}';
    }
}
