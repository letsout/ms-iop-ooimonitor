package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.mysql.DownloadFileMapper;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.service.DownloadInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author H
 * @Date 2019/7/1 17:56
 * @Desc
 **/
@Service
@Slf4j
public class DownloadInterfaceServiceImpl implements DownloadInterfaceService {


    @Autowired
    private DownloadFileMapper downloadFileMapper;

    @Override
    public List<InterfaceInfo> listDownloadFileInterface() {
        return downloadFileMapper.listDownloadFileInterface();
    }
}
