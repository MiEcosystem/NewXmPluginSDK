# CARD FAQ

[Android卡片调试版本下载地址]
http://cdn.cnbj0.fds.api.mi-img.com/miio.files/commonfile_apk_5ac6e5d37cc13085a6b4b30f66a18113.apk

[IOS卡片调试版本下载地址]
https://pan.mioffice.cn:443/link/2A6DCEA3431164E89F3223C8C62A01F1

[所有设备 card_config 旧配置卡片release](https://api.io.mi.com/app/service/getappconfig?data=%7B%22lang%22%3A%22zh_CN%22%2C%22name%22%3A%22card_control_config%22%2C%22version%22%3A%2211%22%7D) 

[所有设备 card_config旧配置卡片debug](https://api.io.mi.com/app/service/getappconfig?data=%7B%22lang%22%3A%22zh_CN%22%2C%22name%22%3A%22card_control_config_preview%22%2C%22version%22%3A%2211%22%7D)

[所有设备 spec 新配置卡片release](http://miot-spec.org/miot-spec-v2/instances?status=released) 

[所有设备 spec 新配置卡片debug](http://miot-spec.org/miot-spec-v2/instances?status=debug)

##### 注意：配置了 spec 或配置了 card_config 两种方式任何一种就会显示卡片。推荐配置 spec 卡片 [文档](https://iot.mi.com/new/guide.html?file=05-%E7%B1%B3%E5%AE%B6%E6%89%A9%E5%B1%95%E7%A8%8B%E5%BA%8F%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97/05-%E7%B1%B3%E5%AE%B6%E5%8D%A1%E7%89%87%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E/00-%E7%B1%B3%E5%AE%B6%E5%8D%A1%E7%89%87%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)。 

### 问题1，状态不同步问题，包括宫格、卡片控件状态和实际设备状态不同步。

答：
1.查看app logcat 中是否调用接口 “/mipush/eventsubbatch” 成功订阅设备属性。

2.确认设备端有无上报对应属性。如果没有上报，请您的固件开发者解决。

3.检查app log，观察设备状态变化时有没有收到订阅消息。关键TAG：PushManager，如果有收到对应设备的上报消息，请找米家app开发进行分析解决。


### 问题2，卡片配置不生效

答：请根据您配置的类型 spec 或者 card_config，去上面的 所有设备card_config旧配置卡片、所有设备spec新配置卡片 搜索 model 是否已经在，并查看是否和您配置的 json 一致。

### 问题3，配置的卡片样式和预期有差别，或打开卡片只有半透明蒙层

答：
1.请参考[所有支持样式](http://cdn.cnbj0.fds.api.mi-img.com/miio.files/resource_package/201810171148_card_config_des.zip)，您预期的样式是否和现有样式匹配(卡片上的控件个数不能多或少)。

2.请在文档顶部的 所有设备配置release 查找和您产品类型或者卡片样式相同的，对比一下配置哪里有差别。


### 问题4，按钮或进度条操作后不生效 或者 其他控制问题

答：在 logcat 中搜索 “D/MIIO” 的 log(进程 com.xiaomi.smarthome:core) 为所有接口请求，其中 spec 卡片请求为 “spec/set”，card_config 卡片请求为 “rpc/设备did”，您都可以通过 设备的did 来过滤。看请求结果的 code=0为成功。

### 问题5，灯调节亮度的滑动条跳变。

答：由于连续操作多次引起，第一次操作订阅的通知 和 第二次操作的回调 同时影响到界面显示的值。

### 问题6，打开app 首页宫格开关或其他状态不对 或者 点击设备打卡快捷卡片开关或其他状态不对。

答：在 logcat 中搜索 “D/MIIO” 的 log(进程 com.xiaomi.smarthome:core) 为所有接口请求，card_config配置卡片 会请求接口 “/v2/device/batchgetdatas”，spec 配置卡片 会请求接口 “/miotspec/prop/get” 搜索您设备 did 看对应的 props 值是否符合预期。


### 问题7，配置了 spec，但是卡片不显示。

答：spec 卡片展示原理，是根据配置的设备类型，比如:设备philips.light.bceiling2配置的 spec 为 urn:miot-spec-v2:device:light:0000A001:philips-bceiling2:1，从这个描述中看到device:light 设备类型为 light ，现有[spec 卡片支持类型汇总](https://api.io.mi.com/app/service/getappconfig?data=%7B%22lang%22%3A%22zh_cn%22%2C%22name%22%3A%22card_control_miotspec_config%22%2C%22version%22%3A%221%22%7D) 搜索 "type":"light",找到匹配的类型为：
{"type":"light","card_items":[{"cardType":1,"prop_key":"p:light:on"},{"cardType":5,"prop_key":"p:light:brightness","small_image":"seekbar_thumb_light","operation":[{"disable_status":[{"key":"p:light:on","value":false}]}]},{"cardType":11,"prop_key":"p:light:color-temperature","operation":[{"disable_status":[{"key":"p:light:on","value":false}]}]}],"card_instance":[{"instance_type":0,"min_support_version":2,"layout_type":0,"card_layout":["p:light:on"]},{"instance_type":1,"min_support_version":3,"layout_type":3,"card_layout":["p:light:on","p:light:brightness","p:light:color-temperature"]},{"instance_type":1,"min_support_version":2,"layout_type":1,"card_layout":["p:light:on","p:light:brightness"]},{"instance_type":5,"min_support_version":4,"layout_type":1,"card_layout":["p:light:on"]}]}，根据card_instance查看现在已经支持的样式。

### 其他问题。
 答：请到 issue 查看是否已经有其他开发者遇到了同样的问题，如果没有[请创建 issue 记录问题](https://github.com/MiEcosystem/NewXmPluginSDK/issues/new?template=card.md).我们会根据提 issue 的先后顺序及时回复您的问题。
 
