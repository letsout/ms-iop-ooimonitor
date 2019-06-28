package com.asiainfo.msooimonitor.mapper.dbt.common;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface CommonMapper {

    /**
     *查询sql返回值并存放在name里面
     * @param sql
     * @return 返回sql查询值
     */
    String getValue(@Param("sql") String sql);

    List<Map<String,String>> getMap(@Param("sql") String sql);
    /**
     * insert
     * @param sql
     */
    void insertSql(@Param("sql") String sql);

}
