package com.xiaomi.smarthome.camera;

public interface XmCameraP2p {
    int P2P_TUTK=1;
    //音频格式ID
    int AUDIO_CODEC_G711 = 0x8A;
    int AUDIO_CODEC_AAC = 0x88;
    int VIDEO_CODEC_264 = 0x4E;
    int VIDEO_CODEC_HEVC = 0x50;

    //指定时间回看返回值
    int IPCAM_PLAYBACK_RESP = 0xF002;
    //云台转动返回值
    int IPCAM_DIRECTION_RESP = 0xF006;

    /**
     * ApiLevel: 89
     * 建立连接，连接结果会在IClientListener回调
     */
    void connect();

    /**
     * ApiLevel: 89
     * 重新连接
     */
    void reconnect();

    /**
     * ApiLevel: 89
     * 解除暂停的状态,这里只是修改client状态具体命令要自己发送
     *
     * @return true 解除成功，false连接出了问题需要重新连接
     */
    boolean resume();

    /**
     * ApiLevel: 89
     * 暂停传输数据,连接会保留,这里只是修改client状态具体命令要自己发送
     *
     * @param listener 只有当前的持有者才可以暂停
     */
    void pause(IClientListener listener);

    /**
     * ApiLevel: 89
     * 是否暂停状态
     *
     * @return true 已暂停
     */
    boolean isPaused();

    /**
     * ApiLevel: 89
     * 是否已建立连接通道
     *
     * @return true 已建立
     */
    boolean isConnected();


    /**
     * ApiLevel: 89
     * 断开连接
     *
     * @param keep     是否需要保活连接，True  会保护活
     *                 false 的话后台保活7秒
     * @param listener 只有当前的持有者才可以释放
     */
    void release(boolean keep, IClientListener listener);


    /**
     * ApiLevel: 89
     * 更新P2P信息
     *
     * @param info 新的P2P信息
     */
    void updateInfo(XmP2PInfo info);


    /**
     * ApiLevel: 89
     * 发送命令给设备
     *
     * @param type 命令类型
     * @param data 命令参数
     * @return 命令发送是否成功, 0成功
     */
    int sendIOCtrl(int type, byte[] data);


    /**
     * ApiLevel: 89
     * 发送命令，需要设备回复数据,主要用来获取设备数据：如时间轴，缩略图
     *
     * @param msgId    命令
     * @param resId    设备回复名令
     * @param data     数据
     * @param response 回调
     */
    void sendMsg(int msgId, int resId, byte[] data, P2pResponse response);

    /**
     * ApiLevel: 89
     * 发送音频数据
     *
     * @param data 自己数据
     * @param type 编码类型 G711 AAC
     */
    void sendAudioData(byte[] data, int type);

    /**
     * ApiLevel: 89
     * 设置连接监听,同一时间只能存在一个监听
     *
     * @param listener 回调
     */
    void setClientListener(IClientListener listener);

    /**
     * ApiLevel: 89
     * 需要在连接线程中跑的方法
     *
     * @param runnable 方法
     */
    void runInP2pThread(Runnable runnable);

    /**
     * ApiLevel: 89
     * 旋转云台,和SendMsg 分开为了清除多余的命令
     * 控制结果会通过 IClientListener onCtrlData 返回 IPCAM_DIRECTION_RESP
     *
     * @param data 控制云台的参数
     * @see IClientListener
     */
    void direction(byte[] data);

    /**
     * ApiLevel: 89
     * 设置清晰度
     *
     * @param quality 清晰度
     */
    void setQuality(int quality);

    /**
     * ApiLevel: 89
     * 开始对讲 设备无法做回音消除
     */
    void startSpeak();

    /**
     * ApiLevel: 89
     * 结束对讲 设备无法做回音消除
     */
    void stopSpeak();

    /**
     * 控制结果会通过 IClientListener onCtrlData 返回 IPCAM_PLAYBACK_RESP
     *
     * @param data 数据
     * @see IClientListener
     * 回看需要特殊调用
     */
    void playBack(byte data[]);

    /**
     * 回看倍速
     *
     * @param data 数据
     */
    void playBackSpeed(byte data[]);

}