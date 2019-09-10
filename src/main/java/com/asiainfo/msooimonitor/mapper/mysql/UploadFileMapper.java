package com.asiainfo.msooimonitor.mapper.mysql;

import java.util.List;

public interface UploadFileMapper {

    /**
     * 查询数据准备完毕即将上传的表
     */
    List<String> getUploadInterfaceTable();
}
