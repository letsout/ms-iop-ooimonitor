package com.asiainfo.msooimonitor.mapper.dbt.upload;

import com.asiainfo.msooimonitor.model.datahandlemodel.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadMapper {

    /**
     *
     * @param tableName
     * @return
     */
 //  @Select("select * from ${tableName}")
    List<Upload93002> selectUpload93002(@Param("tableName") String tableName);

    List<Upload93005> selectUpload93005(@Param("tableName") String tableName);

    List<Upload93005> selectUpload93006(@Param("tableName") String tableName);

    List<Upload93001> selectUpload93001(@Param("tableName") String tableName);

    List<Upload93004> selectUpload93004(@Param("tableName") String tableName);

    List<Upload93010> selectUpload93010(@Param("tableName") String tableName);

    List<Upload93011> selectUpload93011(@Param("tableName") String tableName);

    List<Upload93012> selectUpload93012(@Param("tableName") String tableName);

    List<Upload93013> selectUpload93013(@Param("tableName") String tableName);
}