package com.xiaomi.xmplugindemo.ad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.xmplugindemo.R;

/**
 * 插件广告位demo展示
 */
public class PluginAdActivity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_ad);

        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.float_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), PopAdActivity.class.getName());
            }
        });

        findViewById(R.id.alert_dialog_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), BottomPopAdActivity.class.getName());
            }
        });

        findViewById(R.id.banner_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), BannerAdActivity.class.getName());
            }
        });

        findViewById(R.id.notice_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), NoticeAdActicity.class.getName());
            }
        });

        findViewById(R.id.hot_spot_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(), HotSpotActivity.class.getName());
            }
        });

    }
}
