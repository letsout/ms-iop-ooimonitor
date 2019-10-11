package com.asiainfo.msooimonitor.model.datahandlemodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author H
 * @Date 2019/9/25 11:04
 * @Desc
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadCountInfo {

    private String interfaceId;

    private int uploadNum;

    private int failNum;
    private String activityTime;
}
