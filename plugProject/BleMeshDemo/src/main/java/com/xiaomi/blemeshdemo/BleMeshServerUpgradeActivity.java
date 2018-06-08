package com.xiaomi.blemeshdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.device.api.BtFirmwareUpdateInfo;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangchong3@xiaomi.com on 2018/6/8
 */
public class BleMeshServerUpgradeActivity extends XmPluginBaseActivity {
    private TextView mTitleView;
    private Button mStartUpgradeBtn;
    private TextView mProgressView;
    private TextView mDeviceVersionView;
    private TextView mServerVersionView;
    private boolean isUpgrading = false;
    Device mDevice;
    private String mDeviceVersion;
    private String mServerVersion;
    private BtFirmwareUpdateInfo mBtFirmwareUpdateInfo = null;
    private Map<String, String> mDownloadFileMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_mesh_server_upgrade);

        mTitleView = ((TextView) findViewById(R.id.title_bar_title));
        mStartUpgradeBtn = (Button) findViewById(R.id.start_upgrade);
        mProgressView = (TextView) findViewById(R.id.progress);
        mDeviceVersionView = (TextView) findViewById(R.id.device_version);
        mServerVersionView = (TextView) findViewById(R.id.server_version);

        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        mTitleView.setText("蓝牙Mesh设备固件升级-Server上传固件升级");
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUpgrade();
                finish();
            }
        });

        findViewById(R.id.get_device_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XmBluetoothManager.getInstance().getConnectStatus(mDevice.getMac()) == XmBluetoothManager.STATUS_CONNECTED) {
                    getDeviceFirmwareVersion();
                } else {
                    mProgressView.setText("正在连接设备...(如果一直连不上，30s后会超时失败)");
                    // TODO 暂时先使用connect普通连接，后续正式版需要调用bleMeshConnect安全连接
                    XmBluetoothManager.getInstance().connect(mDevice.getMac(), new Response.BleConnectResponse() {
                        @Override
                        public void onResponse(int code, Bundle data) {
                            if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                getDeviceFirmwareVersion();
                            } else {
                                toast("连接设备失败");
                                mProgressView.setText("连接设备失败");
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.get_server_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServerFirmwareVersion();
            }
        });


        mStartUpgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mDeviceVersion)) {
                    toast("设备固件版本号为空，请先获取设备固件版本号");
                    return;
                }

                if (TextUtils.isEmpty(mServerVersion)) {
                    toast("后台固件版本号为空，请先获取后台固件版本号");
                    return;
                }

                if (compareFirmwareVersion(mServerVersion, mDeviceVersion) <= 0) {
                    toast("后台没有最新的固件版本");
                    return;
                }

                if (isUpgrading) {
                    isUpgrading = false;
                    stopUpgrade();
                } else {
                    isUpgrading = true;
                    startDownloadFirmware();
                }

                updateUI();
            }
        });
    }

    private void getDeviceFirmwareVersion() {
        mProgressView.setText("设备连接成功，正在获取固件版本号...");
        XmBluetoothManager.getInstance().getBleMeshFirmwareVersion(mDevice.getMac(), new Response.BleReadFirmwareVersionResponse() {
            @Override
            public void onResponse(int code, String version) {
                if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                    toast("version = " + version);
                    mProgressView.setText("version = " + version);
                    mDeviceVersion = version;
                    mDeviceVersionView.setText("设备固件版本号: " + version);
                } else {
                    toast("获取固件版本号失败");
                    mProgressView.setText("获取固件版本号失败");
                }
            }
        });
    }

    private void getServerFirmwareVersion() {
        XmPluginHostApi.instance().getBluetoothFirmwareUpdateInfo(mDevice.getModel(), new Callback<BtFirmwareUpdateInfo>() {
            @Override
            public void onSuccess(BtFirmwareUpdateInfo btFirmwareUpdateInfo) {
                if (btFirmwareUpdateInfo != null) {
                    mServerVersion = btFirmwareUpdateInfo.version;
                    mBtFirmwareUpdateInfo = btFirmwareUpdateInfo;
                    mServerVersionView.setText("后台最新固件版本号: " + mServerVersion);
                }

                mProgressView.setText("后台最新固件信息: " + btFirmwareUpdateInfo.toString());
            }

            @Override
            public void onFailure(int code, String result) {
                mProgressView.setText("获取后台固件版本号失败，code = " + code + ", result = " + result);
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

    private void startDownloadFirmware() {
        String filePath = mDownloadFileMap.get(mServerVersion);
        if (!TextUtils.isEmpty(filePath)) {
            startUpgrade(filePath);
            return;
        }

        mProgressView.setText("开始下载固件");
        XmPluginHostApi.instance().downloadFirmware(mBtFirmwareUpdateInfo.url, new Response.FirmwareUpgradeResponse() {
            @Override
            public void onProgress(int progress) {
                mProgressView.setText("已下载固件: " + progress + "%");
            }

            @Override
            public void onResponse(int code, String filePath, String md5) {
                if (code == 0) {
                    mProgressView.setText("下载固件成功");
                    mDownloadFileMap.put(mServerVersion, filePath);
                    startUpgrade(filePath);
                } else {
                    mProgressView.setText("下载固件失败");
                }
            }
        });
    }

    private void startUpgrade(final String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            toast("要升级的固件文件不存在");
            mDownloadFileMap.remove(mServerVersion);
            isUpgrading = false;
            updateUI();
            return;
        }

        mProgressView.setText("正在连接设备...(如果一直连不上，30s后会超时失败)");
        // TODO 暂时先使用connect普通连接，后续正式版需要调用bleMeshConnect安全连接
        XmBluetoothManager.getInstance().connect(mDevice.getMac(), new Response.BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle data) {
                if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                    mProgressView.setText("设备连接成功");
                    XmBluetoothManager.getInstance().startBleMeshUpgrade(mDevice.getMac(), filePath, new Response.BleUpgradeResponse() {
                        @Override
                        public void onProgress(int progress) {
                            updateProgress(progress);
                        }

                        @Override
                        public void onResponse(int code, String errorMsg) {
                            if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                                toast("固件升级成功");
                            } else {
                                toast("固件升级失败, errorCode = " + code + ", errorMsg = " + errorMsg);
                            }

                            isUpgrading = false;
                            updateUI();
                            XmBluetoothManager.getInstance().disconnect(mDevice.getMac());

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
        if (isUpgrading) {
            XmBluetoothManager.getInstance().cancelBleMeshUpgrade(mDevice.getMac());
        }
    }

    private void toast(String msg) {
        Toast.makeText(activity(), msg, Toast.LENGTH_SHORT).show();
    }

    public static int compareFirmwareVersion(String latestVersion, String currentVersion) {
        final String SPLITS = "[._]";

        if (TextUtils.isEmpty(latestVersion) && TextUtils.isEmpty(currentVersion)) {
            return 0;
        } else if (TextUtils.isEmpty(latestVersion)) {
            return -1;
        } else if (TextUtils.isEmpty(currentVersion)) {
            return 1;
        }

        String[] texts1 = latestVersion.split(SPLITS);
        String[] texts2 = currentVersion.split(SPLITS);

        int len = Math.min(texts1.length, texts2.length);

        try {
            for (int i = 0; i < len; i++) {
                int left = Integer.parseInt(texts1[i]);
                int right = Integer.parseInt(texts2[i]);

                if (left != right) {
                    return left - right;
                }
            }
        } catch (Exception e) {
            return 0;
        }

        return texts1.length - texts2.length;
    }
}
