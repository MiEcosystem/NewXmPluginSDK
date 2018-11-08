package com.xiaomi.smarthome.device.api.spec.operation.controller;

import android.content.Context;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;
import com.xiaomi.smarthome.device.api.spec.definitions.ActionDefinition;
import com.xiaomi.smarthome.device.api.spec.instance.SpecAction;
import com.xiaomi.smarthome.device.api.spec.operation.ActionListener;
import com.xiaomi.smarthome.device.api.spec.operation.ActionParam;

import java.util.List;

public class ActionController extends SpecAction {
    private ActionListener mListener;

    public ActionController(int iid, ActionDefinition definition) {
        super(iid, definition);
    }

    public void doAction(Context context, final ActionParam operation) {
        XmPluginHostApi.instance().doAction(context, operation, new Callback<ActionParam>() {
            @Override
            public void onSuccess(ActionParam result) {
                onActionResult(result);
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                onActionResult(operation);
            }
        });
    }

    public void onActionResult(ActionParam actionParam) {
        if (actionParam.getResultCode() != 0) {
            if (mListener != null) {
                mListener.onFail(actionParam.getResultCode());
            }
        } else {
            getActionDefinition().setOut(actionParam.getOut());
            if (mListener != null) {
                mListener.onSuccess(actionParam.getOut());
            }
        }
    }

    public ActionParam getParamObj(String did, int siid, int aiid, List<Object> ins) {
        return new ActionParam(did, siid, aiid, ins);
    }

    public void setListener(ActionListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        mListener = null;
    }
}
