package com.xiaomi.smarthome.camera;

public class TutkInfo extends P2PInfo {
    /**
     * Tutk 所需要的UID
     */
    public String mTutkId = "";
    /**
     * 连接协议中的密码
     */
    public String mTutkPwd = "";
    /**
     * 连接协议中的user
     */
    public String mTutkAccount = "admin";

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TutkInfo) {
            TutkInfo tutkInfo = (TutkInfo) obj;
            return this.mTutkPwd.equals(tutkInfo.mTutkPwd) && this.mRemoteKey.equals(tutkInfo.mRemoteKey);
        }
        return false;
    }

    @Override
    public String getUid() {
        return mTutkId;
    }

    @Override
    public String getKey() {
        return mTutkPwd;
    }
}
