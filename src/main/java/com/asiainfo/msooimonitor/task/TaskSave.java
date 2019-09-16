package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.service.FileDataService;
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
    @Autowired
    FileDataService fileDataService;

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93006() {
        fileDataService.truncateTable("93006");
        try {
            final String lastDaySql = TimeUtil.getLastDaySql(new Date());
            String activityEndDate = lastDaySql.substring(0, 4) + "-" + lastDaySql.substring(4, 6) + "-" + lastDaySql.substring(6, 8);
            taskSaveMethod.savebase93006(activityEndDate);
            taskSaveMethod.savemarking93006(activityEndDate);
        } catch (Exception e) {
            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void testsave93001() {
        fileDataService.truncateTable("93006");
        try {
            final String summaryDate = TimeUtil.getLastDaySql(new Date());
            String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
            String summaryDateBefore = TimeUtil.getTwoDaySql(new Date());
            taskSaveMethod.saveMarking93001(activityEndDate, summaryDate, summaryDateBefore);
        } catch (Exception e) {
            fileDataService.truncateTable("93006");
        }
    }


    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93005() {
        fileDataService.truncateTable("93006");
        try {

            final String summaryDate = TimeUtil.getLastDaySql(new Date());
            String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
            taskSaveMethod.saveBase93005(activityEndDate, summaryDate);
            taskSaveMethod.saveMarking93005(activityEndDate, summaryDate);
        } catch (
                Exception e) {
            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void savemarking93002() {
        fileDataService.truncateTable("93006");
        try {
            final String summaryDate = TimeUtil.getLastDaySql(new Date());
            String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
            String campaignedEndTime = activityEndDate;
            taskSaveMethod.saveMarking93002(activityEndDate, summaryDate, campaignedEndTime);
            taskSaveMethod.savebase93002(activityEndDate, summaryDate);
        } catch (Exception e) {
            fileDataService.truncateTable("93006");
        }

    }

}

