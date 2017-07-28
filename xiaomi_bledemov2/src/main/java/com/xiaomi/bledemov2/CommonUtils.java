package com.xiaomi.bledemov2;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liwentian on 2016/10/13.
 */

public class CommonUtils {

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
