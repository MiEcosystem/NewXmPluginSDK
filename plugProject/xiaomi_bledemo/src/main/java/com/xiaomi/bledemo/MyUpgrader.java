package com.xiaomi.bledemo;

import android.os.Bundle;
import android.os.RemoteException;

import com.xiaomi.smarthome.bluetooth.BleUpgrader;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;

/**
 * Created by liwentian on 2016/10/13.
 */

public class MyUpgrader extends BleUpgrader {

    @Override
    public String getCurrentVersion() throws RemoteException {
        return "1.0";
    }

    @Override
    public String getLatestVersion() throws RemoteException {
        return "2.0";
    }

    @Override
    public String getUpgradeDescription() throws RemoteException {
        return "update";
    }

    @Override
    public void startUpgrade() throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putInt(XmBluetoothManager.EXTRA_UPGRADE_PROCESS, 20);
        showPage(XmBluetoothManager.PAGE_UPGRADING, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) throws RemoteException {
        showPage(XmBluetoothManager.PAGE_CURRENT_DEPRECATED, null);
    }
}
