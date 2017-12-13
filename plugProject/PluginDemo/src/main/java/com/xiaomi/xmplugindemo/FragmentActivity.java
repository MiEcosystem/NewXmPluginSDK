
package com.xiaomi.xmplugindemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

public class FragmentActivity extends XmPluginBaseActivity {
    DemoDevice mDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        
        mDevice = DemoDevice.getDevice(mDeviceStat);
        
//        SerializableData sData = (SerializableData) getIntent().getSerializableExtra("sData");
        ParcelData pData = (ParcelData) getIntent().getParcelableExtra("pData");
    
        
        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        ((TextView) findViewById(R.id.title_bar_title)).setText("测试Fragment");
        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        FragmentPage fragmentPage = new FragmentPage();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragmentPage, "first_Fragment");
        fragmentTransaction.commit();
    }

    @Override
    public void finish() {
        // 必须通过activity().setResult
        activity().setResult(RESULT_OK);
        super.finish();
    }
}
