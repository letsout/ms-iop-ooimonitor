package com.asiainfo.msooimonitor.model.datahandlemodel;

public class Upload93006 {
    private String rows;

    private String nowtime;

    private String province;

    private String city;

    private String phoneNo;

    private String activityId;

    private String activityName;

    private String campId;

    private String campName;

    private String pv;

    private String clickNum;

    private String transNum;

    private String isTouch;

    private String isJoin;

    private String isMarketing;

    public Upload93006(String rows, String nowtime, String province, String city, String phoneNo, String activityId, String activityName, String campId, String campName, String pv, String clickNum, String transNum, String isTouch, String isJoin, String isMarketing) {
        this.rows = rows;
        this.nowtime = nowtime;
        this.province = province;
        this.city = city;
        this.phoneNo = phoneNo;
        this.activityId = activityId;
        this.activityName = activityName;
        this.campId = campId;
        this.campName = campName;
        this.pv = pv;
        this.clickNum = clickNum;
        this.transNum = transNum;
        this.isTouch = isTouch;
        this.isJoin = isJoin;
        this.isMarketing = isMarketing;
    }

    public Upload93006() {
        super();
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows == null ? null : rows.trim();
    }

    public String getNowtime() {
        return nowtime;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime == null ? null : nowtime.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo == null ? null : phoneNo.trim();
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId == null ? null : campId.trim();
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName == null ? null : campName.trim();
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv == null ? null : pv.trim();
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum == null ? null : clickNum.trim();
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum == null ? null : transNum.trim();
    }

    public String getIsTouch() {
        return isTouch;
    }

    public void setIsTouch(String isTouch) {
        this.isTouch = isTouch == null ? null : isTouch.trim();
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin == null ? null : isJoin.trim();
    }

    public String getIsMarketing() {
        return isMarketing;
    }

    public void setIsMarketing(String isMarketing) {
        this.isMarketing = isMarketing == null ? null : isMarketing.trim();
    }

    @Override
    public String toString() {
        return  rows + "€"
                + nowtime + "€"
                + province + "€"
                + city + "€"
                + phoneNo + "€"
                + activityId + "€"
                + activityName + "€"
                + campId +"€"
                + campName + "€"
                + pv + "€"
                + clickNum + "€"
                + transNum + "€"
                + isTouch + "€"
                + isJoin +"€"
                + isMarketing ;
    }
}