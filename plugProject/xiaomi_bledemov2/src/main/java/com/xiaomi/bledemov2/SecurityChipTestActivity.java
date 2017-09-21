package com.xiaomi.bledemov2;

import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import java.text.SimpleDateFormat;

/**
 * Created by wangchong3@xiaomi.com on 2017/7/6.
 */

public class SecurityChipTestActivity extends XmPluginBaseActivity {
    private Device mDevice;
    private BroadcastReceiver mBluetoothReceiver;
    private TextView mTitleView;
    private TextView mDeviceStatusTextView;
    private Button mOwnConnectButton;
    private Button mShareConnectButton;
    private Button mUnLockButton;
    private Button mLockButton;
    private Button mBoltButton;
    private Button mKeyButton;
    private TextView mLockMsgTextView;
    private Handler mHandler;

    private static final int MSG_UNLOCK_TIMEOUT = 0x1001;
    private static final int MSG_LOCK_TIMEOUT = 0x1002;
    private static final int MSG_BOLT_TIMEOUT = 0x1003;

    // 开锁超时时间，可以根据实际情况自己定义
    private static final long OPERATE_TIMEOUT = 3 * 1000;
    private volatile boolean isOperating = false;

    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_chip);
        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        mTitleView = ((TextView) findViewById(R.id.title_bar_title));
        mTitleView.setText(R.string.security_chip_test);
        mDeviceStatusTextView = (TextView) findViewById(R.id.device_status);
        mOwnConnectButton = (Button) findViewById(R.id.own_connect);
        mShareConnectButton = (Button) findViewById(R.id.share_connect);
        mUnLockButton = (Button) findViewById(R.id.unlock);
        mLockButton = (Button) findViewById(R.id.lock);
        mBoltButton = (Button) findViewById(R.id.bolt);
        mKeyButton = (Button) findViewById(R.id.key_management);
        mLockMsgTextView = (TextView) findViewById(R.id.lock_msg);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退出时断开蓝牙连接
                XmBluetoothManager.getInstance().disconnect(mDevice.getMac());
                finish();
            }
        });
        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        mOwnConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
                    Toast.makeText(activity(), "设备已连接", Toast.LENGTH_SHORT).show();
                } else {
                    if (mDevice.isOwner()) {
                        XmBluetoothManager.getInstance().securityChipConnect(mDevice.getMac(), new Response.BleConnectResponse() {
                            @Override
                            public void onResponse(int code, Bundle bundle) {

                            }
                        });
                    } else {
                        Toast.makeText(activity(), "只有Owner才能调用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mShareConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
                    Toast.makeText(activity(), "设备已连接", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isSharedKeyValid = XmBluetoothManager.getInstance().isSecurityChipSharedKeyValid(mDevice.getMac());
                    if (mDevice.isOwner()) {
                        Toast.makeText(activity(), "只有被分享者才能调用", Toast.LENGTH_SHORT).show();
                    } else if (!isSharedKeyValid) {
                        Toast.makeText(activity(), "没有被分享的钥匙", Toast.LENGTH_SHORT).show();
                    } else {
                        XmBluetoothManager.getInstance().securityChipSharedDeviceConnect(mDevice.getMac(), new Response.BleConnectResponse() {
                            @Override
                            public void onResponse(int code, Bundle bundle) {

                            }
                        });
                    }
                }
            }
        });

        mUnLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUnlockOperate();
            }
        });
        mLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLockOperate();
            }
        });
        mBoltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBoltOperate();
            }
        });
        mKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), KeyManagementActivity.class.getName());
            }
        });

        if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
            mDeviceStatusTextView.setText("已连接");
        } else {
            mDeviceStatusTextView.setText("未连接");
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_UNLOCK_TIMEOUT:
                        isOperating = false;
                        updateLockMsg("开锁失败");
                        break;

                    case MSG_LOCK_TIMEOUT:
                        isOperating = false;
                        updateLockMsg("关锁失败");
                        break;

                    case MSG_BOLT_TIMEOUT:
                        isOperating = false;
                        updateLockMsg("反锁失败");
                        break;

                    default:
                        break;
                }
            }
        };

        registerBluetoothReceiver();
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
            mDeviceStatusTextView.setText("已连接");
        } else if (status == XmBluetoothManager.STATUS_DISCONNECTED) {
            mDeviceStatusTextView.setText("未连接");
        }
    }

    private void doUnlockOperate() {
        if (isOperating) {
            updateLockMsg("正在开锁操作，请等待");
            return;
        }

        if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
            mHandler.sendEmptyMessageDelayed(MSG_UNLOCK_TIMEOUT, OPERATE_TIMEOUT);
            isOperating = true;
            XmBluetoothManager.getInstance().securityChipOperate(
                    mDevice.getMac(),
                    XmBluetoothManager.SECURITY_CHIP_UNLOCK_OPERATOR,
                    new Response.BleWriteResponse() {
                        @Override
                        public void onResponse(int code, Void aVoid) {
                            isOperating = false;
                            mHandler.removeMessages(MSG_UNLOCK_TIMEOUT);
                            if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                updateLockMsg("开锁成功");
                            } else {
                                updateLockMsg("开锁失败");
                            }
                        }
                    });
        } else {
            Toast.makeText(activity(), "设备未连接，请先建立连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void doLockOperate() {
        if (isOperating) {
            updateLockMsg("正在关锁操作，请等待");
            return;
        }

        if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
            mHandler.sendEmptyMessageDelayed(MSG_LOCK_TIMEOUT, OPERATE_TIMEOUT);
            isOperating = true;
            XmBluetoothManager.getInstance().securityChipOperate(
                    mDevice.getMac(),
                    XmBluetoothManager.SECURITY_CHIP_LOCK_OPERATOR,
                    new Response.BleWriteResponse() {
                        @Override
                        public void onResponse(int code, Void aVoid) {
                            isOperating = false;
                            mHandler.removeMessages(MSG_LOCK_TIMEOUT);
                            if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                updateLockMsg("关锁成功");
                            } else {
                                updateLockMsg("关锁失败");
                            }
                        }
                    });
        } else {
            Toast.makeText(activity(), "设备未连接，请先建立连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void doBoltOperate() {
        if (isOperating) {
            updateLockMsg("正在反锁操作，请等待");
            return;
        }

        if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
            mHandler.sendEmptyMessageDelayed(MSG_BOLT_TIMEOUT, OPERATE_TIMEOUT);
            isOperating = true;
            XmBluetoothManager.getInstance().securityChipOperate(
                    mDevice.getMac(),
                    XmBluetoothManager.SECURITY_CHIP_BOLT_OPERATOR,
                    new Response.BleWriteResponse() {
                        @Override
                        public void onResponse(int code, Void aVoid) {
                            isOperating = false;
                            mHandler.removeMessages(MSG_BOLT_TIMEOUT);
                            if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                updateLockMsg("反锁成功");
                            } else {
                                updateLockMsg("反锁失败");
                            }
                        }
                    });
        } else {
            Toast.makeText(activity(), "设备未连接，请先建立连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLockMsg(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String date = sDateFormat.format(new java.util.Date());
                mLockMsgTextView.setText(mLockMsgTextView.getText().toString() + "\n" + date + " " + msg);
            }
        });
    }

}
