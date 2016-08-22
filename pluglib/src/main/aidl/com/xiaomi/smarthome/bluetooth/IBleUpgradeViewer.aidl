package com.xiaomi.smarthome.bluetooth;

interface IBleUpgradeViewer {
    void showPage(int page, in Bundle data);
    void setBtnBackEnabled(boolean enabled);
}