package com.xiaomi.blemeshdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.BaseDevice.StateChangedListener;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceUpdateInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

public class MainActivity extends XmPluginBaseActivity implements StateChangedListener {
    static final int MSG_UPDATE_FIRM = 1;

    Device mDevice;

    View mNewFirmView;

    TextView mTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText("version: " + mPluginPackage.packageVersion);

        mNewFirmView = findViewById(R.id.title_bar_redpoint);
        mTitleView = ((TextView) findViewById(R.id.title_bar_title));

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

        findViewById(R.id.mesh_firmware_upgrade_local).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), BleMeshLocalUpgradeActivity.class.getName());
            }
        });

        findViewById(R.id.mesh_firmware_upgrade_server).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), BleMeshServerUpgradeActivity.class.getName());
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
    }

    public void refreshUI() {
        mTitleView.setText(mDevice.getName());
    }

    @Override
    public void onResume() {
        super.onResume();

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
        mDevice.updateDeviceStatus();
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDevice.getName());

        // 监听设备数据变化
        mDevice.addStateChangedListener(this);
        refreshUI();
    }


    @Override
    public void onPause() {
        super.onPause();

        // 取消监听
        mDevice.removeStateChangedListener(this);

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
        refreshUI();
    }

}

