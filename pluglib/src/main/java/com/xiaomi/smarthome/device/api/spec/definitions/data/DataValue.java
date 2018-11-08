package com.xiaomi.smarthome.device.api.spec.definitions.data;

public abstract class DataValue {

    public boolean lessEquals(DataValue maxValue) {
        throw new RuntimeException("not implemented!");
    }

    public boolean validate(DataValue min, DataValue max) {
        throw new RuntimeException("not implemented!");
    }

    public boolean validate(DataValue min, DataValue max, DataValue step) {
        throw new RuntimeException("not implemented!");
    }

    public abstract Object getObjectValue();
}