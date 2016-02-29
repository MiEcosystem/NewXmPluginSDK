package com.xiaomi.smarthome.device.api;

import android.os.Parcel;
import android.os.Parcelable;

//用户信息
//ApiLevel:10小米用户信息
public class UserInfo implements Parcelable {
    public String userId;
    public String nickName;
    public String url;
    public String localPath;
    public long shareTime;
    public String phone;
    public String email;
    public String sex;
    public String birth;

    public UserInfo() {
    }

    public UserInfo(Parcel in) {
        readFromParcel(in);
    }

    void writeString(String str, Parcel dest) {
        if (str == null) {
            dest.writeString("");
        } else {
            dest.writeString(str);
        }
    }

    void readFromParcel(Parcel in) {
        userId = in.readString();
        nickName = in.readString();
        url = in.readString();
        localPath = in.readString();
        shareTime = in.readLong();
        phone = in.readString();
        email = in.readString();
        sex = in.readString();
        birth = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeString(userId, dest);
        writeString(nickName, dest);
        writeString(url, dest);
        writeString(localPath, dest);
        dest.writeLong(shareTime);
        writeString(phone, dest);
        writeString(email, dest);
        writeString(sex, dest);
        writeString(birth, dest);

    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {

        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
