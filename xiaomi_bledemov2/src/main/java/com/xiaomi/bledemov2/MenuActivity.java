package com.xiaomi.bledemov2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.bluetooth.BleUpgrader;
import com.xiaomi.smarthome.bluetooth.ISlideBtnController;
import com.xiaomi.smarthome.bluetooth.ISlideBtnViewer;
import com.xiaomi.smarthome.bluetooth.SlideBtnController;
import com.xiaomi.smarthome.bluetooth.XmBluetoothManager;
import com.xiaomi.smarthome.bluetooth.XmBluetoothSearchManager;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity.*;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import java.util.ArrayList;

/**
 * Created by liwentian on 2016/10/13.
 */

public class MenuActivity extends XmPluginBaseActivity implements View.OnClickListener {

    private static final int MENU2_CODE = 2;

    private static final int SUBMENU1_CODE = 20;

    private static final int MSG_UPDATE = 30;

    private int menuIndex = 1;

    private String mCurrentVersion = "1.0.1";
    private String mNewestVersion = "1.2.3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView tvTitle = ((TextView) findViewById(R.id.title_bar_title));
        tvTitle.setText(R.string.menu);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        initialMenuButton(R.id.menu1);
        initialMenuButton(R.id.menu2);
        initialMenuButton(R.id.menu3);
    }

    private void initialMenuButton(int id) {
        Button btn = (Button) findViewById(id);
        btn.setText(String.format("%s %d", getString(R.string.menu_key), menuIndex++));
        btn.setOnClickListener(this);
    }

    // 打开默认的一级菜单
    private void openMenu1() {
        ArrayList<MenuItemBase> items = new ArrayList<MenuItemBase>();
        hostActivity().openMoreMenu2(items, true, 0, null);
    }

    // 打开默认的一级菜单，隐藏自动化
    private void openMenu2() {
        ArrayList<MenuItemBase> items = new ArrayList<MenuItemBase>();
        Intent intent = new Intent();
        intent.putExtra("scence_enable", false);
        hostActivity().openMoreMenu2(items, true, 0, intent);
    }

    // 一级菜单中添加自定义的菜单
    private void openMenu3() {
        ArrayList<MenuItemBase> items = new ArrayList<MenuItemBase>();

        // StringMenuItem点击后菜单关闭，在onActivityResult中处理点击逻辑
        StringMenuItem stringMenu1 = new StringMenuItem();
        stringMenu1.name = "StringMenu1";
        items.add(stringMenu1);

        StringMenuItem stringMenu2 = new StringMenuItem();
        stringMenu2.name = "StringMenu2";
        items.add(stringMenu2);

        // InfoMenuItem只用于信息展示，点击后没任何反应
        InfoMenuItem infoMenu = new InfoMenuItem();
        infoMenu.name = "InfoMenu";
        items.add(infoMenu);

        SlideBtnMenuItem slideMenu1 = new SlideBtnMenuItem();
        slideMenu1.name = "SlideMenu1";
        slideMenu1.controller = mSlideBtnController;
        items.add(slideMenu1);

        SlideBtnMenuItem slideMenu2 = new SlideBtnMenuItem();
        slideMenu2.name = "SlideMenu2";
        slideMenu2.controller = mSlideBtnController;
        items.add(slideMenu2);

        hostActivity().openMoreMenu2(items, true, MENU2_CODE, null);

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 在一级菜单打开状态下可以设置check状态
                // mSlideBtnController.setChecked("SlideMenu1", true);
            }
        }, 2000);
    }

    private void openSubMenu1() {
        ArrayList<MenuItemBase> items = new ArrayList<MenuItemBase>();

        StringMenuItem stringMenu1 = new StringMenuItem();
        stringMenu1.name = "SubStringMenu1";
        items.add(stringMenu1);

        BleMenuItem bleMenu = BleMenuItem.newUpgraderItem(mBleUpgrader);
        items.add(bleMenu);

        hostActivity().openMoreMenu(items, true, SUBMENU1_CODE, null);
    }

    private final BleUpgrader mBleUpgrader = new BleUpgrader() {
        @Override
        public String getCurrentVersion() throws RemoteException {
            return mCurrentVersion;
        }

        @Override
        public String getLatestVersion() throws RemoteException {
            return mNewestVersion;
        }

        @Override
        public String getUpgradeDescription() throws RemoteException {
            return "Hello World!";
        }

        @Override
        public void startUpgrade() throws RemoteException {
            CommonUtils.toast((Context) hostActivity(), String.format("startUpgrade"));
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 2000);
        }

        @Override
        public void onActivityCreated(Bundle bundle) throws RemoteException {
            CommonUtils.toast((Context) hostActivity(), String.format("onActivityCreated"));

            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (!mCurrentVersion.equals(mNewestVersion)) {
                        mBleUpgrader.showPage(XmBluetoothManager.PAGE_CURRENT_DEPRECATED, null);
                    } else {
                        mBleUpgrader.showPage(XmBluetoothManager.PAGE_CURRENT_LATEST, null);
                    }
                }
            }, 2000);
        }
    };

    private int mProgress = 0;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE:
                if (mProgress <= 100) {
                    Bundle data = new Bundle();
                    data.putInt(XmBluetoothManager.EXTRA_UPGRADE_PROCESS, mProgress++);
                    mBleUpgrader.showPage(XmBluetoothManager.PAGE_UPGRADING, data);
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 100);
                } else {
                    mCurrentVersion = mNewestVersion;
                    mBleUpgrader.showPage(XmBluetoothManager.PAGE_UPGRADE_SUCCESS, null);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MENU2_CODE:
                if (data != null) {
                    String name = data.getStringExtra("menu");
                    if (!TextUtils.isEmpty(name)) {
                        CommonUtils.toast((Context) hostActivity(), String.format("%s clicked", name));

                        if (name.equals("StringMenu1")) {
                            openSubMenu1();
                        } else if (name.equals("StringMenu2")) {
                        }
                    }
                }
                break;

            case SUBMENU1_CODE:
                if (data != null) {
                    String name = data.getStringExtra("menu");
                    if (!TextUtils.isEmpty(name)) {
                        CommonUtils.toast((Context) hostActivity(), String.format("%s clicked", name));
                    }
                }
                break;
        }
    }

    private final SlideBtnController mSlideBtnController = new SlideBtnController() {
        @Override
        public void onCheckedChanged(String name, boolean checked) throws RemoteException {
            CommonUtils.toast((Context) hostActivity(), String.format("%s %s", name, checked ? "checked" : "unchecked"));

            if (name.equals("SlideMenu1")) {

            } else if (name.equals("SlideMenu2")) {

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu1:
                openMenu1();
                break;

            case R.id.menu2:
                openMenu2();
                break;

            case R.id.menu3:
                openMenu3();
                break;
        }
    }
}
