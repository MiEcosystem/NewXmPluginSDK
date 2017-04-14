package com.xiaomi.smarthome.bluetooth;

import com.xiaomi.smarthome.bluetooth.IBleUpgradeViewer;

interface IBleUpgradeController {
    String getCurrentVersion();
    String getLatestVersion();
    String getUpgradeDescription();
    void startUpgrade();

    void onActivityCreated(in Bundle data);
    void attachUpgradeCaller(IBleUpgradeViewer viewer);
    void detachUpgradeCaller();

    boolean onPreEnterActivity(in Bundle data);
}