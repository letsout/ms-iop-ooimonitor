package com.asiainfo.msooimonitor.exception;

/**
 * @Author H
 * @Date 2019/2/24 10:28
 * @Desc
 **/
public class FileNotFoundException extends Exception {

    public FileNotFoundException() {
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundException(Throwable cause) {
        super(cause);
    }

    public FileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
