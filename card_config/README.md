# CARD FAQ

[Android卡片调试版本下载地址]
http://cdn.cnbj0.fds.api.mi-img.com/miio.files/commonfile_apk_7e6a43aa46a639fbbc2002adb8c3dd84.apk
[IOS卡片调试版本下载地址]
https://pan.mioffice.cn:443/link/2A6DCEA3431164E89F3223C8C62A01F1

### 问题1，状态不同步问题，包括宫格、卡片控件状态和实际设备状态不同步

答：

第一步，检查app log，观察设备状态变化时有没有收到订阅消息。关键TAG：DevicePropSubscriber: subscribe onReceive，如果有收到对应设备的上报消息，请找米家app开发进行分析解决。如果没有收到设备上报消息，请看第二步；

第二步：检查设备端有无上报。如果没有上报，请联系自家设备端开发解决。如果有上报，请联系米家后台一起分析解决。







