package com.asiainfo.msooimonitor.model.datahandlemodel;

import lombok.Data;

/**
 * @Author H
 * @Date 2019/9/24 15:55
 * @Desc
 **/
@Data
public class ActivityProcessInfo {

    private String activityId;

    /**
     * 1.打标区分不同场景的活动
     *      1：一级策划省级执行
     *      2：省级策划一级执行-互联网
     *      3：省级策划省级执行
     *      4：一级策划一点部署-一级电渠
     *      5：一级策划一点部署-互联网
     *      6：一级策划一点部署-省级播控平台
     *      7：一级策划一点部署-咪咕
     *      8：省级策划一级执行-电渠
     *      9：省级策划一级执行-咪咕
     *      10：省级策划一级执行-爱流量
     *      98：一级策划一点部署
     *      99：其他
     */
    private String processId;
}
