package com.asiainfo.msooimonitor.thread;

import com.asiainfo.msooimonitor.constant.StateAndTypeConstant;
import com.asiainfo.msooimonitor.mapper.dbt.load.LoadMapper;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.LoadService;
import com.asiainfo.msooimonitor.service.UploadService;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.FtpUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author H
 * @Date 2019/9/9 21:19
 * @Desc
 **/
@Component
@Slf4j
public class WriteFileThread {

    @Autowired
    UploadService uploadService;

    @Autowired
    LoadMapper loadMapper;

    @Autowired
    LoadService loadService;


    public void write(String interfaceId, String fileName, String tableName, String localPath,String remotePath, String date) {

        File file = new File(localPath);
        file.mkdirs();

        fileName = fileName.replaceAll("time", date);
        // 校验文件名称
        String verifyFileName = fileName.substring(0, fileName.lastIndexOf("_")) + ".verf";
        FileOutputStream dataFileWriter = null;
        FileOutputStream verifyFileWriter = null;
        // 记录写入文件条数
        int records = 0;
        // 每写入5000行验证文件大小
        int filterRecords = 5000;
        // 文件个数
        int fileNum = 1;
        String fileNameTmp = fileName.replace("fileNum", "00" + fileNum);
        int sum = loadMapper.getrows(tableName);
        try {
            dataFileWriter = new FileOutputStream(localPath + File.separator  + fileNameTmp);
            verifyFileWriter = new FileOutputStream(localPath + File.separator + verifyFileName);
            // 分页读取文件
            List<String> sqlList = createsql(tableName);

            for (String sql :
                    sqlList) {
                log.info("开始执行sql查询结果数据：{}",sql);
                List<Map<String, String>> interfaceInfoLists = uploadService.getInterfaceInfo(sql);

                for (Map<String, String> map :
                        interfaceInfoLists) {
                    records++;
                    Object[] line = map.values().toArray();
                    if (records % filterRecords == 0 && records > 0) {
                        dataFileWriter.flush();
                        boolean okSize = isOKSize(localPath + File.separator + fileNameTmp);
                        if (okSize) {
                            log.info("文件[{}]大于指定大小", fileNameTmp);
                            dataWrite(dataFileWriter, line, "E");
                            dataFileWriter.flush();
                            fileNum++;
                            // 生成校验文件信息
                            String[] verifyLine = createverifyInfo(fileNameTmp, localPath, date);
                            dataWrite(verifyFileWriter, verifyLine, "U");

                            if (fileNum >= 10) {
                                fileNameTmp = fileName.replaceAll("fileNum", "0" + fileNum);
                                dataFileWriter = new FileOutputStream(localPath + File.separator + fileNameTmp);
                            } else {
                                fileNameTmp = fileName.replaceAll("fileNum", "00" + fileNum);
                                dataFileWriter = new FileOutputStream(localPath + File.separator + fileNameTmp);
                            }
                        } else {
                            dataWrite(dataFileWriter, line, "U");
                        }
                    } else {
                        if (sum == records) {
                            dataWrite(dataFileWriter, line, "U");
                            dataFileWriter.flush();
                            String[] verifyLine = createverifyInfo(fileNameTmp, localPath, date);
                            dataWrite(verifyFileWriter, verifyLine, "U");
                        } else {
                            dataWrite(dataFileWriter, line, "U");
                        }
                    }
                }
                dataFileWriter.flush();

                if(interfaceInfoLists.size() == 0){
                    String[] verifyLine = createverifyInfo(fileNameTmp, localPath, date);
                    dataWrite(verifyFileWriter, verifyLine, "U");
                }

                verifyFileWriter.flush();
            }
            // 记录日志
            log.info("文件[{}]生成成功！！！", fileNameTmp);
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setInterfaceId(interfaceId);
            interfaceRecord.setRunStep(StateAndTypeConstant.FILE_DOWNLOAD_OR_CREATE);
            interfaceRecord.setTypeDesc(StateAndTypeConstant.TRUE);
            interfaceRecord.setFileName(fileName);
            interfaceRecord.setFileNum(String.valueOf(records));
            interfaceRecord.setFileSuccessNum(String.valueOf(records));
            interfaceRecord.setFileTime(date);
            loadService.insertRecord(interfaceRecord);

            // 上传到228
            FtpUtil.uploadFileFTP(localPath,remotePath, interfaceId, loadService,date);

        } catch (Exception e) {
            log.error("message：{}", e);
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setInterfaceId(interfaceId);
            interfaceRecord.setRunStep(StateAndTypeConstant.FILE_DOWNLOAD_OR_CREATE);
            interfaceRecord.setTypeDesc(StateAndTypeConstant.FALSE);
            interfaceRecord.setFileName(fileName);//localPath + File.separator
            interfaceRecord.setFileNum(FileUtil.getFileRows( localPath + File.separator + fileName));
            interfaceRecord.setFileTime(date);
            interfaceRecord.setFileSuccessNum("0");
            if(e.getMessage().length() > 480 ){
                interfaceRecord.setErrorDesc("文件生成出错:" + e.getMessage().substring(0, 470));
            }else {
                interfaceRecord.setErrorDesc("文件生成出错:" + e.getMessage());
            }
            loadService.insertRecord(interfaceRecord);

            log.error("接口[{}]文件[{}]生成出现异常{}", interfaceId, fileNameTmp, e);
        } finally {
            try {
                if (dataFileWriter != null) {
                    dataFileWriter.close();
                }
                if (verifyFileWriter != null) {
                    verifyFileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * e
     * 生成校验文件信息
     *
     * @param fileName
     * @param localPath
     * @param date
     * @return
     */
    private String[] createverifyInfo(String fileName, String localPath, String date) {
        String fileSize = FileUtil.getFileSize(localPath + File.separator + fileName);
        String filerows = FileUtil.getFileRows(localPath + File.separator + fileName);
        String longSeconds = TimeUtil.getLongSeconds(new Date());

        String[] verifyinfo = {fileName, fileSize, filerows, date, longSeconds};

        return verifyinfo;
    }

    /**
     * @param writer
     * @param line
     * @param flag   是否写入换行符
     * @throws Exception
     */
    private void dataWrite(FileOutputStream writer, Object[] line, String flag) throws Exception {
        byte[] bytes = {(byte) 0x80};
        for (int i = 0; i < line.length; i++) {
            String cloumn = String.valueOf(line[i]);
            // 最后一个字符的写入方式
            if (i == line.length - 1) {
                if (!"null".equals(cloumn)) {
                    writer.write(cloumn.getBytes("gbk"));
                }
                break;
            }
            // 字段为空的写入方式
            if ("null".equals(cloumn) || org.springframework.util.StringUtils.isEmpty(cloumn)) {
                writer.write(bytes);
            } else {
                writer.write(cloumn.getBytes("gbk"));
                writer.write(bytes);
            }

        }
        if ("U".equals(flag)) {
            writer.write("\r\n".getBytes("gbk"));
        }

    }

    /**
     * 检验文件大小 单位M
     *
     * @param path
     * @return
     */
    private boolean isOKSize(String path) {
        boolean flag = false;

        String fileSize = FileUtil.getFileSize(path);
        BigDecimal bigDecimal = new BigDecimal(fileSize);
        BigDecimal bigDecimal1 = new BigDecimal("1048576");
        BigDecimal BigDecimal2 = new BigDecimal("1");
        BigDecimal divide = bigDecimal.divide(bigDecimal1);
        int i = divide.compareTo(BigDecimal2);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 生成查询数据的sql
     *
     * @param tableName
     * @return
     */
    private List<String> createsql(String tableName) {

        ArrayList<String> sqlList = new ArrayList<>();

        String structureSql = "select column_name from information_schema.columns where table_schema = 'iop' and table_name = '" + tableName + "' order by ordinal_position";

        List<String> structureList = loadMapper.selectTableSutrct(structureSql);

        String columns = StringUtils.join(structureList, ",");

        // 设置分页参数 每次1000条
        int sum = loadMapper.getrows(tableName);
        // 分页条数
        int limitNum = 5000;
        int start = 0;
        int end = sum;
        for (int i = 0; i < sum / limitNum; i++) {
            sqlList.add("select row_number() over(order by a.A2) as A1,a.* from " + tableName  +" a " + " limit " + start + "," + limitNum);
            start += limitNum;
            end -= limitNum;
        }
        sqlList.add("select row_number() over(order by a.A2) as A1,a.* from " + tableName  +" a " + " limit " + start + "," + end);

        return sqlList;
    }
}
