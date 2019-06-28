package com.asiainfo.msooimonitor.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
public interface LoadService {

    /**
     * 根据不同的表创建不同的insert 语句
     * @param tableName 表名 默认schem 为fcm
     * @return insert 语句
     */
    Map<String,Object> sqlTemplate(String tableName);


   // void batchInsert(String sql, List<Map<String,String>> mapList,String tableName,String date);

    void deleteSql(String sql);

    void updateRecord(Map map);

    int getrows(String tableName);

}
