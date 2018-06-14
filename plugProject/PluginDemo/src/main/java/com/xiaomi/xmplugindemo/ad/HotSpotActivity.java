package com.xiaomi.xmplugindemo.ad;

import android.os.Bundle;
import android.view.View;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.xmplugindemo.R;

/**
 * 热区广告
 * <p>
 * Created by wangyeming on 2016/12/1.
 */
public class HotSpotActivity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_hot_spot);

        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.ad_hot_spot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHostActivity.clickHotSpotAd("custom");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //上报广告的展示事件
    }
}
