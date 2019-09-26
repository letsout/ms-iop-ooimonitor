package com.asiainfo.msooimonitor.utils;

import com.asiainfo.msooimonitor.constant.StateAndTypeConstant;
import com.asiainfo.msooimonitor.model.ooimodel.InterfaceRecord;
import com.asiainfo.msooimonitor.service.LoadService;
import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Created by H on 2018/1/11.
 */
public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * 主机名称
     */
    private static String HOST;

    /**
     * 用户名
     */
    private static String USER;

    /**
     * 密码
     */
    private static String PASS;

    /**
     * FTP 端口
     */
    private static int FTP_PORT;

    /**
     * SFTP 端口
     */
    private static int SFTP_PORT;

    static {
        try {
            Properties properties = new Properties();
            ClassPathResource classPathResource = new ClassPathResource("/application-prod.yml");
            properties.load(classPathResource.getInputStream());
            HOST = properties.getProperty("ftp.host", "10.109.3.228");
            USER = properties.getProperty("ftp.user", "vgop_iop");
            PASS = properties.getProperty("ftp.pass", "456vCe!b");
            FTP_PORT = Integer.parseInt(properties.getProperty("ftp.port", "21"));
            SFTP_PORT = Integer.parseInt(properties.getProperty("ftp.sport", "22"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * SFTP 文件上传
     *
     * @param pathname    远程目录
     * @param fileName    文件名称
     * @param inputStream 文件输入流
     * @return
     * @throws JSchException
     * @throws SftpException
     */
    public static boolean uploadFile(String pathname, String fileName,
                                     InputStream inputStream) throws JSchException, SftpException {

        boolean flag = false;
        ChannelSftp channelSftp = null;
        Session session = null;

        SftpATTRS attrs = null;

        JSch jSch = new JSch();
        session = jSch.getSession(USER, HOST);

        if (session == null) {
            logger.info("session is null");
            return false;
        }
        session.setPassword(PASS);
        session.setPort(SFTP_PORT);
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        session.setConfig(properties);
        session.connect();

        if (!session.isConnected()) {
            logger.info("session can not connect");
            return false;
        }

        Channel channel = session.openChannel("sftp");

        channel.connect();

        if (!channel.isConnected()) {
            logger.info("channel can not connect");
            session.disconnect();
            return false;
        }

        channelSftp = (ChannelSftp) channel;

        String[] strings = pathname.split("/");

        StringBuffer dirPath = new StringBuffer("/");
        for (String str : strings) {
            if (str.equals("")) {
                continue;
            }
            dirPath.append(str + "/");
            if (isDirExit(dirPath.toString(), channelSftp)) {
                channelSftp.cd(dirPath.toString());
            } else {
                //建立目录
                channelSftp.mkdir(dirPath.toString());
                //切换到新建目录下
                channelSftp.cd(dirPath.toString());
            }
        }

        channelSftp.cd(pathname);

        channelSftp.put(inputStream, fileName);

        //更改权限需要传入8进制数
          /*  channelSftp.chmod(Integer.parseInt("755", 8), pathname );
            channelSftp.chmod(Integer.parseInt("755", 8), pathname + "/*");
*/
        logger.info("chmod success");

        flag = true;

        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }


    public static boolean isDirExit(String dir, ChannelSftp channelSftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = channelSftp.lstat(dir);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    public static Map<String, Object> downloadFile(String remotePath,
                                                   String localPath,
                                                   String interfaceId) {
        List<String> remoteFiles = null;

        new File(localPath).mkdirs();

        HashMap<String, Object> returnMap = new HashMap();

        ChannelSftp channelSftp = null;
        Session session = null;

        try {

            JSch jSch = new JSch();
            session = jSch.getSession(USER, HOST);

            if (session == null) {
                logger.info("session is null");
                returnMap.put("flag", "false");
                return returnMap;
            }

            session.setPassword(PASS);
            session.setPort(SFTP_PORT);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            session.connect();

            if (!session.isConnected()) {
                logger.info("session can not connect");
                returnMap.put("flag", "false");
                return returnMap;
            }

            Channel channel = session.openChannel("sftp");
            channel.connect();

            if (!channel.isConnected()) {
                logger.info("channel can not connect");
                session.disconnect();
                returnMap.put("flag", "false");
                return returnMap;
            }

            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(remotePath);
            Vector<ChannelSftp.LsEntry> files = channelSftp.ls(remotePath);

            remoteFiles = new ArrayList<String>();

            for (ChannelSftp.LsEntry file : files) {

                String fileName = file.getFilename();
                if (!("..".equals(fileName) || ".".equals(fileName))) {
                    if (StringUtils.isEmpty(interfaceId)) {

                        logger.info("开始下载" + file.getFilename());

                        OutputStream os = new FileOutputStream(new File(localPath + File.separator + file.getFilename()));

                        channelSftp.get(file.getFilename(), os);

                        os.close();

                    } else {
                        if (fileName.contains(interfaceId)) {

                            logger.info("开始下载" + file.getFilename());

                            OutputStream os = new FileOutputStream(new File(localPath + File.separator + file.getFilename()));

                            channelSftp.get(file.getFilename(), os);

                            os.close();

                            if (fileName.endsWith(".dat")) {

                                remoteFiles.add(fileName);

                            }
                        } else {
                            logger.info("忽略文件：" + file.getFilename());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("flag", "false");
            return returnMap;
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();

            }
        }

        returnMap.put("flag", "true");
        returnMap.put("files", remoteFiles);
        return returnMap;
    }

    /**
     * ftp上传文件
     *
     * @param remptePath    远程文件路径
     * @param interfaceId 接口id
     * @param loadService
     * @return
     */
    public static boolean uploadFileFTP(String remptePath,String localPath, String interfaceId, LoadService loadService) {

        FTPClient ftpClient = new FTPClient();
        FileInputStream inputStream = null;
        try {
            int reply;

            // 连接FTP服务器
            logger.info("连接FTP服务器:{}：{}", USER, FTP_PORT);
            ftpClient.connect(HOST, FTP_PORT);

            // 登录
            logger.info("登录:{},{}", USER, PASS);
            ftpClient.login(USER, PASS);

            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.info("ftp connect error");
                ftpClient.disconnect();
                return false;
            }

            logger.info("登陆成功！！！");

            // 切换到上传目录
            if (!ftpClient.changeWorkingDirectory(remptePath)) {
                // 如果目录不存在创建目录
                String[] dirs = remptePath.split("/");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    // 进不去目录，说明该目录不存在
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        // 创建目录
                        if (!ftpClient.makeDirectory(tempPath)) {
                            //如果创建文件目录失败，则返回
                            System.out.println("创建文件目录" + tempPath + "失败");
                            return false;
                        } else {
                            //目录存在，则直接进入该目录
                            logger.info("进入：{}", tempPath);
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String[] fileNames = FileUtil.listUploadFile(localPath);
            boolean flag = true;
            for (String fileName :
                    fileNames) {
                if (fileName.contains(interfaceId)) {
                    logger.info("开始上传文件：{}", fileName);
                    inputStream = new FileInputStream(new File(localPath + File.separator + fileName));
                    if (ftpClient.storeFile(fileName, inputStream)) {
                        logger.info("文件[{}]上传成功！！！", fileName);
                    } else {
                        flag = false;
                    }
                }
            }
            ftpClient.logout();

            if (flag) {
                logger.info("文件[]接口成功！！！", interfaceId);
                InterfaceRecord interfaceRecord = new InterfaceRecord();
                interfaceRecord.setInterfaceId(interfaceId);
                interfaceRecord.setRunStep(StateAndTypeConstant.FILE_DOWNLOAD_OR_CREATE);
                interfaceRecord.setTypeDesc(StateAndTypeConstant.TRUE);
                interfaceRecord.setFileName("");
                interfaceRecord.setFileNum("");
                interfaceRecord.setFileSuccessNum("");
                interfaceRecord.setFileTime("");
                loadService.insertRecord(interfaceRecord);
            }

        } catch (IOException e) {
            logger.error("文件上传出现异常{}", e);
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setInterfaceId(interfaceId);
            interfaceRecord.setRunStep(StateAndTypeConstant.FILE_DOWNLOAD_OR_CREATE);
            interfaceRecord.setTypeDesc(StateAndTypeConstant.FALSE);
            interfaceRecord.setFileName("");
            interfaceRecord.setFileNum("");
            interfaceRecord.setFileTime("");
            interfaceRecord.setFileSuccessNum("0");
            interfaceRecord.setErrorDesc("文件上传出现异常:" + e.getMessage().substring(0, 470));
            loadService.insertRecord(interfaceRecord);

        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    logger.error("ftp 断开链接出现异常{}", ioe);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("输入流关闭出现异常{}", e);
                }
            }

        }
        return true;
    }

    /**
     * FTP 下载文件
     *
     * @param remotePath  远程目录
     * @param localPath   本地目录
     * @param interfaceId 接口号
     * @return
     */
    public static boolean downloadFileFTP(String remotePath, String localPath, String interfaceId) throws RuntimeException, IOException {

        logger.info("remotePath:{};localPath{};interfaceId{}", remotePath, localPath, interfaceId);

        boolean flag = false;

        FTPClient ftpClient = new FTPClient();

        int reply;

        // 连接FTP服务器
//        logger.info("连接FTP服务器:{}：{}", HOST, FTP_PORT);
        ftpClient.connect(HOST, FTP_PORT);

        // 登录
//        logger.info("登录:{},{}", USER, PASS);
        ftpClient.login(USER, PASS);

        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            logger.info("ftp connect error :{}", reply);
            ftpClient.disconnect();
            return false;
        }

//        logger.info("登陆成功！！！");

        // 切入到远程目录
        ftpClient.changeWorkingDirectory(remotePath);

        FTPFile[] ftpFiles = ftpClient.listFiles();

        // ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        if (ftpFiles.length > 0) {
            for (FTPFile file :
                    ftpFiles) {
                if (file.getName().contains(interfaceId) && (file.getName().endsWith(".dat")|| file.getName().endsWith(".txt"))) {
                    FileUtil.dirExit(localPath);
                    FileOutputStream out = new FileOutputStream(localPath + File.separator + file.getName());
                    boolean b = ftpClient.retrieveFile(file.getName(), out);
                    if (b) {
                        logger.info("文件：{}下载成功！！！", file.getName());
                        flag = true;
                    } else {
                        logger.info("文件：{}下载失败 ！！！", file.getName());
                    }
                    out.close();
                }
            }
        } else {
            logger.info("remotePath:{} 接口：{} 文件不存在！！！！", remotePath, interfaceId);
        }
        ftpClient.logout();
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
        return flag;
    }
}
