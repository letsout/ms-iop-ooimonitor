package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.mysql.InterfaceInfoMapper;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.service.InterfaceInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<InterfaceInfo> getInterfaceInfo(InterfaceInfo serachFilter, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoMapper.getInterfaceInfo(serachFilter);
        PageInfo<InterfaceInfo> interfaceInfoPageInfo = new PageInfo<>(interfaceInfoList);
        return interfaceInfoPageInfo;
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

    @Override
    public int thisIdIsHave(String interfaceId){
        return interfaceInfoMapper.thisIdIsHave(interfaceId);
    }

    @Override
    public InterfaceInfo listInterfaceInfoById(String interfaceId) {
        return interfaceInfoMapper.listInterfaceInfoById(interfaceId);
    }

}
