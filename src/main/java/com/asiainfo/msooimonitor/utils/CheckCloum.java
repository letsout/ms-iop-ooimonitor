package com.asiainfo.msooimonitor.utils;

import com.asiainfo.msooimonitor.config.SendMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author yx
 * @date 2020/1/3  12:41
 * Description
 */
@Component
public class CheckCloum {
    @Autowired
    SendMessage sendMessage;
    private Map<String, Set<String>> map = null;

    Set<String> set93001 = new HashSet();
    Set<String> set93002 = new HashSet();
    Set<String> set93005 = new HashSet();

    public CheckCloum() {
        for (int i = 2; i < 12; i++) {
            set93001.add("A" + i);
        }
        for (int i = 13; i < 34; i++) {
            set93001.add("A" + i);
        }
        for (int i = 43; i < 50; i++) {
            set93001.add("A" + i);
        }


        for (int i = 2; i < 11; i++) {
            set93002.add("A" + i);
        }
        for (int i = 13; i < 34; i++) {
            set93002.add("A" + i);
        }
        for (int i = 43; i < 50; i++) {
            set93002.add("A" + i);
        }


        for (int i = 2; i < 11; i++) {
            set93005.add("A" + i);
        }
        set93005.add("A13");
        set93005.add("A22");
        for (int i = 40; i < 47; i++) {
            set93005.add("A" + i);
        }
        map.put("93001", set93001);
        map.put("93002", set93002);
        map.put("93005", set93005);
    }

    public void checkColunmIsNull(Map<String, Object> resultmap, String interfaceId) throws Exception {
        final Set<String> set = map.get(interfaceId);
        for (String str : set) {
            final String s = resultmap.get(str) + "";
            if (StringUtils.isEmpty(s)) {
                sendMessage.sendSms("接口：" + interfaceId + "检验字段报错字段" + str + "为空，请从gbase查询检验");
                throw new Exception("接口：" + interfaceId + "检验字段报错字段" + str + "为空，请从gbase查询检验");
            }
        }
    }
}
