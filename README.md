# 新版小米智能家庭Android app免安装插件开发手册

## 重要修改

- 添加打点统计规范
 [打点统计规范](打点统计.pdf)

- 添加通用的打开设备安全设置接口

1.调用新版设置页面接口，传入参数"security_setting_enable"，打开安全设置选项
```
        Intent intent = new Intent();
        intent.putExtra("security_setting_enable",true);
        mHostActivity.openMoreMenu2(menus, true, REQUEST_MENUS, intent);
```

2.在需要验证pincode的页面onCreate()中调用如下接口
        
```
  mHostActivity.enableVerifyPincode();
  
```

- 更新了微信sdk库，如果使用下面的接口插件要更新插件，否则会crash

```
  /**
     * 在ApiLevel:25后升级了微信sdk，有用到这个接口的必须更新插件sdk适配，发布新版插件并且修改minPluginSdkApiVersion为25
     * ApiLevel:20 创建米家app注册的微信接口
     */
    public abstract IWXAPI createWXAPI(Context context, boolean bl);
```

## 最新修改

-  [新更多菜单接口](更多菜单接口.md)
XmPluginHostApi
```
  /**
      * ApiLevel: 25
      * 编码生成二维码图片
      * @param barcode 二维码信息
      * @param width 图片宽度
      * @param height 图片高度
      * @return 返回二维码图片,为ARGB_8888格式
      */
     public abstract Bitmap encodeBarcode(String barcode,int width,int height);
 
     /**
      * ApiLevel: 25
      * 解码二维码图片
      * @param bitmap 二维码图片,必须为ARGB_8888格式
      * @return 返回二维码信息
      */
     public abstract String decodeBarcode(Bitmap bitmap);
```

- 添加二维码接口

IXmPluginHostActivity

```
 /**
     * ApiLevel: 27 更多菜单新标准，从上下拉菜单，默认有
     * 智能场景 scence_enable
     * 通用设置 common_setting_enable
     * 帮助与反馈 help_feedback_enable
     */
   public abstract void openMoreMenu2(ArrayList<MenuItemBase> menus,
                                            boolean useDefault, int requestCode, Intent params);
```

IXmPluginHostActivity

```
/**
     * ApiLevel:25 跳转二维码扫描页面
     * @param bundle 请求参数，可以穿null @see #Activity.startActivityForResult(Intent, requestCode)
     * @param requestCode @see #Activity.startActivityForResult(Intent, requestCode)
     */
    public abstract void openScanBarcodePage(Bundle bundle,int requestCode);
```

- 添加水电煤气缴费接口

IXmPluginHostActivity

```
/**
     * ApiLevel:23 跳转水电燃气缴费页面
     * @param type 0:水电燃气缴费主页面 1:水 2:电 3:燃气
     * @param latitude 纬度
     * @param longitude 经度
     */
    public abstract void openRechargePage(int type,double latitude,double longitude);
```

- 添加水电煤气缴费余额查询接口

XmPluginHostApi
```
 /** ApiLevel: 23
     *
     * 查询水电燃气余额
     *
     * @param type  1:水 2:电 3:燃气
     * @param latitude 纬度
     * @param longitude 经度
     * @param callback 返回查询余额Json //{"balance":300,"updateTime":1465781516,"rechargeItemName":"郑州市燃气费"}
     *                 返回null时表示没有绑定机表号
     */
    public abstract void getRechargeBalances(int type,double latitude,double longitude,Callback<JSONObject> callback);

```

- 添加给插件发送broadcast接口

```
IXmPluginHostActivity
  /**
      * ApiLevel: 22
      * 给设备发送broadcast，会发送给IXmPluginMessageReceiver.handleMessage
      */
     public Intent getBroadCastIntent(DeviceStat deviceStat){
         Intent intent = new Intent("com.xiaomi.smarthome.RECEIVE_MESSAGE");
         intent.putExtra("device_id", deviceStat.did);
         return intent;
     }
```
- 插件的build.gradle里边不要使用下面的库，这些库依赖已经在脚本中加入
```
    compile 'com.android.support:support-v4:23.1.1'
    compile 'com.android.support:support-v13:23.1.1'
    compile 'com.android.support:recyclerview-v7:23.1.1'
    compile 'com.android.support:appcompat-v7:23.1.1'
```
- 添加使用帮助接口
```
IXmPluginHostActivity
    /**
     * ApiLevel:22 跳转到帮助页面
     */
    public abstract void openHelpActivity();

```
- 共享权限控制
```
类BaseDevice ApiLevel:20
共享的设备
public boolean isShared()
只读共享的设备，不能控制，兼容旧版同时isShared()返回true
public boolean isReadOnlyShared()
```
- 插件运行独立进程，退出插件后30s，自动退出插件进程
- 新版插件sdk使用Android studio开发
- 自动生成插件工程脚本gen_plug.py
- 执行gradle install自动安装插件并启动app

----


## 插件开发

### 开发前准备工作
- 登陆[智能家庭开放平台](https://open.home.mi.com)
- 申请开发者账号userid
- 登记新产品,记录设备model
- 创建签名证书
- 创建插件信息，提交证书md5信息给小米

```
签名文件的md5信息获取，需要去掉:号

keytool -list -v -keystore  keyFilePath -storepass keypassword  -keypass  keypassword
```

### 安装开发版智能家庭app

智能家庭应用商店版的app不支持本地开发调试，需要安装sdk目录下的智能家庭app

### 工程目录结构

- [github](https://github.com/MiEcosystem/NewXmPluginSDK)更新SDK代码

- 插件工程放置于plugProject目录下，如下图，可以放置多个插件工程，注意插件工程目录结构
也可以在plugProject目录下直接管理其他git创库工程，比如插件实例工程[NewPluginDemo](https://github.com/MiEcosystem/NewPluginDemo)，在plugProject目录下直接clone即可

```
cd plugProject
git clone https://github.com/MiEcosystem/NewPluginDemo.git
```

![](./md_images/gradle_project.png)

### 旧版sdk插件工程迁移到新sdk

执行sdk目录下python脚本move_plug.py如下

```
python move_plug.py oldPlugPath
```

### 创建新插件工程
- 执行SDK目录下python脚本gen_plug.py
  注意userid为申请的小米开发者账号,小米账号,不是手机号码

```
python gen_plug.py model userid
```

### 配置插件签名文件
所有插件在智能家庭app上运行时需要进行签名验证
- 修改插件工程build.gradle的签名信息

```
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

### 配置插件编译脚本

- 修改插件工程build.gradle，末尾添加

```
apply from: "${project.rootDir.absolutePath}/plug.gradle"

```

### 添加插件依赖项目
如果插件有其他的项目依赖，添加到complieProject属性中，如下

```
project.ext.set("complieProject",[":demolib"])
```
依赖项目结构如下

![](./md_images/gradle_lib.png)

依赖jar库和native so放置于libs目录下

### 插件编译运行

- 通过gradle指令编译运行，在插件sdk目录下执行

```
gradle tasks
```
可以看到安装插件task，如下图所示

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

### 关联pluglib库源码
插件开发的时候，发现pluglib中的函数变量名被重命名，没有注释，可以通过下面方法关联pluglib源码看到源码中的注释和变量名
点击系统反编译源码文件的右上角

![](./md_images/attach_sources_0.png)

然选择libs_ex下的pluglib-sources.jar

![](./md_images/attach_sources_1.png)

### 调试插件
安装上插件后，会自动启动智能家庭app，点击android studio 调试按钮，选择com.xiaomi.smarthome:plugin进程，如下图所示按钮，可以在插件代码中打断点调试

![](./md_images/gradle_debug.png)

### 上传插件到智能家庭后台，申请上线

开发完成后，测试通过，可以申请上线，编译好的安装包插件目录/build/outputs/apk下面
除了功能测试通过，必须要注意进行内存测试，原则上在退出插件页面后，插件需要退出所有的后台线程，释放所有的内存资源，特别是Activity对象的内存泄露
上线审核前，会专门针对这两项测试。


## 插件实例测试

[插件实例工程](https://github.com/MiEcosystem/NewPluginDemo)
智能家庭app测试账号，安装完插件后，点击小米开发板设备，可以看到实例效果
```
用户名:923522198
密码:123asdzxc
```

## 依赖其他jar包
如果想依赖其他jar的话，不需要修改gradle，直接将jar放入./libs目录下面

## 其他链接

- [框架描述](框架描述.md)
- [服务器部署](服务器部署.md)
- [插件开发](插件开发.md)
- [开发接口描述](开发接口描述.md)
- [蓝牙规范](智能家庭蓝牙规范.md)
- [插件实例工程](https://github.com/MiEcosystem/NewPluginDemo)


## 开发遇到问题

- 插件里边尽量避免和common_ui里边同名资源名，否则插件中的资源会替换掉common_ui里边资源
- androi-support-*.jar库不需要插件引入，已经在公共配置中加入
- 插件中用到和app相同的库，目前最新开发版app已经解决这个问题，线上app，还是需要保持库版本一致，而且不要混淆

------

开发联系方式
wx:zhaomingnpu
