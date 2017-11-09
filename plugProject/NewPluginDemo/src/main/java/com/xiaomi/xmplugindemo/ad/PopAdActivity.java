package com.xiaomi.xmplugindemo.ad;

import android.os.Bundle;
import android.view.View;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.xmplugindemo.R;

/**
 * 插屏弹窗广告Demo
 */
public class PopAdActivity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_pop);
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.ad_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.showPopAd();
            }
        });
    }
}
