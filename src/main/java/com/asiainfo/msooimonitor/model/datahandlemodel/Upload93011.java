package com.asiainfo.msooimonitor.model.datahandlemodel;

public class Upload93011 {
    private String rows;
    private String phoneNo;

    public Upload93011(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Upload93011() {
        super();
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo == null ? null : phoneNo.trim();
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return rows+"€"
                + phoneNo ;
    }
}