package com.xiaomi.blemeshdemo;

import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;

import com.xiaomi.smarthome.bluetooth.BleUpgrader;
import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.device.api.BleMeshFirmwareUpdateInfo;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import java.io.File;

/**
 * Created by wangchong3@xiaomi.com on 2018/6/22
 *
 * 管理固件升级
 */
public class BleMeshFirmwareUpgrader extends BleUpgrader {
    /** 当前蓝牙设备的Mac地址 */
    private String mMac;
    /** 当前设备的Model */
    private String mModel;
    /** 当前设备的Did */
    private String mDid;
    /** 设备当前的固件版本号 */
    private String mDeviceVersion;
    /** 获取最新的服务端固件版本信息 */
    private BleMeshFirmwareUpdateInfo mBleMeshUpdateInfo = null;
    /** 当前是否在固件升级页面 */
    private boolean mHasInUpgradePage = false;
    private NewFirmwareCallback mNewFirmwareCallback;

    public BleMeshFirmwareUpgrader(String mac, String model, String did, NewFirmwareCallback callback) {
        mMac = mac;
        mModel = model;
        mDid = did;
        mNewFirmwareCallback = callback;
    }

    private void notifyNewFirmwareCallback() {
        if (mNewFirmwareCallback == null) {
            return;
        }

        boolean hasNewFirmware = false;
        if (!TextUtils.isEmpty(mDeviceVersion)
                && mBleMeshUpdateInfo != null
                && !TextUtils.isEmpty(mBleMeshUpdateInfo.version)) {
            if (compareFirmwareVersion(mBleMeshUpdateInfo.version, mDeviceVersion) > 0) {
                hasNewFirmware = true;
            }
        }

        mNewFirmwareCallback.onCallback(hasNewFirmware);
    }

    public void getDeviceFirmwareVersion(final VersionCallback callback) {
        if (!TextUtils.isEmpty(mDeviceVersion)) {
            notifyNewFirmwareCallback();
            if (callback != null) {
                callback.onSuccess(mDeviceVersion);
            }
            return;
        }

        if (XmBluetoothManager.getInstance().getConnectStatus(mMac) != BluetoothProfile.STATE_CONNECTED) {
            if (callback != null) {
                callback.onFailed();
            }
            return;
        }

        XmBluetoothManager.getInstance().getBleMeshFirmwareVersion(mMac, new Response.BleReadFirmwareVersionResponse() {
            @Override
            public void onResponse(int code, String version) {
                if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                    mDeviceVersion = version;

                    getServerFirmwareVersion(null);

                    if (callback != null) {
                        callback.onSuccess(mDeviceVersion);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed();
                    }
                }
            }
        });
    }

    public void getServerFirmwareVersion(final VersionCallback callback) {
        if (mBleMeshUpdateInfo != null) {
            notifyNewFirmwareCallback();
            return;
        }

        XmPluginHostApi.instance().getBleMeshFirmwareUpdateInfo(mModel, mDid, new Callback<BleMeshFirmwareUpdateInfo>() {
            @Override
            public void onSuccess(BleMeshFirmwareUpdateInfo bleMeshFirmwareUpdateInfo) {
                if (bleMeshFirmwareUpdateInfo != null) {
                    mBleMeshUpdateInfo = bleMeshFirmwareUpdateInfo;

                    notifyNewFirmwareCallback();

                    if (callback != null) {
                        callback.onSuccess(bleMeshFirmwareUpdateInfo.version);
                    }
                }
            }

            @Override
            public void onFailure(int code, String s) {
                if (callback != null) {
                    callback.onFailed();
                }
            }
        });
    }

    @Override
    public String getCurrentVersion() throws RemoteException {
        return (mDeviceVersion == null) ? "" : mDeviceVersion;
    }

    @Override
    public String getLatestVersion() throws RemoteException {
        if (mBleMeshUpdateInfo != null) {
            return mBleMeshUpdateInfo.version;
        } else {
            return "";
        }
    }

    @Override
    public String getUpgradeDescription() throws RemoteException {
        if (mBleMeshUpdateInfo != null) {
            return mBleMeshUpdateInfo.changeLog;
        } else {
            return "";
        }
    }

    @Override
    public void startUpgrade() throws RemoteException {
        if (TextUtils.isEmpty(mDeviceVersion)
                || mBleMeshUpdateInfo == null
                || TextUtils.isEmpty(mBleMeshUpdateInfo.version)
                || TextUtils.isEmpty(mBleMeshUpdateInfo.safeUrl)) {
            showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
            return;
        }

        showPage(XmBluetoothManager.PAGE_LOADING, null);
        startDownloadFirmware();
    }

    @Override
    public void detachUpgradeCaller() throws RemoteException {
        super.detachUpgradeCaller();

        mHasInUpgradePage = false;
        cancelUpgrade();
    }

    @Override
    public void onActivityCreated(Bundle bundle) throws RemoteException {
        mHasInUpgradePage = true;

        if (!TextUtils.isEmpty(mDeviceVersion) && mBleMeshUpdateInfo != null) {
            updatePage();
            return;
        }

        showPage(XmBluetoothManager.PAGE_LOADING, null);

        if (mBleMeshUpdateInfo == null) {
            getServerFirmwareVersion(new VersionCallback() {
                @Override
                public void onSuccess(String version) {
                    updateDeviceVersion();
                }

                @Override
                public void onFailed() {
                    if (mHasInUpgradePage) {
                        showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
                        if (mNewFirmwareCallback != null) {
                            mNewFirmwareCallback.onNetworkError();
                        }
                    }
                }
            });
        } else {
            updateDeviceVersion();
        }
    }

    private void updateDeviceVersion() {
        if (XmBluetoothManager.getInstance().getConnectStatus(mMac) == BluetoothProfile.STATE_CONNECTED) {
            getDeviceFirmwareVersion(new VersionCallback() {
                @Override
                public void onSuccess(String version) {
                    updatePage();
                }

                @Override
                public void onFailed() {
                    if (mHasInUpgradePage) {
                        showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
                        if (mNewFirmwareCallback != null) {
                            mNewFirmwareCallback.onConnectError();
                        }
                    }
                }
            });
        } else {
            // TODO 暂时先使用connect普通连接，后续正式版需要调用bleMeshConnect安全连接
            XmBluetoothManager.getInstance().connect(mMac, new Response.BleConnectResponse() {
                @Override
                public void onResponse(int code, Bundle bundle) {
                    if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                        getDeviceFirmwareVersion(new VersionCallback() {
                            @Override
                            public void onSuccess(String version) {
                                updatePage();
                            }

                            @Override
                            public void onFailed() {
                                if (mHasInUpgradePage) {
                                    showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
                                    if (mNewFirmwareCallback != null) {
                                        mNewFirmwareCallback.onConnectError();
                                    }
                                }
                            }
                        });
                    } else {
                        if (mHasInUpgradePage) {
                            showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
                            if (mNewFirmwareCallback != null) {
                                mNewFirmwareCallback.onConnectError();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 获取了设备固件版本和服务端固件版本后刷新页面
     */
    private void updatePage() {
        String latestVersion = null;
        if (mBleMeshUpdateInfo != null) {
            latestVersion = mBleMeshUpdateInfo.version;
        }

        if (TextUtils.isEmpty(mDeviceVersion) || TextUtils.isEmpty(latestVersion)) {
            showPage(XmBluetoothManager.PAGE_CURRENT_LATEST, null);
            return;
        }

        if (compareFirmwareVersion(latestVersion, mDeviceVersion) > 0) {
            showPage(XmBluetoothManager.PAGE_CURRENT_DEPRECATED, null);
        } else {
            showPage(XmBluetoothManager.PAGE_CURRENT_LATEST, null);
        }
    }

    private void startDownloadFirmware() {
        XmPluginHostApi.instance().downloadFirmware(mBleMeshUpdateInfo.safeUrl, new Response.FirmwareUpgradeResponse() {
            @Override
            public void onProgress(int progress) {
            }

            @Override
            public void onResponse(int code, String filePath, String md5) {
                if (mHasInUpgradePage) {
                    if (code == 0) {
                        startConnect(filePath);
                    } else {
                        showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
                    }
                }
            }
        });
    }

    private void startConnect(final String filePath) {
        if (XmBluetoothManager.getInstance().getConnectStatus(mMac) == BluetoothProfile.STATE_CONNECTED) {
            startBleMeshUpgrade(filePath);
        } else {
            // TODO 暂时先使用connect普通连接，后续正式版需要调用bleMeshConnect安全连接
            XmBluetoothManager.getInstance().connect(mMac, new Response.BleConnectResponse() {
                @Override
                public void onResponse(int code, Bundle bundle) {
                    if (mHasInUpgradePage) {
                        if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                            startBleMeshUpgrade(filePath);
                        } else {
                            showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
                        }
                    }
                }
            });
        }
    }

    private void startBleMeshUpgrade(final String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
            return;
        }

        updateProgress(0);
        XmBluetoothManager.getInstance().startBleMeshUpgrade(mMac, filePath, new Response.BleUpgradeResponse() {
            @Override
            public void onProgress(int progress) {
                updateProgress(progress);
            }

            @Override
            public void onResponse(int code, String errorMsg) {
                if (mHasInUpgradePage) {
                    if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                        showPage(XmBluetoothManager.PAGE_UPGRADE_SUCCESS, null);
                    } else {
                        showPage(XmBluetoothManager.PAGE_UPGRADE_FAILED, null);
                    }
                }

                XmBluetoothManager.getInstance().disconnect(mMac);
            }
        });
    }

    private void updateProgress(int progress) {
        if (mHasInUpgradePage) {
            Bundle bundle = new Bundle();
            bundle.putInt(XmBluetoothManager.EXTRA_UPGRADE_PROCESS, progress);
            showPage(XmBluetoothManager.PAGE_UPGRADING, bundle);
        }
    }

    private static int compareFirmwareVersion(String latestVersion, String currentVersion) {
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

    private void cancelUpgrade() {
        if (mBleMeshUpdateInfo != null) {
            XmPluginHostApi.instance().cancelDownloadBleFirmware(mBleMeshUpdateInfo.safeUrl);
        }

        if (!TextUtils.isEmpty(mDeviceVersion)) {
            XmPluginHostApi.instance().cancelDownloadBleFirmware(mMac);
        }
    }

    /**
     * 同时获取到设备固件版本，以及服务端最新版本后，通知刷新固件升级小红点提示
     */
    public interface NewFirmwareCallback {
        void onCallback(boolean hasNewFirmware);
        void onNetworkError();
        void onConnectError();
    }

    public interface VersionCallback {
        void onSuccess(String version);
        void onFailed();
    }
}
