package com.asiainfo.msooimonitor.utils;

import com.asiainfo.msooimonitor.service.FileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public static void getInsert(String interfaceName, List<Map<String, Object>> list) {
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
        System.out.println("list==" + list);

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
        System.out.println("sql:");
        System.out.println(sql);
        System.out.println();
        fileDataService.saveresultList(sql);
    }


}
