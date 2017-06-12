# MiJiaAuth  米家授权

背景：我们开发了一套授权的系统，通过米家App对自己选择的设备进行授权，就可以在其他app中获取这些授权设备的信息并且可以对其进行操作。
前提条件：
在获取授权之前，您需要：
1)	在米家开放平台完成开发者资质认证  https://open.home.mi.com/index.html
2)	完成app应用申请并通过审核，并完善相关资料
3)	下载一个米家App并注册一个属于自己的账号

### 米家Android客户端授权调用

### 1 引入sdk包
compile(name:'mijia_authlib_1.0.1', ext:"aar")

### 2 在activity的oncreate函数中进行初始化
IAuthMangerImpl.getInstance().init(AuthActivity.this);///初始化

### 3 发起授权IAuthMangerImpl.getInstance().callAuth（final Context context, final Bundle data, final int requestCode, IAuthResponse response）

**requestCode**对应的是要请求哪种授权<br>
* 1)如果请求的是给设备进行授权则传入AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE(设备授权之前，该小米账户需要已经绑定该设备)<br>
* 2)请求设备绑定的话传入AuthCode.REQUEST_CODE_CALL_AUTH_FOR_BIND_DEVICE<br>

**Bundle data**对应的是需要传入的参数
 如果是设备授权的话，需要传入AuthConstants.EXTRA_APPLICATION_ID，该参数需要到开放平台申请。<br>
 同时你还需要传入设备的idAuthConstants.EXTRA_DEVICE_DID，该参数是你需要授权的设备的did。<br>
 例如<br>
 如果是绑定设备，除了传入did之外，还需要传入设备token，EXTRA_DEVICE_TOKEN为key。<br>
 
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
    public static final int REQUEST_AUTH_NO_CAPABILITY = -110;///缺少授权的支持，可能是你的该账号下面没有绑定该设备
    public static final int REQUEST_MISS_PARAMS = -112;
    public static final int REQUEST_BIND_ERROR = -113;

    public static final int REQUEST_CODE_CALL_AUTH_FOR_APP = 1;//请求app授权(暂不支持)
    public static final int REQUEST_CODE_CALL_AUTH_FOR_DEVICE = 2;//请求设备授权
    public static final int REQUEST_CODE_CALL_AUTH_FOR_BIND_DEVICE = 6;//给需要绑定的设备授权
}
</pre></code>

而返回的data中，主要有下面四个值，注意判空使用<br>
 /**返回值*****/<br>
    public static final String EXTRA_TOKEN = "extra_token";<br>
    public static final String EXTRA_RESULT_CODE = "extra_result_code";<br>
    public static final String EXTRA_RESULT_MSG = "extra_result_msg";<br>
    public static final String EXTRA_USER_ID = "extra_user_id";<br>
    
<pre><code>    
///一些请求和返回的key常量
public class AuthConstants {
    //    请求参数
    public static final String EXTRA_APPLICATION_ID = "extra_application_id";
    public static final String EXTRA_PACKAGE_NAME = "extra_package_name";
    public static final String EXTRA_APP_SIGN = "extra_app_sign";
    public static final String EXTRA_DEVICE_DID = "device_id";
    public static final String EXTRA_DEVICE_TOKEN = "device_token";
    public static final int ACTIVITY_RESULT_FAIL = -2;

    /**返回值*****/
    public static final String EXTRA_TOKEN = "extra_token";
    public static final String EXTRA_RESULT_CODE = "extra_result_code";
    public static final String EXTRA_RESULT_MSG = "extra_result_msg";
    public static final String EXTRA_USER_ID = "extra_user_id";
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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class AuthActivity extends AppCompatActivity {
    Button mAuthDeviceBtn;
    Button mAuthAppBtn;
    TextView mResult;
    ImageView mAppIcon;
    EditText mAppIdET;
    EditText mDeviceET;
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
        boolean isSuccess = IAuthMangerImpl.getInstance().init(AuthActivity.this);///初始化
        if (!isSuccess){
            Toast.makeText(AuthActivity.this, "请确认已经安装了米家，并且更新到最新的版本啦", Toast.LENGTH_SHORT).show();
        }
        mAuthDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE);
            }
        });
        mAuthAppBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_APP);
                                           }
                                       }
        );
    }

    private void onAuthClick(int requestCode){
        Bundle bundle = new Bundle();
        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, "9971080915123888");

        if (requestCode == AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE){
            if (TextUtils.isEmpty(mDeviceET.getText().toString())){
                Toast.makeText(AuthActivity.this,"device_id 不可以为空",Toast.LENGTH_SHORT);
                return;
            }
//            bundle.putString(AuthConstants.EXTRA_DEVICE_DID,"58067337");
            bundle.putString(AuthConstants.EXTRA_DEVICE_DID,mDeviceET.getText().toString());
        }
        //发起授权
        IAuthMangerImpl.getInstance().callAuth(AuthActivity.this, bundle, requestCode, new IAuthResponse() {
                    @Override
                    public void onSuccess(int i, final Bundle bundle) {
                        if (bundle != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("结果：").append("\n")
                                    .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                                    .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                                    .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                                    .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID));

                            mResult.setText(sb);
                        }
                    }

                    @Override
                    public void onFail(int i, Bundle bundle) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("结果：").append("\n")
                                .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                                .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                                .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                                .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID));
                        mResult.setText(sb);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IAuthMangerImpl.getInstance().release();
    }
}


</pre></code>
### 7 demo使用方法
![image](https://github.com/MiEcosystem/NewXmPluginSDK/blob/master/MiJiaAuthSDK/demo%20UI%E7%95%8C%E9%9D%A2.png)
如上图，需要在设备已经绑入到当前小米账号的情况下，将did填入上面的输入框中，点击去授权即可

### ChangeLog
#### version  1.0.1
修改了检测到没有米家app或者当时米家版本不支持的处理逻辑。不在进行toast提示。<br>
开发者可以自行根据IAuthMangerImpl.getInstance().init(AuthActivity.this);返回值进行判断，如果返回true则表示初始化成功，失败表示没有相应的米家app（这个是推荐使用的）。在调用的时候IAuthMangerImpl.getInstance().callAuth也可以根据返回值进行判断，建议尽量在初始化的时候就进行处理<br>

#### version  1.0.3
新增了对绑定设备授权的功能，以及其使用的一些code值<br>
更新了新的米家apk(#247),增加了对一些权限的校验等功能<br>


### 有问题可以联系:
renlei@xiaomi.com
 
 
