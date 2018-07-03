package com.xiaomi.blemeshdemo;

import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.BaseDevice.StateChangedListener;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends XmPluginBaseActivity implements StateChangedListener {
    private static final int MSG_UPDATE_FIRMWARE = 1;
    private static final int MSG_UPDATE_NETWORK_ERROR = 2;
    private static final int MSG_UPDATE_CONNECT_ERROR = 3;
    private Device mDevice;
    private View mNewFirmView;
    private TextView mTitleView;
    private TextView mConnectStatusView;
    private BroadcastReceiver mBluetoothReceiver;
    private BleMeshFirmwareUpgrader mBleMeshFirmwareUpgrader;
    private BleMeshFirmwareUpgrader.NewFirmwareCallback mNewFirmwareCallback = new BleMeshFirmwareUpgrader.NewFirmwareCallback() {
        @Override
        public void onCallback(boolean hasNewFirmware) {
            Message.obtain(mHandler, MSG_UPDATE_FIRMWARE, hasNewFirmware).sendToTarget();
        }

        @Override
        public void onNetworkError() {
            Message.obtain(mHandler, MSG_UPDATE_NETWORK_ERROR, null).sendToTarget();
        }

        @Override
        public void onConnectError() {
            Message.obtain(mHandler, MSG_UPDATE_CONNECT_ERROR, null).sendToTarget();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        initView();

        registerBluetoothReceiver();

        mBleMeshFirmwareUpgrader = new BleMeshFirmwareUpgrader(mDevice.getMac(), mDevice.getModel(), mDevice.getDid(), mNewFirmwareCallback);

        if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
            mConnectStatusView.setText("已连接");
            mBleMeshFirmwareUpgrader.getDeviceFirmwareVersion(null);
        } else {
            mConnectStatusView.setText("未连接");
            connectDevice();
        }
    }

    private void initView() {
        TextView version = (TextView) findViewById(R.id.version);
        version.setText("version: " + mPluginPackage.packageVersion);

        mNewFirmView = findViewById(R.id.title_bar_redpoint);
        mTitleView = ((TextView) findViewById(R.id.title_bar_title));

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
                List<IXmPluginHostActivity.MenuItemBase> menus = new ArrayList<>();
                menus.add(IXmPluginHostActivity.BleMenuItem.newUpgraderItem(mBleMeshFirmwareUpgrader));
                mHostActivity.openMoreMenu2((ArrayList<IXmPluginHostActivity.MenuItemBase>) menus, true, 0, null);
            }
        });

        mConnectStatusView = (TextView) findViewById(R.id.connect_status);

        findViewById(R.id.click_connect).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDevice();
            }
        });

        findViewById(R.id.mesh_firmware_upgrade_local).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), BleMeshLocalUpgradeActivity.class.getName());
            }
        });

        findViewById(R.id.mesh_control).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), BleMeshControlActivity.class.getName());
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

    private void connectDevice() {
        // TODO 暂时先使用connect普通连接，后续正式版需要调用bleMeshConnect安全连接
        XmBluetoothManager.getInstance().connect(mDevice.getMac(), new Response.BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle bundle) {
                if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                    Toast.makeText(activity(), "连接成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity(), "连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshUI();

        mDevice.updateDeviceStatus();

        // 监听设备数据变化
        mDevice.addStateChangedListener(this);

        // 检测是否有固件更新
        mBleMeshFirmwareUpgrader.getDeviceFirmwareVersion(null);
        mBleMeshFirmwareUpgrader.getServerFirmwareVersion(null);
    }


    @Override
    public void onPause() {
        super.onPause();

        // 取消监听
        mDevice.removeStateChangedListener(this);

    }

    @Override
    public void onBackPressed() {
        // 退出时断开蓝牙连接
        XmBluetoothManager.getInstance().disconnect(mDevice.getMac());
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBluetoothReceiver();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_FIRMWARE:
                // 刷新固件升级状态
                boolean hasNewFirmware = (Boolean) msg.obj;
                if (hasNewFirmware) {
                    mNewFirmView.setVisibility(View.VISIBLE);
                } else {
                    mNewFirmView.setVisibility(View.GONE);
                }
                break;
            case MSG_UPDATE_NETWORK_ERROR:
                Toast.makeText(activity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                break;
            case MSG_UPDATE_CONNECT_ERROR:
                Toast.makeText(activity(), R.string.connect_error, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStateChanged(BaseDevice device) {
        refreshUI();
    }

    private void registerBluetoothReceiver() {
        if (mBluetoothReceiver == null) {
            mBluetoothReceiver = new BluetoothChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(XmBluetoothManager.ACTION_CONNECT_STATUS_CHANGED);
            registerReceiver(mBluetoothReceiver, filter);
        }
    }

    private void unregisterBluetoothReceiver() {
        if (mBluetoothReceiver != null) {
            unregisterReceiver(mBluetoothReceiver);
            mBluetoothReceiver = null;
        }
    }

    private class BluetoothChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null
                    || !intent.hasExtra(XmBluetoothManager.KEY_DEVICE_ADDRESS)) {
                return;
            }

            String mac = intent.getStringExtra(XmBluetoothManager.KEY_DEVICE_ADDRESS);
            if (TextUtils.equals(mac, mDevice.getMac())) {
                String action = intent.getAction();
                if (TextUtils.equals(action, XmBluetoothManager.ACTION_CONNECT_STATUS_CHANGED)) {
                    processConnectStatusChanged(intent);
                }
            }
        }
    }

    private void processConnectStatusChanged(Intent intent) {
        int status = intent.getIntExtra(XmBluetoothManager.KEY_CONNECT_STATUS, 0);

        if (status == XmBluetoothManager.STATUS_CONNECTED) {
            mConnectStatusView.setText("已连接");
            mBleMeshFirmwareUpgrader.getDeviceFirmwareVersion(null);
        } else if (status == XmBluetoothManager.STATUS_DISCONNECTED) {
            mConnectStatusView.setText("未连接");
        }
    }

}

