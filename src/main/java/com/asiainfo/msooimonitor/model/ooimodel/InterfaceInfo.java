package com.asiainfo.msooimonitor.model.ooimodel;

import lombok.Data;

/**
 * @Author H
 * @Date 2019/2/22 16:24
 * @describe
 **/
@Data
public class InterfaceInfo {

    /**
     * 接口号
     */
    private String interfaceId;

    /**
     * 接口类型 接口类型 1-实时 2-文件
     */
    private String interfaceType;

    /**
     * 接口处理类型 文件处理类型 1-上传 2-下载
     */
    private String interfaceDealType;

    /**
     *接口运行周期  1-日 2-周 3-月 4-临时
     */
    private String interfaceCycle;

    /**
     * 接口文件本地路径
     */
    private String interfaceLocalPath;

    /**
     * 接口文件远程路径路径
     */
    private String interfaceRemotePath;

    /**
     * 接口运行时间 接口运行时间 日接口/月接口为日期  周接口为星期几（1，2，3，4，5）
     */
    private String interfaceRunTime;

    /**
     * 接口文件名称
     */
    private String fileName;
}
