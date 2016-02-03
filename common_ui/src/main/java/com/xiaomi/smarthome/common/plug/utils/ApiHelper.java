/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaomi.smarthome.common.plug.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.MediaStore.MediaColumns;
import android.view.View;

import java.lang.reflect.Field;

public class ApiHelper {
    public static interface VERSION_CODES {
        // These value are copied from Build.VERSION_CODES
        public static final int FROYO = 8;
        public static final int GINGERBREAD = 9;
        public static final int GINGERBREAD_MR1 = 10;
        public static final int HONEYCOMB = 11;
        public static final int HONEYCOMB_MR1 = 12;
        public static final int HONEYCOMB_MR2 = 13;
        public static final int ICE_CREAM_SANDWICH = 14;
        public static final int ICE_CREAM_SANDWICH_MR1 = 15;
        public static final int JELLY_BEAN = 16;
        public static final int JELLY_BEAN_MR1 = 17;
    }

    public static final boolean HAS_ANDROID_ANIMATION =
            Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
            
            public static final boolean HAS_NOTIFICATION_BUILDER = Build.VERSION.SDK_INT > VERSION_CODES.GINGERBREAD_MR1;

    public static final boolean HAS_ANDROID_CLEAR_TASK =
            Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;

    public static final boolean HAS_NEW_DISPLAY =
            Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2;

    public static final boolean HAS_VIEW_SYSTEM_UI_FLAG_LAYOUT_STABLE =
            Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN && hasField(View.class,
                    "SYSTEM_UI_FLAG_LAYOUT_STABLE");

    public static final boolean HAS_VIEW_SYSTEM_UI_FLAG_HIDE_NAVIGATION =
            Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH ? hasField(
                    MediaColumns.class, "WIDTH") : false;

    public static final boolean HAS_SET_DEFALT_BUFFER_SIZE =
            Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1 ? hasMethod(
                    "android.graphics.SurfaceTexture", "setDefaultBufferSize",
                    int.class, int.class) : false;

    public static final boolean HAS_RELEASE_SURFACE_TEXTURE =
            Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH ? hasMethod(
                    "android.graphics.SurfaceTexture", "release") : false;

    public static final boolean HAS_SET_SYSTEM_UI_VISIBILITY = Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB ? hasMethod(
            View.class, "setSystemUiVisibility", int.class) : false;

    public static final boolean HAS_EDITOR_APPLY = Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD ? hasMethod(
            SharedPreferences.Editor.class, "apply", (Class<?>[]) null) : false;

    public static final boolean HAS_GET_CAMERA_DISABLED = Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH ?
            hasMethod(DevicePolicyManager.class, "getCameraDisabled", ComponentName.class)
            : false;

    public static final boolean HAS_MULTICORE_CHIPS =
            Runtime.getRuntime().availableProcessors() > 1; // 只是判断当前可用cpu个数，目前够用；后期有需要可以读取配置信息

    public static int getIntFieldIfExists(Class<?> klass, String fieldName,
            Class<?> obj, int defaultVal) {
        try {
            Field f = klass.getDeclaredField(fieldName);
            return f.getInt(obj);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private static boolean hasField(Class<?> klass, String fieldName) {
        try {
            klass.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private static boolean hasMethod(String className, String methodName,
            Class<?>... parameterTypes) {
        try {
            Class<?> klass = Class.forName(className);
            klass.getDeclaredMethod(methodName, parameterTypes);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean hasMethod(
            Class<?> klass, String methodName, Class<?>... paramTypes) {
        try {
            klass.getDeclaredMethod(methodName, paramTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
