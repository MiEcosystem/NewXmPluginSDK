package com.xiaomi.smarthome.device.api.spec.definitions.data.value;

import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;

import java.util.Objects;

public class Vint16 extends DataValue {

    private int value;

    public Vint16() {
        value = 0;
    }

    public Vint16(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean lessEquals(DataValue maxValue) {
        if (maxValue.getClass() != this.getClass()) {
            return false;
        }

        Vint16 v = (Vint16) maxValue;

        return this.value <= v.value;
    }

    @Override
    public boolean validate(DataValue minValue, DataValue maxValue) {
        if (minValue.getClass() != this.getClass() || maxValue.getClass() != this.getClass()) {
            return false;
        }

        Vint16 min = (Vint16) minValue;
        Vint16 max = (Vint16) maxValue;

        if (this.value < min.value || this.value > max.value) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validate(DataValue min, DataValue max, DataValue step) {
        if (min.getClass() != this.getClass() || max.getClass() != this.getClass() || step.getClass() != this.getClass()) {
            return false;
        }

        int minValue = ((Vint16) min).value;
        int maxValue = ((Vint16) max).value;
        int stepValue = ((Vint16) step).value;

        if (this.value < minValue || this.value > maxValue || stepValue <= 0) {
            return false;
        }

        return (this.value - minValue) % stepValue == 0;
    }

    @Override
    public Object getObjectValue() {
        return value;
    }

    public static Vint16 valueOf(Object value) {
        if (value instanceof Integer) {
            Integer intV = (Integer) value;

            if (intV > Short.MAX_VALUE || intV < Short.MIN_VALUE) {
                return null;
            }

            return new Vint16(intV);
        }

        if (value instanceof String) {
            try {
                return new Vint16(Short.valueOf((String) value));
            }catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vint16 v = (Vint16) o;
        return value == v.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
