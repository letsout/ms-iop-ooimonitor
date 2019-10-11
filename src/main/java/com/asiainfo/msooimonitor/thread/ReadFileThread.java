package com.asiainfo.msooimonitor.thread;

import com.alibaba.fastjson.JSON;
import com.asiainfo.msooimonitor.constant.StateAndTypeConstant;
import com.asiainfo.msooimonitor.handle.HandleData;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.LoadService;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Author H
 * @Date 2019/2/20 16:07
 * @Desc
 **/
@Component
public class ReadFileThread {
    private static final Logger logger = LoggerFactory.getLogger(ReadFileThread.class);

    @Autowired
    LoadService loadService;

    @Autowired
    HandleData handleData;

    ArrayList<Map<String, String>> mapList = new ArrayList<>();

    public void readFile(String fileName, String dir, String interfaceId, String tableName, String date) {

        if (StringUtils.isEmpty(tableName)) {
            throw new RuntimeException("接口号[" + interfaceId + "]对应表名不存在！！");
        }

        Map<String, Object> sMap = loadService.sqlTemplate(tableName);

        List<String> paramList = (List<String>) sMap.get("structureList");

        String sqlTemplate = (String) sMap.get("sqlTemplate");

        HashMap<String, String> recordMap = new HashMap<>();

        recordMap.put("updateTime", TimeUtil.getDateTimeFormat(new Date()));

        recordMap.put("interfaceId", interfaceId);

        int count = 0;

        //读取指定文件
        try {
            logger.info("开始读取文件 [{}]", fileName);

            //       dir = "C:\\Users\\40468\\Desktop\\";

            FileInputStream fileInputStream = new FileInputStream(dir + File.separator + fileName);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "gb2312");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = "";

            String[] str = null;

            while (StringUtils.isNotBlank(line = bufferedReader.readLine())) {

                str = splitLine(interfaceId, line, date);

                if (str.length != paramList.size()) {
                    continue;
                }

                HashMap<String, String> hashMap = new HashMap<>();

                for (int i = 0; i < paramList.size(); i++) {
                    hashMap.put(paramList.get(i), str[i]);
                }
                mapList.add(hashMap);
                count++;
                if (50000 == mapList.size()) {
                    handleData.batchInsert(sqlTemplate, mapList);
                    mapList.clear();
                    logger.info("入库条数{}", count);
                }
            }

            //整个表数据循环完后，如果list中还有数据，就要再执行一次入表操作。（最后一批次数据不满10000）
            if (0 != mapList.size()) {
                handleData.batchInsert(sqlTemplate, mapList);
                logger.info("剩余条数不足50000，入库{}", mapList.size());
                mapList.clear();
            }

            logger.info("文件[{}]入库成功！！！", fileName);
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setInterfaceId(interfaceId);
            interfaceRecord.setRunStep(StateAndTypeConstant.FILE_UPLOAD_OR_RK);
            interfaceRecord.setTypeDesc(StateAndTypeConstant.TRUE);
            interfaceRecord.setFileName(fileName);
            interfaceRecord.setFileNum(FileUtil.getFileRows(dir + File.separator + fileName));
            interfaceRecord.setFileSuccessNum(String.valueOf(count));
            interfaceRecord.setFileTime(date);
            loadService.insertRecord(interfaceRecord);
            logger.info("文件[{}]入库完成！！！", fileName);

        } catch (Exception e) {

            // logger.error("文件[{}]入库失败！！！错误行数{}", fileName, JSON.toJSONString(mapList));
            logger.error("message：{}", e);
            loadService.deleteSql("delete from " + tableName + " where data_time='" + date + "'");
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setInterfaceId(interfaceId);
            interfaceRecord.setRunStep(StateAndTypeConstant.FILE_UPLOAD_OR_RK);
            interfaceRecord.setTypeDesc(StateAndTypeConstant.FALSE);
            interfaceRecord.setFileName(fileName);
            interfaceRecord.setFileNum(FileUtil.getFileRows(dir + File.separator + fileName));
            interfaceRecord.setFileTime(date);
            interfaceRecord.setFileSuccessNum("0");
            if (e.getMessage().length() > 1900) {
                interfaceRecord.setErrorDesc("文件解析出错:" + e.getMessage().substring(0, 1900));
            } else {
                interfaceRecord.setErrorDesc("文件解析出错:" + e.getMessage());
            }
            loadService.insertRecord(interfaceRecord);
        }

    }

    /**
     * 分割字符串数组，文件分割符不同
     *
     * @param interfaceId
     * @param line
     * @param date
     * @return
     */
    private String[] splitLine(String interfaceId, String line, String date) {
        String split = "";
        if (interfaceId.contains("migu") || interfaceId.contains("sichuan")) {
            split = new String(new byte[]{(byte) 0x09});
        } else {
            split = new String(new byte[]{(byte) 0x80});
        }

        logger.debug("读取字符串信息：{},分割符{}", line,split);

        String s = line + split + date;
        return s.split(split);
    }
}
