package com.xiaomi.smarthome.camera;

public interface IMISSListener {
    void onSuccess(String result, Object extra);

    void onFailed(int errorCode, String errorInfo);

    void onProgress(int progress);
}
