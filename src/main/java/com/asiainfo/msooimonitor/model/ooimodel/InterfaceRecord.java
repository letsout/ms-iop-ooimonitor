package com.asiainfo.msooimonitor.model.ooimodel;

import lombok.Data;

/**
 * @Author H
 * @Date 2019/2/24 14:15
 * @Desc
 **/
@Data
public class InterfaceRecord {

    private String interfaceId;

    /**
     * 状态描述 0-成功 -1 失败
     */
    private String typeDesc;

    /**
     *运行步骤 1文件生成/下载 2文件上传/入库 3文件校验
     */
    private String runStep;

    private String fileName;

    private String fileNum;

    private String fileSuccessNum;

    private String errorDesc;

    private String updateTime;

    private String fileTime;

}
