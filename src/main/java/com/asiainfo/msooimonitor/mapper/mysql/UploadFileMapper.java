package com.asiainfo.msooimonitor.mapper.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UploadFileMapper {

    /**
     * 查询数据准备完毕即将上传的表
     */
    List<Map<String,String>> getCanCreateFileInterface();
}
