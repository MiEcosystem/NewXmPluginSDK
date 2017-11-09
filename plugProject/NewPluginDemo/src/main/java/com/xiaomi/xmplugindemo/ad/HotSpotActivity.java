package com.xiaomi.xmplugindemo.ad;

import android.os.Bundle;
import android.view.View;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.xmplugindemo.R;

/**
 * 热区广告
 *
 * Created by wangyeming on 2016/12/1.
 */
public class HotSpotActivity extends XmPluginBaseActivity {

    private static final String AD_GID = "97";
    private static final String AD_ID = "1";

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
                mHostActivity.openShopActivity(AD_GID);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //上报广告的展示事件
        mHostActivity.reportHotSpotAdShow(AD_GID, AD_ID);
    }
}
