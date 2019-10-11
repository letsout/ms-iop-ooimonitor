package com.asiainfo.msooimonitor.service;

import java.util.List;
import java.util.Map;

public interface UploadService {

    List<Map<String,String>> getInterfaceInfo(String sql);

    /**
     * 查询可以生成文件的活动
     */
    List<Map<String,String>> getCanCreateFileInterface();
}
