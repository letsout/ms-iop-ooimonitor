package com.asiainfo.msooimonitor.model.ooimodel;

/**
 * @Author H
 * @Date 2019/2/24 14:15
 * @Desc
 **/
public class InterfaceRecord {
    private String interfaceId;
    private String state;
    private String updateTime;
    private String reason;
    private String fileCount;
    private String successCount;

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFileCount() {
        return fileCount;
    }

    public void setFileCount(String fileCount) {
        this.fileCount = fileCount;
    }

    public String getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(String successCount) {
        this.successCount = successCount;
    }

    public InterfaceRecord() {
    }

    public InterfaceRecord(String interfaceId, String state, String updateTime, String reason, String fileCount, String successCount) {
        this.interfaceId = interfaceId;
        this.state = state;
        this.updateTime = updateTime;
        this.reason = reason;
        this.fileCount = fileCount;
        this.successCount = successCount;
    }
}
