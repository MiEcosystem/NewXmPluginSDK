package com.xiaomi.xmplugindemo.ad;

import android.os.Bundle;
import android.view.View;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.xmplugindemo.R;

/**
 * 底部弹窗广告Demo
 */
public class BottomPopAdActivity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_bottom_pop);
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.ad_bottom_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.showBottomPopAd();
            }
        });
    }
}
