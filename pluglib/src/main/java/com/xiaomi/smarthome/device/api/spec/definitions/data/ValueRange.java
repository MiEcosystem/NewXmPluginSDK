package com.xiaomi.smarthome.device.api.spec.definitions.data;

import java.util.ArrayList;
import java.util.List;

public class ValueRange implements ConstraintValue {

    private DataFormat format;
    private DataValue minValue;
    private DataValue maxValue;
    private DataValue stepValue;
    private boolean hasStep;

    public ValueRange(DataFormat format, List<Object> list) {
        if (list.size() == 2) {
            this.init(format, list.get(0), list.get(1), null);
        }
        else if (list.size() == 3) {
            this.init(format, list.get(0), list.get(1), list.get(2));
        }
        else {
            throw new IllegalArgumentException("value list is null");
        }
    }

    public ValueRange(DataFormat format, Object min, Object max) {
        this.init(format, min, max, null);
    }

    public ValueRange(DataFormat format, Object min, Object max, Object step) {
        this.init(format, min, max, step);
    }

    private void init(DataFormat format, Object min, Object max, Object step) {
        this.format = format;
        this.minValue = format.createValue(min);
        this.maxValue = format.createValue(max);

        if (step != null) {
            this.stepValue = format.createValue(step);
            this.hasStep = true;
        }
        else {
            this.stepValue = null;
            this.hasStep = false;
        }

        if (! format.check(minValue, maxValue, stepValue)) {
            throw new IllegalArgumentException("check(min, max, step) failed, min: " + min + " max: "+ max + " step:"+ step);
        }
    }

    @Override
    public boolean validate(DataValue value) {
        return format.validate(value, minValue, maxValue, hasStep ? stepValue : null);
    }

    public DataValue minValue() {
        return minValue;
    }

    public DataValue maxValue() {
        return maxValue;
    }

    public DataValue stepValue() {
        return stepValue;
    }

    public List<Object> toList() {
        List<Object> list = new ArrayList<>();

        list.add(minValue.getObjectValue());
        list.add(maxValue.getObjectValue());

        if (hasStep) {
            list.add(stepValue.getObjectValue());
        }

        return list;
    }
}