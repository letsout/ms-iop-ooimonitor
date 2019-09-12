package com.asiainfo.msooimonitor.test;

import com.alibaba.fastjson.JSON;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.utils.SqlUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
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
public class Test93005 {
    @Resource
    FileDataService fileDataService;

    @Test
    public void testsaveMarking93005() {
        String date = "2019/12/31";

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93005(date);


        //属性编码 5-13、18-33为营销活动相关信息，14-17子活动相关信息，40-84营销活动效果评估指标
        for (Map<String, String> activity : activitys) {
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


            //18 目标客户群编号 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A18", activity.get("customer_group_id"));
            //19 目标客户群名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A19", activity.get("customer_group_name"));
            //20 目标客户群规模 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A20", activity.get("customer_num"));
            //21 目标客户群描述 可为空
            map.put("A21", "");
            //22 目标客户筛选标准 必填
            map.put("A22", activity.get("customer_filter_rule"));


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
            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity.get("activity_id"));

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
            map.put("A45", mapEffect.get(""));
            //46 PCC策略生效用户数 选填：
            //该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
            map.put("A46", mapEffect.get(""));
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
        }
        String sql = SqlUtil.getInsert("93005", list);
        //fileDataService.saveresultList(sql);
    }

    @Test
    public void testBase93005() {
        String date = "2019-12-31";
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, Object>> activitys = fileDataService.getBaseInfo93005(date);
        System.out.println("json:");
        System.out.println(JSON.toJSONString(activitys));
        for (Map<String, Object> activity : activitys) {
            map = new HashMap<>();
            //1 行号
            //2 统计时间 必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getLongSeconds(new Date()));
            //3 省份 必填(长度3位),省份与互联网编码参考9.1省公司编码
            map.put("A3", "280");
            //4 地市 必填 ,长度： 每个地市长度3位或4位（未知地市为00000除外）
            map.put("A4", activity.getOrDefault("city_code", "028"));
            //5 营销活动编号 必填,前三位必须为省份编码
            String activityId = activity.get("activity_id").toString();
            map.put("A5", "280" + activityId.substring(1));
            //6 营销活动名称 必填
            map.put("A6", activity.get("activity_name"));
            //7 活动开始时间 必填,长度14位
            String start_time = activity.get("start_time").toString();
            map.put("A7", start_time);
            //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
            String end_time = activity.get("end_time").toString();
            System.out.println("start_time:" + start_time);
            System.out.println("end_time:" + end_time);
            map.put("A8", end_time);
            //9 营销活动类型 必填，填写枚举值ID
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10 营销活动目的 必填，填写枚举值ID
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11 营销活动描述 对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
            //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.getOrDefault("pcc_id", ""));
            //13 所属流程 必填，填写枚举值ID
            map.put("A13", "1");     //待定


            //18 目标客户群编号 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A18", activity.get("customer_group_id"));
            //19 目标客户群名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A19", activity.get("customer_group_name"));
            //20 目标客户群规模 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A20", activity.get("customer_group_count"));
            //21 目标客户群描述 可为空
            map.put("A21", activity.get("customer_group_createrule_desc"));
            //22 目标客户筛选标准 必填
            map.put("A22", activity.get("sgmt_sift_rule"));

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
            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity.get("activity_id").toString());

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
        }
        String sql = SqlUtil.getInsertObj("93005", list);
        //System.out.println(sql);
        //fileDataService.saveresultList(sql);
    }
}
