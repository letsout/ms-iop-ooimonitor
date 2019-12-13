package com.asiainfo.msooimonitor;

import com.asiainfo.msooimonitor.handle.HandleData;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.task.TaskMethod;
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
    TaskMethod taskMethod;
    @Autowired
    HandleData handleData;
    @Autowired
    TaskService taskServices;


    @Test
    public void test93055(){
        fileDataService.create93055("201909");
    }

    @Test
    public void test93056(){
        fileDataService.create93056("201909");
    }

    @Test
    public void save93052OR93053(){
        taskMethod.save93052OR93053();
    }

    @Test
    public void saveAll93050OR93051(){
        taskMethod.saveAll93050OR93051();
    }

    @Test
    public void handData(){
        handleData.killFile("91050","H:\\data1","iop_91050","20191024");
    }

    @Test
    public void uploadFile(){
        taskServices.uploadFile();
    }
}
