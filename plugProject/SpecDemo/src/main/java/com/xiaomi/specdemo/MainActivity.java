package com.xiaomi.specdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.BaseDevice.StateChangedListener;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceUpdateInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.spec.definitions.data.ValueDefinition;
import com.xiaomi.smarthome.device.api.spec.definitions.data.ValueList;
import com.xiaomi.smarthome.device.api.spec.operation.PropertyListener;
import com.xiaomi.smarthome.device.api.spec.operation.PropertyParam;
import com.xiaomi.smarthome.device.api.spec.operation.controller.DeviceController;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.xiaomi.specdemo.SpecConstant.ACTION_POST;
import static com.xiaomi.specdemo.SpecConstant.ACTION_TURN_OFF;
import static com.xiaomi.specdemo.SpecConstant.PROPERTY_INPUT_CONTROL;
import static com.xiaomi.specdemo.SpecConstant.PROPERTY_MUTE;
import static com.xiaomi.specdemo.SpecConstant.PROPERTY_REQUEST;
import static com.xiaomi.specdemo.SpecConstant.PROPERTY_RESPONSE;
import static com.xiaomi.specdemo.SpecConstant.PROPERTY_VOLUME;
import static com.xiaomi.specdemo.SpecConstant.SERVICE_MESSAGE_ROUTER;
import static com.xiaomi.specdemo.SpecConstant.SERVICE_SPEAKER;
import static com.xiaomi.specdemo.SpecConstant.SERVICE_TELEVISION;

public class MainActivity extends XmPluginBaseActivity implements StateChangedListener {
    static final int MSG_UPDATE_FIRM = 1;
    private static final int MSG_SUB_PROPERTIES = 100;
    private boolean mIsResume;

    Device mDevice;

    View mNewFirmView;

    TextView mTitleView;
    private DeviceController mDeviceController;
    private List<PropertyParam> mRequestParams = new ArrayList();
    private TextView mInputControlTv;
    private Button mTurnOffBt;
    private SeekBar mVolumeSeekBar;
    private Button mMuteBt;
    private Button mPostBt;

    /**
     * 处理订阅属性变化，每次只维持3分钟订阅事件
     */
    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> mMainActivity;

        MyHandler(MainActivity activity) {
            mMainActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mMainActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_SUB_PROPERTIES:
                        if (activity.mIsResume) {
                            activity.mDeviceController.subscribeProperty(activity.mDeviceStat, activity.mRequestParams);
                            sendEmptyMessageDelayed(MSG_SUB_PROPERTIES, 3 * 60000);
                        }
                        break;
                }
            }
        }
    }

    private PropertyListener mInputControlListener = new PropertyListener() {
        @Override
        public void onDataChanged(Object o) {
            updateControlMode(o);
        }
    };

    private PropertyListener mVolumeListener = new PropertyListener() {
        @Override
        public void onDataChanged(Object o) {
            updateVoice(o);
        }
    };

    private PropertyListener mMuteListener = new PropertyListener() {
        @Override
        public void onDataChanged(Object o) {
            updateMuteButton(o);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewFirmView = findViewById(R.id.title_bar_redpoint);
        mTitleView = ((TextView) findViewById(R.id.title_bar_title));
        mInputControlTv = (TextView) findViewById(R.id.input_control);
        mTurnOffBt = (Button) findViewById(R.id.turn_off);
        mVolumeSeekBar = (SeekBar) findViewById(R.id.volume);
        mMuteBt = (Button) findViewById(R.id.mute);
        mPostBt = (Button) findViewById(R.id.post);

        mHandler = new MyHandler(this);
        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.title_bar_more).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openMoreMenu(null, true, -1);
            }
        });

        // 打开分享
        View shareView = findViewById(R.id.title_bar_share);
        if (mDevice.isOwner()) {
            shareView.setVisibility(View.VISIBLE);
            shareView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHostActivity.openShareActivity();
                }
            });
        } else {
            shareView.setVisibility(View.GONE);
        }
        initDeviceController();
    }

    private void initDeviceController() {
        mDeviceController = DeviceController.getDeviceController(mDevice.getDid());
        mRequestParams.add(mDeviceController.newPropertyParam(SERVICE_TELEVISION, PROPERTY_INPUT_CONTROL));
        mRequestParams.add(mDeviceController.newPropertyParam(SERVICE_SPEAKER, PROPERTY_VOLUME));
        mRequestParams.add(mDeviceController.newPropertyParam(SERVICE_SPEAKER, PROPERTY_MUTE));
        mRequestParams.add(mDeviceController.newPropertyParam(SERVICE_MESSAGE_ROUTER, PROPERTY_REQUEST));
        mRequestParams.add(mDeviceController.newPropertyParam(SERVICE_MESSAGE_ROUTER, PROPERTY_RESPONSE));

        mMuteBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Object value = mDeviceController.getPropertyValue(SERVICE_SPEAKER, PROPERTY_MUTE);
                if (value != null) {
                    mMuteBt.setEnabled(false);
                    mDeviceController.setSpecProperty(activity(), mDeviceController.newPropertyParam(SERVICE_SPEAKER, PROPERTY_MUTE,
                            !(boolean) value), new Callback<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            mMuteBt.setEnabled(true);
                            updateMuteButton(o);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            mMuteBt.setEnabled(true);
                            Toast.makeText(activity(), "mute fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                seekBar.setEnabled(false);
                mDeviceController.setSpecProperty(activity(), mDeviceController.newPropertyParam(SERVICE_SPEAKER, PROPERTY_VOLUME,
                        seekBar.getProgress()), new Callback<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        seekBar.setEnabled(true);
                        updateVoice(o);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        seekBar.setEnabled(true);
                        updateVoice(mDeviceController.getPropertyValue(SERVICE_SPEAKER,PROPERTY_VOLUME));
                        Toast.makeText(activity(), "volume fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mTurnOffBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTurnOffBt.setEnabled(false);
                mDeviceController.doAction(activity(), mDeviceController.newActionParam(SERVICE_TELEVISION, ACTION_TURN_OFF,
                        null), new Callback<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        mTurnOffBt.setEnabled(true);
                        Toast.makeText(activity(), "turn off success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        mTurnOffBt.setEnabled(true);
                        Toast.makeText(activity(), "turn off fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mPostBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPostBt.setEnabled(false);
                List<Object> list = new ArrayList<>();
                list.add(mDeviceController.getPropertyValue(SERVICE_MESSAGE_ROUTER, PROPERTY_REQUEST));
                mDeviceController.doAction(activity(), mDeviceController.newActionParam(SERVICE_MESSAGE_ROUTER, ACTION_POST, list),
                        new Callback<List<Object>>() {
                            @Override
                            public void onSuccess(List<Object> objects) {
                                mPostBt.setEnabled(true);
                                Toast.makeText(activity(), "post success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                mPostBt.setEnabled(true);
                                Toast.makeText(activity(), "post fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void updateControlMode(Object value) {
        if (value instanceof Integer) {
            ValueList valueList = (ValueList) mDeviceController.getPropertyController(SERVICE_TELEVISION, PROPERTY_INPUT_CONTROL)
                    .getPropertyDefinition().getConstraintValue();
            for (ValueDefinition dataValue : valueList.values()) {
                if (value == dataValue.value().getObjectValue()) {
                    mInputControlTv.setText(dataValue.description());
                }
            }
        }
    }

    private void updateMuteButton(Object value) {
        if (value instanceof Boolean) {
            if ((boolean) value) {
                mMuteBt.setText("点击取消静音");
            } else {
                mMuteBt.setText("点击置为静音");
            }
        }
    }

    private void updateVoice(Object value) {
        if (value instanceof Integer) {
            mVolumeSeekBar.setProgress((Integer) value);
        }
    }

    private void setListener() {
        mDeviceController.setPropertyListener(SERVICE_TELEVISION, PROPERTY_INPUT_CONTROL, mInputControlListener);
        mDeviceController.setPropertyListener(SERVICE_SPEAKER, PROPERTY_VOLUME, mVolumeListener);
        mDeviceController.setPropertyListener(SERVICE_SPEAKER, PROPERTY_MUTE, mMuteListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsResume = true;
        // 检测是否有固件更新
        mDevice.checkDeviceUpdateInfo(new Callback<DeviceUpdateInfo>() {

            @Override
            public void onSuccess(DeviceUpdateInfo updateInfo) {
                Message.obtain(mHandler, MSG_UPDATE_FIRM, updateInfo).sendToTarget();
            }

            @Override
            public void onFailure(int arg0, String arg1) {

            }
        });
        setListener();
        mDeviceController.getSpecProperties(activity(), mRequestParams, new Callback<List<PropertyParam>>() {
            @Override
            public void onSuccess(List<PropertyParam> params) {
                Object inputMode = getPropertyValue(params, SERVICE_TELEVISION, PROPERTY_INPUT_CONTROL);
                if (inputMode != null) {
                    updateControlMode(inputMode);
                } else {
                    Log.e("spec plugin", "get input mode fail");
                }

                Object mute = getPropertyValue(params, SERVICE_SPEAKER, PROPERTY_MUTE);
                if (mute != null) {
                    updateMuteButton(mute);
                } else {
                    Log.e("spec plugin", "get mute status fail");
                }

                Object voice = getPropertyValue(params, SERVICE_SPEAKER, PROPERTY_VOLUME);
                if (voice != null) {
                    updateVoice(voice);
                } else {
                    Log.e("spec plugin", "get volume fail");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(activity(), "load property fail", Toast.LENGTH_SHORT).show();
            }
        });
        mHandler.sendEmptyMessage(MSG_SUB_PROPERTIES);
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDevice.getName());
    }

    private Object getPropertyValue(List<PropertyParam> params, int siid, int piid) {
        if (params == null || params.size() == 0) return null;
        for (PropertyParam param : params) {
            if (param.getSiid() == siid && param.getPiid() == piid) {
                if (param.getResultCode() == 0) {
                    return param.getValue();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public void onPause() {
        super.onPause();
        mIsResume = false;
        mDeviceController.removeAllListener();
        mHandler.removeMessages(MSG_SUB_PROPERTIES);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_FIRM:
                // 刷新固件升级状态
                DeviceUpdateInfo updateInfo = (DeviceUpdateInfo) msg.obj;
                if (updateInfo.mHasNewFirmware) {
                    mNewFirmView.setVisibility(View.VISIBLE);
                } else {
                    mNewFirmView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStateChanged(BaseDevice device) {

    }

}

