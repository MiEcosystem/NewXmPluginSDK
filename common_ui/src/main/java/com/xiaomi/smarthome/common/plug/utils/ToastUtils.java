package com.xiaomi.smarthome.common.plug.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    /**
     * 插件中调用显示 toast
     *
     * @param context    插件 activity 的 context。
     * @param message    要显示的字符串
     * @param showLength 显示时长  {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
     * @return 显示的 toast
     */
    public static Toast showToast(Context context, String message, int showLength) {
        Toast toast = Toast.makeText(context.getApplicationContext(), message, showLength);
        toast.show();
        return toast;
    }

    /**
     * 插件中调用显示 toast
     *
     * @param context    插件 activity 的 context。
     * @param resId      要显示的字符串
     * @param showLength 显示时长  {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
     * @return 显示的 toast
     */
    public static Toast showToast(Context context, int resId, int showLength) {
        return showToast(context, context.getString(resId), showLength);
    }

    /**
     * 插件中调用显示 toast，默认时长Toast.LENGTH_LONG
     *
     * @param context 插件 activity 的 context。
     * @param message 要显示的字符串
     * @return 显示的 toast
     */
    public static Toast showToast(Context context, String message) {
        return showToast(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 插件中调用显示 toast，默认时长Toast.LENGTH_LONG
     *
     * @param context 插件 activity 的 context。
     * @param resId   要显示的字符串
     * @return 显示的 toast
     */
    public static Toast showToast(Context context, int resId) {
        return showToast(context, resId, Toast.LENGTH_LONG);
    }

}
