# 米家扩展程序（原插件）Android开发手册
[Github项目主页](https://github.com/MiEcosystem/NewXmPluginSDK)

[Wiki](https://github.com/MiEcosystem/NewXmPluginSDK/wiki)


# 概要

NewXmPluginSDK是为已接入米家APP的智能设备提供的米家扩展程序Android开发环境，里面包含米家APP提供给扩展程序的各种功能API，以及常用的UI组件。米家扩展程序也是基于Android环境开发的，开发扩展程序的时候请确保Android配置信息（比如compileSdkVersion、minSdkVersion、targetSdkVersion）与米家最新版本保持一致。

**米家APP最新版本：5.4.17**

**米家APP最新API Level：76**

**米家APP compileSdkVersion：27**

**米家APP minSdkVersion：15**

**米家APP targetSdkVersion：26**

**文档修改日期：2018-10-12**

# changelog
记录关键的sdk修改提交：

1）2018-6-22：在XmPluginHostApi类中新添加了一个接口checkAndRequestPermisson（API Level 75），用于扩展程序检查/请求所用到的Android权限。米家APP已经将targetSdkVersion升级到了25，如果扩展程序调用未经授权的Android相关权限功能，会导致扩展程序崩溃异常退出。

# 目录结构及文件含义

* gen_plug.py
	* 自动生成米家扩展程序工程脚本
* common_ui
	* 封装了一些通用的米家UI组件，米家扩展程序可以使用(扩展程序工程可通过plug.gradle脚本添加引用，不需要直接引用)
* libs_ex
	* 提供了一些通用的第三方sdk，比如appcompat-v7、support-v4、support-v13、recyclerview、微信sdk等，米家扩展程序开发的时候不需要在自己的工程中重复引用这些第三方sdk(扩展程序工程可通过plug.gradle脚本添加引用，不需要直接引用)
* pluglib
	* 封装了米家APP提供给扩展程序的各种api(扩展程序工程可通过plug.gradle脚本添加引用，不需要直接引用) 
* plugProject
	* 提供了一些Demo工程，米家扩展程序开发的时候可以参考
	* 第三方自己开发的米家扩展程序工程也可以放置在此目录下
* plug.gradle
	* 封装了common\_ui、libs\_ex、pluglib工程的引用，米家扩展程序只需要在自己的build.gradle脚本中添加`apply from: "${project.rootDir.absolutePath}/plug.gradle"`就可以自动依赖了
	* 米家扩展程序的调试安装也是在该脚本实现

# 米家扩展程序Android开发指南
[开发指南](https://iot.mi.com/new/guide.html?file=05-%E7%B1%B3%E5%AE%B6%E6%89%A9%E5%B1%95%E7%A8%8B%E5%BA%8F%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97/01-Android%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97/01-%E7%B1%B3%E5%AE%B6%E6%89%A9%E5%B1%95%E7%A8%8B%E5%BA%8F%E6%95%B4%E4%BD%93%E7%AE%80%E4%BB%8B)

# 米家调试APK下载地址

米家扩展程序开发调试的时候请使用此调试apk，不要使用线上版本的米家APP，另外调试的时候务必关闭应用商店的自动升级功能，避免调试apk被覆盖。

下载地址：

[http://cdn.cnbj0.fds.api.mi-img.com/miio.files/commonfile_apk_ac23ddc8b206fa432a11ce088f9a2719.apk](http://cdn.cnbj0.fds.api.mi-img.com/miio.files/commonfile_apk_ac23ddc8b206fa432a11ce088f9a2719.apk)


