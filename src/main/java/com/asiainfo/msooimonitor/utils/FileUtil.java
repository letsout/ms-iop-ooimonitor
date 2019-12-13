package com.asiainfo.msooimonitor.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author H
 * @Date 2019/1/17 17:56
 * @Desc 操作文件工具类
 **/
@Slf4j
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 根据参数生成文件
     *
     * @param path     文件路径
     * @param fileName 文件名
     */
    public static void creatFile(String path, String fileName) {
        Boolean flag = false;
        dirExit(path);
        fileExit(path + File.separator + fileName);
    }

    /**
     * 判断文件夹是否存在，不存在则创建
     *
     * @param dirath 文件夹路路径
     */
    public static void dirExit(String dirath) {
        File file = new File(dirath);
        if (file.exists()) {
            if (!file.isDirectory()) {
                logger.info("the same name file is exit so mkdir {}", dirath);
                file.mkdir();
            }
        } else {
            logger.info("{} Directory is not exit so mkdir", dirath);
            file.mkdirs();
        }

    }

    /**
     * 判断指定路径的文件是否存在，不存在则创建
     *
     * @param fileName 文件名
     */
    public static void fileExit(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                logger.error("{} file is create file", fileName);
            }
        }

    }

    /**
     * 计算文件大小（M）
     *
     * @param filePath 文件全路径
     * @return
     */
    public static String getFileSize(String filePath) {
        File file = new File(filePath);
        return String.valueOf(file.length());
    }

    /**
     * 计算文件行数
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileRows(String filePath) {
        int rows = 0;
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            lineNumberReader.skip(Long.MAX_VALUE);
            rows = lineNumberReader.getLineNumber();
            /*if(rows != 0){
                rows +=1;
            }*/
            fileReader.close();
            lineNumberReader.close();
        } catch (Exception e) {
            log.error("运行异常："+e);e.printStackTrace();

        }
        return String.valueOf(rows);
    }

    /**
     * 遍历指定文件夹下的文件名
     *
     * @param dir
     * @return List文件名
     */
    public static List<String> listFile(String dir) {
        List<String> dat = null;
       // dir = "H:\\data1\\vgop_iop\\iop-OOI\\sbin-data\\download\\20190822\\day";
        File file = new File(dir);
        String[] list = file.list();
        try {
            dat = Arrays.stream(list).filter(name -> name.endsWith("dat") || name.endsWith(".txt")).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("遍历指定文件夹下的文件名出错：{}", dir);
        }
        return dat;
    }

    /**
     * 遍历指定文件夹下的文件名
     *
     * @param dir
     * @return List文件名
     */
    public static String[]  listUploadFile(String dir) {
        List<String> dat = null;
        // dir = "H:\\data1\\vgop_iop\\iop-OOI\\sbin-data\\download\\20190822\\day";
        File file = new File(dir);
        String[] list = file.list();
        return list;
    }


    /**
     * 文件内容格式转换
     *
     * @param name        文件名
     * @param oldPath     当前文件路径
     * @param newPath     转换后文件路径
     * @param fromCharset 当前文件编码格式
     * @param toCharset   转换后文件编码格式
     * @throws IOException
     */
    public static void convent(String name, String oldPath, String newPath, String fromCharset, String toCharset) throws IOException {

      /*  for (String fileName :
                name) {*/


        String oldPath1 = oldPath + File.separator + name;
        String newPath1 = newPath + File.separator + name;
        // 判断文件是否存在
        dirExit(oldPath);
        dirExit(newPath);

        FileInputStream fileInputStream = new FileInputStream(oldPath1);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, fromCharset);
        BufferedReader rw = new BufferedReader(inputStreamReader);

        FileOutputStream fileOutputStream = new FileOutputStream(newPath1);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, toCharset);
        BufferedWriter bw = new BufferedWriter(outputStreamWriter);

        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = rw.readLine()) != null) {
            sb.append(line).append("/n");
        }

        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

/*

    }
*/

    /**
     * 根据接口号判断文件是否存在
     *
     * @param path
     * @param interfaceId
     * @return
     */
    public static Boolean fileIsExit(String path, String interfaceId) {
        File file = new File(path);
        String[] list = file.list();
        List<String> dat = Arrays.stream(list).filter(name -> name.endsWith("dat") && name.contains(interfaceId)).collect(Collectors.toList());
        if (dat.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static int[] binstrToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = temp[i] - 48;
        }
        return result;
    }

    public static char binstrToChar(String binStr) {
        int[] temp = binstrToIntArray(binStr);
        int sum = 0;
        for (int i = 0; i < temp.length; i++) {
            sum += temp[temp.length - 1 - i] << i;
        }
        return (char) sum;
    }

    /**
     * 删除文件最后一行为空行的
     *
     * @param path file全路径
     *             a_13000_20190214_IOP-93001_00_001.dat
     *             a_13000_20190214_IOP-93002_00.verf
     */
    public static void delline(String path, String fileName) {

        File file = new File(path + File.separator + fileName);
        File tmpfile = new File(path + File.separator + fileName + "tmp");

        file.renameTo(tmpfile);
        try {

            FileInputStream fileInputStream = new FileInputStream(path + File.separator + fileName + "tmp");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            OutputStreamWriter outwriter = new OutputStreamWriter(new FileOutputStream(path + File.separator + fileName), "utf-8");


            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line != "/n") {
                    outwriter.write(line);
                }
            }
            outwriter.flush();
            outwriter.close();
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

        } catch (Exception e) {
            log.error("运行异常："+e);e.printStackTrace();

        }
    }

    /**
     * 文件分割，每个300M
     *
     * @param srcPath  文件路径
     * @param fileSize 分割大小
     * @param fileName 文件名称
     */
    public static void splitFile(String srcPath, int fileSize, String fileName) {
        if ("".equals(srcPath) || srcPath == null || fileName == null || "".equals(fileName)) {
            logger.info("分割文件参数出现问题,srcPath:{},fileName{}", srcPath, fileName);
            return;
        }
        File srcFile = new File(srcPath + File.separator + fileName);
        long length = srcFile.length();//源文件大小
        long destSize = 1024 * 1024 * fileSize;//目标文件大小

        int num = (int) (length / destSize);

        num = length % destSize == 0 ? num : num + 1;//分割后文件的数目

        logger.info("文件{}，分割为{}个文件", fileName, num);

        InputStreamReader in = null;
        BufferedReader buffer = null;
        String line = "";
        try {
            in = new InputStreamReader(new FileInputStream(srcFile), "GBK");
            buffer = new BufferedReader(in);
            for (int i = 0; i < num; i++) {

                String file = srcPath + File.separator + fileName + "0" + i + "dat";
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
                int count = 0;
                while ((line = buffer.readLine()) != null) {

                    writer.write(line);

                }
            }
        } catch (Exception e) {

        }

    }


    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

   /* public static void main(String[] args) throws IOException {

        FileOutputStream fos = null ;
        File file = new File("D://test.text");
        *//*byte[]   b   =   {(byte)0x80,(byte)0x0d,(byte)0x0a};

        b[0]   =   (byte)0x80;*//*
        byte[] bytes = {(byte) 0x80};
        try {
            if(!file.exists()){
                file.createNewFile();
                fos = new  FileOutputStream(file);
                fos.write("20090515".getBytes());
                fos.write(bytes);
                fos.write("a中国".getBytes());
                fos.write(bytes);
                fos.write("20090516".getBytes());
                fos.write(bytes);
                fos.write("aaaaaaa中国".getBytes());
                fos.write(bytes);
            }
        }catch (Exception e){
        logger.error("error ");
        }finally {
            if(fos !=null){
                fos.close();
            }
        }

    }*/

}

