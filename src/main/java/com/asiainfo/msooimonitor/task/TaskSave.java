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
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskSaveMethod.savebase93006(activityEndDate);
            taskSaveMethod.saveMarking93006(activityEndDate);
        } catch (Exception e) {
            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void testsave93001() {
        fileDataService.truncateTable("93006");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskSaveMethod.saveMarking93001(activityEndDate);
        } catch (Exception e) {
            fileDataService.truncateTable("93006");
        }
    }


    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93005() {
        fileDataService.truncateTable("93006");
        try {

            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskSaveMethod.saveBase93005(activityEndDate);
            taskSaveMethod.saveMarking93005(activityEndDate);
        } catch (
                Exception e) {
            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void savemarking93002() {
        fileDataService.truncateTable("93006");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskSaveMethod.saveMarking93002(activityEndDate);
            taskSaveMethod.savebase93002(activityEndDate);
        } catch (Exception e) {
            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 50 9 * * ?")//每天09:50触发
    public void insertFlow() {
        fileDataService.insertFlow();
    }

}

