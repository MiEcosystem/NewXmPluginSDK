# 场景相关API
## XmPluginHostApi
```
/**
 * ApiLevel:2 获取场景
 *
 * @param model
 * @param st_id    场景模板id
 * @param did
 * @param name
 * @param callback
 */
@Deprecated
public void loadScene(String model, int st_id, String did, String name,
                      final Callback<JSONObject> callback);

/**
 * ApiLevel:2 编辑场景接口
 *
 * @param model
 * @param st_id    场景模板id
 * @param us_id    场景id
 * @param did
 * @param name
 * @param setting
 * @param authed
 * @param callback
 */
@Deprecated
public void editScene(String model, int st_id, int us_id, String did, String name,
                      JSONObject setting,
                      JSONArray authed, final Callback<JSONObject> callback);

/**
 * ApiLevel:3 设置定时场景
 *
 * @param model
 * @param did
 * @param us_id
 * @param name
 * @param setting
 * @param authed
 * @param callback
 */
@Deprecated
public void editTimerScene(String model, String did, int us_id, String name,
                           JSONObject setting,
                           JSONArray authed, final Callback<JSONObject> callback);

/**
 * ApiLevel:3 加载所有定时场景
 *
 * @param model
 * @param did
 * @param name
 * @param callback
 */
@Deprecated
public void loadTimerScene(String model, String did, String name,
                           final Callback<JSONObject> callback);

/**
 * ApiLevel:3 获取定时场景
 *
 * @param model
 * @param did
 * @param us_id
 * @param callback
 */
@Deprecated
public void getTimerScene(String model, String did, int us_id,
                          final Callback<JSONObject> callback);

/**
 * ApiLevel:3 删除场景
 *
 * @param model
 * @param did
 * @param us_id
 * @param callback
 */
@Deprecated
public void delScene(String model, String did, int us_id,
                     final Callback<JSONObject> callback);

/**
 * ApiLevel:3 设置场景
 *
 * @param model
 * @param did
 * @param name
 * @param st_id
 * @param setting
 * @param authed
 * @param callback
 */
public void setScene(String model, String did, String name, int st_id,
                     JSONObject setting, JSONArray authed,
                     final Callback<Void> callback);

/**
 * ApiLevel:3 获取某个设备场景
 *
 * @param model
 * @param did
 * @param callback
 */
public void getScene(String model, String did,
                     final Callback<JSONObject> callback);

/**
 * ApiLevel:3 启动场景
 *
 * @param model
 * @param did
 * @param key
 * @param callback
 */
public void startScene(String model, String did, String key,
                       final Callback<JSONObject> callback);
                       
/**
 * ApiLevel:8 获取场景
 *
 * @param model
 * @param st_id    场景模板id
 * @param did
 * @param identify
 * @param callback
 */
public void loadScene(String model, int st_id, String did, String identify, String name,
                      final Callback<JSONObject> callback);

/**
 * ApiLevel:8 编辑场景接口
 *
 * @param model
 * @param st_id    场景模板id
 * @param us_id    场景id
 * @param did
 * @param name
 * @param setting
 * @param authed
 * @param callback
 */
public void editScene(String model, int st_id, int us_id, String did, String identify,
                      String name,
                      JSONObject setting,
                      JSONArray authed, final Callback<JSONObject> callback);

/**
 * ApiLevel:8 删除场景接口
 */

public void delScene(String model, int us_id,
                     final Callback<JSONObject> callback);
```
## IPluginHostActivity
```
/**
 * ApiLevel:1 开始加载场景
 */
public abstract void startLoadScene();

/**
 * ApiLevel:7 开始设置智能设备的场景
 *
 * @param deviceId
 */
public abstract void startCreateSceneByDid(String deviceId);

/**
 * ApiLevel:1 编辑当前已有智能场景
 *
 * @param sceneId
 */
public abstract void startEditScene(int sceneId);
    
/**
 * ApiLevel:8 获取当前设备所支持的推荐场景
 */
public abstract void getDeviceRecommendScenes(String did,
                                              AsyncCallback<List<RecommendSceneItem>> callback);

/**
 * ApiLevel:8 开始编辑推荐场景
 *
 * @param model 当前设备的model
 * @param did   当前设备的did
 */
public abstract void startEditRecommendScenes(RecommendSceneItem item, String model, String did);

/**
 * ApiLevel:8 根据did获取场景
 *
 * @param did 当前设备的did
 */
public abstract List<SceneInfo> getSceneByDid(String did);

/**
 * ApiLevel:8 enable or disable the specific scene
 */
public abstract void setSceneEnabled(SceneInfo info, boolean enable,
                                     AsyncCallback<Void> callback);

/**
 * ApiLevel:8 modeify scene name
 */
public abstract void modifySceneName(SceneInfo info, AsyncCallback<Void> callback);
    
/**
 * ApiLevel: 13 打开自定义场景创建页面
 *
 */
public abstract void startEditCustomScene();
    
    
/**
 * ApiLevel:19 开始加载场景
 */
public abstract void startLoadScene(AsyncCallback callback);

```