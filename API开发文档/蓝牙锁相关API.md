# 蓝牙锁相关API
## 蓝牙锁Demo
参考`plugProject/xiaomi_bledemov2`插件工程，里面提供了锁的连接、分享、删除、查询等操作示例。

## XmBluetoothManager
```
/**
 * ApiLevel: 49
 * 安全芯片连接，锁的Owner调用
 */
public abstract void securityChipConnect(String mac, final BleConnectResponse response);

/**
 * ApiLevel: 49
 * 锁的分享设备连接，被分享者调用
 * @param mac 设备的mac
 */
public abstract void securityChipSharedDeviceConnect(String mac, final BleConnectResponse response);

/**
 * ApiLevel: 49
 * 安全芯片数据加密
 */
public abstract void securityChipEncrypt(String mac, byte[] data, final BleReadResponse response);

/**
 * ApiLevel: 49
 * 安全芯片数据解密
 */
public abstract void securityChipDecrypt(String mac, byte[] data, final BleReadResponse response);

/**
 * ApiLevel: 49
 * 锁设备分享后的KeyId是否过期
 * @return true：钥匙没有过期，false：钥匙已过期
 */
public abstract boolean isSecurityChipSharedKeyValid(String mac);

/**
 * ApiLevel: 49
 * 安全芯片操作符 开锁
 */
public static final int SECURITY_CHIP_UNLOCK_OPERATOR = 0;
/**
 * ApiLevel: 49
 * 安全芯片操作符 关锁
 */
public static final int SECURITY_CHIP_LOCK_OPERATOR = 1;
/**
 * ApiLevel: 49
 * 安全芯片操作符 反锁
 */
public static final int SECURITY_CHIP_BOLT_OPERATOR = 2;

/**
 * ApiLevel: 49
 * 开锁成功后，设备通过notify返回的state码
 */
private static byte[] SECURITY_CHIP_UNLOCK_STATE = new byte[] {0x00};
/**
 * ApiLevel: 49
 * 关锁成功后，设备通过notify返回的state码
 */
private static byte[] SECURITY_CHIP_LOCK_STATE = new byte[] {0x01};
/**
 * ApiLevel: 49
 * 反锁成功后，设备通过notify返回的state码
 */
private static byte[] SECURITY_CHIP_BOLT_STATE = new byte[] {0x02};

/**
 * ApiLevel: 49
 * 提供支持安全芯片的锁操作
 * @param mac
 * @param operator 1: 开锁，2：反锁
 */
public abstract void securityChipOperate(String mac, int operator, final BleReadResponse response);

/**
 * ApiLevle: 49
 * 广播接收设备是否登录成功
 */
public static final String ACTION_ONLINE_STATUS_CHANGED = "action.online.status.changed";
public static final String EXTRA_MAC = "extra_mac";
public static final String EXTRA_ONLINE_STATUS = "extra_online_status";
public static final int STATUS_LOGGED_IN = 0x50;

/**
 * ApiLevel: 49
 * 获取MD5处理过的token
 * @return
 */
public abstract String getTokenMd5(String mac);
```

## XmPluginHostApi
```
/**
 * ApiLevel:49
 * 分享电子钥匙
 *
 * @param model 设备model
 * @param did 分享者的did
 * @param shareUid 分享目标的uid
 * @param status 分享类别，1：暂时，2：周期，3：永久
 * @param activeTime 生效时间 UTC时间戳，单位为s
 * @param expireTime 过期时间 UTC时间戳，单位为s
 * @param weekdays 生效日期（星期几，例如周一和周三对应1和3，[1, 3]，星期天对应0），仅在status=2时不可为空
 * @param readonly true：被分享人不可接收锁push，false：被分享人可接收锁push，（family关系用户不受这个字段影响）
 * @param callback
 */
public void shareSecurityKey(final String model, final String did, String shareUid, final int status, final long activeTime, final long expireTime,
                             final List<Integer> weekdays, final boolean readonly, final Callback<Void> callback);

/**
 * ApiLevel:49
 * 更新分享的电子钥匙信息
 *
 * @param model 设备的model
 * @param did 分享者的did
 * @param keyId 电子钥匙的keyId
 * @param status 分享类别，1：暂时，2：周期，3：永久
 * @param activeTime 生效时间 UTC时间戳，单位为s
 * @param expireTime 过期时间 UTC时间戳，单位为s
 * @param weekdays 生效日期（星期几，例如周一和周三对应1和3，[1, 3]），仅在status=2时不可为空
 * @param callback
 */
public void updateSecurityKey(String model, String did, String keyId, int status, long activeTime, long expireTime, List<Integer> weekdays, Callback<Void> callback);

/**
 * ApiLevel:49
 * 删除共享的电子钥匙
 *
 * @param model 设备的model
 * @param did 分享者的did
 * @param keyId 分享电子钥匙的KeyId
 * @param callback
 */
public void deleteSecurityKey(String model, String did, String keyId, final Callback<Void> callback);

/**
 * ApiLevel:49
 * 获取所有分享的电子钥匙信息
 *
 * @param model 设备model
 * @param did 分享者的did
 * @param callback
 */
public void getSecurityKey(String model, String did, final Callback<List<SecurityKeyInfo>> callback);

/**
 * ApiLevel:49
 * 获取UTC时间，单位为ms
 * 被废弃了，使用getUTCFromServer接口
 */
@Deprecated
public abstract long getUTCTimeInMillis();

/**
 * ApiLevel:49
 * 从服务器获取UTC时间，单位为秒（返回-1，说明解析出现异常，当做错误处理）
 * @param callback
 */
public void getUTCFromServer(String model, Callback<Long> callback);

/**
 * ApiLevel: 49
 * 获取蓝牙锁绑定的时间
 * @param model
 * @param did
 * @param callback
 */
public void getBleLockBindInfo(String model, String did, Callback<String> callback);
```