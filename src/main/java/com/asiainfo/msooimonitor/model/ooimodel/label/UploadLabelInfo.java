package com.asiainfo.msooimonitor.model.ooimodel.label;

import lombok.Data;

/**
 * @Author H
 * @Date 2019/10/16 9:44
 * @Desc
 **/
@Data
public class UploadLabelInfo {

    /**
     * 数据反馈周期	1、一次性，2、周期性
     */
    private String cycle;

    private String labelId;

    /**
     * 周期类型	D：日周期 M：月周期
     */
    private String refreshCyc;

    private String taskId;

    /**
     * 101:“一级标签下发（带数据）”
     * 102:“一级下发规则省级上传数据”
     * 103:“省优秀标签上传（带数据）”
     * 104：“一级标签下线任务” 105：“省级标签下线任务
     */
    private String taskType;

    /**
     * 标签引用次数
     */
    private String quoteNum;

    /**
     * 统计时间，yyyymmdd
     */
    private String countTime;
}
