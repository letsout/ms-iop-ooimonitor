package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.config.SendMessage;
import com.asiainfo.msooimonitor.mapper.mysql.DownloadFileMapper;
import com.asiainfo.msooimonitor.model.datahandlemodel.CretaeFileInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.utils.FtpUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
            e.printStackTrace();
//            fileDataService.truncateTable("93006");
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
            e.printStackTrace();
//       fileDataService.truncateTable("93001");
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

    //查看每天校验文件失败的接口
    @Scheduled(cron = "0 00 14 * * ?")//每天14:00触发
    public void checkFile() {
        final String lastDaySql = TimeUtil.getLastDaySql(new Date());
        String remotePath = path228 + "/tmp";
        String loaclPath = path17 + "/tmp/" + lastDaySql;
        log.info("开始处理" + lastDaySql + "这一天的校验文件");
        try {
            boolean b = FtpUtil.downloadCheckFileFTP(remotePath, loaclPath);
            log.info("下载" + lastDaySql + "这一天的校验文件" + (b ? "成功" : "失败"));
            Set fileSet = new HashSet();
            //日接口
            fileSet.add("93001");
            fileSet.add("93002");
            fileSet.add("93004");
            fileSet.add("93005");
            fileSet.add("93006");
            fileSet.add("93011");
            //月接口
            if ("10".equals(lastDaySql.substring(6))) {
                fileSet.add("93003");
                fileSet.add("93055");
                fileSet.add("93056");
                fileSet.add("93053");
                fileSet.add("93052");
                fileSet.add("93051");
                fileSet.add("93050");
                fileSet.add("93054");
            }
            Set rSet = new HashSet(fileSet);
            File files = new File(loaclPath);
            final File[] dir = files.listFiles();
            List<Map<String, String>> result = new ArrayList();

            Map<String, String> map = null;
            InputStream inputStream = null;
            for (File file : dir) {
                String fileName = file.getName();
                log.info("文件名为："+fileName);
                boolean isCheckFile = fileName.startsWith("f");
                inputStream = new FileInputStream(file);
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                String interfaceId = fileName.substring(23, 28);
                String str;
                boolean checkResult = true;
                while ((str = bf.readLine()) != null) {
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    if (isCheckFile) {
                        if (!"00".equals(str.substring(str.length() - 2))) {
                            checkResult = false;
                        }
                    } else {
                        if (!"000000000".equals(str.substring(str.length() - 9))) {
                            checkResult = false;
                        }
                    }
                }
                /**
                 * 校验通过后移除有的接口id
                 */
                if (checkResult) {
                    if (isCheckFile) {
                        fileSet.remove(interfaceId);
                    } else {
                        rSet.remove(interfaceId);
                    }
                }
            }
            log.info(lastDaySql + "这一天的校验文件有误的接口为：" + rSet);
            rSet.addAll(fileSet);
            if (rSet.size() > 0) {
                sendMessage.sendSms(lastDaySql + "这一天接口" + rSet + "的校验文件出现异常，请检查");
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage.sendSms(lastDaySql + "这一天校验文件下载出现异常，请检查");
        }
    }
}