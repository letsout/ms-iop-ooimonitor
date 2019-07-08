package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.model.ooimodel.Result;
import com.asiainfo.msooimonitor.service.InterfaceInfoService;
import com.asiainfo.msooimonitor.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping("/getInterfaceInfo")
    public Result getInterfaceInfo(@RequestBody InterfaceInfo serachFilter) {
        List<InterfaceInfo> interfaceInfo = interfaceInfoService.getInterfaceInfo(serachFilter);
        return ResultUtil.success(interfaceInfo);
    }


    @RequestMapping("/deleteInterfaceInfoById")
    public Result deleteInterfaceInfoById(@RequestParam("interfaceId") String interfaceId){
        interfaceInfoService.deleteInterfaceInfoById(interfaceId);
        return ResultUtil.success();
    }


    @RequestMapping("/updateInterfaceInfoById")
    public Result updateInterfaceInfoById(@RequestBody InterfaceInfo interfaceInfo){
        interfaceInfoService.updateInterfaceInfoById(interfaceInfo);
        return ResultUtil.success();
    }

    @RequestMapping("/insertInterfaceInfo")
    public Result insertInterfaceInfo(@RequestBody InterfaceInfo interfaceInfo){
        interfaceInfoService.insertInterfaceInfo(interfaceInfo);
        return ResultUtil.success();
    }


    @RequestMapping("/hello")
    public String hello() {
        return "hello word";
    }
}