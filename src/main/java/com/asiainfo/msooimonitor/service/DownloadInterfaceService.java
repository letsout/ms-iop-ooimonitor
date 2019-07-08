package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;

import java.util.List;

public interface DownloadInterfaceService {

    /**
     * 查询需要下载的文件接口
     * @return
     */
    List<InterfaceInfo> listDownloadFileInterface();
}
