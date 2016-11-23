
package com.xiaomi.smarthome.device.api;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设备状态数据
 */
public class DeviceStat implements Parcelable {

    // 设备是否是本地设备
    public static final int UNKNOWN = 0;
    public static final int LOCAL = 1;
    public static final int REMOTE = 2;

    public String did = "";// 设备唯一id
    public String name = "";// 名字
    public String mac = "";// mac地址
    public String model = "";// model名
    public String extrainfo = "";// 额外附加信息json格式
    public String ip = "";// ip地址
    public String parentId;// 子设备时表示父设备id
    public String parentModel;// 子设备时表示父设备mode
    public int bindFlag;// 1表示绑定 0表示未绑定
    public int authFlag;// 1表示分享设备，0表示未分享设备

    public String token;
    public String userId = "";
    public int location;// 本地设备还是远程设备
    public double latitude;
    public double longitude;
    public String bssid;
    public long lastModified;
    public int pid;// 设备pid
    public int rssi;
    public boolean isOnline;// 是否在线
    public int resetFlag;
    public String ssid;
    public String ownerName;// 分享设备的拥有者
    public String ownerId;
    public JSONObject propInfo;
    public String version;
    public Bundle property = new Bundle();
    /**
     * ApiLevel:6
     */
    public int showMode;//是否在设备列表显示
    /**
     * ApiLevel:8
     */
    public String event;
    /**
     * ApiLevel:10
     */
    public int permitLevel;

    /**
     * ApiLevel:29
     */
    public int isSetPinCode;

    /**
     * ApiLevel:32
     * 设备实物图
     */
    public String deviceIconReal;

    public DeviceStat() {
        did = "";
        name = "";
        mac = "";
        model = "";
        extrainfo = "";
        event = "";
    }

    public DeviceStat(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceStat))
            return false;
        DeviceStat other = (DeviceStat) obj;
        if (did != null) {
            return did.equals(other.did);
        } else {
            return false;
        }
    }

    void readFromParcel(Parcel in) {
        did = in.readString();
        name = in.readString();
        mac = in.readString();
        model = in.readString();
        extrainfo = in.readString();
        ip = in.readString();
        parentId = in.readString();
        parentModel = in.readString();
        bindFlag = in.readInt();
        authFlag = in.readInt();
        token = in.readString();
        userId = in.readString();
        location = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        bssid = in.readString();
        lastModified = in.readLong();
        pid = in.readInt();
        rssi = in.readInt();
        isOnline = in.readInt() != 0;
        resetFlag = in.readInt();
        ssid = in.readString();
        ownerName = in.readString();
        ownerId = in.readString();
        String prop = in.readString();
        propInfo = null;
        if (!TextUtils.isEmpty(prop)) {
            try {
                propInfo = new JSONObject(prop);
            } catch (JSONException e) {
            }
        }
        version = in.readString();
        property = in.readBundle();
        showMode = in.readInt();
        event = in.readString();
        permitLevel = in.readInt();
        isSetPinCode = in.readInt();
        deviceIconReal = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub 34
        return 0;
    }

    public int hashCode() {
        return did.hashCode();
    }

    void writeString(String str, Parcel dest) {
        if (str == null) {
            dest.writeString("");
        } else {
            dest.writeString(str);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeString(did, dest);
        writeString(name, dest);
        writeString(mac, dest);
        writeString(model, dest);
        writeString(extrainfo, dest);
        writeString(ip, dest);
        writeString(parentId, dest);
        writeString(parentModel, dest);
        dest.writeInt(bindFlag);
        dest.writeInt(authFlag);
        writeString(token, dest);
        writeString(userId, dest);
        dest.writeInt(location);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        writeString(bssid, dest);
        dest.writeLong(lastModified);
        dest.writeInt(pid);
        dest.writeInt(rssi);
        dest.writeInt(isOnline ? 1 : 0);
        dest.writeInt(resetFlag);
        writeString(ssid, dest);
        writeString(ownerName, dest);
        writeString(ownerId, dest);
        if (propInfo == null) {
            dest.writeString("");
        } else {
            dest.writeString(propInfo.toString());
        }
        writeString(version, dest);
        if (property == null)
            property = new Bundle();
        dest.writeBundle(property);
        dest.writeInt(showMode);
        writeString(event, dest);
        dest.writeInt(permitLevel);
        dest.writeInt(isSetPinCode);
        dest.writeString(deviceIconReal);
    }

    public static final Creator<DeviceStat> CREATOR = new Creator<DeviceStat>() {

        @Override
        public DeviceStat createFromParcel(Parcel source) {
            return new DeviceStat(source);
        }

        @Override
        public DeviceStat[] newArray(int size) {
            return new DeviceStat[size];
        }
    };
}
