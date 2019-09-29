package com.asiainfo.msooimonitor;

import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.task.UploadFileTask;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author H
 * @Date 2019/9/29 13:50
 * @Desc
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MsOoiMonitorApplicationTest {
    @Autowired
    FileDataService fileDataService;
    @Autowired
    UploadFileTask uploadFileTask;
    @Test
    public void test93055(){
        fileDataService.create93055("201909");
        uploadFileTask.uploadFile();
    }

    @Test
    public void test93056(){
        fileDataService.create93056("201909");
        uploadFileTask.uploadFile();
    }
}
