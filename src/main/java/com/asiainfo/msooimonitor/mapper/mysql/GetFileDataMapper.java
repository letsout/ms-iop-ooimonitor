package com.asiainfo.msooimonitor.mapper.mysql;

import com.asiainfo.msooimonitor.model.datahandlemodel.*;
import com.asiainfo.msooimonitor.model.ooimodel.label.CocLabelInfo;
import com.asiainfo.msooimonitor.model.ooimodel.label.UploadLabelInfo;
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

//
//    List<Map<String, String>> getCustGroupInfo(String activityId);
//
//    Map<String, String> getChannelInfo(String activityId);
//
//    Map<String, String> getPositionInfo(String activityId);

    //93006查询省级策划省级执行的
//    List<Map<String, String>> getBaseInfo93006(String activityEndDate);

    //93006查询一级策划省级执行的
    List<Map<String, String>> getMarkingInfo93001(String activityEndDate);

    List<Map<String, String>> getMarkingInfo93002(String activityEndDate);

    List<Map<String, Object>> getBaseInfo93002(String activityEndDate);

    List<Map<String, String>> getMarkingInfo93006(String activityEndDate);


    List<Map<String, Object>> getCampaignedInfo(String activityId);

    List<Map<String, Object>> getBeforeCampaignedInfo(String activityId, String activityEndDate);

    List<Map<String, Object>> getCampaignedEndInfo(String activityId, String campaignedEndTime);

    List<Map<String, String>> getOfferBo(String campaignId);

    List<Map<String, Object>> getBaseInfo93005(String activityEndDate);


    List<Map<String, String>> getMarkingInfo93005(String activityEndDate);

    void insertFlow(List<Map<String, String>> list);

    List<Map<String, String>> getFlowInfo1();

    List<Map<String, String>> getFlowInfo2();

//    void insertFailInterface(Map<String, String> map);

    List<String> getIOPActivityIds(String activityId, String date);
//    List<String> getIOPActivityDates(String ooiActivityId);
//
//    List<String> getZHDIOPActivityIds(String activityId);

//    void insertFailDetail(List<Map<String, String>> list);

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

    //    Map<String, String> getAllOfferBo(String activityId);
    Map<String, String> getBaseChannelInfo(String activityId);

    Map<String, String> getBaseOfferBo(String activityId);

    List<Map<String, Object>> getActivityEndTime(String activityId);

    List<Map<String, String>> getJTActivityInfoByOOIId(String activityId);

//    List<String> getIOPActivityByOoiId(String activityId);

    List<Act93006Info> getJTActivityInfo(String activityEndDate);

    List<Act93006Info> getIOPActivityInfo(String activityEndDate);

    List<Act93004Info> getBase93004(String activityEndDate);

    void updateUploadTime(String uploadTime, String activityIds);

    List<Map<String, String>> getData93003(String month);

    List<UploadLabelInfo> getUploadLabelInfo();

    /**
     * 判断标签数据是否上传
     * @param tableName
     * @return
     */
    int labelDataIsUpload(@Param("tableName") String tableName,@Param("content")String content);

    /**
     * 根据标签id查找标签信息
     * @param labelId
     * @return
     */
    CocLabelInfo getCocLabelInfo(String labelId);

    /**
     * 查询集团下发任务省所需要上传的标签的数据的标签信息
     */
    List<UploadLabelInfo> getJTUploadLabelInfo();

    List<UploadLabelInfo> getQuoteLabelInfo();
}
