package com.asiainfo.msooimonitor.service;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;

import java.util.Map;
public interface LoadService {

    /**
     * 根据不同的表创建不同的insert 语句
     * @param tableName 表名 默认schem 为iop
     * @return insert 语句
     */
    Map<String,Object> sqlTemplate(String tableName);


   // void batchInsert(String sql, List<Map<String,String>> mapList,String tableName,String date);

    void deleteSql(String sql);

    int getrows(String tableName);

    void insertRecord(InterfaceRecord interfaceRecord);

    void updateRelTable(String interfaceId, String date);
}
