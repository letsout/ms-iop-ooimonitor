package com.asiainfo.msooimonitor.service;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:24
 * Description
 */
public interface FileDataService {


    List<Map<String, Object>> getBaseInfo93005(String date);

    List<Map<String, String>> getMarkingInfo93005(String date);

    Map<String, String> getCustGroupInfo(String activityId);

    List<Map<String, String>> getMarkingInfo93001(String date);

    List<Map<String, String>> getBaseInfo93006(String date);

    List<Map<String, String>> getCampaignedInfo(String activityId);

    List<Map<String, String>> getDetailEffect(String activity_id, String date);

    Map<String, String> getSummaryEffect(String activity_id);

    void saveresultList(String sql);

    List<Map<String, String>> getResult(String sql);

    List<Map<String, String>> getOfferBo(String campaign_id);

    List<Map<String, String>> getMarkingInfo93002();

    List<Map<String, Object>> getBaseInfo93002();

    List<Map<String, String>> getMarkenInfo93006(String date);

    List<Map<String, String>> getBaseInfo93001();
}
