package com.xiaomi.smarthome.bluetooth;

import android.os.RemoteException;
import android.text.TextUtils;

/**
 * Created by liwentian on 2016/9/13.
 */
public abstract class SlideBtnController extends ISlideBtnController.Stub {

    private ISlideBtnViewer mSlideBtnViewer;

    @Override
    final public void attachSlideBtnViewer(ISlideBtnViewer viewer) throws RemoteException {
        mSlideBtnViewer = viewer;
    }

    @Override
    final public void detachSlideBtnViewer() throws RemoteException {
        mSlideBtnViewer = null;
    }

    public void setChecked(String name, boolean checked) {
        if (mSlideBtnViewer != null && !TextUtils.isEmpty(name)) {
            try {
                mSlideBtnViewer.setChecked(name, checked);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
