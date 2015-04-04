package com.aeasycredit.order.models;

import java.util.List;

public class ResponseBody {

    private String uuid;
    private String errorMsg;
    private List<Task> task;
    
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public List<Task> getTask() {
        return task;
    }
    public void setTask(List<Task> task) {
        this.task = task;
    }
    
}
