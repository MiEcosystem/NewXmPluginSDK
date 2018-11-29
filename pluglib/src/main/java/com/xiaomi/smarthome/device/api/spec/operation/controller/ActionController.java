package com.xiaomi.smarthome.device.api.spec.operation.controller;

import android.content.Context;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;
import com.xiaomi.smarthome.device.api.spec.definitions.ActionDefinition;
import com.xiaomi.smarthome.device.api.spec.instance.SpecAction;
import com.xiaomi.smarthome.device.api.spec.operation.ActionParam;

import java.util.List;

public class ActionController extends SpecAction {

    public ActionController(int iid, ActionDefinition definition) {
        super(iid, definition);
    }

    public void doAction(Context context, final ActionParam operation, final Callback<List<Object>> callback) {
        XmPluginHostApi.instance().doAction(context, operation, new Callback<ActionParam>() {
            @Override
            public void onSuccess(ActionParam result) {
                getActionDefinition().setOut(result.getOut());
                if (callback != null) {
                    callback.onSuccess(result.getOut());
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

    public ActionParam getParamObj(String did, int siid, int aiid, List<Object> ins) {
        return new ActionParam(did, siid, aiid, ins);
    }
}
