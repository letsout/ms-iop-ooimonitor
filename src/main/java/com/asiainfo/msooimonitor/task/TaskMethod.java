package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yx
 * @date 2019/9/6  17:16
 * Description
 */
@Component
@Slf4j
public class TaskMethod {

    @Autowired
    TaskService taskService;
    @Autowired
    FileDataService fileDataService;

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93006() {
        fileDataService.truncateTable("93006");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskService.saveAll93006(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93006")
                            .tableName("iop_93006")
                            .fileName("a_13000_time_IOP-93006_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            e.printStackTrace();
//            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void testsave93001() {
        fileDataService.truncateTable("93001");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskService.saveMarking93001(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93001")
                            .tableName("iop_93001")
                            .fileName("a_13000_time_IOP-93001_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            e.printStackTrace();
//       fileDataService.truncateTable("93001");
        }
    }


    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93005() {
        fileDataService.truncateTable("93005");
        try {

            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskService.saveBase93005(activityEndDate);
            taskService.saveMarking93005(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93005")
                            .tableName("iop_93005")
                            .fileName("i_13000_time_IOP-93005_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            e.printStackTrace();
//       fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void savemarking93002() {
        fileDataService.truncateTable("93002");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskService.saveMarking93002(activityEndDate);
            taskService.saveBase93002(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93002")
                            .tableName("iop_93002")
                            .fileName("i_13000_time_IOP-93002_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            e.printStackTrace();
//      fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 50 9 * * ?")//每天09:50触发
    public void insertFlow() {
        fileDataService.insertFlow();
    }

    @Scheduled(cron = "0 00 10 5 * ?")//每月5号10:00触发
    public void save93055(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        fileDataService.truncateTable("93055");
        try {
            String lastMonthSql = TimeUtil.getLastMonthSql(new Date());
            fileDataService.create93055(lastMonthSql);

            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93055")
                            .tableName("iop_93055")
                            .fileName("i_13000_time_IOP-93055_" + 00 + "_fileNum.dat")
                            .dataTime(sdf.format(new Date()))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("93055 error :{}", e);
            fileDataService.truncateTable("93055");
        }
    }

    @Scheduled(cron = "0 00 10 5 * ?")//每月5号10:00触发
    public void save93056(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        fileDataService.truncateTable("93056");
        try {
            String lastMonthSql = TimeUtil.getLastMonthSql(new Date());

            fileDataService.create93056(lastMonthSql);

            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93056")
                            .tableName("iop_93056")
                            .fileName("i_13000_time_IOP-93056_" + 00 + "_fileNum.dat")
                            .dataTime(sdf.format(new Date()))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("93056 error :{}", e);
            fileDataService.truncateTable("93056");
        }
    }
}

