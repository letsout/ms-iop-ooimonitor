package com.asiainfo.msooimonitor.service;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:24
 * Description
 */
public interface FileDataService {


    List<Map<String, Object>> getBaseInfo93005(String activityEndDate);

    List<Map<String, String>> getMarkingInfo93005(String activityEndDate);

    Map<String, String> getCustGroupInfo(String activityId);

    List<Map<String, String>> getMarkingInfo93001(String activityEndDate);

    List<Map<String, String>> getBaseInfo93006(String activityEndDate);

    List<Map<String, Object>> getCampaignedInfo(String activityId);

    List<Map<String, String>> getDetailEffect(String activityIds, String nowDatem, int start, int limit);

    Map<String, String> getSummaryEffect(String activity_id, String summaryDate);

    Map<String, String> getSummaryEffectJT(String activity_id, String summaryDate, String type);

    List<Map<String, Object>> getCampaignedEndInfo(String activityId, String campaignedEndTime);

    void insertFailInterface(Map<String, String> map);

    void saveresultList(String sql);

    List<Map<String, String>> getResult(String sql);

    List<Map<String, String>> getOfferBo(String campaign_id);

    List<Map<String, String>> getMarkingInfo93002(String activityEndDate);

    List<Map<String, Object>> getBaseInfo93002(String activityEndDate);

    List<Map<String, String>> getMarkingInfo93006(String activityEndDate);


    Map<String, String> getChannelInfo(String activity_id);

    Map<String, String> getPositionInfo(String activity_id);

    void insertFlow();

    /**
     * 查询表中数据条数 用于分页查询
     *
     * @param activityIds
     * @param dateTimeFormat
     * @return
     */
    int getTableRows(String activityIds, String dateTimeFormat);

    /**
     * 根据集团下发id获取关联iop的活动id
     *
     * @param activityId
     * @return
     */
    String getIOPActivityIds(String activityId);

    void truncateTable(String tableName);

    void insertFailDetail(List<Map<String, String>> list);

    void insertUploadCount(Map<String, Object> map);

    /**
     * 省级IOP同步子活动结束次月效果评估数据给一级IOP
     */
    void create93055(String month);

    /**
     * 累计效果评估接口：省级IOP上报营销活动结束次月效果数据
     */
    void create93056(String month);
}
