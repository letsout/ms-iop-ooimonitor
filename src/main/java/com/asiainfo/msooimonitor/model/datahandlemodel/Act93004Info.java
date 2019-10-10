package com.asiainfo.msooimonitor.model.datahandlemodel;

import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.Data;

/**
 * @Author H
 * @Date 2019/10/8 22:32
 * @Desc
 **/
@Data
public class Act93004Info {
    private String city = "280";
    private String activityId;
    private String activityName;
    private String startTime;
    private String endTime;
    private String campaignId;
    private String campaignName;
    private String campaignStartTime;
    private String campaignEndTime;
    private String channelId;
    private String channeType;
    private String channeTypeOne;
    private String channeTypeTwo;
    private String channelName;
    private String positionId;
    private String positionidOne;
    private String positionidTwo;
    private String positionName;
    private String proName;
    private String proCode;
    private String proCodeSplite = "0428000";
    private String finalObjUserAmount;
    private String finalObjTableName;
}
