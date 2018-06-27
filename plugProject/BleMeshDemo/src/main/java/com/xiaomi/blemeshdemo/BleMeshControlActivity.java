package com.xiaomi.blemeshdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangchong3@xiaomi.com on 2018/6/21
 */
public class BleMeshControlActivity extends XmPluginBaseActivity {
    private TextView mTitleView;
    Device mDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_mesh_control);

        mTitleView = ((TextView) findViewById(R.id.title_bar_title));
        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

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
                getSwithStatus();
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

    private void getSwithStatus() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("did", mDevice.getDid());
            jsonObject.put("siid", 2);
            jsonObject.put("piid", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        XmPluginHostApi.instance().getMiotSpecProp(mDevice.getModel(), jsonArray, new Callback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                Integer value = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject result = jsonArray.optJSONObject(0);
                    if (result.has("code") && result.optInt("code") == 0) {
                        value = result.optInt("value");
                    }
                }

                if (value != null) {
                    if (value == 1) {
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

    private void getBrightness() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("did", mDevice.getDid());
            jsonObject.put("siid", 2);
            jsonObject.put("piid", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        XmPluginHostApi.instance().getMiotSpecProp(mDevice.getModel(), jsonArray, new Callback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                Integer value = null;
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject result = jsonArray.optJSONObject(0);
                    if (result.has("code") && result.optInt("code") == 0) {
                        value = result.optInt("value");
                    }
                }

                if (value != null) {
                    toast("亮度：" + value);
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("did", mDevice.getDid());
            jsonObject.put("siid", 2);
            jsonObject.put("piid", 1);
            jsonObject.put("value", on ? "true" : "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        XmPluginHostApi.instance().setMiotSpecProp(mDevice.getModel(), jsonArray, new Callback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject result = jsonArray.optJSONObject(0);
                    if (result.has("code") && result.optInt("code") == 0) {
                        if (on) {
                            toast("开灯成功");
                        } else {
                            toast("关灯成功");
                        }

                        return;
                    }
                }

                if (on) {
                    toast("开灯失败");
                } else {
                    toast("关灯失败");
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("did", mDevice.getDid());
            jsonObject.put("siid", 2);
            jsonObject.put("piid", 2);
            jsonObject.put("value", brightness);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        XmPluginHostApi.instance().setMiotSpecProp(mDevice.getModel(), jsonArray, new Callback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject result = jsonArray.optJSONObject(0);
                    if (result.has("code") && result.optInt("code") == 0) {
                        toast("亮度设置成功：" + brightness);

                        return;
                    }
                }

                toast("亮度设置失败");
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
