
package com.xiaomi.xmplugindemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.DeviceStat;
import com.xiaomi.smarthome.device.api.IXmPluginHostActivity;
import com.xiaomi.smarthome.device.api.MessageCallback;
import com.xiaomi.smarthome.device.api.Parser;
import com.xiaomi.smarthome.device.api.SceneInfo;
import com.xiaomi.smarthome.device.api.UserInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiTestActivity extends XmPluginBaseActivity {
    static final String TAG = "ApiTest";

    HtmlTextView mTextView;
    StringBuffer mTextInfo = new StringBuffer();
    Handler mHandler = new Handler();

    List<DeviceStat> mDeviceList;


    static class TestStatus {
        public TestStatus(String method) {
            this.methodName = method;
        }

        public long startThread;
        public long endThread;
        public int status;
        public String methodName;
        public String result;
        public long startTime;
        public long consumeTime;
    }

    HandlerThread mTestHanderThread;
    Handler mTestThreadHandler;

    HashMap<String, TestStatus> mTestStatusHasmap = new HashMap<>();
    ArrayList<TestStatus> mTestStatusList = new ArrayList<>();

    boolean mIsResumed = false;
    Handler mMainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refresh();
            if(mIsResumed)
                mMainHandler.sendEmptyMessageDelayed(1,100);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestHanderThread = new HandlerThread("test api");
        mTestHanderThread.start();
        mTestThreadHandler = new Handler(mTestHanderThread.getLooper());

        setContentView(R.layout.activity_api_test);

        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.title_bar_title)).setText("ApiTest");

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        mTextView = (HtmlTextView) findViewById(R.id.text_view);
        findViewById(R.id.test_other).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextInfo = new StringBuffer();
                mTestStatusHasmap.clear();
                mTestStatusList.clear();
                mMainHandler.sendEmptyMessageDelayed(1, 100);
                mTestThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        test();
                    }
                });
            }
        });
        findViewById(R.id.test_main).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextInfo = new StringBuffer();
                mTestStatusHasmap.clear();
                mTestStatusList.clear();
                mMainHandler.sendEmptyMessageDelayed(1, 100);
                test();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsResumed = false;
        mHandler.removeMessages(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsResumed = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTestHanderThread.quit();
    }

    void test(){
        addInfo(XmPluginHostApi.instance() != null, "instance");
        addInfo(XmPluginHostApi.instance().getApiLevel() > 0, "getApiLevel", XmPluginHostApi.instance().getApiLevel());
        addInfo(!TextUtils.isEmpty(XmPluginHostApi.instance().getChannel()), "getChannel", XmPluginHostApi.instance().getChannel());
        addInfo(XmPluginHostApi.instance().application() != null, "application");
        addInfo(XmPluginHostApi.instance().context() != null, "context");
        testCallSmartHomeApi();
        testCallHttpApi();
        testCallHttpApiV13();
        testCallMethod();
        testDeviceList();
        testSubscribeDevice();
        addInfo(XmPluginHostApi.instance().getAccountId() != null, "getAccountId", XmPluginHostApi.instance().getAccountId());
        addInfo(XmPluginHostApi.instance().getLastLocation() != null, "getLastLocation");
        testUpdateDeviceList();
        testUpdateDeviceProperties();
        testGetUserInfo();
        testSendMessage();
        testScence();
        testGetDeviceHistoryEvent();
        testGetDeviceHistoryProp();

        testBarcodeCodec();
    }

    void testCallSmartHomeApi() {
        start("callSmartHomeApi");
        XmPluginHostApi.instance().callSmartHomeApi(mDeviceStat.model, "/home/device_list", new JSONObject(), new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                addInfo(result != null, "callSmartHomeApi");
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "callSmartHomeApi", "error:" + error + " " + errorInfo);
            }
        }, Parser.DEFAULT_PARSER);
    }

    void testCallHttpApi() {
        start("callHttpApi");
        XmPluginHostApi.instance().callHttpApi(mDeviceStat.model, "http://www.baidu.com", XmPluginHostApi.METHOD_GET, null, new Callback<String>() {
            @Override
            public void onSuccess(String result) {
                addInfo(result != null, "callHttpApi");
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "callHttpApi", "error:" + error + " " + errorInfo);
            }
        }, new Parser<String>() {
            @Override
            public String parse(String result) {
                return result;
            }
        });
    }

    void testCallHttpApiV13() {
        start("callHttpApiV13");
        XmPluginHostApi.instance().callHttpApiV13(mDeviceStat.model, "http://www.baidu.com", XmPluginHostApi.METHOD_GET, null, new Callback<String>() {
            @Override
            public void onSuccess(String result) {
                addInfo(result != null, "callHttpApiV13");
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "callHttpApiV13", "error:" + error + " " + errorInfo);
            }
        }, new Parser<String>() {
            @Override
            public String parse(String result) {
                return result;
            }
        });
    }

    void testCallMethod() {
        start("callMethod");
        JSONArray params = new JSONArray();
        params.put(0);
        XmPluginHostApi.instance().callMethod(mDeviceStat.did, "set_rgb", params, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                addInfo(result != null, "callMethod");
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "callMethod", "error:" + error + " " + errorInfo);
            }
        }, Parser.DEFAULT_PARSER);
    }

    void testDeviceList() {
        mDeviceList = XmPluginHostApi.instance().getDeviceList();
        addInfo(mDeviceList != null && mDeviceList.size() > 0, "getDeviceList");
        addInfo(XmPluginHostApi.instance().getDeviceByDid(mDeviceStat.did) != null, "getDeviceByDid");
        for (DeviceStat device : mDeviceList) {
            if (!TextUtils.isEmpty(device.parentId)) {
                addInfo(XmPluginHostApi.instance().getSubDeviceByParentDid(device.parentId) != null, "getDeviceByDid");
                start("updateSubDevice");
                XmPluginHostApi.instance().updateSubDevice(mPluginPackage, new String[]{device.parentId}, new Callback<List<DeviceStat>>() {
                    @Override
                    public void onSuccess(List<DeviceStat> result) {
                        addInfo(true, "updateSubDevice");
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {
                        addInfo(false, "callMethod", "error:" + error + " " + errorInfo);
                    }
                });
                break;
            }
        }
//        for (final DeviceStat device : mDeviceList) {
//            if (device.model.startsWith("xiaomi.router")) {
//                String did = device.did;
//                if (did.startsWith("miwifi.")) {
//                    did = did.substring(7);
//                }
//                XmPluginHostApi.instance().checkLocalRouterInfo(did, new Callback<Void>() {
//                    @Override
//                    public void onSuccess(Void result) {
//                        addInfo(true, "checkLocalRouterInfo",device.name);
//                    }
//
//                    @Override
//                    public void onFailure(int error, String errorInfo) {
//                        addInfo(false, "checkLocalRouterInfo", device.name+" error:" + error + " " + errorInfo);
//                    }
//                });
//            }
//        }


    }

    void testSubscribeDevice() {
        start("subscribeDevice");
        List<String> events = new ArrayList<>();
        events.add("event.button_pressed");
        events.add("prop.rgb");
        XmPluginHostApi.instance().subscribeDevice(mDeviceStat.did, mDeviceStat.pid, events, 3, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                addInfo(true, "subscribeDevice");
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "callMethod", "error:" + error + " " + errorInfo);
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        start("unsubscribeDevice");
        XmPluginHostApi.instance().unsubscribeDevice(mDeviceStat.did, mDeviceStat.pid, events, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                addInfo(true, "unsubscribeDevice");
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "unsubscribeDevice", "error:" + error + " " + errorInfo);
            }
        });

    }

    void testUpdateDeviceList() {
        start("updateDeviceList");
        XmPluginHostApi.instance().updateDeviceList(new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                addInfo(true, "updateDeviceList");
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "updateDeviceList", "error:" + error + " " + errorInfo);
            }
        });
    }


    void testUpdateDeviceProperties() {
        int testValue = (int) (Math.random() * 10000);
        JSONObject jsobject = new JSONObject();
        try {
            jsobject.put("test", testValue);
        } catch (JSONException e) {
        }
        XmPluginHostApi.instance().updateDeviceProperties(mDeviceStat.did, jsobject);
        DeviceStat devices = XmPluginHostApi.instance().getDeviceByDid(mDeviceStat.did);
        int newTestValue = 0;
        try {
            newTestValue = devices.propInfo.getInt("test");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addInfo(testValue == newTestValue, "updateDeviceProperties");
    }

    void testGetUserInfo() {
        start("getUserInfo");
        XmPluginHostApi.instance().getUserInfo(XmPluginHostApi.instance().getAccountId(), new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo result) {
                addInfo(true, "getUserInfo", result.nickName + result.phone);
            }

            @Override
            public void onFailure(int error, String errorInfo) {
                addInfo(false, "getUserInfo", "error:" + error + " " + errorInfo);
            }
        });
    }

    void testSendMessage() {
        final int testValue = (int) (Math.random() * 10000);
        Intent intent = new Intent();
        intent.putExtra("test", testValue);
        start("sendMessage");
        XmPluginHostApi.instance().sendMessage(mDeviceStat.did, 10001, intent, mDeviceStat, new MessageCallback() {
            @Override
            public void onSuccess(Intent result) {
                int newValue = result.getIntExtra("test", 0);
                addInfo(newValue == testValue, "sendMessage");
            }

            @Override
            public void onFailure(int errorCode, String errorInfo) {
                addInfo(false, "getUserInfo", "error:" + errorCode + " " + errorInfo);
            }
        });
    }

    void testScence() {
        start("startLoadScene");
        mHostActivity.startLoadScene(new IXmPluginHostActivity.AsyncCallback() {
            @Override
            public void onSuccess(Object result) {
                addInfo(true, "startLoadScene");
                List<SceneInfo> infos = mHostActivity.getSceneByDid(mDeviceStat.did);
                addInfo(infos != null, "getSceneByDid");
            }

            @Override
            public void onFailure(int shError, Object errorInfo) {
                addInfo(false, "startLoadScene", "error:" + shError + " " + errorInfo);
            }
        });

        start("getDeviceRecommendScenes");
        mHostActivity.getDeviceRecommendScenes(mDeviceStat.did, new IXmPluginHostActivity.AsyncCallback() {
            @Override
            public void onSuccess(Object result) {
                addInfo(true, "getDeviceRecommendScenes");
            }

            @Override
            public void onFailure(int shError, Object errorInfo) {
                addInfo(false, "getDeviceRecommendScenes", "error:" + shError + " " + errorInfo);
            }
        });

    }


    public void testGetDeviceHistoryEvent(){
        start("testGetDeviceHistoryEvent");

        long endTime = System.currentTimeMillis()/1000;
        long startTime = endTime-24*60*60*2;
        XmPluginHostApi.instance().getUserDeviceData(mDeviceStat.model, mDeviceStat.did,"event", "button_pressed" , startTime, endTime, new Callback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                addInfo(true, "testGetDeviceHistoryEvent");
            }

            @Override
            public void onFailure(int code, String info) {
                addInfo(false, "testGetDeviceHistoryEvent", "error:" + code + " " + info);
            }
        });

    }
    public void testGetDeviceHistoryProp(){
        start("testGetDeviceHistoryProp");

        long endTime = System.currentTimeMillis()/1000;
        long startTime = endTime-24*60*60*2;
        XmPluginHostApi.instance().getUserDeviceData(mDeviceStat.model, mDeviceStat.did,  "prop","temperature", startTime, endTime, new Callback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                addInfo(true, "testGetDeviceHistoryProp");
            }

            @Override
            public void onFailure(int code, String info) {
                addInfo(false, "testGetDeviceHistoryProp", "error:" + code + " " + info);
            }
        });
    }

    public void testBarcodeCodec(){
        AsyncTask<Void,Void,Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                start("testBarcodeCodec");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                String barcode = "testBarcodeCodec";
                Bitmap bitmap = XmPluginHostApi.instance().encodeBarcode(barcode,300,300);
                String decodeBarcode = XmPluginHostApi.instance().decodeBarcode(bitmap);
                return barcode.equals(decodeBarcode);
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                addInfo(aVoid, "testBarcodeCodec");
            }
        };
        task.execute();
    }

    public synchronized void start(String method) {
        if (mTestStatusHasmap.containsKey(method)) {
            throw new RuntimeException("method:" + method + " already exists");
        }
        TestStatus testStatus = new TestStatus(method);
        testStatus.startTime = System.currentTimeMillis();
        testStatus.startThread = Thread.currentThread().getId();
        mTestStatusHasmap.put(method, testStatus);
        mTestStatusList.add(testStatus);
    }

    public synchronized void addInfo(final boolean bl, final String method) {
        addInfo(bl, method, null);
    }

    public synchronized void addInfo(final boolean bl, final String method, final Object info) {
        Log.d(TAG,"thread:"+Thread.currentThread().getName()+" "+Thread.currentThread().getId()+" method:"+method);
        TestStatus testStatus = mTestStatusHasmap.get(method);
        if (testStatus == null) {
            testStatus = new TestStatus(method);
            mTestStatusHasmap.put(method, testStatus);
            mTestStatusList.add(testStatus);
        }
        testStatus.status = bl ? 1 : 2;
        testStatus.methodName = method;
        testStatus.result = info != null ? info.toString() : null;
        if(testStatus.startTime>0){
            testStatus.consumeTime = System.currentTimeMillis()-testStatus.startTime;
        }
        if(testStatus.startThread>0){
            testStatus.endThread = Thread.currentThread().getId();
        }
        if(testStatus.startThread!=testStatus.endThread){
            testStatus.status = 2;
            testStatus.result+="--- callback not in same Thread";
        }
    }

    public synchronized void refresh() {
        mTextInfo = new StringBuffer();
        long currentTimeMillis = System.currentTimeMillis();
        for (TestStatus testStatus : mTestStatusList) {
            if (testStatus.status == 0) {
                mTextInfo.append("<font size =\"10\" color = \"blue\">");
                mTextInfo.append(testStatus.methodName);
                mTextInfo.append("----");
                mTextInfo.append("processing:"+(currentTimeMillis-testStatus.startTime));
                mTextInfo.append("</font> <br>");
            } else if (testStatus.status == 1) {
                mTextInfo.append("<font size =\"10\" color = \"black\">");
                mTextInfo.append(testStatus.methodName);
                if (!TextUtils.isEmpty(testStatus.result)) {
                    mTextInfo.append("----");
                    mTextInfo.append(testStatus.result);
                }
                if(testStatus.consumeTime>0){
                    mTextInfo.append(" time:"+testStatus.consumeTime);
                }
                mTextInfo.append("</font> <br>");
            } else if (testStatus.status == 2) {
                mTextInfo.append("<font size =\"10\" color = \"red\">");
                mTextInfo.append(testStatus.methodName);
                if (!TextUtils.isEmpty(testStatus.result)) {
                    mTextInfo.append("----");
                    mTextInfo.append(testStatus.result);
                }
                if(testStatus.consumeTime>0){
                    mTextInfo.append(" time:"+testStatus.consumeTime);
                }
                mTextInfo.append("</font> <br>");
            }
        }
        mTextView.setHtmlFromString(mTextInfo.toString(), new HtmlTextView.RemoteImageGetter());
    }
}
