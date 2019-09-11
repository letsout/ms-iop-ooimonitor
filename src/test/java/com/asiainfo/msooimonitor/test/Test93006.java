package com.asiainfo.msooimonitor.test;

import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.utils.SqlUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test93006 {
    @Autowired
    FileDataService fileDataService;

    @Test
    public void testsavebase93006() {

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getBaseInfo93006();
        System.out.println("activitys.size=" + activitys.size());
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
            map.put("A6", "280" + activity_id.substring(1));
            //7 营销活动名称 必填
            map.put("A7", activity.get("activity_name"));
            //8 子活动编号 可为空，参考附录1统一编码规则中的营销子活动编号编码规则；当营销活动涉及多子活动时，以逗号分隔
            map.put("A8", null);
            //9 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A9", null);
            //10 PV 可为空,口径：该用户打开网页次数,电子渠道效果指标
            map.put("A10", null);
            //11 用户点击量 可为空,口径：页面内容被该用户点击的次数,电子渠道效果指标
            map.put("A11", null);
            //12 用户办理量 可为空,口径：该用户业务办理次数,电子渠道效果指标
            map.put("A12", null);
            //16 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A16", null);
            List<Map<String, String>> detaileffect = fileDataService.getDetailEffect(activity.get("activity_id"), TimeUtil.getDaySql(new Date()));
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
        }

        String sql = SqlUtil.getInsert("93006", list);
        fileDataService.saveresultList(sql);

    }


    @Test
    public void testsavemarking93006() {

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        Map<String, String> mapresult = null;
        List<Map<String, String>> activitys = fileDataService.getMarkenInfo93006();
        System.out.println("activitys.size=" + activitys.size());
        //1 行号
        for (Map<String, String> activity : activitys) {
            map = new HashMap<>();
            //2 统计时间 必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getLongSeconds(new Date()));
            //3 省份 必填，长度3位
            map.put("A3", "280");
            //4 地市 必填,长度： 3位或4位
           map.put("A4", activity.getOrDefault("city_code","028"));
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
            map.put("A10", null);
            //11 用户点击量 可为空,口径：页面内容被该用户点击的次数,电子渠道效果指标
            map.put("A11", null);
            //12 用户办理量 可为空,口径：该用户业务办理次数,电子渠道效果指标
            map.put("A12", null);
            //16 活动专题ID 当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A16", activity.get("spetopic_id"));
//            activity.put("final_obj_table_name", "OBJ_284576966837831");
//            final List<Map<String, String>> phones = fileDataService.getPhone93006(activity.get("final_obj_table_name"));
////            System.out.println("phones=="+phones);
            List<Map<String, String>> detaileffect = fileDataService.getDetailEffect(activity.get("activity_id"), TimeUtil.getDaySql(new Date()));
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
        }
        String sql = SqlUtil.getInsert("93006", list);
        fileDataService.saveresultList(sql);
    }
}
