package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.model.ooimodel.Result;
import com.asiainfo.msooimonitor.service.InterfaceRunRecordService;
import com.asiainfo.msooimonitor.utils.ResultUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class InterfaceRunRecordController {

    @Autowired
    InterfaceRunRecordService interfaceRunRecordService;

    @RequestMapping("/getInterfaceRunRecordInfo")
    public Result getInterfaceRunRecordInfo(@RequestParam(value = "interfaceId",required = false) String interfaceId ,
                                            @RequestParam(value = "typeDesc",required = false) String typeDesc ,
                                            @RequestParam(value = "runStep",required = false) String runStep ,
                                   @RequestParam("page") int pageNum,
                                   @RequestParam("limit") int pageSize) {

        InterfaceRecord serachFilter = new InterfaceRecord();
        serachFilter.setInterfaceId(interfaceId);
        serachFilter.setTypeDesc(typeDesc);
        serachFilter.setRunStep(runStep);
        PageInfo<InterfaceRecord> interfaceRunRecordInfo = interfaceRunRecordService.getInterfaceRunRecordInfo(serachFilter, pageNum, pageSize);

        return ResultUtil.success(interfaceRunRecordInfo.getList(),interfaceRunRecordInfo.getTotal());
    }

    @RequestMapping("/deleteInterfaceInfos")
    public Result deleteInterfaceInfos(@RequestParam("interfaceId") String interfaceId) {
        interfaceRunRecordService.deleteInterfaceInfos(interfaceId);
        return ResultUtil.success();
    }
}
