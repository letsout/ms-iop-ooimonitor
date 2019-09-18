package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.dbt.common.CommonMapper;
import com.asiainfo.msooimonitor.mapper.dbt.ooi.InterfaceInfoMpper;
import com.asiainfo.msooimonitor.mapper.mysql.GetFileDataMapper;
import com.asiainfo.msooimonitor.service.FileDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class FileDataServiceImpl implements FileDataService {
    @Autowired
    GetFileDataMapper getFileDataMapper;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    InterfaceInfoMpper interfaceInfoMpper;


    @Override
    public List<Map<String, String>> getMarkingInfo93005(String activityEndDate) {
        List<Map<String, String>> markingInfo93005 = getFileDataMapper.getMarkingInfo93005(activityEndDate);
        markingInfo93005.forEach(map -> {
            final String activity_id = map.get("activity_id");
            final List<Map<String, String>> campaignedInfo = getFileDataMapper.getCampaignedInfo(activity_id);
            Map<String, String> campaignedMap = new HashMap<>();
            String campaign_id = "";
            String campaign_name = "";
            String campaign_starttime = "";
            String campaign_endtime = "";
            if (campaignedInfo.size() > 0) {
                for (Map<String, String> map1 : campaignedInfo) {
                    campaign_id += ",280" + map1.get("campaign_id")+map1.get("iop_activity_id").substring(1);
                    campaign_name += "," + map1.get("campaign_name");
                    campaign_starttime += "," + map1.get("campaign_starttime");
                    campaign_endtime += "," + map1.get("campaign_endtime");

                    campaignedMap.put("campaign_id", campaign_id.substring(1));
                    campaignedMap.put("campaign_name", campaign_name.substring(1));
                    campaignedMap.put("campaign_starttime", campaign_starttime.substring(1));
                    campaignedMap.put("campaign_endtime", campaign_endtime.substring(1));
                }
                map.putAll(campaignedMap);
            }
        });
        return markingInfo93005;
    }

    @Override
    public List<Map<String, Object>> getBaseInfo93005(String activityEndDate) {
        List<Map<String, Object>> baseInfo93005 = getFileDataMapper.getBaseInfo93005(activityEndDate);
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
    public List<Map<String, String>> getMarkingInfo93001(String activityEndDate) {
        final List<Map<String, String>> markingInfo93001 = getFileDataMapper.getMarkingInfo93001(activityEndDate);
        return markingInfo93001;
    }

    public List<Map<String, String>> getMarkingInfo93002(String activityEndDate) {
        final List<Map<String, String>> markingInfo93002 = getFileDataMapper.getMarkingInfo93002(activityEndDate);
        return markingInfo93002;
    }

    @Override
    public List<Map<String, Object>> getBaseInfo93002(String activityEndDate) {
        final List<Map<String, Object>> baseInfo93002 = getFileDataMapper.getBaseInfo93002(activityEndDate);
        return baseInfo93002;
    }


    @Override
    public List<Map<String, String>> getCampaignedInfo(String activityId) {
        final List<Map<String, String>> campaignedInfo93001 = getFileDataMapper.getCampaignedInfo(activityId);
        return campaignedInfo93001;
    }

    @Override
    public List<Map<String, String>> getCampaignedEndInfo(String activityId, String campaignedEndTime) {
        final List<Map<String, String>> campaignedInfo93001 = getFileDataMapper.getCampaignedEndInfo(activityId, campaignedEndTime);
        return campaignedInfo93001;
    }

    @Override
    public List<Map<String, String>> getBaseInfo93006(String activityEndDate) {
        List<Map<String, String>> baseInfo93006 = getFileDataMapper.getBaseInfo93006(activityEndDate);
        return baseInfo93006;
    }

    @Override
    public List<Map<String, String>> getDetailEffect(String activityIds, String activityEndDate, int start, int limit) {

        return interfaceInfoMpper.getDetailEffect(activityIds, activityEndDate, start, limit);
    }

    @Override
    public Map<String, String> getSummaryEffect(String activityId, String activityEndDate) {
        // 根据自互动取
        Map<String, String> summaryEffect = interfaceInfoMpper.getSummaryEffect(activityId, activityEndDate);
        return summaryEffect;
    }

    @Override
    public Map<String, String> getSummaryEffectJT(String activityId, String summaryDate, String type) {
        List<String> iopActivityIds;
        // 根据集团下发活动查询iop关联活动
        if ("ZHD".equals(type)) {
            iopActivityIds = getFileDataMapper.getZHDIOPActivityIds(activityId);
        } else {
            iopActivityIds = getFileDataMapper.getIOPActivityIds(activityId);
        }
        if (iopActivityIds == null || iopActivityIds.size() == 0) {
            return null;
        }
        String activityIds = "'" + StringUtils.join(iopActivityIds, "','") + "'";
        List<Map<String, String>> summaryEffect = interfaceInfoMpper.getSummaryEffects(activityIds, summaryDate);
        if (summaryEffect.size() == 0)
            return null;
        int customerNum = 0;
        int touchNum = 0;
        int vicNum = 0;
        float inOutRate = 0;
        float terminalFlowRate = 0;
        int i = 0;
        for (Map<String, String> map :
                summaryEffect) {
            customerNum += Integer.valueOf(map.get("customer_num"));
            touchNum += Integer.valueOf(map.get("touch_num"));
            vicNum += Integer.valueOf(map.get("vic_num"));
            inOutRate += Float.valueOf(map.get("in_out_rate"));
            terminalFlowRate += Float.valueOf(map.get("terminal_flow_rate"));
            i++;
        }

        Map<String, String> map = summaryEffect.get(0);
        map.put("customer_num", String.valueOf(customerNum));
        map.put("touch_num", String.valueOf(touchNum));
        map.put("vic_num", String.valueOf(vicNum));

        DecimalFormat df = new DecimalFormat("0.000000");
        map.put("touhe_rate", df.format((float) touchNum / customerNum));
        map.put("response_rate", df.format((float) touchNum / customerNum));
        map.put("vic_rate", df.format((float) vicNum / touchNum));
        map.put("in_out_rate", df.format(inOutRate / i));
        map.put("terminal_flow_rate", df.format(terminalFlowRate / i));

        return map;
    }

    @Override
    public void saveresultList(String sql) {
        commonMapper.insertSql(sql);
    }

    public List<Map<String, String>> getResult(String sql) {
        return commonMapper.getMap(sql);
    }

    public List<Map<String, String>> getOfferBo(String campaign_id) {
        return getFileDataMapper.getOfferBo(campaign_id);

    }

    @Override
    public List<Map<String, String>> getMarkingInfo93006(String activityEndDate) {
        // 一级策划省级执行
        List<Map<String, String>> markingInfo93006 = getFileDataMapper.getMarkingInfo93006(activityEndDate);
        markingInfo93006.forEach(markingmap -> {
            final String activity_id = markingmap.get("activity_id");
            final List<Map<String, String>> campaignedInfo = getFileDataMapper.getCampaignedInfo(activity_id);
            String campaignId = "";
            String campaignName = "";
            for (Map<String, String> map : campaignedInfo) {
                campaignId += ",280" + map.get("campaign_id")+map.get("iop_activity_id").substring(1);
                campaignName += "," + map.get("campaign_name");
            }
            if (campaignId.length() > 0) {
                campaignId = campaignId.substring(1);
            }
            if (campaignName.length() > 0) {
                campaignName = campaignName.substring(1);
            }
            markingmap.put("campaign_id", campaignId);
            markingmap.put("campaign_name", campaignName);
        });
        return markingInfo93006;
    }

    @Override
    public List<Map<String, String>> getBaseInfo93001() {
        List<Map<String, String>> baseInfo93001 = getFileDataMapper.getBaseInfo93001();
        return baseInfo93001;
    }

    @Override
    public Map<String, String> getChannelInfo(String activity_id) {
        Map<String, String> channelInfo = getFileDataMapper.getChannelInfo(activity_id);
        return channelInfo;
    }

    @Override
    public Map<String, String> getPositionInfo(String activity_id) {
        Map<String, String> positionInfo = getFileDataMapper.getPositionInfo(activity_id);
        if (positionInfo == null)
            positionInfo= new HashMap<>();
        return positionInfo;
    }

    @Override
    public void insertFlow() {
        final List<Map<String, String>> flowInfo1 = getFileDataMapper.getFlowInfo1();
        getFileDataMapper.insertFlow(flowInfo1);
        final List<Map<String, String>> flowInfo2 = getFileDataMapper.getFlowInfo2();
        getFileDataMapper.insertFlow(flowInfo2);
    }

    @Override
    public int getTableRows(String activityIds, String dateTimeFormat) {
        return interfaceInfoMpper.getTableRows(activityIds, dateTimeFormat);
    }

    @Override
    public String getIOPActivityIds(String activityId) {
        List<String> iopActivityIds = getFileDataMapper.getIOPActivityIds(activityId);
        String activitys = "'" + StringUtils.join(iopActivityIds, "','") + "'";
        return activitys;
    }

    @Override
    public void insertFailInterface(Map<String, String> map) {
        getFileDataMapper.insertFailInterface(map);
    }

    @Override
    public void truncateTable(String tableName) {
        interfaceInfoMpper.truncateTable(tableName);
    }
}
