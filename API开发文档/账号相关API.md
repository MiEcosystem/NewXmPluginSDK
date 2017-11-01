# 账号相关
## XmPluginHostApi
```
/**
 * ApiLevel:2 获取当前登录的账号id
 *
 * @return
 */
public abstract String getAccountId();

/**
 * ApiLevel:10 获取小米用户信息
 *
 * @param userid 小米账号
 */
public abstract void getUserInfo(String userid, final Callback<UserInfo> callback);
```