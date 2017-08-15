package com.xiaomi.smarthome.mibtservice;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.xiaomi.smarthome.mibtservice.library.IMiBTCallback;
import com.xiaomi.smarthome.mibtservice.library.IMiServiceHandler;
import com.xiaomi.smarthome.mibtservice.library.MiBTCallback;
import com.xiaomi.smarthome.mibtservice.library.MiBTConstants;
import com.xiaomi.smarthome.mibtservice.library.MiBTGattCallback;
import com.xiaomi.smarthome.mibtservice.library.MiBTServiceClient;
import com.xiaomi.smarthome.mibtservice.library.MiServiceCallback;
import com.xiaomi.smarthome.mibtservice.library.channel.Code;
import com.xiaomi.smarthome.mibtservice.library.compat.MiBTAdvertiseCallback;
import com.xiaomi.smarthome.mibtservice.library.compat.MiBTAdvertiseData;
import com.xiaomi.smarthome.mibtservice.library.compat.MiBTAdvertiseSettings;
import com.xiaomi.smarthome.mibtservice.library.compat.MiBTGattCharacteristic;
import com.xiaomi.smarthome.mibtservice.library.compat.MiBTGattDescriptor;
import com.xiaomi.smarthome.mibtservice.library.compat.MiBTGattService;
import com.xiaomi.smarthome.mibtservice.library.utils.BluetoothLog;
import com.xiaomi.smarthome.mibtservice.library.utils.ByteUtils;
import com.xiaomi.smarthome.mibtservice.library.utils.UUIDUtils;

/**
 * Created by liwentian on 2017/3/22.
 */

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PRODUCTID = 472;

    private MiBTServiceClient mClient;

    private IMiServiceHandler mMiServiceHandler;

    private static final int NOTIFY_CYCLE = 300000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            sendNotify();
            mHandler.sendEmptyMessageDelayed(0, NOTIFY_CYCLE);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdvertising();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAdvertising();
            }
        });

        findViewById(R.id.start2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServer();
            }
        });

        findViewById(R.id.stop2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopServer();
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mClient = new MiBTServiceClient.Builder(this)
                .setGattServerCallback(mGattCallback)
                .setMiServiceCallback(mMiServiceCallback)
                .addGattService(new MiBTGattService(UUIDUtils.makeUUID(0xFEE0), MiBTGattService.SERVICE_TYPE_PRIMARY))
                .setProductId(PRODUCTID)
                .addCustomMacService(true)
                .setRelation(MiBTConstants.RELATION_WEAK)
                .build();
    }

    private void sendMessage() {
        if (mMiServiceHandler != null) {
            try {
                mMiServiceHandler.writeBlock("test info".getBytes(), new IMiBTCallback() {
                    @Override
                    public void onCallback(int code, byte[] data) throws RemoteException {
                        Log.d("MainActivity", "writeBlock code = " + code);
                    }

                    @Override
                    public IBinder asBinder() {
                        return null;
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotify() {
        mClient.notifyCharacteristicChanged(UUIDUtils.makeUUID(0xFE95),
                UUIDUtils.makeUUID(0x20), new byte[] {1});
    }

    private IMiBTCallback mBlockListener = new IMiBTCallback() {

        @Override
        public IBinder asBinder() {
            return null;
        }

        @Override
        public void onCallback(int code, byte[] data) throws RemoteException {
            Log.d("MainActivity", "mBlockListener code = " + code + ", data = " + new String(data));
        }
    };

    private IMiBTCallback mStateChangedListener = new IMiBTCallback() {
        @Override
        public void onCallback(int code, byte[] data) throws RemoteException {
            if (code == Code.STATE_BUSY) {
                Log.d("MainActivity", "device is busy");
            } else if (code == Code.STATE_IDLE) {
                Log.d("MainActivity", "device is idle");
            }
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    private void startAdvertising() {
        MiBTAdvertiseSettings settings = new MiBTAdvertiseSettings.Builder()
                .setAdvertiseMode(MiBTAdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(MiBTAdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        MiBTAdvertiseData data = new MiBTAdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .setIncludeTxPowerLevel(false)
                .setProductId(PRODUCTID)
//                .addManufacturerData(1, "ab".getBytes())
                .build();

        mClient.startAdvertising(settings, data, new MiBTAdvertiseCallback() {

            @Override
            public void onStartSuccess(MiBTAdvertiseSettings settingsInEffect) throws RemoteException {
                BluetoothLog.v(String.format("MainActivity startAdvertising onStartSuccess"));
            }

            @Override
            public void onStartFailure(int errorCode, String detail) throws RemoteException {
                BluetoothLog.v(String.format("MainActivity startAdvertising onStartFailure: %d, %s", errorCode, detail));
            }
        });
    }

    private void stopAdvertising() {
        mClient.stopAdvertising();
    }

    private void startServer() {
        mClient.startGattServer(new MiBTCallback() {
            @Override
            public void onCallback(int code, byte[] data) throws RemoteException {

            }
        });
    }

    private void stopServer() {
        if (mClient != null) {
            mClient.stopGattServer();
        }
    }

    private MiServiceCallback mMiServiceCallback = new MiServiceCallback() {

        @Override
        public void onSuccess(IMiServiceHandler handler) throws RemoteException {
            mMiServiceHandler = handler;
            if (mMiServiceHandler != null) {
                mMiServiceHandler.registerBlockListener(mBlockListener);
                mMiServiceHandler.registerStateChangedListener(mStateChangedListener);
            }
        }

        @Override
        public void onFailure(int error, String detail) throws RemoteException {
            BluetoothLog.v(String.format("MiServiceCallback onFailure"));
        }
    };

    private MiBTGattCallback mGattCallback = new MiBTGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) throws RemoteException {
            BluetoothLog.v(String.format("%s.onConnectionStateChange: status = %d, newState = %d", TAG, status, newState));

            if (newState == BluetoothGatt.STATE_CONNECTED) {
                mHandler.sendEmptyMessageDelayed(0, NOTIFY_CYCLE);
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, MiBTGattCharacteristic characteristic) throws RemoteException {
            BluetoothLog.v(String.format("%s.onCharacteristicReadRequest: characteristic = %s", TAG, characteristic.getUuid()));
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, MiBTGattCharacteristic characteristic, byte[] value) throws RemoteException {
            BluetoothLog.v(String.format("%s.onCharacteristicWriteRequest: characteristic = %s, value = %s",
                    TAG, characteristic.getUuid(), ByteUtils.byteToString(value)));
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, MiBTGattDescriptor descriptor) throws RemoteException {
            BluetoothLog.v(String.format("%s.onDescriptorReadRequest: descriptor = %s", TAG, descriptor.getUuid()));
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, MiBTGattDescriptor descriptor, byte[] value) throws RemoteException {
            BluetoothLog.v(String.format("%s.onDescriptorWriteRequest: descriptor = %s, value = %s",
                    TAG, descriptor.getUuid(), ByteUtils.byteToString(value)));
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMiServiceHandler != null) {
            try {
                mMiServiceHandler.unregisterBlockListener();
                mMiServiceHandler.unregisterStateChangedListener();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
