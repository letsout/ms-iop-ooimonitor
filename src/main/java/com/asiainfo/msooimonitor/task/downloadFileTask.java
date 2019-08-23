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
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private String path228;

    @Value("${file.path17}")
    private String path17;

    /**
     * 每天9点到23点 运行
     * 下载接口分为 日接口 周接口 月接口
     * 日接口 每天一次
     * 周接口 每周一次
     * 月接口 每月一次
     */
    @RequestMapping("/dotask")
    @Scheduled(cron = "0 0/30 09-23 * * ?")
    public void downloadFile() {
        // 查询需要处理的下载接口
        List<InterfaceInfo> interfaceInfos = downloadInterfaceServic.listDownloadFileInterface();
        interfaceInfos.stream()
                .filter(Objects::nonNull)
                .forEach(info -> {
                    try {
                        String yesterday = TimeUtil.getLastDaySql(new Date());
                        String lastMonth = TimeUtil.getLastMonthSql(new Date());
                        String remotePathDay = path228 + File.separator + info.getInterfaceRemotePath() + File.separator + yesterday + File.separator + "day";
                        String remotePathMon = path228 + File.separator + info.getInterfaceRemotePath() + File.separator + lastMonth + File.separator + "month";
                        String localPathDay = path17 + File.separator + info.getInterfaceLocalPath() + File.separator + yesterday + File.separator + "day";
                        String localPathMon = path17 + File.separator + info.getInterfaceLocalPath() + File.separator + lastMonth + File.separator + "month";
                        String interfaceId = info.getInterfaceId();
                        // 查询此接口成功入库的最大时间
                        String successTime = downloadInterfaceServic.getMaxSuccessTime(interfaceId);
                        String startTime = TimeUtil.getAfterDay(successTime);

                        List<String> betweenTime = new ArrayList<>();
                        String localPath = "";
                        String remotePath = "";
                        switch (info.getInterfaceCycle()) {
                            case StateAndTypeConstant.DAY_INTERFACE:
                                betweenTime = TimeUtil.getBetweenDate(startTime, yesterday);
                                localPath = localPathDay;
                                remotePath = remotePathDay;
                                break;
                            case StateAndTypeConstant.WEEK_INTERFACE:
                                betweenTime = TimeUtil.getBetweenDate(startTime, yesterday);
                                List<String> list = new ArrayList<>();
                                for (String time :
                                        betweenTime) {
                                    int week = TimeUtil.getWeek(time);
                                    Integer startInt = Integer.valueOf(startTime);
                                    Integer nowInt = Integer.valueOf(time);
                                    if (week > 0 && nowInt > startInt) {
                                        list.add(time);
                                    }
                                }
                                betweenTime.clear();
                                betweenTime.addAll(list);
                                localPath = localPathDay;
                                remotePath = remotePathDay;
                                break;
                            case StateAndTypeConstant.MONTH_INTERFACE:
                                String endMonth = TimeUtil.getLastMonthSql(new Date());
                                betweenTime = TimeUtil.getBetweenMonth(startTime, endMonth);
                                localPath = localPathMon;
                                remotePath = remotePathMon;
                                break;
                            default:
                                break;
                        }

                        if (betweenTime.size() > 0) {
                            for (String time :
                                    betweenTime) {
                                boolean isDownload = dealInterface(interfaceId, remotePath, localPath, time);
                                if (isDownload) {
                                    log.info("接口：{}准备入库！！！", info.getInterfaceId());
                                    handleData.killFile(interfaceId, localPath, time);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("接口：{}入库失败！！！！", info.getInterfaceId());
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
    public boolean dealInterface(String interfaceId, String remotePath, String loaclPath, String date) throws IOException {

        return FtpUtil.downloadFileFTP(remotePath, loaclPath, interfaceId);
    }
}
