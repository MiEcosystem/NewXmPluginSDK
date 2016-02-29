package com.xiaomi.smarthome.bluetooth;

import android.os.Bundle;
import android.os.Looper;

/**
 * Created by liwentian on 2016/2/19.
 */
public abstract class BleUpgrader implements IBleUpgradeCaller {

    private IBleUpgradeCaller mUpgradeCaller;

    public final void attachUpgradeCaller(IBleUpgradeCaller caller) {
        if (caller != null) {
            mUpgradeCaller = caller;
        }
    }

    @Override
    public final void showPage(int page, Bundle data) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("showPage should be called in UI Thread!");
        }

        if (mUpgradeCaller != null) {
            mUpgradeCaller.showPage(page, data);
        }
    }

    public abstract String getCurrentVersion();
    public abstract String getLatestVersion();
    public abstract String getUpgradeDescription();

    public abstract void startUpgrade();
}
