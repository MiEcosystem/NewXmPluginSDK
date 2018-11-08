package com.xiaomi.smarthome.device.api.spec.operation.controller;

import android.content.Context;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;
import com.xiaomi.smarthome.device.api.spec.definitions.PropertyDefinition;
import com.xiaomi.smarthome.device.api.spec.definitions.data.DataValue;
import com.xiaomi.smarthome.device.api.spec.instance.SpecProperty;
import com.xiaomi.smarthome.device.api.spec.operation.PropertyListener;
import com.xiaomi.smarthome.device.api.spec.operation.PropertyParam;

public class PropertyController extends SpecProperty {
    private PropertyListener mListener;

    public PropertyController(int iid, PropertyDefinition definition) {
        super(iid, definition);
    }

    public void updateValue(PropertyParam operation) {
        if (operation.getResultCode() != 0) {
            if (mListener != null) {
                mListener.onFail(operation.getResultCode());
            }
        } else if (setValue(operation.getValue())) {
            if (mListener != null) {
                mListener.onSuccess(operation.getValue());
            }
        } else {
            if (mListener != null) {
                mListener.onFail(operation.getResultCode());
            }
        }
    }

    public void setSpecProperty(Context context, final PropertyParam propertyParam) {
        if (!validateParam(propertyParam.getValue())) {
            if (mListener != null) {
                mListener.onFail(-1);
            }
        }
        XmPluginHostApi.instance().setPropertyValue(context, propertyParam, new Callback<PropertyParam>() {
            @Override
            public void onSuccess(PropertyParam result) {
                updateValue(result);
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                updateValue(propertyParam);
            }
        });
    }

    private boolean validateParam(Object value) {
        DataValue dataValue = getPropertyDefinition().getFormat().createValue(value);
        if (dataValue == null) return false;
        if (!getPropertyDefinition().validate(dataValue)) {
            return false;
        }
        return true;
    }

    public PropertyParam getParamObj(String did, int siid, int piid, Object value) {
        return new PropertyParam(did, siid, piid, value);
    }

    public void setListener(PropertyListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        mListener = null;
    }
}
