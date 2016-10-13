package com.xiaomi.bledemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.BaseDevice;
import com.xiaomi.smarthome.device.api.BaseDevice.StateChangedListener;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceUpdateInfo;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity.*;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends XmPluginBaseActivity {

    Device mDevice;

    View mNewFirmView;

    TextView mTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewFirmView = findViewById(R.id.title_bar_redpoint);
        mTitleView = ((TextView) findViewById(R.id.title_bar_title));
        mTitleView.setText(R.string.title);

        // 初始化device
        mDevice = Device.getDevice(mDeviceStat);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.title_bar_more).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                List<MenuItemBase> menus = new ArrayList<MenuItemBase>();
                menus.add(BleMenuItem.newUpgraderItem(new MyUpgrader()));
                hostActivity().openMoreMenu((ArrayList<MenuItemBase>) menus, true, 0);
            }
        });

        // 打开分享
        View shareView = findViewById(R.id.title_bar_share);
        if (mDevice.isOwner()) {
            shareView.setVisibility(View.VISIBLE);
            shareView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHostActivity.openShareActivity();
                }
            });
        } else {
            shareView.setVisibility(View.GONE);
        }

        Button btnMenu = (Button) findViewById(R.id.menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(), MenuActivity.class.getName());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void handleMessage(Message msg) {
    }
}

