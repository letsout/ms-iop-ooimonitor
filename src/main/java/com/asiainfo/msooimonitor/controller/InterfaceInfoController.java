package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.handle.HandleData;
import com.asiainfo.msooimonitor.handle.HandleXml;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.model.ooimodel.Result;
import com.asiainfo.msooimonitor.service.InterfaceInfoService;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.ResultUtil;
import com.asiainfo.msooimonitor.utils.SFTPUtils;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/2/22 16:18
 * @Desc
 **/
@RestController
public class InterfaceInfoController {

    private static final Logger logger = LoggerFactory.getLogger(InterfaceInfoController.class);

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @Autowired
    private HandleXml handelXml;

    @Autowired
    private HandleData handleData;

    @Value("${ftp.remotePath}")
    private String remotePath;

    @Value("${ftp.localPath}")
    private String localPath;

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.user}")
    private String user;

    @Value("${ftp.password}")
    private String password;



    /**
     * @param page  当前页数
     * @param limit 每页展示记录条数
     * @param str   参数(模糊查询时使用)
     * @return
     */
    @GetMapping("/interfaceInfo")
    public Result getInterfaceInfo(@RequestParam("page") int page,
                                   @RequestParam("limit") int limit,
                                   @RequestParam(name = "str", required = false) String str) {

        //传给数据库的参数
        Map params = new HashMap<String, Object>();

        if (str != null && !"".equals(str)) {
            params.put("str", str);
        }

        //page,limit获取
        params.put("page", ((page - 1) * limit));
        params.put("limit", limit);

        PageInfo<InterfaceInfo> interfaceInfo = interfaceInfoService.getInterfaceInfo(params);

        Result<Object> result = new Result<>();
        result.setCode(0);
        result.setCount((int) interfaceInfo.getTotal());
        result.setData(interfaceInfo.getList());

        return result;
    }


    @GetMapping("/checkInterface")
    public Result checkInterface(@RequestParam("interfaceId") String interfaceId) {

        Boolean flag = interfaceInfoService.checkInterface(interfaceId);

        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.error("接口号已存在！！！");
    }

    @PostMapping("/saveInterfaceInfo")
    public Result saveInterfaceInfo(@RequestBody InterfaceInfo interfaceInfo) {

        if (interfaceInfo == null) {
            return ResultUtil.error("参数传递错误！！！！");
        }
        interfaceInfoService.saveInterfceInfo(interfaceInfo);
        return ResultUtil.success();
    }


    @DeleteMapping("/deleteInterfaceId/{interfaceId}")
    public Result deleteInterfaceId(@PathVariable("interfaceId") String interfaceId) {

        interfaceInfoService.deleteInterfaceId(interfaceId);

        return ResultUtil.success();
    }

    @PostMapping("editInterfaceInfo")
    public Result editInterfaceInfo(@RequestBody InterfaceInfo interfaceInfo) {

        interfaceInfoService.editInterfaceInfo(interfaceInfo);

        return ResultUtil.success();
    }

    @GetMapping("/run")
    public Result run(@RequestParam(value = "interfaceId") String interfaceId,
                      @RequestParam(value = "updateTime") String updateTime) {


        String date = updateTime.replace("-", "");
        String remotePath1 = remotePath + File.separator + "download" + File.separator + date + File.separator + "day";
        String localPath1 = localPath + File.separator + "upload" + File.separator + date + File.separator + "day";
        logger.info("localPath:{}", remotePath1);

        String type = interfaceInfoService.getInterfaceIdType(interfaceId);
        if ("2".equals(type)) {
            logger.info("下载接口！！！{}", interfaceId);

            // 从228上获取文件



            handleData.killFile(interfaceId, remotePath1, date);
        } else if ("1".equals(type)) {

            logger.info("上传接口！！！{}", interfaceId);

            boolean doXml = handelXml.doXml(TimeUtil.str2Date(date, "yyyyMMdd"), interfaceId, localPath1);

            HashMap<String, Object> param = new HashMap<>();

            if(doXml){
                // 1文件生成成功 ，2文件上传成功 ，3文件校验成功 ，-1 失败
                param.put("state","1");
                param.put("reason","文件生成成功");
                param.put("successCount", String.valueOf(FileUtil.getFileRows(localPath1,interfaceId)));
                param.put("fileCount",String.valueOf(FileUtil.getFileRows(localPath1,interfaceId)));
            }else {
                // 1文件生成成功 ，2文件上传成功 ，3文件校验成功 ，-1 文件生成失败失败，-2 文件上传失败，-3 文件校验失败
                param.put("state","-1");
                param.put("reason","文件生成失败");
                param.put("successCount", "0");
                param.put("fileCount","0");
            }

            interfaceInfoService.saveInterfaceRecord(param);

            // sftp 从17传送至228 接口号-重传序号
            SFTPUtils instance = SFTPUtils.getInstance(host, user, password);



        } else {
            logger.info("无此类接口请重新输入！！！！{}", interfaceId);
            ResultUtil.error("无此类接口请重新输入！！！！" + interfaceId);
        }
        return ResultUtil.success();
    }


    @GetMapping("/sftp")
    public Result sftp(@RequestParam("time") String time) {
        String date = time.replace("-", "");
        String remotePath1 = remotePath + File.separator + "download" + File.separator + date + File.separator + "day";
        String localPath1 = localPath + File.separator + "upload" + File.separator + date + File.separator + "day";


        logger.info("remotePath:{}", remotePath1);
        logger.info("localPath:{}", localPath1);
        SFTPUtils instance = SFTPUtils.getInstance(host, user, password);

        logger.info("开始下载文件夹：{}，到本地：{}",remotePath1,localPath1);
        instance.downloadDir(remotePath1, localPath1);

        logger.info("开始上传文件");
        instance.upload(localPath1,remotePath1);
        return ResultUtil.success();
    }

    @GetMapping("/interfaceRecord")
    public Result getInterfaceRecord(@RequestParam("page") int page,
                                     @RequestParam("limit") int limit,
                                     @RequestParam(name = "str", required = false) String str) {

        //传给数据库的参数
        Map params = new HashMap<String, Object>();

        if (str != null && !"".equals(str)) {
            params.put("str", str);
        }

        int start = (page - 1) * limit;
        int end = start + limit;
        params.put("start", start);
        params.put("end", end);

        List<InterfaceRecord> interfaceRecord = interfaceInfoService.getInterfaceRecord(params);

        Result result;
        if (interfaceRecord.size() == 0) {
            result = ResultUtil.success("暂无此接口数据信息，请核对后查阅！！！");
        }
        result = ResultUtil.success(interfaceRecord);

        return result;
    }

    @GetMapping("/interfaceRecord/find")
    public Result find(int page, int limit, String interfaceId, String updateType, String isSuccess, String updateTime) {


        int start = (page - 1) * limit;
        int end = start + limit;
     /*   Map<String,Object> params = new HashMap<>();
        params.put("start",start);
        params.put("end",end);
        params.put("interfaceId",interfaceId);
        params.put("updateType",updateType);
        params.put("state",isSuccess);
        params.put("updateTime",updateTime.replace("-",""));
*/

        List<InterfaceRecord> interfaceRecord = interfaceInfoService.getInterfaceRecordByParam(start, end, interfaceId, updateType, isSuccess, updateTime.replace("-", ""));

        return ResultUtil.success(interfaceRecord);
    }
}