
package com.xiaomi.xmplugindemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.common.ui.dialog.MLAlertDialog;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity.IntentMenuItem;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity.SlideBtnMenuItem;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity.MenuItemBase;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity.StringMenuItem;

import org.json.JSONArray;

import java.util.ArrayList;

public class MoreActivity extends XmPluginBaseActivity {
    static final int REQUEST_MENUS = 1;
    DemoDevice mDevice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        
        // 初始化device
        mDevice = DemoDevice.getDevice(mDeviceStat);
        
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        ((TextView) findViewById(R.id.title_bar_title)).setText("更多");
        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.hello).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(null, WelcomeActivity.class.getName());
            }
        });
        findViewById(R.id.transparent_titlerar).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(null, TransparentActivity.class.getName());
            }
        });
        findViewById(R.id.dialog).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(null, DiaglogActivity.class.getName());
            }
        });
        findViewById(R.id.share).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(null, ShareActivity.class.getName());
            }
        });
        findViewById(R.id.setting).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(null, SettingActivity.class.getName());
                mHostActivity.overridePendingTransition(IXmPluginHostActivity.ANIM_SLIDE_IN_TOP,
                        IXmPluginHostActivity.ANIM_SLIDE_OUT_BOTTOM);
            }
        });
        findViewById(R.id.others).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(null, ApiDemosActivity.class.getName());
            }
        });
        findViewById(R.id.test_case).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(null, TestCaseActivity.class.getName());
            }
        });

        findViewById(R.id.base_setting).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // // 点击菜单返回界面，需要在onActivityResult接收参数
                // ArrayList<String> menus = new ArrayList<String>();
                // menus.add("ApiDemo-MenuOnResult-分享");
                //
                // ArrayList<Intent> intents = new ArrayList<Intent>();
                //
                // Intent welcomeIntent = mHostActivity.getActivityIntent(null,
                // WelcomeActivity.class.getName());
                // welcomeIntent.putExtra("menu", "你好, 开发者");
                // intents.add(welcomeIntent);
                //
                // Intent intent = mHostActivity.getActivityIntent(null,
                // SettingActivity.class.getName());
                // intent.putExtra("menu", "设置");
                // intents.add(intent);
                //
                // Intent apiDemosIntent = mHostActivity.getActivityIntent(null,
                // ApiDemosActivity.class.getName());
                // apiDemosIntent.putExtra("menu", "ApiDemo-Others");
                // intents.add(apiDemosIntent);
                //
                // // Intent mihomeIntent = new Intent();
                // // mihomeIntent.setClassName("com.xiaomi.smarthome",
                // //
                // "com.xiaomi.smarthome.framework.webview.CommonWebViewActivity");
                // // mihomeIntent.putExtra("url", "http://home.mi.com");
                // // mihomeIntent.putExtra("title", "MiHome");
                // // mihomeIntent.putExtra("menu", "MiHome");
                // // intents.add(mihomeIntent);
                //
                // String did, ArrayList<MenuItemBase> menus,
                // boolean useDefault, int requestCode
                // 设置自定义菜单
                ArrayList<MenuItemBase> menus = new ArrayList<IXmPluginHostActivity.MenuItemBase>();

                //
                StringMenuItem stringMenuItem = new StringMenuItem();
                stringMenuItem.name = "test string menu";
                menus.add(stringMenuItem);
                

                //
                IntentMenuItem intentMenuItem = new IntentMenuItem();
                intentMenuItem.name = "test intent menu";
                intentMenuItem.intent = mHostActivity.getActivityIntent(null,
                        ApiDemosActivity.class.getName());
                menus.add(intentMenuItem);
                
                //
                SlideBtnMenuItem slideBtnMenuItem = new SlideBtnMenuItem();
                slideBtnMenuItem.name = "test slide menu";
                slideBtnMenuItem.isOn = mDevice.getRgb()>0;
                slideBtnMenuItem.onMethod = "set_rgb";
                JSONArray onparams = new JSONArray();
                onparams.put(0xffffff);
                slideBtnMenuItem.onParams = onparams.toString();
                slideBtnMenuItem.offMethod = "set_rgb";
                JSONArray offparams = new JSONArray();
                offparams.put(0);
                slideBtnMenuItem.offParams = offparams.toString();
                menus.add(slideBtnMenuItem);
                

                mHostActivity.openMoreMenu(menus, true,
                        REQUEST_MENUS);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 自定义菜单返回
            if (requestCode == REQUEST_MENUS && data != null) {
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
}
