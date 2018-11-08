package com.xiaomi.smarthome.device.api.spec.instance;


import com.xiaomi.smarthome.device.api.spec.definitions.ActionDefinition;

/**
 * Created by wangyh on 18-10-22.
 */

public class SpecAction {
    private int iid;
    private ActionDefinition actionDefinition;

    public SpecAction(int iid, ActionDefinition actionDefinition) {
        this.iid = iid;
        this.actionDefinition = actionDefinition;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public ActionDefinition getActionDefinition() {
        return actionDefinition;
    }

    public void setActionDefinition(ActionDefinition actionDefinition) {
        this.actionDefinition = actionDefinition;
    }
}
