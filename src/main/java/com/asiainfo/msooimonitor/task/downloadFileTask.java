package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.service.InterfaceInfoService;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.SFTPUtils;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/** @Author H
 **@Date 2019/2/24 10:44
 * @Desc
 **/

@Component
public class downloadFileTask {

    private static final Logger logger = LoggerFactory.getLogger(downloadFileTask.class);

    @Value("{ftp.host}")
    private String host;

    @Value("{ftp.user}")
    private String user;

    @Value("{ftp.password}")
    private String password;

    @Value("{ftp.remotePath}")
    private String remotePath;

    @Value("{ftp.localPath}")
    private String localPath;

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    /**
     * 每天9点到23点 运行
     */
    @Scheduled(cron = "0 0 09-23 * * ?")
    public void downloadFile(){
        logger.info("download file from 228 !!!!!!");
        String remotePath1 = remotePath+ File.separator+"download"+File.separator+TimeUtil.getDaySql(new Date())+File.separator+"day";
        String localPath1 = localPath+ File.separator+"download"+File.separator+TimeUtil.getDaySql(new Date())+File.separator+"day";
        SFTPUtils sftp = SFTPUtils.getInstance(host, user, password);
        List<String> downloadFile = interfaceInfoService.getDownloadFile();
        if(downloadFile.size()>0){
            for (String interfaceId:
                 downloadFile) {
                HashMap<String, String> param = new HashMap<>();
                param.put("interfaceId",interfaceId);
                param.put("updateTime", TimeUtil.getDateTimeFormat(new Date()));

                if (!FileUtil.fileIsExit(localPath1,interfaceId)){
                    Boolean download = sftp.download(remotePath1, localPath1, interfaceId);
                    if (download){
                        param.put("state","1");//1文件生成成功 ，2文件上传成功 ，3文件校验成功 ，-1 失败
                        param.put("reason","文件下载成功");
                        param.put("successCount", String.valueOf(FileUtil.getFileRows(localPath1,interfaceId)));
                        param.put("fileCount",String.valueOf(FileUtil.getFileRows(localPath1,interfaceId)));
                        interfaceInfoService.saveInterfaceRecord(param);
                    }else {
                        param.put("state","-1");//1文件生成成功 ，2文件上传成功 ，3文件校验成功 ，-1 失败
                        param.put("reason","文件下载失败");
                        param.put("successCount","0");
                        param.put("fileCount","0");
                        interfaceInfoService.saveInterfaceRecord(param);
                    }
                }
            }
            }
    }
}
