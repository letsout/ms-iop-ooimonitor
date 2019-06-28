package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.dbt.ooi.LoginMapper;
import com.asiainfo.msooimonitor.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author H
 * @Date 2019/2/21 15:40
 * @Desc
 **/
@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private LoginMapper loginMapper;

    @Override
    public Boolean login(String userName, String password) {

        Boolean flag = false;

        int num = loginMapper.login(userName, password);

        if (num > 0 ){
            flag=true;
        }
        return flag;
    }
}
