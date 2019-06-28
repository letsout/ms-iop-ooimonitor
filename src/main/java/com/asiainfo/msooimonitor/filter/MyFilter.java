/*
package com.asiainfo.msooimonitor.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

*/
/**
 * @Author H
 * @Date 2019/1/2 10:59
 * @Desc
 *
*//*

//@WebFilter(urlPatterns = {"*.html","/","/ooimonitor/" }, filterName = "Filter")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = ((HttpServletRequest) servletRequest).getSession();
        String url=request.getRequestURI();
        System.out.println("url"+url);
        String name= (String) session.getAttribute("name");
        if (url.equals("/login.html")) {
            System.out.println("放行");
            filterChain.doFilter(servletRequest, servletResponse);
        }else if( name==null){
            System.out.println("拦截");
            ((HttpServletResponse) servletResponse).sendRedirect("/login.html");
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
*/
