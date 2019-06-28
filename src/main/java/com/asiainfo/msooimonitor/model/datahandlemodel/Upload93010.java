package com.asiainfo.msooimonitor.model.datahandlemodel;

public class Upload93010 {
    private String rowNumber;

    private String phoneNo;

    private String imei;

    private String idfa;

    private String activityName;

    private String orderDate;

    private String orderSource;

    private String monthType;

    public Upload93010(String rowNumber, String phoneNo, String imei, String idfa, String activityName, String orderDate, String orderSource, String monthType) {
        this.rowNumber = rowNumber;
        this.phoneNo = phoneNo;
        this.imei = imei;
        this.idfa = idfa;
        this.activityName = activityName;
        this.orderDate = orderDate;
        this.orderSource = orderSource;
        this.monthType = monthType;
    }

    public Upload93010() {
        super();
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber == null ? null : rowNumber.trim();
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo == null ? null : phoneNo.trim();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa == null ? null : idfa.trim();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate == null ? null : orderDate.trim();
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource == null ? null : orderSource.trim();
    }

    public String getMonthType() {
        return monthType;
    }

    public void setMonthType(String monthType) {
        this.monthType = monthType == null ? null : monthType.trim();
    }

    @Override
    public String toString() {
        return  rowNumber + "€"
                + phoneNo + "€"
                + imei + "€"
                + idfa + "€"
                + activityName + "€"
                + orderDate + "€"
                + orderSource + "€"
                + monthType ;
    }
}