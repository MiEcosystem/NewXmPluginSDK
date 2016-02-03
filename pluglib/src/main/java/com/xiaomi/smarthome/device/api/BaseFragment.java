
package com.xiaomi.smarthome.device.api;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.reflect.Field;

/**
 * ApiLevel:2 
 * 插件对Fragment支持,所有Fragment必须继承BaseFragment
 */
public class BaseFragment extends Fragment {
    XmPluginBaseActivity mXmPluginBaseActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initilXmPlugActivity();
    }

    void initilXmPlugActivity() {
        try {
            Field field = getActivity().getClass().getDeclaredField("mXmPluginActivity");
            field.setAccessible(true);
            mXmPluginBaseActivity = (XmPluginBaseActivity) field.get(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XmPluginBaseActivity xmPluginActivity() {
        return mXmPluginBaseActivity;
    }
    
    

    // 插件内启动activity
    public void startActivityForResult(Intent intent, String className,
            int requestCode) {
        if (mXmPluginBaseActivity != null) {
            mXmPluginBaseActivity.startActivityForResult(intent, className, requestCode);
        }
    }
}
