package com.xiaomi.smarthome.device.api.spec.operation;

import java.util.ArrayList;
import java.util.List;

public class ActionParam {
    private String did;
    private int siid;
    private int aiid;
    private List<Object> in = new ArrayList<>();
    private List<Object> out = new ArrayList<>();
    private int resultCode = -1;

    public ActionParam() {
    }

    public ActionParam(String did, int siid, int aiid, List<Object> in) {
        this.did = did;
        this.siid = siid;
        this.aiid = aiid;
        this.in = in;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public int getSiid() {
        return siid;
    }

    public void setSiid(int siid) {
        this.siid = siid;
    }

    public int getAiid() {
        return aiid;
    }

    public void setAiid(int aiid) {
        this.aiid = aiid;
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

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
