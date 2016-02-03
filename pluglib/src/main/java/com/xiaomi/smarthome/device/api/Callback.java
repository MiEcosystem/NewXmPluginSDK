
package com.xiaomi.smarthome.device.api;

/**
 * 调用设备的方法，回调
 * 
 * @param <T>
 */
public interface Callback<T> {
    public static final int LOGIN_XIAOMI_ACCOUNT_FAIL = 1;
    public static final int LOGIN_TIME_OUT = 2;
    public static final int SUCCESS = 0;
    public static final int ERROR_PERMISSION_DENIED = -1;
    public static final int ERROR_DEVICE_OFF_LINE = -2;
    public static final int ERROR_REQUEST_TIME_OUT = -3;
    public static final int ERROR_INTERNAL_SERVER_ERROR = -4;
    public static final int ERROR_INTERNAL_DEVICE_ERROR = -5;
    public static final int ERROR_INVALID_REQUEST = -6;
    public static final int ERROR_MSG_TOO_LONG = -7;
    public static final int ERROR_MSG_FORMAT_ERROR = -8;
    public static final int ERROR_UNKNOWN_ERROR = -9;
    public static final int ERROR_NO_METHOD = -10;
    public static final int ERROR_REPEATED_REQUEST = -11;
    public static final int ERROR_FEQUENT_REQUEST = -12;
    public static final int ERROR_MAXIMAL_SHARE_REQUEST = -13;
    public static final int ERROR_SUBDEVICE_NOT_FOUND = -14;
    public static final int ERROR_SUBDEVICE_NO_METHOD = -15;
    public static final int ERROR_SUBDEVICE_ERROR = -16;
    public static final int ERROR_DECRYPT_FAIL = -30;
    public static final int ERROR_RESPONSE_JSON_FAIL = -31;
    public static final int ERROR_UNLOGIN = -32;
    public static final int ERROR_NETWORK_ERROR = -33;
    public static final int ERROR_ENCRYPT_FAIL = -34;
    public static final int ERROR_PARAM_JSON_ERROR = -35;
    public static final int ERROR_ALARM = -5001;
    
    public static final int INVALID = -9999;
    public static final int ERROR_JSON_PARSER_EXCEPTION = -10000;

    public void onSuccess(T result);

    public void onFailure(int error, String errorInfo);
}
