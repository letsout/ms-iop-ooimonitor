package com.asiainfo.msooimonitor.constant;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/9/24 16:09
 * @Desc
 **/
public class CommonConstant {

    public static Map<String, String> cityMap = null;
    public static Map<String, String> activityTypeMap = null;

    // 初始化本省地市与集团地市对应关系
    static {
        cityMap = new HashMap();
        activityTypeMap = new HashMap();
        cityMap.put("1", "028");
        cityMap.put("2", "028");
        cityMap.put("3", "0816");
        cityMap.put("4", "0813");
        cityMap.put("5", "0812");
        cityMap.put("6", "0839");
        cityMap.put("7", "0818");
        cityMap.put("8", "0830");
        cityMap.put("9", "0826");
        cityMap.put("10", "0827");
        cityMap.put("11", "0825");
        cityMap.put("12", "0831");
        cityMap.put("13", "0832");
        cityMap.put("14", "028");
        cityMap.put("15", "0833");
        cityMap.put("16", "0835");
        cityMap.put("17", "0838");
        cityMap.put("18", "0817");
        cityMap.put("19", "028");
        cityMap.put("20", "0837");
        cityMap.put("21", "0836");
        cityMap.put("22", "0834");
        cityMap.put("87", "028");
        activityTypeMap.put("1", "7");
        activityTypeMap.put("2", "6");
        activityTypeMap.put("3", "5");
        activityTypeMap.put("4", "9");
        activityTypeMap.put("5", "3");
        activityTypeMap.put("6", "6");
        activityTypeMap.put("7", "9");
    }

    public static String SC = "280";

    public static String YJCH = "1";
    public static String SJCHHLW = "2";
    public static String SJCHSJ = "3";

    public static String EFFECT_DAY_TABLE = "ooi_activity_detail_effect_";
}
