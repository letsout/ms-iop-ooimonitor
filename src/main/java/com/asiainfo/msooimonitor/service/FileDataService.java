package com.asiainfo.msooimonitor.service;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:24
 * Description
 */
public interface FileDataService {
    void getPhone();

    List<Map<String, String>> getPhone93006(String tableName);

    List<Map<String, String>> getBaseInfo();

    List<Map<String, String>> getBaseInfo93005();

    Map<String, String> getCustGroupInfo(String activityId);

    List<Map<String, String>> getBaseInfo93001();

    List<Map<String, String>> getBaseInfo93006();

    List<Map<String, String>> getCampaignedInfo(String activityId);

    List<Map<String, String>> getDetailEffect(String activity_id, String date);

    Map<String, String> getSummaryEffect(String activity_id);

    void saveresultList(String sql);

    List<Map<String, String>> getResult(String sql);

    List<Map<String, String>> getOfferBo(String campaign_id);

    List<Map<String, String>> getBaseInfo93002();
}
