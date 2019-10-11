package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.github.pagehelper.PageInfo;

public interface InterfaceRunRecordService {

    /**
     * 获取所有接口信息
     * @param serachFilter
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<InterfaceRecord> getInterfaceRunRecordInfo(InterfaceRecord serachFilter, int pageNum, int pageSize);


    /**
     * 根据id删除接口信息
     * @param interfaceId
     */
    void deleteInterfaceInfos(String interfaceId);
}
