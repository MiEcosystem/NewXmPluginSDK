# MiJiaAuth 米家授权

背景：我们开发了一套授权的系统，通过米家App对自己选择的设备进行授权，就可以在其他app中获取这些授权设备的信息并且可以对其进行操作。

前提条件：

在获取授权之前，您需要：

1)	登录[小米IoT开放平台](https://iot.mi.com)完成开发者资质认证；

2)	完成app应用申请并通过审核，并完善相关资料；

3)	下载一个米家App并注册一个属于自己的账号。

### 米家Android客户端授权调用

### 1 引入sdk包
compile(name:'mijia_authlib_1.0.1', ext:"aar")

### 2 在activity的oncreate函数中进行初始化
 <pre><code>
IAuthMangerImpl.getInstance().intiWithCallBack(AuthActivity.this, new IInitCallBack() {
                        @Override
                        public void onServiceConnected(int result) {
//                        Toast.makeText(AuthActivity.this, "已经初始化完毕啦", Toast.LENGTH_SHORT).show();
                            Log.d("AuthActivity","IAuthMangerImpl.getInstance().getSdkApiLevel()" + IAuthMangerImpl.getInstance().getSdkApiLevel()+"   result:    "+result);
                            onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE);
                        }

                    });
 </pre></code>
### 3 发起授权IAuthMangerImpl.getInstance().callAuth（final Context activityContext, final Bundle data, final int requestCode, IAuthResponse response）

#######context需要activity的context。

**requestCode**对应的是要请求哪种授权<br>
* 1)如果请求的是给设备进行授权则传入AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE(设备授权之前，该小米账户需要已经绑定该设备)<br>
* 2)请求设备绑定的话传入AuthCode.REQUEST_CODE_CALL_AUTH_FOR_BIND_DEVICE<br>

**Bundle data**对应的是需要传入的参数
 如果是设备授权的话，需要传入AuthConstants.EXTRA_APPLICATION_ID，该参数需要到小米IoT开放平台申请。<br>
 同时你还需要传入设备的idAuthConstants.EXTRA_DEVICE_DID，该参数是你需要授权的设备的did。<br>
 例如<br>
 如果是绑定设备，除了传入did之外，还需要传入下面几个参数:<br>
 设备bindkey，EXTRA_DEVICE_BIND_KEY为key。<br>
 
 <pre><code>
   Bundle bundle = new Bundle();
   bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, "app_id");
   if (requestCode == AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE){
        bundle.putString(AuthConstants.EXTRA_DEVICE_DID,"device_id");    
  }               
 </pre></code>

 
### 4 接收返回值 通过IAuthResponse
void onSuccess(int code, Bundle data);表示授权成功<br>
void onFail(int code, Bundle data);  表示授权失败<br>
其中code值的含义与下面的对应。  可以直接使用该AuthCode<br>
<pre><code>
public class AuthCode {
    public static final int AUTH_SUCCESS = 100;//授权成功
    public static final int BIND_SUCCESS = 101;//绑定成功
    public static final int PACKAGE_ERROR = -100;//包名错误
    public static final int LACK_PARAMS_ERROR = -101;//缺少参数
    public static final int GET_TOKEN_ERROR = -102;//获取token失败
    public static final int AUTH_ERROR = -103;//授权失败
    public static final int APP_ID_ERROR = -104;//appid有问题
    public static final int APP_SIGN_ERROR = -105;//签名错误
    public static final int AUTH_CANCEL = -106;///取消授权
    public static final int REQUEST_AUTH_ERROR = -107;//请求授权失败
    public static final int REQUEST_CODE_ERROR = -108;//请求的code错误
    public static final int REQUSET_DID_ERROR = -109;///缺少did
    public static final int REQUEST_AUTH_NO_CAPABILITY = -110;///该设备不支持语音授权，或者该设备不属于你的名下
    public static final int REQUEST_AUTH_NO_PERMISSION = -111;///该账号不支持该类型授权，请到小米IoT开放平台申请
    public static final int REQUEST_MISS_PARAMS = -112;//缺少参数
    public static final int REQUEST_BIND_ERROR = -113;//绑定失败
    public static final int REQUEST_API_LEVEL_ERR = -114;///版本号不匹配
    public static final int REQUEST_NOT_SUPPORT_FOR_INTERNAL = -115;//暂时不支持海外版
    public static final int NET_ERR = -116;//网络不稳定或者没有联网，请检查网络后重试
    public static final int REQUEST_SERVICE_DISCONNECT = -901;//service已经断开
    public static final int REQUEST_MIJIA_VERSION_ERR = -902;//可能没有安装米家，或者米家版本太低
    public static final int REQUEST_NO_RESPONSE = -903;///IAuthResponse 为空
    


    public static final int REQUEST_CODE_CALL_AUTH_FOR_DEVICE = 2;//给设备授权
    public static final int REQUEST_CODE_CALL_AUTH_FOR_BIND_DEVICE = 6;//给需要绑定的设备授权
}
</pre></code>

而返回的data中，主要有下面值，注意判空使用<br>
<pre><code>
/**返回值*****/
    public static final String EXTRA_TOKEN = "extra_token";
    public static final String EXTRA_RESULT_CODE = "extra_result_code";
    public static final String EXTRA_RESULT_MSG = "extra_result_msg";
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_VERSION_INFO = "extra_version_info";
    </pre></code>
<pre><code>    
///一些请求和返回的key常量
public class AuthConstants {
     //    请求参数
    public static final String EXTRA_APPLICATION_ID = "extra_application_id";
    public static final String EXTRA_PACKAGE_NAME = "extra_package_name";
    public static final String EXTRA_APP_SIGN = "extra_app_sign";
    public static final String EXTRA_DEVICE_DID = "device_id";
    public static final String EXTRA_DEVICE_TOKEN = "device_token";
    public static final String EXTRA_DEVICE_BIND_KEY = "device_bind_key";
    public static final int ACTIVITY_RESULT_FAIL = -2;
    public static final String EXTRA_SDK_VERSION_CODE ="sdk_version_code";
    public static final String EXTRA_SDK_VERSION_NAME = "sdk_version_name";
    public static final String EXTRA_DEVICE_TOKEN_TIMESTAMP = "token_timestamp";
    public static final String EXTRA_DEVICE_SN = "device_sn";

    /**返回值*****/
    public static final String EXTRA_TOKEN = "extra_token";
    public static final String EXTRA_RESULT_CODE = "extra_result_code";
    public static final String EXTRA_RESULT_MSG = "extra_result_msg";
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_VERSION_INFO = "extra_version_info";
}<br>
</pre></code>

### 5  最后需要在onDestroy里面释放
<pre><code>
@Override
    protected void onDestroy() {
        super.onDestroy();
        IAuthMangerImpl.getInstance().release();
    }</pre></code>
    
### 6  调用示例
<pre><code>

package com.xiaomi.smarthome.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.authlib.AuthCode;
import com.xiaomi.smarthome.authlib.AuthConstants;
import com.xiaomi.smarthome.authlib.IAuthMangerImpl;
import com.xiaomi.smarthome.authlib.IAuthResponse;
import com.xiaomi.smarthome.authlib.IInitCallBack;

public class AuthActivity extends Activity {
    Button mAuthDeviceBtn;
    Button mAuthAppBtn;
    TextView mResult;
    ImageView mAppIcon;
    EditText mAppIdET;
    EditText mDeviceET;
    Button mRelase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuthDeviceBtn = (Button) findViewById(R.id.go_auth);
        mAuthAppBtn = (Button) findViewById(R.id.go_auth2);
        mResult = (TextView) findViewById(R.id.result);
        mAppIcon = (ImageView) findViewById(R.id.app_icon);
        mAppIdET = (EditText) findViewById(R.id.app_id);
        mDeviceET = (EditText) findViewById(R.id.device);
        mRelase = (Button) findViewById(R.id.release_btn);
        mAuthDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IAuthMangerImpl
                IAuthMangerImpl.getInstance().intiWithCallBack(AuthActivity.this, new IInitCallBack() {
                    @Override
                    public void onServiceConnected(int result) {
                        Toast.makeText(AuthActivity.this, "已经初始化完毕啦", Toast.LENGTH_SHORT).show();
                        Log.d("AuthActivity", "IAuthMangerImpl.getInstance().getSdkApiLevel()" + IAuthMangerImpl.getInstance().getSdkApiLevel() + "   result:    " + result);
//                        onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE);
                        goToDeviceAuth(AuthActivity.this,"9971080915123888","58067422");
                    }

                });
            }
        });
        mRelase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAuthMangerImpl.getInstance().release();
            }
        });
    }

    /**
     * 发起设备授权，该设备已经在米家app绑定过的
     *
     * @param context  最好是activity的
     * @param appId
     * @param deviceId
     */
    private void goToDeviceAuth(Context context, String appId, String deviceId) {
        Bundle bundle = new Bundle();
        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, appId);
        bundle.putString(AuthConstants.EXTRA_DEVICE_DID, deviceId);
        //发起授权
        IAuthMangerImpl.getInstance().callAuth(context, bundle, AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE, new IAuthResponse() {
                    @Override
                    public void onSuccess(int i, final Bundle bundle) {
                        print(bundle);
                    }

                    @Override
                    public void onFail(int i, Bundle bundle) {
                        print(bundle);
                    }
                }
        );
    }

    /**
     * 发起设备绑定授权，该设备没有在米家app绑定过，需要走绑定流程
     *
     * @param context  最好是activity的
     * @param appId
     * @param deviceId
     */
    private void goToBindDeviceAuth(Context context, String appId, String deviceId, String bindKey) {
        Bundle bundle = new Bundle();
        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, appId);
        bundle.putString(AuthConstants.EXTRA_DEVICE_DID, deviceId);
        bundle.putString(AuthConstants.EXTRA_DEVICE_BIND_KEY, bindKey);
        //发起授权
        IAuthMangerImpl.getInstance().callAuth(context, bundle, AuthCode.REQUEST_CODE_CALL_AUTH_FOR_BIND_DEVICE, new IAuthResponse() {
                    @Override
                    public void onSuccess(int i, final Bundle bundle) {
                        print(bundle);
                    }

                    @Override
                    public void onFail(int i, Bundle bundle) {
                        print(bundle);
                    }
                }
        );
    }


    private void print(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        if (bundle == null)
            return;
        sb.append("结果：").append("\n")
                .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID));
        Log.d("result", sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IAuthMangerImpl.getInstance().release();
    }
}



</pre></code>


### 混淆处理
-keep class com.xiaomi.smarthome.**{*;}

### ChangeLog
#### version  1.0.1
修改了检测到没有米家app或者当时米家版本不支持的处理逻辑。不在进行toast提示。<br>
开发者可以自行根据IAuthMangerImpl.getInstance().init(AuthActivity.this);返回值进行判断，如果返回true则表示初始化成功，失败表示没有相应的米家app（这个是推荐使用的）。在调用的时候IAuthMangerImpl.getInstance().callAuth也可以根据返回值进行判断，建议尽量在初始化的时候就进行处理<br>

#### version  1.0.3
新增了对绑定设备授权的功能，以及其使用的一些code值<br>
更新了新的米家apk(#247),增加了对一些权限的校验等功能<br>

#### version 1.1.0
采用了最新的服务api<br>
添加了对sdk的版本控制，会返回version_info,开发者可以自己监测版本是否正确(暂时不用处理)<br>
修改了app的一些UI逻辑等<br>
（由于服务改动，该版本改动比较，建议抓紧替换，之前的版本已经不可用，）<br>


#### versionName 1.1.01   versionCode 5
添加了intiWithCallBack接口，供给没有些生命周期等的使用<br>
修改了public int init(Context context)得返回值，从boolean改为int  当返回值==0时，表示初始化成功<br>
（如果不想改老版本SDK还可以继续使用）<br>


<pre><code>
/**
     * 给没有生命周期的初始化，像RN等
     * @apilevel  5
     * @param context
     * @param callBack
     */
 public abstract void intiWithCallBack(Context context,IInitCallBack callBack);
 

        IAuthMangerImpl.getInstance().intiWithCallBack(AuthActivity.this, new IInitCallBack() {
                        @Override
                        public void onServiceConnected(int result) {
//                        Toast.makeText(AuthActivity.this, "已经初始化完毕啦", Toast.LENGTH_SHORT).show();
                             //// TODO:  callAuth
                        }

                    });            
 
 </pre></code>
 
 ####  versionName 1.1.02 versionCode 6
 
 添加了对华为以及魅族手机在授权时，引导用户去开启权限的逻辑<br>
 
 #### versionName 1.1.2 versionCode 7
 
 修改了授权调起米家的方式,解决之前部分厂商无法调起的问题 需要同步修改米家的apk可以使用<br>
 之后调用需要Sdk的版本号大于app的版本号，否则会返回REQUEST_API_LEVEL_ERR = -114;///版本号不匹配<br>
 当版本不匹配的这时候，需要第三方能根据两边的版本号去提箱用户更新米家的app<br>
 新增了两个code   REQUEST_SERVICE_DISCONNECT = -901;//service已经断开   以及   REQUEST_MIJIA_VERSION_ERR = -902;//可能没有安装米家，或者米家版本太低<br>
 
 ####  versionName 1.1.3 versionCode 7
 
 优化了在授权过程中点击home问题，（此时会终止授权流程，关闭米家授权相关页面）<br>
 
 ####  versionName 1.1.4 versionCode 8
 添加了device_bind_key的支持，<br>
 
 
