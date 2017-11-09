
package com.xiaomi.xmplugindemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.common.ui.dialog.MLAlertDialog;
import com.xiaomi.smarthome.common.ui.dialog.XQProgressDialog;
import com.xiaomi.smarthome.common.ui.dialog.XQProgressDialogSimple;
import com.xiaomi.smarthome.common.ui.dialog.XQProgressHorizontalDialog;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

public class DiaglogActivity extends XmPluginBaseActivity {
    private DemoDevice mDevice;
    static final int MSG_REFRESH_DIALOG = 1;
    private XQProgressHorizontalDialog mXQProgressHorizontalDialog;
    private int progress;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_DIALOG:
                    if (mXQProgressHorizontalDialog != null) {
                        progress += 5;
                        if (progress <= 100) {
                            mXQProgressHorizontalDialog.setProgress(100, progress);
                            mHandler.sendEmptyMessageDelayed(MSG_REFRESH_DIALOG, 200);
                        } else {
                            mXQProgressHorizontalDialog.setProgress(100, 100);
                        }
                    }
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        mDevice = DemoDevice.getDevice(mDeviceStat);

        // 设置titlebar在顶部透明显示时的顶部padding
        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));
        ((TextView) findViewById(R.id.title_bar_title)).setText("Dialog");
        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.message_dialog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new MLAlertDialog.Builder(DiaglogActivity.this)
                        .setTitle("Message Dialog")
                        .setMessage("欢迎使用智能家庭插件系统公共UI组件，这是一个消息Dialog实例")
                        .setPositiveButton("确定", null)
//                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        findViewById(R.id.input_dialog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MLAlertDialog.Builder builder = new MLAlertDialog.Builder(DiaglogActivity.this)
                        .setTitle("Message Dialog")
                        .setInputView("请输入", true)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null);
                MLAlertDialog dialog = builder.show();
                String inputString = dialog.getInputView().getEditableText().toString();
            }
        });

        findViewById(R.id.progress_dialog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                XQProgressDialog dialog = new XQProgressDialog(DiaglogActivity.this);
                dialog.setMessage("处理中...");
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });

        findViewById(R.id.progress_dialog_1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mXQProgressHorizontalDialog = new XQProgressHorizontalDialog(DiaglogActivity.this);
                mXQProgressHorizontalDialog.setMessage("正在处理中");
                mXQProgressHorizontalDialog.show();
                progress = 0;
                mHandler.sendEmptyMessageDelayed(MSG_REFRESH_DIALOG, 200);
            }
        });

        findViewById(R.id.progress_dialog_2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                XQProgressDialogSimple dialog = new XQProgressDialogSimple(DiaglogActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        findViewById(R.id.item_select_dialog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new MLAlertDialog.Builder(DiaglogActivity.this)
                        .setTitle("单选 Dialog")
                        .setItems(new String[] {
                                "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"
                        },  new MLAlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog.dismiss();
                            }
                        }).show();
            }
        });
        
        findViewById(R.id.single_select_dialog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new MLAlertDialog.Builder(DiaglogActivity.this)
                        .setTitle("单选 Dialog")
                        .setSingleChoiceItems(new String[] {
                                "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"
                        }, 0, new MLAlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog.dismiss();
                            }
                        }).setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        findViewById(R.id.multi_select_dialog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new MLAlertDialog.Builder(DiaglogActivity.this)
                        .setTitle("多选 Dialog")
                        .setMultiChoiceItems(new String[] {
                                "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"
                        }, null, null)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);
    }

}
