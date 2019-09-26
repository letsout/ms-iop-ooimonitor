package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.task.TaskSaveMethod;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

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

    @RequestMapping("/93006/{activityEndDate}/{type}")
    public String testsav93006(@PathVariable String activityEndDate, @PathVariable String type) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93006");
                try {
                    if (type.equals("1")) {
                        taskSaveMethod.savebase93006(activityEndDate);
                    } else if (type.equals("2")) {
                        taskSaveMethod.saveMarking93006(activityEndDate);
                    } else {
                        taskSaveMethod.savebase93006(activityEndDate);
                        taskSaveMethod.saveMarking93006(activityEndDate);
                    }
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93006")
                                    .tableName("iop_93006")
                                    .fileName("i_13000_time_IOP-93006_00_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                } catch (Exception e) {
                    log.error("93006 error :{}", e);
                    fileDataService.truncateTable("93006");
                }
            }
        }.run();
        return "success：请查看日志";
    }

    @RequestMapping("/93001/{activityEndDate}")
    public String testsaveMarking93001(@PathVariable String activityEndDate) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93001");
                try {
                    taskSaveMethod.saveMarking93001(activityEndDate);
                } catch (Exception e) {
                    log.error("93001 error :{}", e);
                    fileDataService.truncateTable("93001");
                }
                try {
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93001")
                                    .tableName("iop_93001")
                                    .fileName("i_13000_time_IOP-93001_00_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }.run();
        return "success：请查看日志";
    }

    @RequestMapping("/93005/{activityEndDate}/{type}")
    public String save93005(@PathVariable String activityEndDate, @PathVariable String type) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93005");
                try {
                    if (type.equals("1")) {
                        taskSaveMethod.saveBase93005(activityEndDate);
                    } else if (type.equals("2")) {
                        taskSaveMethod.saveMarking93005(activityEndDate);
                    } else {
                        taskSaveMethod.saveBase93005(activityEndDate);
                        taskSaveMethod.saveMarking93005(activityEndDate);
                    }
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93005")
                                    .tableName("iop_93005")
                                    .fileName("i_13000_time_IOP-93005_00_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                } catch (Exception e) {
                    log.error("93005 error :{}", e);
                    fileDataService.truncateTable("93005");
                }

            }
        }.run();
        return "success：请查看日志";
    }


    @RequestMapping("/93002/{activityEndDate}/{type}")
    public String savemarking93002(@PathVariable String activityEndDate, @PathVariable String type) {
        new Runnable() {
            @Override
            public void run() {
                fileDataService.truncateTable("93002");
                try {
                    if (type.equals("1")) {
                        taskSaveMethod.saveBase93002(activityEndDate);
                    } else if (type.equals("2")) {
                        taskSaveMethod.saveMarking93002(activityEndDate);
                    } else {
                        taskSaveMethod.saveBase93002(activityEndDate);
                        taskSaveMethod.saveMarking93002(activityEndDate);
                    }
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93002")
                                    .tableName("iop_93002")
                                    .fileName("i_13000_time_IOP-93002_00_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
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
}
