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

    public void updateValue(PropertyParam operation, boolean notice) {
        if (operation.getResultCode() == 0 && setValue(operation.getValue())) {
            if (notice && mListener != null) {
                mListener.onDataChanged(operation.getValue());
            }
        }
    }

    public void setSpecProperty(Context context, final PropertyParam propertyParam, final Callback<Object> callback) {
        if (!validateParam(propertyParam.getValue())) {
            if (callback != null) {
                callback.onFailure(-1, "set value wrong type");
            }
        }
        XmPluginHostApi.instance().setPropertyValue(context, propertyParam, new Callback<PropertyParam>() {
            @Override
            public void onSuccess(PropertyParam result) {
                if (setValue(result.getValue())) {
                    if (callback != null) {
                        callback.onSuccess(result.getValue());
                    }
                } else if (callback != null) {
                    callback.onFailure(-1, "set value failed");
                }
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                if (callback != null) {
                    callback.onFailure(error, errorInfo);
                }
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
