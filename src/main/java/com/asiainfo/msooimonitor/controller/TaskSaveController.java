package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.task.TaskSaveMethod;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author yx
 * @date 2019/9/16  11:24
 * Description
 */
@RestController
@Slf4j
public class TaskSaveController {
    @Autowired
    TaskSaveMethod taskSaveMethod;
    @Autowired
    FileDataService fileDataService;

    @RequestMapping("/93006/{lastDaySql}")
    public String testsav93006(@PathVariable String lastDaySql) {
        String activityEndDate = lastDaySql.substring(0, 4) + "/" + lastDaySql.substring(4, 6) + "/" + lastDaySql.substring(6, 8);
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93006");
                try {
                    taskSaveMethod.savebase93006(activityEndDate);
                    taskSaveMethod.savemarking93006(activityEndDate);
                } catch (Exception e) {
                    log.error("93006 error :{}",e);
                    fileDataService.truncateTable("93006");
                }
            }
        }.run();
        return "success：请查看日志";
    }

    @RequestMapping("/93001/{summaryDate}")
    public String testsaveMarking93001(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        String summaryDateBefore = TimeUtil.getTwoDaySql(new Date());
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93006");
                try {
                    taskSaveMethod.saveMarking93001(activityEndDate, summaryDate, summaryDateBefore);
                } catch (Exception e) {
                    fileDataService.truncateTable("93006");
                }
            }
        }.run();

        ;
        return "success：请查看日志";
    }

    @RequestMapping("/93005/{summaryDate}")
    public String save93005(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93006");
                try {
                    taskSaveMethod.saveBase93005(activityEndDate, summaryDate);
                    taskSaveMethod.saveMarking93005(activityEndDate, summaryDate);
                } catch (Exception e) {
                    fileDataService.truncateTable("93006");
                }
            }
        }.run();
        return "success：请查看日志";
    }


    @RequestMapping("/93002/{summaryDate}")
    public String savemarking93002(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        String campaignedEndTime = activityEndDate;
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93006");
                try {
                    taskSaveMethod.saveMarking93002(activityEndDate, summaryDate, campaignedEndTime);
                    taskSaveMethod.savebase93002(activityEndDate, summaryDate);
                } catch (Exception e) {
                    fileDataService.truncateTable("93006");
                }
            }
        }.run();
        return "success：请查看日志";
    }

    @RequestMapping("/insertFlow")
    public String insertFlow() {
        fileDataService.insertFlow();
        return "success";
    }
}
