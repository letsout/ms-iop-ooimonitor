package com.asiainfo.msooimonitor.service;

/**
 * @author yx
 * @date 2019/9/6  17:29
 * Description
 */

public interface TaskService {


    void saveAll93006(String activityEndDate) throws Exception;

    void saveMarking93001(String activityEndDate) throws Exception;

    void saveMarking93005(String activityEndDate) throws Exception;

    void saveBase93005(String activityEndDate) throws Exception;

    void saveMarking93002(String activityEndDate) throws Exception;

    void saveBase93002(String activityEndDate) throws Exception;

    void uploadFile();
}
