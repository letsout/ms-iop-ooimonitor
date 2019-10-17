package com.asiainfo.msooimonitor.model.datahandlemodel;

import lombok.Builder;
import lombok.Data;

/**
 * @Author H
 * @Date 2019/9/25 14:50
 * @Desc
 **/
@Data
@Builder
public class CretaeFileInfo {

    private String interfaceId;

    private String tableName;

    private String fileName;
    /**
     * 文件日期
     */
    private String dataTime;

    private String step;
    private String dataState="U";
}
