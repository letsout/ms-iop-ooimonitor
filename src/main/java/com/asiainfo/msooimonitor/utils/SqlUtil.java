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
    static {
        tableMap.put("93001", 50);
        tableMap.put("93002", 90);
        tableMap.put("93005", 86);
        tableMap.put("93006", 16);
    }

    public static String getInsert(String interfaceName, List<Map<String, String>> list) {
        int columSize = tableMap.get(interfaceName);
        for (Map<String, String> mapinsert : list) {
            StringBuilder sb = new StringBuilder();
            sb.append("('");
            StringBuilder sbcolum = new StringBuilder();
            sbcolum.append("insert into iop_" + interfaceName + "(");
            for (int a = 2; a <= columSize; a++) {
                String value = mapinsert.get("A" + a);
                if (value.equals("null") || value == null)
                    value = "";
                if (a == columSize) {
                    sbcolum.append("A").append(a).append(",");
                    sb.append(value).append("','");
                } else {
                    sbcolum.append("A").append(a).append(") values ");
                    sb.append(value).append("'),");
                }
            }
            String sql = sbcolum.append(sb.substring(0, sb.length() - 1)).toString();
            System.out.println(sql);
            return sql;
        }
        return null;
    }
}
