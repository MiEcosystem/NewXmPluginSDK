package com.xiaomi.smarthome.device.api;

import android.os.Parcel;

/**
 * 设备标签信息
 *
 * Created by zengcheng on 2016/12/28.
 */

public class DeviceTag implements android.os.Parcelable {
    public static final int DEVICE_TAG_CATEGORY = 0;//品类
    public static final int DEVICE_TAG_ROUTER = 2;//路由
    public static final int DEVICE_TAG_CUSTOM = 4;//自定义
    public String infoJson;//  {"type1":["tag1","tag2"],"type2":["tag1","tag2","tag3"]}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeString(infoJson, dest);
    }
    public DeviceTag(){

    }
    private DeviceTag(Parcel in) {
        readFromParcel(in);
    }

    void readFromParcel(Parcel in) {
        infoJson = in.readString();
    }

    void writeString(String str, Parcel dest) {
        if (str == null) {
            dest.writeString("");
        } else {
            dest.writeString(str);
        }
    }

    public static final Creator<DeviceTag> CREATOR = new Creator<DeviceTag>() {

        @Override
        public DeviceTag createFromParcel(Parcel source) {
            return new DeviceTag(source);
        }

        @Override
        public DeviceTag[] newArray(int size) {
            return new DeviceTag[size];
        }
    };
}
