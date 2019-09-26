package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.constant.CommonConstant;
import com.asiainfo.msooimonitor.mapper.dbt.common.CommonMapper;
import com.asiainfo.msooimonitor.mapper.dbt.ooi.InterfaceInfoMpper;
import com.asiainfo.msooimonitor.mapper.mysql.GetFileDataMapper;
import com.asiainfo.msooimonitor.model.datahandlemodel.ActivityProcessInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadCountInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadDetailInfo;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public List<Map<String, String>> getMarkingInfo93005(String activityEndDate) throws Exception {
        List<Map<String, String>> markingInfo93005 = getFileDataMapper.getMarkingInfo93005(activityEndDate);
        for (Map<String, String> map : markingInfo93005) {
            String activity_id = map.get("activity_id");
//            iop_activity_id,c.activity_name,c.start_time,c.end_time
            List<Map<String, Object>> campaignedInfo = getFileDataMapper.getCampaignedInfo(activity_id);
            if (campaignedInfo == null || campaignedInfo.size() == 0) {
                continue;
            }
            Map<String, String> campaignedMap = new HashMap<>();
            String campaign_id = "";
            String campaign_name = "";
            String campaign_starttime = "";
            String campaign_endtime = "";
            String offer_code = "";
            String offer_name = "";
            String offer_type = "";
            String channel_name = "";
            String channel_id = "";
            String channel_type = "";
            String channel_rule = "";
            String time_distindes = "";
            for (Map<String, Object> map1 : campaignedInfo) {
                campaign_id += ",280_" + map1.get("campaign_id") + "_" + map1.get("iop_activity_id").toString().substring(1);
                campaign_name += "," + map1.get("activity_name");
                campaign_starttime += "," + TimeUtil.getOoiDate(map1.get("campaign_starttime").toString());
                campaign_endtime += "," + TimeUtil.getOoiDate(map1.get("end_time").toString());
                offer_code += "," + map1.get("offer_code");
                offer_name += "," + map1.get("offer_name");
                offer_type += "," + map1.get("offer_type");
                channel_name += "," + map1.get("channel_name");
                channel_id += "," + map1.get("channel_id");
                channel_type += "," + map1.get("channel_type");
                channel_rule += "," + map1.get("channel_rule");
                time_distindes += "," + map1.get("time_distindes");
            }
            campaignedMap.put("campaign_id", campaign_id.substring(1));
            campaignedMap.put("campaign_name", campaign_name.substring(1));
            campaignedMap.put("campaign_starttime", campaign_starttime.substring(1));
            campaignedMap.put("campaign_endtime", campaign_endtime.substring(1));
            campaignedMap.put("offer_code", offer_code.substring(1));
            campaignedMap.put("offer_name", offer_name.substring(1));
            campaignedMap.put("offer_type", offer_type.substring(1));
            campaignedMap.put("channel_name", channel_name.substring(1));
            campaignedMap.put("channel_id", channel_id.substring(1));
            campaignedMap.put("channel_type", channel_type.substring(1));
            campaignedMap.put("channel_rule", channel_rule.substring(1));
            campaignedMap.put("time_distindes", time_distindes.substring(1));
            map.putAll(campaignedMap);

        }
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
    public List<Map<String, Object>> getCampaignedEndInfo(String activityId, String campaignedEndTime) {
        final List<Map<String, Object>> campaignedInfo93001 = getFileDataMapper.getCampaignedEndInfo(activityId, campaignedEndTime);
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
        Map<String, String> map = interfaceInfoMpper.getSummaryEffect(activityId, activityEndDate);
//        int i = 1;
//        customerNum += Integer.valueOf(map.get("customer_num"));
//        touchNum += Integer.valueOf(map.get("touch_num"));
//        vicNum += Integer.valueOf(map.get("vic_num"));
//        inOutRate += Float.valueOf(map.get("in_out_rate"));
//        terminalFlowRate += Float.valueOf(map.get("terminal_flow_rate"));
//        map.put("customer_num", String.valueOf(customerNum));
//        map.put("touch_num", String.valueOf(touchNum));
//        map.put("vic_num", String.valueOf(vicNum));
//
//        DecimalFormat df = new DecimalFormat("0.000000");
//        map.put("touhe_rate", df.format((float) touchNum / customerNum));
//        map.put("response_rate", df.format((float) touchNum / customerNum));
//        map.put("vic_rate", df.format((float) vicNum / touchNum));
//        map.put("in_out_rate", df.format(inOutRate / i));
//        map.put("terminal_flow_rate", df.format(terminalFlowRate / i));
        return map;
    }

    @Override
    public String getSummaryEffectMaxDate(String activityId, String beforeDate) {
        String maxDate = interfaceInfoMpper.getSummaryEffectMaxDate(activityId, beforeDate);
        return maxDate;
    }

//    public static void main(String[] args) {
//        List arrayList;
//        System.out.println("'" + StringUtils.join(arrayList, "','") + "'");
//        arrayList = new ArrayList();
//        System.out.println("'" + StringUtils.join(arrayList, "','") + "'");
//        arrayList.add("1");
//        System.out.println("'" + StringUtils.join(arrayList, "','") + "'");
//    }

    @Override
    public Map<String, String> getSummaryEffectJT(String activityId, String summaryDate, String type) {
        List<String> iopActivityIds;
        // 根据集团下发活动查询iop关联活动
        String activityIds = "";
        if ("ZHD".equals(type)) {//根据主活动查找关联的子活动
            iopActivityIds = getFileDataMapper.getZHDIOPActivityIds(activityId);
            activityIds = "'" + StringUtils.join(iopActivityIds, "','") + "'";
        } else {//上传的本来就是iop活动id
            activityIds = "'" + activityId + "'";
        }
        if (activityIds.equals("''") || activityIds.equals("'null'")) {
            return null;
        }
        List<Map<String, String>> summaryEffect = interfaceInfoMpper.getSummaryEffects(activityIds, summaryDate);
        if (summaryEffect == null || summaryEffect.size() == 0)
            return null;
        int customerNum = 0;
        int touchNum = 0;
        int vicNum = 0;
        float inOutRate = 0;
        float terminalFlowRate = 0;
        int i = 0;
        String customer_group_id = "";
        String customer_group_name = "";
        String customer_num = "";
        String customer_filter_rule = "";
        for (Map<String, String> map : summaryEffect) {
            customer_group_id += "," + map.get("customer_group_id");
            customer_group_name += "," + map.get("customer_group_name");
            customer_num += "," + map.get("customer_num");
            customer_filter_rule = map.get("customer_filter_rule");
            customerNum += Integer.valueOf(map.get("customer_num"));
            touchNum += Integer.valueOf(map.get("touch_num"));
            vicNum += Integer.valueOf(map.get("vic_num"));
            inOutRate += Float.valueOf(map.get("in_out_rate"));
            terminalFlowRate += Float.valueOf(map.get("terminal_flow_rate"));
            i++;
        }


        Map<String, String> map = new HashMap<>();
        if (i > 0) {
            map.put("customer_filter_rule", customer_filter_rule.substring(1));
            map.put("customer_num", customer_num.substring(1));
            map.put("customer_group_id", customer_group_id.substring(1));
            map.put("customer_group_name", customer_group_name.substring(1));
        }
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

    @Override
    public List<Map<String, String>> getResult(String sql) {

        return commonMapper.getMap(sql);
    }

    @Override
    public List<Map<String, String>> getOfferBo(String campaign_id) {
        return getFileDataMapper.getOfferBo(campaign_id);

    }

    public Map<String, String> getAllOfferBo(String activityId) {
        getFileDataMapper.getAllOfferBo(activityId);
        return null;

    }

    @Override
    public Map<String, String> getBaseOfferBo(String activityId) {
        final Map<String, String> baseOfferBo = getFileDataMapper.getBaseOfferBo(activityId);
        return baseOfferBo;

    }

    @Override
    public List<Map<String, String>> getMarkingInfo93006(String activityEndDate) {
        // 一级策划省级执行
        List<Map<String, String>> markingInfo93006 = getFileDataMapper.getMarkingInfo93006(activityEndDate);
        markingInfo93006.forEach(markingmap -> {
            final String activity_id = markingmap.get("activity_id");
            final List<Map<String, Object>> campaignedInfo = getFileDataMapper.getCampaignedEndInfo(activity_id, activityEndDate);
            String campaignId = "";
            String campaignName = "";
            for (Map<String, Object> map : campaignedInfo) {
                campaignId += ",280_" + map.get("campaign_id") + "_" + map.get("iop_activity_id").toString().substring(1);
                campaignName += "," + map.get("activity_name");
            }
            if (StringUtils.isNotEmpty(campaignId)) {
                campaignId = campaignId.substring(1);
            }
            if (StringUtils.isNotEmpty(campaignName)) {
                campaignName = campaignName.substring(1);
            }
            markingmap.put("campaign_id", campaignId);
            markingmap.put("campaign_name", campaignName);
        });
        return markingInfo93006;
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
            positionInfo = new HashMap<>();
        return positionInfo;
    }

    @Override
    public void insertFlow() {
        final List<Map<String, String>> flowInfo1 = getFileDataMapper.getFlowInfo1();
        if (flowInfo1.size() != 0)
            getFileDataMapper.insertFlow(flowInfo1);
        final List<Map<String, String>> flowInfo2 = getFileDataMapper.getFlowInfo2();
        if (flowInfo2.size() != 0)
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

    @Override
    public void insertFailDetail(List<Map<String, String>> list) {
        if (list == null || list.size() == 0)
            return;
        getFileDataMapper.insertFailDetail(list);
    }

    @Override
    public void insertUploadCount(UploadCountInfo uploadCountInfo) {
        getFileDataMapper.insertUploadCount(uploadCountInfo);
    }

    @Override
    @Transactional(transactionManager = "DBTTransactionManager", rollbackFor = Exception.class)
    public void create93055(String month) {

        ArrayList<ActivityProcessInfo> allActivitys = new ArrayList<>();
        allActivitys.addAll(getFileDataMapper.getYJCH(month));
        allActivitys.addAll(getFileDataMapper.getSJCHSJ(month));
        allActivitys.addAll(getFileDataMapper.getSJCHHLW(month));

        List<Map<String, String>> paramList = new ArrayList();
        ArrayList<Map<String, String>> errorParamList = new ArrayList<>();

        int errorNum = 0;
        for (ActivityProcessInfo Info :
                allActivitys) {
            Map<String, String> paramMap = new HashMap();
            String activityId = Info.getActivityId();
            String processId = Info.getProcessId();
            // 记录活动信息
            if (CommonConstant.SJCHHLW.equals(processId) || CommonConstant.SJCHHLW.equals(processId)) {
                // 查询活动详情
                Map<String, String> activityInfo = getFileDataMapper.getActivityInfoById(activityId);
                paramMap.put("A2", CommonConstant.SC);
                paramMap.put("A3", CommonConstant.cityMap.get(activityInfo.get("city_id")));
                paramMap.put("A4", "280" + activityId.substring(1));
                paramMap.put("A5", activityInfo.get("activity_name"));
                paramMap.put("A6", "9");
                paramMap.put("A7", activityId.substring(1));
                paramMap.put("A8", activityInfo.get("activity_name"));
                // 根据活动id获取产品信息
                Map<String, String> productInfo = getFileDataMapper.getActivityProductByActId(activityId);
                paramMap.put("A9", "0428000" + productInfo.get("prc_id"));
                paramMap.put("A10", productInfo.get("prc_name"));
            } else if (CommonConstant.YJCH.equals(processId)) {
                // 查询集团活动详情
                Map<String, String> jtActivityInfo = getFileDataMapper.getJTActivityInfoById(activityId);
                paramMap.put("A2", CommonConstant.SC);
                paramMap.put("A3", CommonConstant.cityMap.get("1"));
                paramMap.put("A4", "280" + jtActivityInfo.get(jtActivityInfo.get("activity_id")));
                paramMap.put("A5", jtActivityInfo.get("activity_name"));
                paramMap.put("A6", "9");
                paramMap.put("A7", CommonConstant.SC + "_" + jtActivityInfo.get("ooi_campaign_id") + "_" + activityId.substring(1));
                paramMap.put("A8", jtActivityInfo.get("activity_name"));
                paramMap.put("A9", jtActivityInfo.get("offer_code"));
                paramMap.put("A10", jtActivityInfo.get("offer_name"));
            }
            // 不记录最终插入语句
            paramMap.put("processId", processId);
            paramMap.put("activityId", activityId);

            // 校验必填参数不能为空
            boolean isNotNull = filterParamer(paramMap);

            if (!isNotNull) {
                errorNum++;
                paramMap.put("error", "改行数据存在空字段");
                errorParamList.add(paramMap);
                continue;
            }

            paramMap.put("error", "");
            paramList.add(paramMap);
        }

        // 插入记录表 1.汇总表
        getFileDataMapper.insertUploadCount(UploadCountInfo.builder().interfaceId("93055").uploadNum(paramList.size()).failNum(errorNum).build());
        // 2上传明细表
        List<UploadDetailInfo> uploadDetailList = new ArrayList<>();
        errorParamList.addAll(paramList);

        // 记录明细
        errorParamList.stream().forEach(info -> {
            UploadDetailInfo build = UploadDetailInfo.builder().interfaceId("91055")
                    .activityId(info.get("activityId"))
                    .activitytype(info.get("processId"))
                    .failDesc(info.get("error"))
                    .build();
            uploadDetailList.add(build);
        });
        getFileDataMapper.insertFailDetails(uploadDetailList);

        // 插入数据表待上传
        interfaceInfoMpper.insert93055(paramList);

        // 插入状态表代表可以生成文件
        getFileDataMapper.insertInterfaceRelTable(
                CretaeFileInfo.builder()
                        .interfaceId("93005")
                        .tableName("iop_93055")
                        .fileName("i_13000_time_IOP-93055_00_fileNum.dat")
                        .dataTime(month)
                        .step("1")
                        .build()
        );

    }

    /**
     * 校验参数信息不能为空
     *
     * @param paramMap
     * @return
     */
    private boolean filterParamer(Map<String, String> paramMap) {

        AtomicBoolean flag = new AtomicBoolean(true);

        paramMap.forEach((k, v) -> {
            if (StringUtils.isBlank(v)) {
                log.error("参数[{}]为空", k);
                flag.set(false);
            }
        });

        return flag.get();
    }

    @Override
    @Transactional(transactionManager = "DBTTransactionManager", rollbackFor = Exception.class)
    public void create93056(String month) {
        ArrayList<ActivityProcessInfo> allActivitys = new ArrayList<>();
        allActivitys.addAll(getFileDataMapper.getYJCHBIG(month));
        allActivitys.addAll(getFileDataMapper.getSJCHSJ(month));
        allActivitys.addAll(getFileDataMapper.getSJCHHLW(month));

        List<Map<String, String>> paramList = new ArrayList();
        ArrayList<Map<String, String>> errorParamList = new ArrayList<>();

        int errorNum = 0;
        for (ActivityProcessInfo Info :
                allActivitys) {
            Map<String, String> paramMap = new HashMap();
            String activityId = Info.getActivityId();
            String processId = Info.getProcessId();
            // 记录活动信息
            if (CommonConstant.SJCHHLW.equals(processId) || CommonConstant.SJCHHLW.equals(processId)) {
                // 查询活动详情
                Map<String, String> activityInfo = getFileDataMapper.getActivityInfoById(activityId);
                paramMap.put("A2", CommonConstant.SC);
                paramMap.put("A3", CommonConstant.cityMap.get(activityInfo.get("city_id")));
                paramMap.put("A4", "280" + activityId.substring(1));
                paramMap.put("A5", activityInfo.get("activity_name"));
                paramMap.put("A6", "9");
                paramMap.put("A7", activityId.substring(1));
                paramMap.put("A8", activityInfo.get("activity_name"));
                // 根据活动id获取产品信息
                Map<String, String> productInfo = getFileDataMapper.getActivityProductByActId(activityId);
                paramMap.put("A9", "0428000" + productInfo.get("prc_id"));
                paramMap.put("A10", productInfo.get("prc_name"));
            } else if (CommonConstant.YJCH.equals(processId)) {
                // 查询集团活动详情
                Map<String, String> jtActivityInfo = getFileDataMapper.getJTActivityInfoById(activityId);
                paramMap.put("A2", CommonConstant.SC);
                paramMap.put("A3", CommonConstant.cityMap.get("1"));
                paramMap.put("A4", "280" + jtActivityInfo.get(jtActivityInfo.get("activity_id")));
                paramMap.put("A5", jtActivityInfo.get("activity_name"));
                paramMap.put("A6", "9");
                // TODO 具备多个子活动怎么处理 联调时与一级沟通
                paramMap.put("A7", CommonConstant.SC + "_" + jtActivityInfo.get("ooi_campaign_id") + "_" + activityId.substring(1));
                paramMap.put("A8", jtActivityInfo.get("activity_name"));
                paramMap.put("A9", jtActivityInfo.get("offer_code"));
                paramMap.put("A10", jtActivityInfo.get("offer_name"));
            }
            // 不记录最终插入语句
            paramMap.put("processId", processId);
            paramMap.put("activityId", activityId);

            // 校验必填参数不能为空
            boolean isNotNull = filterParamer(paramMap);

            if (!isNotNull) {
                errorNum++;
                paramMap.put("error", "改行数据存在空字段");
                errorParamList.add(paramMap);
                continue;
            }

            paramMap.put("error", "");
            paramList.add(paramMap);
        }

        // 插入记录表 1.汇总表
        getFileDataMapper.insertUploadCount(UploadCountInfo.builder().interfaceId("93056").uploadNum(paramList.size()).failNum(errorNum).build());
        // 2上传明细表
        List<UploadDetailInfo> uploadDetailList = new ArrayList<>();
        errorParamList.addAll(paramList);

        // 记录明细
        errorParamList.stream().forEach(info -> {
            UploadDetailInfo build = UploadDetailInfo.builder().interfaceId("91056")
                    .activityId(info.get("activityId"))
                    .activitytype(info.get("processId"))
                    .failDesc(info.get("error"))
                    .build();
            uploadDetailList.add(build);
        });
        getFileDataMapper.insertFailDetails(uploadDetailList);

        // 插入数据表待上传
        interfaceInfoMpper.insert93056(paramList);

        // 插入状态表代表可以生成文件
        getFileDataMapper.insertInterfaceRelTable(
                CretaeFileInfo.builder()
                        .interfaceId("91056")
                        .tableName("iop_93056")
                        .fileName("i_13000_time_IOP-93056_00_fileNum.dat")
                        .dataTime(month)
                        .step("1")
                        .build()
        );

    }

    @Override
    public List<Map<String, Object>> getCampaignedInfo(String activityId) {
        final List<Map<String, Object>> campaignedInfo93001 = getFileDataMapper.getCampaignedInfo(activityId);
        return campaignedInfo93001;
    }

}
