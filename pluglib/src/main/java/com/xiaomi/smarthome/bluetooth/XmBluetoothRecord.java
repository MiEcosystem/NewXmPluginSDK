package com.xiaomi.smarthome.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liwentian on 2016/2/29.
 * 蓝牙上报数据格式
 */
public class XmBluetoothRecord implements Parcelable {

    public static final String TYPE_PROP = "prop";
    public static final String TYPE_EVENT = "event";

    public String type;
    public String key;
    public String value;
    public String trigger;

    public XmBluetoothRecord() {
    }

    protected XmBluetoothRecord(Parcel in) {
        type = in.readString();
        key = in.readString();
        value = in.readString();
        trigger = in.readString();
    }

    public static final Creator<XmBluetoothRecord> CREATOR = new Creator<XmBluetoothRecord>() {
        @Override
        public XmBluetoothRecord createFromParcel(Parcel in) {
            return new XmBluetoothRecord(in);
        }

        @Override
        public XmBluetoothRecord[] newArray(int size) {
            return new XmBluetoothRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(key);
        dest.writeString(value);
        dest.writeString(trigger);
    }

    public JSONObject toJson() throws JSONException {
        if (!TYPE_PROP.equals(type) && !TYPE_EVENT.equals(type)) {
            throw new IllegalArgumentException("Record's type should be prop or event");
        }

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Record's key and value should not be empty");
        }

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", type);
        jsonObj.put("key", key);
        jsonObj.put("value", value);

        if (!TextUtils.isEmpty(trigger)) {
            jsonObj.put("trigger", trigger);
        }

        return jsonObj;
    }
}
