
package com.xiaomi.xmplugindemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

public class BlueDemoMainActivity extends XmPluginBaseActivity {

    boolean mIsResume;
    LayoutInflater mLayoutInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_main);

        mLayoutInflater = LayoutInflater.from(activity());
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        ((TextView) findViewById(R.id.title_bar_title)).setText(mDeviceStat.name);
        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.title_bar_redpoint).setVisibility(View.VISIBLE);

        // 打开分享
        View shareView = findViewById(R.id.title_bar_share);
        shareView.setVisibility(View.VISIBLE);
        shareView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openShareActivity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsResume = false;
    }

}
