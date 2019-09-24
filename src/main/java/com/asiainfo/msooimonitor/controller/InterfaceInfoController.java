package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.constant.StateAndTypeConstant;
import com.asiainfo.msooimonitor.handle.HandleData;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.Result;
import com.asiainfo.msooimonitor.service.InterfaceInfoService;
import com.asiainfo.msooimonitor.task.DownloadFileTask;
import com.asiainfo.msooimonitor.utils.ResultUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @Author H
 * @Date 2019/2/22 16:18
 * @Desc
 **/
@RestController
@Slf4j
public class InterfaceInfoController {

    @Autowired
    InterfaceInfoService interfaceInfoService;

    @Autowired
    DownloadFileTask downloadFileTask;

    @Autowired
    HandleData handleData;

    @Value("${ftp.path}")
    private String path228;

    @Value("${file.path17}")
    private String path17;

    @RequestMapping("/getInterfaceInfo")
    public Result getInterfaceInfo(@RequestParam(value = "interfaceId", required = false) String interfaceId,
                                   @RequestParam(value = "interfaceType", required = false) String interfaceType,
                                   @RequestParam(value = "interfaceDealType", required = false) String interfaceDealType,
                                   @RequestParam(value = "interfaceCycle", required = false) String interfaceCycle,
                                   @RequestParam(value = "fuzzyQueryInfo", required = false) String fuzzyQueryInfo,
                                   @RequestParam("page") int pageNum,
                                   @RequestParam("limit") int pageSize) {

        InterfaceInfo serachFilter = new InterfaceInfo();
        serachFilter.setInterfaceId(interfaceId);
        serachFilter.setInterfaceType(interfaceType);
        serachFilter.setInterfaceDealType(interfaceDealType);
        serachFilter.setInterfaceCycle(interfaceCycle);
        serachFilter.setFuzzyQueryInfo(fuzzyQueryInfo);
        PageInfo<InterfaceInfo> interfaceInfo = interfaceInfoService.getInterfaceInfo(serachFilter, pageNum, pageSize);

        return ResultUtil.success(interfaceInfo.getList(), interfaceInfo.getTotal());
    }


    @RequestMapping("/deleteInterfaceInfoById")
    public Result deleteInterfaceInfoById(@RequestParam("interfaceId") String interfaceId) {
        interfaceInfoService.deleteInterfaceInfoById(interfaceId);
        return ResultUtil.success();
    }


    @RequestMapping("/updateInterfaceInfoById")
    public Result updateInterfaceInfoById(@RequestBody InterfaceInfo interfaceInfo) {
        interfaceInfoService.updateInterfaceInfoById(interfaceInfo);
        return ResultUtil.success();
    }

    @RequestMapping("/insertInterfaceInfo")
    public Result insertInterfaceInfo(@RequestBody InterfaceInfo interfaceInfo) {
        int isHave = interfaceInfoService.thisIdIsHave(interfaceInfo.getInterfaceId());
        if (isHave > 0) {
            return ResultUtil.error("接口编码已存在");
        }
        interfaceInfoService.insertInterfaceInfo(interfaceInfo);
        return ResultUtil.success();
    }


    @RequestMapping("/runInterface")
    public Result runInterface(@RequestParam("interfaceId") String interfaceId, @RequestParam("time") String time, @RequestParam("step") String step) {
        try {
            if (StateAndTypeConstant.FILE_UPLOAD_OR_RK.equals(step)) {
                InterfaceInfo interfaceInfo = interfaceInfoService.listInterfaceInfoById(interfaceId);
                String tableName = interfaceInfo.getFileName();
                String remotePath = "";
                String localPath = "";
                if (StateAndTypeConstant.MONTH_INTERFACE.equals(interfaceInfo.getInterfaceCycle())) {
                    remotePath = path228 + File.separator + interfaceInfo.getInterfaceLocalPath() + File.separator + time + File.separator + "month";
                    localPath = path17 + File.separator + interfaceInfo.getInterfaceLocalPath() + File.separator + time + File.separator + "month";
                } else  {
                    remotePath = path228 + File.separator + interfaceInfo.getInterfaceLocalPath() + File.separator + time + File.separator + "day";
                    localPath = path17 + File.separator + interfaceInfo.getInterfaceLocalPath() + File.separator + time + File.separator + "day";
                }
                log.info("接口：{}准备下载周期:{}文件！！！", interfaceInfo.getInterfaceId(), time);
                boolean isDownload = downloadFileTask.dealInterface(interfaceId, remotePath, localPath, time);
                if (isDownload) {
                    log.info("接口：{}准备入库周期:{}！！！", interfaceInfo.getInterfaceId(), time);
                    handleData.killFile(interfaceId, localPath.replace("replaceTime", time), tableName, time);
                }
            }
        } catch (Exception e) {
            log.error("重跑数据异常！！！[{}]", e);
            return ResultUtil.error("重跑数据异常" + e.getMessage());
        }
        return ResultUtil.success();
    }

}