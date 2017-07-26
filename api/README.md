#接口说明
##提供接口的几个基本文件
- XmPluginHostApi.java
- IXmPluginHostActivity.java
- XmBluetoothManager.java
##使用方法
XmPluginHostApi.java  
使用XmPluginHostApi.instance()调用其中方法

IXmPluginHostActivity.java  
继承XmPluginBaseActivity，基类面有个mHostActivity变量类型是IXmPluginHostActivity

XmBluetoothManager.java  
使用XmBluetoothManager.instance()调用其中方法

[基本功能Api](Base.md)  
[Activity及页面相关](Activity和页面相关.md)  
[更多菜单通用设置页面](control.md)  
[设备状态获取、控制和状态相关](control.md)  
[云端相关接口](net.md)  
[账号相关](account.md)  
[场景相关](scene.md)  
[统计相关](stat.md)  
[固件升级相关](firmware.md)  
[Notification相关](notification.md)  