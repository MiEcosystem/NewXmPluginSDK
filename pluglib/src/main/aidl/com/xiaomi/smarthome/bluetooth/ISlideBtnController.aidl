// IBleSlideBtnHandler.aidl
package com.xiaomi.smarthome.bluetooth;

// Declare any non-default types here with import statements

import com.xiaomi.smarthome.bluetooth.ISlideBtnViewer;

interface ISlideBtnController {
    void onCheckedChanged(String name, boolean isChecked);

    void attachSlideBtnViewer(ISlideBtnViewer viewer);
    void detachSlideBtnViewer();
}
