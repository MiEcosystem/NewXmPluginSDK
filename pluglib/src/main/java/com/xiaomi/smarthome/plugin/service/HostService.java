
package com.xiaomi.smarthome.plugin.service;

/**
 * Created by chenhao on 16/8/2.
 */
public enum HostService {
    /**
     * ApiLevel: 27
     */
    OneMore("OneMore"),

    /**
     * ApiLevel: 32
     */
    DesaiShoe("DesaiShoe");

    private String mValue;

    HostService(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
