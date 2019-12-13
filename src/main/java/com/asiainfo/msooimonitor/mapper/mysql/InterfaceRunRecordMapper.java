package com.asiainfo.msooimonitor.mapper.mysql;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceRunRecordMapper {
    List<InterfaceRecord> getInterfaceRunRecordInfo(InterfaceRecord serachFilter);

    /**
     * 根据id删除接口信息
     * @param interfaceId
     */
    void deleteInterfaceInfos(@Param("interfaceId")String interfaceId);
}
