package com.asiainfo.msooimonitor.mapper.dbt.load;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LoadMapper {

    List<String> selectTableSutrct (@Param("sql") String sql );


    void deleteSql(@Param("sql") String sql);

    void updateRecord(Map map);

    int getrows(@Param("tableName") String tableName);
}
