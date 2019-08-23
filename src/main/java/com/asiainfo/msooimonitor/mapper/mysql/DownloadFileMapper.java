package com.asiainfo.msooimonitor.mapper.mysql;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadFileMapper {

    /**
     * 查询需要下载的文件接口
     * @return
     */
    List<InterfaceInfo> listDownloadFileInterface();

    /**
     * 保存文件入库记录
     * @param interfaceRecord
     */
    void insertRecord(InterfaceRecord interfaceRecord);

    /**
     * 获取此接口成功的最大时间
     * @param interfaceId
     * @return
     */
    String getMaxSuccessTime(String interfaceId);
}
