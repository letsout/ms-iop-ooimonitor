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
public class Test93002 {
    @Resource
    FileDataService fileDataService;

    @Test
    public void testsavemarking93002() throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> resultmap = null;
        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93002();
//        属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-90子活动效果评估指标

        for (Map<String, String> activity : activitys) {
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
            map.put("A7", activity.get("activity_starttime").replace("-", "").replace(" ", "").replace(":", ""));
            //8,活动结束时间,格式：YYYYMMDDHH24MISS,必填,示例：20170213161140,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
            map.put("A8", activity.get("activity_endtime").replace("-", "").replace(" ", "").replace(":", ""));
            //9,营销活动类型,1：入网类,必填，填写枚举值ID,2：终端类,3：流量类,4：数字化服务类,5：基础服务类,6：客户保有类,7：宽带类,8：融合套餐类,9：其它类
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10,营销活动目的,1：新增客户类,必填，填写枚举值ID,2：存量保有类,3：价值提升类,4：离网预警类,9：其它类
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11,营销活动描述,对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_objective", activity.get("activity_name")));
            //12,PCC策略编码,（当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13,所属流程,1：一级策划省级执行,必填，填写枚举值ID,2：省级策划一级执行-互联网,3：省级策划省级执行,4：一级策划一点部署-一级电渠,5：一级策划一点部署-互联网,6：一级策划一点部署-省级播控平台,7：一级策划一点部署-咪咕,8：省级策划一级执行-电渠,9：省级策划一级执行-咪咕,10：省级策划一级执行-爱流量,98：一级策划一点部署,99：其他
            map.put("A13", "");


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
            map.put("A42", map.get("spetopic_id"));


            /**
             * 43-90子活动效果评估指标
             */
            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity_id);
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


            /**
             * 14-36子活动相关信息
             */
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
                resultmap.put("A18", campaignedmap.get("customer_group_id"));
                //19,目标客户群名称,必填
                resultmap.put("A19", campaignedmap.get("customer_group_name"));
                //20,目标客户群规模,可为空
                resultmap.put("A20", campaignedmap.get("customer_num"));
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
            }
        }
        String sql = SqlUtil.getInsert("93002", list);
//        fileDataService.saveresultList(sql);
    }

    @Test
    public void testsavebase93002() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        Map<String, Object> resultmap = null;
        List<Map<String, Object>> activitys = fileDataService.getBaseInfo93002();
//        属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-90子活动效果评估指标
        for (Map<String, Object> activity : activitys) {
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
            map.put("A11", activity.getOrDefault("activity_objective", activity.get("activity_name")));
            //12,PCC策略编码,（当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13,所属流程,1：一级策划省级执行,必填，填写枚举值ID,2：省级策划一级执行-互联网,3：省级策划省级执行,4：一级策划一点部署-一级电渠,5：一级策划一点部署-互联网,6：一级策划一点部署-省级播控平台,7：一级策划一点部署-咪咕,8：省级策划一级执行-电渠,9：省级策划一级执行-咪咕,10：省级策划一级执行-爱流量,98：一级策划一点部署,99：其他
            map.put("A13", "");


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
            map.put("A42", map.get("spetopic_id"));


            /**
             * ，43-90子活动效果评估指标
             */
            Map<String, String> mapEffect = fileDataService.getSummaryEffect(activity_id);
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
            List<Map<String, String>> offerMaps = fileDataService.getOfferBo(activity.get("activity_id").toString());
            Map<String, String> offerMap = offerMaps.get(0);
            //23,产品编码,必填,比如0200100xxxx,02：一级分类,001：二级分类,00：三级分类,Xxxx：自定义产品编号,编码规则参考8.1产品编码规则
            String proCode = offerMap.get("offer_code");
            map.put("A23", proCode);
            //24,产品编码截取,截取“产品编码”前7位,必填,编码规则参考8.1产品编码规则,长度限制7位（省份截取前7位）
            map.put("A24", proCode.substring(0, 7));
            //25,产品名称,必填
            map.put("A25", offerMap.get("offer_name"));
            //26,产品分类,1：电信服务,必填，填写枚举值ID,2：客户服务,3：数字内容服务,4：实物,5：虚拟物品
            map.put("A26", offerMap.get("offer_type"));
            //27,渠道编码,必填,比如,00108xxxx,001：一级分类,08：二级分类,Xxxx：自定义渠道编码,编码规则参考8.2渠道和运营位编码规则
            map.put("A27", mapEffect.get("channel_id"));
            //28,渠道编码一级分类,截取“渠道编码”前三位,必填,编码规则参考8.2渠道和运营位编码规则,长度3位,（省份截取前3位）
            map.put("A28", mapEffect.get("channel_id").substring(0, 3));
            //29,渠道编码二级分类,截取“渠道编码”第四、五位,必填,编码规则参考8.2渠道和运营位编码规则,长度2位,（省份截取第4、5位）
            map.put("A29", mapEffect.get("channel_id").substring(3, 5));
            //30,渠道名称,必填
            map.put("A30", mapEffect.get("channel_name"));
            //31,渠道类型,参考10附录3渠道类型编码,必填,渠道类型为偶数位
            map.put("A31", mapEffect.get("channe_type"));
            //32,渠道接触规则,可为空
            map.put("A32", mapEffect.get("channel_rule"));
            //33,时机识别,1：互联网使用事件,必填，填写枚举值ID,2：社会事件,3：位置行踪事件,4：业务办理事件,5：业务使用事件,6：周期业务事件,7：自助系统接触事件,8：PCC事件,9：其它事件,0：无事件
            map.put("A33", mapEffect.get("time_id"));
            //34,时机识别描述,可为空
            map.put("A34", mapEffect.get("time_distindes"));
            //35,客户质量情况,描述性信息,可为空
            map.put("A35", "");
            //36,资源使用情况,描述性信息,可为空
            map.put("A36", "");
            list.add(resultmap);
        }

        String sql = SqlUtil.getInsertObj("93002", list);
//        fileDataService.saveresultList(sql);
    }

}
