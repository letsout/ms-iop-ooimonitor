package com.asiainfo.msooimonitor.mapper.dbt.ooi;

import com.asiainfo.msooimonitor.model.datahandlemodel.Act93004Info;
import com.asiainfo.msooimonitor.model.datahandlemodel.Act93006Info;
import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.model.ooimodel.label.UploadLabelInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InterfaceInfoMpper {
    List<Map<String, String>> getDetailEffect(@Param("activityIds") String activityIds, @Param("date") String date, @Param("start") int start, @Param("limit") int limit);

    Map<String, String> getSummaryEffect(@Param("activityId") String activityId, @Param("date") String date);

    int getTableRows(@Param("activityIds") String activityIds, @Param("dateTimeFormat") String dateTimeFormat);

    List<Map<String, String>> getSummaryEffects(@Param("activityIds") String activityIds, @Param("date") String date);

    void truncateTable(@Param("tableName") String tableName);

    void insert93055(List<Map<String, String>> list);

    String getSummaryEffectMaxDate(@Param("activityId") String activityId, @Param("beforeDate") String beforeDate);

    void insert93056(List<Map<String, String>> list);

    String getMaxTime(String activityId);

    void insert93006Info(List<Act93006Info> list);

    void insertiop93006(@Param("activityEndDate") String activityEndDate);

    int getTableRowsByTableName(@Param("tableName") String tableName);

    int tableIsExit(@Param("schema") String schema, @Param("tableName") String tableName);

    void insertIop93004(Act93004Info activity);

    Map<String, String> getLastSummaryEffect(@Param("activityId") String activityId, @Param("month") String month);

    void insertIop93011();
    String getNewTableName(@Param("tableName") String tableName,@Param("length") int length);

    void countLabeData(@Param("tableName") String tableName,@Param("rule") String rule,@Param("uploadTableName") String uploadTableName);

    void createTbale(@Param("tableName") String tableName,@Param("content") String content);

    void countJTLabeData(@Param("cocTableName") String cocTableName,@Param("rule") String rule,@Param("uploadTableName") String uploadTableName,@Param("sexTable") String sexTable);

    void dropTable(@Param("schema") String schema,@Param("tableName") String tableName);

    void insert93054(List<UploadLabelInfo> quoteLabelInfo);
}
