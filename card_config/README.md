# CARD FAQ

Android卡片调试版本下载地址

http://cdn.cnbj0.fds.api.mi-img.com/miio.files/commonfile_apk_be5d80edd091ced11d132a483e8d8f09.apk

IOS卡片调试版本下载地址

https://pan.mioffice.cn/#/link/51BB9E73F1BDE103A723FEAB2A6E5E7C



卡片图片资源

https://pan.mioffice.cn:443/link/9A2EDA2665A7A07947A4AD66A650AA49

##### 使用 json 配置每个 model 的卡片样式为 profile 卡片
##### 使用 spec 协议实现的固件自动生成的卡片样式为 spec 卡片


访问密码：3McN

##### 注意：配置了 spec 或配置了 card_config 两种方式任何一种就会显示卡片。推荐配置 spec 卡片 [文档](https://iot.mi.com/new/guide.html?file=05-%E7%B1%B3%E5%AE%B6%E6%89%A9%E5%B1%95%E7%A8%8B%E5%BA%8F%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97/05-%E7%B1%B3%E5%AE%B6%E5%8D%A1%E7%89%87%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E/00-%E7%B1%B3%E5%AE%B6%E5%8D%A1%E7%89%87%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)。 

### 问题1，从A手机上控制卡片、插件或者直接控制设备，B手机卡片或宫格状态不同步变化，怎么定位是否收到了订阅？

答：
1.米家刚启动的时候，logcat 请搜索 D/MIIO: 结果中找 /mipush/eventsubbatch 订阅接口，请求参数中 didList 是 did 和 card_config声明的 prop的对对应关系，应该包含对应 model 配置中声明的 prop。接口返回 code:0 为订阅成功，code 为其他值请退出米家重试。
```
D/MIIO: host:api.io.mi.com prefix:nullpath:/mipush/eventsubbatch method:POST params:{"didList":{"yunyi.ba0313c9953448e43fc8":["event.dev_online","event.dev_offline"],"13061632":["prop.power","event.dev_online","event.dev_offline"]},"pushId":"iyxLngoZjY7zYvBKh4WvBflwyFX4Bd1HLg1q0erHOk\/E1WxegrZ4JHGXheuEfzmg","expire":180}
D/MIIO: {"code":0,"message":"ok","result":{"subId":"E6dQbagxTcOGU_Id4Tj2w","expire":180}}。
```

2.当 A 手机控制卡片、插件或者直接控制设备时，查看 B 手机是否收到推送，请搜索  E/PushManager: 结果如下，其中 attr 是收到的订阅属性 key 和值 value，did 是您设备的 did，subid和第一步服务器返回的subId对应，如果没收到如下推送，是固件的问题，如果收到如下推送请分享设备到894158105并提 issue。
```
 E/PushManager: process - {"attrs":[{"key":"prop.power","time":1557384858,"value":["on"]}],"did":"13061632","model":"lumi.gateway.v3","subid":"E6dQbagxTcOGU_Id4Tj2w"}
```

### 问题2，profile卡片：从插件里控制后，回到卡片或者首页宫格状态不同步变化。

答：logcat 请搜索 D/MIIO: 结果中找 /v2/device/batchgetdatas 获取设备属性接口，其中 did 是您设备的 did，prop 为 card_config 声明的所有属性，如果参数中缺少prop则配置有问题，返回结果中的prop 的值，和配置中声明的 card_items:operation:[prop_value]或者prop_key:prop_extra:[value]对应，影响展示的 button 状态和展示的文案。
```
D/MIIO: host:api.io.mi.com prefix:nullpath:/v2/device/batchgetdatas method:POST params:[data:[{"did":"lumi.158d0002401332","props":["prop.leak","event.leak","event.no_leak"]},{"from_flag":"ControlCardInfoManager:updatePropValue"}]]
D/MIIO: {"code":0,"message":"ok","result":{"lumi.158d0002401332":{"event.leak":null,"event.no_leak":null,"prop.leak":null}}}
```

### 问题3，profile卡片：卡片配置不生效

答：请根据您配置的类型 spec 或者 card_config，去上面的 所有设备card_config旧配置卡片、所有设备spec新配置卡片 搜索 model 是否已经在，并查看是否和您配置的 json 一致。


### 问题4，profile卡片：配置了 json 卡片样式和预期有差别，或打开卡片只有半透明蒙层 

答：1.请参考[所有支持样式](http://cdn.cnbj0.fds.api.mi-img.com/miio.files/resource_package/201810171148_card_config_des.zip)，您预期的样式是否和现有样式匹配(卡片上的控件个数不能多或少)。

2.请在 [所有设备卡片配置 profile卡片](https://api.io.mi.com/app/service/getappconfig?data=%7B%22lang%22%3A%22zh_CN%22%2C%22name%22%3A%22card_control_config_preview%22%2C%22version%22%3A%2211%22%7D) 中搜索是否已经配置了您的 model。


### 问题5，按钮或进度条操作后不生效 或者 其他控制问题

答：在 logcat 中搜索 “D/MIIO” 的 log(进程 com.xiaomi.smarthome:core) 为所有接口请求，其中 spec 卡片请求为 “spec/set”，card_config 卡片请求为 “rpc/设备did”，您都可以通过 设备的did 来过滤。看请求结果的 code=0为成功。

### 问题6，灯调节亮度的滑动条跳变。

答：由于连续操作多次引起，第一次操作订阅的通知 和 第二次操作的回调 同时影响到界面显示的值。

### 问题7，打开app 首页宫格开关或其他状态不对 或者 点击设备打卡快捷卡片开关或其他状态不对。

答：在 logcat 中搜索 “D/MIIO” 的 log(进程 com.xiaomi.smarthome:core) 为所有接口请求，card_config配置卡片 会请求接口 “/v2/device/batchgetdatas”，spec 配置卡片 会请求接口 “/miotspec/prop/get” 搜索您设备 did 看对应的 props 值是否符合预期。


### 问题8，spec卡片：产品支持转义的 spec 或者使用 spec 实现固件，但是卡片不显示。

答：1.如果设备未上线，需要使用 pv 环境才能更新到 spec instance。请参考视频 [如何使用 pv 配置](http://cdn.cnbj0.fds.api.mi-img.com/miio.files/commonfile_mp4_549e425be9a15e342f2a1eee3958488b.mp4)。

2.spec 卡片展示原理，是根据配置的设备类型，比如:设备 philips.light.bceiling2 [查询所有的 spec urn 列表](http://miot-spec.org/miot-spec-v2/instances?status=all) 结果为 urn:miot-spec-v2:device:light:0000A001:philips-bceiling2:1，从这个描述中看到 device:light 设备类型为 light，app 可以拉取到的spec协议定义 [http://miot-spec.org/miot-spec-v2/instance?type=](http://miot-spec.org/miot-spec-v2/instance?type=)urn:miot-spec-v2:device:switch:0000A003:090615-xswitch01:1  ,目前 [spec 卡片支持类型](https://api.io.mi.com/app/service/getappconfig?data=%7B%22lang%22%3A%22zh_cn%22%2C%22name%22%3A%22card_control_miotspec_config%22%2C%22version%22%3A%221%22%7D) 中搜索 "type":"light",找到匹配的类型为：
 ```
 {
    "type":"light",
    "card_items":[//配置了多个需要的属性
        {
            "cardType":1,//使用的卡片样式 文档:https://iot.mi.com/new/guide.html?file=05-米家扩展程序开发指南/09-米家卡片配置说明/00-米家卡片配置说明#card_items
            "prop_key":"p:light:on"//属性定义 p: 开头为 property，a: 开头为 action 两个冒号之间为 service 名称，冒号后面的为prop 名称
        },
        {
            "cardType":5,
            "prop_key":"p:light:brightness",
            "small_image":"seekbar_thumb_light",
            "operation":[
                {
                    "disable_status":[
                        {
                            "key":"p:light:on",
                            "value":false
                        }
                    ]
                }
            ]
        },
        {
            "cardType":11,
            "prop_key":"p:light:color-temperature",
            "operation":[
                {
                    "disable_status":[
                        {
                            "key":"p:light:on",
                            "value":false
                        }
                    ]
                }
            ]
        }
    ],
    "card_instance":[//配置了可以展示的卡片样式
        {
            "instance_type":0,// 1可以使用的样式 0该品类必须支持的样式  5宫格上的样式
            "min_support_version":2,
            "layout_type":0,//样式列表http://cdn.cnbj0.fds.api.mi-img.com/miio.files/resource_package/201810171148_card_config_des.zip
            "card_layout":[//spec协议中 需要定义的属性，所有的都匹配到才会展示为当前样式。
                "p:light:on"
            ]
        },
        {
            "instance_type":1,
            "min_support_version":3,
            "layout_type":3,
            "card_layout":[
                "p:light:on",
                "p:light:brightness",
                "p:light:color-temperature"
            ]
        },
        {
            "instance_type":1,
            "min_support_version":2,
            "layout_type":1,
            "card_layout":[
                "p:light:on",
                "p:light:brightness"
            ]
        },
        {
            "instance_type":5,
            "min_support_version":4,
            "layout_type":1,
            "card_layout":[
                "p:light:on"
            ]
        }
    ]
}
 ```
 其中 card_instance 为支持的样式 card_layout 为需要的属性，当您的 MIOT-SPEC 协议匹配中的时候，才会显示。


### 问题9，spec卡片：展示的 spec 卡片文案或图片缺失、错误。

答：spec 卡片展示样式和 MIOT-SPEC 协议 的[功能定义](https://iot.mi.com/fe-op/productCenter/config/function?productId=1946)强关联。如果功能定义的[描述](http://cdn.cnbj0.fds.api.mi-img.com/miio.files/commonfile_jpg_23e10d8672946186dc54a57ede3ac356.jpg)不能被解析,则会出现此问题。

spec 文案先匹配 [开放平台配置-高阶配置-文案多语言配置](https://iot.mi.com/fe-op/productCenter/config/advance/i18n?model=您真实设备的英文索引值&productId=您真实设备的产品编号值) (连接的中文替换为您对应的值)  如果没有配置到，会从 [默认配置列表](https://api.io.mi.com/app/service/getappconfig?data=%7B%22lang%22%3A%22zh_CN%22%2C%22name%22%3A%22card_language%22%2C%22version%22%3A%221%22%7D) json 的 content字段中 names 的所有数据中匹配对应的文案 匹配规则如下，如果不能展示 请您修改spec instance (Miot-SPEC 协议的配置) 的 description或添加 文案多语言配置。

spec 图片配置从 [默认配置列表](https://api.io.mi.com/app/service/getappconfig?data=%7B%22lang%22%3A%22zh_CN%22%2C%22name%22%3A%22card_language%22%2C%22version%22%3A%221%22%7D) json 的 content 字段中 icon_download_url 任意选一个语言下载压缩包，匹配规则如下：

匹配规则 按优先级排序为，区分大小写：
```
 category_serviceName_propertyDescription_ValueDescription 
 category_propertyDescription_ValueDescription
 propertyDescription_ValueDescription
 ValueDescription
```
###### 当您修改完 “Miot-SPEC 协议的配置” 请重新安装米家 app 生效

### 问题10，蓝牙设备展示的 value 特别大，不符合预期。

答：蓝牙设备的 value 是通过插件或者网关上报的服务端的，插件通过米家接口/device/ble_event或/device/event上报（具体请查看[文档](https://github.com/MiEcosystem/miot-plugin-sdk/wiki/20-%E7%B3%BB%E7%BB%9F%E6%9C%8D%E5%8A%A1_%E6%99%BA%E8%83%BD%E5%AE%B6%E5%BA%AD%E6%A8%A1%E5%9D%97)）,经过服务端转发到 app。app 获取到 属性值类似 0f0100 这样的16进制数，小端在前为 00010f，转换为10进制为271，根据卡片配置的 ratio=0.1 换算为最终展示到页面上的27.1。app 拉取属性值接口如下，可以根据关键字 “D/MIIO” 过滤：

profile卡片 device/batchgetdatas 服务器返回{"code":0,"message":"ok","result":{"设备id":{"属性名":属性值}}

spec 卡片 /miotspec/prop/get 服务器返回 {"code":0,"message":"","result":[{"did":"设备id","siid":???,"piid":???,"value"属性值,"code":0}]}



### 其他问题。
答：logcat 请过滤 mijia-card 其中 Info 级别为调试信息，Error 级别为报错的信息。请您根据 log 的描述试着解决一下，如果仍然无法解决，请到 issue 查看是否已经有其他开发者遇到了同样的问题，如果没有[请附上您的 log 创建 issue 记录问题](https://github.com/MiEcosystem/NewXmPluginSDK/issues/new?template=card.md).我们会根据提 issue 的先后顺序及时回复您的问题。
 
