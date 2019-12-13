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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author H
 * *@Date 2019/2/24 10:44
 * @Desc
 **/

@Component
@Slf4j
@RequestMapping("/download")
public class DownloadFileTask {

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
    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void downloadFile() {
        // 查询需要处理的下载接口
        List<InterfaceInfo> interfaceInfos = downloadInterfaceServic.listDownloadFileInterface();
        for (InterfaceInfo info :
                interfaceInfos) {

            try {
                String yesterday = TimeUtil.getLastDaySql(new Date());
                String lastMonth = TimeUtil.getLastMonthSql(new Date());
                String remotePathDay = path228 + File.separator + info.getInterfaceVgopPath() + File.separator + "replaceTime" + File.separator + "day";
                String remotePathMon = path228 + File.separator + info.getInterfaceVgopPath() + File.separator + "replaceTime" + File.separator + "month";
                String localPathDay = path17 + File.separator + info.getInterfaceLocalPath() + File.separator + "replaceTime" + File.separator + "day";
                String localPathMon = path17 + File.separator + info.getInterfaceLocalPath() + File.separator + "replaceTime" + File.separator + "month";

                 String interfaceId = info.getInterfaceId();
                 String tableName = info.getFileName();

                // 查询此接口成功入库的最大时间
                  String  startTime = downloadInterfaceServic.getMaxSuccessTime(interfaceId);

                if (StringUtils.isEmpty(startTime)) {
                    continue;
                }

                // 判断时间是否ok
                if (startTime.length() == 4) {
                    log.info("开始处理[{}]接口数据，开始时间[{}]，结束时间[{}]", interfaceId,startTime,lastMonth);
                    if (!TimeUtil.timeIsOk(startTime, lastMonth)) {
                        log.info("开始时间：{}大于结束时间;{}", startTime, lastMonth);
                        continue;
                    }
                } else {
                    log.info("开始处理[{}]接口数据，开始时间[{}]，结束时间[{}]", interfaceId,startTime,yesterday);
                    if (!TimeUtil.timeIsOk(startTime, yesterday)) {
                        log.info("开始时间：{}大于结束时间;{}", startTime, yesterday);
                        continue;
                    }
                }

                log.info("处理接口[{}],开始周期[{}]", interfaceId, startTime);
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
                            // 一周一次
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
                        betweenTime = TimeUtil.getBetweenMonth(startTime, lastMonth);
                        localPath = localPathMon;
                        remotePath = remotePathMon;
                        break;
                    default:
                        break;
                }

                if (betweenTime.size() > 0) {
                    for (String time :
                            betweenTime) {
                        log.info("接口：{}准备下载周期:{}文件！！！", info.getInterfaceId(), time);
                        boolean isDownload = dealInterface(interfaceId, remotePath.replace("replaceTime", time), localPath.replace("replaceTime", time), time);
                 //         boolean isDownload = false;
                        if (isDownload) {
                            log.info("接口：{}准备入库周期:{}！！！", info.getInterfaceId(), time);
                            handleData.killFile(interfaceId, localPath.replace("replaceTime", time), tableName, time);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("接口：{}入库失败！！！！：{}", info.getInterfaceId(), e);
                continue;
            }
        }
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
