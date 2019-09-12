package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.dbt.common.CommonMapper;
import com.asiainfo.msooimonitor.mapper.mysql.GetFileDataMapper;
import com.asiainfo.msooimonitor.mapper.mysql.InterfaceInfoMapper;
import com.asiainfo.msooimonitor.service.FileDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("fileDataService")
@Slf4j
public class FileDataServiceImpl implements FileDataService {
    @Autowired
    GetFileDataMapper getFileDataMapper;
    @Autowired
    CommonMapper commonMapper;

    @Override
    public void getPhone() {
        final List<String> phoneFromBlackRed = getFileDataMapper.getPhoneFromBlackRed();
        final List<String> phone = getFileDataMapper.getPhone();

    }


    @Override
    public List<Map<String, String>> getPhone93006(String tableName) {
        return commonMapper.getMap("select * from " + tableName);
    }

    @Override
    public List<Map<String, String>> getBaseInfo() {
        return getFileDataMapper.getBaseInfo();
    }

    @Override
    public List<Map<String, String>> getMarkingInfo93005() {
        List<Map<String, String>> markingInfo93005 = getFileDataMapper.getBaseInfo93005();
        markingInfo93005.forEach(map -> {
            final String activity_id = map.get("activity_id");
            final List<Map<String, String>> campaignedInfo = getFileDataMapper.getCampaignedInfo(activity_id);
            Map<String, String> campaignedMap = new HashMap<>();
            String[] keyList = {"campaign_id", "campaign_name", "campaign_starttime", "campaign_endtime"};
            if (campaignedInfo.size() > 0) {
                for (Map<String, String> map1 : campaignedInfo) {
                    for (String key : keyList) {
                        String value = "," + map1.get(key);
                        campaignedMap.put(key, value);
                    }
                }
                for (String key : keyList) {
                    campaignedMap.put(key, campaignedMap.get(key).substring(1));
                }
            }
//            String campaign_name = "";
//            String campaign_starttime = "";
//            String campaign_endtime = "";
//            String cust_group_id = "";
//            String cust_group_name = "";
//            String cust_group_count = "";
//            String cust_group_createrule_desc = "";
//            String sgmt_sift_rule = "";
//            for (Map<String, String> map1 : campaignedInfo) {
//                campaign_name += map1.get("campaign_name") + ",";
//                campaign_starttime += map1.get("campaign_starttime").replace("-", "").replace(":", "").replace(" ", "") + ",";
//                campaign_endtime += map1.get("campaign_endtime").replace("-", "").replace(":", "").replace(" ", "") + ",";
//                cust_group_id += map1.get("cust_group_id") + ",";
//                cust_group_name += map1.get("cust_group_name") + ",";
//                cust_group_count += map1.get("cust_group_count") + ",";
//                cust_group_createrule_desc += map1.get("cust_group_createrule_desc") + ",";
//                sgmt_sift_rule += map1.get("sgmt_sift_rule") + ",";
//            }
//            map.put("campaign_name", campaign_name);
//            map.put("campaign_starttime", campaign_starttime);
//            map.put("campaign_endtime", campaign_endtime);
//            map.put("cust_group_id", cust_group_id);
//            map.put("cust_group_name", cust_group_name);
//            map.put("cust_group_count", cust_group_count);
//            map.put("cust_group_createrule_desc", cust_group_createrule_desc);
//            map.put("sgmt_sift_rule", sgmt_sift_rule);
        });
        return markingInfo93005;
    }

    @Override
    public List<Map<String, String>> getBaseInfo93005() {
        List<Map<String, String>> baseInfo93005 = getFileDataMapper.getBaseInfo93005();
        return baseInfo93005;
    }

    @Override
    public Map<String, String> getCustGroupInfo(String activityId) {
        Map<String, String> maps = new HashMap<>();
        final List<Map<String, String>> custGroupInfo = getFileDataMapper.getCustGroupInfo(activityId);
        for (Map<String, String> map : custGroupInfo) {
            final Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String value = "";
                if (StringUtils.isBlank(maps.get(iterator))) {
                    value = map.get(iterator.next());
                } else {
                    value = maps.get(iterator) + "," + map.get(iterator.next());
                }
                maps.put(iterator.next(), value);
            }
        }
        return maps;
    }

    @Override
    public List<Map<String, String>> getMarkingInfo93001() {
        final List<Map<String, String>> markingInfo93001 = getFileDataMapper.getMarkingInfo93001();
        return markingInfo93001;
    }

    @Override
    public List<Map<String, String>> getMarkingInfo93002() {
        final List<Map<String, String>> markingInfo93002 = getFileDataMapper.getMarkingInfo93002();
        return markingInfo93002;
    }
    @Override
    public List<Map<String, String>> getBaseInfo93002() {
        final List<Map<String, String>> baseInfo93002 = getFileDataMapper.getBaseInfo93002();
        return baseInfo93002;
    }


    @Override
    public List<Map<String, String>> getCampaignedInfo(String activityId) {
        final List<Map<String, String>> campaignedInfo93001 = getFileDataMapper.getCampaignedInfo(activityId);
        return campaignedInfo93001;
    }

    @Override
    public List<Map<String, String>> getBaseInfo93006() {
        List<Map<String, String>> baseInfo93006 = getFileDataMapper.getBaseInfo93006();


//        baseInfo93004.addAll(markingInfo3004);
        return baseInfo93006;
    }

    @Override
    public List<Map<String, String>> getDetailEffect(String activity_id, String date) {

        return getFileDataMapper.getDetailEffect(activity_id, date);
    }

    @Override
    public Map<String, String> getSummaryEffect(String activity_id) {

        return getFileDataMapper.getSummaryEffect(activity_id);
    }

    @Override
    public void saveresultList(String sql) {
        commonMapper.insertSql(sql);
    }

    @Override
    public List<Map<String, String>> getResult(String sql) {
        return commonMapper.getMap(sql);
    }

    @Override
    public List<Map<String, String>> getOfferBo(String campaign_id) {
        return getFileDataMapper.getOfferBo(campaign_id);

    }

    @Override
    public List<Map<String, String>> getMarkenInfo93006() {

        List<Map<String, String>> markingInfo93006 = getFileDataMapper.getMarkenInfo93006();
        markingInfo93006.forEach(markingmap -> {
            final String activity_id = markingmap.get("activity_id");
            final List<Map<String, String>> campaignedInfo = getFileDataMapper.getCampaignedInfo(activity_id);
            String campaignId = "";
            String campaignName = "";
            for (Map<String, String> map : campaignedInfo) {
                campaignId += "," + map.get("campaign_id");
                campaignName += "," + map.get("campaign_name");
            }
            if (campaignId.length() > 0)
                campaignId = campaignId.substring(1);
            if (campaignName.length() > 0)
                campaignName = campaignName.substring(1);
            markingmap.put("campaign_id", campaignId);
            markingmap.put("activity_name", campaignName);
        });
        return markingInfo93006;
    }

    public List<Map<String, String>> getBaseInfo93001() {
        List<Map<String, String>> baseInfo93001 = getFileDataMapper.getBaseInfo93001();
        return null;
    }


}
