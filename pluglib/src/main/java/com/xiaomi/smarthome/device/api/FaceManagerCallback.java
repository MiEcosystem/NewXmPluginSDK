
package com.xiaomi.smarthome.device.api;

/**
 * 人脸管理接口，回调
 * 
 * @param <T>
 */
public interface FaceManagerCallback<T> {
    public static final int SUCCESS = 0;
    public static final int ERROR_UNKNOWN_ERROR = -9;
    public static final int INVALID = -9999;

    public void onSuccess(T result, T extra);

    public void onFailure(int error, String errorInfo);
}
