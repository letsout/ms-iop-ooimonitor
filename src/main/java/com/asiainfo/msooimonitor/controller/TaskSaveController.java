package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yx
 * @date 2019/9/16  11:24
 * Description
 */
@RestController
@Slf4j
public class TaskSaveController {
    @Autowired
    TaskService taskService;
    @Autowired
    FileDataService fileDataService;

    @RequestMapping("/93006/{activityEndDate}")
    public String testsav93006(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93006");
                try {
                    taskService.saveAll93006(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93006")
                                    .tableName("iop_93006")
                                    .fileName("a_13000_time_IOP-93006_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                    taskService.uploadFile();
                } catch (Exception e) {
                    log.error("93006 error :{}", e);
                    fileDataService.truncateTable("93006");
                }
            }
        }.run();
        return "success：请查看日志";
    }

    @RequestMapping("/93001/{activityEndDate}")
    public String testsaveMarking93001(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93001");
                try {
                    taskService.saveMarking93001(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93001")
                                    .tableName("iop_93001")
                                    .fileName("a_13000_time_IOP-93001_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                    taskService.uploadFile();
                } catch (Exception e) {
                    log.error("93001 error :{}", e);
                    fileDataService.truncateTable("93001");
                }

            }
        }.run();
        return "success：请查看日志";
    }

    @RequestMapping("/93005/{activityEndDate}/{type}")
    public String save93005(@PathVariable String activityEndDate, @PathVariable String type, @RequestParam(defaultValue = "00") String num) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93005");
                try {
                    if (type.equals("1")) {
                        taskService.saveBase93005(activityEndDate);
                    } else if (type.equals("2")) {
                        taskService.saveMarking93005(activityEndDate);
                    } else {
                        taskService.saveBase93005(activityEndDate);
                        taskService.saveMarking93005(activityEndDate);
                    }
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93005")
                                    .tableName("iop_93005")
                                    .fileName("i_13000_time_IOP-93005_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                    taskService.uploadFile();
                } catch (Exception e) {
                    log.error("93005 error :{}", e);
                    fileDataService.truncateTable("93005");
                }

            }
        }.run();
        return "success：请查看日志";
    }


    @RequestMapping("/93002/{activityEndDate}/{type}")
    public String savemarking93002(@PathVariable String activityEndDate, @PathVariable String type, @RequestParam(defaultValue = "00") String num) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93002");
                try {
                    if (type.equals("1")) {
                        taskService.saveBase93002(activityEndDate);
                    } else if (type.equals("2")) {
                        taskService.saveMarking93002(activityEndDate);
                    } else {
                        taskService.saveBase93002(activityEndDate);
                        taskService.saveMarking93002(activityEndDate);
                    }
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93002")
                                    .tableName("iop_93002")
                                    .fileName("i_13000_time_IOP-93002_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                    taskService.uploadFile();
                } catch (Exception e) {
                    log.error("93002 error :{}", e);
                    fileDataService.truncateTable("93002");
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
    @RequestMapping("/createFile")
    public String createFile() {
        taskService.uploadFile();
        return "success";
    }
}
