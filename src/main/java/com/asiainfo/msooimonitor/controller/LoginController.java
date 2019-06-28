package com.asiainfo.msooimonitor.controller;

import com.asiainfo.msooimonitor.model.ooimodel.Result;
import com.asiainfo.msooimonitor.model.ooimodel.User;
import com.asiainfo.msooimonitor.service.LoginService;
import com.asiainfo.msooimonitor.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @Author H
 * @Date 2019/2/21 15:03
 * @Desc 用户登陆
 **/
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    final Base64.Decoder decoder = Base64.getDecoder();

    @Autowired
    private LoginService loginService;

    @PostMapping("/userLogin")
    public Result login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password, HttpServletRequest request){
        try {
            userName = new String(decoder.decode(userName),"utf-8");
            password = new String(decoder.decode(password),"utf-8");

            logger.info("userName:{},password:{}",userName,password);

            Boolean login = loginService.login(userName, password);
            if(login){
                User user = new User(userName,null);
                request.getSession().setAttribute("name",userName);
                return ResultUtil.success(user);
            }else {
                return ResultUtil.error("用户名或密码错误！！！");
            }

        } catch (UnsupportedEncodingException e) {
           return ResultUtil.error("系统异常！！！");
        }
    }

    @GetMapping("/logOut")
    public Result logOut(HttpSession session){

        session.removeAttribute("admin");

        return ResultUtil.success();
    }

}
