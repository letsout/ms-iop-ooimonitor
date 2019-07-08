package com.asiainfo.msooimonitor.model.ooimodel;

import lombok.Data;

/**
 * @Author H
 * @Date 2019/7/8 17:13
 * @Desc 接口信息条件过滤实体
 **/
@Data
public class SerachFilter {

    /**
     * 接口号
     */
    private String interfaceId;

    /**
     * 活动id
     */
    private String actId;

    /**
     * 活动名称
     */
    private String actName;

    /**
     *  接口类型 1-实时 2-文件
     */
    private String interfaceType;

    /**
     * 文件处理类型 1-上传 2-下载
     */
    private String interfaceDealType;

    /**
     * 接口运行周期  1-日 2-周 3-月 4-临时
     */
    private String interfaceCycle;

    /**
     * 上传接口生成/下载接口存放路径   相对
     */
    private String interfaceLocaPath;

    /**
     * 上传接口上传/下载接口下载到本地路径  相对
     */
    private String interfaceRemotePath;

    /**
     * 接口运行时间 日接口/月接口为日期  周接口为星期几（1，2，3，4，5）
     */
    private String interfaceRunTime;

    /**
     * 文件生成名称
     */
    private String fileName;
}

