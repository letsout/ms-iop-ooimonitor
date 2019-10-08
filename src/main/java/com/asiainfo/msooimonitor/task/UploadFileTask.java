package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.service.UploadService;
import com.asiainfo.msooimonitor.thread.WriteFileThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/9/9 21:02
 * @Desc 根据接口汇总表生成文件
 **/
@Slf4j
@RequestMapping("/createFile")
@Component
public class UploadFileTask {

    @Value("${file.path17}")
    private String path17;

    @Value("${ftp.path}")
    private String path228;

    @Autowired
    WriteFileThread writeFileThread;
    @Autowired
    UploadService uploadService;

    @RequestMapping("/createFile")
    public void uploadFile() {

        // 查询数据已准备完成的
        List<Map<String, String>> canCreateFileInterface = uploadService.getCanCreateFileInterface();

        if(canCreateFileInterface.size() == 0 ){
            log.info("暂无带生成文件！！！！");
            return;
        }

        for (Map<String, String> map :
                canCreateFileInterface) {
            String interfaceId = "";
            String tableName = "";
            String date = "";
            String fileName = "";
            String localPath = "";
            String remotePath= "";
            // 设置基本属性
            // TODO 后面修改表模型然后优化
            for (Map.Entry enty :
                    map.entrySet()) {
                String k = (String) enty.getKey();
                String v = (String) enty.getValue();
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
                        if (("1").equals(v) || "2".equals(v)) {
                            localPath = path17 + File.separator + "upload" + File.separator + "time/day";
                            remotePath = path228 + File.separator + "upload" + File.separator + "time/day";
                        } else if ("3".equals(v)) {
                            localPath = path17 + File.separator + "upload" + File.separator + "time/month";
                            remotePath = path228 + File.separator + "upload" + File.separator + "time/month";
                        }
                        break;
                    default:
                        break;
                }
            }
            localPath = localPath.replaceAll("time", date);
            remotePath = remotePath.replaceAll("time", date);
            log.info("interfaceId:{},fileName：{}",interfaceId,fileName);
            writeFileThread.write(interfaceId, fileName, tableName, localPath,remotePath, date);
        }
    }
}
