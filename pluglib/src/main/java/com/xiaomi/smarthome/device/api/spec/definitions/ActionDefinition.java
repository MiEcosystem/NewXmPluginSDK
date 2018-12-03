package com.xiaomi.smarthome.device.api.spec.definitions;

import java.util.ArrayList;
import java.util.List;

public class ActionDefinition {

    private String type;
    private String description;
    private List<Object> in = new ArrayList<>();
    private List<Object> out = new ArrayList<>();

    public ActionDefinition(){}

    public ActionDefinition(String type, String description) {
        this.type=type;
        this.description=description;
    }

    public ActionDefinition(String type,String description,List<Object> in,List<Object> out){
        this.type=type;
        this.description=description;
        this.in=in;
        this.out = out;
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

    public List<Object> getIn() {
        return in;
    }

    public void setIn(List<Object> in) {
        this.in = in;
    }

    public List<Object> getOut() {
        return out;
    }

    public void setOut(List<Object> out) {
        this.out = out;
    }
}