package com.asiainfo.msooimonitor.model.ooimodel;

/**
 * @Author H
 * @Date 2019/2/21 15:08
 * @Desc http请求统一返回结果.
 **/
public class Result<T> {

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 数据内容
     */
    private T data;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
