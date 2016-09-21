package com.xiaomi.smarthome.upnp;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.KeyValuePair;

import java.util.List;

/**
 * Created by swx on 4/25/16.
 * new api for UPnP
 */
public abstract class UPnPApi {
    protected volatile static UPnPApi _instance = null;

    public static UPnPApi instance() {
        return _instance;
    }

    /**
     * Api level 23
     *
     * @param did        udn
     * @param serviceId  service id
     * @param actionName action name
     * @param params     parameters
     */
    public abstract void invokeServiceAction(
            String did,
            String serviceId,
            String actionName,
            List<KeyValuePair> params,
            Callback<String> callback);

    /**
     * Api level 23
     * @param did device id
     * @param name node name
     * @return node value
     */
    public abstract String getRootNodeValue(String did, String name) ;

    /**
     * Api level 23
     */

    public static final String EVENT_KEY = "event.data.key";

    public static class EventData implements Parcelable {
        public String udn;
        public long seq;
        public String name;
        public String value;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.udn);
            dest.writeLong(this.seq);
            dest.writeString(this.name);
            dest.writeString(this.value);
        }

        public EventData() {
        }

        protected EventData(Parcel in) {
            this.udn = in.readString();
            this.seq = in.readLong();
            this.name = in.readString();
            this.value = in.readString();
        }

        public static final Parcelable.Creator<EventData> CREATOR = new Parcelable.Creator<EventData>() {
            @Override
            public EventData createFromParcel(Parcel source) {
                return new EventData(source);
            }

            @Override
            public EventData[] newArray(int size) {
                return new EventData[size];
            }
        };
    }

}
