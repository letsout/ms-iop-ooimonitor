package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author yx
 * @date 2019/9/6  17:16
 * Description
 */
@Component
@Slf4j
public class TaskSave {

    @Autowired
    TaskSaveMethod taskSaveMethod;

    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void savebase93006() {
        final String lastDaySql = TimeUtil.getLastDaySql(new Date());
        String activityEndDate = lastDaySql.substring(0, 4) + "-" + lastDaySql.substring(4, 6) + "-" + lastDaySql.substring(6, 8);
        taskSaveMethod.savebase93006(activityEndDate);
    }

    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void testsavemarking93006() {
        final String lastDaySql = TimeUtil.getLastDaySql(new Date());
        String activityEndDate = lastDaySql.substring(0, 4) + "/" + lastDaySql.substring(4, 6) + "/" + lastDaySql.substring(6, 8);
        taskSaveMethod.savemarking93006(activityEndDate);
    }

    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void testsaveMarking93001() {
        final String summaryDate = TimeUtil.getLastDaySql(new Date());
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        String summaryDateBefore = TimeUtil.getTwoDaySql(new Date());
        taskSaveMethod.saveMarking93001(activityEndDate, summaryDate, summaryDateBefore);
    }

    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void saveMarking93005() {
        final String summaryDate = TimeUtil.getLastDaySql(new Date());
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        taskSaveMethod.saveMarking93005(activityEndDate, summaryDate);
    }

    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void saveBase93005() {
        final String summaryDate = TimeUtil.getLastDaySql(new Date());
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        taskSaveMethod.saveBase93005(activityEndDate, summaryDate);
    }

    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void savemarking93002() {
        final String summaryDate = TimeUtil.getLastDaySql(new Date());
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        String campaignedEndTime = activityEndDate;
        taskSaveMethod.saveMarking93002(activityEndDate, summaryDate, campaignedEndTime);

    }

    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void savebase93002() {
        final String summaryDate = TimeUtil.getLastDaySql(new Date());
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        taskSaveMethod.savebase93002(activityEndDate, summaryDate);
    }

}

