package com.xiaomi.smarthome.bluetooth;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by liwentian on 2016/3/11.
 */
public abstract class BleUpgrader extends IBleUpgradeController.Stub {

    private IBleUpgradeViewer mBleUpgradeViewer;

    @Override
    public void attachUpgradeCaller(IBleUpgradeViewer bleUpgradeViewer) {
        mBleUpgradeViewer = bleUpgradeViewer;
    }

    public void showPage(int page, Bundle data) {
        if (mBleUpgradeViewer != null) {
            try {
                mBleUpgradeViewer.showPage(page, data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
