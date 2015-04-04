package com.aeasycredit.order.models;

public class InspectTask {
    private int id;
    private String customerName;
    private String inspectDate;
    private String inspectAddress;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getInspectDate() {
        return inspectDate;
    }
    public void setInspectDate(String inspectDate) {
        this.inspectDate = inspectDate;
    }
    public String getInspectAddress() {
        return inspectAddress;
    }
    public void setInspectAddress(String inspectAddress) {
        this.inspectAddress = inspectAddress;
    }
}
