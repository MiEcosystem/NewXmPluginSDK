
package com.xiaomi.xmplugindemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ShareActivity extends XmPluginBaseActivity {
    private DemoDevice mDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

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

        findViewById(R.id.multi_images_share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = getFileStreamPath("images.zip");
                    copyAsserts(ShareActivity.this, "images.zip", file.getAbsolutePath());
                    mHostActivity.openShareMediaActivity("智能家庭", "分享多图", file.getAbsolutePath());
                } catch (Exception e) {

                }
            }
        });
        findViewById(R.id.single_image_share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = getFileStreamPath("image.zip");
                    copyAsserts(ShareActivity.this, "image.zip", file.getAbsolutePath());
                    mHostActivity.openShareMediaActivity("智能家庭", "单多图", file.getAbsolutePath());
                } catch (Exception e) {

                }
            }
        });

        findViewById(R.id.multi_images_download_share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // File file = getFileStreamPath("image.zip");
                    // copyAsserts(ShareActivity.this,"image.zip",file.getAbsolutePath());
                    mHostActivity.openShareMediaActivity("智能家庭", "多图下载分享",
                            "http://cdn.fds.api.xiaomi.com/miio.files/caches/images.zip");
                } catch (Exception e) {

                }
            }
        });

        findViewById(R.id.testShareUrl).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHostActivity.openShareMediaActivity("智能家庭开发平台", "小米智能家庭开发平台",
                        "https://open.home.mi.com/index.html#/intro", null, null, null);
            }
        });

        findViewById(R.id.testShareImage).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                // R.drawable.welcome);
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                View view = findViewById(android.R.id.content);
                view.draw(canvas);
                
                File dir = activity().getExternalCacheDir();
                String filePath = dir.getAbsolutePath() + File.separator + "local_share.jpg";
                new File(filePath).deleteOnExit();
                try {
                    OutputStream os = new FileOutputStream(filePath);
                    bitmap.compress(CompressFormat.JPEG, 80, os);
                } catch (Exception e) {

                }

               
                mHostActivity.openShareMediaActivity("智能家庭开发平台", "小米智能家庭开发平台", filePath);
            }
        });
        
        findViewById(R.id.testShareImageDialog).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                // R.drawable.welcome);
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                View view = findViewById(android.R.id.content);
                view.draw(canvas);
                
                File dir = activity().getExternalCacheDir();
                String filePath = dir.getAbsolutePath() + File.separator + "local_share.jpg";
                new File(filePath).deleteOnExit();
                try {
                    OutputStream os = new FileOutputStream(filePath);
                    bitmap.compress(CompressFormat.JPEG, 80, os);
                } catch (Exception e) {

                }

               
                mHostActivity.openSharePictureActivity("智能家庭开发平台", "小米智能家庭开发平台", filePath);
            }
        });

        findViewById(R.id.shopShareImage).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView(
                        "http://api.io.mi.com/app/shop/content?data=%7B%22ct_id%22%3A%22436%22%7D",
                        "");
            }
        });

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);
    }

    public static File createFileWhetherExists(String filePath) {
        File file = new File(filePath);

        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
        }

        return file;
    }

    public static void copyAsserts(Context ctx, String assetsPath, String targetPath)
            throws IOException {
        InputStream is = null;
        is = ctx.getResources().getAssets().open(assetsPath);
        File f = createFileWhetherExists(targetPath);
        FileOutputStream fo = new FileOutputStream(f);
        int len = -1;
        byte[] bt = new byte[2048];
        while ((len = is.read(bt)) != -1) {
            fo.write(bt, 0, len);
        }
        fo.flush();
        is.close();
        fo.close();
    }
}
