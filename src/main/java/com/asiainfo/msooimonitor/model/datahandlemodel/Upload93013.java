package com.asiainfo.msooimonitor.model.datahandlemodel;

public class Upload93013 {
    private String rowNumber;

    private String phoneNo;

    private String ktQqwFlag;

    private String qqwList;

    public Upload93013(String rowNumber, String phoneNo, String ktQqwFlag, String qqwList) {
        this.rowNumber = rowNumber;
        this.phoneNo = phoneNo;
        this.ktQqwFlag = ktQqwFlag;
        this.qqwList = qqwList;
    }

    public Upload93013() {
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

    public String getKtQqwFlag() {
        return ktQqwFlag;
    }

    public void setKtQqwFlag(String ktQqwFlag) {
        this.ktQqwFlag = ktQqwFlag == null ? null : ktQqwFlag.trim();
    }

    public String getQqwList() {
        return qqwList;
    }

    public void setQqwList(String qqwList) {
        this.qqwList = qqwList == null ? null : qqwList.trim();
    }

    @Override
    public String toString() {
        return  rowNumber +"€"
                + phoneNo + "€"
                + ktQqwFlag + "€"
                + qqwList ;
    }
}