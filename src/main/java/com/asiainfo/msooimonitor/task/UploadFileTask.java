package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.mapper.dbt.upload.UploadMapper;
import com.asiainfo.msooimonitor.service.UploadService;
import com.asiainfo.msooimonitor.thread.WriteFileThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author H
 * @Date 2019/9/9 21:02
 * @Desc 根据接口汇总表生成文件
 **/
@Slf4j
@Component
public class UploadFileTask {

    @Autowired
    WriteFileThread writeFileThread;

    public void uploadFile(){
        String interfaceId = "";
        String tableName = "";
        String data = "";
        String fileName = "";
        String localPath = "";
        writeFileThread.write(interfaceId,fileName,tableName,localPath,data);
    }
    // 查询满足条件的汇总表
}
