
package com.xiaomi.xmplugindemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.common.ui.dialog.MLAlertDialog;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;
import com.xiaomi.xmplugindemo.ad.PluginAdActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApiDemosActivity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_api_demos);

        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.title_bar_title)).setText("ApiDemos");

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        findViewById(R.id.webview).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView("http://home.mi.com", "MiHome");
            }
        });

        findViewById(R.id.dialog).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLAlertDialog dialog = new MLAlertDialog.Builder(ApiDemosActivity.this).setMessage(
                        "测试Dialog").setPositiveButton("确定", null).create();
                // XmPluginHostApi.instance().setWindowAnimations(dialog);
                dialog.show();
            }
        });

        findViewById(R.id.openDevice).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final List<DeviceStat> deviceList = XmPluginHostApi.instance().getDeviceList();
                String[] items = new String[deviceList.size()];
                for (int i = 0; i < deviceList.size(); i++) {
                    items[i] = deviceList.get(i).name;
                }
                new MLAlertDialog.Builder(ApiDemosActivity.this).setTitle(
                        "选择设备").setItems(items, new MLAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHostActivity.openDevice(deviceList.get(which).did, null);
                        // XmPluginHostApi.instance().addToLauncher(mPluginPackage,deviceList.get(which).did,
                        // null);
                    }
                }).show();
            }
        });

        findViewById(R.id.testFragment).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // SerializableData sData = new SerializableData();
                // sData.id = 5;
                // intent.putExtra("sData", sData);
                ParcelData pData = new ParcelData();
                pData.mData = 2;
                intent.putExtra("pData", pData);
                startActivity(intent, FragmentActivity.class.getName());
            }
        });

        findViewById(R.id.test_set_timer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startSetTimerList(mDeviceStat.did, "set_rgb", String.valueOf(0x00ffffff),
                        "set_rgb", String.valueOf(0x00000000), mDeviceStat.did, "RGB灯定时器", "RGB灯定时器");
            }
        });
        findViewById(R.id.test_scene).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> menus = new ArrayList<String>();
                ArrayList<Intent> intents = new ArrayList<Intent>();
                if (XmPluginHostApi.instance().getApiLevel() > 5) {
                    Intent sceneIntent = new Intent();
                    sceneIntent.putExtra(
                            IXmPluginHostActivity.KEY_INTENT_TARGET_ACTIVITY_IN_HOST,
                            IXmPluginHostActivity.TARGET_ACTIVITY_IN_HOST_DEVICE_SCENE);

                    sceneIntent.putExtra("menu", "智能场景");
                    sceneIntent.putExtra("device_id", mDeviceStat.did);
                    intents.add(sceneIntent);
                }
                mHostActivity.openMoreMenu(menus, intents, true, 1);
            }
        });
        
        findViewById(R.id.open_shop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://home.mi.com/shop/search?action=check&keyword=小米空气净化器2&source=com.xiaomi.smarthome​");
                XmPluginHostApi.instance().gotoPage(activity(), mPluginPackage, uri, null);
            }
        });
        
        findViewById(R.id.activity_finish).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                finishParent(null);
            }
        });

        findViewById(R.id.activity_set_timer).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = XmPluginHostApi.instance().getBroadCastIntent(mDeviceStat);
                PendingIntent pi = PendingIntent.getBroadcast(
                        getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.add(Calendar.SECOND, 5);
                AlarmManager alarmManager=(AlarmManager)activity().getSystemService(Activity.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);// 设置闹钟
                Toast.makeText(activity(),"定时设置成功，5s后重新启动插件",Toast.LENGTH_SHORT).show();
                finishParent(null);

            }
        });

        findViewById(R.id.activity_show_ad).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), PluginAdActivity.class.getName());
            }
        });
    }

}
