package com.asiainfo.msooimonitor.service;

@FunctionalInterface
public interface LambdaThrowException <T,E extends Exception>{
    void accept(T t) throws E;
}
