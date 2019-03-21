package com.xiaomi.blemeshdemo;

import android.app.Activity;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.xiaomi.smarthome.bluetooth.Response;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.common.plug.utils.ToastUtils;

import java.util.ArrayList;

public enum MeshManager {
    INSTANCE;

    private static final int STATUS_UNCONNECT = 0;
    private static final int STATUS_CONNECTING = 1;
    private static final int STATUS_CONNECTED = 2;
    private ArrayList<Response.BleConnectResponse> mConnectCallback = new ArrayList<>();
    private Handler handler;
    private int mStatus = STATUS_UNCONNECT;

    MeshManager() {
        handler = new Handler();
    }

    /**
     * 系统的蓝牙连接状态和当前的链接状态组合到一起
     *
     * @param mac
     * @return
     */
    private int getConnectStatus(String mac) {
        int connectStatus = XmBluetoothManager.getInstance().getConnectStatus(mac);
        int result = 0;
        if (connectStatus == BluetoothProfile.STATE_CONNECTED || connectStatus == BluetoothProfile.STATE_CONNECTING) {
            if (mStatus == STATUS_CONNECTED) {
                result = BluetoothProfile.STATE_CONNECTED;
            } else if (mStatus == STATUS_CONNECTING) {
                result = BluetoothProfile.STATE_CONNECTING;
            } else {
                XmBluetoothManager.getInstance().disconnect(mac);
                result = BluetoothProfile.STATE_DISCONNECTING;
            }
        } else if (connectStatus == BluetoothProfile.STATE_DISCONNECTING) {
            if (mStatus == STATUS_UNCONNECT) {
                result = BluetoothProfile.STATE_DISCONNECTING;
            } else {
                mStatus = STATUS_UNCONNECT;
                XmBluetoothManager.getInstance().disconnect(mac);
                result = BluetoothProfile.STATE_DISCONNECTING;
            }
        } else if (connectStatus == BluetoothProfile.STATE_DISCONNECTED) {
            if (mStatus == STATUS_UNCONNECT) {
                result = BluetoothProfile.STATE_DISCONNECTED;
            } else if (mStatus == STATUS_CONNECTING) {
                result = BluetoothProfile.STATE_CONNECTING;
            } else {
                mStatus = STATUS_UNCONNECT;
                XmBluetoothManager.getInstance().disconnect(mac);
                result = BluetoothProfile.STATE_DISCONNECTING;
            }
        }
        Log.i("MeshManager", connectStatus + "  " + mStatus + "  " + result);
        return result;
    }

    public void connectDevice(final Activity context, final String mac, final String did, final Response.BleConnectResponse response) {
        int connectStatus = getConnectStatus(mac);
        if (connectStatus == BluetoothProfile.STATE_CONNECTED) {
            ToastUtils.showToast(context, R.string.connected);
            if (response != null) {
                response.onResponse(XmBluetoothManager.Code.REQUEST_SUCCESS, new Bundle());
            }
        } else if (connectStatus == BluetoothProfile.STATE_CONNECTING) {
            mConnectCallback.add(response);
        } else if (connectStatus == BluetoothProfile.STATE_DISCONNECTING) {
            mConnectCallback.add(response);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!context.isFinishing()) {
                        connectDevice(context, mac, did, response);
                    }
                }
            }, 1000);
        } else {//STATUS_UNCONNECT
            mConnectCallback.add(response);
            mStatus = STATUS_CONNECTING;
            XmBluetoothManager.getInstance().bleMeshConnect(mac, did, new Response.BleConnectResponse() {
                @Override
                public void onResponse(int code, Bundle bundle) {
                    if (context.isFinishing()) {
                        return;
                    }
                    if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                        mStatus = STATUS_CONNECTED;
                        Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                    } else {
                        mStatus = STATUS_UNCONNECT;
                        Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                    for (Response.BleConnectResponse connectResponse : mConnectCallback) {
                        if (connectResponse != null) {
                            connectResponse.onResponse(code, bundle);
                        }
                    }
                    mConnectCallback.clear();
                }
            });
        }
    }

    public void getBleMeshFirmwareVersion(final Activity context, final String mac, final String did, final Response.BleReadFirmwareVersionResponse response) {
        if (getConnectStatus(mac) == BluetoothProfile.STATE_CONNECTED) {
            XmBluetoothManager.getInstance().getBleMeshFirmwareVersion(mac, new Response.BleReadFirmwareVersionResponse() {
                @Override
                public void onResponse(int code, String version) {
                    if (context.isFinishing()) {
                        return;
                    }
                    if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                        ToastUtils.showToast(context, "设备固件版本号: " + version);
                    } else {
                        ToastUtils.showToast(context, "获取设备固件版本号失败");
                    }
                    if (response != null) {
                        response.onResponse(code, version);
                    }
                }
            });
        } else {//STATUS_UNCONNECT
            connectDevice(context, mac, did, new Response.BleConnectResponse() {
                @Override
                public void onResponse(int code, Bundle bundle) {
                    if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                        getBleMeshFirmwareVersion(context, mac, did, response);
                    } else {
                        if (response != null) {
                            response.onResponse(code, "");
                        }
                    }
                }
            });
        }
    }

    public void startBleMeshUpgrade(final Activity context, final String mac, final String did, final String filePath, final Response.BleUpgradeResponse response) {
        if (getConnectStatus(mac) == BluetoothProfile.STATE_CONNECTED) {
            XmBluetoothManager.getInstance().startBleMeshUpgrade(mac, did, "", filePath, new Response.BleUpgradeResponse() {
                @Override
                public void onProgress(int progress) {
                    if (response != null) {
                        response.onProgress(progress);
                    }
                }

                @Override
                public void onResponse(int code, String errorMsg) {
                    if (response != null) {
                        response.onResponse(code, errorMsg);
                    }
                    XmBluetoothManager.getInstance().disconnect(mac);
                    mStatus = STATUS_UNCONNECT;
                }
            });
        } else {//STATUS_UNCONNECT
            connectDevice(context, mac, did, new Response.BleConnectResponse() {
                @Override
                public void onResponse(int code, Bundle bundle) {
                    if (code == XmBluetoothManager.Code.REQUEST_SUCCESS) {
                        startBleMeshUpgrade(context, mac, did, filePath, response);
                    } else {
                        if (response != null) {
                            response.onResponse(code, "");
                        }
                    }
                }
            });
        }
    }
}
