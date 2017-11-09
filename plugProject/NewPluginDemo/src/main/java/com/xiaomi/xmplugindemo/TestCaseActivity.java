
package com.xiaomi.xmplugindemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.xiaomi.smarthome.common.ui.widget.XmRadioGroup;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.Parser;
import com.xiaomi.smarthome.device.api.SceneInfo;
import com.xiaomi.smarthome.device.api.UserInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestCaseActivity extends XmPluginBaseActivity {

    static final int SCAN_BARCODE = 1;
    static final int REQUEST_MENU = 2;
    static final int REQUEST_CODE_PICK_IMAGE = 3;
    static final int REQUEST_CODE_PHOTO_WITH_CAMERA = 4;
    LinearLayout mListContainer;
    LayoutInflater mInflater;
    DemoDevice mDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_case);
        // 初始化device
        mDevice = DemoDevice.getDevice(mDeviceStat);

        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.title_bar_title)).setText("插件测试用例");

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        mListContainer = (LinearLayout) findViewById(R.id.container);
        mInflater = LayoutInflater.from(this);

        addTestCase("intent parcelable", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ParcelData pData = new ParcelData();
                pData.mData = 2;
                intent.putExtra("pData", pData);
                startActivity(intent, FragmentActivity.class.getName());
            }
        });

        addTestCase("openShareActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openShareActivity();
            }
        });

        addTestCase("goUpdateActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.goUpdateActivity();
            }
        });

//        addTestCase("startLoadScene", new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHostActivity.startLoadScene();
//            }
//        });

        addTestCase("startCreateSceneByDid", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startCreateSceneByDid(mDeviceStat.did);
            }
        });

        addTestCase("startEditScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SceneInfo> sceneInfos = mHostActivity.getSceneByDid(mDeviceStat.did);
                if (sceneInfos != null && sceneInfos.size() > 0)
                    mHostActivity.startEditScene(sceneInfos.get(0).mSceneId);
            }
        });

        addTestCase("startSetTimerList", new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray onParams = new JSONArray();
                onParams.put(0xffffff);
                JSONArray offParams = new JSONArray();
                offParams.put(0);
                mHostActivity.startSetTimerList(mDeviceStat.did, "set_rgb", onParams.toString(),
                        "set_rgb", offParams.toString(), mDeviceStat.did, "开关",
                        "开关灯");
            }
        });

        addTestCase("openShareMediaActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().getDecorView();
                if (view == null) {
                    return;
                }
                Bitmap bitmap = null;//= view.getDrawingCache();
                if (bitmap == null) {
                    if (view.getWidth() > 0 && view.getHeight() > 0) {
                        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);
                    }
                }

                if (bitmap == null) {
                    return;
                }
                File dir = getExternalCacheDir();
                if (dir == null) {
                    Toast.makeText(activity(), "没有存储空间", Toast.LENGTH_SHORT).show();
                    return;
                }
                File shareFile = new File(dir, "share.jpg");
                boolean savesuccess = false;
                try {
                    OutputStream os = new FileOutputStream(shareFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
                    os.close();
                    savesuccess = true;
                    bitmap.recycle();
                    bitmap = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (savesuccess) {
                    mHostActivity.openShareMediaActivity("智能家庭",
                            "测试分享",
                            shareFile.getAbsolutePath()
                    );
                }
            }
        });


        addTestCase("startEditCustomScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startEditCustomScene();
            }
        });

        addTestCase("goUpdateActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.goUpdateActivity(mDeviceStat.did);
            }
        });

        addTestCase("startEditCustomScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startEditCustomScene();
            }
        });

        addTestCase("loadWebView", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView("http://smartmifaq.mi-ae.cn/AirPurifierQA/index.html", "Q & A");
            }
        });

        addTestCase("createWXAPI", new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().getDecorView();
                if (view == null) {
                    return;
                }
                Bitmap bitmap = null;//= view.getDrawingCache();
                if (bitmap == null) {
                    if (view.getWidth() > 0 && view.getHeight() > 0) {
                        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);
                    }
                }

                if (bitmap == null) {
                    return;
                }

                IWXAPI wxapi = XmPluginHostApi.instance().createWXAPI(activity(), true);
                WXMediaMessage msg = new WXMediaMessage();
                msg.title = "test";
                msg.description = "wx share test";
                msg.setThumbImage(resizeBitmap(bitmap, 150));
                msg.mediaObject = new WXImageObject(bitmap);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;

                boolean ret = wxapi.sendReq(req);
            }
        });

        addTestCase("水电煤缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(0, mDeviceStat.latitude, mDeviceStat.longitude);
            }
        });

        addTestCase("水缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(1, mDeviceStat.latitude, mDeviceStat.longitude);
            }
        });
        addTestCase("电缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(2, mDeviceStat.latitude, mDeviceStat.longitude);
            }
        });
        addTestCase("燃气缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(3, mDeviceStat.latitude, mDeviceStat.longitude);
            }
        });

        addTestCase("查询剩余煤气费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                XmPluginHostApi.instance().getRechargeBalances(3, mDeviceStat.latitude, mDeviceStat.longitude, new Callback<JSONObject>() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        if (result == null) {
                            Toast.makeText(activity(), "没有查询到余额", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity(), "" + result.optInt("balance"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {
                        Toast.makeText(activity(), errorInfo, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        addTestCase("扫描二维码", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openScanBarcodePage(null, SCAN_BARCODE);
            }
        });

        addTestCase("条形码扫描", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putIntArray("barcode_format", new int[]{IXmPluginHostActivity.BarcodeFormat.CODABAR.ordinal()});
                mHostActivity.openScanBarcodePage(bundle, SCAN_BARCODE);
            }
        });

        addTestCase("上传文件", new OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile("/sdcard/DCIM/share.jpg");
            }
        });

        addTestCase("测试支付", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView("https://api.ucashier.mipay.com/api/trade/doWapCreate?createTime=1467784399&goodType=ordinary&inputCharset=UTF-8&notifyUrl=http://shopapi.io.mi.srv/app/shop/notify&orderDesc=%E6%99%BA%E8%83%BD%E5%AE%B6%E5%BA%AD&outOrderId=20160706135319101057766&partnerId=10000096&payMethod=directPay&productName=%E6%99%BA%E8%83%BD%E5%AE%B6%E5%BA%AD&returnUrl=https://home.mi.com/shop/orderdetail&sellerId=10000096&service=createDirectPayByUser&totalFee=100&xiaomiId=103434651&sign=nO_W8u7V7lrlF666_YrymWb31wy6zRQmW52qA56stON-UCcZwezRfqwCcJWeb3QkSCD8uPzSA_U8dIG1OsySX_KZxNrDp18q_i16vzNyfDArOakRCJq8qOUK_GqkuErl30PrTzEI0SZxaOJmnmb7yRnJPaCht_nnmdvKjkJAEes.", "");
            }
        });

        addTestCase("异步请求", new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("api_type", "locate");
                    jsonObject.put("data", 1);
                } catch (Exception e) {

                }
                XmPluginHostApi.instance().callRemoteAsync(new String[]{"insistek.351564056062854"}, 10046, jsonObject,
                        new Callback<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Log.d("test", result.toString());
                            }

                            @Override
                            public void onFailure(int error, String errorInfo) {
                                Log.d("test", "error:" + error);
                            }
                        });
            }
        });

        addTestCase("刷新设备信息", new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> dids = new ArrayList<String>();
                dids.add(mDeviceStat.did);
                XmPluginHostApi.instance().updateDevice(dids, new Callback<List<DeviceStat>>() {
                    @Override
                    public void onSuccess(List<DeviceStat> result) {
                        Toast.makeText(activity(), "onSuccess:" + result.size(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {
                        Toast.makeText(activity(), "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        addTestCase("更多菜单", new OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<IXmPluginHostActivity.MenuItemBase> menus = new
                        ArrayList<>();

                ////插件自定义菜单，可以在public void onActivityResult(int requestCode, int resultCode, Intent data) 中接收用户点击的菜单项，String result = data.getStringExtra("menu");
                IXmPluginHostActivity.StringMenuItem stringMenuItem = new
                        IXmPluginHostActivity.StringMenuItem();
                stringMenuItem.name = "test string menu";
                menus.add(stringMenuItem);

                //跳转到插件下一个activity的菜单
                IXmPluginHostActivity.IntentMenuItem intentMenuItem = new
                        IXmPluginHostActivity.IntentMenuItem();
                intentMenuItem.name = "test intent menu";
                intentMenuItem.intent =
                        mHostActivity.getActivityIntent(null,
                                ApiDemosActivity.class.getName());
                menus.add(intentMenuItem);

                //带开关按钮的菜单，可以自动调用设备rpc
                IXmPluginHostActivity.SlideBtnMenuItem slideBtnMenuItem = new
                        IXmPluginHostActivity.SlideBtnMenuItem();
                slideBtnMenuItem.name = "test slide menu";
                slideBtnMenuItem.isOn = mDevice.getRgb() > 0;
                slideBtnMenuItem.onMethod = "set_rgb";
                JSONArray onparams = new JSONArray();
                onparams.put(0xffffff);
                slideBtnMenuItem.onParams = onparams.toString();
                slideBtnMenuItem.offMethod = "set_rgb";
                JSONArray offparams = new JSONArray();
                offparams.put(0);
                slideBtnMenuItem.offParams =
                        offparams.toString();
                menus.add(slideBtnMenuItem);

                Intent intent = new Intent();
                intent.putExtra("security_setting_enable",true);
                mHostActivity.openMoreMenu2(menus, true, REQUEST_MENU, intent);
            }
        });

        addTestCase("测试支付", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView("http://home.mi.com/device/guide?model=zhimi.airpurifier.m1&locale=cn", "test");
            }
        });

        addTestCase("本地ping接口", new OnClickListener() {
            @Override
            public void onClick(View v) {
                XmPluginHostApi.instance().localPing(mDeviceStat.did, new Callback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(activity(),"onSuccess",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {
                        Toast.makeText(activity(),"onFailure",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        addTestCase("local ping", new OnClickListener() {
            @Override
            public void onClick(View v) {
                XmPluginHostApi.instance().localPing(mDeviceStat.did, new Callback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d("localping","onSuccess");
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {
                        Log.d("localping","onFailure");
                    }
                });
            }
        });

        addTestCase("相册中获取图片", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                activity().startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });

        addTestCase("拍照中获取图片", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activity().startActivityForResult(intent, REQUEST_CODE_PHOTO_WITH_CAMERA);
            }
        });

        addTestCase("测试获取用户信息", new OnClickListener() {
            @Override
            public void onClick(View v) {
                XmPluginHostApi.instance().getUserInfo(XmPluginHostApi.instance().getAccountId(), new Callback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        Log.d("getUserInfo","onSuccess");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("getUserInfo","onFailure");
                    }
                });
            }
        });


    }


    private static class VoiceUploadUrl {

        public static String parse(String key, JSONObject jsonObject) {
            if (jsonObject.isNull(key)) {
                return null;
            }
            JSONObject jo1 = jsonObject.optJSONObject(key);
            if (jo1.isNull("url")) {
                return null;
            }
            return jo1.optString("url");
        }

    }

    private void uploadFile(final String filePath) {
        try {
            JSONObject jo = new JSONObject();
            jo.put("did", "12464");
            jo.put("suffix", "1");

            XmPluginHostApi.instance().callSmartHomeApi(mDeviceStat.model, "/voiceint/genvoiceuploadurl", jo, new Callback<String>() {
                @Override
                public void onSuccess(final String result) {
                    AsyncTask<Void, Void, Response> task = new AsyncTask<Void, Void, Response>() {
                        @Override
                        protected Response doInBackground(Void... params) {
                            try {
                                return uploadFile(result, filePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Response response) {
                            super.onPostExecute(response);
                            Log.d("test", "" + response.code() + " " + response.message());
                        }
                    };

                    task.execute();
                }

                @Override
                public void onFailure(int error, String errorInfo) {

                }
            }, new Parser<String>() {
                @Override
                public String parse(String result) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    return VoiceUploadUrl.parse("1", jsonObject);
                }
            });

        } catch (Exception ex) {
            // Handle the error
            ex.printStackTrace();
        }
    }

    public Response uploadFile(String url, String filePath) throws IOException {
        Log.d("test", "url:" + url);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.parse(""), new File(filePath)))
                .build();
        return client.newCall(request).execute();
    }

    void addTestCase(String name, OnClickListener listener) {
        View view = mInflater.inflate(R.layout.list_item, null);
        ((TextView) view.findViewById(R.id.name)).setText(name);
        view.setOnClickListener(listener);
        LinearLayout.LayoutParams lp = new XmRadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mListContainer.addView(view, lp);
    }

    public static Bitmap resizeBitmap(Bitmap target, int newWidth) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        if (width > height) {
            float scaleWidth = ((float) newWidth) / width;
            matrix.postScale(scaleWidth, scaleWidth);
        } else {
            float scaleHeight = ((float) newWidth) / height;
            matrix.postScale(scaleHeight, scaleHeight);
        }
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        return bmp;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_BARCODE) {
                String result = data.getStringExtra("scan_result");
                Toast.makeText(activity(), result, Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_MENU) {
                String result = data.getStringExtra("menu");
                Toast.makeText(activity(), result, Toast.LENGTH_SHORT).show();
            } else if(requestCode == REQUEST_CODE_PICK_IMAGE){
                Uri uri = data.getData();
                Toast.makeText(activity(),uri.toString(),Toast.LENGTH_SHORT).show();
            }else if(requestCode == REQUEST_CODE_PHOTO_WITH_CAMERA){
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
                if(bitmap!=null){
                    Toast.makeText(activity(),"获取图片成功",Toast.LENGTH_SHORT).show();
                    bitmap.recycle();
                }
            }

        }
    }

}
