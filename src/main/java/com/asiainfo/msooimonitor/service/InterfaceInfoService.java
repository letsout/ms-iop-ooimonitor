package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface InterfaceInfoService {

    /**
     * 获取接口信息
     * @param params
     * @return
     */
    PageInfo<InterfaceInfo> getInterfaceInfo(Map params);

    /**
     * 检查接口信息是否存在
     * @param interfaceId
     * @return
     */
    Boolean checkInterface(String interfaceId);

    /**
     * 保存接口信息
     * @param interfaceInfo
     */
    void saveInterfceInfo(InterfaceInfo interfaceInfo);

    /**
     * 保存接口更新信息
     * @param params
     */
    void saveInterfaceUpdate(Map params);

    /**
     * 保存接口上传信息
     * @param params
     */
    void saveInterfaceRecord(Map params);


    /**
     * 删除信息
     * @param interfaceId
     */
    void deleteInterfaceId(String interfaceId);

    /**
     * 修改信息
     * @param params
     */
    void editInterfaceInfo(InterfaceInfo params);

    /**
     * 获取要需要下载文件的接口
     * @return
     */
    List<String> getDownloadFile();

    /**
     * 获取需要上传的接口
     * @return
     */
    List<InterfaceInfo> getuploadInterface();

    /**
     * 获取需要load的接口
     * @return
     */
    List<InterfaceInfo> getLoadInterface();

    /**
     * 获取接口上传记录信息
     * @return
     */
    List<InterfaceRecord> getInterfaceRecord(int  start,int end);

    List<InterfaceRecord> getInterfaceRecord(Map params);

    List<InterfaceRecord> getInterfaceRecordByParam(int start,int end,String interfaceId,String updateType,String state,String updateTime);

    String getInterfaceIdType(String interfaceId);
}
