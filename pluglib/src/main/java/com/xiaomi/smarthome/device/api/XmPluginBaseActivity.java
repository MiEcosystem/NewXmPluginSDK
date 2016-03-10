
package com.xiaomi.smarthome.device.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.xiaomi.plugin.core.XmPluginPackage;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 免安装插件activity基类 所有与activity和context相关的调用，请使用方法activity()获取，比如Toast弹出和Dialog创建
 * 该类只提供宿主activity中调用方法
 */
public class XmPluginBaseActivity extends FragmentActivity implements IXmPluginActivity {
    private static final String TAG = "XmPluginBaseActivity";
    
    public static String FINISH_TAG = "xmplugin_finish";
    public static String LAST_FINISH_ACTIVITY = "xmplugin_last_finish_activity";
    public static int START_ACTIVITY_TAG = 10000;

    protected IXmPluginHostActivity mHostActivity;
    // 宿主Activity
    protected FragmentActivity mMainActivity;

    // 当前插件package信息
    protected XmPluginPackage mPluginPackage;
    // 提供设备Device数据
    protected DeviceStat mDeviceStat;

    // 弱引用XmPluginBaseActivity对象，推荐使用该机制
    public Handler mHandler;

    private static class ActivityHandler extends Handler {

        WeakReference<XmPluginBaseActivity> mRefActivity;

        private ActivityHandler(XmPluginBaseActivity activity) {
            mRefActivity = new WeakReference<XmPluginBaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mRefActivity != null) {
                XmPluginBaseActivity activity = mRefActivity.get();
                if (activity != null && !activity.isFinishing()) {
                    activity.handleMessage(msg);
                }
            }
        }
    }

    /**
     * 如果需要用到mHandler发送消息，需要子类重载该函数
     * 
     * @param msg
     */
    public void handleMessage(Message msg) {

    }

    // 判断是否是由宿主activity调用
    boolean isLocalLaunch() {
        return mMainActivity == null;
    }

    @Override
    // 在宿主activity中创建完成后，调用attach，传递宿主信息
    public void attach(IXmPluginHostActivity xmPluginHostActivity, XmPluginPackage pluginPackage,
            DeviceStat deviceStat) {
        mHostActivity = xmPluginHostActivity;
        mMainActivity = xmPluginHostActivity.activity();
        mPluginPackage = pluginPackage;
        mDeviceStat = deviceStat;
        
        attachBaseContext(mMainActivity);
    }

    // 获取宿主activity，提供Context环境，用于获取资源，获取service服务，创建Dialog等
    public Activity activity() {
        if (isLocalLaunch()) {
            return this;
        } else {
            return mMainActivity;
        }
    }

    public DeviceStat getDeviceStat() {
        return mDeviceStat;
    }
    
    /**ApiLevel:14
     * 按照调用栈一直回退finish，直到lastActivityClass为止,当lastActivityClass为null会一直回退到设备列表
     * @param lastActivityClass
     */
    public void finishParent(String lastActivityClass) {
        Intent data = new Intent();
        data.putExtra(FINISH_TAG, true);
        if (!TextUtils.isEmpty(lastActivityClass))
            data.putExtra(LAST_FINISH_ACTIVITY, lastActivityClass);
        activity().setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void setContentView(View view) {
        if (isLocalLaunch()) {
            super.setContentView(view);
        } else {
            mMainActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        if (isLocalLaunch()) {
            super.setContentView(view, params);
        } else {
            mMainActivity.setContentView(view, params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (isLocalLaunch()) {
            super.setContentView(layoutResID);
        } else {
            mMainActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        if (isLocalLaunch()) {
            super.addContentView(view, params);
        } else {
            mMainActivity.addContentView(view, params);
        }
    }

    @Override
    public Looper getMainLooper() {
        if (isLocalLaunch()) {
            return super.getMainLooper();
        } else {
            return mMainActivity.getMainLooper();
        }
    }

    @Override
    public View findViewById(int id) {
        if (isLocalLaunch()) {
            return super.findViewById(id);
        } else {
            return mMainActivity.findViewById(id);
        }
    }

    @Override
    public Intent getIntent() {
        if (isLocalLaunch()) {
            return super.getIntent();
        } else {
            return mMainActivity.getIntent();
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        if (isLocalLaunch()) {
            super.setIntent(newIntent);
        } else {
            mMainActivity.setIntent(newIntent);
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if (isLocalLaunch()) {
            return super.getClassLoader();
        } else {
            return mMainActivity.getClassLoader();
        }
    }

    @Override
    public Resources getResources() {
        if (isLocalLaunch()) {
            return super.getResources();
        } else {
            return mMainActivity.getResources();
        }
    }
    @Override
    public AssetManager getAssets() {
        if (isLocalLaunch()) {
            return super.getAssets();
        } else {
            return mMainActivity.getAssets();
        }
    }

    @Override
    public String getPackageName() {
        if (isLocalLaunch()) {
            return super.getPackageName();
        } else {
            if (mPluginPackage != null)
                return mPluginPackage.packageName;
            else
                return mMainActivity.getPackageName();
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if (isLocalLaunch()) {
            return super.getLayoutInflater();
        } else {
            return mMainActivity.getLayoutInflater();
        }
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (isLocalLaunch()) {
            return super.getMenuInflater();
        } else {
            return mMainActivity.getMenuInflater();
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (isLocalLaunch()) {
            return super.getSharedPreferences(name, mode);
        } else {
            return mMainActivity.getSharedPreferences(name, mode);
        }
    }

    @Override
    public Context getApplicationContext() {
        if (isLocalLaunch()) {
            return super.getApplicationContext();
        } else {
            return mMainActivity.getApplicationContext();
        }
    }

    @Override
    public WindowManager getWindowManager() {
        if (isLocalLaunch()) {
            return super.getWindowManager();
        } else {
            return mMainActivity.getWindowManager();
        }
    }

    @Override
    public Window getWindow() {
        if (isLocalLaunch()) {
            return super.getWindow();
        } else {
            return mMainActivity.getWindow();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (isLocalLaunch()) {
            return super.getSystemService(name);
        } else {
            return mMainActivity.getSystemService(name);
        }
    }

    @Override
    public void finish() {
        if (isLocalLaunch()) {
            super.finish();
        } else {
            mMainActivity.finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (isLocalLaunch()) {
            super.onCreate(savedInstanceState);
        }
        mHandler = new ActivityHandler(this);
    }

    @Override
    public void onBackPressed() {
        if (isLocalLaunch()) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isLocalLaunch()) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (data != null) {
            boolean isFinish = data.getBooleanExtra(FINISH_TAG, false);
            String activitName = this.getClass().getName();
            String lastActivity = data.getStringExtra(LAST_FINISH_ACTIVITY);
            if (isFinish && !activitName.equals(lastActivity)) {
                finishParent(lastActivity);
            }
        }
    }

    @Override
    public void onStart() {
        if (isLocalLaunch()) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (isLocalLaunch()) {
            super.onRestart();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (isLocalLaunch()) {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (isLocalLaunch()) {
            super.onSaveInstanceState(outState);
        }
    }

    public void onNewIntent(Intent intent) {
        if (isLocalLaunch()) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onResume() {
        if (isLocalLaunch()) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (isLocalLaunch()) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (isLocalLaunch()) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (isLocalLaunch()) {
            super.onDestroy();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isLocalLaunch()) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isLocalLaunch()) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isLocalLaunch()) {
            return super.dispatchKeyEvent(event);
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isLocalLaunch()) {
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isLocalLaunch()) {
            return super.onKeyUp(keyCode, event);
        }
        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (isLocalLaunch()) {
            return super.onKeyLongPress(keyCode, event);
        }
        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        if (isLocalLaunch()) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }
        return false;
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        if (isLocalLaunch()) {
            return super.onKeyShortcut(keyCode, event);
        }
        return false;
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (isLocalLaunch()) {
            super.onWindowAttributesChanged(params);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (isLocalLaunch()) {
            super.onWindowFocusChanged(hasFocus);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (isLocalLaunch()) {
            return super.onCreateOptionsMenu(menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (isLocalLaunch()) {
            return onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onContentChanged() {
        if (isLocalLaunch()) {
            super.onContentChanged();
        } else {
            mHostActivity.setTitleBarPadding(getTitleBar());
        }
    }
    

    public View getTitleBar() {
        return null;
    }

    // 场景加载完成回调
    @Deprecated
    public void onSceneLoaded(boolean success) {

    }

    // public void startActivityForResult(Intent intent, int requestCode,
    // Bundle options) {
    // if (isLocalLaunch()) {
    // super.startActivityForResult(intent, requestCode, options);
    // } else {
    // XmPluginManager.getInstance(mMainActivity).startActivityForResult(
    // mMainActivity, intent.getExtras(),
    // intent.getComponent().getPackageName(),
    // intent.getComponent().getClassName(), requestCode);
    //
    // }
    //
    // }

    // 启动插件包内的activity
    public void startActivity(Intent intent, String className) {
        startActivityForResult(intent, className, START_ACTIVITY_TAG);
    }

    public void startActivityForResult(Intent intent, String className,
            int requestCode) {
        if (isLocalLaunch()) {
            intent.setClassName(getPackageName(), className);
            if(mDeviceStat!=null)
            intent.putExtra("extra_device_did", mDeviceStat.did);
            this.startActivityForResult(intent, requestCode);
        } else {
            if (mPluginPackage == null) {
                mHostActivity.startActivityForResult(intent,
                        "", className, requestCode);
            } else {
                mHostActivity.startActivityForResult(intent,
                        mPluginPackage.packageName, className, requestCode);
            }
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (isLocalLaunch()) {
            super.startActivityForResult(intent, requestCode, options);
        } else {
            mMainActivity.startActivityForResult(intent, requestCode, options);
        }

    }

    // ------------------------------------------------------------------------
    // methods override from FragmentActivity
    // ------------------------------------------------------------------------

    @Override
    public FragmentManager getSupportFragmentManager() {
        if (isLocalLaunch()) {
            return super.getSupportFragmentManager();
        } else {
            return mMainActivity.getSupportFragmentManager();
        }
    }

    @Override
    public LoaderManager getSupportLoaderManager() {
        if (isLocalLaunch()) {
            return super.getSupportLoaderManager();
        } else {
            return mMainActivity.getSupportLoaderManager();
        }
    }

    // ////////////////////// ////////////////////// //////////////////////
    // ApiLevel:2
    public void setResult0(int resultCode) {
        if (isLocalLaunch()) {
            super.setResult(resultCode);
        } else {
            mMainActivity.setResult(resultCode);
        }
    }

    public void setResult0(int resultCode, Intent data) {
        if (isLocalLaunch()) {
            super.setResult(resultCode, data);
        } else {
            mMainActivity.setResult(resultCode, data);
        }
    }

    public IXmPluginHostActivity hostActivity() {
        return mHostActivity;
    }

    public XmPluginPackage pluginPackage() {
        return mPluginPackage;
    }

    public boolean isFinishing() {
        if (isLocalLaunch()) {
            return super.isFinishing();
        } else {
            return mMainActivity.isFinishing();
        }
    }

    @Override
    public boolean isChangingConfigurations() {
        if (isLocalLaunch()) {
            return super.isChangingConfigurations();
        } else {
            return mMainActivity.isChangingConfigurations();
        }
    }

    @Override
    public int getRequestedOrientation() {
        if (isLocalLaunch()) {
            return super.getRequestedOrientation();
        } else {
            return mMainActivity.getRequestedOrientation();
        }
    }

    @Override
    public int getTaskId() {
        if (isLocalLaunch()) {
            return super.getTaskId();
        } else {
            return mMainActivity.getTaskId();
        }
    }

    @Override
    public boolean isTaskRoot() {
        if (isLocalLaunch()) {
            return super.isTaskRoot();
        } else {
            return mMainActivity.isTaskRoot();
        }
    }

    @Override
    public SharedPreferences getPreferences(int mode) {
        if (isLocalLaunch()) {
            return super.getPreferences(mode);
        } else {
            return mMainActivity.getPreferences(mode);
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (isLocalLaunch()) {
            super.setRequestedOrientation(requestedOrientation);
        } else {
            mMainActivity.setRequestedOrientation(requestedOrientation);
        }

    }

    @Override
    public android.app.LoaderManager getLoaderManager() {
        if (isLocalLaunch()) {
            return super.getLoaderManager();
        } else {
            return mMainActivity.getLoaderManager();
        }
    }

    @Override
    public View getCurrentFocus() {
        if (isLocalLaunch()) {
            return super.getCurrentFocus();
        } else {
            return mMainActivity.getCurrentFocus();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        if (isLocalLaunch()) {
            super.onPostCreate(savedInstanceState);
        }
    }

    @Override
    public void onPostResume() {
        if (isLocalLaunch()) {
            super.onPostResume();
        }
    }

    @Override
    public void onUserLeaveHint() {
        if (isLocalLaunch()) {
            super.onUserLeaveHint();
        }
    }
    

}
