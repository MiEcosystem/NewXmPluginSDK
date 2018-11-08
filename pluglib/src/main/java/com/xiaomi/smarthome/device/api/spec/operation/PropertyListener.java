package com.xiaomi.smarthome.device.api.spec.operation;

public interface PropertyListener {
    void onSuccess(Object value);

    void onFail(int code);
}
