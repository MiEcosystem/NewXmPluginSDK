package com.xiaomi.bledemo;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.SecurityKeyInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wangchong3@xiaomi.com on 2017/7/14.
 */

public class KeyManagementActivity extends XmPluginBaseActivity implements View.OnClickListener {
    private Device mDevice;
    private Button mShareButton;
    private Button mUpdateButton;
    private Button mDeleteButton;
    private Button mGetButton;
    private Button mShareInfoButton;
    private Button mClearButton;
    private TextView mContentTextView;
    private Handler mHandler;
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private List<SecurityKeyInfo> mSecurityKeyInfos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_management);
        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        mShareButton = (Button) findViewById(R.id.share_key);
        mUpdateButton = (Button) findViewById(R.id.update_key);
        mDeleteButton = (Button) findViewById(R.id.delete_key);
        mGetButton = (Button) findViewById(R.id.get_key);
        mShareInfoButton = (Button) findViewById(R.id.get_share_info);
        mClearButton = (Button) findViewById(R.id.clear);
        mContentTextView = (TextView) findViewById(R.id.content);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        mHandler = new Handler();

        mShareButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mGetButton.setOnClickListener(this);
        mShareInfoButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);

//        String date = sDateFormat.format(new java.util.Date());
//        Toast.makeText(activity(), date, Toast.LENGTH_LONG).show();
    }

    private void updateContent(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mContentTextView.setText(mContentTextView.getText().toString() + "\n" + msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mShareButton) {
            // TODO 测试，分享给自己
            String shareUid = mDeviceStat.userId;
            int status = 3; // 永久分享
            long activeTime = getUTCTime();
            long expireTime = activeTime + 1 * DateUtils.YEAR_IN_MILLIS;
            List<Integer> weekdays = null;
            XmPluginHostApi.instance().shareSecurityKey(mDevice.getModel(), mDevice.getDid(), shareUid,
                    status, activeTime / 1000, expireTime / 1000, weekdays, new Callback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateContent("分享钥匙成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            updateContent("分享钥匙失败, code = " + i + ", detail = " + s);
                        }
                    });
        } else if (v == mUpdateButton) {
            if (mSecurityKeyInfos != null && mSecurityKeyInfos.size() > 0) {
                final SecurityKeyInfo securityKeyInfo = mSecurityKeyInfos.get(0);
                XmPluginHostApi.instance().updateSecurityKey(mDevice.getModel(), mDevice.getDid(), securityKeyInfo.keyId, securityKeyInfo.status,
                        securityKeyInfo.activeTime, securityKeyInfo.expireTime + DateUtils.DAY_IN_MILLIS / 1000, securityKeyInfo.weekdays, new Callback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateContent(String.format("更新钥匙(keyId=%s)成功", securityKeyInfo.keyId));
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                updateContent(String.format("更新钥匙(keyId=%s)失败, code = %d, detail = %s", securityKeyInfo.keyId, i, s));
                            }
                        });
            } else {
                updateContent("更新失败，分享的钥匙列表为空，请刷新钥匙列表");
            }
        } else if (v == mDeleteButton) {
            if (mSecurityKeyInfos != null && mSecurityKeyInfos.size() > 0) {
                final String keyId = mSecurityKeyInfos.get(0).keyId;
                XmPluginHostApi.instance().deleteSecurityKey(mDevice.getModel(), mDevice.getDid(), keyId, new Callback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateContent(String.format("删除钥匙(keyId=%s)成功", keyId));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        updateContent(String.format("删除钥匙(keyId=%s)失败, code = %d, detail = %s", keyId, i, s));
                    }
                });
            } else {
                updateContent("删除失败，分享的钥匙列表为空，请刷新钥匙列表");
            }
        } else if (v == mGetButton) {
            XmPluginHostApi.instance().getSecurityKey(mDevice.getModel(), mDevice.getDid(), new Callback<List<SecurityKeyInfo>>() {
                @Override
                public void onSuccess(List<SecurityKeyInfo> securityKeyInfos) {
                    mSecurityKeyInfos = securityKeyInfos;
                    updateContent(String.format("获取到%d把钥匙：", securityKeyInfos.size()));
                    for (int i = 0; i < securityKeyInfos.size(); i++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("钥匙" + (i + 1) + ":" + "\n");
                        sb.append("keyId = " + securityKeyInfos.get(i).keyId + "\n");
                        sb.append("shareUid = " + securityKeyInfos.get(i).shareUid + "\n");
                        sb.append("status = " + securityKeyInfos.get(i).status + "\n");
                        sb.append("activeTime = " + securityKeyInfos.get(i).activeTime + "\n");
                        sb.append("expireTime = " + securityKeyInfos.get(i).expireTime + "\n");
                        sb.append("weekdays = " + securityKeyInfos.get(i).weekdays + "\n");
                        sb.append("isoutofdate = " + securityKeyInfos.get(i).isoutofdate + "\n");
                        updateContent(sb.toString());
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    updateContent("获取钥匙失败, code = " + i + ", detail = " + s);
                }
            });
        } else if (v == mShareInfoButton) {
            if (mSecurityKeyInfos != null && mSecurityKeyInfos.size() > 0) {
                final SecurityKeyInfo securityKeyInfo = mSecurityKeyInfos.get(0);
                XmPluginHostApi.instance().askSecurityShareKey(mDevice.getModel(), mDevice.getDid(), securityKeyInfo.keyId, new Callback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        updateContent(String.format("获取到的ShareInfo(keyId=%s)：%s", securityKeyInfo.keyId, s));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        updateContent(String.format("获取ShareInfo(keyId=%s)失败, code = %d, detail = %s", securityKeyInfo.keyId, i, s));
                    }
                });
            } else {
                updateContent("获取ShareInfo失败，分享的钥匙列表为空，请刷新钥匙列表");
            }
        } else if (v == mClearButton) {
            mContentTextView.setText("");
        }
    }

    public long getUTCTime() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTimeInMillis();
    }
}
