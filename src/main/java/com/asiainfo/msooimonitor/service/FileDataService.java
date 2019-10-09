package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadCountInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadDetailInfo;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:24
 * Description
 */
public interface FileDataService {
    void insertFailDetails(List<UploadDetailInfo> list);


//    List<Map<String, Object>> getBaseInfo93005(String activityEndDate);
//
    List<Map<String, String>> getMarkingInfo93005(String activityEndDate) throws Exception;
//
////    Map<String, String> getCustGroupInfo(String activityId);
//
//    List<Map<String, String>> getMarkingInfo93001(String activityEndDate);
//
//    List<Map<String, String>> getBaseInfo93006(String activityEndDate);
//
//    List<Map<String, Object>> getCampaignedInfo(String activityId);
//
////    List<Map<String, String>> getDetailEffect(String activityIds, String nowDatem, int start, int limit);
//
////    Map<String, String> getSummaryEffect(String activity_id, String summaryDate);
//
//    /**
//     * @param activity_id
//     * @param summaryDate
//     * @param type        ZHD根据主活动查找关联的子活动,HD上传的本来就是iop活动id
//     * @return
//     */
//    Map<String, String> getSummaryEffectJT(String activity_id, String summaryDate);
//
//    List<Map<String, Object>> getCampaignedEndInfo(String activityId, String campaignedEndTime);
//
//    void insertFailInterface(Map<String, String> map);
//
//    void saveresultList(String sql);
//
//    List<Map<String, String>> getResult(String sql);
//
//    List<Map<String, String>> getOfferBo(String campaign_id);
//
//    Map<String, String> getAllOfferBo(String activityId);
//
//    List<Map<String, String>> getMarkingInfo93002(String activityEndDate);
//
//    List<Map<String, Object>> getBaseInfo93002(String activityEndDate);
//
//    List<Map<String, String>> getMarkingInfo93006(String activityEndDate);
//
//
//    Map<String, String> getChannelInfo(String activity_id);
//
//    Map<String, String> getPositionInfo(String activity_id);
//
    void insertFlow();
//
//    /**
//     * 查询表中数据条数 用于分页查询
//     *
//     * @param activityIds
//     * @param dateTimeFormat
//     * @return
//     */
//    int getTableRows(String activityIds, String dateTimeFormat);
//
//    /**
//     * 根据集团下发id获取关联iop的活动id
//     *
//     * @param activityId
//     * @return
//     */
//    String getIOPActivityIds(String activityId,String date);
//
    void truncateTable(String tableName);
//
//
//    void insertUploadCount(UploadCountInfo uploadCountInfo);
//
//    String getSummaryEffectMaxDate(String activityId, String beforeDate);
//
//    Map<String, String> getBaseOfferBo(String activityId);
//
//
//    /**
//     * 省级IOP同步子活动结束次月效果评估数据给一级IOP
//     */
    void create93055(String month);
//
//    /**
//     * 累计效果评估接口：省级IOP上报营销活动结束次月效果数据
//     */
    void create93056(String month);
    void insertInterfaceRelTable(CretaeFileInfo cretaeFileInfo);
//
    Map<String, String> getSummaryEffectAll(String activityId,String activityEndDate);
}
