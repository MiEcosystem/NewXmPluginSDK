package com.xiaomi.smarthome.camera;

public interface XmP2PInfo {

    /**
     * ApiLevel: 89
     * 获取info的ID
     *
     * @return UID
     */
    public String getUid();


    /**
     * ApiLevel: 89
     * 获取info的Key
     *
     * @return key
     */
    public String getPwd();

    /**
     * ApiLevel: 89
     * 获取视频流解密的密钥
     */
    byte[] getShareKey();

    /**
     * ApiLevel: 89
     * 获取帧信息的大小，主要为了缓存帧信息
     */
    int getFrameInfoSize();

    /**
     * ApiLevel: 89
     * 设置帧信息的大小
     */
    void setFrameInfoSize(int size);

    /**
     * ApiLevel: 89
     * 设置设备的DID
     *
     * @param did
     */
    void setDid(String did);

    /**
     * ApiLevel: 89
     * 获取设备的did
     *
     * @return
     */
    String getDid();

    /**
     * ApiLevel: 89
     * 设置插件的model
     *
     * @param model
     */
    void setModel(String model);

    /**
     * ApiLevel: 89
     * 获取插件的model
     *
     * @return
     */
    String getModel();

    /**
     * ApiLevel: 89
     * 设置插件的版本号
     *
     * @param version
     */
    void setVersion(int version);

    /**
     * ApiLevel: 89
     * 获取插件的版本号
     *
     * @return
     */
    int getVersion();

    /**
     * ApiLevel: 89
     * 设置是否离线
     *
     * @param isOnLine
     */
    void setOnLine(boolean isOnLine);

    /**
     * ApiLevel: 89
     * 获取是否离线
     *
     * @return
     */
    boolean getOnLine();

    /**
     * ApiLevel: 89
     * 设置插件ID
     *
     * @param pluginId
     */
    void setPluginId(long pluginId);

    /**
     * ApiLevel: 89
     * 设置包ID
     *
     * @param packId
     */
    void setPackId(long packId);

    /**
     * ApiLevel: 89
     * 获取打点的key
     *
     * @return
     */
    String getPluginKey();

}