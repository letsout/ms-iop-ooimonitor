package com.asiainfo.msooimonitor.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author H
 * @Date 2019/1/17 10:30
 * @Desc
 **/
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext == null){
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
}
