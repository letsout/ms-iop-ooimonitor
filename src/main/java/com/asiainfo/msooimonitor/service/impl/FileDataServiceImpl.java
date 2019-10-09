package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.config.SendMessage;
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
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class FileDataServiceImpl implements FileDataService {
    @Autowired
    GetFileDataMapper getFileDataMapper;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    InterfaceInfoMpper interfaceInfoMpper;
    @Autowired
    SendMessage sendMessage;

    @Override
    public void insertFailDetails(List<UploadDetailInfo> list) {
        if (list == null || list.size() == 0)
            return;
        getFileDataMapper.insertFailDetails(list);

    }

    @Override
    public Map<String, String> getSummaryEffectAll(String activityId, String activityEndDate) {
        List<Map<String, Object>> activityEndTimeList = getFileDataMapper.getActivityEndTime(activityId);
        if (activityEndTimeList == null || activityEndTimeList.size() == 0)
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
        String activityIds = "";
        for (Map<String, Object> map : activityEndTimeList) {
            // 获取最大时间
//            String time = interfaceInfoMpper.getMaxTime(map.get("activity_id").toString());
            String time = map.get("end_time").toString();
            Map<String, String> summaryEffect = interfaceInfoMpper.getSummaryEffect(map.get("activity_id").toString(), time);
            if (summaryEffect == null) {
                activityIds = activityIds + "," + map.get("activity_id");
                log.info("activityIds:{}", activityIds);
                continue;
            }
            customer_group_id += "," + summaryEffect.get("customer_group_id");
            customer_group_name += "," + summaryEffect.get("customer_group_name");
            customer_num += "," + summaryEffect.get("customer_num");
            customer_filter_rule = summaryEffect.get("customer_filter_rule").toString();
            customerNum += Integer.valueOf(summaryEffect.get("customer_num").toString());
            touchNum += Integer.valueOf(summaryEffect.get("touch_num").toString());
            vicNum += Integer.valueOf(summaryEffect.get("vic_num").toString());
            inOutRate += Float.valueOf(summaryEffect.get("in_out_rate").toString());
            terminalFlowRate += Float.valueOf(summaryEffect.get("terminal_flow_rate").toString());
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
        if (StringUtils.isNotEmpty(activityIds)) {
            activityIds = activityIds.substring(1);
            String message = activityIds + "在" + activityEndDate + "当天缺少效果数据，请核查";
            sendMessage.sendSms(message);
        }
        return map;
    }

    @Override
    public void insertInterfaceRelTable(CretaeFileInfo cretaeFileInfo) {
        getFileDataMapper.updateStateInterfaceRelTable(cretaeFileInfo);
        getFileDataMapper.insertInterfaceRelTable(cretaeFileInfo);

    }

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
//    @Override
//    public List<Map<String, String>> getMarkingInfo93006(String activityEndDate) {
//        // 一级策划省级执行
//        List<Map<String, String>> markingInfo93006 = getFileDataMapper.getMarkingInfo93006(activityEndDate);
//        markingInfo93006.forEach(markingmap -> {
//            final String activity_id = markingmap.get("activity_id");
//            final List<Map<String, Object>> campaignedInfo = getFileDataMapper.getCampaignedInfo(activity_id);
//            String campaignId = "";
//            String campaignName = "";
//            for (Map<String, Object> map : campaignedInfo) {
//                campaignId += ",280_" + map.get("campaign_id") + "_" + map.get("iop_activity_id").toString().substring(1);
//                campaignName += "," + map.get("activity_name");
//            }
//            if (StringUtils.isNotEmpty(campaignId)) {
//                campaignId = campaignId.substring(1);
//            }
//            if (StringUtils.isNotEmpty(campaignName)) {
//                campaignName = campaignName.substring(1);
//            }
//            markingmap.put("campaign_id", campaignId);
//            markingmap.put("campaign_name", campaignName);
//        });
//        return markingInfo93006;
//    }


    @Override
    public void insertFlow() {
        final List<Map<String, String>> flowInfo1 = getFileDataMapper.getFlowInfo1();
        if (flowInfo1.size() != 0)
            getFileDataMapper.insertFlow(flowInfo1);
        final List<Map<String, String>> flowInfo2 = getFileDataMapper.getFlowInfo2();
        if (flowInfo2.size() != 0)
            getFileDataMapper.insertFlow(flowInfo2);
    }


//    @Override
//    public String getIOPActivityIds(String activityId, String date) {
//        List<String> iopActivityIds = getFileDataMapper.getIOPActivityIds(activityId, date);
//        String activitys = "'" + StringUtils.join(iopActivityIds, "','") + "'";
//        return activitys;
//    }


    @Override
    public void truncateTable(String tableName) {
        interfaceInfoMpper.truncateTable(tableName);
    }


    @Override
    @Transactional(transactionManager = "MysqlTransactionManager", rollbackFor = Exception.class)
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
            if (CommonConstant.SJCHSJ.equals(processId) || CommonConstant.SJCHHLW.equals(processId)) {
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
                paramMap.put("A9", "0428000" + ((productInfo == null) ? "00" : productInfo.get("prc_id")));
                paramMap.put("A10", (productInfo == null) ? "自定义产品名称" : productInfo.get("prc_name"));
            } else if (CommonConstant.YJCH.equals(processId)) {
                // 查询集团活动详情
                Map<String, String> jtActivityInfo = getFileDataMapper.getJTActivityInfoById(activityId);
                paramMap.put("A2", CommonConstant.SC);
                paramMap.put("A3", CommonConstant.cityMap.get("1"));
                paramMap.put("A4", "280" + jtActivityInfo.get("jt_activity_id"));
                paramMap.put("A5", jtActivityInfo.get("jt_activity_name"));
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
        this.insertFailDetails(uploadDetailList);

        // 清空当前数据表
        interfaceInfoMpper.truncateTable("93055");

        // 插入数据表待上传
        interfaceInfoMpper.insert93055(paramList);
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
    @Transactional(transactionManager = "MysqlTransactionManager", rollbackFor = Exception.class)
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
            if (CommonConstant.SJCHSJ.equals(processId) || CommonConstant.SJCHHLW.equals(processId)) {
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
                paramMap.put("A9", "0428000" + ((productInfo == null) ? "00" : productInfo.get("prc_id")));
                paramMap.put("A10", (productInfo == null) ? "自定义产品名称" : productInfo.get("prc_name"));
            } else if (CommonConstant.YJCH.equals(processId)) {
                // 查询集团活动详情
                List<Map<String, String>> jtActivityInfo = getFileDataMapper.getJTActivityInfoByOOIId(activityId);
                AtomicReference<String> ooiActivityId = new AtomicReference<>(new String());
                AtomicReference<String> ooiActivityName = new AtomicReference<>(new String());
                ArrayList<String> campaignIdList = new ArrayList<>();
                ArrayList<String> campaigNameList = new ArrayList<>();
                ArrayList<String> offerCodeList = new ArrayList<>();
                ArrayList<String> offerNameList = new ArrayList<>();
                for (Map<String, String> map :
                        jtActivityInfo) {
                    AtomicReference<String> campaignId = new AtomicReference<>(new String());
                    AtomicReference<String> iopActivityId = new AtomicReference<>(new String());
                    map.forEach((k, v) -> {
                        switch (k) {
                            case "ooi_campaign_id":
                                campaignId.set(v);
                                break;
                            case "activity_name":
                                campaigNameList.add(v);
                                break;
                            case "offer_code":
                                offerCodeList.add(v);
                                break;
                            case "offer_name":
                                offerNameList.add(v);
                                break;
                            case "jt_activity_id":
                                ooiActivityId.set(v);
                                break;
                            case "jt_activity_name":
                                ooiActivityName.set(v);
                                break;
                            case "activity_id":
                                iopActivityId.set(v);
                                break;
                            default:
                                break;
                        }
                    });
                    campaignIdList.add(CommonConstant.SC + "_" + campaignId.get() + "_" + iopActivityId.get().substring(1));
                }
                paramMap.put("A2", CommonConstant.SC);
                paramMap.put("A3", CommonConstant.cityMap.get("1"));
                paramMap.put("A4", "280" + ooiActivityId.get());
                paramMap.put("A5", ooiActivityName.get());
                paramMap.put("A6", "9");
                // TODO 具备多个子活动怎么处理 联调时与一级沟通
                paramMap.put("A7", StringUtils.join(campaignIdList,","));
                paramMap.put("A8",StringUtils.join(campaigNameList,","));
                paramMap.put("A9", StringUtils.join(offerCodeList,","));
                paramMap.put("A10", StringUtils.join(offerNameList,","));
            }
            // 不记录最终插入语句
            paramMap.put("processId", processId);
            paramMap.put("activityId", activityId);

            // 校验必填参数不能为空
            boolean isNotNull = filterParamer(paramMap);

            if (!isNotNull) {
                errorNum++;
                paramMap.put("error", "该行数据存在空字段");
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

        // 清空当前数据表
        interfaceInfoMpper.truncateTable("93056");

        // 插入数据表待上传
        interfaceInfoMpper.insert93056(paramList);
    }
}
