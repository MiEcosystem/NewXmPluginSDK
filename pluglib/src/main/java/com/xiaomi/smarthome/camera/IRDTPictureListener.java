package com.xiaomi.smarthome.camera;

public interface IRDTPictureListener {
    void onPictureDataReceived(byte[] data);
}
