package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.mysql.InterfaceInfoMapper;
import com.asiainfo.msooimonitor.mapper.mysql.InterfaceRunRecordMapper;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.InterfaceRunRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceRunRecordServiceImpl implements InterfaceRunRecordService {

    @Autowired
    InterfaceRunRecordMapper interfaceRunRecordMapper;

    @Override
    public PageInfo<InterfaceRecord> getInterfaceRunRecordInfo(InterfaceRecord serachFilter, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<InterfaceRecord> interfaceRecordList = interfaceRunRecordMapper.getInterfaceRunRecordInfo(serachFilter);
        PageInfo<InterfaceRecord> interfaceRecordPageInfo = new PageInfo<>(interfaceRecordList);
        return interfaceRecordPageInfo;
    }

    @Override
    public void deleteInterfaceInfos(String interfaceId) {
        interfaceRunRecordMapper.deleteInterfaceInfos(interfaceId);
    }
}
