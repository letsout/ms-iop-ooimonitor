package com.asiainfo.msooimonitor.handle;

import com.asiainfo.msooimonitor.mapper.dbt.common.CommonMapper;
import com.asiainfo.msooimonitor.service.LoadService;
import com.asiainfo.msooimonitor.utils.FileUtil;
import com.asiainfo.msooimonitor.utils.SpringUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author H
 * @Date 2019/1/7 16:27
 * @Desc 解析xml处理上传数据
 **/
@Component
public class HandleXml {

    private static final Logger logger = LoggerFactory.getLogger(HandleXml.class);

    @Autowired
    private CommonMapper handelXmlMapper;

    @Autowired
    private LoadService loadService;

    private HashMap<String, String> replaceHashMap;

    private HashMap<String, List<Map<String, String>>> replaceListMap = new HashMap<>();


    /**
     * 处理xml生成文件
     *
     * @param date 文件生成日期
     */
    public boolean doXml(Date date, String interfaceId, String localPath) {
        if (date == null) {
            date = new Date();
        }
        String nowTime = TimeUtil.getLongSeconds(date);
        String nowDay = TimeUtil.getDaySql(date);
        String lastDay = TimeUtil.getLastDaySql(date);
        String twoDaysAgo = TimeUtil.getTwoDaySql(date);
        String threeDaysAgo = TimeUtil.getThreeDaySql(date);
        String nowMonth = TimeUtil.getMonthSql(date);
        String lastMonth = TimeUtil.getLastMonthSql(date);

        /**
         * 定义要替换标量的hashmap
         */
        replaceHashMap = new HashMap<>();
        replaceHashMap.put("nowTime", nowTime);
        replaceHashMap.put("nowDay", nowDay);
        replaceHashMap.put("lastDay", lastDay);
        replaceHashMap.put("twoDaysAgo", twoDaysAgo);
        replaceHashMap.put("threeDaysAgo", threeDaysAgo);
        replaceHashMap.put("nowMonth", nowMonth);
        replaceHashMap.put("lastMonth", lastMonth);


        //使用SAXReader读取XMl
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(new File(this.getClass().getResource("/xml/" + interfaceId + ".xml").getPath()));
//            Document document = saxReader.read(new File("/data1/miscro-service/ms-ooi/ms-ooi-monitor/xml/" + interfaceId + ".xml"));

            Element root = document.getRootElement();

            List<Element> elements = root.elements();

            /**
             * 处理xml
             */
            for (Element element : elements) {
                /**
                 * 处理constant标签 定义常量部分
                 */
                if (element.getName().equals("constant")) {
                    logger.info("处理constant标签 定义常量部分!!!!");
                    List<Element> constantElements = element.elements();
                    for (Element constantElement : constantElements) {
                        ArrayList<String> replaceList = new ArrayList<>();
                        constantElement.attributes().stream().filter(attr -> attr.getName().substring(0, 10).equals("replaceStr")).forEach(attribute -> replaceList.add(attribute.getValue()));
                        if (replaceList.isEmpty()) {
                            replaceHashMap.put(constantElement.getName(), constantElement.getText());
                        } else {
                            replaceList.forEach(
                                    list -> replaceHashMap.put(constantElement.getName(), constantElement.getText().replace("$" + list, replaceHashMap.get(list)))
                            );
                        }
                    }

                }
                /**
                 * 处理sql并运行
                 */

                if (element.getName().equals("sql")) {
                    logger.info("处理sql并运行!!!");
                    List<Element> sqlElements = element.elements();
                    for (Element sqlElement : sqlElements) {
                        doElenemt(sqlElement);
                    }
                }
            }
            /**
             * 导出数据部分
             */
            doCurcos(interfaceId, localPath);
        } catch (Exception e) {
            logger.error("文件生成失败！！！[{}]", e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * 处理指定模块元素
     *
     * @param element
     */
    public void doElenemt(Element element) {
        try {
            String elementName = null;
            if (element.getName().startsWith("step")) {
                elementName = "step";
            } else {
                elementName = element.getName();
            }
            //去返值名称
            Attribute replace = element.attribute("replaceType");
            switch (elementName) {
                case "return":
                    /**
                     * sql 查询返回值 name
                     */
                    //取type值
                    String retrunType = element.attributeValue("type");
                    //是否有有变量
                    String returnName = element.attributeValue("name");
                    ArrayList<String> returnReplaceList = new ArrayList<>();
                    replaceHashMap.put("returnSql", element.getText());
                    if ("String".equals(retrunType)) {
                        if (replace != null) {
                            element.attributes().stream().filter(attr -> attr.getName().substring(0, 10).equals("replaceStr")).forEach(attribute -> returnReplaceList.add(attribute.getValue()));
                            returnReplaceList.forEach(list -> replaceHashMap.put("returnSql", replaceHashMap.get("returnSql").replace("$" + list, replaceHashMap.get(list))));
                        }
                        String str = handelXmlMapper.getValue(replaceHashMap.get("returnSql"));
                        replaceHashMap.put(returnName, str);
                    } else if ("Map".equals(retrunType)) {
                        if (replace != null) {
                            element.attributes().stream().filter(attr -> attr.getName().substring(0, 10).equals("replaceStr")).forEach(attribute -> returnReplaceList.add(attribute.getValue()));
                            String mapName = element.attributeValue("replaceMapName");
                            List<Map<String, String>> mapList = replaceListMap.get(mapName);
                            for (Map<String, String> map :
                                    mapList) {
                                returnReplaceList.forEach(str -> replaceHashMap.put("returnSql", replaceHashMap.get("returnSql").replace("$" + str, map.get(str))));
                            }
                        }
                        List<Map<String, String>> returnmap = handelXmlMapper.getMap(replaceHashMap.get("returnSql"));
                        replaceListMap.put(returnName, returnmap);
                        break;
                    } else {
                        logger.error("类型解析出错，暂不支持此类型[{}]", retrunType);
                    }
                case "if":
                    /**
                     * 进行简单条件判断操作
                     */
                    String test = element.attributeValue("test");
                    if (test.contains("$")) {
                        test = test.replace("$", "");
                    }
                    if (test.contains("==")) {
                        String[] split = test.split("==");
                        if (split[1].trim().equals(replaceHashMap.get(split[0].trim()))) {
                            element.elements().forEach(ele -> doElenemt(ele));
                        }
                    } else if (test.contains("!=")) {

                    } else if (test.contains(">")) {
                        if (test.contains("=")) {
                            String[] split = test.split(">=");
                            if (Integer.valueOf(split[1].trim()) >= Integer.valueOf(replaceHashMap.get(split[0].trim()))) {
                                element.elements().forEach(ele -> doElenemt(ele));
                            }
                        } else {
                            String[] split = test.split(">");
                            Arrays.stream(split).filter(str -> str.contains("$")).forEach(str1 -> str1.replace(str1, replaceHashMap.get(str1.substring(1))));
                            if (Integer.valueOf(split[1].trim()) > Integer.valueOf(replaceHashMap.get(split[0].trim()))) {
                                element.elements().forEach(ele -> doElenemt(ele));
                            }
                        }
                    } else if (test.contains("<")) {
                        if (test.contains("=")) {
                            String[] split = test.split("<=");
                            if (Integer.valueOf(split[1].trim()) <= Integer.valueOf(replaceHashMap.get(split[0].trim()))) {
                                element.elements().forEach(ele -> doElenemt(ele));
                            }
                        } else {
                            String[] split = test.split("<");
                            if (Integer.valueOf(split[1].trim()) < Integer.valueOf(replaceHashMap.get(split[0].trim()))) {
                                element.elements().forEach(ele -> doElenemt(ele));
                            }
                        }
                    } else {
                        logger.error("暂未定义此符号，请使用 ==,!=,>,<,>=,<= ");
                    }
                    break;
                case "step":
                    /**
                     * 运行sql
                     */
                    ArrayList<String> stepReplaceList = new ArrayList<>();
                    element.attributes().stream().filter(attr -> attr.getName().substring(0, 10).equals("replaceStr")).forEach(attribute -> stepReplaceList.add(attribute.getValue()));
                    HashMap<String, String> stepMap = new HashMap<>();
                    stepMap.put("replacesql", element.getText());
                    if (replace != null) {
                        if ("Map".equals(element.attributeValue("replaceType"))) {
                            List<Map<String, String>> mapList = replaceListMap.get(element.attributeValue("replaceName"));
                            for (Map<String, String> map :
                                    mapList) {
                                stepReplaceList.forEach(list -> stepMap.put("replacesql", stepMap.get("replacesql").replace("$" + list, map.get(list.toUpperCase()))));
                            }
                        } else if ("String".equals(element.attributeValue("replaceType"))) {
                            stepReplaceList.forEach(str -> stepMap.put("replacesql", stepMap.get("replacesql").replace("$" + str, replaceHashMap.get(str))));
                        } else {
                            logger.error("类型解析出错，暂不支持此类型[{}]", element.attributeValue("replaceType"));
                        }

                    }
                    logger.info("{} : {}", element.getName(), stepMap.get("replacesql"));
                    handelXmlMapper.insertSql(stepMap.get("replacesql"));
                    break;
                case "foreach":
                    replaceHashMap.put("foreachSql", element.getText());
                    ArrayList<String> foreachReplaceList = new ArrayList<>();
                    element.attributes().stream().filter(attr -> attr.getName().substring(0, 10).equals("replaceStr")).forEach(attribute -> foreachReplaceList.add(attribute.getValue()));
                    List<Map<String, String>> mapList = replaceListMap.get(element.attributeValue("replaceName"));
                    for (Map<String, String> map :
                            mapList) {
                       /* for (String str:
                        foreachReplaceList) {
                            String s = map.get(str.toUpperCase());
                            String foreachSql = replaceHashMap.get("foreachSql").replace("$" + str, map.get(str.toUpperCase()));
                            replaceHashMap.put("foreachSql",replaceHashMap.get("foreachSql").replace("$"+str,map.get(str.toUpperCase())));
                        }*/
                        foreachReplaceList.forEach(each -> replaceHashMap.put("foreachSql", replaceHashMap.get("foreachSql").replace("$" + each, map.get(each.toUpperCase()))));
                        handelXmlMapper.insertSql(replaceHashMap.get("foreachSql"));
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("--------->{} Error:{}", element.getName(), e);
            throw new RuntimeException();
        }

    }

    /**
     * 使用游标获取信息
     *
     * @param interfaceId 接口号
     */
    public void doCurcos(String interfaceId, String path) throws Exception {


        logger.info("处理导出数据部分");
        FileOutputStream dataFileWriter;
        FileOutputStream verfFileWrite;

        String name = replaceHashMap.get("FILE_NAME");
        if (path == null || ("").equals(path)) {
            path = replaceHashMap.get("binPath");
        }
        String className = replaceHashMap.get("className");
        String fileName = name + "_001.dat";
        int count = 1;
        long nowFileSize = 0;
        // 文件名个数
        int filecount = 1;
        // 判断即将写入的信息是否超过文件大小次数，第一次是还是写入源文件,写的时候不换行，当第二次是换成下一个文件并且做换行处理
        String splitCount = "0";
        // 300M
        long fileSize = 1024 * 1024 * 300;

        logger.info("dataFile:{}", path + File.separator + fileName);
        logger.info("verfFileWrite:{}", path + File.separator + name + ".verf");

        FileUtil.creatFile(path, File.separator + fileName);
        dataFileWriter = new FileOutputStream(path + File.separator + fileName);
        verfFileWrite = new FileOutputStream(path + File.separator + name + ".verf");

        MyBatisCursorItemReader myBatisCursorItemReader = (MyBatisCursorItemReader) SpringUtil.getBean("myBatisCursorItemReader");

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("tableName", "fcm.iop_" + interfaceId + "_upload");

        myBatisCursorItemReader.setParameterValues(paramMap);
        //查询表中有多行
        int rows = loadService.getrows("fcm.iop_" + interfaceId + "_upload");
        myBatisCursorItemReader.open(new ExecutionContext());

        Class<?> c = Class.forName(className);
        Object object;
        Method method = c.getDeclaredMethod("toString");

        // 文件是否换行标识 true 换行
        boolean flag = true;
        while ((object = myBatisCursorItemReader.read()) != null) {

            String invoke = (String) method.invoke(object, null);

            logger.info("invoke:{}", invoke);

            nowFileSize = nowFileSize + invoke.getBytes().length;

            // logger.info(nowFileSize + ">" + fileSize);

            if (nowFileSize > fileSize) {
                if (splitCount == "1") {
                    dataFileWriter.flush();
                    dataFileWriter.close();

                    String newPath = path + File.separator + fileName;

                    FileUtil.creatFile(path, File.separator + fileName);
                    write(verfFileWrite, fileName + "@" + FileUtil.getFileSize(newPath) + "@" + FileUtil.getFileRows(newPath) + "@" + replaceHashMap.get("nowDay") + "@" + replaceHashMap.get("nowTime") + System.getProperty("line.separator"), "E");

                    filecount++;
                    fileName = name + "_00" + filecount + ".dat";

                    dataFileWriter = new FileOutputStream(path + File.separator + fileName);

                    fileSize = nowFileSize + nowFileSize;
                    flag = true;
                    splitCount = "0";

                    logger.info("第一次文件数量超过,生成新的文件，aplitCount:{},flag{},nowFileSize{},fileSize{}", splitCount, flag, nowFileSize, fileSize);
                } else {
                    splitCount = "1";

                    flag = false;

                    logger.info("第一次文件数量超过，aplitCount:{},flag{},nowFileSize{},fileSize{}", splitCount, flag, nowFileSize, fileSize);
                }
            }

            /* logger.info(rows + "=" + count + flag);*/

            if (!flag || rows == count) {

                write(dataFileWriter, invoke, "E");

            } else {

                write(dataFileWriter, invoke, "U");

            }

            count++;
        }
        dataFileWriter.flush();
        dataFileWriter.close();

        write(verfFileWrite, fileName + "@" + FileUtil.getFileSize(path + File.separator + fileName) + "@" + FileUtil.getFileRows(path + File.separator + fileName) + "@" + replaceHashMap.get("nowDay") + "@" + replaceHashMap.get("nowTime"), "E");

        verfFileWrite.flush();
        verfFileWrite.close();

        myBatisCursorItemReader.close();
        logger.info("数据导出完成：{},路径：{}", fileName, path);
    }

    public void write(FileOutputStream writer, String str, String flag) throws Exception {
        //      logger.info("str:{}",str);
        byte[] bytes = {(byte) 0x80};
        String[] split = str.split("@");
        for (int i = 0; i < split.length; i++) {
            // 最后一个字符的写入方式
            if (i == split.length - 1) {
                if (!"null".equals(split[i])) {
                    writer.write(split[i].getBytes("gbk"));
                }
                break;
            }
            // 字段为空的写入方式
            if ("null".equals(split[i]) || StringUtils.isEmpty(split[i])) {
                writer.write(bytes);
            } else {
                writer.write(split[i].getBytes("gbk"));
                writer.write(bytes);
            }

        }
        if ("U".equals(flag)) {
            writer.write(System.getProperty("line.separator").getBytes("gbk"));
        }

    }
}
