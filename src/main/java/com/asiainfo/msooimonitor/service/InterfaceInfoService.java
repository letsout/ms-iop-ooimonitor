package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;

import java.util.List;

public interface InterfaceInfoService {

    /**
     * 获取所有接口信息
     * @param serachFilter
     * @return
     */
    List<InterfaceInfoService> getInterfaceInfo(InterfaceInfo serachFilter);
}
