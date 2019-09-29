package com.asiainfo.msooimonitor.mapper.mysql;

import com.asiainfo.msooimonitor.model.datahandlemodel.ActivityProcessInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadCountInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadDetailInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:27
 * Description
 */
@Repository
public interface GetFileDataMapper {


    List<Map<String, String>> getCustGroupInfo(String activityId);

    Map<String, String> getChannelInfo(String activity_id);

    Map<String, String> getPositionInfo(String activity_id);

    //93006查询省级策划省级执行的
    List<Map<String, String>> getBaseInfo93006(String activityEndDate);

    //93006查询一级策划省级执行的
    List<Map<String, String>> getMarkingInfo93001(String activityEndDate);

    List<Map<String, String>> getMarkingInfo93002(String activityEndDate);

    List<Map<String, Object>> getBaseInfo93002(String activityEndDate);

    List<Map<String, String>> getMarkingInfo93006(String activityEndDate);


    List<Map<String, Object>> getCampaignedInfo(String activity_id);

    List<Map<String, Object>> getCampaignedEndInfo(String activity_id, String campaignedEndTime);

    List<Map<String, String>> getOfferBo(String campaign_id);

    List<Map<String, Object>> getBaseInfo93005(String activityEndDate);


    List<Map<String, String>> getMarkingInfo93005(String activityEndDate);

    void insertFlow(List<Map<String, String>> list);

    List<Map<String, String>> getFlowInfo1();

    List<Map<String, String>> getFlowInfo2();

    void insertFailInterface(Map<String, String> map);

    List<String> getIOPActivityIds(String activityId);

    List<String> getZHDIOPActivityIds(String activityId);

    void insertFailDetail(List<Map<String, String>> list);

    void insertUploadCount(UploadCountInfo uploadCountInfo);

    List<ActivityProcessInfo> getYJCH(String month);

    List<ActivityProcessInfo> getSJCHSJ(String month);

    List<ActivityProcessInfo> getSJCHHLW(String month);

    Map<String, String> getActivityInfoById(String activityId);

    Map<String, String> getActivityProductByActId(String activityId);

    Map<String, String> getJTActivityInfoById(String activityId);

    void insertFailDetails(List<UploadDetailInfo> list);

    List<ActivityProcessInfo> getYJCHBIG(String month);

    void insertInterfaceRelTable(CretaeFileInfo cretaeFileInfo);

    void updateStateInterfaceRelTable(CretaeFileInfo cretaeFileInfo);

    Map<String, String> getAllOfferBo(String activityId);

    Map<String, String> getBaseOfferBo(String activityId);

    List<Map<String, String>> getJTActivityInfoByOOIId(String activityId);
}
