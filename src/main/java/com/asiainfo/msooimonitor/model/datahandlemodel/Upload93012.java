package com.asiainfo.msooimonitor.model.datahandlemodel;

public class Upload93012 {
    private String rowNumber;

    private String phoneNo;

    private String imei;

    private String idfa;

    private String tongyongFlowTotal;

    private String tongyongFlowUse;

    private String migudingxiangTotal;

    private String migudingxiangUse;

    private String miguShiping;

    private String aiqiyiShiping;

    private String tengxunShiping;

    private String youkuShiping;

    private String mangguoShiping;

    private String pptvShiping;

    public Upload93012(String rowNumber, String phoneNo, String imei, String idfa, String tongyongFlowTotal, String tongyongFlowUse, String migudingxiangTotal, String migudingxiangUse, String miguShiping, String aiqiyiShiping, String tengxunShiping, String youkuShiping, String mangguoShiping, String pptvShiping) {
        this.rowNumber = rowNumber;
        this.phoneNo = phoneNo;
        this.imei = imei;
        this.idfa = idfa;
        this.tongyongFlowTotal = tongyongFlowTotal;
        this.tongyongFlowUse = tongyongFlowUse;
        this.migudingxiangTotal = migudingxiangTotal;
        this.migudingxiangUse = migudingxiangUse;
        this.miguShiping = miguShiping;
        this.aiqiyiShiping = aiqiyiShiping;
        this.tengxunShiping = tengxunShiping;
        this.youkuShiping = youkuShiping;
        this.mangguoShiping = mangguoShiping;
        this.pptvShiping = pptvShiping;
    }

    public Upload93012() {
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

    public String getTongyongFlowTotal() {
        return tongyongFlowTotal;
    }

    public void setTongyongFlowTotal(String tongyongFlowTotal) {
        this.tongyongFlowTotal = tongyongFlowTotal == null ? null : tongyongFlowTotal.trim();
    }

    public String getTongyongFlowUse() {
        return tongyongFlowUse;
    }

    public void setTongyongFlowUse(String tongyongFlowUse) {
        this.tongyongFlowUse = tongyongFlowUse == null ? null : tongyongFlowUse.trim();
    }

    public String getMigudingxiangTotal() {
        return migudingxiangTotal;
    }

    public void setMigudingxiangTotal(String migudingxiangTotal) {
        this.migudingxiangTotal = migudingxiangTotal == null ? null : migudingxiangTotal.trim();
    }

    public String getMigudingxiangUse() {
        return migudingxiangUse;
    }

    public void setMigudingxiangUse(String migudingxiangUse) {
        this.migudingxiangUse = migudingxiangUse == null ? null : migudingxiangUse.trim();
    }

    public String getMiguShiping() {
        return miguShiping;
    }

    public void setMiguShiping(String miguShiping) {
        this.miguShiping = miguShiping == null ? null : miguShiping.trim();
    }

    public String getAiqiyiShiping() {
        return aiqiyiShiping;
    }

    public void setAiqiyiShiping(String aiqiyiShiping) {
        this.aiqiyiShiping = aiqiyiShiping == null ? null : aiqiyiShiping.trim();
    }

    public String getTengxunShiping() {
        return tengxunShiping;
    }

    public void setTengxunShiping(String tengxunShiping) {
        this.tengxunShiping = tengxunShiping == null ? null : tengxunShiping.trim();
    }

    public String getYoukuShiping() {
        return youkuShiping;
    }

    public void setYoukuShiping(String youkuShiping) {
        this.youkuShiping = youkuShiping == null ? null : youkuShiping.trim();
    }

    public String getMangguoShiping() {
        return mangguoShiping;
    }

    public void setMangguoShiping(String mangguoShiping) {
        this.mangguoShiping = mangguoShiping == null ? null : mangguoShiping.trim();
    }

    public String getPptvShiping() {
        return pptvShiping;
    }

    public void setPptvShiping(String pptvShiping) {
        this.pptvShiping = pptvShiping == null ? null : pptvShiping.trim();
    }

    @Override
    public String toString() {
        return rowNumber +"€"
                + phoneNo + "€"
                + imei + "€"
                + idfa + "€"
                + tongyongFlowTotal + "€"
                + tongyongFlowUse + "€"
                + migudingxiangTotal + "€"
                + migudingxiangUse +"€"
                + miguShiping + "€"
                + aiqiyiShiping + "€"
                + tengxunShiping + "€"
                + youkuShiping + "€"
                + mangguoShiping + "€"
                + pptvShiping ;
    }
}