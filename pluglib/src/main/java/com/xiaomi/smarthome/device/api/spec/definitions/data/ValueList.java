package com.xiaomi.smarthome.device.api.spec.definitions.data;

import java.util.List;

public class ValueList implements ConstraintValue {

    private List<ValueDefinition> values;

    public ValueList(List<ValueDefinition> values) {
        this.values = values;
    }

    @Override
    public boolean validate(DataValue value) {
        for (ValueDefinition v : values) {
            if (v.value().equals(value)) {
                return true;
            }
        }

        return false;
    }

    public List<ValueDefinition> values() {
        return values;
    }
}