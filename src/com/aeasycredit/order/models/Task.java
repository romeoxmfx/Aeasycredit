
package com.aeasycredit.order.models;

import java.util.List;

public class Task {
    private String taskid;
    private String loanBillNumber;
    private List<ClientInfo> clientInfos;
    private String clientName;
    private String clientPhone;
    private String investigateAddr;
    private String contactName;
    private String contactPhone;
    private String investigateName;
    private String appointInvestigateTime;
    private String remarks;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getLoanBillNumber() {
        return loanBillNumber;
    }

    public void setLoanBillNumber(String loanBillNumber) {
        this.loanBillNumber = loanBillNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getInvestigateAddr() {
        return investigateAddr;
    }

    public void setInvestigateAddr(String investigateAddr) {
        this.investigateAddr = investigateAddr;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getInvestigateName() {
        return investigateName;
    }

    public void setInvestigateName(String investigateName) {
        this.investigateName = investigateName;
    }

    public String getAppointInvestigateTime() {
        return appointInvestigateTime;
    }

    public void setAppointInvestigateTime(String appointInvestigateTime) {
        this.appointInvestigateTime = appointInvestigateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<ClientInfo> getClientInfos() {
        return clientInfos;
    }

    public void setClientInfos(List<ClientInfo> clientInfos) {
        this.clientInfos = clientInfos;
    }

    public static class ClientInfo {

        private String clientName;
        private String clientPhone;
        private String clientType;

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getClientPhone() {
            return clientPhone;
        }

        public void setClientPhone(String clientPhone) {
            this.clientPhone = clientPhone;
        }

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }

        @Override
        public String toString() {
            // return super.toString();
            return "客户类型:" + clientType + " 客户姓名:" + clientName + " 联系电话:" + clientPhone;
        }
    }
}
