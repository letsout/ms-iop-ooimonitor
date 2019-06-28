package com.asiainfo.msooimonitor.utils;

import com.asiainfo.msooimonitor.enums.ResultEnum;
import com.asiainfo.msooimonitor.model.ooimodel.Result;

/**
 * @Author H
 * @Date 2019/2/21 15:07
 * @Desc 返回
 **/
public class ResultUtil {

    public static Result success(Object object){
        Result<Object> result = new Result<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }

    public static Result success(){
        return success(null);
    }

    public static Result error(String msg){
        Result<Object> result = new Result<>();
        result.setCode(ResultEnum.UNKNOWN_ERROR.getCode());
        result.setMsg(msg);
        return result;
    }

}
