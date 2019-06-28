package com.asiainfo.msooimonitor.model.datahandlemodel;

import java.util.HashMap;

/**
 * @Author H
 * @Date 2019/1/18 16:43
 * @Desc 93004中activityId 和对应客户群表名的关系
 **/
public class ActPh93004 {
    String activityId;
    String finalGroupTableName;


    public ActPh93004(String activityId, String finalGroupTableName) {
        this.activityId = activityId;
        this.finalGroupTableName = finalGroupTableName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String finalGroupTableName() {
        return finalGroupTableName;
    }

    public void finalGroupTableName(String finalGroupTableName) {
        this.finalGroupTableName = finalGroupTableName;
    }


    public String getReplace(String str){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("activity_id",this.getActivityId());
        hashMap.put("FINAL_GROUP_TABLE_NAME",this.finalGroupTableName());

        return hashMap.get(str);

    }


}
