package com.xiaomi.smarthome.device.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;

public class RoomStat implements Parcelable {
    /**
     * bssid :
     * desc :
     * dids : ["56062796","12603439","blt.Yn5S7nXL4s9JIvuP","54877453"]
     * id : 434001000063
     * name : 客厅
     * parentid : 434001000061
     * shareflag : 0
     * icon:
     */

    public String bssid = "";
    public String desc = "";
    public String id = "";
    public String name = "";
    public String parentid = "";
    public int shareflag = 0;
    public volatile List<String> dids = Collections.emptyList();
    public String icon = "";

    public int hashCode() {
        return id.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof RoomStat))
            return false;
        RoomStat room = (RoomStat) obj;
        return !(room.id == null || id == null) && id.equalsIgnoreCase(room.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bssid);
        dest.writeString(this.desc);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.parentid);
        dest.writeInt(this.shareflag);
        dest.writeStringList(this.dids);
        dest.writeString(this.icon);
    }

    protected RoomStat(Parcel in) {
        this.bssid = in.readString();
        this.desc = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.parentid = in.readString();
        this.shareflag = in.readInt();
        this.dids = in.createStringArrayList();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<RoomStat> CREATOR = new Parcelable.Creator<RoomStat>() {
        @Override
        public RoomStat createFromParcel(Parcel source) {
            return new RoomStat(source);
        }

        @Override
        public RoomStat[] newArray(int size) {
            return new RoomStat[size];
        }
    };

    public RoomStat() {
    }
}
