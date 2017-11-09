package com.xiaomi.xmplugindemo.ad;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.xmplugindemo.R;

/**
 * 横切公告Demo
 */
public class NoticeAdActicity extends XmPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_notice);
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        findViewById(R.id.title_bar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final LinearLayout vNoticeGroup = (LinearLayout) findViewById(R.id.ad_notice);

        findViewById(R.id.ad_notice_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.showNoticeAd(vNoticeGroup);
            }
        });

    }
}
