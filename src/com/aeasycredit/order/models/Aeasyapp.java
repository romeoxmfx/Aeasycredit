package com.aeasycredit.order.models;

import com.google.gson.annotations.SerializedName;

public class Aeasyapp {
    private String serialNumber;
    private String version;
    private String method;
    private String usercode;
    @SerializedName("private")
    private String mprivate;
    private String uuid;
    private RequestBody requestBody;
    private ResponseBody responseBody;
    private String responseCode;
    
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getUsercode() {
        return usercode;
    }
    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
    public String getMprivate() {
        return mprivate;
    }
    public void setMprivate(String mprivate) {
        this.mprivate = mprivate;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public RequestBody getRequestBody() {
        return requestBody;
    }
    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }
    public ResponseBody getResponseBody() {
        return responseBody;
    }
    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }
    public String getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    
}