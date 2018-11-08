package com.xiaomi.smarthome.device.api.spec.definitions.data.value;

import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;

public class Vfloat extends DataValue {

    private float value;

    public Vfloat() {
        value = 0.0f;
    }

    public Vfloat(float value) {
        this.value = value;
    }

    public Vfloat(double value) {
        this.value = (float) value;
    }

    public Vfloat(int value) {
        this.value = (float) value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean lessEquals(DataValue maxValue) {
        if (maxValue.getClass() != this.getClass()) {
            return false;
        }

        Vfloat v = (Vfloat) maxValue;

        return this.value < v.value;
    }

    @Override
    public boolean validate(DataValue minValue, DataValue maxValue) {
        if (minValue.getClass() != this.getClass() || maxValue.getClass() != this.getClass()) {
            return false;
        }

        Vfloat min = (Vfloat) minValue;
        Vfloat max = (Vfloat) maxValue;

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
        float minValue = ((Vfloat) min).value;
        float maxValue = ((Vfloat) max).value;
        float stepValue = ((Vfloat) step).value;

        if (this.value < minValue || this.value > maxValue || stepValue <= 0) {
            return false;
        }

        long steps = (long)(((double)this.value - minValue) / stepValue);
        float precisionThreshold = Math.max(stepValue / 100, 0.00001f);

        float leftValue = minValue + steps * stepValue;
        float rightValue = leftValue + stepValue;
        if (Math.abs(leftValue - value) < precisionThreshold ||
                Math.abs(rightValue - value) < precisionThreshold) {
            return true;
        }

        return false;
    }

    @Override
    public Object getObjectValue() {
        return value;
    }

    public static Vfloat valueOf(Object value) {
        if (value instanceof Float) {
            return new Vfloat((Float) value);
        }

        if (value instanceof Double) {
            return new Vfloat((Double) value);
        }

        if (value instanceof Integer) {
            return new Vfloat((Integer) value);
        }

        if (value instanceof String) {
            try {
                return new Vfloat(Float.valueOf((String) value));
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
}
