package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;

import java.util.List;

public interface InterfaceInfoService {

    /**
     * 获取所有接口信息
     * @param serachFilter
     * @return
     */
    List<InterfaceInfo> getInterfaceInfo(InterfaceInfo serachFilter);

    /**
     * 根据id删除接口信息
     * @param interfaceId
     */
    void deleteInterfaceInfoById(String interfaceId);

    /**
     * 修改接口信息
     * @param interfaceInfo
     */
    void updateInterfaceInfoById(InterfaceInfo interfaceInfo);

    /**
     * 新增接口信息
     * @param interfaceInfo
     */
    void insertInterfaceInfo(InterfaceInfo interfaceInfo);
}
