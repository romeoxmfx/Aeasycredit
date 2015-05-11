package com.aeasycredit.order.models;

public class RequestBody {
    public static final int INVESTIGATE_TYPE_SECERT = 1;
    public static final int INVESTIGATE_TYPE_PERCEIVE = 0;
    
    public static final String ID = "id";
    public static final String TASKID = "taskid";
    public static final String INVESTIGATETYPE = "investigateType";
    public static final String INVESTIGATEENDTIME = "investigateEndTime";
    public static final String INVESTIGATESTARTTIME = "investigateStartTime";
    public static final String INVESTIGATEADDR = "investigateAddr"; 
    public static final String CONTACTNAME = "contactName"; 
    public static final String CONTACTPHONE = "contactPhone"; 
    public static final String CONTACTPOST = "contactPost"; 
    public static final String COMPANYNAME = "companyName"; 
    public static final String COMPANYNATURE = "companyNature"; 
    public static final String STAFFNUMBER = "staffNumber"; 
    public static final String BUSINESSAREA = "businessArea"; 
    public static final String ISHAVECOMPANYBOARD = "isHaveCompanyBoard"; 
    public static final String ISHAVECOMPANYNAMEATOFFICEAREA = "isHaveCompanyNameAtOfficeArea"; 
    public static final String SERVICECONTENT = "serviceContent"; 
    public static final String COMPANYSCALE = "companyScale"; 
    public static final String PRODUCTIONAPPARATUS = "productionApparatus"; 
    public static final String INVENTORY = "inventory"; 
    public static final String INTERVIEWCONTENT = "interviewContent"; 
    public static final String SUMMARY = "summary"; 
    public static final String OTHER = "other"; 
    public static final String FILES = "files"; 

    private int id;
    private String loginname;
    private String uuid;
    private String password;
    private String taskid;
    private String investigateType;
    private String investigateEndTime;
    private String investigateStartTime;
    private String investigateAddr;
    private String contactName;
    private String contactPhone;
    private String contactPost;
    private String companyName;
    private String companyNature;
    private String staffNumber;
    private String businessArea;
    private boolean isHaveCompanyBoard;
    private boolean isHaveCompanyNameAtOfficeArea;
    private String serviceContent;
    private String companyScale;
    private String productionApparatus;
    private String inventory;
    private String interviewContent;
    private String summary;
    private String other;
    private String imageSize;
//    @Expose(serialize=false)
    private String files;
    
    public String getUuid() {
        return uuid;
    }
    public String getTaskid() {
        return taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    public String getInvestigateType() {
        return investigateType;
    }
    public void setInvestigateType(String investigateType) {
        this.investigateType = investigateType;
    }
    public String getInvestigateEndTime() {
        return investigateEndTime;
    }
    public void setInvestigateEndTime(String investigateEndTime) {
        this.investigateEndTime = investigateEndTime;
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
    public String getContactPost() {
        return contactPost;
    }
    public void setContactPost(String contactPost) {
        this.contactPost = contactPost;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyNature() {
        return companyNature;
    }
    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature;
    }
    public String getStaffNumber() {
        return staffNumber;
    }
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }
    public String getBusinessArea() {
        return businessArea;
    }
    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }
    public boolean getIsHaveCompanyBoard() {
        return isHaveCompanyBoard;
    }
    public void setIsHaveCompanyBoard(boolean isHaveCompanyBoard) {
        this.isHaveCompanyBoard = isHaveCompanyBoard;
    }
    public boolean getIsHaveCompanyNameAtOfficeArea() {
        return isHaveCompanyNameAtOfficeArea;
    }
    public void setIsHaveCompanyNameAtOfficeArea(boolean isHaveCompanyNameAtOfficeArea) {
        this.isHaveCompanyNameAtOfficeArea = isHaveCompanyNameAtOfficeArea;
    }
    public String getServiceContent() {
        return serviceContent;
    }
    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }
    public String getCompanyScale() {
        return companyScale;
    }
    public void setCompanyScale(String companyScale) {
        this.companyScale = companyScale;
    }
    public String getProductionApparatus() {
        return productionApparatus;
    }
    public void setProductionApparatus(String productionApparatus) {
        this.productionApparatus = productionApparatus;
    }
    public String getInventory() {
        return inventory;
    }
    public void setInventory(String inventory) {
        this.inventory = inventory;
    }
    public String getInterviewContent() {
        return interviewContent;
    }
    public void setInterviewContent(String interviewContent) {
        this.interviewContent = interviewContent;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getOther() {
        return other;
    }
    public void setOther(String other) {
        this.other = other;
    }
    public String getImageSize() {
        return imageSize;
    }
    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFiles() {
        return files;
    }
    public void setFiles(String files) {
        this.files = files;
    }
    public String getLoginname() {
        return loginname;
    }
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    public String getInvestigateStartTime() {
        return investigateStartTime;
    }
    public void setInvestigateStartTime(String investigateStartTime) {
        this.investigateStartTime = investigateStartTime;
    }
    
}
