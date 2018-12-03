package com.xiaomi.smarthome.device.api.spec.instance;

import java.util.HashMap;
import java.util.Map;

public class SpecService {
    private int iid;
    private String type;
    private String description;
    private Map<Integer, SpecProperty> properties = new HashMap<>();
    private Map<Integer, SpecAction> actions = new HashMap<>();

    public SpecService(int iid, String type, String description) {
        this.iid = iid;
        this.type = type;
        this.description = description;
    }

    public SpecService(int iid, String type, String description, Map<Integer, SpecProperty> properties, Map<Integer, SpecAction> actions) {
        this.iid = iid;
        this.type = type;
        this.description = description;
        this.properties = properties;
        this.actions = actions;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public Map<Integer, SpecProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<Integer, SpecProperty> properties) {
        this.properties = properties;
    }

    public Map<Integer, SpecAction> getActions() {
        return actions;
    }

    public void setActions(Map<Integer, SpecAction> actions) {
        this.actions = actions;
    }

}
