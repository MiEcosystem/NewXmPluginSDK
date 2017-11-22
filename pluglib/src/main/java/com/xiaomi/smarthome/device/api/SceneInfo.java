package com.xiaomi.smarthome.device.api;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景信息类
 *
 * ApiLevel:8
 *
 */
public class SceneInfo implements Parcelable {

    /**
     * ApiLevel:8
     */
    public static final int LAUNCH_CLICK = 0;
    public static final int LAUNCH_TIMER = 1;
    public static final int LAUNCH_DEVICE = 2;
    public static final int LAUNCH_LEAVE_HOME = 3;
    public static final int LAUNCH_COME_HOME = 4;
    public static final int LAUNCH_MIKEY = 5;
    public static final int LAUNCH_MIBAND = 6;

    /**
     * ApiLevel:34
     */
    public static final int LAUNCH_PHONE_CALL = 7;
    public static final int LAUNCH_SMS_RECEIVED = 8;

    /**
     * ApiLevel:51
     */
    public static final int LAUNCH_HUMIDITY = 14;
    public static final int LAUNCH_AQI = 15;
    public static final int LAUNCH_SUN_RISE = 16;
    public static final int LAUNCH_SUN_SET = 17;
    public static final int LAUNCH_TEMPERATURE = 18;
    public static final int LAUNCH_COME_LOC = 19;
    public static final int LAUNCH_LEAVE_LOC = 20;



    public static final int ACTION_DELAY = 9;
    public static final int ACTION_EXEC_AUTONMATION = 10;
    public static final int ACTION_PUSH = 11;
    public static final int ACTION_EXEC_SCENE = 12;
    public static final int ACTION_DEVICE = 13;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSceneId);
        dest.writeInt(mRecommId);
        dest.writeString(mName);
        dest.writeInt(mEnable ? 1 : 0);
        dest.writeValue(mLaunch);
        dest.writeList(mLaunchList);
        dest.writeList(mActions);
        dest.writeInt(mType);
        dest.writeInt(mStatus);
    }

    public SceneInfo() {

    }

    public SceneInfo(Parcel in) {
        readFromParcel(in);
    }

    void readFromParcel(Parcel in) {
        mSceneId = in.readInt();
        mRecommId = in.readInt();
        mName = in.readString();
        mEnable = in.readInt() == 1;
        mLaunch = (SceneLaunch) in.readValue(SceneLaunch.class.getClassLoader());
        mLaunchList = new ArrayList<SceneLaunch>();
        in.readList(mLaunchList, SceneLaunch.class.getClassLoader());
        mActions = new ArrayList<SceneAction>();
        in.readList(mActions, SceneAction.class.getClassLoader());
        mType = in.readInt();
        mStatus = in.readInt();
    }

    /**
     * ApiLevel:8
     */
    public int mSceneId;
    /**
     * ApiLevel:8
     */
    public int mRecommId;
    /**
     * ApiLevel:8
     */
    public String mName;
    /**
     * ApiLevel:8
     */
    public boolean mEnable;
    /**
     * ApiLevel:8
     */
    @Deprecated
    public SceneLaunch mLaunch;
    /**
     * ApiLevel:16
     */
    public List<SceneLaunch> mLaunchList;
    /**
     * ApiLevel:8
     */
    public List<SceneAction> mActions;
    /**
     * ApiLevel:22
     */
    public int mType;
    /**
     * ApiLevel:22
     */
    public int mStatus;

    /**
     * ApiLevel:8
     */
    public static class SceneLaunch implements Parcelable {
        /**
         * ApiLevel:8
         */
        public int mLaunchType;
        /**
         * ApiLevel:8
         */
        public String mLaunchName;
        /**
         * ApiLevel:8
         */
        public String mDeviceModel;
        /**
         * ApiLevel:8
         */
        public String mEventString;
        /**
         * ApiLevel:8
         */
        public Object mEventValue;
        /**
         * ApiLevel:16
         */
        public String mDid;
        /**
         * ApiLevel:16
         */
        public String mExtra;

        public SceneLaunch() {

        }

        public SceneLaunch(Parcel in) {
            mLaunchType = in.readInt();
            mLaunchName = in.readString();
            mDeviceModel = in.readString();
            mEventString = in.readString();
            mEventValue = in.readValue(ClassLoader.getSystemClassLoader());
            mDid = in.readString();
            mExtra = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mLaunchType);
            dest.writeString(mLaunchName);
            dest.writeString(mDeviceModel);
            dest.writeString(mEventString);
            if(mEventValue instanceof JSONArray || mEventValue instanceof JSONObject)
                dest.writeValue(mEventValue.toString());
            else
                dest.writeValue(mEventValue);
            dest.writeString(mDid);
            dest.writeString(mExtra);
        }

        public static final Creator<SceneLaunch> CREATOR = new Creator<SceneLaunch>() {

            @Override
            public SceneLaunch createFromParcel(Parcel source) {
                return new SceneLaunch(source);
            }

            @Override
            public SceneLaunch[] newArray(int size) {
                return new SceneLaunch[size];
            }
        };
    }

    /**
     * ApiLevel:8
     */
    public static class SceneAction implements Parcelable{
        /**
         * ApiLevel:8
         */
        public String mDeviceName;
        /**
         * ApiLevel:8
         */
        public String mDeviceModel;
        /**
         * ApiLevel:8
         */
        public String mActionName;
        /**
         * ApiLevel:8
         */
        public String mActionString;
        /**
         * ApiLevel:16
         */
        public Object mActionValue;
        /**
         * ApiLevel:16
         */
        public String mDid;
        /**
         * ApiLevel:16
         */
        public String mExtra;
        /**
         * ApiLevel:18
         */
        public int mDelayTime;
        /**
         * ApiLevel:34
         */
        public int mActionType;

        public SceneAction() {

        }

        public SceneAction(Parcel in) {
            mDeviceName = in.readString();
            mDeviceModel = in.readString();
            mActionName = in.readString();
            mActionString = in.readString();
            mActionValue = in.readValue(ClassLoader.getSystemClassLoader());
            mDid = in.readString();
            mExtra = in.readString();
            mDelayTime = in.readInt();
            mActionType = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mDeviceName);
            dest.writeString(mDeviceModel);
            dest.writeString(mActionName);
            dest.writeString(mActionString);
            if(mActionValue instanceof JSONArray || mActionValue instanceof JSONObject)
                dest.writeValue(mActionValue.toString());
            else
                dest.writeValue(mActionValue);
            dest.writeString(mDid);
            dest.writeString(mExtra);
            dest.writeInt(mDelayTime);
            dest.writeInt(mActionType);
        }

        public static final Creator<SceneAction> CREATOR = new Creator<SceneAction>() {

            @Override
            public SceneAction createFromParcel(Parcel source) {
                return new SceneAction(source);
            }

            @Override
            public SceneAction[] newArray(int size) {
                return new SceneAction[size];
            }
        };
    }

    public static final Creator<SceneInfo> CREATOR = new Creator<SceneInfo>() {

        @Override
        public SceneInfo createFromParcel(Parcel source) {
            return new SceneInfo(source);
        }

        @Override
        public SceneInfo[] newArray(int size) {
            return new SceneInfo[size];
        }
    };
}