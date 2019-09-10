package com.asiainfo.msooimonitor.task;

import com.alibaba.fastjson.JSON;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

    public void save93006() {

        List<Map> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getBaseInfo93006();
        System.out.println("activitys.size=" + activitys.size());
        int lineNum = 1;
        //1 行号
        for (Map<String, String> activity : activitys) {
            map = new HashMap<>();
            //2 统计时间 必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getLongSeconds(new Date()));
            //3 省份 必填，长度3位
            map.put("A3", "280");
            //4 地市 必填,长度： 3位或4位
            map.put("A4", activity.get("city_code"));
            //6 营销活动编号 必填,前三位必须为省份编码
            String activity_id = activity.get("activity_id");
            if (activity_id.startsWith("A"))
                activity_id = "280" + activity_id.substring(1);
            if (!activity_id.startsWith("280")) {
                activity_id = "280" + activity_id;
            }
            map.put("A6", activity_id);
            //7 营销活动名称 必填
            map.put("A7", activity.get("activity_name"));
            //8 子活动编号 可为空，参考附录1统一编码规则中的营销子活动编号编码规则；当营销活动涉及多子活动时，以逗号分隔
            map.put("A8", activity.getOrDefault("campaigned_id", activity_id));
            //9 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A9", activity.getOrDefault("campaigned_name", activity.get("activity_name")));
            //10 PV 可为空,口径：该用户打开网页次数,电子渠道效果指标
            map.put("A10", null);
            //11 用户点击量 可为空,口径：页面内容被该用户点击的次数,电子渠道效果指标
            map.put("A11", null);
            //12 用户办理量 可为空,口径：该用户业务办理次数,电子渠道效果指标
            map.put("A12", null);
            //16 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.putIfAbsent("A16", activity.get("spetopic_id"));
//            activity.put("final_obj_table_name", "OBJ_284576966837831");
//            final List<Map<String, String>> phones = fileDataService.getPhone93006(activity.get("final_obj_table_name"));
////            System.out.println("phones=="+phones);
            List<Map<String, String>> detaileffect = fileDataService.getDetailEffect(activity.get("activity_id"), TimeUtil.getDaySql(new Date()));
            for (Map<String, String> map1 : detaileffect) {
                mapresult = new HashMap<>(map);
                mapresult.put("A5", map1.get("phone_no"));
                list.add(mapresult);
                //13 是否成功接触 必填,口径：运营活动中，通过各触点，是否成功接触到该用户，如短信下发成功下发给该用户、外呼成功接通该用户、APP成功在该用户终端弹出等
                mapresult.putIfAbsent("A13", map1.get("is_touch"));
                //14 是否参与运营活动 必填,标识该用户是否参与运营活动
                mapresult.putIfAbsent("A14", map1.get("is_join_activity"));
                //15 是否营销 必填,口径：根据运营目的，该用户是否成功办理或者成功使用的运营产品
                mapresult.putIfAbsent("A15", map1.get("is_marketed"));
                //17 0x0D0A 行间分隔符－回车换行符
                //5 用户号码 必填,运营对象手机号码
            }
            System.out.println("list==" + list);
        }
        for (Map<String, String> mapinsert : list) {
            StringBuilder sb = new StringBuilder();
            sb.append("('");
            StringBuilder sbcolum = new StringBuilder();
            sbcolum.append("insert into iop_93006(");
            for (int a = 2; a < 17; a++) {
                String value = mapinsert.get("A" + a);
                if (value.equals("null") || value == null)
                    value = "";
                if (a < 16) {
                    sbcolum.append("A").append(a).append(",");
                    sb.append(value).append("','");
                } else {
                    sbcolum.append("A").append(a).append(") values ");
                    sb.append(value).append("'),");
                }
            }
            String sql = sbcolum.append(sb.substring(0, sb.length() - 1)).toString();
            System.out.println(sql);
            fileDataService.saveresultList(sql);
        }
    }


    public void save93001() {
        List<Map> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> resultmap = null;
        List<Map<String, String>> activitys = fileDataService.getBaseInfo93001();

        for (Map<String, String> activity : activitys) {
            map = new HashMap<>();
            //2	统计时间	必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getLongSeconds(new Date()));
            //3	省份	必填(长度3位),省份与互联网编码参考9.1省公司编码
            map.put("A3", "280");
            //4	地市	必填,长度： 每个地市长度3位或4位（未知地市为00000除外）
            map.put("A4", activity.getOrDefault("city_code", "028"));
            //5	营销活动编号	必填,前三位必须为省份编码
            String activity_id = activity.get("activity_id");
            if (activity_id.startsWith("A"))
                activity_id = "280" + activity_id.substring(1);
            if (!activity_id.startsWith("280")) {
                activity_id = "280" + activity_id;
            }
            map.put("A5", activity_id);
            //6	营销活动名称	必填
            map.put("A6", activity.get("activity_name"));
            //7	活动开始时间	必填,长度14位,为数据生成时间
            map.put("A7", activity.get("activity_starttime").replace("-", "").replace(" ", "").replace(":", ""));
            //8	活动结束时间	必填,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
            map.put("A8", activity.get("activity_endtime").replace("-", "").replace(" ", "").replace(":", ""));
            //9	营销活动类型	必填，填写枚举值ID
            map.put("A9", activity.get("activity_type"));
            //10	营销活动目的	必填，填写枚举值ID
            map.put("A10", activity.get("activity_objective"));
            //11	营销活动描述	对产品、服务等信息进行简要描述
            map.put("A11", activity.get("activity_describe"));
            //12	PCC策略编码	（当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
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
            //42	活动专题ID	当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A42", activity.get("spetopic_id"));
            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity_id);
            System.out.println("mapEffect=" + mapEffect);
            //43	成功接触客户数	日指标，必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
            map.put("A43", mapEffect.get("touhe_rate"));
            //44	接触成功率	日指标，必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
            map.put("A44", mapEffect.get("touhe_rate"));
            //45	响应率	日指标，必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,		（注意需填小数，而不是百分数）
            map.put("A45", mapEffect.get("response_rate"));
            //46	营销成功用户数	日指标，必填；,口径：根据运营目的，成功办理或者成功使用的用户数
            map.put("A46", mapEffect.get("vic_num"));
            //47	营销成功率	日指标,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,		（注意需填小数，而不是百分数）
            map.put("A47", mapEffect.get("vic_rate"));
            //48	使用用户数	日指标，必填：,运营成功用户产生本业务使用行为的用户
            map.put("A48", mapEffect.get("touch_num"));
            //49	4G终端4G流量客户占比	日指标，必填且取值小于1；,口径：4G流量客户数/4G终端用户数,例：填0.1代表10%,		（注意需填小数，而不是百分数）
            map.put("A49", mapEffect.get("terminal_flow_rate"));
            //50	4G流量客户数	日指标，选填，口径：统计周期内，使用4G网络产生4G流量的客户数
            map.put("A50", "");


            List<Map<String, String>> campaignedList = fileDataService.getCampaignedInfo(activity_id);
            for (Map<String, String> campaignedmap : campaignedList) {
                resultmap = new HashMap<>();
                //14	子活动编号	必填,参考附录1统一编码规则中的营销子活动编号编码规则,所属流程为2、8、9、10时，前3位编号为省份编码
                resultmap.put("A14", campaignedmap.get("campaign_id"));
                //15	子活动名称	必填
                resultmap.put("A15", campaignedmap.get("campaign_name"));
                //16	子活动开始时间	必填,长度14位,为数据生成时间
                resultmap.put("A16", campaignedmap.get("campaign_starttime").replace("-", "").replace(":", "").replace(" ", ""));
                //17	子活动结束时间	必填,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
                resultmap.put("A17", campaignedmap.get("campaign_starttime").replace("-", "").replace(":", "").replace(" ", ""));
                //18	目标客户群编号	必填
                resultmap.put("A18", campaignedmap.get("sgmt_id"));
                //19	目标客户群名称	必填
                resultmap.put("A19", campaignedmap.get("sgmt_name"));
                //20	目标客户群规模	可为空
                resultmap.put("A20", campaignedmap.get("sgmt_num"));
                //21	目标客户群描述	可为空
                resultmap.put("A21", campaignedmap.get("sgmt_desc"));
                //22	目标客户筛选标准	必填
                resultmap.put("A22", campaignedmap.get("sgmt_sift_rule"));

                List<Map<String, String>> offerMaps = fileDataService.getOfferBo(campaignedmap.get("campaign_id"));
                Map<String, String> offerMap = new HashMap<>();
                if (offerMaps.size() > 0) {
                    offerMap = offerMaps.get(0);
                }
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
                resultmap.put("A27", campaignedmap.get("channel_id"));
                //28	渠道编码一级分类	必填,长度3位,		（省份截取前3位）
                resultmap.put("A28", campaignedmap.get("channel_id").substring(0, 3));
                //29	渠道编码二级分类	必填,长度2位,		（省份截取第4、5位）
                resultmap.put("A29", campaignedmap.get("channel_id").substring(3, 5));
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
            }


        }
        System.out.println(JSON.toJSONString(list));
        for (Map<String, String> mapinsert : list) {
            StringBuilder sb = new StringBuilder();
            sb.append("('");
            StringBuilder sbcolum = new StringBuilder();
            sbcolum.append("insert into iop_93001(");
            for (int a = 2; a < 51; a++) {
                String value = mapinsert.getOrDefault("A" + a, "");
                System.out.println("a" + a + "-->" + value);

                if (value.equals("null") || value == null)
                    value = "";
                if (a < 50) {
                    sbcolum.append("A").append(a).append(",");
                    sb.append(value).append("','");
                } else {
                    sbcolum.append("A").append(a).append(") values ");
                    sb.append(value).append("'),");
                }
            }
            String sql = sbcolum.append(sb.substring(0, sb.length() - 1)).toString();
            System.out.println(sql);
            fileDataService.saveresultList(sql);
        }
    }


    public void save93005() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getBaseInfo93005();

        for (Map<String, String> activity : activitys) {
            //1 行号
            //2 统计时间 必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getLongSeconds(new Date()));
            //3 省份 必填(长度3位),省份与互联网编码参考9.1省公司编码
            map.put("A3", "280");
            //4 地市 必填 ,长度： 每个地市长度3位或4位（未知地市为00000除外）
            map.put("A4", activity.getOrDefault("city_code", "028"));
            //5 营销活动编号 必填,前三位必须为省份编码
            String activityId = activity.get("activity_id");
            if (activityId.startsWith("A"))
                activityId = "280" + activityId.substring(1);
            map.put("A5", activityId);
            //6 营销活动名称 必填
            map.put("A6", activity.get("activity_name"));
            //7 活动开始时间 必填,长度14位
            map.put("A7", activity.get("activity_starttime").replace("-", "").replace(":", "").replace(" ", ""));
            //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
            map.put("A8", activity.get("activity_endtime").replace("-", ""));
            //9 营销活动类型 必填，填写枚举值ID
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10 营销活动目的 必填，填写枚举值ID
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11 营销活动描述 对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
            //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13 所属流程 必填，填写枚举值ID
            map.put("A13", "1");//待定


//18 目标客户群编号 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A18", activity.get("cust_group_id"));
            //19 目标客户群名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A19", activity.get("cust_group_name"));
            //20 目标客户群规模 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A20", activity.get("cust_group_count"));
            //21 目标客户群描述 可为空
            map.put("A21", activity.get("cust_group_createrule_desc"));
            //22 目标客户筛选标准 必填
            map.put("A22", activity.get("sgmt_sift_rule"));
            List<Map<String, String>> offerMaps = fileDataService.getOfferBo(activity.get("campaign_id"));
            Map<String, String> offerMap = new HashMap<>();
            if (offerMaps.size() > 0) {
                offerMap = offerMaps.get(0);
            }
            //23 产品编码 可为空
            //比如0200100xxxx
            //02：一级分类
            //001：二级分类
            //00：三级分类
            //Xxxx：自定义产品编号
            //编码规则参考8.1产品编码规则
            //当营销活动涉及多子活动时，以逗号分隔
            map.put("A23", offerMap.get("offer_code"));
            //24 产品名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A24", offerMap.get("offer_name"));
            //25 产品分类 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A25", offerMap.get("offer_type"));
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
            map.put("A14", activity.get("campaign_id"));
            //15 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A15", activity.get("campaign_name"));
            //16 子活动开始时间 可为空,格式：YYYYMMDDHH24MISS,示例：20170213161140
            map.put("A16", activity.get("campaign_starttime"));
            //17 子活动结束时间 可为空,格式：YYYYMMDDHH24MISS,子活动结束时间不早于子活动开始时间,示例：20170213161140
            map.put("A17", activity.get("campaign_starttime"));


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
            //39 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A39", activity.get("spetopic_id"));


            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity.get("activity_id"));

            //40 成功接触客户数 必填
            //口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
            map.put("A40", mapEffect.get("touch_num"));
            //41 接触成功率 必填且取值小于1；
            //口径：成功接触客户数/活动总客户数
            //例：填0.1代表10%（注意需填小数，而不是百分数）
            map.put("A41", mapEffect.get("touhe_rate"));
            //42 响应率 必填且取值小于1；
            //口径：运营活动参与用户/成功接触用户
            //例：填0.1代表10%
            //  （注意需填小数，而不是百分数）
            map.put("A42", mapEffect.get("response_rate"));
            //43 营销成功用户数 必填；
            //口径：根据运营目的，成功办理或者成功使用的用户数
            map.put("A43", mapEffect.get("vic_num"));
            //44 营销成功率 必填且取值小于1；
            //口径：营销成功用户数/成功接触客户数
            //例：填0.1代表10%
            //  （注意需填小数，而不是百分数）
            map.put("A44", mapEffect.get("vic_rate"));
            //45 使用用户数 必填：
            //运营成功用户产生本业务使用行为的用户
            map.put("A45", mapEffect.get("touch_num"));
            //46 活动总用户数 必填：
            map.put("A46", mapEffect.get("customer_num"));
            //47 PCC签约用户数 选填：
            //该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
            map.put("A47", mapEffect.get(""));
            //48 PCC策略生效用户数 选填：
            //该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
            map.put("A48", mapEffect.get(""));
            //49 PCC策略生效次数 选填：
            //该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
            map.put("A49", "");
            //50 签约客户转化率 选填：
            //该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）
            //例：填0.1代表10%
            map.put("A50", "");
            //51 累计办理宽带用户数 选填：活动结束次日累计办理宽带用户数
            map.put("A51", "");
            //52 用户宽带融合提升率 选填：
            //口径：
            //统计周期（活动开始时间至活动结束时间）活动结束次日累计办理宽带用户数/活动开始前一日累计办理宽带用户数-1
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A52", "");
            //53 活跃用户数 选填：
            //活动结束次日客户端累计用户数
            map.put("A53", "");
            //54 活跃用户增长率 选填：口径：
            //统计周期（活动开始时间至活动结束时间）；营销成功用户，活动结束次日客户端累计用户数/活动开始前一日客户端累计用户数-1
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A54", "");
            //55 新增流量用户数 选填：
            //活动结束次日客户端累计新增流量用户数
            map.put("A55", "");
            //56 新入网流量用户占比 选填：
            //口径：
            //统计周期（活动开始时间至活动结束时间）新增流量用户数/新增用户数
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A56", "");
            //57 新增4G用户数 选填：
            //活动结束次日客户端累计新增4G用户数
            map.put("A57", "");
            //58 新入网4g用户占比 选填：
            //口径：
            //统计周期（活动开始时间至活动结束时间）新增4g用户数/新增用户数
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A58", "");
            //59 大流量套餐办理量 选填：
            //大流量套餐指流量大于10G套餐。
            map.put("A59", "");
            //60 大流量套餐办理率（融合套餐） 选填：小于1
            //口径：大流量套餐办理量/套餐总办理量
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A60", "");
            //61 基础套餐内语音实际使用量 选填
            map.put("A61", "");
            //62 语音使用用户 选填
            //套餐中产生语音的用户占比
            map.put("A62", "");
            //63 套餐语音饱和度 选填口径：
            //统计周期（活动开始时间至活动结束时间）用户基础套餐内语音实际使用量/基础套餐内语音包含量。
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A63", "");
            //64 套餐语音活跃用户占比 选填
            //套餐语音使用用户数/套餐用户数
            map.put("A64", "");
            //65 套餐语音低使用天数（5天）占比 选填
            //使用语音的天数少于5天的用户占比
            map.put("A65", "");
            //66 低通话量用户数 选填：
            //低通话量用户，指月主叫通话时长少于10分钟的用户。
            map.put("A66", "");
            //67 低通话量用户占比 选填：小于1
            //口径：
            //统计周期（活动开始时间至活动结束时间）营销成功用户，低通话量用户数/营销成功用户数
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A67", "");
            //68 基础套餐内流量实际使用量 选填：
            map.put("A68", "");
            //69 套餐流量饱和度 选填：
            //口径：
            //统计周期（活动开始时间至活动结束时间）用户基础套餐内流量实际使用量/基础套餐内流量包含量
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A69", "");
            //70 套餐流量活跃度 选填
            //套餐流量使用用户数/套餐用户数
            map.put("A70", "");
            //71 套餐流量低使用天数（5天）占比 选填
            //使用流量的天数少于5天的用户占比
            map.put("A71", "");
            //72 低流量用户数 选填：
            //低流量用户指月GPRS使用流量(DOU)少于100M的用户。
            map.put("A72", "");
            //73 低流量用户占比 选填：小于1
            //口径：
            //统计周期（活动开始时间至活动结束时间）营销成功用户，低流量客户数/营销成功用户数
            //例：填0.1代表10%
            //  （注意不要填百分数）
            map.put("A73", "");
            //74 4G终端4G流量客户占比 选填：
            //4G流量客户数/4G终端用户数
            map.put("A74", "");
            //75 4G流量客户数 选填：
            //当月使用4G网络产生4G流量的客户数
            map.put("A75", "");
            //76 4G客户中4G流量低使用天数（5天）占比 选填：
            //4G客户中， 当月产生4G流量天数低于5天的用户占比
            map.put("A76", "");
            //77 4G客户中4G低流量用户占比 选填：
            //4G低流量客户占比=当月4G客户中移动数据流量低于100M客户/当月4G客户（4G客户，指使用4G网络客户数）
            map.put("A77", "");
            //78 月一次使用用户占比 选填：
            //统计周期内使用一次的用户/本周期使用用户数
            map.put("A78", "");
            //79 包月产品活跃度 选填：
            //统计周期内包月付费且使用用户数/统计周期内包月付费用户
            map.put("A79", "");
            //80 宽带账户活跃用户数 选填：
            //口径：
            //统计周期（活动开始时间至活动结束时间）
            //家庭宽带流量使用大于0的用户
            map.put("A80", "");
            //81 宽带账户活跃度 选填：
            //口径：
            //统计周期（活动开始时间至活动结束时间）
            //家庭宽带活跃用户数/家庭宽带办理用户数
            map.put("A81", "");
            //82 低使用次数用户占比 选填：
            //统计周期内,只使用一次\且有流量产生的家庭宽带活跃用户占家庭宽带活跃用户比
            map.put("A82", "");
            //83 魔百和用户活跃度 选填：
            //统计周期末，魔百和活跃客户数与魔百和客户数的比值
            map.put("A83", "");
            //84 户均收入 选填：
            //统计周期（活动开始时间至活动结束时间），营销成功用户总收入/营销成功用户数。
            map.put("A84", "");
            //85 总收入 选填：
            //统计周期（活动开始时间至活动结束时间），营销成功用户总收入。
            map.put("A85", "");
            //86 投入产出比 选填：
            //营销成功用户总收入/运营活动投入的成本
            map.put("A86", "");
            //87 0x0D0A 行间分隔符－回车换行符
            list.add(map);

        }


        for (Map<String, String> mapinsert : list) {
            StringBuilder sb = new StringBuilder();
            sb.append("('");
            StringBuilder sbcolum = new StringBuilder();
            sbcolum.append("insert into iop_93005(");
            for (int a = 2; a < 87; a++) {
                String value = mapinsert.getOrDefault("A" + a, "");
                System.out.println("a" + a + "-->" + value);

                if (value.equals("null") || value == null)
                    value = "";
                if (a < 86) {
                    sbcolum.append("A").append(a).append(",");
                    sb.append(value).append("','");
                } else {
                    sbcolum.append("A").append(a).append(") values ");
                    sb.append(value).append("'),");
                }
            }
            String sql = sbcolum.append(sb.substring(0, sb.length() - 1)).toString();
            System.out.println(sql);
            fileDataService.saveresultList(sql);
        }
    }

    public void test93002() {
        List<Map> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> resultmap = null;
        List<Map<String, String>> activitys = fileDataService.getBaseInfo93002();
        System.out.println("activitys==" + activitys.size());
        for (Map<String, String> activity : activitys) {
            map = new HashMap<>();
            //2,统计时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getLongSeconds(new Date()));
            //3,省份,参考9.1省公司编码,必填(长度3位),省份与互联网编码参考9.1省公司编码
            map.put("A3", "280");
            //4,地市,参考9.2市公司编码,必填,当营销活动涉及多个地市时，以逗号分隔,长度： 每个地市长度3位或4位（未知地市为00000除外）
            map.put("A4", activity.getOrDefault("city_code", "028"));
            //5,营销活动编号参考附录1统一编码规则中的编号规则，当涉及到一级策划省级执行时，营销活动编号需要与IOP-92001接口营销活动编码一致。当涉及省级策划一级执行时，营销活动编号需要与IOP-92004接口的营销活动编号一致,必填,前三位必须为省份编码
            String activity_id = activity.get("activity_id");
            if (activity_id.startsWith("A"))
                activity_id = "280" + activity_id.substring(1);
            if (!activity_id.startsWith("280")) {
                activity_id = "280" + activity_id;
            }
            map.put("A5", activity_id);
            //6,营销活动名称,必填
            map.put("A6", activity.get("activity_name"));
            //7,活动开始时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
            map.put("A7", activity.get("activity_starttime").replace("-", "").replace(" ", "").replace(":", ""));
            //8,活动结束时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
            map.put("A8", activity.get("activity_endtime").replace("-", "").replace(" ", "").replace(":", ""));
            //9,营销活动类型,1：入网类,必填，填写枚举值ID,2：终端类,3：流量类,4：数字化服务类,5：基础服务类,6：客户保有类,7：宽带类,8：融合套餐类,9：其它类
            map.put("A9", activity.get("activity_type"));
            //10,营销活动目的,1：新增客户类,必填，填写枚举值ID,2：存量保有类,3：价值提升类,4：离网预警类,9：其它类
            map.put("A10", activity.get("activity_objective"));
            //11,营销活动描述,对产品、服务等信息进行简要描述
            map.put("A11", activity.get("activity_describe"));
            //12,PCC策略编码,（当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13,所属流程,1：一级策划省级执行,必填，填写枚举值ID,2：省级策划一级执行-互联网,3：省级策划省级执行,4：一级策划一点部署-一级电渠,5：一级策划一点部署-互联网,6：一级策划一点部署-省级播控平台,7：一级策划一点部署-咪咕,8：省级策划一级执行-电渠,9：省级策划一级执行-咪咕,10：省级策划一级执行-爱流量,98：一级策划一点部署,99：其他
            map.put("A13", "");


            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity_id);


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
            //42,活动专题ID,当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A42", map.getOrDefault("spetopic_id", ""));
            //43,成功接触客户数,必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
            map.put("A43", mapEffect.get("touch_num"));
            //44,接触成功率,必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
            map.put("A44", mapEffect.get("touhe_rate"));
            //45,响应率,必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A45", mapEffect.get("response_rate"));
            //46,营销成功用户数,必填；,口径：根据运营目的，成功办理或者成功使用的用户数
            map.put("A46", mapEffect.get("vic_num"));
            //47,营销成功率,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A47", mapEffect.get("vic_rate"));
            //48,使用用户数,必填：,运营成功用户产生本业务使用行为的用户
            map.put("A48", mapEffect.get("touch_num"));
            //49,活动总用户数,必填：
            map.put("A49", mapEffect.get("customer_num"));
            //50,PCC签约用户数,选填：,该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
            map.put("A50", "");
            //51,PCC策略生效用户数,选填：,该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
            map.put("A51", "");
            //52,PCC策略生效次数,选填：,该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
            map.put("A52", "");
            //53,签约客户转化率,选填：,该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）,例：填0.1代表10%
            map.put("A53", "");
            //54,累计办理宽带用户数,子活动结束次日累计办理宽带用户数,选填：
            map.put("A54", "");
            //55,用户宽带融合提升率,统计周期（活动开始时间至活动结束时间）,选填：,口径：活动结束次日累计办理宽带用户数/活动开始前一日累计办理宽带用户数-1,例：填0.1代表10%,（注意不要填百分数）
            map.put("A55", "");
            //56,活跃用户数,子活动结束次日客户端累计用户数,选填：
            map.put("A56", "");
            //57,活跃用户增长率,统计周期（活动开始时间至活动结束时间）；,选填：,新业务包括和留言、和彩云、咪咕直播、MM移动商城、和多号、咪咕音乐、灵犀；和家亲、和工作、咪咕圈圈、咪咕视频、咪咕游戏（数据重传）、咪咕影院（视频）、139邮箱、咪咕阅读；和彩印、12580、彩铃（咪咕音乐）、咪咕善跑（数据重传）、视频彩铃（音乐）、无线城市（和生活）；和家固话、智能组网、SIM盾、和地图；和包、和飞信、和目云、魔百和；和通讯录。,口径：营销成功用户，活动结束次日客户端累计用户数/活动开始前一日客户端累计用户数-1,例：填0.1代表10%,（注意不要填百分数）
            map.put("A57", "");
            //58,新增流量用户数,子活动结束次日客户端累计新增流量用户数,选填：
            map.put("A58", "");
            //59,新入网流量用户占比,统计周期（活动开始时间至活动结束时间）,选填：,新入网用户指新办理了移动号卡用户。,口径：新增流量用户数/新增用户数,例：填0.1代表10%,（注意不要填百分数）
            map.put("A59", "");
            //60,新增4G用户数,子活动结束次日客户端累计新增4G用户数,选填：
            map.put("A60", "");
            //61,新入网4g用户占比,统计周期（活动开始时间至活动结束时间）,选填：口径：新增4g用户数/新增用户数,新入网用户指新办理了移动号卡用户。,例：填0.1代表10%,（注意不要填百分数）
            map.put("A61", "");
            //62,大流量套餐办理量,大流量套餐指流量大于10G套餐。,选填：
            map.put("A62", "");
            //63,大流量套餐办理率（融合套餐）,选填：小于1,口径：大流量套餐办理量/套餐总办理量,例：填0.1代表10%,（注意不要填百分数）
            map.put("A63", "");
            //65,基础套餐内语音实际使用量,单位：分钟,选填：
            map.put("A64", "");
            //66,语音使用用户,套餐中产生语音的用户占比,选填
            map.put("A65", "");
            //,套餐语音饱和度,统计周期（活动开始时间至活动结束时间）,选填：口径：用户基础套餐内语音实际使用量/基础套餐内语音包含量。,67 例：填0.1代表10%,（注意不要填百分数）
            map.put("A66", "");
            //68,套餐语音活跃用户占比,选填：,套餐语音使用用户数/套餐用户数
            map.put("A67", "");
            //69,套餐语音低使用天数（5天）占比,选填：,使用语音的天数少于5天的用户占比
            map.put("A68", "");
            //,低通话量用户数,低通话量用户，指月主叫通话时长少于10分钟的用户。,选填：
            map.put("A69", "");
            //70,低通话量用户占比,统计周期（活动开始时间至活动结束时间）,选填：小于1
            map.put("A70", "");
            //71 口径：营销成功用户，低通话量用户数/营销成功用户数,例：填0.1代表10%,（注意不要填百分数）,基础套餐内流量实际使用量,单位：M,选填：
            map.put("A71", "");
            //72,套餐流量饱和度,统计周期（活动开始时间至活动结束时间）,选填：口径：用户基础套餐内流量实际使用量/基础套餐内流量包含量
            map.put("A72", "");
            //73 例：填0.1代表10%,（注意不要填百分数）
            map.put("A73", "");
            //74,套餐流量活跃度,选填：,套餐流量使用用户数/套餐用户数
            map.put("A74", "");
            //75,套餐流量低使用天数（5天）占比,选填：,使用流量的天数少于5天的用户占比
            map.put("A75", "");
            //76,低流量用户数,低流量用户指月GPRS使用流量(DOU)少于100M的用户。,选填：,低流量用户占比,统计周期（活动开始时间至活动结束时间）,选填：小于1
            map.put("A76", "");
            //77 口径：营销成功用户，低流量客户数/营销成功用户数,例：填0.1代表10%,（注意不要填百分数）
            map.put("A77", "");
            //77 口径：营销成功用户，低流量客户数/营销成功用户数,例：填0.1代表10%,（注意不要填百分数）
            map.put("A78", "");
            //79,4G流量客户数,选填：,当月使用4G网络产生4G流量的客户数
            map.put("A79", "");
            //80,4G客户中4G流量低使用天数（5天）占比,选填：,4G客户中， 当月产生4G流量天数低于5天的用户占比
            map.put("A80", "");
            //81,4G客户中4G低流量用户占比,选填：,4G低流量客户占比=当月4G客户中移动数据流量低于100M客户/当月4G客户（4G客户，指使用4G网络客户数）
            map.put("A81", "");
            //82,月一次使用用户占比,选填：,统计周期内使用一次的用户/本周期使用用户数
            map.put("A82", "");
            //83,包月产品活跃度,选填：,统计周期内包月付费且使用用户数/统计周期内包月付费用户
            map.put("A83", "");
            //84,宽带账户活跃用户数,统计周期（活动开始时间至活动结束时间）,选填：,口径：,家庭宽带流量使用大于0的用户宽带账户活跃度,统计周期（活动开始时间至活动结束时间）,
            map.put("A84", "");
            //85 选填：,口径：,家庭宽带活跃用户数/家庭宽带办理用户数,
            map.put("A85", "");
            //86,低使用次数用户占比,选填：,统计周期内,只使用一次\且有流量产生的家庭宽带活跃用户占家庭宽带活跃用户比
            map.put("A86", "");
            //87,魔百和用户活跃度,选填：,统计周期末，魔百和活跃客户数与魔百和客户数的比值
            map.put("A87", "");
            //88,户均收入,单位，元,选填：,统计周期（活动开始时间至活动结束时间），营销成功用户总收入/营销成功用户数。
            map.put("A88", "");
            //89,总收入,单位，元,选填：,统计周期（活动开始时间至活动结束时间），营销成功用户总收入。
            map.put("A89", "");
            //90,投入产出比,选填：,营销成功用户总收入/运营活动投入的成本
            map.put("A90", "");
            //91,0x0D0A,行间分隔符－回车换行符


            final List<Map<String, String>> campaignedInfo = fileDataService.getCampaignedInfo(activity_id);
            for (Map<String, String> campaignedmap : campaignedInfo) {
                resultmap = new HashMap<>();
                //14,子活动编号,必填,参考附录1 统一编码规则中的营销子活动编号编码规则
                resultmap.put("A14", campaignedmap.get("campaign_id"));
                //15,子活动名称,必填
                resultmap.put("A15", campaignedmap.get("campaign_name"));
                //16,子活动开始时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间
                resultmap.put("A16", campaignedmap.get("campaign_starttime").replace("-", "").replace(":", "").replace(" ", ""));
                //17,子活动结束时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
                resultmap.put("A17", campaignedmap.get("campaign_endtime").replace("-", "").replace(":", "").replace(" ", ""));
                //18,目标客户群编号,必填
                resultmap.put("A18", campaignedmap.get("sgmt_id"));
                //19,目标客户群名称,必填
                resultmap.put("A19", campaignedmap.get("sgmt_id"));
                //20,目标客户群规模,可为空
                resultmap.put("A20", campaignedmap.get("sgmt_num"));
                //21,目标客户群描述,可为空
                resultmap.put("A21", campaignedmap.get("sgmt_desc"));
                //22,目标客户筛选标准,必填
                resultmap.put("A22", campaignedmap.get("sgmt_sift_rule"));
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
            }
        }
        System.out.println(JSON.toJSONString(list));
        for (Map<String, String> mapinsert : list) {
            StringBuilder sb = new StringBuilder();
            sb.append("('");
            StringBuilder sbcolum = new StringBuilder();
            sbcolum.append("insert into iop_93002(");
            for (int a = 2; a < 91; a++) {
                if (a == 64)
                    continue;
                String value = mapinsert.getOrDefault("A" + a, "");
                System.out.println("a" + a + "-->" + value);

                if (value.equals("null") || value == null)
                    value = "";
                if (a < 90) {
                    sbcolum.append("A").append(a).append(",");
                    sb.append(value).append("','");
                } else {
                    sbcolum.append("A").append(a).append(") values ");
                    sb.append(value).append("'),");
                }
            }
            String sql = sbcolum.append(sb.substring(0, sb.length() - 1)).toString();
            System.out.println(sql);
            fileDataService.saveresultList(sql);
        }
    }
}
