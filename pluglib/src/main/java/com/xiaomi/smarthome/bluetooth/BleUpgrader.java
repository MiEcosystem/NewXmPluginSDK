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

    @Override
    public void detachUpgradeCaller() throws RemoteException {
        mBleUpgradeViewer = null;
    }

    public void showPage(int page, Bundle data) {
        if (mBleUpgradeViewer != null) {
            try {
                mBleUpgradeViewer.showPage(page, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setBtnBackEnabled(boolean enabled) {
        if (mBleUpgradeViewer != null) {
            try {
                mBleUpgradeViewer.setBtnBackEnabled(enabled);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
