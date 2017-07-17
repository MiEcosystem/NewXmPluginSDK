package com.xiaomi.bledemo;

import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created by wangchong3@xiaomi.com on 2017/7/6.
 */

public class SecurityChipTestActivity extends XmPluginBaseActivity {
    private Device mDevice;
    private BroadcastReceiver mBluetoothReceiver;
    private TextView mTitleView;
    private TextView mDeviceStatusTextView;
    private Button mConnectButton;
    private Button mUnLockButton;
    private Button mBoltButton;
    private TextView mLockMsgTextView;
    private Handler mHandler;

    public static final String LOCK_UUID_FORMAT = "0000%04x-0065-6C62-2E74-6F696D2E696D";
    public static UUID makeLockUUID(int value) {
        return UUID.fromString(String.format(LOCK_UUID_FORMAT, value));
    }
    static UUID UUID_LOCK_SERVICE = makeLockUUID(0x1000);
    static UUID UUID_LOCK_OPERATOR_CHARACTER = makeLockUUID(0x1001);
    static UUID UUID_LOCK_STATE_CHARACTER = makeLockUUID(0x1002);
    static byte[] LOCK_OPERATOR = new byte[] {0x00};
    static byte[] UNLOCK_OPERATOR = new byte[] {0x01};
    static byte[] BOLT_OPERATOR= new byte[] {0x02};
    static byte[] LOCK_STATE = new byte[] {0x00};
    static byte[] UNLOCK_STATE = new byte[] {0x01};
    static byte[] BOLT_STATE = new byte[] {0x02};

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
        mConnectButton = (Button) findViewById(R.id.connect);
        mUnLockButton = (Button) findViewById(R.id.unlock);
        mBoltButton = (Button) findViewById(R.id.bolt);
        mLockMsgTextView = (TextView) findViewById(R.id.lock_msg);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
                    Toast.makeText(activity(), "设备已连接", Toast.LENGTH_SHORT).show();
                } else {
                    XmBluetoothManager.getInstance().securityChipConnect(mDevice.getMac(), new Response.BleConnectResponse() {
                        @Override
                        public void onResponse(int i, Bundle bundle) {

                        }
                    });
                }
            }
        });

        mUnLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
                    sendLockMsg(UNLOCK_OPERATOR);
                } else {
                    Toast.makeText(activity(), "设备未连接，请先建立连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBoltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
                    sendLockMsg(BOLT_OPERATOR);
                } else {
                    Toast.makeText(activity(), "设备未连接，请先建立连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == BluetoothProfile.STATE_CONNECTED) {
            mDeviceStatusTextView.setText("已连接");
            XmBluetoothManager.getInstance().notify(mDevice.getMac(), UUID_LOCK_SERVICE, UUID_LOCK_STATE_CHARACTER, null);
        } else {
            mDeviceStatusTextView.setText("未连接");
        }

        mHandler = new Handler();
        registerBluetoothReceiver();
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
            filter.addAction(XmBluetoothManager.ACTION_CHARACTER_CHANGED);
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
                } else if (TextUtils.equals(action, XmBluetoothManager.ACTION_CHARACTER_CHANGED)) {
                    processCharacterChanged(intent);
                }
            }
        }
    }

    private void processConnectStatusChanged(Intent intent) {
        int status = intent.getIntExtra(XmBluetoothManager.KEY_CONNECT_STATUS, 0);

        if (status == XmBluetoothManager.STATUS_CONNECTED) {
            mDeviceStatusTextView.setText("已连接");
            XmBluetoothManager.getInstance().notify(mDevice.getMac(), UUID_LOCK_SERVICE, UUID_LOCK_STATE_CHARACTER, null);
        } else if (status == XmBluetoothManager.STATUS_DISCONNECTED) {
            mDeviceStatusTextView.setText("未连接");
        }
    }

    private void processCharacterChanged(Intent intent) {
        UUID service = (UUID) intent.getSerializableExtra(XmBluetoothManager.KEY_SERVICE_UUID);
        UUID character = (UUID) intent.getSerializableExtra(XmBluetoothManager.KEY_CHARACTER_UUID);
        byte[] value = intent.getByteArrayExtra(XmBluetoothManager.KEY_CHARACTER_VALUE);
        if (UUID_LOCK_SERVICE.equals(service) && UUID_LOCK_STATE_CHARACTER.equals(character)) {
            XmBluetoothManager.getInstance().securityChipDecrypt(mDevice.getMac(), value, new Response.BleReadResponse() {
                @Override
                public void onResponse(int i, byte[] bytes) {
                    if (i == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                        if (byteEquals(bytes, LOCK_STATE)) {
                            updateLockMsg("关锁成功");
                        } else if (byteEquals(bytes, UNLOCK_STATE)) {
                            updateLockMsg("开锁成功");
                        } else if (byteEquals(bytes, BOLT_STATE)) {
                            updateLockMsg("反锁成功");
                        }
                    } else {
                        updateLockMsg("接收到的锁数据解密失败");
                    }
                }
            });
        }
    }

    private void sendLockMsg(final byte[] operator) {
        XmBluetoothManager.getInstance().securityChipEncrypt(mDevice.getMac(), operator, new Response.BleReadResponse() {
            @Override
            public void onResponse(int i, byte[] bytes) {
                if (i == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                    XmBluetoothManager.getInstance().write(mDevice.getMac(), UUID_LOCK_SERVICE, UUID_LOCK_OPERATOR_CHARACTER, bytes, new Response.BleWriteResponse() {
                        @Override
                        public void onResponse(int i, Void aVoid) {
                            if (i != XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                if (byteEquals(operator, UNLOCK_OPERATOR)) {
                                    Toast.makeText(activity(), "发送开锁数据失败", Toast.LENGTH_SHORT).show();
                                } else if (byteEquals(operator, BOLT_OPERATOR)) {
                                    Toast.makeText(activity(), "发送反锁数据失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    if (byteEquals(operator, UNLOCK_OPERATOR)) {
                        Toast.makeText(activity(), "开锁数据加密失败", Toast.LENGTH_SHORT).show();
                    } else if (byteEquals(operator, BOLT_OPERATOR)) {
                        Toast.makeText(activity(), "反锁数据加密失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    public static boolean byteEquals(byte[] lbytes, byte[] rbytes) {
        if (lbytes == null && rbytes == null) {
            return true;
        }

        if (lbytes == null || rbytes == null) {
            return false;
        }

        int llen = lbytes.length;
        int rlen = rbytes.length;

        if (llen != rlen) {
            return false;
        }

        for (int i = 0; i < llen; i++) {
            if (lbytes[i] != rbytes[i]) {
                return false;
            }
        }

        return true;
    }
}
