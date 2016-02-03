
package com.xiaomi.smarthome.common.plug.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DisplayUtils {
    static final String TAG = DisplayUtils.class.getSimpleName();

    public static Point getDisplaySize(Activity activity) {
        Point point = new Point();
        if (ApiHelper.HAS_NEW_DISPLAY) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getSize(point);
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(final Activity activity, final float dpValue) {
        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float scale = metrics.density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(final Context context, final float dpValue) {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float scale = metrics.density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
//    public static int dip2px(final float dpValue) {
//        return dip2px(GlobalData.app(), dpValue);
//    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(final Activity activity, final float pxValue) {
        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float scale = metrics.density;
        return (int) ((pxValue / scale) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(final Context context, final float pxValue) {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float scale = metrics.density;
        return (int) ((pxValue / scale) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
//    public static int px2dip(final float pxValue) {
//        return px2dip(GlobalData.app(), pxValue);
//    }

    public static void overridingPendingAnim(Activity activity, int inAnim, int outAnim) {
        if (activity == null) {
            return;
        }

        Log.d(TAG, "OverridePending:Activity=" + activity);

        activity.overridePendingTransition(inAnim, outAnim);
    }

    public static Bitmap getBlurBmp(Context context, Bitmap srcBmp, float scale) {
        try {
            Bitmap dstBmp = Bitmap.createScaledBitmap(srcBmp, (int) (srcBmp.getWidth() * scale), (int) (srcBmp.getHeight() * scale), false);

            Class<?> cScreenShotUtils = Class.forName("miui.util.ScreenshotUtils");
            Method mGetBlurBackground = cScreenShotUtils.getMethod("getBlurBackground", Bitmap.class, Bitmap.class);
            Bitmap bluredBmp = (Bitmap) mGetBlurBackground.invoke(cScreenShotUtils, dstBmp, null);
//            srcBmp.recycle();
            dstBmp.recycle();
            return bluredBmp;
        } catch (Exception e) {
        }

        return null;
    }

    public static Bitmap getLockscreenBmp(Context context) {
        try {
            Class<?> cThemeResources = Class.forName("miui.content.res.ThemeResources");
            Method mGetLockWallpaperCache = null;
            mGetLockWallpaperCache = cThemeResources.getDeclaredMethod("getLockWallpaperCache", Context.class);
            BitmapDrawable backgound = (BitmapDrawable) mGetLockWallpaperCache.invoke(cThemeResources, context);
            Bitmap srcBmp = backgound.getBitmap();
            return srcBmp;
        } catch (Exception e) {
        }
        return null;
    }

    public static void setTransparentStatusBar(Window targetWindow) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }

        try {
            Class<? extends Window> clazz = targetWindow.getClass();
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class<?> layoutParams = Class
                    .forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams
                    .getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
                    int.class);
            // 状态栏透明
//            extraFlagField.invoke(targetWindow, tranceFlag, tranceFlag);
            // //状态栏darkMode
            // extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            // 即透明又darkMode
            extraFlagField.invoke(targetWindow, tranceFlag, tranceFlag |
                    darkModeFlag);

            targetWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } catch (Exception e) {
        }
    }
    
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}
