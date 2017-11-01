# 统计相关API
## XmPluginHostApi
```    
/**
 * ApiLevel: 6 米家后台统计
 *
 * @param loadedInfo 插件上下文
 * @param key
 * @param value
 * @param extra      添加打点统计新规范，必须按照下面的key value来传参数
 *                   key: PageStart	应用页面打开，自动上报
 *                   value: {
 *                   "name":"页面名称",
 *                   "starttime":"打开时间，时间戳格式"
 *                   }
 *                   key: Task	应用表现，例如等待时间、执行结果等
 *                   value:{
 *                   "name":"任务名称",
 *                   "parent":"任务所在的页面",
 *                   "duration":"任务耗时，以毫秒计，可选",
 *                   "result":"任务结果，默认0为成功，-1为失败，其它自行定义，可选",
 *                   "starttime":"操作时间，时间戳格式"
 *                   }
 *                   key: PageEnd	应用页面关闭，自动上报
 *                   value:{
 *                   "name":"页面名称",
 *                   "starttime":"关闭时间，时间戳格式"
 *                   }
 *                   key: FloatWindow	悬浮窗打开
 *                   value:{
 *                   "name":"悬浮窗名称，可使用悬浮窗标题",
 *                   "parent":"弹出框所在页面名称",
 *                   "starttime":"打开时间，时间戳格式"
 *                   }
 *                   key: RPC	手机向设备发送指令，自动上报
 *                   value:{
 *                   "name":"指令名称",
 *                   "parameter":"指令的参数列表，json格式",
 *                   "web":"使用的网络，0为局域网，1为外网",
 *                   "starttime":"操作时间，时间戳格式"
 *                   }
 *                   key: Operation	用户行为，例如点击按钮
 *                   value:{
 *                   "name":"操作名称",
 *                   "parent":"弹出框所在页面名称",
 *                   "position":"操作所在的位置，格式为x/屏幕宽度&y/屏幕高度，如0.4&0.6",
 *                   "starttime":"操作时间，时间戳格式"
 *                   }
 *                   key:WebEnd	网页关闭，自动上报
 *                   value: {
 *                   "url":"网页连接",
 *                   "starttime":"关闭时间，时间戳格式"
 *                   }
 *                   key:WebStart	网页打开，自动上报
 *                   value:{
 *                   "url":"网页连接",
 *                   "starttime":"打开时间，时间戳格式"
 *                   }
 */
public abstract void addRecord(XmPluginPackage loadedInfo, String key, Object value,
                               JSONObject extra);
                               
/**
 * ApiLevel:39 米家后台统计.该打点同时也更新到小米开放平台上.
 *
 * @param loadedInfo 插件上下文
 * @param model      当前设备model
 * @param value      为Object 可以为int或String或JsonObject
 * @param extra      可以为null
 */
@Deprecated
public abstract void addRecordV2(XmPluginPackage loadedInfo, String model, String key, Object value, JSONObject extra);

/**
 * ApiLevel:4 写log文件，可以从反馈上报到统计平台
 *
 * @param tag
 * @param info
 * @return
 */
public abstract void log(String tag, String info);
```
## 更详细的打点统计参数说明
见文档[打点统计参数说明](打点统计参数说明.pdf)