package com.xiaomi.xmplugindemo;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelData implements Parcelable {
    public int mData;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<ParcelData> CREATOR
            = new Parcelable.ClassLoaderCreator<ParcelData>() {
        public ParcelData createFromParcel(Parcel in) {
            return new ParcelData(in);
        }

        public ParcelData[] newArray(int size) {
            return new ParcelData[size];
        }

        @Override
        public ParcelData createFromParcel(Parcel source, ClassLoader loader) {
            return new ParcelData(source);
        }
    };
    
    private ParcelData(Parcel in) {
        mData = in.readInt();
    }
    public ParcelData(){
        
    }
}