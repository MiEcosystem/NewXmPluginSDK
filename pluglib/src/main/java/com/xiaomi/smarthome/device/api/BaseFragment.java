
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
        initilXmPlugActivity(activity);
    }

    void initilXmPlugActivity(Activity activity) {
        Field field = null;
        try {
            field = activity.getClass().getDeclaredField("mXmPluginActivity");
        } catch (NoSuchFieldException e) {
        }
        if (field == null) {
            Class superClass = activity.getClass().getSuperclass();
            if (superClass != null) {
                try {
                    field = superClass.getDeclaredField("mXmPluginActivity");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        if(field==null){
            return;
        }
        try {
            field.setAccessible(true);
            mXmPluginBaseActivity = (XmPluginBaseActivity) field.get(activity);
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
