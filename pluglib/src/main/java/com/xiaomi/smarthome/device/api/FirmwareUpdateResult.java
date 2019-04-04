package com.xiaomi.smarthome.device.api;

public class FirmwareUpdateResult {
    public boolean updating;
    public String curr;
    public String latest;
    public boolean isLatest;
    public String description;
    public int ota_progress;
    public String ota_status;
    public int timeout;

    @Override
    public String toString() {
        return "updating:" + updating + " curr:" + curr + " latest:" + latest + " isLatest:" + isLatest + " description:" + description + " ota_progress:" + ota_progress + " ota_status:" + ota_status + " timeout:" + timeout;
    }
}
