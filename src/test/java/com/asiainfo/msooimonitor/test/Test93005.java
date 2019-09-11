package com.asiainfo.msooimonitor.test;

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
    /**
     * 时间后缀
     */
    private String suffix = "0000000";
    /**
     * 分隔符
     */
    private String splite = new String(new byte[]{(byte) 0X80});

    /**
     * 换行符
     */
    private String enter = new String(new byte[]{(byte) 0x0D0A});

    String date = "";
    //本地存储文件路径
    String localPath = "";
    String checkFileName = "";
    String filePreName = "";

    @Test
    public void testsaveMarking93005() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93005();


//        属性编码 5-13、18-33为营销活动相关信息，14-17子活动相关信息，40-84营销活动效果评估指标
        for (Map<String, String> activity : activitys) {
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
            map.put("A7", activity.get("activity_starttime").replace("-", "").replace(":", "").replace(" ", ""));
            //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
            map.put("A8", activity.get("activity_endtime").replace("-", "").replace(":", "").replace(" ", ""));
            //9 营销活动类型 必填，填写枚举值ID
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10 营销活动目的 必填，填写枚举值ID
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11 营销活动描述 对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_objective", activity.get("activity_name")));
            //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13 所属流程 必填，填写枚举值ID
            map.put("A13", "1");//待定


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
            map.put("A16", activity.get("campaign_starttime"));
            //17 子活动结束时间 可为空,格式：YYYYMMDDHH24MISS,子活动结束时间不早于子活动开始时间,示例：20170213161140
            map.put("A17", activity.get("campaign_endtime"));


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

/**
 * 40-84营销活动效果评估指标
 */
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
        String sql = SqlUtil.getInsert("93005", list);
        fileDataService.saveresultList(sql);
    }


    public void testBase93005() {
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
            map.put("A4", activity.get("city_code"));
            //5 营销活动编号 必填,前三位必须为省份编码
            String activityId = activity.get("activity_id");
            map.put("A5", "280" + activityId.substring(1));
            //6 营销活动名称 必填
            map.put("A6", activity.get("activity_name"));
            //7 活动开始时间 必填,长度14位
            map.put("A7", activity.get("start_time").replace("-", "") + "000000");
            //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
            map.put("A8", activity.get("end_time").replace("-", "") + "000000");
            //9 营销活动类型 必填，填写枚举值ID
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10 营销活动目的 必填，填写枚举值ID
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11 营销活动描述 对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
            //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.getOrDefault("pcc_id", ""));
            //13 所属流程 必填，填写枚举值ID
            map.put("A13", "1");//待定


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
//            map.put("A14", "280_000"+activity.get("activity_id"));
            map.put("A14", "");
            //15 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
//            map.put("A15", activity.get("campaign_name"));
            map.put("A15", "");
            //16 子活动开始时间 可为空,格式：YYYYMMDDHH24MISS,示例：20170213161140
//            map.put("A16", activity.get("campaign_starttime"));
            map.put("A16", "");
            //17 子活动结束时间 可为空,格式：YYYYMMDDHH24MISS,子活动结束时间不早于子活动开始时间,示例：20170213161140
//            map.put("A17", activity.get("campaign_starttime"));
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
        String sql = SqlUtil.getInsert("93005", list);
        System.out.println(sql);
        fileDataService.saveresultList(sql);
    }
}
