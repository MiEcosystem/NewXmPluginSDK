package com.xiaomi.smarthome.camera;

import android.support.annotation.WorkerThread;

public interface IClientListener {

    /**
     * ApiLevel: 89
     * 连接出现问题,连接失败
     *
     * @param code 错误ID
     * @param info 错误信息
     */
    void onError(int code, String info);

    /**
     * ApiLevel: 89
     * 连接建立的进度
     *
     * @param progress 进度
     */
    void onProgress(int progress);

    /**
     * ApiLevel: 89
     * 通道连接已建立，准备发送接收数据的指令
     * P2P 线程

     */
    @WorkerThread
    void onConnected();

    /**
     * ApiLevel: 89
     * 连接已断开
     * P2P 线程
     */
    void onDisConnected();


    /**
     * ApiLevel: 89
     * 收到设备的命令
     * UI线程
     *
     * @param type 命令类型
     * @param data 命令数据
     */
    void onCtrlData(int type, byte[] data);

    /**
     * ApiLevel: 89
     * 收到的音频数据
     *
     * @param data 帧数据
     * @param info 帧信息
     */
    void onAudioData(byte[] data, byte[] info);

    /**
     * ApiLevel: 89
     * 收到的视频数据
     *
     * @param data 帧数据
     * @param info 帧信息
     */
    void onVideoData(byte[] data, byte[] info);

    /**
     * ApiLevel: 89
     * 连接被暂停了
     *
     * @see XmCameraP2p sendIOCtrl();
     */
    void onPause();

    /**
     * ApiLevel: 89
     * 暂停的连接被恢复了
     * *
     *
     * @see XmCameraP2p sendIOCtrl();
     */
    void onResume();

    void onRetry(int errorCode, String errorInfo, int retryCount);
}
