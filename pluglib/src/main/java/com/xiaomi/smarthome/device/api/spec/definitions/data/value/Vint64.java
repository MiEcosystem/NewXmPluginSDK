package com.xiaomi.smarthome.device.api.spec.definitions.data.value;

import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;

import java.util.Objects;

public class Vint64 extends DataValue {

    private long value;

    public Vint64() {
        value = 0;
    }

    public Vint64(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean lessEquals(DataValue maxValue) {
        if (maxValue.getClass() != this.getClass()) {
            return false;
        }

        Vint64 v = (Vint64) maxValue;

        return this.value <= v.value;
    }

    @Override
    public boolean validate(DataValue minValue, DataValue maxValue) {
        if (minValue.getClass() != this.getClass() || maxValue.getClass() != this.getClass()) {
            return false;
        }

        Vint64 min = (Vint64) minValue;
        Vint64 max = (Vint64) maxValue;

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

        long minValue = ((Vint64) min).value;
        long maxValue = ((Vint64) max).value;
        long stepValue = ((Vint64) step).value;

        if (this.value < minValue || this.value > maxValue || stepValue <= 0) {
            return false;
        }

        return (this.value - minValue) % stepValue == 0;
    }

    @Override
    public Object getObjectValue() {
        return value;
    }

    public static Vint64 valueOf(Object value) {
        if (value instanceof Integer) {
            return new Vint64((Integer) value);
        } else if (value instanceof Long) {
            return new Vint64((Long) value);
        }

        if (value instanceof String) {
            try {
                return new Vint64(Long.valueOf((String) value));
            } catch (NumberFormatException e) {
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

        Vint64 v = (Vint64) o;
        return value == v.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
