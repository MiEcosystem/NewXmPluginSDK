
package com.xiaomi.smarthome.device.api;

/**
 * ApiLevel: 13 Created by chenhao on 15/10/29.
 */
public class KeyValuePair {
    private final String key;
    private final String value;

    /**
     * ApiLevel: 13
     * 
     * @param name
     * @param value
     */
    public KeyValuePair(final String name, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.key = name;
        this.value = value;
    }

    /**
     * ApiLevel: 13
     * 
     * @return
     */
    public String getKey() {
        return this.key;
    }

    /**
     * ApiLevel: 13
     * 
     * @return
     */
    public String getValue() {
        return this.value;
    }
}
