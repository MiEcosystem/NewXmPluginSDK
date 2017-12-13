
package com.xiaomi.xmplugindemo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

public class SettingActivity extends XmPluginBaseActivity {
    DemoDevice mDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        mDevice = DemoDevice.getDevice(mDeviceStat);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        ((TextView) findViewById(R.id.title_bar_title)).setText("设置页面");
        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mHostActivity.overridePendingTransition(IXmPluginHostActivity.ANIM_SLIDE_IN_BOTTOM, IXmPluginHostActivity.ANIM_SLIDE_OUT_TOP);
            }
        });

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHostActivity.overridePendingTransition(IXmPluginHostActivity.ANIM_SLIDE_IN_BOTTOM, IXmPluginHostActivity.ANIM_SLIDE_OUT_TOP);
    }
}
