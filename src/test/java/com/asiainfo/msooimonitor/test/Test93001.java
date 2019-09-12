package com.asiainfo.msooimonitor.test;

import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.utils.SqlUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yx
 * @date 2019/9/6  21:03
 * Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Test93001 {
    @Resource
    FileDataService fileDataService;

    @Test
    public void testsaveMarking93001() throws Exception {
        String date = "2019/12/31";
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
//            //42	活动专题ID	当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A42", activity.get("spetopic_id"));


                //子活动效果评估指标
                Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity_id);
                //42	成功接触客户数	日指标，必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                map.put("A42", mapEffect.get("touch_num"));
                //43	接触成功率	日指标，必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
                map.put("A43", mapEffect.get("touhe_rate"));
                //44	响应率	日指标，必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,		（注意需填小数，而不是百分数）
                map.put("A44", mapEffect.get("response_rate"));
                //45	营销成功用户数	日指标，必填；,口径：根据运营目的，成功办理或者成功使用的用户数
                map.put("A45", mapEffect.get("vic_num"));
                //46	营销成功率	NUMBER (20,6)日指标,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%
                map.put("A46", mapEffect.get("vic_rate"));
                //47	4G终端4G流量客户占比	日指标，必填且取值小于1；,口径：4G流量客户数/4G终端用户数,例：填0.1代表10%,		（注意需填小数，而不是百分数）
                map.put("A47", mapEffect.get("terminal_flow_rate"));
                //48 4G流量客户数,日指标，选填，口径：统计周期内，使用4G网络产生4G流量的客户数
                map.put("A48", "");

                //子活动相关信息
                List<Map<String, String>> campaignedList = fileDataService.getCampaignedInfo(activity_id);
                for (Map<String, String> campaignedmap : campaignedList) {
                    resultmap = new HashMap<>();
                    //14	子活动编号	必填,参考附录1统一编码规则中的营销子活动编号编码规则,所属流程为2、8、9、10时，前3位编号为省份编码
                    resultmap.put("A14", campaignedmap.get("campaign_id"));
                    //15	子活动名称	必填
                    resultmap.put("A15", campaignedmap.get("campaign_name"));
                    //16	子活动开始时间	必填,长度14位,为数据生成时间
                    resultmap.put("A16", campaignedmap.get("campaign_starttime").replace("/", "").replace(":", "").replace(" ", ""));
                    //17	子活动结束时间	必填,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
                    resultmap.put("A17", campaignedmap.get("campaign_starttime").replace("/", "").replace(":", "").replace(" ", ""));
                    //18	目标客户群编号	必填
                    resultmap.put("A18", campaignedmap.get("customer_group_id"));
                    //19	目标客户群名称	必填
                    resultmap.put("A19", campaignedmap.get("customer_group_name"));
                    //20	目标客户群规模	可为空
                    resultmap.put("A20", campaignedmap.get("customer_num"));
                    //21	目标客户群描述	可为空
                    resultmap.put("A21", "");
                    //22	目标客户筛选标准	必填
                    resultmap.put("A22", campaignedmap.get("customer_filter_rule"));

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
                }
            } catch (Exception e) {
                log.info("93001" + activity.get("activity_id") + "异常:\r" + e.getMessage());
                e.printStackTrace();
            }
        }
        SqlUtil.getInsert("93001", list);
//
    }


//    @Test
//    public void testsavebase93001() throws Exception {
//        List<Map> list = new ArrayList<>();
//        Map<String, String> map = null;
//        Map<String, String> resultmap = null;
//        List<Map<String, String>> activitys = fileDataService.getBaseInfo93001();
//        //属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-50子活动效果评估指标
//        for (Map<String, String> activity : activitys) {
//            map = new HashMap<>();
//            //2	统计时间	必填,长度14位,为数据生成时间
//            map.put("A2", TimeUtil.getLongSeconds(new Date()));
//            //3	省份	必填(长度3位),省份与互联网编码参考9.1省公司编码
//            map.put("A3", "280");
//            //4	地市	必填,长度： 每个地市长度3位或4位（未知地市为00000除外）
//            map.put("A4", activity.get("city_code"));
//
//
//            /**
//             * 营销活动相关信息
//             */
//            //5	营销活动编号	必填,前三位必须为省份编码
//            String activity_id = activity.get("activity_id");
//            map.put("A5", "280" + activity_id.substring(1));
//            //6	营销活动名称	必填
//            map.put("A6", activity.get("activity_name"));
//            //7	活动开始时间	必填,长度14位,为数据生成时间
//            map.put("A7", activity.get("start_time").replace("-", "") + "000000");
//            //8	活动结束时间	必填,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
//            map.put("A8", activity.get("end_time").replace("-", "") + "000000");
//            //9	营销活动类型	必填，填写枚举值ID
//            map.put("A9", "9");
//            //10	营销活动目的	必填，填写枚举值ID
//            map.put("A10", "9");
//            //11	营销活动描述	对产品、服务等信息进行简要描述
//            map.put("A11", activity.get("activity_name"));
//            //12	PCC策略编码	（当采用了PCC能力时，相关内容必填）
//            map.put("A12", "");
//            //13	所属流程	必填,数字枚举值
//            map.put("A13", "1");
//
//
//            //37	PV	可为空，,口径：页面曝光量、接触量、浏览量。,电子渠道效果指标
//            map.put("A37", "");
//            //38	点击量	可为空，,口径：页面内容被点击的次数。,电子渠道效果指标
//            map.put("A38", "");
//            //39	UV（剔重）	可为空，,口径：独立用户/独立访客。,电子渠道效果指标
//            map.put("A39", "");
//            //40	办理量	可为空，,口径：业务办理次数。,电子渠道效果指标
//            map.put("A40", "");
//            //41	用户号码明细	互联网特有，可为空
//            map.put("A41", "");
//            //42	活动专题ID	当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
//            map.put("A42", "");
//
//
//            /**
//             * 子活动效果评估指标
//             */
//            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity_id);
//            System.out.println("mapEffect=" + mapEffect);
//            //43	成功接触客户数	日指标，必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
//            map.put("A43", mapEffect.get("touch_num"));
//            //44	接触成功率	日指标，必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
//            map.put("A44", mapEffect.get("touhe_rate"));
//            //45	响应率	日指标，必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,		（注意需填小数，而不是百分数）
//            map.put("A45", mapEffect.get("response_rate"));
//            //46	营销成功用户数	日指标，必填；,口径：根据运营目的，成功办理或者成功使用的用户数
//            map.put("A46", mapEffect.get("vic_num"));
//            //47	营销成功率	日指标,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,		（注意需填小数，而不是百分数）
//            map.put("A47", mapEffect.get("vic_rate"));
//
//
//            //48	使用用户数	日指标，必填：,运营成功用户产生本业务使用行为的用户
//            map.put("A48", mapEffect.get("touch_num"));
//            //49	4G终端4G流量客户占比	日指标，必填且取值小于1；,口径：4G流量客户数/4G终端用户数,例：填0.1代表10%,		（注意需填小数，而不是百分数）
//            map.put("A49", mapEffect.get("terminal_flow_rate"));
//            //50	4G流量客户数	日指标，选填，口径：统计周期内，使用4G网络产生4G流量的客户数
//
//
//            map.put("A50", "");
//
//            /**
//             * 子活动相关信息
//             */
//            List<Map<String, String>> campaignedList = fileDataService.getCampaignedInfo(activity_id);
//            for (Map<String, String> campaignedmap : campaignedList) {
//                resultmap = new HashMap<>();
//                //14	子活动编号	必填,参考附录1统一编码规则中的营销子活动编号编码规则,所属流程为2、8、9、10时，前3位编号为省份编码
//                resultmap.put("A14", campaignedmap.get("campaign_id"));
//                //15	子活动名称	必填
//                resultmap.put("A15", campaignedmap.get("campaign_name"));
//                //16	子活动开始时间	必填,长度14位,为数据生成时间
//                resultmap.put("A16", campaignedmap.get("campaign_starttime").replace("-", "").replace(":", "").replace(" ", ""));
//                //17	子活动结束时间	必填,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
//                resultmap.put("A17", campaignedmap.get("campaign_starttime").replace("-", "").replace(":", "").replace(" ", ""));
//                //18	目标客户群编号	必填
//                resultmap.put("A18", campaignedmap.get("sgmt_id"));
//                //19	目标客户群名称	必填
//                resultmap.put("A19", campaignedmap.get("sgmt_name"));
//                //20	目标客户群规模	可为空
//                resultmap.put("A20", campaignedmap.get("sgmt_num"));
//                //21	目标客户群描述	可为空
//                resultmap.put("A21", campaignedmap.get("sgmt_desc"));
//                //22	目标客户筛选标准	必填
//                resultmap.put("A22", campaignedmap.get("sgmt_sift_rule"));
//
//                List<Map<String, String>> offerMaps = fileDataService.getOfferBo(campaignedmap.get("campaign_id"));
//                if (offerMaps.size() != 1) {
//                    throw new Exception("活动产品信息不唯一，需要处理");
//                }
//                Map<String, String> offerMap = offerMaps.get(0);
//                //23	产品编码	必填,前七位需符合8.1产品编码规则
//                String proCode = offerMap.get("offer_code");
//                resultmap.put("A23", proCode);
//                //24	产品编码截取	必填,长度限制7位,		（省份截取前7位）
//                resultmap.put("A24", proCode.substring(0, 7));
//                //25	产品名称	必填
//                resultmap.put("A25", offerMap.get("offer_name"));
//                //26	产品分类	必填，填写枚举值ID
//                resultmap.put("A26", offerMap.get("offer_type"));
//                //27	渠道编码	必填,比如,00108xxxx,001：一级分类,08：二级分类,Xxxx：自定义渠道编码,编码规则参考8.2渠道和运营位编码规则
//                resultmap.put("A27", campaignedmap.get("channel_id"));
//                //28	渠道编码一级分类	必填,长度3位,		（省份截取前3位）
//                resultmap.put("A28", campaignedmap.get("channel_id").substring(0, 3));
//                //29	渠道编码二级分类	必填,长度2位,		（省份截取第4、5位）
//                resultmap.put("A29", campaignedmap.get("channel_id").substring(3, 5));
//                //30	渠道名称	必填
//                resultmap.put("A30", campaignedmap.get("channel_name"));
//                //31	渠道类型	必填,参考10附录3渠道类型编码,位数为偶数位
//                resultmap.put("A31", campaignedmap.get("channe_type"));
//                //32	渠道接触规则	必填
//                resultmap.put("A32", campaignedmap.get("channel_rule"));
//                //33	时机识别	必填，填写枚举值ID
//                resultmap.put("A33", campaignedmap.get("time_id"));
//                //34	时机识别描述	可为空
//                resultmap.put("A34", campaignedmap.get("time_distindes"));
//                //35	客户质量情况	描述性信息,可为空
//                resultmap.put("A35", "");
//                //36	资源使用情况	描述性信息,可为空
//                resultmap.put("A36", "");
//                resultmap.putAll(map);
//                list.add(resultmap);
//            }
//
//
//        }
//
//    }
}
