package com.xiaomi.smarthome.camera;

public interface P2pResponse {

    /**
     * ApiLevel: 89     *
     * @param remainLen remainLen还剩余多少数据,为0时表示数据已经接收完成,用来传输文件
     * @param data
     */
    public void onResponse(int remainLen, byte[] data);

    /**
     * ApiLevel: 89
     * 获取数据失败
     *
     * @param error -1超时 -2摄像头没打开 -3 cancel
     */
    public void onError(int error);

}