package com.asiainfo.msooimonitor.mapper.mysql;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceRunRecordMapper {
    List<InterfaceRecord> getInterfaceRunRecordInfo(InterfaceRecord serachFilter);
}
