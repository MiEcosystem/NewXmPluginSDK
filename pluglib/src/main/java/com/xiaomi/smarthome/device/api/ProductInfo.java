package com.xiaomi.smarthome.device.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangchong3@xiaomi.com on 2018/9/4
 *
 * 获取产品基本信息
 *
 * ApiLevel: 69
 */
public class ProductInfo implements Parcelable {
    /**
     * 产品id
     */
    public int productId;
    /**
     * 产品model
     */
    public String model;
    /**
     * 产品名称
     */
    public String name = "";
    /**
     * 设备实物图
     */
    public String iconReal = "";
    /**
     * 产品描述信息
     */
    public String desc = "";

    public ProductInfo() {

    }

    public ProductInfo(Parcel in) {
        productId = in.readInt();
        model = in.readString();
        name = in.readString();
        iconReal = in.readString();
        desc = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void writeString(String str, Parcel dest) {
        if (str == null) {
            dest.writeString("");
        } else {
            dest.writeString(str);
        }
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        writeString(model, dest);
        writeString(name, dest);
        writeString(iconReal, dest);
        writeString(desc, dest);
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {

        @Override
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };

    @Override
    public String toString() {
        return "ProductInfo{" +
                "productId=" + productId +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", iconReal='" + iconReal + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
