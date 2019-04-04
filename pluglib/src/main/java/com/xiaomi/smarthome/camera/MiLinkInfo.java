package com.xiaomi.smarthome.camera;

public class MiLinkInfo extends P2PInfo {
    /**
     * MiLink 连接中的Key
     */
    public String mMiLinkKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDH6Tvnsh5NM6WBzRqkHS5pyijmQ/W5LaL41CS7UGFNZlsl7/dke9Rt8tErcjzydbQ+fbXMD8dw36yIV64Q7CSkWr/qmy69/wBuijWWX4evFe557y5xm8GjhAPu4Yjz8TidqbI2H2EzSEjFltmSx2gpxEts//ifjLcMKhR43HSIKwIDAQAB";
    /**
     * MiLink 连接中的UID
     */
    public String mMiLinkId = "android_test_guid1";
    /**
     * MiLink 连接中的设备UID 为了发送消息
     */
    public String mMiLinkRemoteId = "android_test_guid2";
    /**
     * MiLink 连接中的设备pid
     */
    public String mMiLinkPid = "1";
    /**
     * MiLink 连接中的设备token
     */
    public String mMiLinkToken = "V5+3MEnozKKNppWIu4lmPWOKvHKupP1ILFTukfDDx74=";
    /**
     * MiLink 连接中的设备房间ID
     */
    public long mMiLinkRomId = 429496729600000005L;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MiLinkInfo) {
            MiLinkInfo info = (MiLinkInfo) obj;
            return this.mMiLinkId.equals(info.mMiLinkId) && this.mRemoteKey.equals(info.mRemoteKey);
        }
        return false;
    }

    @Override
    public String getUid() {
        return mMiLinkId;
    }

    @Override
    public String getKey() {
        return mMiLinkKey;
    }
}
