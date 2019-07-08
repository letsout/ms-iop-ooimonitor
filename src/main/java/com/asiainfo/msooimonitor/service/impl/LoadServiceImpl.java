package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.mapper.Mysql.DownloadFileMapper;
import com.asiainfo.msooimonitor.mapper.dbt.load.LoadMapper;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.LoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/2/20 11:13
 * @Desc
 **/
@Service
public class LoadServiceImpl implements LoadService {

    private static final Logger logger = LoggerFactory.getLogger(LoadServiceImpl.class);

    @Autowired
    private LoadMapper loadMapper;

    @Autowired
    private DownloadFileMapper downloadFileMapper;

    @Override
    public Map<String,Object> sqlTemplate(String tableName) {

        String structureSql = "select column_name from sysibm.columns where table_schema = 'FCM' and table_name = '"+tableName+"' order by ordinal_position";

        List<String> structureList = loadMapper.selectTableSutrct(structureSql);

        StringBuffer sqlTemplate1 = new StringBuffer("insert into fcm." + tableName + " (");

        structureList.forEach(name -> sqlTemplate1.append(name+","));

        String sqlTmp1 = sqlTemplate1.substring(0,sqlTemplate1.length()-1);

        StringBuffer sqlTemplate2 = new StringBuffer(sqlTmp1 + ") values  ( ");

        structureList.forEach(name -> sqlTemplate2.append(":"+name+","));

        String sqlTmp2 = sqlTemplate2.substring(0,sqlTemplate2.length()-1);

        StringBuffer sqlTemplate4 = new StringBuffer(sqlTmp2 + ")");

        logger.info("sql模板：{}",sqlTemplate4.toString());

        Map<String, Object> returnMap = new HashMap<>();

        returnMap.put("sqlTemplate",sqlTemplate4.toString());
        returnMap.put("structureList",structureList);

        return returnMap;
    }

    @Override
    public void deleteSql(String sql) {
        loadMapper.deleteSql(sql);
    }

    @Override
    public int getrows(String tableName) {
       return loadMapper.getrows(tableName);
    }

    @Override
    public void insertRecord(InterfaceRecord interfaceRecord) {
        downloadFileMapper.insertRecord(interfaceRecord);
    }


}
