package com.asiainfo.msooimonitor.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yx
 * @date 2019/9/6  17:29
 * Description
 */

public interface TaskService {
    /**
     * 活动效果评估接口：用户号码明细
     * IOP-93006
     * 活动效果评估接口中用户明细数据
     * 省级IOP在运营活动结束之后通过该接口上传明细数
     * 所属流程：一级策划省级执行、省级策划省级执行
     * 省级IOP
     * 日增量传送，每日11:00前传输前一日的数据文件
     *
     * @param activityEndDate
     * @throws Exception
     */

    void saveAll93006(String activityEndDate) throws Exception;

    /**
     * 省公司上报营销结果数据
     * IOP-93003
     * 省级IOP同步活动整体效果评估数据给一级IOP，用于营销监控和营销效果评估。
     * 所属流程：省级策划省级执行、省级策划一级执行-互联网
     * 省级IOP
     * 月增量传送，每月10日前传输前一月的数据文件
     *
     * @param activityEndDate
     * @throws Exception
     */
    void saveAll93003(String activityEndDate) throws Exception;

    /**
     * 日效果评估接口：省级IOP同步子活动效果评估数据给一级IOP
     * IOP-93001
     * 省级IOP同步子活动效果评估数据给一级IOP，用于营销监控和营销效果评估。
     * 所属流程：一级策划省级执行
     * 按日反馈已完成的活动或正在执行的活动的前一日的日指标数据
     * 属性编码 5-13为营销活动相关信息，14-36子活动相关信息，42-48子活动效果评估指标
     * 省级IOP
     * 日增量传送，每日11:00前传输前一日的数据文件
     *
     * @param activityEndDate
     * @throws Exception
     */
    void saveMarking93001(String activityEndDate) throws Exception;

    void saveMarking93005(String activityEndDate) throws Exception;

    void saveBase93005(String activityEndDate) throws Exception;

    void saveMarking93002(String activityEndDate) throws Exception;

    void saveBase93002(String activityEndDate) throws Exception;

    void saveBase93004(String activityEndDate) throws Exception;

    /**
     * 营销回避用户信息
     * IOP-93011
     * 营销回避用户信息
     * 省级IOP
     * 省侧每天14:00前上传前一天的数据文件。
     *
     * @param date
     * @throws Exception
     */
    void saveAll93011(String date) throws Exception;

    void uploadFile();

    List<Map<String, String>> getCheckFileByDate(String fileDate) throws IOException;

    void saveAll93052OR93053();

    void saveAll93050OR93051();

    void saveAll93054() throws Exception;

    /**
     * 检验校验文件是否正确
     *
     * @param date    需要检验的日期
     * @param fileSet 需要校验的日期的集合
     */
    void checkFile(String date, Set<String> fileSet);

    /**
     * 检测每个活动id与日期对应每一天的最大效果数据数量，用语判断效果数据是否正确
     */
    void getMaxCount();
}
