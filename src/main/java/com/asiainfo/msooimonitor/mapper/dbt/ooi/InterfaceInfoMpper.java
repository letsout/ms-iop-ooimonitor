package com.asiainfo.msooimonitor.mapper.dbt.ooi;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InterfaceInfoMpper {

    /**
     * 获取接口信息
     *
     * @param params
     * @return
     */
    List<InterfaceInfo> getInterfaceInfo(Map params);

    int checkInterface(String interfaceId);

    void saveInterfceInfo(InterfaceInfo params);

    /*   List<InterfaceRecord> getInterfaceRecord(@Param("start") int start,@Param("end") int end);*/

    List<InterfaceRecord> getInterfaceRecord(Map params);

    void saveInterfaceRecord(Map params);

    void deleteInterfaceId(String interfaceId);

    void editInterfaceInfo(InterfaceInfo params);

    List<String> getDownloadFile(String date);

    List<InterfaceInfo> getuploadInterface();

    List<InterfaceInfo> getLoadInterface();

    List<InterfaceRecord> getInterfaceRecordByParam(@Param("start") int start,
                                                    @Param("end") int end,
                                                    @Param("interfaceId") String interfaceId,
                                                    @Param("updateType") String updateType,
                                                    @Param("state") String state,
                                                    @Param("updateTime") String updateTime);

    String getInterfaceIdType(@Param("interfaceId") String interfaceId);

    List<Map<String, String>> getDetailEffect(@Param("activityIds") String activityIds, @Param("date") String date, @Param("start") int start, @Param("limit") int limit);

    Map<String, String> getSummaryEffect(@Param("activityIds") String activityIds, @Param("date") String date);

    int getTableRows(@Param("activityIds") String activityIds, @Param("dateTimeFormat") String dateTimeFormat);

    List<Map<String, String>> getSummaryEffects(@Param("activityIds") String activityIds, @Param("date") String date);

    void truncateTable(@Param("tableName") String tableName);

    void insert93055(List<Map<String, String>> paramList);

    void insertInterfaceRelTable(CretaeFileInfo cretaeFileInfo);
}
