package com.xiaomi.smarthome.device.api.spec.definitions;

import com.xiaomi.smarthome.device.api.spec.definitions.data.ConstraintValue;
import com.xiaomi.smarthome.device.api.spec.definitions.data.DataFormat;
import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;

public class PropertyDefinition {

    private String type;
    private String description;
    private DataFormat format = DataFormat.UNKNOWN;
    private ConstraintValue constraintValue;

    public PropertyDefinition() {
    }

    public PropertyDefinition(String type, String description, DataFormat dataFormat, ConstraintValue constraintValue){
        this.type=type;
        this.description=description;
        this.format=dataFormat;
        this.constraintValue=constraintValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataFormat getFormat() {
        return format;
    }

    public void setFormat(DataFormat format) {
        this.format = format;
    }

    public ConstraintValue getConstraintValue() {
        return constraintValue;
    }

    public void setConstraintValue(ConstraintValue constraintValue) {
        this.constraintValue = constraintValue;
    }

    public boolean validate(DataValue value) {
        if (value == null) {
            return false;
        }

        if (!this.format.getJavaClass().isInstance(value)) {
            return false;
        }

        return constraintValue == null || constraintValue.validate(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        PropertyDefinition other = (PropertyDefinition) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }

        } else if (!type.equals(other.type)) {
            return false;
        }

        return true;
    }
}