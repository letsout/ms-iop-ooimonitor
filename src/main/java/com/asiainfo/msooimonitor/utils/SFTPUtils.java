package com.asiainfo.msooimonitor.utils;

import com.asiainfo.msooimonitor.exception.FileNotFoundException;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

/**
 * @Author H
 * @Date 2018/10/26 10:03
 * @Desc
 **/

public class SFTPUtils {

    private static final Logger logger = LoggerFactory.getLogger(SFTPUtils.class);
    private static ChannelSftp sftp;

    private static SFTPUtils instance = null;
    private SFTPUtils() {
    }

    public static SFTPUtils getInstance(String host, String username, String password) {
        if (instance == null) {
            if (instance == null) {
                instance = new SFTPUtils();
                //获取连接
                logger.info("开始链接：{}", host);
                sftp = instance.connect(host, 22, username, password);
            }
        }
        return instance;
    }

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */

    public ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("SFTP Session connected.");

            Channel channel = sshSession.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sftp;
    }


    /**
     * @param remoteFile 远程文件全路径,路径不能以'/'结尾
     * @param localFile  本地文件全路径
     * @return
     */


    public void upload(String remoteFile, String localFile,ChannelSftp sftp) {
        FileInputStream localFileStream = null;
        try {
            //远程文件全路径,路径不能以'/'结尾
            if (remoteFile.endsWith("/")) {
                remoteFile = remoteFile.substring(0, remoteFile.length() - 1);
            }
            String remoteDir = remoteFile.substring(0, remoteFile.lastIndexOf("/"));
            System.out.println("making dirs");
            //创建目录
            mkDir(new File(remoteDir));
            Collection<String> list = FileUtil.listFile(localFile);
            if (list.size() > 0) {
                //判断远程文件夹是否有相同的文件名称
                Vector<ChannelSftp.LsEntry> files = sftp.ls(remoteDir);
                list.remove(files);
                for (String name :
                        list) {
                    localFileStream = new FileInputStream(new File(localFile + File.separator + name));
                    logger.info("{} uploading....", name);
                    sftp.put(localFileStream, remoteFile);
                }
            } else {
                logger.info("该目录{}下无需要上传的文件", localFile);
                return;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (localFileStream != null) {
                    localFileStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("upload complete!");
            disconnect(sftp);
        }
    }

    /**
     * @param remotePath 远程路径
     * @param localPath  本地路径,必须以'/'结尾
     * @return
     */

    public Boolean download(String remotePath, String localPath, String interfaceId, String host, int port, String userName, String password) {

        ChannelSftp sftp = connect(host, port, userName, password);

        Boolean flag = false;
        try {
            // 本地路径,必须以'/'结尾
            if (!localPath.endsWith("/")) {
                localPath += "/";
            }
            File directory = new File(localPath);
            mkDir(directory);

            Vector<ChannelSftp.LsEntry> files = sftp.ls(remotePath);

            if (files.size() <= 2) {
                System.out.println("该目录下没有文件");
                return flag;
            }

            sftp.cd(remotePath);

            for (ChannelSftp.LsEntry file : files) {
                String fileName = file.getFilename();
                if (fileName.equals(".") || fileName.equals("..")) {
                    continue;
                }
                if (fileName.contains(interfaceId) && fileName.endsWith("dat")) {
                    System.out.println(fileName + "is downloading.");
                    OutputStream os = new FileOutputStream(new File(localPath + fileName));
                    sftp.get(fileName, os);
                    os.close();
                    logger.info("download file {}  success !!!", fileName);
                    flag = true;
                } else {
                    logger.info("download file {}  error !!!", fileName);
                    throw new FileNotFoundException("该文件不存在");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("download complete!");
            disconnect(sftp);
        }

        return flag;
    }


    public void disconnect(ChannelSftp sftp) {
        try {
            sftp.getSession().disconnect();
            sftp.quit();
            sftp.disconnect();
        } catch (JSchException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * 根据传入参数新建文件夹
     *
     * @param dir
     * @throws Exception
     */

    public void mkDir(File dir) throws Exception {
        if (!dir.exists()) {
            logger.info("文件夹：{}不存在，新建！！", dir.toPath());
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                logger.info("新建文件夹：{}失败", dir.toPath());
            }
        }
    }


    /**
     * 判断远程文件夹是否存在
     *
     * @param dir
     * @return
     */

    private boolean isDirExit(String dir, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(dir);
            isDirExistFlag = sftpATTRS.isDir();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return isDirExistFlag;
    }

}

