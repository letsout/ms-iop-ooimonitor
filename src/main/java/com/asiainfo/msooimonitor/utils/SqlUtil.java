package com.asiainfo.msooimonitor.utils;

import com.asiainfo.msooimonitor.mapper.dbt.common.CommonMapper;
import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.service.FileDataService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SqlUtil {
    public static Map<String, Integer> tableMap = new HashMap<>();

    static CommonMapper commonMapper;

    @Autowired
    public SqlUtil(CommonMapper commonMapper) {
        SqlUtil.commonMapper = commonMapper;
    }

    static {
        tableMap.put("93001", 48);
        tableMap.put("93002", 83);
        tableMap.put("93005", 80);
        tableMap.put("93006", 15);
    }

    public static void getInsert(String interfaceName, List<Map<String, Object>> list) {
        // 每5000条插入一次
        int start = 0;
        int splitNum = 1500;
        for (int i = 0; i < list.size() / splitNum; i++) {
            getInsertsql(interfaceName, list.subList(start, splitNum + start));
            start += splitNum;
        }
        getInsertsql(interfaceName, list.subList(start, list.size()));

    }

    public static void getInsertsql(String interfaceName, List<Map<String, Object>> list) {
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
            Map<String, Object> mapinsert = list.get(i);
            sbvalue = new StringBuilder();
            sbvalue.append("('");
            for (int a = 2; a <= columSize; a++) {
                String key = "A" + a;
                String value =  mapinsert.getOrDefault(key, "")+"";
                value = value.replaceAll("\n", "");
                if (value == null || value.equals("null") || value.replaceAll("null", "").replaceAll(",", "").equals("")) {
                    value = null;
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
//                System.out.println("不是最后一行");
                sb.append(",");
            }
        }

        String sql = sb.toString();
        commonMapper.insertSql(sql);
        log.info("{}接口成功插入数据：{}条", interfaceName, list.size());
    }

    public static void main(String[] args) {
        String str="str\nstr1";
        System.out.println(str);
        str=str.replaceAll("\n","");
        System.out.println(str
        );
    }
}
