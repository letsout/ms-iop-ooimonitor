/*
package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.handle.HandleData;
import com.asiainfo.msooimonitor.handle.HandleXml;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

*/
/**
 * @Author H
 * @Date 2019/2/24 11:30
 * @Desc 接口运行
 **//*

@Component
public class UploadFileTask {

    private static final Logger logger = LoggerFactory.getLogger(UploadFileTask.class);

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @Autowired
    private HandleXml handleXml;

    @Autowired
    private HandleData handleData;


    @Scheduled(cron = "0 0 02 * * ?")
    public void uploadInterface() {
        logger.info("上传接口开始运行！！！！！");
        String remotePath1 = remotePath + File.separator + "upload" + File.separator + TimeUtil.getDaySql(new Date()) + File.separator + "day";
        String localPath1 = localPath + File.separator + "upload" + File.separator + TimeUtil.getDaySql(new Date()) + File.separator + "day";
        List<InterfaceInfo> interfaceInfos = interfaceInfoService.getuploadInterface();
        for (InterfaceInfo info :
                interfaceInfos) {

            HashMap<String, String> param = new HashMap<>();
            param.put("interfaceId", info.getInterfaceId());
            param.put("updateTime", TimeUtil.getDateTimeFormat(new Date()));

            Boolean flag = false;
            switch (info.getType()) {
                case "1":
                    logger.info("日接口{}，运行！！！", info.getInterfaceId());
                    flag = handleXml.doXml(new Date(), info.getInterfaceId(), localPath1);
                    break;
                case "2":
                    if (TimeUtil.getDay() == 10) {
                        logger.info("月接口{}，运行！！！", info.getInterfaceId());
                        flag = handleXml.doXml(new Date(), info.getInterfaceId(), localPath1);
                    }
                    break;
                case "3":
                    if (TimeUtil.getWeek() == 2) {
                        logger.info("周接口{}，运行！！！", info.getInterfaceId());
                        flag = handleXml.doXml(new Date(), info.getInterfaceId(), localPath1);
                    }
                    break;
                default:
                    break;
            }
            if (flag) {
                // 1文件生成成功 ，2文件上传成功 ，3文件校验成功 ，-1 失败
                param.put("state", "1");
                param.put("reason", "文件生成成功");
                param.put("successCount", String.valueOf(FileUtil.getFileRows(localPath1, info.getInterfaceId())));
                param.put("fileCount", String.valueOf(FileUtil.getFileRows(localPath1, info.getInterfaceId())));
                interfaceInfoService.saveInterfaceRecord(param);
            } else {
                // 1文件生成成功 ，2文件上传成功 ，3文件校验成功 ，-1 文件生成失败失败，-2 文件上传失败，-3 文件校验失败
                param.put("state", "-1");
                param.put("reason", "文件生成失败");
                param.put("successCount", "0");
                param.put("fileCount", "0");
                interfaceInfoService.saveInterfaceRecord(param);
            }
        }

    }


    */
/**
     * load接口每天9点开始
     *//*

    @Scheduled(cron = "0 0 09 * * ?")
    public void loadFile() {
        logger.info("入库接口开始运行！！！！！");
        String remotePath1 = remotePath + File.separator + "download" + File.separator + TimeUtil.getDaySql(new Date()) + File.separator + "day";
        String localPath1 = localPath + File.separator + "download" + File.separator + TimeUtil.getDaySql(new Date()) + File.separator + "day";
        List<InterfaceInfo> loadInterface = interfaceInfoService.getLoadInterface();
        for (InterfaceInfo info :
                loadInterface) {
            if (FileUtil.fileIsExit(localPath1, info.getInterfaceId())) {
                logger.info("接口{}，开始入库！！！", info.getInterfaceId());
                handleData.killFile(info.getInterfaceId(), localPath1, TimeUtil.getDaySql(new Date()));
            } else {
                logger.info("接口：{" + info.getInterfaceId() + "} 文件不存在！！！");
            }

        }

    }
}
*/
