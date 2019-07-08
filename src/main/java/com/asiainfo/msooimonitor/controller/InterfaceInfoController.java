package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.enums.ResultEnum;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import com.asiainfo.msooimonitor.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author H
 * @Date 2019/2/22 16:18
 * @Desc
 **/
@RestController
@Slf4j
public class InterfaceInfoController {


    /*@RequestMapping("/getInterfaceInfo")
    public ResultUtil getInterfaceInfo(@RequestBody InterfaceInfo interfaceInfo){

        return null;
    }*/

    @RequestMapping("/hello")
    public String hello(){
        return "hello word";
    }
}