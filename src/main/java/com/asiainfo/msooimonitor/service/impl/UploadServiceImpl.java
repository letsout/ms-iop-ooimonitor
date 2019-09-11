package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.dbt.upload.UploadMapper;
import com.asiainfo.msooimonitor.mapper.mysql.UploadFileMapper;
import com.asiainfo.msooimonitor.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/9/9 21:16
 * @Desc
 **/
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    UploadMapper uploadMapper;

    @Autowired
    UploadFileMapper uploadFileMapper;

    @Override
    public List<Map<String,String>>  getInterfaceInfo(String sql) {
        List<Map<String,String>> interfaceInfo = uploadMapper.getInterfaceInfo(sql);
        return interfaceInfo;
    }

    @Override
    public List<Map<String,String>> getCanCreateFileInterface() {
        return uploadFileMapper.getCanCreateFileInterface();
    }
}
