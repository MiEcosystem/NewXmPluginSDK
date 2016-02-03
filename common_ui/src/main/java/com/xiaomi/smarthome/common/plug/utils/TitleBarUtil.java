
package com.xiaomi.smarthome.common.plug.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class TitleBarUtil {
    public static boolean TRANSLUCENT_STATUS_ENABLED = false;

    public static void enableTranslucentStatus(Window window) {
        if (Build.VERSION.SDK_INT < 19) {
            TRANSLUCENT_STATUS_ENABLED = false;
            return;
        }

        try {

            Class clazz = window.getClass();
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class
                    .forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams
                    .getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
                    int.class);
            // 状态栏透明
            // extraFlagField.invoke(window, tranceFlag, tranceFlag);
            // //状态栏darkMode
            // extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            // 即透明又darkMode
            extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag
                    | darkModeFlag);

            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            TRANSLUCENT_STATUS_ENABLED = true;
        } catch (Exception e) {
            TRANSLUCENT_STATUS_ENABLED = false;
        }
    }

    public static void enableWhiteTranslucentStatus(Window window) {
        if (Build.VERSION.SDK_INT < 19) {
            TRANSLUCENT_STATUS_ENABLED = false;
            return;
        }

        try {
            Class clazz = window.getClass();
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class
                    .forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams
                    .getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
                    int.class);
            // 状态栏透明
            // extraFlagField.invoke(window, tranceFlag, tranceFlag);
            // //状态栏darkMode
            // extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            // 即透明又darkMode
            extraFlagField.invoke(window, tranceFlag, tranceFlag
                    | darkModeFlag);

            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            TRANSLUCENT_STATUS_ENABLED = true;
        } catch (Exception e) {
            TRANSLUCENT_STATUS_ENABLED = false;
        }
    }
    
    public static void setTitleBarPadding(int topPadding,View titleBar) {
    	if (!TRANSLUCENT_STATUS_ENABLED) {
            return;
        }

        if (titleBar == null)
            return;
//        titleBar.getLayoutParams().height += topPadding;
        titleBar.setPadding(0, topPadding, 0, 0);
        titleBar.setLayoutParams(titleBar.getLayoutParams());
    }
}
