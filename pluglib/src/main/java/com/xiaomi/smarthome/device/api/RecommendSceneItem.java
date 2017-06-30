package com.xiaomi.smarthome.device.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 推荐场景信息类
 *
 * ApiLevel:8
 */
public class RecommendSceneItem implements Parcelable {

    /**
     * ApiLevel:8
     */
    public int mRecommId;
    /**
     * ApiLevel:8
     */
    public String mName;
    /**
     * ApiLevel:16
     */
    public RemommendSceneCondition[] mRecommendConditionList;
    /**
     * ApiLevel:8
     */
    public RemommendSceneAction[] mRecommendActionList;
    /**
     * ApiLevel:17
     */
    public double mRecommLevel;
    /**
     * ApiLevel:17
     */
    public boolean mEnablePush;
    /**
     * ApiLevel:17
     */
    public boolean mShowInMainPage;

    /**
     * ApiLevel:17
     */
    public String mIcon;

    /**
     * ApiLevel:17
     */
    public int mStatus;

    /**
     * ApiLevel:8
     */
    public static class Key {
        public String mKey;
        public String mName;
        public Object mValues;
        public void writeToParcel(Parcel parcel) {
            parcel.writeString(mKey);
            parcel.writeValue(mValues);
            parcel.writeString(mName);
        }

        public void readFromParcel(Parcel in) {
            mKey = in.readString();
            mValues = in.readValue(ClassLoader.getSystemClassLoader());
            mName = in.readString();
        }
    }

    public static class RemommendSceneCondition {
        /**
         * ApiLevel:8
         */
        public String [] mDeviceModels;
        /**
         * ApiLevel:8
         */
        public String mConditionName;
        /**
         * ApiLevel:8
         */
        public Key[] mKeys;
        /**
         * ApiLevel:8
         */
        public String mProductId;
        /**
         * ApiLevel:8
         */
        public Boolean mAddAllDevice = false;

        public String mSrc;

        public int tempId;
        public void writeToParcel(Parcel parcel) {
            parcel.writeStringArray(mDeviceModels);
            parcel.writeString(mConditionName);
            if(mKeys != null) {
                parcel.writeInt(mKeys.length);
                for(int j=0; j<mKeys.length; j++) {
                    mKeys[j].writeToParcel(parcel);
                }
            }
            else
                parcel.writeInt(0);
            parcel.writeString(mProductId);
            parcel.writeValue(mAddAllDevice);
            parcel.writeString(mSrc);
        }

        public void readFromParcel(Parcel parcel) {
            mDeviceModels = parcel.createStringArray();
            mConditionName = parcel.readString();
            int size = parcel.readInt();
            if(size != 0) {
                mKeys = new Key[size];
                for(int i=0; i<size; i++) {
                    mKeys[i] = new Key();
                    mKeys[i].readFromParcel(parcel);
                }
            }
            mProductId = parcel.readString();
            mAddAllDevice = (Boolean) parcel.readValue(ClassLoader.getSystemClassLoader());
            mSrc = parcel.readString();
        }
    }

    public static class RemommendSceneAction {
        /**
         * ApiLevel:8
         */
        public String [] mDeviceModels;
        /**
         * ApiLevel:8
         */
        public String mActionName;
        /**
         * ApiLevel:8
         */
        public Key[] mKeys;
        /**
         * ApiLevel:8
         */
        public String mProductId;
        /**
         * ApiLevel:8
         */
        public Boolean mAddAllDevice;

        public int tempId;
        public void writeToParcel(Parcel parcel) {
            parcel.writeStringArray(mDeviceModels);
            parcel.writeString(mActionName);
            if(mKeys != null) {
                parcel.writeInt(mKeys.length);
                for(int j=0; j<mKeys.length; j++) {
                    mKeys[j].writeToParcel(parcel);
                }
            }
            else
                parcel.writeInt(0);
            parcel.writeString(mProductId);
            parcel.writeValue(mAddAllDevice);
        }

        public void readFromParcel(Parcel parcel) {
            mDeviceModels = parcel.createStringArray();
            mActionName = parcel.readString();
            int size = parcel.readInt();
            if(size != 0) {
                mKeys = new Key[size];
                for(int i=0; i<size; i++) {
                    mKeys[i] = new Key();
                    mKeys[i].readFromParcel(parcel);
                }
            }
            mProductId = parcel.readString();
            mAddAllDevice = (Boolean) parcel.readValue(ClassLoader.getSystemClassLoader());
        }

        @Override
        public String toString() {
            return "RemommendSceneAction{" +
                    "mDeviceModels=" + Arrays.toString(mDeviceModels) +
                    ", mActionName='" + mActionName + '\'' +
                    ", mKeys=" + Arrays.toString(mKeys) +
                    ", mProductId='" + mProductId + '\'' +
                    ", mAddAllDevice=" + mAddAllDevice +
                    ", tempId=" + tempId +
                    '}';
        }
    }

    public RecommendSceneItem(Parcel in) {
        mRecommId = in.readInt();
        mName = in.readString();
        mIcon = in.readString();
        mRecommLevel = in.readDouble();
        mEnablePush = (boolean) in.readValue(ClassLoader.getSystemClassLoader());
        int sizeCondition = in.readInt();
        if(sizeCondition > 0) {
            mRecommendConditionList = new RemommendSceneCondition[sizeCondition];
            for(int i=0; i<sizeCondition; i++) {
                mRecommendConditionList[i] = new RemommendSceneCondition();
                mRecommendConditionList[i].readFromParcel(in);
            }
        }

        int sizeAction = in.readInt();
        if(sizeAction > 0) {
            mRecommendActionList = new RemommendSceneAction[sizeAction];
            for(int i=0; i<sizeAction; i++) {
                mRecommendActionList[i] = new RemommendSceneAction();
                mRecommendActionList[i].readFromParcel(in);
            }
        }

        mStatus = in.readInt();
    }

    public RecommendSceneItem() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mRecommId);
        parcel.writeString(mName);
        parcel.writeString(mIcon);
        parcel.writeDouble(mRecommLevel);
        parcel.writeValue(mEnablePush);
        parcel.writeInt(mRecommendConditionList.length);
        for(int j=0; j<mRecommendConditionList.length; j++) {
            mRecommendConditionList[j].writeToParcel(parcel);
        }

        parcel.writeInt(mRecommendActionList.length);
        for(int j=0; j<mRecommendActionList.length; j++) {
            mRecommendActionList[j].writeToParcel(parcel);
        }

        parcel.writeInt(mStatus);
    }

    public static final Creator<RecommendSceneItem> CREATOR = new Creator<RecommendSceneItem>() {

        @Override
        public RecommendSceneItem createFromParcel(Parcel in) {
            return new RecommendSceneItem(in);
        }

        @Override
        public RecommendSceneItem[] newArray(int size) {
            return new RecommendSceneItem[size];
        }
    };

    @Override
    public String toString() {
        return "RecommendSceneItem{" +
                "mRecommId=" + mRecommId +
                ", mName='" + mName + '\'' +
                ", mRecommendConditionList=" + Arrays.toString(mRecommendConditionList) +
                ", mRecommendActionList=" + Arrays.toString(mRecommendActionList) +
                ", mRecommLevel=" + mRecommLevel +
                ", mEnablePush=" + mEnablePush +
                ", mShowInMainPage=" + mShowInMainPage +
                ", mIcon='" + mIcon + '\'' +
                ", mStatus=" + mStatus +
                '}';
    }
}