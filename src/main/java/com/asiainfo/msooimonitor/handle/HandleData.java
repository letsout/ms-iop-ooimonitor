package com.asiainfo.msooimonitor.handle;

import com.asiainfo.msooimonitor.service.LoadService;
import com.asiainfo.msooimonitor.thread.ReadFileThread;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Author H
 * @Date 2019/2/20 15:13
 * @Desc 解析文件处理load接口
 **/
@Component
public class HandleData {

    private static final Logger logger = LoggerFactory.getLogger(HandleData.class);

    @Autowired
    private ReadFileThread readFileThread;

    @Autowired
    private LoadService loadService;


    /**
     * 根据接口号过滤文件并读取过滤之后的
     *
     * @param interfaceId
     */
    public void killFile(String interfaceId, String localPath, String tableName, String date) {

        List<String> list = FileUtil.listFile(localPath);
       /* String newPath= localPath+File.separator+"tmp";
        FileUtil.dirExit(newPath);*/
        logger.info("localPath:{}", localPath);

        if(!(list==null||list.size()==0)){
            List<String> collect = list.stream().filter(name -> name.contains(interfaceId)).collect(Collectors.toList());
            // 加载文件之前先删除当天入库文件
            logger.info("清除接口[{}]-[{}]周期",interfaceId,date);
            loadService.deleteSql("delete from " + tableName + " where data_time='" + date + "'");
            if (collect.size() > 0) {
                for (String fileName:
                collect) {
                    readFileThread.ReadFile(fileName,localPath,interfaceId,tableName,date);
                }
            } else {
                logger.info("接口：{" + interfaceId + "} 文件不存在！！！");
            }
        }else {
            logger.info("接口：{" + interfaceId + "} 文件不存在！！！");
        }


    }


    /**
     * laod数据批处理
     *
     * @param sql     sql模板
     * @param mapList 对应的字段信息
     * @throws Exception 发生异常向上层抛出
     */
    public void batchInsert(String sql, List<Map<String, String>> mapList) throws Exception {


        long begin = System.currentTimeMillis();

        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) SpringUtil.getBean("loadJdbc");

        Map<String, ?>[] map = new HashMap[mapList.size()];

        jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(mapList.toArray(map)));

        String timeW = String.valueOf(System.currentTimeMillis() - begin);

        logger.info("inser sql a wast of time: {}ms", timeW);
    }
}
