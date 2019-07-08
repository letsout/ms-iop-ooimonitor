package com.asiainfo.msooimonitor.exception;

import lombok.Data;

/**
 * @Author H
 * @Date 2019/7/2 14:52
 * @Desc
 **/
@Data
public class FtpException extends RuntimeException{

    private Object e;

    private String message;

    public FtpException(Object e, String message) {
        this.e = e;
        this.message = message;
    }
}
