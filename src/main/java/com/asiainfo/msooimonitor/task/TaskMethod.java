package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.config.SendMessage;
import com.asiainfo.msooimonitor.mapper.mysql.DownloadFileMapper;
import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yx
 * @date 2019/9/6  17:16
 * Description
 */
@Component
@Slf4j
public class TaskMethod {

    @Autowired
    TaskService taskServices;
    @Autowired
    FileDataService fileDataService;
    @Autowired
    DownloadFileMapper downloadFileMapper;
    @Autowired
    SendMessage sendMessage;
    @Value("${ftp.path}")
    private String path228;

    @Value("${file.path17}")
    private String path17;

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93006() {
        fileDataService.truncateTable("93006");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskServices.saveAll93006(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93006")
                            .tableName("iop_93006")
                            .fileName("a_13000_time_IOP-93006_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
            sendMessage.sendFileUploadSuccess("93006");
        } catch (Exception e) {
            sendMessage.sendFileUploadFail("93006");
            log.error("运行异常：" + e);
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93011() {
        fileDataService.truncateTable("93011");
        try {
            final String date = TimeUtil.getLastDaySql(new Date());
            taskServices.saveAll93011(date);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93011")
                            .tableName("iop_93011")
                            .fileName("i_13000_time_IOP-93011_00_fileNum.dat")
                            .dataTime(date)
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93011接口运行异常");

            log.error("运行异常：" + e);
            e.printStackTrace();
        }
    }

    //    @Scheduled(cron = "0 00 23 * * ?")//每天23:00触发
    public void save93004() {
        fileDataService.truncateTable("93006");
        try {
            final String activityEndDate = TimeUtil.getDaySql(new Date());
            taskServices.saveBase93004(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93004")
                            .tableName("iop_93004")
                            .fileName("i_13000_time_IOP-93004_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93004接口运行异常");
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93001() {
        fileDataService.truncateTable("93001");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskServices.saveMarking93001(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93001")
                            .tableName("iop_93001")
                            .fileName("a_13000_time_IOP-93001_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93001接口运行异常");
            e.printStackTrace();
        }
    }


    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void save93005() {
        fileDataService.truncateTable("93005");
        try {

            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskServices.saveBase93005(activityEndDate);
            taskServices.saveMarking93005(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93005")
                            .tableName("iop_93005")
                            .fileName("i_13000_time_IOP-93005_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
            sendMessage.sendFileUploadSuccess("93005");

        } catch (Exception e) {
            sendMessage.sendFileUploadFail("93005");
            log.error("运行异常：" + e);
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 00 10 * * ?")//每天10:00触发
    public void saveMarking93002() {
        fileDataService.truncateTable("93002");
        try {
            final String activityEndDate = TimeUtil.getTwoDaySql(new Date());
            taskServices.saveMarking93002(activityEndDate);
            taskServices.saveBase93002(activityEndDate);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93002")
                            .tableName("iop_93002")
                            .fileName("i_13000_time_IOP-93002_00_fileNum.dat")
                            .dataTime(TimeUtil.getAfterDay(activityEndDate))
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93002接口运行异常");
            log.error("运行异常：" + e);
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 50 9 * * ?")//每天09:50触发
    public void insertFlow() {
        log.info("插入当前新建活动的流程信息");
        fileDataService.insertFlow();
    }

    @Scheduled(cron = "0 00 10 5 * ?")//每月5号10:00触发
    public void save93055() {
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
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93055接口运行异常");
            log.error("93055 error :{}", e);
            fileDataService.truncateTable("93055");
        }
    }

    @Scheduled(cron = "0 00 10 5 * ?")//每月5号10:00触发
    public void save93056() {

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
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93056接口运行异常");
            log.error("93056 error :{}", e);
            fileDataService.truncateTable("93056");
        }
    }

    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void save93003() {
        fileDataService.truncateTable("93003");
        try {
            String month = TimeUtil.getLastMonthSql(new Date());
            taskServices.saveAll93003(month);
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93003")
                            .tableName("iop_93003")
                            .fileName("a_13000_time_IOP-93003_00_fileNum.dat")
                            .dataTime(month)
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93003接口运行异常");
            log.error("93056 error :{}", e);
            fileDataService.truncateTable("93003");
        }
    }

    // 两级标签互动省侧上传优秀标签数据
    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void save93052OR93053() {
        //   fileDataService.truncateTable("93051");
        try {
            taskServices.saveAll93052OR93053();
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("9352OR93053接口运行异常");
            log.error("save93052OR93053 error :{}", e);
        }
    }

    // 两级标签互动省侧上传集团下发任务标签
    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void saveAll93050OR93051() {
        //   fileDataService.truncateTable("93050");
        try {
            taskServices.saveAll93050OR93051();
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93050OR93051接口运行异常");
            log.error("93050 error :{}", e);
            //  fileDataService.truncateTable("93050");
        }
    }

    //标签引用次数同步接口
    @Scheduled(cron = "0 00 00 10 * ?")//每月10号00:00触发
    public void save93054() {
        fileDataService.truncateTable("93054");
        try {
            taskServices.saveAll93054();
            fileDataService.insertInterfaceRelTable(
                    CretaeFileInfo.builder()
                            .interfaceId("93054")
                            .tableName("iop_93054")
                            .fileName("i_280_time_IOP-93054_fileNum.dat")
                            .dataTime(TimeUtil.getLastDaySql(new Date()))
                            .step("1")
                            .build()
            );
            taskServices.uploadFile();
        } catch (Exception e) {
            sendMessage.sendSms("93054接口运行异常");
            log.error("93054 error :{}", e);
            fileDataService.truncateTable("93054");
        }
    }

    //查看每天上传失败的接口
    @Scheduled(cron = "0 00 12 * * ?")//每天12:00触发
    public void getUploadEror() {
        String date = TimeUtil.getLastDaySql(new Date());
        List<InterfaceRecord> uploadEror = downloadFileMapper.getUploadEror(date);
        if ("10".equals(date.substring(6, 8)) || "09".equals(date.substring(6, 8))) {
            uploadEror.addAll(downloadFileMapper.getUploadEror(date.substring(0, 4)));
        }
        String interfaceIds = "";
        if (uploadEror.size() == 0) {
            return;
        } else {
            for (InterfaceRecord record : uploadEror) {
                interfaceIds += "," + record.getInterfaceId();
            }
            sendMessage.sendSms(date + "当天的" + interfaceIds.substring(1) + "接口上传失败");
        }
    }

    /**
     * 查看每天校验文件失败的接口
     */
    @Scheduled(cron = "0 15 10 * * ?")//每天10:15触发
    public void checkFile() {
        final String lastDaySql = TimeUtil.getLastDaySql(new Date());
        Set<String> set = new HashSet<>();

        /**
         * 这里暂时只加考核的两个接口
         */
        set.add("93005");
        set.add("93006");
        taskServices.checkFile(lastDaySql, set);
    }

    /**
     * 每天检验国信的数据是否存在有同一个活动每一天有两条效果数据的情况
     */
    @Scheduled(cron = "0 55 09 * * ?")//每天9:55触发
    public void getMaxCount() {
        taskServices.getMaxCount();
    }
}