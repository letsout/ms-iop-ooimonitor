package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.constant.StateAndTypeConstant;
import com.asiainfo.msooimonitor.handle.HandleData;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.service.DownloadInterfaceService;
import com.asiainfo.msooimonitor.utils.FtpUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author H
 * *@Date 2019/2/24 10:44
 * @Desc
 **/

@Component
@Slf4j
public class downloadFileTask {

    @Autowired
    private DownloadInterfaceService downloadInterfaceServic;

    @Autowired
    HandleData handleData;

    @Value("${ftp.path}")
    private String path;

    /**
     * 每天9点到23点 运行
     * 下载接口分为 日接口 周接口 月接口
     * 日接口 每天一次
     * 周接口 每周一次
     * 月接口 每月一次
     */
    @Scheduled(cron = "0 0 09-23 * * ?")
    public void downloadFile() {
        // 查询需要处理的下载接口
        List<InterfaceInfo> interfaceInfos = downloadInterfaceServic.listDownloadFileInterface();
        String yesterday = TimeUtil.getLastDaySql(new Date());
        interfaceInfos.stream()
                .filter(Objects::nonNull)
                .forEach(info -> {
                    String remotePath = path + File.separator + info.getInterfaceRemotePath();
                    String loaclPath = path + File.separator + info.getInterfaceLocalPath();
                    String interfaceId = info.getInterfaceId();
                    switch (info.getInterfaceCycle()) {
                        case StateAndTypeConstant.DAY_INTERFACE:
                            dealInterface(interfaceId, remotePath, loaclPath, yesterday);
                            break;
                        case StateAndTypeConstant.WEEK_INTERFACE:
                            if (info.getInterfaceRunTime().equals(TimeUtil.getWeek())) {
                                dealInterface(interfaceId, remotePath, loaclPath, yesterday);
                            }
                            break;
                        case StateAndTypeConstant.MONTH_INTERFACE:
                            if (info.getInterfaceRunTime().equals(yesterday)) {
                                dealInterface(interfaceId, remotePath, loaclPath, yesterday);
                            }
                            break;
                        default:
                            break;
                    }
                });
    }

    /**
     * 处理文件接口信息
     *
     * @param interfaceId 接口号
     * @param remotePath  远程路径
     * @param loaclPath   本地路径
     * @param date        日期
     * @throws IOException
     * @throws RuntimeException
     */
    public void dealInterface(String interfaceId, String remotePath, String loaclPath, String date) {
        try {
            FtpUtil.downloadFileFTP(remotePath, loaclPath, interfaceId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接口：{} 文件下载失败！！！", interfaceId);

        }
        handleData.killFile(interfaceId, loaclPath, date);
    }
}
