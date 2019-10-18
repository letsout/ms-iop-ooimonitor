package com.asiainfo.msooimonitor.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author yx
 * @date 2019/9/16  11:40
 * Description
 */
@Aspect  // 使用@Aspect注解声明一个切面
@Component
@Slf4j
public class InterfaceLogAspect {


    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
//    com.asiainfo.msooimonitor.task.TaskSaveMethod
    @Pointcut(value = "execution(* com.asiainfo.msooimonitor.service.impl.TaskServiceImpl.*(..))")
    public void logPointCut() {
    }

    @Pointcut(value = "execution(* com.asiainfo.msooimonitor.mapper.mysql.GetFileDataMapper.get*(..))")
    public void logPointCut1() {
    }

    @Before("logPointCut()")
    public void printArg(JoinPoint point) {
        log.info("任务" + point.getSignature().getName() + "被调用，参数为：" + Arrays.toString(point.getArgs()));
    }

    @Around("logPointCut1()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //访问目标方法的参数：
        Object[] args = point.getArgs();
        //用改变后的参数执行目标方法
        Object returnValue = point.proceed(args);
        try {
            final int length = returnValue.toString().length();
            log.info("mapper层方法" + point.getSignature().getName() + "被调用，参数为：" + Arrays.toString(args) + "结果数据是否为空:" + (length == 2 ? true : false));
        } catch (Exception e) {
//            log.error("运行异常："+e);log.error("运行异常："+e);e.printStackTrace();


            log.info("mapper层方法" + point.getSignature().getName() + "被调用，参数为：" + Arrays.toString(args) + "结果数据是否为空:" + true);
        }
        return returnValue;
    }

}