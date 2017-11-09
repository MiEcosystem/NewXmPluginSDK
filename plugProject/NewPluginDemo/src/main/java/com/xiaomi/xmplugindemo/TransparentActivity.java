
package com.xiaomi.xmplugindemo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

public class TransparentActivity extends XmPluginBaseActivity {
    private boolean misWhite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        mHostActivity.enableWhiteTranslucentStatus();
        misWhite = true;
        ((TextView) findViewById(R.id.title_bar_title)).setText("透明title bar");
        ((TextView) findViewById(R.id.sub_title_bar_title)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.sub_title_bar_title)).setText("子title name");
        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.title_bar_share).setVisibility(View.VISIBLE);

        findViewById(R.id.title_bar_more).setVisibility(View.VISIBLE);
        findViewById(R.id.test).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (misWhite) {
                    misWhite = false;
                    mHostActivity.enableBlackTranslucentStatus();
                } else {
                    misWhite = true;
                    mHostActivity.enableWhiteTranslucentStatus();
                }
            }
        });
    }

}
