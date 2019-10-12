package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.ooimodel.Result;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.utils.ResultUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
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
    TaskService taskService;
    @Autowired
    FileDataService fileDataService;

    @RequestMapping("/93004/{activityEndDate}")
    public String testsav93004(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Thread() {

            @Override
            public void run() {
                fileDataService.truncateTable("93004");
                try {
                    taskService.saveBase93004(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93004")
                                    .tableName("iop_93004")
                                    .fileName("i_13000_time_IOP-93004_" + num + "_fileNum.dat")
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
        }.start();
        return "success：请查看日志";
    }

    @RequestMapping("/93003/{month}")
    public String saveAll93003(@PathVariable String month, @RequestParam(defaultValue = "00") String num) {
        new Thread() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            @Override
            public void run() {
                fileDataService.truncateTable("93003");
                try {
                    taskService.saveAll93003(month);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93003")
                                    .tableName("iop_93003")
                                    .fileName("a_13000_time_IOP-93003_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterMonthSql(TimeUtil.strToDate(month+"10")))
                                    .step("1")
                                    .build()
                    );
                    taskService.uploadFile();
                } catch (Exception e) {
                    log.error("93003 error :{}", e);
                    fileDataService.truncateTable("93003");
                }
            }
        }.start();
        return "success：请查看日志";
    }
    @RequestMapping("/93006/{activityEndDate}")
    public String testsav93006(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Thread() {

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
        }.start();
        return "success：请查看日志";
    }

    @RequestMapping("/93001/{activityEndDate}")
    public String testsaveMarking93001(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Thread() {

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
        }.start();
        return "success：请查看日志";
    }

    @RequestMapping("/93005/{activityEndDate}/{type}")
    public String save93005(@PathVariable String activityEndDate, @PathVariable String type, @RequestParam(defaultValue = "00") String num) {
        new Thread() {

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
        }.start();
        return "success：请查看日志";
    }


    @RequestMapping("/93002/{activityEndDate}/{type}")
    public String savemarking93002(@PathVariable String activityEndDate, @PathVariable String type, @RequestParam(defaultValue = "00") String num) {
        new Thread() {

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
        }.start();
        return "success：请查看日志";
    }

    @RequestMapping("/insertFlow")
    public String insertFlow() {
        fileDataService.insertFlow();
        return "success";
    }

    @RequestMapping("/createFile")
    public String createFile() {
        new Thread() {

            @Override
            public void run() {
                taskService.uploadFile();
            }
        }.start();
        return "success";
    }

    /**
     * @param activityEndDate 上传数据活动结束日期
     * @param num             重传序号 默认00  重传之后依次加1
     * @return
     */
    @RequestMapping("/93056/{activityEndDate}")
    public String sqve93056(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

                fileDataService.truncateTable("93056");
                try {
                    Date dataMonth = sdf.parse(activityEndDate);

                    fileDataService.create93056(activityEndDate);

                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93056")
                                    .tableName("iop_93056")
                                    .fileName("i_13000_time_IOP-93056_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterMonthSql(dataMonth))
                                    .step("1")
                                    .build()
                    );
                    taskService.uploadFile();
                } catch (Exception e) {
                    log.error("93056 error :{}", e);
                    fileDataService.truncateTable("93056");
                }
            }
        }.run();
        return "success：请查看日志";
    }


    /**
     * @param activityEndDate 上传数据活动结束日期
     * @param num             重传序号 默认00  重传之后依次加1
     * @return
     */
    @RequestMapping("/93055/{activityEndDate}")
    public String sqve93055(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                fileDataService.truncateTable("93055");
                try {
                    Date dataMonth = sdf.parse(activityEndDate);
                    fileDataService.create93055(activityEndDate);

                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93055")
                                    .tableName("iop_93055")
                                    .fileName("i_13000_time_IOP-93055_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterMonthSql(dataMonth))
                                    .step("1")
                                    .build()
                    );
                    taskService.uploadFile();
                } catch (Exception e) {
                    log.error("93055 error :{}", e);
                    fileDataService.truncateTable("93055");
                }
            }
        }.run();
        return "success：请查看日志";
    }
}
