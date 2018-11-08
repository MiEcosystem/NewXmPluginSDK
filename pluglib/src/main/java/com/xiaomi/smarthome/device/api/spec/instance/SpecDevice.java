package com.xiaomi.smarthome.device.api.spec.instance;

import java.util.HashMap;
import java.util.Map;

public class SpecDevice {
    private String type;
    private String description;
    private Map<Integer, SpecService> services = new HashMap<>();

    public SpecDevice(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public SpecDevice(String type, String description, Map<Integer, SpecService> services) {
        this.type = type;
        this.description = description;
        this.services = services;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Integer, SpecService> getServices() {
        return services;
    }

    public void setServices(Map<Integer, SpecService> services) {
        this.services = services;
    }

}
