package com.asiainfo.msooimonitor.model.datahandlemodel;

import lombok.Builder;
import lombok.Data;

/**
 * @Author H
 * @Date 2019/9/25 11:21
 * @Desc
 **/
@Data
@Builder
public class UploadDetailInfo {

    private String interfaceId;

    private String activityId;

    private String activitytype;

    private String failDesc;

    private String uploadTime;
}
