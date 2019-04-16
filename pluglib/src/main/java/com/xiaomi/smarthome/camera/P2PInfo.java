package com.xiaomi.smarthome.camera;

public abstract class P2PInfo {
    /**
     * 设备的Model
     */
    public String mModel = "";

    /**
     * 帧信息的大小,默认28个字节
     */
    public int mFrameInfoSize = 28;
    /**
     * 设备的签名校验
     */
    public String mRemoteSing = "";
    /**
     * 设备的公钥
     */
    public String mRemoteKey = "";
    /**
     * 自己的私钥
     */
    public byte[] mPrivateKey = new byte[32];
    /**
     * 自己的公钥
     */
    public byte[] mPublicKey = new byte[32];
    /**
     * 视频流加密的秘钥
     */
    public byte[] mShareKey = null;

    /**
     * 获取info的ID
     *
     * @return UID
     * @see TutkInfo mTutkId
     * @see MiLinkInfo mMiLinkId
     */
    public abstract String getUid();

    /**
     * 获取info的Key
     *
     * @return key
     * @see TutkInfo mTutkPwd
     * @see MiLinkInfo mMiLinkKey
     */
    public abstract String getKey();
}