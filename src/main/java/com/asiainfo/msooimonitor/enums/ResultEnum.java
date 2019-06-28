package com.asiainfo.msooimonitor.enums;

/**
 * @Author H
 * @Date 2019/2/21 15:11
 * @Desc 返回结果枚举
 **/
public enum  ResultEnum {

    SUCCESS(0,"成功"),
    UNKNOWN_ERROR(-1,"系统错误");


    private Integer code;

    private String msg;

    ResultEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
