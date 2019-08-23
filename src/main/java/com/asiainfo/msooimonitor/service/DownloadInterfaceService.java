package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;

import java.util.List;

public interface DownloadInterfaceService {

    /**
     * 查询需要下载的文件接口
     * @return
     */
    List<InterfaceInfo> listDownloadFileInterface();

    /**
     * 查询此接口成功入库的最大时间
     * @param interfaceId
     */
    String getMaxSuccessTime(String interfaceId);
}
