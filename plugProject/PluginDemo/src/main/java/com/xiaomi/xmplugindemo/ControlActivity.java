
package com.xiaomi.xmplugindemo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.BaseDevice.StateChangedListener;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.Parser;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;
import com.xiaomi.xmplugindemo.widget.MyEditText;
import com.xiaomi.xmplugindemo.widget.MyEditText.MyEditTextListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ControlActivity extends XmPluginBaseActivity implements StateChangedListener {
    private EditText mFocusHolder;
    private MyEditText mREdit;
    private MyEditText mGEdit;
    private MyEditText mBEdit;
    private View mColorBoard;
    private Button mSend;
    private DemoDevice mDevice;
    private OnEditorActionListener mEditorListener = new OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.post(new Runnable() {

                    @Override
                    public void run() {
                        clearAllFocus();
                        setRGB();
                    }
                });
            }
            return false;
        }
    };
    private MyEditTextListener mEditListener = new MyEditTextListener() {

        @Override
        public void onImeBack(View view, String text) {
            view.post(new Runnable() {

                @Override
                public void run() {
                    clearAllFocus();
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);

        mDevice = DemoDevice.getDevice(mDeviceStat);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.title_bar_title)).setText(getResources().getString(R.string.control_title));
        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        mFocusHolder = (EditText) findViewById(R.id.focus_holder);
        mREdit = (MyEditText) findViewById(R.id.r_edit);
        mREdit.setOnEditorActionListener(mEditorListener);
        mREdit.addListener(mEditListener);
        mREdit.setSelectAllOnFocus(true);

        mGEdit = (MyEditText) findViewById(R.id.g_edit);
        mGEdit.setOnEditorActionListener(mEditorListener);
        mGEdit.addListener(mEditListener);
        mGEdit.setSelectAllOnFocus(true);

        mBEdit = (MyEditText) findViewById(R.id.b_edit);
        mBEdit.setOnEditorActionListener(mEditorListener);
        mBEdit.addListener(mEditListener);
        mBEdit.setSelectAllOnFocus(true);

        mColorBoard = findViewById(R.id.color_board);
        mSend = (Button) findViewById(R.id.send);

        mSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clearAllFocus();
                setRGB();
            }
        });

        clearAllFocus();

        mDevice.addStateChangedListener(this);

        getRGB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDevice.removeStateChangedListener(this);
    }

    private void clearAllFocus() {
        mREdit.clearFocus();
        mGEdit.clearFocus();
        mBEdit.clearFocus();
        mFocusHolder.requestFocus();
    }

    private void setRGB() {
        int r = 0;
        try {
            r = Integer.parseInt(mREdit.getText().toString());
        } catch (Exception e) {
        }
        if (r < 0) {
            r = 0;
        } else if (255 < r) {
            r = 255;
        }

        int g = 0;
        try {
            g = Integer.parseInt(mGEdit.getText().toString());
        } catch (Exception e) {
        }
        if (g < 0) {
            g = 0;
        } else if (255 < g) {
            g = 255;
        }

        int b = 0;
        try {
            b = Integer.parseInt(mBEdit.getText().toString());
        } catch (Exception e) {
        }
        if (b < 0) {
            b = 0;
        } else if (255 < b) {
            b = 255;
        }

        final int rgb = r << 16 | g << 8 | b;

        JSONArray params = new JSONArray();
        params.put(rgb);

        XmPluginHostApi.instance().callMethod(mDevice.getDid(), "set_rgb", params,
                new Callback<Boolean>() {

                    @Override
                    public void onSuccess(Boolean result) {
                        mDevice.set_rgb(rgb,null);
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {

                    }
                }, null);
    }

    private void getRGB() {
        JSONArray params = new JSONArray();
        params.put("rgb");
        XmPluginHostApi.instance().callMethod(mDevice.getDid(), "get_prop", params,
                new Callback<Integer>() {

                    @Override
                    public void onSuccess(Integer rgb) {
//                        mDevice.set_rgb(rgb,null);
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {

                    }
                }, new Parser<Integer>() {

                    @Override
                    public Integer parse(String result) throws JSONException {
                        int rgb = 0;
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray jsonArray = jsonObj.optJSONArray("result");
                        if (jsonArray != null && jsonArray.length() > 0
                                && (jsonArray.get(0) instanceof Integer)) {
                            rgb = (Integer) jsonArray.get(0);
                        }
                        return rgb;
                    }

                });
    }

    private void refreshUI() {
        int r = mDevice.getR();
        int g = mDevice.getG();
        int b = mDevice.getB();

        int tmp = Math.max(r, g);
        int max = Math.max(tmp, b);

        if (max <= 0) {
            max = 1;
        }

        int showR = (255 * r) / max;
        int showG = (255 * g) / max;
        int showB = (255 * b) / max;

        mREdit.setText(Integer.toString(r));
        mGEdit.setText(Integer.toString(g));
        mBEdit.setText(Integer.toString(b));
        mColorBoard.setBackgroundColor(Color.argb(255, showR, showG, showB));
    }


    @Override
    public void onStateChanged(BaseDevice device) {
        refreshUI();
    }
}
