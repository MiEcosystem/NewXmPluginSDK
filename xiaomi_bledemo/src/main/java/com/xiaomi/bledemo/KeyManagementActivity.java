package com.xiaomi.bledemo;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.SecurityKeyInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

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
    private Button mClearButton;
    private TextView mContentTextView;
    private EditText mShareInput;
    private EditText mUpdateInput;
    private EditText mDeleteInput;
    private Handler mHandler;

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
        mClearButton = (Button) findViewById(R.id.clear);
        mContentTextView = (TextView) findViewById(R.id.content);
        mShareInput = (EditText) findViewById(R.id.shared_key_input);
        mUpdateInput = (EditText) findViewById(R.id.update_key_input);
        mDeleteInput = (EditText) findViewById(R.id.delete_key_input);

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
        mClearButton.setOnClickListener(this);
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
            String shareUid = mShareInput.getEditableText().toString();
            if (!TextUtils.isEmpty(shareUid)) {
                int status = 3; // 1：暂时有效，2：周期有效，3：永久有效
                long activeTime = getUTCTime(); // 生效时间 UTC时间戳，单位为s
                long expireTime = activeTime + 1 * DateUtils.YEAR_IN_MILLIS; // 过期时间 UTC时间戳，单位为s
                List<Integer> weekdays = null; // 生效日期（星期几，例如周一和周三对应1和3，[1, 3]），仅在status=2时不可为空
                // 分享钥匙的有效时间可以配置
                shareSecurityKey(mDevice.getModel(), mDevice.getDid(), shareUid, status, activeTime / 1000, expireTime / 1000, weekdays);
            } else {
                Toast.makeText(KeyManagementActivity.this.activity(), "输入为空", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mUpdateButton) {
            String keyId = mUpdateInput.getEditableText().toString();
            if (!TextUtils.isEmpty(keyId)) {
                int status = 3; // 1：暂时有效，2：周期有效，3：永久有效
                long activeTime = getUTCTime();
                long expireTime = activeTime + 1 * DateUtils.YEAR_IN_MILLIS;
                List<Integer> weekdays = null;
                updateSecurityKey(mDevice.getModel(), mDevice.getDid(), keyId, status, activeTime / 1000, expireTime / 1000, weekdays);
            } else {
                Toast.makeText(KeyManagementActivity.this.activity(), "输入为空", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mDeleteButton) {
            String keyId = mDeleteInput.getEditableText().toString();
            if (!TextUtils.isEmpty(keyId)) {
                deleteSecurityKey(mDevice.getModel(), mDevice.getDid(), keyId);
            } else {
                Toast.makeText(KeyManagementActivity.this.activity(), "输入为空", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mGetButton) {
            getSecurityKey();
        } else if (v == mClearButton) {
            mContentTextView.setText("");
        }
    }

    public void shareSecurityKey(String model, String did, String shareUid, int status, long activeTime, long expireTime, List<Integer> weekdays) {
        XmPluginHostApi.instance().shareSecurityKey(model, did, shareUid,
                status, activeTime, expireTime, weekdays, new Callback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateContent("分享钥匙成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        updateContent("分享钥匙失败, code = " + i + ", detail = " + s);
                        if (i == Callback.INVALID) {
                            updateContent("输入的用户id不存在");
                        }
                    }
                });
    }

    public void updateSecurityKey(String model, String did, final String keyId, int status, long activeTime, long expireTime, List<Integer> weekdays) {
        XmPluginHostApi.instance().updateSecurityKey(model, did, keyId, status,
                activeTime, expireTime, weekdays, new Callback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateContent(String.format("更新钥匙(keyId=%s)成功", keyId));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        updateContent(String.format("更新钥匙(keyId=%s)失败, code = %d, detail = %s", keyId, i, s));
                    }
                });
    }

    public void deleteSecurityKey(String model, String did, final String keyId) {
        XmPluginHostApi.instance().deleteSecurityKey(model, did, keyId, new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateContent(String.format("删除钥匙(keyId=%s)成功", keyId));
            }

            @Override
            public void onFailure(int i, String s) {
                updateContent(String.format("删除钥匙(keyId=%s)失败, code = %d, detail = %s", keyId, i, s));
            }
        });
    }

    public void getSecurityKey() {
        XmPluginHostApi.instance().getSecurityKey(mDevice.getModel(), mDevice.getDid(), new Callback<List<SecurityKeyInfo>>() {
            @Override
            public void onSuccess(List<SecurityKeyInfo> securityKeyInfos) {
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
    }

    public long getUTCTime() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTimeInMillis();
    }
}
