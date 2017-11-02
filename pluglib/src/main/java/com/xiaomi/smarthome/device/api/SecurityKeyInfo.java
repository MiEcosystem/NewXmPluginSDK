package com.xiaomi.smarthome.device.api;

import java.util.List;

/**
 * Created by wangchong3@xiaomi.com on 2017/7/14.
 * 分享的电子钥匙信息
 */
public class SecurityKeyInfo {
    /** 钥匙的id */
    public String keyId;
    /** 分享目标uid */
    public String shareUid;
    /** 分享类别 1：暂时，2：周期，3：永久 */
    public int status;
    /** 生效时间 UTC时间戳，单位为s */
    public long activeTime;
    /** 失效时间 UTC时间戳，单位为s */
    public long expireTime;
    /** 生效星期，（星期几，例如周一和周三对应1和3，[1, 3]），仅在status=2时不可为空 */
    public List<Integer> weekdays;
    /** 是否过期，0：有效，1：过期 */
    public int isoutofdate;

    @Override
    public String toString() {
        return "SecurityKeyInfo{" +
                "keyId='" + keyId + '\'' +
                ", shareUid='" + shareUid + '\'' +
                ", status=" + status +
                ", activeTime=" + activeTime +
                ", expireTime=" + expireTime +
                ", weekdays=" + weekdays +
                ", isoutofdate=" + isoutofdate +
                '}';
    }
}
