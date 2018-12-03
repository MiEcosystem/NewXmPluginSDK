package com.xiaomi.smarthome.device.api.spec.instance;

import com.xiaomi.smarthome.device.api.spec.definitions.PropertyDefinition;
import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;

/**
 * Created by wangyh on 18-10-22.
 */

public class SpecProperty {
    private int iid;
    private PropertyDefinition propertyDefinition;
    private DataValue value;

    public SpecProperty(int iid, PropertyDefinition propertyDefinition) {
        this.iid = iid;
        this.propertyDefinition = propertyDefinition;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    public void setPropertyDefinition(PropertyDefinition propertyDefinition) {
        this.propertyDefinition = propertyDefinition;
    }

    public Object getValue() {
        if (value == null) {
            return null;
        }
        return value.getObjectValue();
    }

    public boolean setValue(Object newValue) {
        DataValue dataValue = propertyDefinition.getFormat().createValue(newValue);
        if (dataValue == null) return false;
        if (!propertyDefinition.validate(dataValue)) {
            return false;
        }
        value = dataValue;
        return true;
    }
}
