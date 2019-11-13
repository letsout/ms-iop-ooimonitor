package com.asiainfo.msooimonitor.model.datahandlemodel;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

/**
 * @Author H
 * @Date 2019/1/18 16:43
 * @Desc 93004中activityId 和对应客户群表名的关系
 **/
@Data
@Builder
public class ActPh93004 {
    String activityId;
    String finalGroupTableName;
    int b;
    ActivityProcessInfo activityProcessInfo;

}
