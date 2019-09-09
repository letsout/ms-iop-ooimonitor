package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.github.pagehelper.PageInfo;

public interface InterfaceInfoService {

    /**
     * 获取所有接口信息
     * @param serachFilter
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<InterfaceInfo> getInterfaceInfo(InterfaceInfo serachFilter, int pageNum, int pageSize);

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

    int thisIdIsHave(String interfaceId);

    /**
     * 根据接口号查询接口信息
     * @param interfaceId
     */
    InterfaceInfo listInterfaceInfoById(String interfaceId);
}
