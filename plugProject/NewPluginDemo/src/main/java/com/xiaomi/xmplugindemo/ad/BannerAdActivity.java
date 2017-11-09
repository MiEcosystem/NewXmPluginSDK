package com.xiaomi.xmplugindemo.ad;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.xmplugindemo.R;

/**
 * Banner广告Demo
 */
public class BannerAdActivity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_banner);
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final LinearLayout vBannerGroup = (LinearLayout) findViewById(R.id.ad_banner);

        findViewById(R.id.ad_banner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.showBannerAd(vBannerGroup);
            }
        });


    }
}
