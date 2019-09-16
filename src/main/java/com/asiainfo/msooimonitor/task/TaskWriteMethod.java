package com.asiainfo.msooimonitor.task;

import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2019/9/6  17:29
 * Description
 */
@Component
@Slf4j
public class TaskWriteMethod {

    @Autowired
    FileDataService fileDataService;
    /**
     * 时间后缀
     */
    private String suffix = "0000000";
    /**
     * 分隔符
     */
    private String splite = new String(new byte[]{(byte) 0X80});

    /**
     * 换行符
     */
    private String enter = new String(new byte[]{(byte) 0x0D0A});

    String date = "";
    //本地存储文件路径
    String localPath = "";
    String checkFileName = "";
    String filePreName = "";

    public void write93006(String date1, int restry, String upload_time) throws Exception {
        log.info("date1:" + date1);
        if (StringUtils.isBlank(date1)) {
            date = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
        } else {
            date = date1;
        }
        log.info("date:" + date);
        //本地存储文件路径
        localPath = "/data1/vgop_iop/iop-OOI/upload/" + date + "/day/";
        checkFileName = localPath + "i_13000_" + date + "_IOP-93006_" + addZero(restry, 2) + ".verf";
        filePreName = "i_13000_" + date + "_IOP-93006_" + addZero(restry, 2) + "_";
        int fileIndex = 1;
        log.info("date:" + date);
        log.info("localPath:" + localPath);
        log.info("checkFileName:" + checkFileName);
        String sql = "select * from iop_93006";
        List<Map<String, String>> list = fileDataService.getResult(sql);
        StringBuilder sbresult = new StringBuilder();
        int line = 1;
        for (Map map : list) {
            sbresult.append(line).append(splite);
            for (int a = 2; a < 17; a++) {
                sbresult.append(map.get("A" + a)).append(splite);
            }
            sbresult.append(enter);
        }
        File file = new File(localPath + filePreName + addZero(fileIndex, 3) + ".dat");
        OutputStream out = new FileOutputStream(file);
        out.write(sbresult.toString().getBytes("GBK"));
        final String checkfilename = writeCheckFile(file.getName(), sbresult.toString().getBytes("GBK").length, line);
        uploadFile(checkfilename);
        uploadFile(file.getAbsolutePath());
    }


    public void write93001(String date1, int restry, String upload_time) throws Exception {
        log.info("date1:" + date1);
        if (StringUtils.isBlank(date1)) {
            date = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
        } else {
            date = date1;
        }
        log.info("date:" + date);
        //本地存储文件路径
        localPath = "/data1/vgop_iop/iop-OOI/upload/" + date + "/day/";
        checkFileName = localPath + "i_13000_" + date + "_IOP-93001_" + addZero(restry, 2) + ".verf";
        filePreName = "i_13000_" + date + "_IOP-93001_" + addZero(restry, 2) + "_";
        int fileIndex = 1;
        log.info("date:" + date);
        log.info("localPath:" + localPath);
        log.info("checkFileName:" + checkFileName);
        String sql = "select * from iop_93001";
        List<Map<String, String>> list = fileDataService.getResult(sql);
        StringBuilder sbresult = new StringBuilder();
        int line = 1;
        for (Map map : list) {
            sbresult.append(line).append(splite);
            for (int a = 2; a < 51; a++) {
                sbresult.append(map.get("A" + a)).append(splite);
            }
            sbresult.append(enter);
        }
        File file = new File(localPath + filePreName + addZero(fileIndex, 3) + ".dat");
        OutputStream out = new FileOutputStream(file);
        out.write(sbresult.toString().getBytes("GBK"));
        final String checkfilename = writeCheckFile(file.getName(), sbresult.toString().getBytes("GBK").length, line);
        uploadFile(checkfilename);
        uploadFile(file.getAbsolutePath());
    }

    /**
     * 写入校验文件
     *
     * @param fileSize 文件大小
     * @param fileLine 文件行数
     * @param fileName 校验文件名称
     * @return 校验文件名称
     */
    public String writeCheckFile(String fileName, long fileSize, int fileLine) {
        String fileContext = fileName + splite + fileSize + splite + fileLine + splite + date + splite + TimeUtil.getLongSeconds(new Date()) + enter;
        OutputStream out = null;
        try {
            File file = new File(checkFileName);
            mkdir(file);
            //如果文件夹不存在则创建
            out = new FileOutputStream(file, true);
            out.write(fileContext.getBytes("GBK"));
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //把传入的x转换成00x的格式
    public String addZero(int index, int size) {
        int indexSize = (index + "").length();
        String indexString = "";
        int addsize = size - indexSize;
        for (int a = 0; a < addsize; a++)
            indexString += "0";
        return indexString + index;
    }

    /**
     * @param File
     * @param boo  true表示是最底层目录最底层要创建文件，false表示是递归自己调用
     */
    private void mkdir(File File, boolean boo) {
        File dir = null;
        if (boo) {
            dir = File.getParentFile();
        } else {
            dir = File;
        }
        if (!dir.getParentFile().exists()) {
            mkdir(dir.getParentFile(), false);
        }
        dir.mkdir();
    }

    private void mkdir(File dir) {
        mkdir(dir, true);
    }

    public String getFileName(String absolutePath) {
        String[] sz = absolutePath.split("/");
        String fileName = sz[sz.length - 1];
        return fileName;
    }


    public Boolean uploadFile(String absolutePath) {
        String fileName = getFileName(absolutePath);
        log.info("执行上传操作absolutePathfilename=" + absolutePath);
        //FTP服务器保存目录
        String remotePath = "/datain/qcd_iop/iop-OOI/sbin-data/upload/" + date + "/day/";
        String host = "10.109.3.228";
        int port = 21;
        String userName = "vgop_iop";
        String pass = "456vCe!b";
        log.info("上传到服务器的文件名为：" + remotePath + fileName);
        try {
            /**
             * * 上传文件（可供Action/Controller层使用） * @param hostname FTP服务器地址 * @param port
             * FTP服务器端口号 * @param username FTP登录帐号 * @param password FTP登录密码 * @param
             * pathname FTP服务器保存目录 * @param fileName 上传到FTP服务器后的文件名称 * @param inputStream
             * 输入文件流 * @return
             */
            //boolean isok = FtpUtil.uploadFileFTP(host, port, userName, pass, remotePath, fileName, new FileInputStream(new File(absolutePath)));
            //log.info("上传结果文件:" + fileName + "    ：isok=" + isok + "      ");
            //return isok;
            return null;
        } catch (Exception e) {
            log.info("上传文件:" + fileName + "失败");
            e.printStackTrace();
            return false;
        }
    }
}
