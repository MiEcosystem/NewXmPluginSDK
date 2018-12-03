package com.xiaomi.smarthome.device.api.spec.definitions.data;


public class ValueDefinition {

    private DataValue value;
    private String description;

    public ValueDefinition() {
    }

    public ValueDefinition(DataValue value, String description) {
        this.value = value;
        this.description = description;
    }

    public ValueDefinition(DataFormat format, Object value, String description) {
        this.description = description;
        this.value = format.createValue(value);
        if (this.value == null) {
            throw new IllegalArgumentException("invalid value: " + value + " type: " + value.getClass().getSimpleName() + " description: " + description);
        }
    }

    public DataValue value() {
        return value;
    }

    public void setValue(DataValue value) {
        this.value = value;
    }

    public String description() {
        return description;
    }

    public void description(String description) {
        this.description = description;
    }
}
