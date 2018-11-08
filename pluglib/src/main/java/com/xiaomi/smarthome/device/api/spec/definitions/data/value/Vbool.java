package com.xiaomi.smarthome.device.api.spec.definitions.data.value;

import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;

public final class Vbool extends DataValue {

    private boolean value;

    public Vbool() {
        value = false;
    }

    public Vbool(boolean value) {
        this.value = value;
    }

    public Vbool(int value) {
        this.value = (value == 1);
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Object getObjectValue() {
        return value;
    }

    public static Vbool valueOf(Object value) {
        if (value instanceof Boolean) {
            return new Vbool((Boolean)value);
        } else if (value instanceof String) {
            String s = (String) value;
            if ("true".equalsIgnoreCase(s)) {
                return new Vbool(true);
            } else if ("false".equalsIgnoreCase(s)) {
                return new Vbool(false);
            }
        } else if (value instanceof Integer) {
            Integer v = (Integer) value;
            if (v == 1) {
                return new Vbool(true);
            } else if (v == 0) {
                return new Vbool(false);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}