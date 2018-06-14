
package com.xiaomi.xmplugindemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.BaseDevice.StateChangedListener;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends XmPluginBaseActivity implements StateChangedListener {
    static final int REQUEST_MENUS = 1;
    static final int MSG_SUB_PROPERTIES = 1;
    private static final String IS_FIRST_OPEN = "isFirstOpen";
    private static final String PREF_NAME = "prefName";
    private TextView mInfoView;
    private boolean mIsResume;
    private MyHandler mHandler;
    private DemoDevice mDevice;

    /**
     * 处理订阅属性变化，每次只维持3分钟订阅事件
     */
    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> mMainActivity;

        MyHandler(MainActivity activity) {
            mMainActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mMainActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_SUB_PROPERTIES:
                        if (activity.mIsResume) {
                            activity.mDevice.subscribeProperty(DemoDevice.PROPERTIES, null);
                            sendEmptyMessageDelayed(MSG_SUB_PROPERTIES, 3 * 60000);
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIsFirstOpen();
        TextView version = (TextView) findViewById(R.id.version);
        version.setText("version:" + mPluginPackage.packageVersion);
        TextView subTitleView = ((TextView) findViewById(R.id.sub_title_bar_title));
        mInfoView = (TextView) findViewById(R.id.info);

        mHandler = new MyHandler(this);

        // 初始化device
        mDevice = DemoDevice.getDevice(mDeviceStat);
        if (mDevice.isSubDevice()) {
            subTitleView.setText("子设备");
            subTitleView.setVisibility(View.VISIBLE);
        }

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        // 通过mDevice除了可以获取设备名称，还可以获取其他信息，比如mac、model、did等
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDevice.getName());

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 右上角菜单按钮显示小红点
        findViewById(R.id.title_bar_redpoint).setVisibility(View.VISIBLE);

        // 打开更多菜单页面，第一页插件自定义，然后跳转到公共页面
        findViewById(R.id.title_bar_more).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openMoreMenuNew();
            }
        });

        // 打开分享
        View shareView = findViewById(R.id.title_bar_share);
        if (mDevice.isBinded2()) {
            shareView.setVisibility(View.VISIBLE);
            shareView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHostActivity.openShareActivity();
                }
            });
        } else {
            shareView.setVisibility(View.GONE);
        }

        // 简单演示如何向设备发送RPC请求
        findViewById(R.id.control).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent, ControlActivity.class.getName());
            }
        });

        // 在通用设置菜单中配置了安全设置后，每次进入的时候弹出密码输入框
        mHostActivity.enableVerifyPincode();

        mHostActivity.enableAd();
    }

    private void checkIsFirstOpen() {
//        if (getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(IS_FIRST_OPEN, true)) {
//            if (XmPluginHostApi.instance().getApiLevel() >= 48) {
                mHostActivity.showUserLicenseDialog("小米用户协议", "用户协议", Html.fromHtml(getString(R.string.user_license_new)),
                        "隐私条款", Html.fromHtml(getString(R.string.user_license_new)), new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(IS_FIRST_OPEN, false).apply();
                            }
                        });
//            }
            //使用小米协议
//            if (XmPluginHostApi.instance().getApiLevel() >= 56) {
//                mHostActivity.showUseDefaultLicenseDialog("小米用户协议", new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(IS_FIRST_OPEN, true).apply();
//                    }
//                });
//            }
//        }
    }

    private void openMoreMenuNew() {
        ArrayList<IXmPluginHostActivity.MenuItemBase> menus = new ArrayList<>();

        // 插件自定义菜单，可以在onActivityResult(int requestCode, int resultCode, Intent data) 中接收用户点击的菜单项，String result = data.getStringExtra("menu");
        IXmPluginHostActivity.StringMenuItem stringMenuItem = new IXmPluginHostActivity.StringMenuItem();
        stringMenuItem.name = "test string menu";
        menus.add(stringMenuItem);

        // 带开关按钮的菜单，可以自动调用设备rpc
        IXmPluginHostActivity.SlideBtnMenuItem slideBtnMenuItem = new IXmPluginHostActivity.SlideBtnMenuItem();
        slideBtnMenuItem.name = "开关灯";
        slideBtnMenuItem.isOn = mDevice.getRgb() > 0;
        slideBtnMenuItem.onMethod = "set_rgb";
        JSONArray onParams = new JSONArray();
        onParams.put(0xffffff);
        slideBtnMenuItem.onParams = onParams.toString();
        slideBtnMenuItem.offMethod = "set_rgb";
        JSONArray offParams = new JSONArray();
        offParams.put(0);
        slideBtnMenuItem.offParams = offParams.toString();
        menus.add(slideBtnMenuItem);

        // 跳转到插件下一个activity的菜单
        IXmPluginHostActivity.IntentMenuItem intentMenuItem = new IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "透明titlebar";
        intentMenuItem.intent = mHostActivity.getActivityIntent(null, TransparentActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "Dialog";
        intentMenuItem.intent = mHostActivity.getActivityIntent(null, DiaglogActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "分享";
        intentMenuItem.intent = mHostActivity.getActivityIntent(null, ShareActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "ApiDemo";
        intentMenuItem.intent = mHostActivity.getActivityIntent(null, ApiDemosActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "测试用例";
        intentMenuItem.intent = mHostActivity.getActivityIntent(null, TestCaseActivity.class.getName());
        menus.add(intentMenuItem);

        intentMenuItem = new IXmPluginHostActivity.IntentMenuItem();
        intentMenuItem.name = "Api自动测试";
        intentMenuItem.intent = mHostActivity.getActivityIntent(null, ApiTestActivity.class.getName());
        menus.add(intentMenuItem);

        Intent intent = new Intent();
        intent.putExtra("security_setting_enable", true);

        //在通用设置中显示解除协议选项
        Intent commonSetting = new Intent();
        commonSetting.putExtra("enableRemoveLicense", true);
        //此项置成true，使用小米默认协议
//        commonSetting.putExtra("useDefaultLicense", true);
        commonSetting.putExtra("licenseContent", Html.fromHtml(getString(R.string.user_license_new)));
        commonSetting.putExtra("privacyContent", Html.fromHtml(getString(R.string.user_license_new)));

        String rootPath = activity().getFilesDir().getAbsolutePath();
        File licenseFile = new File(rootPath, "license.html");
        File privacyFile = new File(rootPath, mDevice.getModel() + "_privacy.html");
//        if (!licenseFile.exists() && !privacyFile.exists()) {
        FileOutputStream fos1 = null;
        FileOutputStream fos2 = null;
        InputStream is = null;
        try {
            fos1 = openFileOutput(licenseFile.getName(), Context.MODE_PRIVATE);
            fos2 = openFileOutput(privacyFile.getName(), Context.MODE_PRIVATE);
            is = getAssets().open("user_license.html");
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            fos1.write(buffer);
            fos1.flush();
            fos2.write(buffer);
            fos2.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.d("wangyh", "openMoreMenuNew: ");
            if (is != null && fos1 != null && fos2 != null) {
                try {
                    is.close();
                    fos1.close();
                    fos2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        }
        commonSetting.putExtra("licenseContentUri", licenseFile.getAbsolutePath());
        commonSetting.putExtra("privacyContentUri", licenseFile.getAbsolutePath());

        mHostActivity.openMoreMenu2(menus, true, REQUEST_MENUS, intent, commonSetting);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 自定义菜单返回
            if (requestCode == REQUEST_MENUS && data != null) {
                String removedLicense = data.getStringExtra("result_data");
                if (removedLicense != null && removedLicense.equals("removedLicense")) {
                    Toast.makeText(this, "removedLicense", Toast.LENGTH_SHORT).show();
                    getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(IS_FIRST_OPEN, true).apply();
                    finish();
                }
                String selectMenu = data.getStringExtra("menu");
                if (TextUtils.isEmpty(selectMenu)) {
                    return;
                }
                if (selectMenu.equals("test string menu")) {
                    // 分享微博，微信，米聊
                    Toast.makeText(activity(), selectMenu, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsResume = true;
        mHandler.sendEmptyMessage(MSG_SUB_PROPERTIES);
        mDevice.addStateChangedListener(this);
        mDevice.updateDeviceStatus();
        mDevice.updateProperty(DemoDevice.PROPERTIES);
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDevice.getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsResume = false;
        mDevice.removeStateChangedListener(this);
        mHandler.removeMessages(MSG_SUB_PROPERTIES);
    }

    @Override
    public void onStateChanged(BaseDevice device) {
        if (!mIsResume)
            return;
        String info = "温度:" + mDevice.getTemperature() + " 湿度:" + mDevice.getHumidity();
        mInfoView.setText(info);
    }
}
