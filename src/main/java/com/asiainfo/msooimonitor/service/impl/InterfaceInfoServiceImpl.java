package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.dbt.ooi.InterfaceInfoMpper;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.InterfaceInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/2/23 10:41
 * @Desc
 **/
@Service
public class InterfaceInfoServiceImpl implements InterfaceInfoService {

    @Autowired
    private InterfaceInfoMpper interfaceInfoMpper;

    @Override
    public PageInfo<InterfaceInfo> getInterfaceInfo(Map params) {

        int page = (Integer) params.get("page");
        int limit = (Integer) params.get("limit");

        int start = (page-1) * limit;
        int end = start+limit;

        params.put("start",start);
        params.put("end",end);
        List<InterfaceInfo> interfaceInfo = interfaceInfoMpper.getInterfaceInfo(params);

        PageInfo<InterfaceInfo> interfaceInfoPageInfo = new PageInfo<>(interfaceInfo);
        interfaceInfoPageInfo.setPageSize(limit);
        interfaceInfoPageInfo.setPageNum(page);
        interfaceInfoPageInfo.setTotal(interfaceInfo.size());
        return interfaceInfoPageInfo;
    }



    @Override
    public Boolean checkInterface(String interfaceId) {
        Boolean flag = false;
        int i = interfaceInfoMpper.checkInterface(interfaceId);
        if (i == 0 ) {
            flag=true;
        }
        return flag;
    }

    @Override
    public void saveInterfceInfo(InterfaceInfo interfaceInfo) {

        interfaceInfoMpper.saveInterfceInfo(interfaceInfo);

    }

    @Override
    public void saveInterfaceUpdate(Map params) {

    }

    @Override
    public void saveInterfaceRecord(Map params) {

        interfaceInfoMpper.saveInterfaceRecord(params);
    }

    @Override
    public void deleteInterfaceId(String interfaceId) {

        interfaceInfoMpper.deleteInterfaceId(interfaceId);

    }

    @Override
    public void editInterfaceInfo(InterfaceInfo params) {

        interfaceInfoMpper.editInterfaceInfo(params);
    }

    @Override
    public List<String> getDownloadFile() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        return interfaceInfoMpper.getDownloadFile(date);
    }

    @Override
    public List<InterfaceInfo> getuploadInterface() {
        return interfaceInfoMpper.getuploadInterface();
    }

    @Override
    public List<InterfaceInfo> getLoadInterface() {
        return interfaceInfoMpper.getLoadInterface();
    }


    @Override
    public List<InterfaceRecord> getInterfaceRecord(int start,int end) {
        /*return interfaceInfoMpper.getInterfaceRecord(start,end);*/
        return null;
    }

    @Override
    public List<InterfaceRecord> getInterfaceRecord(Map params) {

        params.put("start",params.get("start"));
        params.put("end",params.get("end"));
        return interfaceInfoMpper.getInterfaceRecord(params);
    }

    @Override
    public List<InterfaceRecord> getInterfaceRecordByParam(int start,int end,String interfaceId,String updateType,String state,String updateTime) {

        return interfaceInfoMpper.getInterfaceRecordByParam(start,end,interfaceId,updateType,state,updateTime);
    }

    @Override
    public String getInterfaceIdType(String interfaceId) {
        return interfaceInfoMpper.getInterfaceIdType(interfaceId);
    }

}
