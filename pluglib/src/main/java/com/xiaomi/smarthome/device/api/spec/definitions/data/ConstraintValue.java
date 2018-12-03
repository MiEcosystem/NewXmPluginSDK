package com.xiaomi.smarthome.device.api.spec.definitions.data;

public interface ConstraintValue {

    boolean validate(DataValue value);
}