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
     * 接口名称
     */
    private String interfaceName;

    /**
     *  接口描述
     */
    private String interfaceDesc;

    /**
     * 活动id
     */
    private String actId;

    /**
     * 活动名称
     */
    private String actName;

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
     * 228主机文件存放地址
     */
    private String interfaceVgopPath;

    /**
     * 接口文件名称
     */
    private String fileName;

    /**
     * 数据是否有效
     */
    private String dataState;

    /**
     * 模糊查询参数
     */
    private String fuzzyQueryInfo;
}
