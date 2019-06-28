package com.asiainfo.msooimonitor.intercepter;

import com.asiainfo.msooimonitor.model.ooimodel.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author H
 * @Date 2019/2/22 10:09
 * @Desc
 **/
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag =true;
        String ip = request.getRemoteAddr();
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        System.out.println("用户:"+ip+",访问目标:"+method.getDeclaringClass().getName() + "." + method.getName());

        User user=(User)request.getSession().getAttribute("user");
//        if(null==user){
//            flag = false;
//        }else{
//            flag = true;
//        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
