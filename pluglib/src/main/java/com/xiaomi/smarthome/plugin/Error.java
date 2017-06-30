
package com.xiaomi.smarthome.plugin;

/**
 * Created by chenhao on 16/6/15.
 */
public class Error {
    private int mCode;
    private String mDetail;

    public Error(int code, String detail) {
        mCode = code;
        mDetail = detail;
    }

    final public int getCode() {
        return mCode;
    }

    final public String getDetail() {
        return mDetail;
    }

    @Override
    public String toString() {
        return "Error{" +
                "mCode=" + mCode +
                ", mDetail='" + mDetail + '\'' +
                '}';
    }
}
