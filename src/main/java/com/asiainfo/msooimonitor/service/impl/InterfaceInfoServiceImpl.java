package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.mysql.InterfaceInfoMapper;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.service.InterfaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author H
 * @Date 2019/7/8 17:39
 * @Desc
 **/
@Service
public class InterfaceInfoServiceImpl implements InterfaceInfoService {

    @Autowired
    InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public List<InterfaceInfo> getInterfaceInfo(InterfaceInfo serachFilter) {
       return interfaceInfoMapper.getInterfaceInfo(serachFilter);
    }

    @Override
    public void deleteInterfaceInfoById(String interfaceId) {
        interfaceInfoMapper.deleteInterfaceInfoById(interfaceId);
    }

    @Override
    public void updateInterfaceInfoById(InterfaceInfo interfaceInfo) {
        interfaceInfoMapper.updateInterfaceInfoById(interfaceInfo);
    }

    @Override
    public void insertInterfaceInfo(InterfaceInfo interfaceInfo) {
        interfaceInfoMapper.insertInterfaceInfo(interfaceInfo);
    }

}
