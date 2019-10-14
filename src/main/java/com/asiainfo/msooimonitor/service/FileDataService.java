package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadDetailInfo;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:24
 * Description
 */
public interface FileDataService {
    void insertFailDetails(List<UploadDetailInfo> list);


    List<Map<String, String>> getMarkingInfo93005(String activityEndDate) throws Exception;

    void insertFlow();

    void truncateTable(String tableName);

    /**
     * 省级IOP同步子活动结束次月效果评估数据给一级IOP
     */
    void create93055(String month);

    /**
     * 累计效果评估接口：省级IOP上报营销活动结束次月效果数据
     */
    void create93056(String month);

    void insertInterfaceRelTable(CretaeFileInfo cretaeFileInfo);

    Map<String, String> getSummaryEffectAll(String activityId, String activityEndDate);

    List<Map<String, String>> getData93003(String month);
}
