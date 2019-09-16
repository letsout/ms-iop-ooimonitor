package com.asiainfo.msooimonitor.utils;

import com.asiainfo.msooimonitor.service.FileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/10  21:25
 * Description
 */
@Component
public class SqlUtil {
    public static Map<String, Integer> tableMap = new HashMap<>();

    static FileDataService fileDataService;

    @Autowired
    public SqlUtil(FileDataService fileDataService) {
        SqlUtil.fileDataService = fileDataService;
    }

static {
    tableMap.put("93001", 48);
    tableMap.put("93002", 90);
    tableMap.put("93005", 80);
    tableMap.put("93006", 15);
}

    public static void getInsert(String interfaceName, List<Map<String, String>> list) {
        // 每5000条插入一次
        int start = 0;
        int splitNum = 1500;
        for (int i = 0; i <list.size()/splitNum ; i++) {
            getInsertsql(interfaceName,list.subList(start,splitNum+start));
            start +=splitNum;
        }
        getInsertsql(interfaceName,list.subList(start,list.size()));

    }
    public static void getInsertsql(String interfaceName, List<Map<String, String>> list) {
        if (list.size() == 0)
            return;

        int columSize = tableMap.get(interfaceName);
        StringBuilder sb = new StringBuilder();

        StringBuilder sbcolum = new StringBuilder();
        sb.append("insert into iop_" + interfaceName + "(");
        for (int a = 2; a <= columSize; a++) {
            sbcolum.append(",").append("A").append(a);
        }
        sb.append(sbcolum.substring(1)).append(") values ");
//        System.out.println("ab==" + sb);

        StringBuilder sbvalue;
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> mapinsert = list.get(i);
            sbvalue = new StringBuilder();
            sbvalue.append("('");
            for (int a = 2; a <= columSize; a++) {
                String key = "A" + a;
                String value = mapinsert.getOrDefault(key, "");
                if (value == null) {
                    value = "";
                }
                if (value.equals("null")) {
                    value = "";
                }
                System.out.println(key + "->" + value);
                if (a != columSize) {
                    sbvalue.append(value).append("','");
                } else {
                    sbvalue.append(value).append("')");
                }
            }
            sb.append(sbvalue);
            if (i != (list.size() - 1)) {
                System.out.println("不是最后一行");
                sb.append(",");
            }
        }

        String sql = sb.toString();
        System.out.println("sql:");
        System.out.println(sql);
        System.out.println();
        fileDataService.saveresultList(sql);
    }

    public static void getInsertObj(String interfaceName, List<Map<String, Object>> list) {
        int columSize = tableMap.get(interfaceName);
        StringBuilder sb = new StringBuilder();

        StringBuilder sbcolum = new StringBuilder();
        sb.append("insert into iop_" + interfaceName + "(");
        for (int a = 2; a <= columSize; a++) {
            sbcolum.append(",").append("A").append(a);
        }
        sb.append(sbcolum.substring(1)).append(") values ");
//        System.out.println("ab==" + sb);

        StringBuilder sbvalue;
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> mapinsert = list.get(i);
            sbvalue = new StringBuilder();
            sbvalue.append("('");
            for (int a = 2; a <= columSize; a++) {
                String key = "A" + a;
                Object value = mapinsert.getOrDefault(key, "");
                if (value == null) {
                    value = "";
                }
                if (value.equals("null")) {
                    value = "";
                }
                System.out.println(key + "->" + value);
                if (value == null)
                    value = "";
                if (value.equals("null"))
                    value = "";
                if (a != columSize) {
                    sbvalue.append(value).append("','");
                } else {
                    sbvalue.append(value).append("')");
                }
            }
            sb.append(sbvalue);
            if (i != (list.size() - 1)) {
                System.out.println("不是最后一行");
                sb.append(",");
            }
        }

        String sql = sb.toString();
        System.out.println("sql:");
        System.out.println(sql);
        System.out.println();
        fileDataService.saveresultList(sql);
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap();
        map.put("A2", "统计时间");
        map.put("A3", "省份");
        map.put("A4", "地市");
        map.put("A5", "营销活动编号");
        map.put("A6", "营销活动名称");
        map.put("A7", "活动开始时间");
        map.put("A8", "活动结束时间");
        map.put("A9", "营销活动类型");
        map.put("A10", "营销活动目的");
        map.put("A11", "营销活动描述");
        map.put("A12", "PCC策略编码");
        map.put("A13", "所属流程");
        map.put("A14", "子活动编号");
        map.put("A15", "子活动名称");
        map.put("A16", "子活动开始时间");
        map.put("A17", "子活动结束时间");
        map.put("A18", "目标客户群编号");
        map.put("A19", "目标客户群名称");
        map.put("A20", "目标客户群规模");
        map.put("A21", "目标客户群描述");
        map.put("A22", "目标客户筛选标准");
        map.put("A23", "产品编码");
        map.put("A24", "产品名称");
        map.put("A25", "产品分类");
        map.put("A26", "渠道编码");
        map.put("A27", "渠道名称");
        map.put("A28", "渠道类型");
        map.put("A29", "渠道接触规则");
        map.put("A30", "时机识别");
        map.put("A31", "时机识别描述");
        map.put("A32", "客户质量情况");
        map.put("A33", "资源使用情况");
        map.put("A34", "PV");
        map.put("A35", "点击量");
        map.put("A36", "UV(剔重)");
        map.put("A37", "办理量");
        map.put("A38", "用户号码明细");
        map.put("A39", "活动专题ID");
        map.put("A40", "成功接触客户数");
        map.put("A41", "接触成功率");
        map.put("A42", "响应率");
        map.put("A43", "营销成功用户数");
        map.put("A44", "营销成功率");
        map.put("A45", "使用用户数");
        map.put("A46", "活动总用户数");
        map.put("A47", "PCC签约用户数");
        map.put("A48", "PCC策略生效用户数");
        map.put("A49", "PCC策略生效次数");
        map.put("A50", "签约客户转化率");
        map.put("A51", "累计办理宽带用户数");
        map.put("A52", "用户宽带融合提升率");
        map.put("A53", "活跃用户数");
        map.put("A54", "活跃用户增长率");
        map.put("A55", "新增流量用户数");
        map.put("A56", "新入网流量用户占比");
        map.put("A57", "新增4G用户数");
        map.put("A58", "新入网4g用户占比");
        map.put("A59", "大流量套餐办理量");
        map.put("A60", "大流量套餐办理率（融合套餐）");
        map.put("A61", "基础套餐内语音实际使用量");
        map.put("A62", "语音使用用户");
        map.put("A63", "套餐语音饱和度");
        map.put("A64", "套餐语音活跃用户占比");
        map.put("A65", "套餐语音低使用天数（5天）占比");
        map.put("A66", "低通话量用户数");
        map.put("A67", "低通话量用户占比");
        map.put("A68", "基础套餐内流量实际使用量");
        map.put("A69", "套餐流量饱和度");
        map.put("A70", "套餐流量活跃度");
        map.put("A71", "套餐流量低使用天数（5天）占比");
        map.put("A72", "低流量用户数");
        map.put("A73", "低流量用户占比");
        map.put("A74", "4G终端4G流量客户占比");
        map.put("A75", "4G流量客户数");
        map.put("A76", "4G客户中4G流量低使用天数（5天）占比");
        map.put("A77", "4G客户中4G低流量用户占比");
        map.put("A78", "月一次使用用户占比");
        map.put("A79", "包月产品活跃度");
        map.put("A80", "宽带账户活跃用户数");
        map.put("A81", "宽带账户活跃度");
        map.put("A82", "低使用次数用户占比");
        map.put("A83", "魔百和用户活跃度");
        map.put("A84", "户均收入");
        map.put("A85", "总收入");
        map.put("A86", "投入产出比");
        map.put("A87", "0x0D0A");


//
//        List<Map<String, String>> objects = new ArrayList<>();
//        objects.add(map);
//        SqlUtil.getInsert("93005", objects);

        Map<String, String> mapstr = new HashMap<>();
        Map<String, String> mapstr1 = new HashMap<>();
        mapstr.put("A2", "");
        mapstr.put("A3", "");
        mapstr.put("A4", "");
        mapstr.put("A5", "A5");
        mapstr.put("A6", "222222222222");
//        final String key = mapstr.get("key");
//        final String key1 = mapstr.getOrDefault("key", "1");
//        System.out.println(key);
//        System.out.println(key1);
        List<Map<String, String>> list = new ArrayList<>();
        mapstr1.putAll(mapstr);
        list.add(mapstr);
        SqlUtil.getInsert("93006", list);

    }
}
