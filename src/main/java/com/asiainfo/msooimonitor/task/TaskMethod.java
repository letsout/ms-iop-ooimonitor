package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

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
            log.error("运行异常：" + e);
            e.printStackTrace();

//            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93011() {
        fileDataService.truncateTable("93011");
        try {
            final String date = TimeUtil.getLastDaySql(new Date());
            taskService.saveAll93011(date);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93011")
                            .tableName("iop_93011")
                            .fileName("i_13000_time_IOP-93011_00_fileNum.dat")
                            .dataTime(date)
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("运行异常：" + e);
            e.printStackTrace();

//            fileDataService.truncateTable("93006");
        }
    }

    //    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void save93004() {
        fileDataService.truncateTable("93006");
        try {
            final String activityEndDate = TimeUtil.getDaySql(new Date());
            taskService.saveBase93004(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93004")
                            .tableName("iop_93004")
                            .fileName("i_13000_time_IOP-93004_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("运行异常：" + e);
            e.printStackTrace();

//            fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93001() {
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
            log.error("运行异常：" + e);
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
            log.error("运行异常：" + e);
            e.printStackTrace();
//       fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void saveMarking93002() {
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
            log.error("运行异常：" + e);
            e.printStackTrace();

//      fileDataService.truncateTable("93006");
        }
    }

    @Scheduled(cron = "0 50 9 * * ?")//每天09:50触发
    public void insertFlow() {
        log.info("插入当前新建活动的流程信息");
        fileDataService.insertFlow();
    }

    @Scheduled(cron = "0 00 10 5 * ?")//每月5号10:00触发
    public void save93055() {
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
                            .dataTime(lastMonthSql)
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
    public void save93056() {
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
                            .dataTime(lastMonthSql)
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("93056 error :{}", e);
            fileDataService.truncateTable("93056");
        }
    }

    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void save93003() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        fileDataService.truncateTable("93003");
        try {
            String month = TimeUtil.getLastMonthSql(new Date());

            taskService.saveAll93003(month);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93003")
                            .tableName("iop_93003")
                            .fileName("a_13000_time_IOP-93003_00_fileNum.dat")
                            .dataTime(month)
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("93056 error :{}", e);
            fileDataService.truncateTable("93056");
        }
    }

    // 两级标签互动省侧上传优秀标签数据
    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void save93052OR93053(){
     //   fileDataService.truncateTable("93051");
        try {
            taskService.saveAll93052OR93053();
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("save93052OR93053 error :{}", e);
        }
    }

    // 两级标签互动省侧上传集团下发任务标签
    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void saveAll93050OR93051(){
     //   fileDataService.truncateTable("93050");
        try {
            taskService.saveAll93050OR93051();
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("93050 error :{}", e);
          //  fileDataService.truncateTable("93050");
        }
    }

    //标签引用次数同步接口
    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void save93054(){
          fileDataService.truncateTable("93054");
        try {
            taskService.saveAll93054();
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93054")
                            .tableName("iop_93054")
                            .fileName("i_280_time_IOP-93054_fileNum.dat")
                            .dataTime(TimeUtil.getLastDaySql(new Date()))
                            .step("1")
                            .build()
            );
            taskService.uploadFile();
        } catch (Exception e) {
            log.error("93054 error :{}", e);
            fileDataService.truncateTable("93054");
        }
    }
}

