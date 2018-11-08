package com.xiaomi.smarthome.device.api.spec.operation;

import java.util.List;

public interface ActionListener {
    void onSuccess(List<Object> out);

    void onFail(int code);
}
