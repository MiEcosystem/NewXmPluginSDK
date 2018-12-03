package com.xiaomi.blemeshdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.spec.operation.PropertyParam;
import com.xiaomi.smarthome.device.api.spec.operation.controller.DeviceController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchong3@xiaomi.com on 2018/6/21
 */
public class BleMeshControlActivity extends XmPluginBaseActivity {
    private final static int LIGHT_SERVICE_IID = 2;
    private final static int SWITCH_STATUS_PROPERTY_IID = 1;
    private final static int BRIGHTNESS_PROPERTY_IID = 2;
    private TextView mTitleView;
    private Device mDevice;
    private DeviceController mDeviceController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_mesh_control);

        mTitleView = ((TextView) findViewById(R.id.title_bar_title));
        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        mDeviceController = DeviceController.getDeviceController(mDevice.getDid());

        mTitleView.setText("Mesh Light控制");
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.get_switch_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSwitchStatus();
            }
        });

        findViewById(R.id.get_brightness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBrightness();
            }
        });

        findViewById(R.id.turn_on_light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSwitchStatus(true);
            }
        });

        findViewById(R.id.turn_off_light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSwitchStatus(false);
            }
        });

        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setActivated(true);
        seekBar.setEnabled(true);
        seekBar.setMax(100);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setBrightness(seekBar.getProgress());
            }
        });
    }

    private void getSwitchStatus() {
        PropertyParam switchStatusParam = new PropertyParam(mDevice.getDid(), LIGHT_SERVICE_IID, SWITCH_STATUS_PROPERTY_IID);
        List<PropertyParam> paramList = new ArrayList<>();
        paramList.add(switchStatusParam);

        mDeviceController.getSpecProperties(activity(), paramList, new Callback<List<PropertyParam>>() {
            @Override
            public void onSuccess(List<PropertyParam> propertyParams) {
                Object switchStatus  = getValueFromResult(propertyParams, LIGHT_SERVICE_IID, SWITCH_STATUS_PROPERTY_IID);
                if (switchStatus instanceof Boolean) {
                    if ((Boolean) switchStatus) {
                        toast("已开灯");
                    } else {
                        toast("已关灯");
                    }
                } else {
                    toast("获取开关状态失败");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                toast("获取开关状态失败, errorCode = " + i + ", errorMsg = " + s);
            }
        });
    }

    private Object getValueFromResult(List<PropertyParam> propertyParams, int siid, int piid) {
        if (propertyParams == null || propertyParams.size() == 0) {
            return null;
        }

        Object value = null;
        for (PropertyParam propertyParam : propertyParams) {
            if (propertyParam.getSiid() == siid && propertyParam.getPiid() == piid) {
                if (propertyParam.getResultCode() == 0) {
                    value = propertyParam.getValue();
                }
                break;
            }
        }
        return value;
    }

    private void getBrightness() {
        PropertyParam switchStatusParam = new PropertyParam(mDevice.getDid(), LIGHT_SERVICE_IID, BRIGHTNESS_PROPERTY_IID);
        List<PropertyParam> paramList = new ArrayList<>();
        paramList.add(switchStatusParam);

        mDeviceController.getSpecProperties(activity(), paramList, new Callback<List<PropertyParam>>() {
            @Override
            public void onSuccess(List<PropertyParam> propertyParams) {
                Object brightness = getValueFromResult(propertyParams, LIGHT_SERVICE_IID, BRIGHTNESS_PROPERTY_IID);
                if (brightness instanceof Integer) {
                    toast("亮度：" + brightness);
                } else {
                    toast("获取亮度失败");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                toast("获取亮度失败, errorCode = " + i + ", errorMsg = " + s);
            }
        });
    }

    private void setSwitchStatus(final boolean on) {
        PropertyParam propertyParam = new PropertyParam(mDevice.getDid(), LIGHT_SERVICE_IID, SWITCH_STATUS_PROPERTY_IID, on);
        mDeviceController.setSpecProperty(activity(), propertyParam, new Callback<Object>() {
            @Override
            public void onSuccess(Object result) {
                if (on) {
                    toast("开灯成功");
                } else {
                    toast("关灯成功");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                if (on) {
                    toast("开灯失败, errorCode = " + i + ", errorMsg = " + s);
                } else {
                    toast("关灯失败, errorCode = " + i + ", errorMsg = " + s);
                }
            }
        });
    }

    private void setBrightness(final int brightness) {
        PropertyParam propertyParam = new PropertyParam(mDevice.getDid(), LIGHT_SERVICE_IID, BRIGHTNESS_PROPERTY_IID, brightness);
        mDeviceController.setSpecProperty(activity(), propertyParam, new Callback<Object>() {
            @Override
            public void onSuccess(Object result) {
                toast("亮度设置成功：" + brightness);
            }

            @Override
            public void onFailure(int i, String s) {
                toast("亮度设置失败, errorCode = " + i + ", errorMsg = " + s);
            }
        });
    }

    private void toast(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
