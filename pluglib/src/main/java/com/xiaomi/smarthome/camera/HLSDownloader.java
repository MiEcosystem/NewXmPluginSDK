package com.xiaomi.smarthome.camera;

import android.content.Context;

public interface HLSDownloader {

    public final int ERROR_CODE_NO_FILE_EXIST = -4001;

    public void start(Context context, String m3u8Url, String mp4FilePath);

    public void cancel();

    public boolean isRunning();

    public void setInfoListener(OnInfoListenerP infoListener);

    public void removeInfoListener();

    public interface OnInfoListenerP {
        void onStart();

        void onComplete();

        void onCancelled();

        void onInfo(int code);

        void onError(int code);

        void onProgress(int progress);

        void onSize(int bytes);
    }
}
