package com.asiainfo.msooimonitor.task;

import com.alibaba.fastjson.JSON;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.utils.SqlUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author yx
 * @date 2019/9/6  17:29
 * Description
 */
@Component
@Slf4j
public class TaskSaveMethod {

    @Autowired
    FileDataService fileDataService;

    private final int limitNum = 10000;

    public void savebase93006(String activityEndDate) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getBaseInfo93006(activityEndDate);
        log.debug("activitys.size=" + activitys.size());
        //1 行号
        for (Map<String, String> activity : activitys) {
            try {
                map = new HashMap<>();
                //2 统计时间 必填,长度14位,为数据生成时间
                map.put("A2", TimeUtil.getLongSeconds(new Date()));
                //3 省份 必填，长度3位
                map.put("A3", "280");
                //4 地市 必填,长度： 3位或4位
                map.put("A4", activity.get("city_code"));
                //6 营销活动编号 必填,前三位必须为省份编码
                String activity_id = activity.get("activity_id");
                map.put("A6", "280" + activity_id.substring(1));
                //7 营销活动名称 必填
                map.put("A7", activity.get("activity_name"));
                //8 子活动编号 可为空，参考附录1统一编码规则中的营销子活动编号编码规则；当营销活动涉及多子活动时，以逗号分隔
                map.put("A8", "");
                //9 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A9", "");
                //10 PV 可为空,口径：该用户打开网页次数,电子渠道效果指标
                map.put("A10", "");
                //11 用户点击量 可为空,口径：页面内容被该用户点击的次数,电子渠道效果指标
                map.put("A11", "");
                //12 用户办理量 可为空,口径：该用户业务办理次数,电子渠道效果指标
                map.put("A12", "");
//            //16 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A16", "");
                int num = fileDataService.getTableRows("'"+activity.get("activity_id")+"'", TimeUtil.getLastDaySql(new Date()));
                int start = 0;
                int end = num;
                for (int i = 0; i < num / limitNum; i++) {
                    List<Map<String, String>> detaileffect = fileDataService.getDetailEffect(activity.get("activity_id"), TimeUtil.getLastDaySql(new Date()), start, limitNum);
                    log.debug("detaileffect:" + JSON.toJSONString(detaileffect.get(0)));
                    for (Map<String, String> mapEffect : detaileffect) {
                        mapresult = new HashMap<>(map);
                        mapresult.put("A5", mapEffect.get("phone_no"));
                        //13 是否成功接触 必填,口径：运营活动中，通过各触点，是否成功接触到该用户，如短信下发成功下发给该用户、外呼成功接通该用户、APP成功在该用户终端弹出等
                        mapresult.put("A13", mapEffect.get("is_touch"));
                        //14 是否参与运营活动 必填,标识该用户是否参与运营活动
                        mapresult.put("A14", mapEffect.get("is_join_activity"));
                        //15 是否营销 必填,口径：根据运营目的，该用户是否成功办理或者成功使用的运营产品
                        mapresult.put("A15", mapEffect.get("is_marketed"));
                        //17 0x0D0A 行间分隔符－回车换行符
                        //5 用户号码 必填,运营对象手机号码
                        mapresult.putAll(map);
                        list.add(mapresult);
                    }
                    SqlUtil.getInsert("93006", list);
                    list.clear();
                    start += limitNum;
                    end -= limitNum;
                }
                List<Map<String, String>> detaileffect = fileDataService.getDetailEffect("'" + activity.get("activity_id") + "'", TimeUtil.getLastDaySql(new Date()), start, end);
                for (Map<String, String> mapEffect : detaileffect) {
                    mapresult = new HashMap<>(map);
                    mapresult.put("A5", mapEffect.get("phone_no"));
                    //13 是否成功接触 必填,口径：运营活动中，通过各触点，是否成功接触到该用户，如短信下发成功下发给该用户、外呼成功接通该用户、APP成功在该用户终端弹出等
                    mapresult.put("A13", mapEffect.get("is_touch"));
                    //14 是否参与运营活动 必填,标识该用户是否参与运营活动
                    mapresult.put("A14", mapEffect.get("is_join_activity"));
                    //15 是否营销 必填,口径：根据运营目的，该用户是否成功办理或者成功使用的运营产品
                    mapresult.put("A15", mapEffect.get("is_marketed"));
                    //17 0x0D0A 行间分隔符－回车换行符
                    //5 用户号码 必填,运营对象手机号码
                    mapresult.putAll(map);
                    list.add(mapresult);
                }
                SqlUtil.getInsert("93006", list);
                list.clear();
            } catch (Exception e) {
                log.error("93006 base 接口异常:{}", e);
                Map<String, String> failMap = new HashMap();
                failMap.put("activity_id", activity.get("activity_id"));
                failMap.put("interface_name", "93006");
                failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                if(e.getMessage().length() > 2000){
                    failMap.put("error_desc",e.getMessage().substring(0,1999));
                }else {
                    failMap.put("error_desc",e.getMessage());
                }
                //System.out.println("json=" + JSON.toJSONString(failMap));
                fileDataService.insertFailInterface(failMap);
                throw new Exception("接口异常");
            }
        }
        SqlUtil.getInsert("93006", list);

    }

    public void savemarking93006(String activityEndDate) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93006(activityEndDate);
        //System.out.println("activitys:" + activitys);
        //1 行号
        for (Map<String, String> activity : activitys) {
            try {
                map = new HashMap<>();
                //2 统计时间 必填,长度14位,为数据生成时间
                map.put("A2", TimeUtil.getLongSeconds(new Date()));
                //3 省份 必填，长度3位
                map.put("A3", "280");
                //4 地市 必填,长度： 3位或4位
                map.put("A4", activity.getOrDefault("city_code", "028"));
                //6 营销活动编号 必填,前三位必须为省份编码
                String activity_id = activity.get("activity_id");
                map.put("A6", activity_id);
                //7 营销活动名称 必填
                map.put("A7", activity.get("activity_name"));
                //8 子活动编号 可为空，参考附录1统一编码规则中的营销子活动编号编码规则；当营销活动涉及多子活动时，以逗号分隔
                map.put("A8", activity.get("campaign_id"));
                //9 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A9", activity.get("campaign_name"));
                //10 PV 可为空,口径：该用户打开网页次数,电子渠道效果指标
                map.put("A10", "");
                //11 用户点击量 可为空,口径：页面内容被该用户点击的次数,电子渠道效果指标
                map.put("A11", "");
                //12 用户办理量 可为空,口径：该用户业务办理次数,电子渠道效果指标
                map.put("A12", "");
//            //16 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A16", activity.get("spetopic_id"));
                // 根据集团下发活动查询关联iop的活动
                String activityIds = fileDataService.getIOPActivityIds(activity.get("activity_id"));
                if ("''".equals(activityIds)) {
                    continue;
                }
                // 查询当前表中数据量
                int num = fileDataService.getTableRows(activityIds, TimeUtil.getLastDaySql(new Date()));
                int start = 0;
                int end = num;
                for (int i = 0; i < num / limitNum; i++) {
                    List<Map<String, String>> detaileffect = fileDataService.getDetailEffect(activityIds, TimeUtil.getLastDaySql(new Date()), start, limitNum);
                    log.debug("detaileffect:" + JSON.toJSONString(detaileffect.get(0)));
                    for (Map<String, String> mapEffect : detaileffect) {
                        mapresult = new HashMap<>(map);
                        //5 用户号码 必填,运营对象手机号码
                        mapresult.put("A5", mapEffect.get("phone_no"));
                        //13 是否成功接触 必填,口径：运营活动中，通过各触点，是否成功接触到该用户，如短信下发成功下发给该用户、外呼成功接通该用户、APP成功在该用户终端弹出等
                        mapresult.put("A13", mapEffect.get("is_touch"));
                        //14 是否参与运营活动 必填,标识该用户是否参与运营活动
                        mapresult.put("A14", mapEffect.get("is_join_activity"));
                        //15 是否营销 必填,口径：根据运营目的，该用户是否成功办理或者成功使用的运营产品
                        mapresult.put("A15", mapEffect.get("is_marketed"));
                        //17 0x0D0A 行间分隔符－回车换行符
                        mapresult.putAll(map);
                        list.add(mapresult);
                    }
                    SqlUtil.getInsert("93006", list);
                    list.clear();
                    start += limitNum;
                    end -= limitNum;
                }
                List<Map<String, String>> detaileffect = fileDataService.getDetailEffect(activityIds, TimeUtil.getLastDaySql(new Date()), start, end);
                for (Map<String, String> mapEffect : detaileffect) {
                    mapresult = new HashMap<>(map);
                    //5 用户号码 必填,运营对象手机号码
                    mapresult.put("A5", mapEffect.get("phone_no"));
                    //13 是否成功接触 必填,口径：运营活动中，通过各触点，是否成功接触到该用户，如短信下发成功下发给该用户、外呼成功接通该用户、APP成功在该用户终端弹出等
                    mapresult.put("A13", mapEffect.get("is_touch"));
                    //14 是否参与运营活动 必填,标识该用户是否参与运营活动
                    mapresult.put("A14", mapEffect.get("is_join_activity"));
                    //15 是否营销 必填,口径：根据运营目的，该用户是否成功办理或者成功使用的运营产品
                    mapresult.put("A15", mapEffect.get("is_marketed"));
                    //17 0x0D0A 行间分隔符－回车换行符
                    mapresult.putAll(map);
                    list.add(mapresult);
                }
                SqlUtil.getInsert("93006", list);
                list.clear();
            } catch (Exception e) {
                Map<String, String> failMap = new HashMap();
                failMap.put("activity_id", activity.get("activity_id"));
                failMap.put("interface_name", "93006");
                failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                if(e.getMessage().length() > 2000){
                    failMap.put("error_desc",e.getMessage().substring(0,1999));
                }else {
                    failMap.put("error_desc",e.getMessage());
                }

                fileDataService.insertFailInterface(failMap);
                log.error("93006 error:{}", e);
                throw new Exception("接口异常");
            }
        }
    }


    public void saveMarking93001(String date, String summaryDate, String summaryDateBefore) throws Exception {

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> resultmap = null;
        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93001(date);
        //属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-50子活动效果评估指标
        for (Map<String, String> activity : activitys) {
            try {
                map = new HashMap<>();
                //2	统计时间	必填,长度14位,为数据生成时间
                map.put("A2", TimeUtil.getLongSeconds(new Date()));
                //3	省份	必填(长度3位),省份与互联网编码参考9.1省公司编码
                map.put("A3", "280");
                //4	地市	必填,长度： 每个地市长度3位或4位（未知地市为00000除外）
                map.put("A4", activity.getOrDefault("city_code", "028"));

                //营销活动相关信息
                //5	营销活动编号	必填,前三位必须为省份编码
                String activity_id = activity.get("activity_id");
                map.put("A5", activity_id);
                //6	营销活动名称	必填
                map.put("A6", activity.get("activity_name"));
                //7	活动开始时间	必填,长度14位,为数据生成时间
                map.put("A7", activity.get("activity_starttime").replace("/", "").replace(" ", "").replace(":", ""));
                //8	活动结束时间	必填,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
                map.put("A8", activity.get("activity_endtime").replace("/", "").replace(" ", "").replace(":", ""));
                //9	营销活动类型	必填，填写枚举值ID
                map.put("A9", activity.getOrDefault("activity_type", "9"));
                //10	营销活动目的	必填，填写枚举值ID
                map.put("A10", activity.getOrDefault("activity_objective", "9"));
                //11	营销活动描述	对产品、服务等信息进行简要描述
                map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
                //12	PCC策略编码	（当采用了PCC能力时，相关内容必填）
                map.put("A12", activity.getOrDefault("pcc_id", ""));
                //13	所属流程	必填,数字枚举值
                map.put("A13", "1");


                //37	PV	可为空，,口径：页面曝光量、接触量、浏览量。,电子渠道效果指标
                map.put("A37", "");
                //38	点击量	可为空，,口径：页面内容被点击的次数。,电子渠道效果指标
                map.put("A38", "");
                //39	UV（剔重）	可为空，,口径：独立用户/独立访客。,电子渠道效果指标
                map.put("A39", "");
                //40	办理量	可为空，,口径：业务办理次数。,电子渠道效果指标
                map.put("A40", "");
                //41	用户号码明细	互联网特有，可为空
                map.put("A41", "");
//            //42	活动专题ID	当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A42", activity.get("spetopic_id"));


                //子活动效果评估指标
                Map<String, String> mapEffect = fileDataService.getSummaryEffectJT(activity_id, summaryDate, "ZHD");
                Map<String, String> mapEffect1 = fileDataService.getSummaryEffectJT(activity_id, summaryDateBefore, "ZHD");
                if (mapEffect == null)
                    return;
                if (mapEffect1 == null)
                    return;
                //42	成功接触客户数	日指标，必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                map.put("A42", String.valueOf(Integer.parseInt(mapEffect.get("touch_num")) - Integer.parseInt(mapEffect1.get("touch_num"))));
                //43	接触成功率	日指标，必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
                map.put("A43", String.valueOf(Integer.parseInt(mapEffect.get("touhe_rate")) - Integer.parseInt(mapEffect1.get("touhe_rate"))));
                //44	响应率	日指标，必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,		（注意需填小数，而不是百分数）
                map.put("A44", String.valueOf(Integer.parseInt(mapEffect.get("response_rate")) - Integer.parseInt(mapEffect1.get("response_rate"))));
                //45	营销成功用户数	日指标，必填；,口径：根据运营目的，成功办理或者成功使用的用户数
                map.put("A45", String.valueOf(Integer.parseInt(mapEffect.get("vic_num")) - Integer.parseInt(mapEffect1.get("vic_num"))));
                //46	营销成功率	NUMBER (20,6)日指标,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%
                map.put("A46", String.valueOf(Integer.parseInt(mapEffect.get("vic_rate")) - Integer.parseInt(mapEffect1.get("vic_rate"))));
                //47	4G终端4G流量客户占比	日指标，必填且取值小于1；,口径：4G流量客户数/4G终端用户数,例：填0.1代表10%,		（注意需填小数，而不是百分数）
                map.put("A47", String.valueOf(Integer.parseInt(mapEffect.get("terminal_flow_rate")) - Integer.parseInt(mapEffect1.get("terminal_flow_rate"))));
                //48 4G流量客户数,日指标，选填，口径：统计周期内，使用4G网络产生4G流量的客户数
                map.put("A48", "");

                //子活动相关信息
                List<Map<String, String>> campaignedList = fileDataService.getCampaignedInfo(activity_id);
                for (Map<String, String> campaignedmap : campaignedList) {
                    try {
                        resultmap = new HashMap<>();
                        //14	子活动编号	必填,参考附录1统一编码规则中的营销子活动编号编码规则,所属流程为2、8、9、10时，前3位编号为省份编码
                        resultmap.put("A14", campaignedmap.get("campaign_id"));
                        //15	子活动名称	必填
                        resultmap.put("A15", campaignedmap.get("campaign_name"));
                        //16	子活动开始时间	必填,长度14位,为数据生成时间
                        resultmap.put("A16", campaignedmap.get("campaign_starttime").replace("/", "").replace(":", "").replace(" ", ""));
                        //17	子活动结束时间	必填,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
                        resultmap.put("A17", campaignedmap.get("campaign_endtime").replace("/", "").replace(":", "").replace(" ", ""));
                        Map<String, String> mapCampaignedEffect = fileDataService.getSummaryEffectJT(campaignedmap.get("campaign_id"), summaryDate, "ZHD");
                        if (mapCampaignedEffect == null)
                            return;
                        //18	目标客户群编号	必填
                        resultmap.put("A18", mapCampaignedEffect.get("customer_group_id"));
                        //19	目标客户群名称	必填
                        resultmap.put("A19", mapCampaignedEffect.get("customer_group_name"));
                        //20	目标客户群规模	可为空
                        resultmap.put("A20", mapCampaignedEffect.get("customer_num"));
                        //21	目标客户群描述	可为空
                        resultmap.put("A21", "");
                        //22	目标客户筛选标准	必填
                        resultmap.put("A22", mapCampaignedEffect.get("customer_filter_rule"));

                        List<Map<String, String>> offerMaps = fileDataService.getOfferBo(campaignedmap.get("campaign_id"));
                        Map<String, String> offerMap = offerMaps.get(0);
                        //23	产品编码	必填,前七位需符合8.1产品编码规则
                        String proCode = offerMap.get("offer_code");
                        resultmap.put("A23", proCode);
                        //24	产品编码截取	必填,长度限制7位,		（省份截取前7位）
                        resultmap.put("A24", proCode.substring(0, 7));
                        //25	产品名称	必填
                        resultmap.put("A25", offerMap.get("offer_name"));
                        //26	产品分类	必填，填写枚举值ID
                        resultmap.put("A26", offerMap.get("offer_type"));
                        //27	渠道编码	必填,比如,00108xxxx,001：一级分类,08：二级分类,Xxxx：自定义渠道编码,编码规则参考8.2渠道和运营位编码规则
                        String channel_id = campaignedmap.get("channel_id");
                        resultmap.put("A27", channel_id);
                        //28	渠道编码一级分类	必填,长度3位,		（省份截取前3位）
                        resultmap.put("A28", channel_id.substring(0, 3));
                        //29	渠道编码二级分类	必填,长度2位,		（省份截取第4、5位）
                        resultmap.put("A29", channel_id.substring(3, 5));
                        //30	渠道名称	必填
                        resultmap.put("A30", campaignedmap.get("channel_name"));
                        //31	渠道类型	必填,参考10附录3渠道类型编码,位数为偶数位
                        resultmap.put("A31", campaignedmap.get("channe_type"));
                        //32	渠道接触规则	必填
                        resultmap.put("A32", campaignedmap.get("channel_rule"));
                        //33	时机识别	必填，填写枚举值ID
                        resultmap.put("A33", campaignedmap.get("time_id"));
                        //34	时机识别描述	可为空
                        resultmap.put("A34", campaignedmap.get("time_distindes"));
                        //35	客户质量情况	描述性信息,可为空
                        resultmap.put("A35", "");
                        //36	资源使用情况	描述性信息,可为空
                        resultmap.put("A36", "");
                        resultmap.putAll(map);
                        list.add(resultmap);
                    } catch (Exception e) {
                        Map<String, String> failMap = new HashMap();
                        failMap.put("activity_id", activity.get("activity_id"));
                        failMap.put("interface_name", "93001");
                        failMap.put("campaign_id", campaignedmap.get("campaign_id"));
                        failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                        failMap.put("error_desc", e.getMessage().substring(0, 2000));
                        e.printStackTrace();
                        fileDataService.insertFailInterface(failMap);
                        log.error("93001 接口异常:{}", e);
                        throw new Exception("接口异常");
                    }
                }
            } catch (Exception e1) {
                Map<String, String> failMap = new HashMap();
                failMap.put("activity_id", activity.get("activity_id"));
                failMap.put("interface_name", "93001");
                failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                failMap.put("error_desc", e1.getMessage());
                e1.printStackTrace();
                fileDataService.insertFailInterface(failMap);
                log.error("93006 base 接口异常:{}", e1);
                throw new Exception("接口异常");
            }
        }
        SqlUtil.getInsert("93001", list);
    }


    public void saveMarking93005(String activityEndDate, String summaryDate) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93005(activityEndDate);


        //属性编码 5-13、18-33为营销活动相关信息，14-17子活动相关信息，40-84营销活动效果评估指标
        for (Map<String, String> activity : activitys) {
            try {
                map = new HashMap<>();
                //1 行号
                //2 统计时间 必填,长度14位,为数据生成时间
                map.put("A2", TimeUtil.getLongSeconds(new Date()));
                //3 省份 必填(长度3位),省份与互联网编码参考9.1省公司编码
                map.put("A3", "280");
                //4 地市 必填 ,长度： 每个地市长度3位或4位（未知地市为00000除外）
                map.put("A4", activity.getOrDefault("city_code", "028"));

                /**
                 *  5-13、18-33为营销活动相关信息
                 */
                //5 营销活动编号 必填,前三位必须为省份编码
                String activityId = activity.get("activity_id");
                map.put("A5", activityId);
                //6 营销活动名称 必填
                map.put("A6", activity.get("activity_name"));
                //7 活动开始时间 必填,长度14位
                map.put("A7", activity.get("activity_starttime").replace("/", "").replace(":", "").replace(" ", ""));
                //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
                map.put("A8", activity.get("activity_endtime").replace("/", "").replace(":", "").replace(" ", ""));
                //9 营销活动类型 必填，填写枚举值ID
                map.put("A9", activity.getOrDefault("activity_type", "9"));
                //10 营销活动目的 必填，填写枚举值ID
                map.put("A10", activity.getOrDefault("activity_objective", "9"));
                //11 营销活动描述 对产品、服务等信息进行简要描述
                map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
                //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
                map.put("A12", activity.get("pcc_id"));
                //13 所属流程 必填，填写枚举值ID
                map.put("A13", "1");     //待定
                // 根据集团下发活动获取效果信息
                final Map<String, String> mapEffect = fileDataService.getSummaryEffectJT(activityId, summaryDate, "HD");
                if (mapEffect == null)
                    return;
                //18 目标客户群编号 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A18", mapEffect.get("customer_group_id"));
                //19 目标客户群名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A19", mapEffect.get("customer_group_name"));
                //20 目标客户群规模 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A20", mapEffect.get("customer_num"));
                //21 目标客户群描述 可为空
                map.put("A21", "");
                //22 目标客户筛选标准 必填
                map.put("A22", mapEffect.get("customer_filter_rule"));


                //23 产品编码 可为空
                //比如0200100xxxx
                //02：一级分类
                //001：二级分类
                //00：三级分类
                //Xxxx：自定义产品编号
                //编码规则参考8.1产品编码规则
                //当营销活动涉及多子活动时，以逗号分隔
                map.put("A23", activity.get("offer_code"));
                //24 产品名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A24", activity.get("offer_name"));
                //25 产品分类 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A25", activity.get("offer_type"));
                //26 渠道编码 可为空
                //比如
                //00108xxxx
                //001：一级分类
                //08：二级分类
                //Xxxx：自定义渠道编码
                //编码规则参考8.2渠道和运营位编码规则
                //当营销活动涉及多子活动时，以逗号分隔
                map.put("A26", "");
                //27 渠道名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A27", "");
                //28 渠道类型 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A28", "");
                //渠道类型为偶数位
                //29 渠道接触规则 可为空
                map.put("A29", "");
                //30 时机识别 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A30", "");
                //31 时机识别描述 可为空
                map.put("A31", "");
                //32 客户质量情况 描述性信息
                //可为空
                map.put("A32", "");
                //33 资源使用情况 描述性信息
                //可为空
                map.put("A33", "");

/**
 * 14-17子活动相关信息
 */

                //14 子活动编号 可为空，参考附录1统一编码规则中的营销子活动编号编码规则；当营销活动涉及多子活动时，以逗号分隔
                map.put("A14", activity.get("campaign_id"));
                //15 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A15", activity.get("campaign_name"));
                //16 子活动开始时间 可为空,格式：YYYYMMDDHH24MISS,示例：20170213161140
                final String campaign_starttime = activity.get("campaign_starttime").replace(" ", "").replace("/", "").replace(":", "");
                map.put("A16", campaign_starttime.split(",")[0]);
                //17 子活动结束时间 可为空,格式：YYYYMMDDHH24MISS,子活动结束时间不早于子活动开始时间,示例：20170213161140
                final String campaign_endtime = activity.get("campaign_starttime").replace(" ", "").replace("/", "").replace(":", "");
                map.put("A17", campaign_endtime.split(",")[0]);


                //34 PV 可为空，
                //口径：页面曝光量、接触量、浏览量。
                //电子渠道效果指标
                map.put("A34", "");
                //35 点击量 可为空，
                //口径：页面内容被点击的次数。
                //电子渠道效果指标
                map.put("A35", "");
                //36 UV(剔重) 可为空，
                //口径：独立用户/独立访客。
                //电子渠道效果指标
                map.put("A36", "");
                //37 办理量 可为空，
                //口径：业务办理次数。
                //电子渠道效果指标
                map.put("A37", "");
                //38 用户号码明细 互联网特有，可为空
                map.put("A38", "");
                //     //39 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
                //map.put("A39", activity.get("spetopic_id"));
/**
 * 40-84营销活动效果评估指标
 */
                //39 成功接触客户数 必填
                //口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                map.put("A39", mapEffect.get("touch_num"));
                //40 接触成功率 必填且取值小于1；
                //口径：成功接触客户数/活动总客户数
                //例：填0.1代表10%（注意需填小数，而不是百分数）
                map.put("A40", mapEffect.get("touhe_rate"));
                //41 响应率 必填且取值小于1；
                //口径：运营活动参与用户/成功接触用户
                //例：填0.1代表10%
                //  （注意需填小数，而不是百分数）
                map.put("A41", mapEffect.get("response_rate"));
                //42 营销成功用户数 必填；
                //口径：根据运营目的，成功办理或者成功使用的用户数
                map.put("A42", mapEffect.get("vic_num"));
                //43 营销成功率 必填且取值小于1；
                //口径：营销成功用户数/成功接触客户数
                //例：填0.1代表10%
                //  （注意需填小数，而不是百分数）
                map.put("A43", mapEffect.get("vic_rate"));
                //44 投入产出比 选填：
                //营销成功用户总收入/运营活动投入的成本
                map.put("A44", "");
                //     //45 使用用户数 必填：
                //     //运营成功用户产生本业务使用行为的用户
                //map.put("A45", mapEffect.get("touch_num"));
                //     //46 活动总用户数 必填：
                //map.put("A46", mapEffect.get("customer_num"));
                //45 PCC签约用户数 选填：
                //该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
                map.put("A45", "");
                //46 PCC策略生效用户数 选填：
                //该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
                map.put("A46", "");
                //47 PCC策略生效次数 选填：
                //该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
                map.put("A47", "");
                //48 签约客户转化率 选填：
                //该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）
                //例：填0.1代表10%
                map.put("A48", "");
                //49	套餐流量使用用户数	NUMBER(32)	选填；口径： 统计周期（活动开始时间至活动结束时间）套餐中产生流量的用户数量
                map.put("A49", "");


                //50	套餐流量饱和度	NUMBER (20,6)选填； 口径： 统计周期（活动开始时间至活动结束时间） 套餐用户产生的套餐内总流量/套餐包含的流量资源总数            例：填0.1代表10%
                map.put("A50", "");

                //51	套餐流量活跃度NUMBER (20,6)选填； 口径： 统计周期（活动开始时间至活动结束时间）套餐流量使用用户数/套餐用户数            例：填0.1代表10%
                map.put("A51", "");

                //52	套餐流量低使用天数（5天）占比NUMBER (20,6)选填；口径：  统计周期（活动开始时间至活动结束时间） 使用流量的天数少于5天的用户占比  例：填0.1代表10%

                map.put("A52", "");


                //	53	4G客户次月留存率	选填；,口径：,次月4G用户/本月的4G用户,例：填0.1代表10%
                map.put("A53", "");
                //	54	低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用流量少于100M的用户占比,例：填0.1代表10%
                map.put("A54", "");
                //	55	语音使用用户	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐中产生语音的用户占比
                map.put("A55", "");
                //	56	套餐语音饱和度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐用户产生的套餐内总语音时长/套餐包含的语音资源总数,例：填0.1代表10%
                map.put("A56", "");
                //	57	套餐语音活跃用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐语音使用用户数/套餐用户数,例：填0.1代表10%
                map.put("A57", "");
                //	58	套餐语音低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音的天数少于5天的用户占比,例：填0.1代表10%
                map.put("A58", "");
                //	59	低通话量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音少于10分钟的用户占比,例：填0.1代表10%
                map.put("A59", "");
                //	60	4G终端4G流量客户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G流量客户数/4G终端用户数,例：填0.1代表10%
                map.put("A60", "");
                //	61	4G流量客户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,本月使用4G网络产生4G流量的客户数
                map.put("A61", "");
                //	62	4G客户中4G流量低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G客户中，本月产生4G流量天数低于5天的用户占比,例：填0.1代表10%
                map.put("A62", "");
                //	63	4G客户中4G低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G低流量客户占比=本月4G客户中移动数据流量低于100M客户/本月4G客户（4G客户，指使用4G网络客户数）,例：填0.1代表10%
                map.put("A63", "");
                //	64	月一次使用用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用一次的用户/本周期使用用户数,例：填0.1代表10%
                map.put("A64", "");
                //	65	包月产品活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,包月付费且使用用户数/统计周期包月付费用户,例：填0.1代表10%
                map.put("A65", "");
                //	66	使用用户次月留存率	选填；,口径：,统计周期，次月持续使用行为的用户数/统计月的使用用户数,例：填0.1代表10%
                map.put("A66", "");
                //	67	家庭宽带帐户活跃用户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,流量大于0的用户
                map.put("A67", "");
                //	68	家庭宽带帐户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,家庭宽带活跃客户数与家庭宽带出账客户数的比值。家庭宽带活性客户比例=家庭宽带活跃客户数/家庭宽带出账客户数,例：填0.1代表10%
                map.put("A68", "");
                //	69	魔百和用户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,魔百和活跃客户数与魔百和客户数的比值,例：填0.1代表10%
                map.put("A69", "");
                //	70	低使用次数用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,只使用一次\且有流量产生的家庭宽带活跃用户占家庭旷代活跃用户比,例：填0.1代表10%
                map.put("A70", "");
                //	71	家庭宽带使用用户次月留存率	选填；,口径：,上个月活跃,本月继续活跃的家庭宽带使用用户占上月家庭宽带活跃用户比,例：填0.1代表10%
                map.put("A71", "");
                //	72	ARPU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的ARPU/运营活动上月的ARPU-1,例：填0.1代表10%
                map.put("A72", "");
                //	73	流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）运营成功的用户，运营活动次月的流量/运营活动上月的流量-1,例：填0.1代表10%
                map.put("A73", "");
                //	74	4G流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G流量/运营活动上月的4G流量-1,例：填0.1代表10%
                map.put("A74", "");
                //	75	DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的DOU/运营活动上月的DOU-1,例：填0.1代表10%
                map.put("A75", "");
                //	76	4G DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G DOU/运营活动上月的4G DOU-1,例：填0.1代表10%
                map.put("A76", "");
                //	77	MOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的MOU/运营活动上月的MOU-1,例：填0.1代表10%
                map.put("A77", "");
                //	78	通话时长提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的通话时长/运营活动上月的通话时长-1,例：填0.1代表10%
                map.put("A78", "");
                //	79	使用用户数	选填，口径：运营成功用户产生本业务使用行为的用户
                map.put("A79", "");
                //	80	家庭宽带出帐用户数	选填，口径：家庭宽带出帐使用用户数
                map.put("A80", "");
                list.add(map);
            } catch (Exception e1) {
                Map<String, String> failMap = new HashMap();
                failMap.put("activity_id", activity.get("activity_id"));
                failMap.put("interface_name", "93005");
                failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                failMap.put("error_desc", e1.getMessage());
                e1.printStackTrace();
                fileDataService.insertFailInterface(failMap);
                throw new Exception("接口异常");
            }
        }
        SqlUtil.getInsert("93005", list);
    }

    public void saveBase93005(String activityEndDate, String summaryDate) throws Exception {

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        List<Map<String, Object>> activitys = fileDataService.getBaseInfo93005(activityEndDate);
        //System.out.println("json:");
        //System.out.println(JSON.toJSONString(activitys));
        for (Map<String, Object> activity : activitys) {
            try {
                map = new HashMap<>();
                //1 行号
                //2 统计时间 必填,长度14位,为数据生成时间
                map.put("A2", TimeUtil.getLongSeconds(new Date()));
                //3 省份 必填(长度3位),省份与互联网编码参考9.1省公司编码
                map.put("A3", "280");
                //4 地市 必填 ,长度： 每个地市长度3位或4位（未知地市为00000除外）
                map.put("A4", activity.getOrDefault("city_code", "028").toString());
                //5 营销活动编号 必填,前三位必须为省份编码
                String activityId = activity.get("activity_id").toString();
                map.put("A5", "280" + activityId.substring(1));
                //6 营销活动名称 必填
                map.put("A6", activity.get("activity_name").toString());
                //7 活动开始时间 必填,长度14位
                String start_time = activity.get("start_time").toString();
                map.put("A7", start_time.replace("-", "") + "000000");
                //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
                String end_time = activity.get("end_time").toString();
                //System.out.println("start_time:" + start_time);
                //System.out.println("end_time:" + end_time);
                map.put("A8", end_time.replace("-", "") + "000000");
                //9 营销活动类型 必填，填写枚举值ID
                map.put("A9", activity.getOrDefault("activity_type", "9").toString());
                //10 营销活动目的 必填，填写枚举值ID
                map.put("A10", activity.getOrDefault("activity_objective", "9").toString());
                //11 营销活动描述 对产品、服务等信息进行简要描述
                map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")).toString());
                //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
                map.put("A12", "");
                //13 所属流程 必填，填写枚举值ID
                map.put("A13", activity.get("flow").toString());     //待定

                final Map<String, String> effectMap = fileDataService.getSummaryEffect(activity.get("activity_id").toString(), summaryDate);
                //18 目标客户群编号 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A18", effectMap.get("customer_group_id"));
                //19 目标客户群名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A19", effectMap.get("customer_group_name"));
                //20 目标客户群规模 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A20", effectMap.get("customer_num"));
                //21 目标客户群描述 可为空
                map.put("A21", effectMap.get(""));
                //22 目标客户筛选标准 必填
                map.put("A22", effectMap.get("customer_filter_rule"));

                //23 产品编码 可为空
                //比如0200100xxxx
                //02：一级分类
                //001：二级分类
                //00：三级分类
                //Xxxx：自定义产品编号
                //编码规则参考8.1产品编码规则
                //当营销活动涉及多子活动时，以逗号分隔
                map.put("A23", "0428000" + activityId.substring(1));
                //24 产品名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A24", "");
                //25 产品分类 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A25", "");
                //26 渠道编码 可为空
                //比如
                //00108xxxx
                //001：一级分类
                //08：二级分类
                //Xxxx：自定义渠道编码
                //编码规则参考8.2渠道和运营位编码规则
                //当营销活动涉及多子活动时，以逗号分隔
                map.put("A26", "");
                //27 渠道名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A27", "");
                //28 渠道类型 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A28", "");
                //渠道类型为偶数位
                //29 渠道接触规则 可为空
                map.put("A29", "");
                //30 时机识别 可为空，当营销活动涉及多子活动时，以逗号分隔
                map.put("A30", "");
                //31 时机识别描述 可为空
                map.put("A31", "");
                //32 客户质量情况 描述性信息
                //可为空
                map.put("A32", "");
                //33 资源使用情况 描述性信息
                //可为空
                map.put("A33", "");


                //14 子活动编号 可为空，参考附录1统一编码规则中的营销子活动编号编码规则；当营销活动涉及多子活动时，以逗号分隔
                //map.put("A14", "280_000"+activity.get("activity_id"));
                map.put("A14", "");
                //15 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
                //map.put("A15", activity.get("campaign_name"));
                map.put("A15", "");
                //16 子活动开始时间 可为空,格式：YYYYMMDDHH24MISS,示例：20170213161140
                //map.put("A16", activity.get("campaign_starttime"));
                map.put("A16", "");
                //17 子活动结束时间 可为空,格式：YYYYMMDDHH24MISS,子活动结束时间不早于子活动开始时间,示例：20170213161140
                //map.put("A17", activity.get("campaign_starttime"));
                map.put("A17", "");


                //34 PV 可为空，
                //口径：页面曝光量、接触量、浏览量。
                //电子渠道效果指标
                map.put("A34", "");
                //35 点击量 可为空，
                //口径：页面内容被点击的次数。
                //电子渠道效果指标
                map.put("A35", "");
                //36 UV(剔重) 可为空，
                //口径：独立用户/独立访客。
                //电子渠道效果指标
                map.put("A36", "");
                //37 办理量 可为空，
                //口径：业务办理次数。
                //电子渠道效果指标
                map.put("A37", "");
                //38 用户号码明细 互联网特有，可为空
                map.put("A38", "");
//            //39 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A39", activity.get("spetopic_id"));

                //39 成功接触客户数 必填
                //口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                map.put("A39", effectMap.get("touch_num"));
                //40 接触成功率 必填且取值小于1；
                //口径：成功接触客户数/活动总客户数
                //例：填0.1代表10%（注意需填小数，而不是百分数）
                map.put("A40", effectMap.get("touhe_rate"));
                //41 响应率 必填且取值小于1；
                //口径：运营活动参与用户/成功接触用户
                //例：填0.1代表10%
                //  （注意需填小数，而不是百分数）
                map.put("A41", effectMap.get("response_rate"));
                //42 营销成功用户数 必填；
                //口径：根据运营目的，成功办理或者成功使用的用户数
                map.put("A42", effectMap.get("vic_num"));
                //43 营销成功率 必填且取值小于1；
                //口径：营销成功用户数/成功接触客户数
                //例：填0.1代表10%
                //  （注意需填小数，而不是百分数）
                map.put("A43", effectMap.get("vic_rate"));
                //44 投入产出比 选填：
                //营销成功用户总收入/运营活动投入的成本
                map.put("A44", "");
                //     //45 使用用户数 必填：
                //     //运营成功用户产生本业务使用行为的用户
                //map.put("A45", mapEffect.get("touch_num"));
                //     //46 活动总用户数 必填：
                //map.put("A46", mapEffect.get("customer_num"));
                //45 PCC签约用户数 选填：
                //该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
                map.put("A45", "");
                //46 PCC策略生效用户数 选填：
                //该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
                map.put("A46", "");
                //47 PCC策略生效次数 选填：
                //该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
                map.put("A47", "");
                //48 签约客户转化率 选填：
                //该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）
                //例：填0.1代表10%
                map.put("A48", "");
                //49	套餐流量使用用户数	NUMBER(32)	选填；口径： 统计周期（活动开始时间至活动结束时间）套餐中产生流量的用户数量
                map.put("A49", "");


                //50	套餐流量饱和度	NUMBER (20,6)选填； 口径： 统计周期（活动开始时间至活动结束时间） 套餐用户产生的套餐内总流量/套餐包含的流量资源总数            例：填0.1代表10%
                map.put("A50", "");

                //51	套餐流量活跃度NUMBER (20,6)选填； 口径： 统计周期（活动开始时间至活动结束时间）套餐流量使用用户数/套餐用户数            例：填0.1代表10%
                map.put("A51", "");

                //52	套餐流量低使用天数（5天）占比NUMBER (20,6)选填；口径：  统计周期（活动开始时间至活动结束时间） 使用流量的天数少于5天的用户占比  例：填0.1代表10%

                map.put("A52", "");


                //	53	4G客户次月留存率	选填；,口径：,次月4G用户/本月的4G用户,例：填0.1代表10%
                map.put("A53", "");
                //	54	低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用流量少于100M的用户占比,例：填0.1代表10%
                map.put("A54", "");
                //	55	语音使用用户	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐中产生语音的用户占比
                map.put("A55", "");
                //	56	套餐语音饱和度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐用户产生的套餐内总语音时长/套餐包含的语音资源总数,例：填0.1代表10%
                map.put("A56", "");
                //	57	套餐语音活跃用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐语音使用用户数/套餐用户数,例：填0.1代表10%
                map.put("A57", "");
                //	58	套餐语音低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音的天数少于5天的用户占比,例：填0.1代表10%
                map.put("A58", "");
                //	59	低通话量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音少于10分钟的用户占比,例：填0.1代表10%
                map.put("A59", "");
                //	60	4G终端4G流量客户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G流量客户数/4G终端用户数,例：填0.1代表10%
                map.put("A60", "");
                //	61	4G流量客户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,本月使用4G网络产生4G流量的客户数
                map.put("A61", "");
                //	62	4G客户中4G流量低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G客户中，本月产生4G流量天数低于5天的用户占比,例：填0.1代表10%
                map.put("A62", "");
                //	63	4G客户中4G低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G低流量客户占比=本月4G客户中移动数据流量低于100M客户/本月4G客户（4G客户，指使用4G网络客户数）,例：填0.1代表10%
                map.put("A63", "");
                //	64	月一次使用用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用一次的用户/本周期使用用户数,例：填0.1代表10%
                map.put("A64", "");
                //	65	包月产品活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,包月付费且使用用户数/统计周期包月付费用户,例：填0.1代表10%
                map.put("A65", "");
                //	66	使用用户次月留存率	选填；,口径：,统计周期，次月持续使用行为的用户数/统计月的使用用户数,例：填0.1代表10%
                map.put("A66", "");
                //	67	家庭宽带帐户活跃用户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,流量大于0的用户
                map.put("A67", "");
                //	68	家庭宽带帐户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,家庭宽带活跃客户数与家庭宽带出账客户数的比值。家庭宽带活性客户比例=家庭宽带活跃客户数/家庭宽带出账客户数,例：填0.1代表10%
                map.put("A68", "");
                //	69	魔百和用户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,魔百和活跃客户数与魔百和客户数的比值,例：填0.1代表10%
                map.put("A69", "");
                //	70	低使用次数用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,只使用一次\且有流量产生的家庭宽带活跃用户占家庭旷代活跃用户比,例：填0.1代表10%
                map.put("A70", "");
                //	71	家庭宽带使用用户次月留存率	选填；,口径：,上个月活跃,本月继续活跃的家庭宽带使用用户占上月家庭宽带活跃用户比,例：填0.1代表10%
                map.put("A71", "");
                //	72	ARPU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的ARPU/运营活动上月的ARPU-1,例：填0.1代表10%
                map.put("A72", "");
                //	73	流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）运营成功的用户，运营活动次月的流量/运营活动上月的流量-1,例：填0.1代表10%
                map.put("A73", "");
                //	74	4G流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G流量/运营活动上月的4G流量-1,例：填0.1代表10%
                map.put("A74", "");
                //	75	DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的DOU/运营活动上月的DOU-1,例：填0.1代表10%
                map.put("A75", "");
                //	76	4G DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G DOU/运营活动上月的4G DOU-1,例：填0.1代表10%
                map.put("A76", "");
                //	77	MOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的MOU/运营活动上月的MOU-1,例：填0.1代表10%
                map.put("A77", "");
                //	78	通话时长提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的通话时长/运营活动上月的通话时长-1,例：填0.1代表10%
                map.put("A78", "");
                //	79	使用用户数	选填，口径：运营成功用户产生本业务使用行为的用户
                map.put("A79", "");
                //	80	家庭宽带出帐用户数	选填，口径：家庭宽带出帐使用用户数
                map.put("A80", "");
                list.add(map);
            } catch (Exception e) {
                Map<String, String> failMap = new HashMap();
                failMap.put("activity_id", activity.get("activity_id").toString());
                failMap.put("interface_name", "93005");
                failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                failMap.put("error_desc", e.getMessage().substring(0, 2000));
                e.printStackTrace();
                fileDataService.insertFailInterface(failMap);
                throw new Exception("接口异常");
            }
        }
        SqlUtil.getInsert("93005", list);
        ////System.out.println(sql);
        //
    }

    public void saveMarking93002(String activityEndDate, String summaryDate, String campaignedEndTime) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> resultmap = null;

        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93002(activityEndDate);
//        属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-90子活动效果评估指标
        for (Map<String, String> activity : activitys) {
            try {
                map = new HashMap<>();
                //2,统计时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
                map.put("A2", TimeUtil.getLongSeconds(new Date()));
                //3,省份,参考9.1省公司编码,必填(长度3位),省份与互联网编码参考9.1省公司编码
                map.put("A3", "280");
                //4,地市,参考9.2市公司编码,必填,当营销活动涉及多个地市时，以逗号分隔,长度： 每个地市长度3位或4位（未知地市为00000除外）
                map.put("A4", activity.getOrDefault("city_code", "028"));

                /**
                 * 5-13为营销活动相关信息
                 */
                //5,营销活动编号参考附录1统一编码规则中的编号规则，当涉及到一级策划省级执行时，营销活动编号需要与IOP-92001接口营销活动编码一致。当涉及省级策划一级执行时，营销活动编号需要与IOP-92004接口的营销活动编号一致,必填,前三位必须为省份编码
                String activity_id = activity.get("activity_id");
                map.put("A5", activity_id);
                //6,营销活动名称,必填
                map.put("A6", activity.get("activity_name"));
                //7,活动开始时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
                map.put("A7", activity.get("activity_starttime").replace("/", "").replace(" ", "").replace(":", ""));
                //8,活动结束时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
                map.put("A8", activity.get("activity_endtime").replace("/", "").replace(" ", "").replace(":", ""));
                //9,营销活动类型,1：入网类,必填，填写枚举值ID,2：终端类,3：流量类,4：数字化服务类,5：基础服务类,6：客户保有类,7：宽带类,8：融合套餐类,9：其它类
                map.put("A9", activity.getOrDefault("activity_type", "9"));
                //10,营销活动目的,1：新增客户类,必填，填写枚举值ID,2：存量保有类,3：价值提升类,4：离网预警类,9：其它类
                map.put("A10", activity.getOrDefault("activity_objective", "9"));
                //11,营销活动描述,对产品、服务等信息进行简要描述
                map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
                //12,PCC策略编码,（当采用了PCC能力时，相关内容必填）
                map.put("A12", activity.get("pcc_id"));
                //13,所属流程,1：一级策划省级执行,必填，填写枚举值ID,2：省级策划一级执行-互联网,3：省级策划省级执行,4：一级策划一点部署-一级电渠,5：一级策划一点部署-互联网,6：一级策划一点部署-省级播控平台,7：一级策划一点部署-咪咕,8：省级策划一级执行-电渠,9：省级策划一级执行-咪咕,10：省级策划一级执行-爱流量,98：一级策划一点部署,99：其他
                map.put("A13", "1");


                //37,PV,可为空，,口径：页面曝光量、接触量、浏览量。,电子渠道效果指标
                map.put("A37", "");
                //38,点击量,可为空，,口径：页面内容被点击的次数。,电子渠道效果指标
                map.put("A38", "");
                //39,UV(剔重),可为空，,口径：独立用户/独立访客。,电子渠道效果指标
                map.put("A39", "");
                //40,办理量,可为空，,口径：业务办理次数。,电子渠道效果指标
                map.put("A40", "");
                //41,用户号码明细,互联网特有，可为空
                map.put("A41", "");
//            //42,活动专题ID,当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A42", map.get("spetopic_id"));


                /**
                 * 43-90子活动效果评估指标
                 */
                Map<String, String> mapEffect = fileDataService.getSummaryEffectJT(activity_id, summaryDate, "ZHD");
                if (mapEffect==null)
                    return;
                //42,成功接触客户数,必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                map.put("A42", mapEffect.get("touch_num"));
                //43,接触成功率,必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
                map.put("A43", mapEffect.get("touhe_rate"));
                //44,响应率,必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
                map.put("A44", mapEffect.get("response_rate"));
                //45,营销成功用户数,必填；,口径：根据运营目的，成功办理或者成功使用的用户数
                map.put("A45", mapEffect.get("vic_num"));
                //46,营销成功率,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
                map.put("A46", mapEffect.get("vic_rate"));

//            47	投入产出比,NUMBER (20,6)	,必填且取值小于1；,口径：,统计周期（活动开始时间至活动结束时间）,运营活动成功用户产生的收入/运营活动投入的成本,例：填0.1代表10%
                map.put("A47", mapEffect.get("in_out_rate"));


                //48,PCC签约用户数,选填：,该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
                map.put("A48", "");
                //49,PCC策略生效用户数,选填：,该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
                map.put("A49", "");
                //50,PCC策略生效次数,选填：,该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
                map.put("A50", "");
                //	51	签约客户转化率	该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）,例：填0.1代表10%
                map.put("A51", "");
                //	52	套餐流量使用用户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐中产生流量的用户数量
                map.put("A52", "");
                //	53	套餐流量饱和度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐用户产生的套餐内总流量/套餐包含的流量资源总数,例：填0.1代表10%
                map.put("A53", "");
                //	54	套餐流量活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐流量使用用户数/套餐用户数,例：填0.1代表10%
                map.put("A54", "");
                //	55	套餐流量低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用流量的天数少于5天的用户占比,例：填0.1代表10%
                map.put("A55", "");
                //	56	4G客户次月留存率	选填；,口径：,次月4G用户/本月的4G用户,例：填0.1代表10%
                map.put("A56", "");
                //	57	低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用流量少于100M的用户占比,例：填0.1代表10%
                map.put("A57", "");
                //	58	语音使用用户	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐中产生语音的用户占比
                map.put("A58", "");
                //	59	套餐语音饱和度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐用户产生的套餐内总语音时长/套餐包含的语音资源总数,例：填0.1代表10%
                map.put("A59", "");
                //	60	套餐语音活跃用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐语音使用用户数/套餐用户数,例：填0.1代表10%
                map.put("A60", "");
                //	61	套餐语音低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音的天数少于5天的用户占比,例：填0.1代表10%
                map.put("A61", "");
                //	62	低通话量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音少于10分钟的用户占比,例：填0.1代表10%
                map.put("A62", "");
                //	63	4G终端4G流量客户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G流量客户数/4G终端用户数,例：填0.1代表10%
                map.put("A63", "");
                //	64	4G流量客户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,本月使用4G网络产生4G流量的客户数
                map.put("A64", "");
                //	65	4G客户中4G流量低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G客户中，本月产生4G流量天数低于5天的用户占比,例：填0.1代表10%
                map.put("A65", "");
                //	66	4G客户中4G低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G低流量客户占比=本月4G客户中移动数据流量低于100M客户/本月4G客户（4G客户，指使用4G网络客户数）,例：填0.1代表10%
                map.put("A66", "");
                //	67	月一次使用用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用一次的用户/本周期使用用户数,例：填0.1代表10%
                map.put("A67", "");
                //	68	包月产品活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,包月付费且使用用户数/统计周期包月付费用户,例：填0.1代表10%
                map.put("A68", "");
                //	69	使用用户次月留存率	选填；,口径：,统计周期，次月持续使用行为的用户数/统计月的使用用户数,例：填0.1代表10%
                map.put("A69", "");
                //	70	家庭宽带帐户活跃用户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,流量大于0的用户
                map.put("A70", "");
                //	71	家庭宽带帐户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,家庭宽带活跃客户数与家庭宽带出账客户数的比值。家庭宽带活性客户比例=家庭宽带活跃客户数/家庭宽带出账客户数,例：填0.1代表10%
                map.put("A71", "");
                //	72	魔百和用户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,魔百和活跃客户数与魔百和客户数的比值,例：填0.1代表10%
                map.put("A72", "");
                //	73	低使用次数用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,只使用一次\且有流量产生的家庭宽带活跃用户占家庭旷代活跃用户比,例：填0.1代表10%
                map.put("A73", "");
                //	74	家庭宽带使用用户次月留存率	选填；,口径：,上个月活跃,本月继续活跃的家庭宽带使用用户占上月家庭宽带活跃用户比,例：填0.1代表10%
                map.put("A74", "");
                //	75	ARPU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的ARPU/运营活动上月的ARPU-1,例：填0.1代表10%
                map.put("A75", "");
                //	76	流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）运营成功的用户，运营活动次月的流量/运营活动上月的流量-1,例：填0.1代表10%
                map.put("A76", "");
                //	77	4G流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G流量/运营活动上月的4G流量-1,例：填0.1代表10%
                map.put("A77", "");
                //	78	DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的DOU/运营活动上月的DOU-1,例：填0.1代表10%
                map.put("A78", "");
                //	79	4G DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G DOU/运营活动上月的4G DOU-1,例：填0.1代表10%
                map.put("A79", "");
                //	80	MOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的MOU/运营活动上月的MOU-1,例：填0.1代表10%
                map.put("A80", "");
                //	81	通话时长提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的通话时长/运营活动上月的通话时长-1,例：填0.1代表10%
                map.put("A81", "");
                //	82	使用用户数	选填，口径：运营成功用户产生本业务使用行为的用户
                map.put("A82", "");
                //	83	家庭宽带出帐用户数	选填，口径：家庭宽带出帐使用用户数
                map.put("A83", "");


                /**
                 * 14-36子活动相关信息
                 */
                final List<Map<String, String>> campaignedInfo = fileDataService.getCampaignedEndInfo(activity_id, campaignedEndTime);
                for (Map<String, String> campaignedmap : campaignedInfo) {
                    try {

                        resultmap = new HashMap<>();
                        //14,子活动编号,必填,参考附录1 统一编码规则中的营销子活动编号编码规则
                        resultmap.put("A14", campaignedmap.get("campaign_id"));
                        //15,子活动名称,必填
                        resultmap.put("A15", campaignedmap.get("campaign_name"));
                        //16,子活动开始时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
                        resultmap.put("A16", campaignedmap.get("campaign_starttime").replace("/", "").replace(":", "").replace(" ", ""));
                        //17,子活动结束时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
                        resultmap.put("A17", campaignedmap.get("campaign_endtime").replace("/", "").replace(":", "").replace(" ", ""));
                        Map<String, String> mapEffect1 = fileDataService.getSummaryEffectJT(campaignedmap.get("campaign_id"), summaryDate, "ZHD");
if(mapEffect1==null)
    return;
                        //18,目标客户群编号,必填
                        resultmap.put("A18", mapEffect1.get("customer_group_id"));
                        //19,目标客户群名称,必填
                        resultmap.put("A19", mapEffect1.get("customer_group_name"));
                        //20,目标客户群规模,可为空
                        resultmap.put("A20", mapEffect1.get("customer_num"));
                        //21,目标客户群描述,可为空
                        resultmap.put("A21", "");
                        //22,目标客户筛选标准,必填
                        resultmap.put("A22", campaignedmap.get("customer_filter_rule"));
                        List<Map<String, String>> offerMaps = fileDataService.getOfferBo(campaignedmap.get("campaign_id"));

                        Map<String, String> offerMap = offerMaps.get(0);
                        //23,产品编码,必填,比如0200100xxxx,02：一级分类,001：二级分类,00：三级分类,Xxxx：自定义产品编号,编码规则参考8.1产品编码规则
                        String proCode = offerMap.get("offer_code");
                        resultmap.put("A23", proCode);
                        //24,产品编码截取,截取“产品编码”前7位,必填,编码规则参考8.1产品编码规则,长度限制7位（省份截取前7位）
                        resultmap.put("A24", proCode.substring(0, 7));
                        //25,产品名称,必填
                        resultmap.put("A25", offerMap.get("offer_name"));
                        //26,产品分类,1：电信服务,必填，填写枚举值ID,2：客户服务,3：数字内容服务,4：实物,5：虚拟物品
                        resultmap.put("A26", offerMap.get("offer_type"));
                        //27,渠道编码,必填,比如,00108xxxx,001：一级分类,08：二级分类,Xxxx：自定义渠道编码,编码规则参考8.2渠道和运营位编码规则
                        resultmap.put("A27", campaignedmap.get("channel_id"));
                        //28,渠道编码一级分类,截取“渠道编码”前三位,必填,编码规则参考8.2渠道和运营位编码规则,长度3位,（省份截取前3位）
                        resultmap.put("A28", campaignedmap.get("channel_id").substring(0, 3));
                        //29,渠道编码二级分类,截取“渠道编码”第四、五位,必填,编码规则参考8.2渠道和运营位编码规则,长度2位,（省份截取第4、5位）
                        resultmap.put("A29", campaignedmap.get("channel_id").substring(3, 5));
                        //30,渠道名称,必填
                        resultmap.put("A30", campaignedmap.get("channel_name"));
                        //31,渠道类型,参考10附录3渠道类型编码,必填,渠道类型为偶数位
                        resultmap.put("A31", campaignedmap.get("channe_type"));
                        //32,渠道接触规则,可为空
                        resultmap.put("A32", campaignedmap.get("channel_rule"));
                        //33,时机识别,1：互联网使用事件,必填，填写枚举值ID,2：社会事件,3：位置行踪事件,4：业务办理事件,5：业务使用事件,6：周期业务事件,7：自助系统接触事件,8：PCC事件,9：其它事件,0：无事件
                        resultmap.put("A33", campaignedmap.get("time_id"));
                        //34,时机识别描述,可为空
                        resultmap.put("A34", campaignedmap.get("time_distindes"));
                        //35,客户质量情况,描述性信息,可为空
                        resultmap.put("A35", "");
                        //36,资源使用情况,描述性信息,可为空
                        resultmap.put("A36", "");
                        resultmap.putAll(map);
                        list.add(resultmap);
                    } catch (Exception e) {
                        Map<String, String> failMap = new HashMap();
                        failMap.put("activity_id", activity.get("activity_id").toString());
                        failMap.put("interface_name", "93002");
                        failMap.put("campaign_id", campaignedmap.get("campaign_id"));
                        failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                        failMap.put("error_desc", e.getMessage().substring(0, 2000));
                        e.printStackTrace();
                        fileDataService.insertFailInterface(failMap);
                        throw new Exception("接口异常");
                    }
                }
            } catch (Exception e1) {
                Map<String, String> failMap = new HashMap();
                failMap.put("activity_id", activity.get("activity_id").toString());
                failMap.put("interface_name", "93002");
                failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                failMap.put("error_desc", e1.getMessage());
                e1.printStackTrace();
                fileDataService.insertFailInterface(failMap);
                throw new Exception("接口异常");
            }
            SqlUtil.getInsert("93002", list);
        }
    }


    public void savebase93002(String activityEndDate, String summaryDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        Map<String, Object> resultmap = null;
        List<Map<String, Object>> activitys = fileDataService.getBaseInfo93002(activityEndDate);
//        属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-90子活动效果评估指标
        for (Map<String, Object> activity : activitys) {
            try {
                map = new HashMap<>();
                //2,统计时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
                map.put("A2", TimeUtil.getLongSeconds(new Date()));
                //3,省份,参考9.1省公司编码,必填(长度3位),省份与互联网编码参考9.1省公司编码
                map.put("A3", "280");
                //4,地市,参考9.2市公司编码,必填,当营销活动涉及多个地市时，以逗号分隔,长度： 每个地市长度3位或4位（未知地市为00000除外）
                map.put("A4", activity.getOrDefault("city_code", "028"));

                /**
                 * 5-13为营销活动相关信息
                 */
                //5,营销活动编号参考附录1统一编码规则中的编号规则，当涉及到一级策划省级执行时，营销活动编号需要与IOP-92001接口营销活动编码一致。当涉及省级策划一级执行时，营销活动编号需要与IOP-92004接口的营销活动编号一致,必填,前三位必须为省份编码
                String activity_id = activity.get("activity_id").toString();
                map.put("A5", "280" + activity_id.substring(1));
                //6,营销活动名称,必填
                map.put("A6", activity.get("activity_name"));
                //7,活动开始时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
                map.put("A7", activity.get("start_time").toString().replace("-", "") + "000000");
                //8,活动结束时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
                map.put("A8", activity.get("end_time").toString().replace("-", "") + "000000");
                //9,营销活动类型,1：入网类,必填，填写枚举值ID,2：终端类,3：流量类,4：数字化服务类,5：基础服务类,6：客户保有类,7：宽带类,8：融合套餐类,9：其它类
                map.put("A9", activity.getOrDefault("activity_type", "9"));
                //10,营销活动目的,1：新增客户类,必填，填写枚举值ID,2：存量保有类,3：价值提升类,4：离网预警类,9：其它类
                map.put("A10", activity.getOrDefault("activity_objective", "9"));
                //11,营销活动描述,对产品、服务等信息进行简要描述
                map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
                //12,PCC策略编码,（当采用了PCC能力时，相关内容必填）
                map.put("A12", activity.get("pcc_id"));
                //13,所属流程,1：一级策划省级执行,必填，填写枚举值ID,2：省级策划一级执行-互联网,3：省级策划省级执行,4：一级策划一点部署-一级电渠,5：一级策划一点部署-互联网,6：一级策划一点部署-省级播控平台,7：一级策划一点部署-咪咕,8：省级策划一级执行-电渠,9：省级策划一级执行-咪咕,10：省级策划一级执行-爱流量,98：一级策划一点部署,99：其他
                map.put("A13", activity.get("flow"));


                //37,PV,可为空，,口径：页面曝光量、接触量、浏览量。,电子渠道效果指标
                map.put("A37", "");
                //38,点击量,可为空，,口径：页面内容被点击的次数。,电子渠道效果指标
                map.put("A38", "");
                //39,UV(剔重),可为空，,口径：独立用户/独立访客。,电子渠道效果指标
                map.put("A39", "");
                //40,办理量,可为空，,口径：业务办理次数。,电子渠道效果指标
                map.put("A40", "");
                //41,用户号码明细,互联网特有，可为空
                map.put("A41", "");
                //            //42,活动专题ID,当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A42", map.get("spetopic_id"));


                /**
                 * 43-90子活动效果评估指标
                 */
                Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity_id, summaryDate);
                //42,成功接触客户数,必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                map.put("A42", mapEffect.get("touch_num"));
                //43,接触成功率,必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
                map.put("A43", mapEffect.get("touhe_rate"));
                //44,响应率,必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
                map.put("A44", mapEffect.get("response_rate"));
                //45,营销成功用户数,必填；,口径：根据运营目的，成功办理或者成功使用的用户数
                map.put("A45", mapEffect.get("vic_num"));
                //46,营销成功率,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
                map.put("A46", mapEffect.get("vic_rate"));

//            47	投入产出比,NUMBER (20,6)	,必填且取值小于1；,口径：,统计周期（活动开始时间至活动结束时间）,运营活动成功用户产生的收入/运营活动投入的成本,例：填0.1代表10%
                map.put("A47", mapEffect.get("vic_rate"));

                //48,PCC签约用户数,选填：,该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
                map.put("A48", "");
                //49,PCC策略生效用户数,选填：,该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
                map.put("A49", "");
                //50,PCC策略生效次数,选填：,该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
                map.put("A50", "");
                //	51	签约客户转化率	该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）,例：填0.1代表10%
                map.put("A51", "");
                //	52	套餐流量使用用户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐中产生流量的用户数量
                map.put("A52", "");
                //	53	套餐流量饱和度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐用户产生的套餐内总流量/套餐包含的流量资源总数,例：填0.1代表10%
                map.put("A53", "");
                //	54	套餐流量活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐流量使用用户数/套餐用户数,例：填0.1代表10%
                map.put("A54", "");
                //	55	套餐流量低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用流量的天数少于5天的用户占比,例：填0.1代表10%
                map.put("A55", "");
                //	56	4G客户次月留存率	选填；,口径：,次月4G用户/本月的4G用户,例：填0.1代表10%
                map.put("A56", "");
                //	57	低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用流量少于100M的用户占比,例：填0.1代表10%
                map.put("A57", "");
                //	58	语音使用用户	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐中产生语音的用户占比
                map.put("A58", "");
                //	59	套餐语音饱和度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐用户产生的套餐内总语音时长/套餐包含的语音资源总数,例：填0.1代表10%
                map.put("A59", "");
                //	60	套餐语音活跃用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,套餐语音使用用户数/套餐用户数,例：填0.1代表10%
                map.put("A60", "");
                //	61	套餐语音低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音的天数少于5天的用户占比,例：填0.1代表10%
                map.put("A61", "");
                //	62	低通话量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用语音少于10分钟的用户占比,例：填0.1代表10%
                map.put("A62", "");
                //	63	4G终端4G流量客户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G流量客户数/4G终端用户数,例：填0.1代表10%
                map.put("A63", "");
                //	64	4G流量客户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,本月使用4G网络产生4G流量的客户数
                map.put("A64", "");
                //	65	4G客户中4G流量低使用天数（5天）占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G客户中，本月产生4G流量天数低于5天的用户占比,例：填0.1代表10%
                map.put("A65", "");
                //	66	4G客户中4G低流量用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,4G低流量客户占比=本月4G客户中移动数据流量低于100M客户/本月4G客户（4G客户，指使用4G网络客户数）,例：填0.1代表10%
                map.put("A66", "");
                //	67	月一次使用用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,使用一次的用户/本周期使用用户数,例：填0.1代表10%
                map.put("A67", "");
                //	68	包月产品活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,包月付费且使用用户数/统计周期包月付费用户,例：填0.1代表10%
                map.put("A68", "");
                //	69	使用用户次月留存率	选填；,口径：,统计周期，次月持续使用行为的用户数/统计月的使用用户数,例：填0.1代表10%
                map.put("A69", "");
                //	70	家庭宽带帐户活跃用户数	选填；,口径：,统计周期（活动开始时间至活动结束时间）,流量大于0的用户
                map.put("A70", "");
                //	71	家庭宽带帐户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,家庭宽带活跃客户数与家庭宽带出账客户数的比值。家庭宽带活性客户比例=家庭宽带活跃客户数/家庭宽带出账客户数,例：填0.1代表10%
                map.put("A71", "");
                //	72	魔百和用户活跃度	选填；,口径：,统计周期（活动开始时间至活动结束时间）,魔百和活跃客户数与魔百和客户数的比值,例：填0.1代表10%
                map.put("A72", "");
                //	73	低使用次数用户占比	选填；,口径：,统计周期（活动开始时间至活动结束时间）,只使用一次\且有流量产生的家庭宽带活跃用户占家庭旷代活跃用户比,例：填0.1代表10%
                map.put("A73", "");
                //	74	家庭宽带使用用户次月留存率	选填；,口径：,上个月活跃,本月继续活跃的家庭宽带使用用户占上月家庭宽带活跃用户比,例：填0.1代表10%
                map.put("A74", "");
                //	75	ARPU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的ARPU/运营活动上月的ARPU-1,例：填0.1代表10%
                map.put("A75", "");
                //	76	流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）运营成功的用户，运营活动次月的流量/运营活动上月的流量-1,例：填0.1代表10%
                map.put("A76", "");
                //	77	4G流量提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G流量/运营活动上月的4G流量-1,例：填0.1代表10%
                map.put("A77", "");
                //	78	DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的DOU/运营活动上月的DOU-1,例：填0.1代表10%
                map.put("A78", "");
                //	79	4G DOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的4G DOU/运营活动上月的4G DOU-1,例：填0.1代表10%
                map.put("A79", "");
                //	80	MOU提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的MOU/运营活动上月的MOU-1,例：填0.1代表10%
                map.put("A80", "");
                //	81	通话时长提升率	选填；,口径：,统计周期（活动开始时间至活动结束时间）,运营成功的用户，运营活动次月的通话时长/运营活动上月的通话时长-1,例：填0.1代表10%
                map.put("A81", "");
                //	82	使用用户数	选填，口径：运营成功用户产生本业务使用行为的用户
                map.put("A82", "");
                //	83	家庭宽带出帐用户数	选填，口径：家庭宽带出帐使用用户数
                map.put("A83", "");


/**
 * 14-36子活动相关信息
 */
                //14,子活动编号,必填,参考附录1 统一编码规则中的营销子活动编号编码规则
                map.put("A14", "280" + activity.get("activity_id").toString().substring(1));
                //15,子活动名称,必填
                map.put("A15", activity.get("activity_name"));
                //16,子活动开始时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
                map.put("A16", activity.get("start_time").toString().replace("-", "") + "000000");
                //17,子活动结束时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
                map.put("A17", activity.get("end_time").toString().replace("-", "") + "000000");
                //18,目标客户群编号,必填
                map.put("A18", mapEffect.get("customer_group_id"));
                //19,目标客户群名称,必填
                map.put("A19", mapEffect.get("customer_group_name"));
                //20,目标客户群规模,可为空
                map.put("A20", mapEffect.get("customer_num"));
                //21,目标客户群描述,可为空
                map.put("A21", "");
                //22,目标客户筛选标准,必填
                map.put("A22", mapEffect.get("customer_filter_rule"));
                Map<String, String> channelInfo = fileDataService.getChannelInfo(activity_id);

                //23,产品编码,必填,比如0200100xxxx,02：一级分类,001：二级分类,00：三级分类,Xxxx：自定义产品编号,编码规则参考8.1产品编码规则
                String proCode = "0428000" + activity.get("activity_id").toString().substring(1);
                map.put("A23", proCode);
                //24,产品编码截取,截取“产品编码”前7位,必填,编码规则参考8.1产品编码规则,长度限制7位（省份截取前7位）
                map.put("A24", proCode.substring(0, 7));
                //25,产品名称,必填
                map.put("A25", activity.get("activity_name"));
                //26,产品分类,1：电信服务,必填，填写枚举值ID,2：客户服务,3：数字内容服务,4：实物,5：虚拟物品
                map.put("A26", "1");
                Map<String, String> positionInfo = fileDataService.getPositionInfo(activity_id);
                String CHANNELID = positionInfo.getOrDefault("CHANNELID", "28000" + channelInfo.get("channel_id"));
                String CHANNELNAME = positionInfo.getOrDefault("CHANNELNAME", channelInfo.get("channel_name"));
                String CHANNETYPE = positionInfo.getOrDefault("CHANNETYPE", channelInfo.get("channel_name"));
                //27,渠道编码,必填,比如,00108xxxx,001：一级分类,08：二级分类,Xxxx：自定义渠道编码,编码规则参考8.2渠道和运营位编码规则
                map.put("A27", CHANNELID);//024800
                //28,渠道编码一级分类,截取“渠道编码”前三位,必填,编码规则参考8.2渠道和运营位编码规则,长度3位,（省份截取前3位）
                map.put("A28", CHANNELID.substring(0, 3));
                //29,渠道编码二级分类,截取“渠道编码”第四、五位,必填,编码规则参考8.2渠道和运营位编码规则,长度2位,（省份截取第4、5位）
                map.put("A29", CHANNELID.substring(3, 5));
                //30,渠道名称,必填
                map.put("A30", CHANNELNAME);
                //31,渠道类型,参考10附录3渠道类型编码,必填,渠道类型为偶数位
                map.put("A31", CHANNETYPE);
                //32,渠道接触规则,可为空
                map.put("A32", "");
                //33,时机识别,1：互联网使用事件,必填，填写枚举值ID,2：社会事件,3：位置行踪事件,4：业务办理事件,5：业务使用事件,6：周期业务事件,7：自助系统接触事件,8：PCC事件,9：其它事件,0：无事件
                map.put("A33", "");
                //34,时机识别描述,可为空
                map.put("A34", "");
                //35,客户质量情况,描述性信息,可为空
                map.put("A35", "");
                //36,资源使用情况,描述性信息,可为空
                map.put("A36", "");
                list.add(resultmap);
            } catch (Exception e1) {
                Map<String, String> failMap = new HashMap();
                failMap.put("activity_id", activity.get("activity_id").toString());
                failMap.put("interface_name", "93002");
                failMap.put("syn_time", TimeUtil.getDateTimeFormat(new Date()));
                failMap.put("error_desc", e1.getMessage());
                e1.printStackTrace();
                fileDataService.insertFailInterface(failMap);
                throw new Exception("93002接口异常");
            }
        }
        SqlUtil.getInsertObj("93002", list);
    }
}
