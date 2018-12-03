package com.xiaomi.smarthome.device.api.spec.operation;

public class PropertyParam {
    private String did;
    private int siid;
    private int piid;
    private Object value;
    private int resultCode = -1;

    public PropertyParam() {
    }

    public PropertyParam(String did, int siid, int piid) {
        this(did, siid, piid, null);
    }

    public PropertyParam(String did, int siid, int piid, Object value) {
        this.did = did;
        this.siid = siid;
        this.piid = piid;
        this.value = value;
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

    public int getPiid() {
        return piid;
    }

    public void setPiid(int piid) {
        this.piid = piid;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
