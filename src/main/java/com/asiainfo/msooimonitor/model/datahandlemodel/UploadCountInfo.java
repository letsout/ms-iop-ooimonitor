package com.asiainfo.msooimonitor.model.datahandlemodel;

import lombok.Builder;
import lombok.Data;

/**
 * @Author H
 * @Date 2019/9/25 11:04
 * @Desc
 **/
@Data
@Builder
public class UploadCountInfo {

    private String interfaceId;

    private int uploadNum;

    private int failNum;
}
