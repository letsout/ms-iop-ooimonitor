package com.asiainfo.msooimonitor.mapper.dbt.upload;

import com.asiainfo.msooimonitor.model.datahandlemodel.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UploadMapper {
    List<Map<String,String>> getInterfaceInfo(@Param("sql") String sql );
}