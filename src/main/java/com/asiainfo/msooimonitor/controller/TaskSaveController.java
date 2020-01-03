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
import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/16  11:24
 * Description 手动补传的原则数据数据原则是需要补传那天的数据就输入那一天日期：数据日期是文件日期的前一个日期，比如11号要传的是10号的我呢见9号的数据，那么应该输入9号，93011除外
 */
@RestController
@Slf4j
public class TaskSaveController {
    @Autowired
    TaskService taskServices;
    @Autowired
    FileDataService fileDataService;

    @RequestMapping("/getCheckFile")
    public Result getCheckFile(@RequestParam(value = "fileDate", required = false) String fileDate) throws Exception {
        if (StringUtils.isEmpty(fileDate)) {
            fileDate = TimeUtil.getDaySql(new Date());
        }
        List<Map<String, String>> list = taskServices.getCheckFileByDate(fileDate);
        log.info("需要获取校验文件的日期{}", fileDate);
        return ResultUtil.success(list, list.size());
    }

    @RequestMapping("/93004/{activityEndDate}")
    public String saveAll93004(@PathVariable String activityEndDate, @RequestParam(defaultValue = "00") String num) {
        new Thread() {

            @Override
            public void run() {
                fileDataService.truncateTable("93004");
                try {
                    taskServices.saveBase93004(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93004")
                                    .tableName("iop_93004")
                                    .fileName("i_13000_time_IOP-93004_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93006 error :{}", e);
                }
            }
        }.start();
        return "success：请查看日志";
    }

    /**
     * 日期指决定文件的日期，与数据无关，如果补昨天的数据就直接传昨天的日期就行
     *
     * @param date
     * @param num
     * @return
     */
    @RequestMapping("/93011/{activityEndDate}")
    public String saveAll93011(@PathVariable String date, @RequestParam(defaultValue = "00") String num) {
        new Thread() {

            @Override
            public void run() {
                fileDataService.truncateTable("93011");
                try {
                    taskServices.saveAll93011(date);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93011")
                                    .tableName("iop_93011")
                                    .fileName("i_13000_time_IOP-93011_" + num + "_fileNum.dat")
                                    .dataTime(date)
                                    .step("1")
                                    .build()
                    );
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93011 error :{}", e);
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
                    taskServices.saveAll93003(month);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93003")
                                    .tableName("iop_93003")
                                    .fileName("a_13000_time_IOP-93003_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterMonthSql(TimeUtil.strToDate(month + "10")))
                                    .step("1")
                                    .build()
                    );
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93003 error :{}", e);
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
                    taskServices.saveAll93006(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93006")
                                    .tableName("iop_93006")
                                    .fileName("a_13000_time_IOP-93006_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93006 error :{}", e);
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
                    taskServices.saveMarking93001(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93001")
                                    .tableName("iop_93001")
                                    .fileName("a_13000_time_IOP-93001_" + num + "_fileNum.dat")
                                    .dataTime(TimeUtil.getAfterDay(activityEndDate))
                                    .step("1")
                                    .build()
                    );
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93001 error :{}", e);
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
                        taskServices.saveBase93005(activityEndDate);
                    } else if (type.equals("2")) {
                        taskServices.saveMarking93005(activityEndDate);
                    } else {
                        taskServices.saveBase93005(activityEndDate);
                        taskServices.saveMarking93005(activityEndDate);
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
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93005 error :{}", e);
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
                        taskServices.saveBase93002(activityEndDate);
                    } else if (type.equals("2")) {
                        taskServices.saveMarking93002(activityEndDate);
                    } else {
                        taskServices.saveBase93002(activityEndDate);
                        taskServices.saveMarking93002(activityEndDate);
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
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93002 error :{}", e);
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
                taskServices.uploadFile();
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
                fileDataService.truncateTable("93056");
                try {
                    fileDataService.create93056(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93056")
                                    .tableName("iop_93056")
                                    .fileName("i_13000_time_IOP-93056_" + num + "_fileNum.dat")
                                    .dataTime(activityEndDate)
                                    .step("1")
                                    .build()
                    );
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93056 error :{}", e);
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
                fileDataService.truncateTable("93055");
                try {
                    fileDataService.create93055(activityEndDate);
                    fileDataService.insertInterfaceRelTable(
                            CretaeFileInfo.builder()
                                    .interfaceId("93055")
                                    .tableName("iop_93055")
                                    .fileName("i_13000_time_IOP-93055_" + num + "_fileNum.dat")
                                    .dataTime(activityEndDate)
                                    .step("1")
                                    .build()
                    );
                    taskServices.uploadFile();
                } catch (Exception e) {
                    log.error("93055 error :{}", e);
                }
            }
        }.run();
        return "success：请查看日志";
    }

    @RequestMapping("/checkFile/{date}")
    public String checkFile(@PathVariable String date) {
        taskServices.checkFile(date);
        return "校验成功，请查看短信，校验的日期为：" + date;
    }
}
