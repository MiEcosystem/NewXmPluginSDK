# 新版米家Android app免安装插件开发手册
[Github项目主页](https://github.com/MiEcosystem/NewXmPluginSDK)

[Wiki](https://github.com/MiEcosystem/NewXmPluginSDK/wiki)

# 目录
- [概要](#概要)
- [SDK结构说明](#SDK结构说明)
- [功能模块文档](#功能模块文档)
- [插件示例代码](#插件示例代码)
- [开发前准备工作](#开发前准备工作)
- [开发插件](#开发插件)
- [调试插件](#调试插件)
- [测试插件](#测试插件)
- [上传插件](#上传插件)
- [开发遇到的问题](# 开发遇到的问题)

# 概要

NewXmPluginSDK是为已接入米家APP的智能设备提供的Android插件开发环境，里面包含米家APP提供给插件的各种功能API，以及常用的UI组件。插件也是基于Android环境开发的，开发插件的时候请确保插件的Android配置信息（比如compileSdkVersion、minSdkVersion、targetSdkVersion）与米家最新版本保持一致。

**米家app最新版本：5.0.3**

**米家app最新API Level：48**

**米家app compileSdkVersion：25**

**米家app minSdkVersion：14**

**米家app targetSdkVersion：19**

**文档修改日期：2017-10-26**

**本文档描述了米家app Android客户端插件的申请、创建、开发、调试的流程，更多内容详见下列文档，遇到问题先查看[Wiki](https://github.com/MiEcosystem/NewXmPluginSDK/wiki)**

# SDK结构说明
* gen_plug.py
	* 自动生成插件工程脚本
* MiJiaAuthSDK
	* 提供米家Auth授权SDK
* mibtservice
	* 当设备运行的是Android系统的时候，此SDK给设备提供米家标准蓝牙通信协议，并且封装RPC、获取米家app设备属性等功能
* common_ui
	* 封装了一些通用的米家UI组件，插件可以使用(插件工程可通过plug.gradle脚本添加引用，不需要直接引用)
* libs_ex
	* 提供了一些通用的第三方sdk，比如appcompat-v7、support-v4、support-v13、recyclerview、微信sdk等，插件开发的时候不需要在自己的工程中重复引用这些第三方sdk(插件工程可通过plug.gradle脚本添加引用，不需要直接引用)
* pluglib
	* 封装了米家app提供给插件的各种api(插件工程可通过plug.gradle脚本添加引用，不需要直接引用) 
* plugProject
	* 提供了一些Demo工程，插件开发的时候可以参考
	* 第三方自己开发的插件工程也可以放置在此目录下
* plug.gradle
	* 封装了common\_ui、libs\_ex、pluglib工程的引用，插件只需要在自己的build.gradle脚本中添加`apply from: "${project.rootDir.absolutePath}/plug.gradle"`就可以自动依赖了
	* 插件的调试安装也是在该脚本实现
* SmartHome.apk
	* 插件开发调试的时候请使用此调试apk，不要使用线上版本的米家app，另外调试的时候务必关闭应用商店的自动升级功能，避免调试apk被覆盖
* md_image
	* 存放SDK使用文档中引用到的图片，插件开发可忽略

# 功能模块文档
* [Android客户端插件人机界面指南](Android客户端插件人机界面指南.pdf)
	* 供插件UI设计参考
* UI交互规范
	* 供插件UI设计参考
* [蓝牙连接规范](蓝牙连接规范.md)
	* 显示米家app中蓝牙设备的配对、连接授权流程，供了解参考
* [插件框架描述](框架描述.md)
	* 整体描述了米家app插件的原理 
* [插件开发入门](插件开发.md)
	* 整体描述了开发一个米家app插件的流程
* [API开发文档](API开发文档/README.md)
	* 详细描述了插件开发中可以使用哪些米家app提供的API

# 插件示例代码

从github下载[插件示例工程](https://github.com/MiEcosystem/NewPluginDemo)，里面包含米家app提供给插件的各种功能演示。
	
安装完示例插件后，点击小米开发板设备，可以看到实例效果。
	
*说明：必须得使用米家app测试账号才能运行示例插件，测试账号如下：*
```
用户名:923522198
密码:123asdzxc
```

# 开发前准备工作
1. 登陆[米家开放平台](https://open.home.mi.com)
1. 申请开发者账号DevelopId
1. 登记新产品,记录设备model
1. 创建签名证书
1. 创建插件信息，提交证书md5信息给小米

	```
	签名文件的md5信息获取，需要去掉:号
	
	keytool -list -v -keystore  keyFilePath -storepass keypassword  -keypass  keypassword
	```
1. 配置账号白名单
	
	如果开发者如果不是用注册开发者的小米账号登录的话，需要把当前的小米账号配置成协作开发或者测试白名单：
	![](./md_images/391501033771_.pic.jpg)
1. 安装开发版米家app	

	米家应用商店版的app不支持本地开发调试，需要安装sdk目录下的米家app（SmartHome.apk）。
	
# 开发插件
1. [github](https://github.com/MiEcosystem/NewXmPluginSDK)更新SDK代码
	
	插件工程放置于plugProject目录下，如下图，可以放置多个插件工程，注意插件工程目录结构也可以在plugProject目录下直接管理其他git创库工程，比如插件实例工程[NewPluginDemo](https://github.com/MiEcosystem/NewPluginDemo)，在plugProject目录下直接clone即可：
	
	```
	cd plugProject
	git clone https://github.com/MiEcosystem/NewPluginDemo.git
	```
	![](./md_images/gradle_project.png)

1. 创建新插件工程
	
	执行SDK目录下python脚本gen_plug.py：
	
	注意：DevelopId为申请的小米开发者账号（小米账号），不是手机号码。
	
	```
	python gen_plug.py model userid
	```
1. 配置插件签名文件
	
	所有插件在米家app上运行时需要进行签名验证，修改插件工程build.gradle的签名信息：
	
	```
	defaultConfig {
	    // minSdkVersion和targetSdkVersion必须与米家app保持一致，如果minSdkVersion设置过高，则插件无法在低版本Android平台下载安装
        minSdkVersion 14
        targetSdkVersion 19
    }
    signingConfigs {
        release {
            storeFile new File("${project.projectDir}/keystore/key.keystore")
            storePassword 'mihome'
            keyAlias 'mihome-demo-key'
            keyPassword 'mihome'
        }
    }
	
    buildTypes {
        debug {
            debuggable true
            signingConfig signingConfigs.release
        }
        release {
            minifyEnabled true
            shrinkResources false
            zipAlignEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
	```
1. 配置插件编译脚本
	
	修改插件工程build.gradle，末尾添加：
	
	```
	apply from: "${project.rootDir.absolutePath}/plug.gradle"
	
	```

1. 添加插件依赖项目

	如果插件有其他的项目依赖，添加到complieProject属性中，如下：
	
	```
	project.ext.set("complieProject",[":demolib"])
	```
	依赖项目结构如下：
	
	![](./md_images/gradle_lib.png)
	
	依赖jar库和native so放置于libs目录下。

1. 插件编译运行

	通过gradle指令编译运行，在插件sdk目录下执行：
	
	```
	gradle tasks
	```
	可以看到安装插件task，如下图所示：
	
	![](./md_images/gradle_tasks.png)
	
	```
	./gradlew install 安装运行release配置插件
	./gradlew installRelease 安装运行release配置插件
	./gradlew installDebug 安装运行debug配置插件
	
	如果有多个插件工程，上面指令会安装所有插件，指定安装某个插件工程,比如插件Xiaomi_demo
	
	./gradlew installXiaomi_demo 安装运行release配置插件
	./gradlew installXiaomi_demoRelease 安装运行release配置插件
	./gradlew installXiaomi_demoDebug 安装运行debug配置插件
	```

1. 关联pluglib库源码

	插件开发的时候，发现pluglib中的函数变量名被重命名，没有注释，可以通过下面方法关联pluglib源码看到源码中的注释和变量名。
	
	先点击系统反编译源码文件的右上角：
	
	![](./md_images/attach_sources_0.png)
	
	然后选择libs_ex下的pluglib-sources.jar：
	
	![](./md_images/attach_sources_1.png)

1. 依赖其他jar包
	
	如果想依赖其他jar的话，不需要修改gradle，直接将jar放入./libs目录下面。

# 调试插件
安装上插件后，会自动启动米家app，点击android studio 调试按钮，选择com.xiaomi.smarthome:plugin进程，如下图所示按钮，可以在插件代码中打断点调试
![](./md_images/gradle_debug.png)

# 测试插件

1. 有真实的设备测试：
	
	1）米家app中扫描设备；
	
	2）连接设备（不同设备连接过程可能不一样，按照提示引导进行）；
	
	3）连接成功后就可以进入插件进行功能测试了。
1. 暂时没有真实设备：
	
	如果开发者暂时没有真实的设备，想跳过连接过程直接打开插件，可以在米家app的开发者选项中打开“是否开启无设备开发模式”，然后进入手动添加设备页面，长按设备，即可进入设备添加页面。

# 上传插件

插件开发完成，测试通过后，可以到米家后台申请上线，编译好的安装包插件目录/build/outputs/apk下面。

除了功能测试通过，必须要注意进行内存测试，原则上在退出插件页面后，插件需要退出所有的后台线程，释放所有的内存资源，特别是Activity对象的内存泄露
上线审核前，会专门针对这两项测试。

**在插件的测试和发布过程中如有其它疑问可联系米家的工作人员。**

# 开发遇到的问题

- 插件里边尽量避免和common\_ui里边同名资源名，否则插件中的资源会替换掉common\_ui里边资源
- 暂时不支持Serializable，相关需求的话请用Parcelable代替
- androi-support-*.jar库不需要插件引入，已经在公共配置中加入
- 插件中用到和app相同的库，目前最新开发版app已经解决这个问题，线上app，还是需要保持库版本一致，而且不要混淆
- **更多其它问题可查看[Wiki](https://github.com/MiEcosystem/NewXmPluginSDK/wiki)**



