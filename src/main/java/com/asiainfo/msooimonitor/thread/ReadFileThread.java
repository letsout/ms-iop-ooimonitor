package com.asiainfo.msooimonitor.thread;

import com.asiainfo.msooimonitor.constant.StateAndTypeConstant;
import com.asiainfo.msooimonitor.handle.HandleData;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.LoadService;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.ws.Action;
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

    public void ReadFile(String fileName, String dir, String interfaceId, String date) {

        // 根据接口号查询表名
        String tableName =   loadService.getInterfaceTableName(interfaceId);

        if (StringUtils.isEmpty(tableName)){

            throw new RuntimeException("接口号["+interfaceId+"]对应表名不存在！！");

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

            // dir = "H:\\data1\\vgop_iop\\iop-OOI\\sbin-data\\download\\20190822\\day";

            FileInputStream fileInputStream = new FileInputStream(dir + File.separator+ fileName);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "gb2312");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = "";

            String[] str = null;

            ArrayList<Map<String, String>> mapList = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                if(count == 0 ){
                    loadService.deleteSql("delete from " + tableName + " where data_time='" + date + "'");
                }

                //    logger.info("读取字符串信息：{},分割符{}", line, new String(new byte[]{(byte) 0x80}));

                String s = line + new String(new byte[]{(byte) 0x80}) + date;

                str = s.split(new String(new byte[]{(byte) 0x80}));

                HashMap<String, String> hashMap = new HashMap<>();

                for (int i = 0; i < paramList.size(); i++) {
                    hashMap.put(paramList.get(i), str[i]);
                }
                mapList.add(hashMap);
                count++;
                if (10000 == mapList.size()) {
                    handleData.batchInsert(sqlTemplate, mapList);
                    mapList.clear();
                    logger.info("入库条数{}", count);
                }
            }

            //整个表数据循环完后，如果list中还有数据，就要再执行一次入表操作。（最后一批次数据不满10000）
            if (0 != mapList.size()) {
                handleData.batchInsert(sqlTemplate, mapList);
                logger.info("剩余条数不足10000，入库{}", mapList.size());
                mapList.clear();
            }

            logger.info("文件[{}]入库成功！！！", fileName);
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setInterfaceId(interfaceId);
            interfaceRecord.setRunStep(StateAndTypeConstant.FILE_UPLOAD_OR_RK);
            interfaceRecord.setTypeDesc(StateAndTypeConstant.TRUE);
            interfaceRecord.setFileName(fileName);
            interfaceRecord.setFileNum(FileUtil.getFileRows(dir,fileName));
            interfaceRecord.setFileSuccessNum(String.valueOf(count));
            interfaceRecord.setFileTime(date);
            loadService.insertRecord(interfaceRecord);
            logger.info("文件[{}]入库完成！！！", fileName);

        } catch (Exception e) {
            logger.error("文件[{}]入库失败！！！错误行数{}", fileName, count);
            logger.error("message：{}", e);
            loadService.deleteSql("delete from " + tableName + " where data_time='" + date + "'");
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setInterfaceId(interfaceId);
            interfaceRecord.setRunStep(StateAndTypeConstant.FILE_UPLOAD_OR_RK);
            interfaceRecord.setTypeDesc(StateAndTypeConstant.FALSE);
            interfaceRecord.setFileName(fileName);
            interfaceRecord.setFileNum(FileUtil.getFileRows(dir,fileName));
            interfaceRecord.setFileTime(date);
            interfaceRecord.setFileSuccessNum("0");
            interfaceRecord.setErrorDesc("文件解析出错:"+e.getMessage().substring(0,470));
            loadService.insertRecord(interfaceRecord);
        }

    }
}
