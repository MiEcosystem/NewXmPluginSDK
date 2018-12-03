package com.xiaomi.smarthome.device.api.spec.definitions.data.value;

import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;

import java.util.Objects;

public class Vstring extends DataValue {

    private String value;

    public Vstring() {
        value = "";
    }

    public Vstring(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object getObjectValue() {
        return value;
    }

    public static Vstring valueOf(Object value) {
        if (value instanceof String) {
            return new Vstring((String) value);
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vstring vstring = (Vstring) o;
        return Objects.equals(value, vstring.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }
}
