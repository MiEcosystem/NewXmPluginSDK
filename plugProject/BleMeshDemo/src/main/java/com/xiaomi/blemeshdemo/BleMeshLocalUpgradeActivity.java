package com.xiaomi.blemeshdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothDevice;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import java.io.File;

/**
 * Created by wangchong3@xiaomi.com on 2018/6/8
 *
 * Mesh设备固件升级
 *
 * 现在暂时只提供把手机本地的固件传输到设备，待后台上传固件后，可以直接从后台下载固件，后续要做的改进：
 * 1）从后台获取最新的固件版本信息
 * 2）从设备获取固件版本信息
 * 3）从后台调用接口下载固件到本地
 */
public class BleMeshLocalUpgradeActivity extends XmPluginBaseActivity {
    private TextView mTitleView;
    private EditText mInputMacView;
    private EditText mInputFilepathView;
    private Button mStartUpgradeBtn;
    private TextView mProgressView;
    private boolean isUpgrading = false;
    Device mDevice;
    // 估计传输完成后，扫描周围的蓝牙设备广播，等待固件升级完成
    private int mCheckTime = 0;
    private boolean mIsScanComplete = false;

    /**
     * 收到固件传输成功的请求后，等待10s再开始扫描
     */
    private Runnable mDelayScanRunnable = new Runnable() {
        @Override
        public void run() {
            final String mac = mInputMacView.getText().toString().toUpperCase();
            scanDeviceBeacon(mac);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_mesh_local_upgrade);

        mTitleView = ((TextView) findViewById(R.id.title_bar_title));
        mInputMacView = (EditText) findViewById(R.id.input_mac);
        mInputFilepathView = (EditText) findViewById(R.id.input_filepath);
        mStartUpgradeBtn = (Button) findViewById(R.id.start_upgrade);
        mProgressView = (TextView) findViewById(R.id.progress);

        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        mTitleView.setText("蓝牙Mesh设备固件升级-读取本地固件升级");
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUpgrade();
                finish();
            }
        });

        mInputMacView.setText(mDevice.getMac());
        mInputFilepathView.setText("/sdcard/ble_mesh_dfu.bin");

        findViewById(R.id.get_device_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceVersion();
            }
        });

        mStartUpgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpgrading) {
                    isUpgrading = false;
                    stopUpgrade();
                } else {
                    isUpgrading = true;
                    startUpgrade();
                }

                updateUI();
            }
        });
    }

    private void getDeviceVersion() {
        final String mac = mInputMacView.getText().toString().toUpperCase();
        if (TextUtils.isEmpty(mac)) {
            toast("输入的MAC地址为空");
            isUpgrading = false;
            updateUI();
            return;
        }

        XmBluetoothManager.getInstance().bleMeshConnect(mac, mDevice.getDid(), new Response.BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle bundle) {
                if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                    XmBluetoothManager.getInstance().getBleMeshFirmwareVersion(mac, new Response.BleReadFirmwareVersionResponse() {
                        @Override
                        public void onResponse(int code, String version) {
                            if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                toast("设备固件版本号: " + version);
                                mProgressView.setText("设备固件版本号: " + version);
                            } else {
                                toast("获取设备固件版本号失败");
                                mProgressView.setText("获取设备固件版本号失败");
                            }
                        }
                    });
                } else {
                    toast("连接设备失败");
                    mProgressView.setText("连接设备失败");
                }
            }
        });
    }

    private void updateUI() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isUpgrading) {
                    mStartUpgradeBtn.setText("取消升级");
                } else {
                    mStartUpgradeBtn.setText("开始升级");
                    mProgressView.setText("");
                }
            }
        });
    }

    private void updateProgress(final int progress) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressView.setText("已升级： " + progress + "%");
            }
        });
    }

    private void startUpgrade() {
        final String mac = mInputMacView.getText().toString().toUpperCase();
        if (TextUtils.isEmpty(mac)) {
            toast("输入的MAC地址为空");
            isUpgrading = false;
            updateUI();
            return;
        }

        final String filePath = mInputFilepathView.getText().toString();
        if (TextUtils.isEmpty(filePath)) {
            toast("输入的固件升级地址为空");
            isUpgrading = false;
            updateUI();
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            toast("要升级的固件文件不存在");
            isUpgrading = false;
            updateUI();
            return;
        }

        mProgressView.setText("正在连接设备...(如果一直连不上，30s后会超时失败)");
        XmBluetoothManager.getInstance().bleMeshConnect(mac, mDevice.getDid(), new Response.BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle data) {
                if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                    mProgressView.setText("设备连接成功");
                    XmBluetoothManager.getInstance().startBleMeshUpgrade(mac, mDevice.getDid(), "", filePath, new Response.BleUpgradeResponse() {
                        @Override
                        public void onProgress(int progress) {
                            if (progress < 100) {
                                updateProgress(progress);
                            }
                        }

                        @Override
                        public void onResponse(int code, String errorMsg) {
                            if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                // 固件传输完成后，等待升级完成后再提示用户升级完成，避免用户提前断电
                                mCheckTime = 1;
                                mIsScanComplete = false;
                                mHandler.postDelayed(mDelayScanRunnable, 10000);
                            } else {
                                toast("固件升级失败, errorCode = " + code + ", errorMsg = " + errorMsg);
                                isUpgrading = false;
                                updateUI();
                            }

                            XmBluetoothManager.getInstance().disconnect(mac);

                        }
                    });
                } else {
                    toast("连接设备失败");
                    mProgressView.setText("连接设备失败");
                    isUpgrading = false;
                    updateUI();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        stopUpgrade();
    }

    private void stopUpgrade() {
        final String mac = mInputMacView.getText().toString().toUpperCase();
        if (isUpgrading) {
            XmBluetoothManager.getInstance().cancelBleMeshUpgrade(mac);
            mIsScanComplete = true;
            mHandler.removeCallbacks(mDelayScanRunnable);
            XmBluetoothManager.getInstance().stopScan();
        }
    }

    private void toast(String msg) {
        Toast.makeText(activity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XmBluetoothManager.getInstance().stopScan();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设备升级完成后会重新发送广播，检测到广播后重连设备，重新读取设备版本号，确定是否真的升级完成了
     */
    private void scanDeviceBeacon(final String mac) {
        XmBluetoothManager.getInstance().stopScan();

        XmBluetoothManager.getInstance().startScan(8000, XmBluetoothManager.SCAN_BLE, new XmBluetoothManager.BluetoothSearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(XmBluetoothDevice xmBluetoothDevice) {
                if (TextUtils.equals(xmBluetoothDevice.device.getAddress(), mac) && !mIsScanComplete) {
                    mIsScanComplete = true;

                    XmBluetoothManager.getInstance().stopScan();

                    updateProgress(100);
                    toast("固件升级成功");
                    isUpgrading = false;
                    updateUI();
                }
            }

            @Override
            public void onSearchStopped() {
                if (!mIsScanComplete) {
                    if (mCheckTime >= 3) {
                        mIsScanComplete = true;

                        toast("固件升级失败");
                        isUpgrading = false;
                        updateUI();
                    } else {
                        mCheckTime++;
                        scanDeviceBeacon(mac);
                    }
                }
            }

            @Override
            public void onSearchCanceled() {
                if (!mIsScanComplete) {
                    if (mCheckTime >= 3) {
                        mIsScanComplete = true;

                        toast("固件升级失败");
                        isUpgrading = false;
                        updateUI();
                    } else {
                        mCheckTime++;
                        scanDeviceBeacon(mac);
                    }
                }
            }
        });
    }
}
