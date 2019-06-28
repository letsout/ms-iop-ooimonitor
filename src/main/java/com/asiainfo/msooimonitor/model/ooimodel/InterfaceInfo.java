package com.asiainfo.msooimonitor.model.ooimodel;

/**
 * @Author H
 * @Date 2019/2/22 16:24
 * @describe
 **/
public class InterfaceInfo {

    /**
     * 接口号
     */
    private String interfaceId;

    /**
     * 接口描述
     */
    private String desc;

    /**
     * 接口类型 1-上传，2-下载
     */
    private String type;

    /**
     *更新周期 1-日，2-月，3-周
     */
    private String updateType;

    /**
     * 集团文件存放路径
     */
    private String blocPath;

    /**
     *接口更新账期
     */
    private String updateTime;


    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getBlocPath() {
        return blocPath;
    }

    public void setBlocPath(String blocPath) {
        this.blocPath = blocPath;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public InterfaceInfo() {
    }

    public InterfaceInfo(String interfaceId, String desc, String type, String updateType, String blocPath, String updateTime) {
        this.interfaceId = interfaceId;
        this.desc = desc;
        this.type = type;
        this.updateType = updateType;
        this.blocPath = blocPath;
        this.updateTime = updateTime;
    }


}
