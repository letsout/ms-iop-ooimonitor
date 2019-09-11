package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.service.UploadService;
import com.asiainfo.msooimonitor.thread.WriteFileThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/9/9 21:02
 * @Desc 根据接口汇总表生成文件
 **/
@Slf4j
@Component
public class UploadFileTask {

    @Value("${path17}")
    private String path17;

    @Autowired
    WriteFileThread writeFileThread;
    @Autowired
    UploadService uploadService;

    public void uploadFile() {

        // 查询数据已准备完成的
        List<Map<String, String>> canCreateFileInterface = uploadService.getCanCreateFileInterface();

        canCreateFileInterface.stream()
                .forEach(info -> {
                    info.forEach((k, v) -> {
                        // 设置基本属性
                        String interfaceId = "";
                        String tableName = "";
                        String date = "";
                        String fileName = "";
                        // TODO 后面修改表模型然后优化
                        String localPath = "";
                        switch (k) {
                            case "interface_id":
                                interfaceId = v;
                                break;
                            case "table_name":
                                tableName = v;
                                break;
                            case "data_time":
                                date = v;
                                break;
                            case "file_name":
                                fileName = v;
                                break;
                            case "interface_cycle":
                                if(("1").equals(v) || "2".equals(v)){
                                    localPath = path17 + File.separator + "upload" + File.separator +"time/day";
                                }else if("3".equals(v)){
                                    localPath = path17 + File.separator + "upload" + File.separator +"time/month";
                                }
                                break;
                            default:
                                break;
                        }
                        localPath =  localPath.replaceAll("time",date);
                        writeFileThread.write(interfaceId, fileName, tableName, localPath, date);
                    });
                });
    }
}
