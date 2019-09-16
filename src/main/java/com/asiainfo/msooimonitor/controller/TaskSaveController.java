package com.asiainfo.msooimonitor.controller;

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

    @RequestMapping("/base93006/{lastDaySql")
    public String savebase93006(@PathVariable String lastDaySql) {
        String activityEndDate = lastDaySql.substring(0, 4) + "-" + lastDaySql.substring(4, 6) + "-" + lastDaySql.substring(6, 8);
        new Runnable() {
            @Override
            public void run() {
                taskSaveMethod.savebase93006(activityEndDate);
            }
        };
        return "success";
    }

    @RequestMapping("/marking93006/{lastDaySql")
    public String testsavemarking93006(@PathVariable String lastDaySql) {
        String activityEndDate = lastDaySql.substring(0, 4) + "/" + lastDaySql.substring(4, 6) + "/" + lastDaySql.substring(6, 8);
        new Runnable() {
            @Override
            public void run() {
                taskSaveMethod.savemarking93006(activityEndDate);
            }
        };
        return "success";
    }

    @RequestMapping("/marking93001/{summaryDate")
    public String testsaveMarking93001(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        String summaryDateBefore = TimeUtil.getTwoDaySql(new Date());
        new Runnable() {
            @Override
            public void run() {
                taskSaveMethod.saveMarking93001(activityEndDate, summaryDate, summaryDateBefore);
            }
        };
        return "success";
    }

    @RequestMapping("/marking93005/{summaryDate")
    public String saveMarking93005(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        new Runnable() {
            @Override
            public void run() {
                taskSaveMethod.saveMarking93005(activityEndDate, summaryDate);
            }
        };
        return "success";
    }

    @RequestMapping("/base93005/{summaryDate")
    public String saveBase93005(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        new Runnable() {
            @Override
            public void run() {
                taskSaveMethod.saveBase93005(activityEndDate, summaryDate);
            }
        };
        return "success";
    }


    @RequestMapping("/marking93002/{summaryDate")
    public String savemarking93002(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        String campaignedEndTime = activityEndDate;
        new Runnable() {
            @Override
            public void run() {
                taskSaveMethod.saveMarking93002(activityEndDate, summaryDate, campaignedEndTime);
            }
        };
        return "success";
    }

    @RequestMapping("/base93002/{summaryDate")
    public String savebase93002(@PathVariable String summaryDate) {
        String activityEndDate = summaryDate.substring(0, 4) + "/" + summaryDate.substring(4, 6) + "/" + summaryDate.substring(6, 8);
        new Runnable() {
            @Override
            public void run() {
                taskSaveMethod.savebase93002(activityEndDate, summaryDate);
            }
        };
        return "success";
    }
}
