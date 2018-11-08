package com.xiaomi.smarthome.device.api.spec.operation.controller;

import android.content.Context;
import android.text.TextUtils;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;
import com.xiaomi.smarthome.device.api.spec.instance.SpecAction;
import com.xiaomi.smarthome.device.api.spec.instance.SpecDevice;
import com.xiaomi.smarthome.device.api.spec.instance.SpecProperty;
import com.xiaomi.smarthome.device.api.spec.instance.SpecService;
import com.xiaomi.smarthome.device.api.spec.operation.ActionListener;
import com.xiaomi.smarthome.device.api.spec.operation.ActionParam;
import com.xiaomi.smarthome.device.api.spec.operation.PropertyListener;
import com.xiaomi.smarthome.device.api.spec.operation.PropertyParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceController extends SpecDevice {
    private String did;
    private static Map<String, DeviceController> mControllerCache = new HashMap<>();

    public DeviceController(String did, String type, String description, List<ServiceController> services) {
        super(type, description);
        this.did = did;
        for (ServiceController controller : services) {
            this.getServices().put(controller.getIid(), controller);
        }
    }

    /**
     * ApiLevel 77
     *
     * @param did
     * @return
     */
    public static DeviceController getDeviceController(String did) {
        if (mControllerCache.get(did) != null) {
            return mControllerCache.get(did);
        }
        DeviceController controller = XmPluginHostApi.instance().getSpecDeviceController(did);
        if (controller != null) {
            mControllerCache.put(did, controller);
        } else {
            controller = new DeviceController(did, "", "", new ArrayList<ServiceController>());
        }
        return controller;
    }

    /**
     * ApiLevel 77
     * 从服务器获取设备属性值
     *
     * @param context
     * @param propertyParamList
     */
    public void getSpecProperties(Context context, final List<PropertyParam> propertyParamList) {
        XmPluginHostApi.instance().getPropertyValues(context, propertyParamList, new Callback<List<PropertyParam>>() {
            @Override
            public void onSuccess(List<PropertyParam> result) {
                updateValue(result);
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                updateValue(propertyParamList);
            }
        });
    }

    /**
     * 更新数据
     *
     * @param operations
     */
    private void updateValue(List<PropertyParam> operations) {
        if (operations == null) {
            return;
        }
        for (PropertyParam operation : operations) {
            ServiceController serviceController = (ServiceController) getServices().get(operation.getSiid());
            if (serviceController != null) {
                serviceController.updateValue(operation);
            }
        }
    }

    /**
     * ApiLevel 77
     * 设置设备属性值
     *
     * @param context
     * @param propertyParam
     */
    public void setSpecProperty(Context context, final PropertyParam propertyParam) {
        if (propertyParam == null) return;
        ServiceController controller = (ServiceController) getServices().get(propertyParam.getSiid());
        if (controller != null) {
            controller.setSpecProperty(context, propertyParam);
        }
    }

    /**
     * ApiLevel 77
     * 执行action
     *
     * @param context
     * @param actionParam
     */
    public void doAction(Context context, final ActionParam actionParam) {
        if (actionParam == null) return;
        ServiceController controller = (ServiceController) getServices().get(actionParam.getSiid());
        if (controller != null) {
            controller.doAction(context, actionParam);
        }
    }

    /**
     * ApiLevel 77
     * 获取serviceController
     *
     * @param siid
     * @return
     */
    public ServiceController getServiceController(int siid) {
        return (ServiceController) getServices().get(siid);
    }

    /**
     * ApiLevel 77
     * 获取propertyController
     *
     * @param siid
     * @param piid
     * @return
     */
    public PropertyController getPropertyController(int siid, int piid) {
        SpecService service = getServiceController(siid);
        if (service != null) {
            return (PropertyController) service.getProperties().get(piid);
        }
        return null;
    }

    /**
     * ApiLevel 77
     * 获取本地保存的值
     *
     * @param siid
     * @param piid
     * @return
     */
    public Object getPropertyValue(int siid, int piid) {
        PropertyController propertyController = getPropertyController(siid, piid);
        if (propertyController != null) {
            return propertyController.getValue();
        }
        return null;
    }

    /**
     * ApiLevel 77
     * 获取actionController
     *
     * @param siid
     * @param aiid
     * @return
     */
    public ActionController getActionController(int siid, int aiid) {
        SpecService service = getServiceController(siid);
        if (service != null) {
            return (ActionController) service.getActions().get(aiid);
        }
        return null;
    }

    /**
     * ApiLevel 77
     * 获取get property请求参数对象
     *
     * @param siid
     * @param piid
     * @return
     */
    public PropertyParam newPropertyParam(int siid, int piid) {
        return newPropertyParam(siid, piid, null);
    }

    /**
     * ApiLevel 77
     * 获取set property请求参数对象
     *
     * @param siid
     * @param piid
     * @param value
     * @return
     */
    public PropertyParam newPropertyParam(int siid, int piid, Object value) {
        ServiceController serviceController = (ServiceController) getServices().get(siid);
        if (serviceController == null) {
            return null;
        }
        PropertyController propertyController = (PropertyController) serviceController.getProperties().get(piid);
        if (propertyController == null) {
            return null;
        }
        return propertyController.getParamObj(did, siid, piid, value);
    }

    /**
     * ApiLevel 77
     * 获取action请求参数对象
     *
     * @param siid
     * @param aiid
     * @param ins
     * @return
     */
    public ActionParam newActionParam(int siid, int aiid, List<Object> ins) {
        ServiceController serviceController = (ServiceController) getServices().get(siid);
        if (serviceController == null) {
            return null;
        }
        ActionController actionController = (ActionController) serviceController.getActions().get(aiid);
        if (actionController == null) {
            return null;
        }
        return actionController.getParamObj(did, siid, aiid, ins);
    }

    /**
     * ApiLevel 77
     * 订阅属性变化，每次只维持3分钟订阅事件
     */
    public void subscribeProperty(DeviceStat deviceStat, List<PropertyParam> params) {
        ArrayList<String> propList = new ArrayList<String>();
        for (PropertyParam param : params) {
            propList.add("prop." + param.getSiid() + "." + param.getPiid());
        }
        XmPluginHostApi.instance()
                .subscribeDevice(deviceStat.did, deviceStat.pid, propList, 3, null);
    }

    /**
     * ApiLevel 77
     * 取消订阅属性，在订阅后，再次订阅前取消，避免重复订阅
     */
    public void unSubscribeProperty(DeviceStat deviceStat, List<PropertyParam> params) {
        ArrayList<String> propList = new ArrayList<String>();
        for (PropertyParam param : params) {
            propList.add("prop." + param.getSiid() + "." + param.getPiid());
        }
        XmPluginHostApi.instance()
                .unsubscribeDevice(deviceStat.did, deviceStat.pid, propList, null);
    }

    /**
     * ApiLevel 77
     * 处理订阅消息
     *
     * @param data
     */
    public void onSubscribeData(String data) {
        if (TextUtils.isEmpty(data)) return;
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.optJSONObject(i);
                String key = jsonItem.optString("key");
                String[] items = key.split("\\.");
                if (items.length < 3) continue;
                PropertyController controller = getPropertyController(Integer.valueOf(items[1]), Integer.valueOf(items[2]));
                if (controller != null) {
                    JSONArray dataArray = jsonItem.optJSONArray("value");
                    if (dataArray == null || dataArray.length() == 0) continue;
                    Object object = dataArray.opt(0);
                    PropertyParam param = newPropertyParam(Integer.valueOf(items[1]), Integer.valueOf(items[2]), object);
                    param.setResultCode(0);
                    controller.updateValue(param);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * ApiLevel 77
     * 添加property监听
     *
     * @param siid
     * @param piid
     * @param listener
     */
    public void setPropertyListener(int siid, int piid, PropertyListener listener) {
        PropertyController controller = getPropertyController(siid, piid);
        if (controller != null) {
            controller.setListener(listener);
        }
    }

    /**
     * ApiLevel 77
     * 添加action监听
     *
     * @param siid
     * @param aiid
     * @param listener
     */
    public void setActionListener(int siid, int aiid, ActionListener listener) {
        ActionController controller = getActionController(siid, aiid);
        if (controller != null) {
            controller.setListener(listener);
        }
    }

    /**
     * ApiLevel 77
     * 移除监property听
     *
     * @param siid
     * @param piid
     */
    public void removePropertyListener(int siid, int piid) {
        PropertyController controller = getPropertyController(siid, piid);
        if (controller != null) {
            controller.removeListener();
        }
    }

    /**
     * ApiLevel 77
     * 移除action监听
     *
     * @param siid
     * @param aiid
     */
    public void removeActionListener(int siid, int aiid) {
        ActionController controller = getActionController(siid, aiid);
        if (controller != null) {
            controller.removeListener();
        }
    }

    /**
     * ApiLevel 77
     * 移除所有监听
     */
    public void removeAllListener() {
        Map<Integer, SpecService> specServices = getServices();
        if (specServices == null) return;
        for (SpecService specService : specServices.values()) {
            Map<Integer, SpecProperty> propertyMap = specService.getProperties();
            if (propertyMap != null) {
                for (SpecProperty specProperty : propertyMap.values()) {
                    ((PropertyController) specProperty).removeListener();
                }
            }

            Map<Integer, SpecAction> actionMap = specService.getActions();
            if (actionMap != null) {
                for (SpecAction specAction : actionMap.values()) {
                    ((ActionController) specAction).removeListener();
                }
            }
        }

    }

}