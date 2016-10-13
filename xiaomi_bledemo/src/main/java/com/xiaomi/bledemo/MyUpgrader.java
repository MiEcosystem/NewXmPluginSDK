package com.xiaomi.bledemo;

import android.os.Bundle;
import android.os.RemoteException;

import com.xiaomi.smarthome.bluetooth.BleUpgrader;

/**
 * Created by liwentian on 2016/10/13.
 */

public class MyUpgrader extends BleUpgrader {

    @Override
    public String getCurrentVersion() throws RemoteException {
        return null;
    }

    @Override
    public String getLatestVersion() throws RemoteException {
        return null;
    }

    @Override
    public String getUpgradeDescription() throws RemoteException {
        return null;
    }

    @Override
    public void startUpgrade() throws RemoteException {

    }

    @Override
    public void onActivityCreated(Bundle bundle) throws RemoteException {

    }
}
