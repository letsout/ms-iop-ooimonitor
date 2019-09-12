package com.asiainfo.msooimonitor.mapper.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:27
 * Description
 */
@Repository
public interface GetFileDataMapper {
    //93011
    List<String> getPhoneFromBlackRed();

    //93011
    List<String> getPhone();

    List<Map<String, String>> getBaseInfo();

    List<String> getPhone93006(String tableName);

    List<Map<String, String>> getCustGroupInfo(String activityId);

    //93006查询省级策划省级执行的
    List<Map<String, String>> getBaseInfo93006();

    //93006查询一级策划省级执行的
    List<Map<String, String>> getMarkingInfo93001();

    List<Map<String, String>> getMarkingInfo93002();

    List<Map<String, Object>> getBaseInfo93002();

    List<Map<String, String>> getMarkenInfo93006();


    List<Map<String, String>> getCampaignedInfo(String activity_id);

    List<Map<String, String>> getDetailEffect(String activity_id, String date);

    Map<String, String> getSummaryEffect(String activity_id);

    List<Map<String, String>> getOfferBo(String campaign_id);

    List<Map<String, Object>> getBaseInfo93005();

    List<Map<String, String>> getBaseInfo93001();

    List<Map<String, String>> getMarkingInfo93005();

}
