package com.xiaomi.smarthome.device.api;

public interface ICloudDataCallback<T> {

    public void onCloudDataSuccess(T result, Object extra);

    public void onCloudDataFailed(int errorCode, String errorInfo);

    public void onCloudDataProgress(int percentage);
}
