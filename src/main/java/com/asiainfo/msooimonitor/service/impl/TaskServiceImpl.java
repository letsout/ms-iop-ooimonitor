package com.asiainfo.msooimonitor.service.impl;

import com.asiainfo.msooimonitor.config.SendMessage;
import com.asiainfo.msooimonitor.constant.CommonConstant;
import com.asiainfo.msooimonitor.mapper.dbt.ooi.InterfaceInfoMpper;
import com.asiainfo.msooimonitor.mapper.mysql.GetFileDataMapper;
import com.asiainfo.msooimonitor.model.datahandlemodel.Act93004Info;
import com.asiainfo.msooimonitor.model.datahandlemodel.Act93006Info;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadCountInfo;
import com.asiainfo.msooimonitor.model.datahandlemodel.UploadDetailInfo;
import com.asiainfo.msooimonitor.service.FileDataService;
import com.asiainfo.msooimonitor.service.TaskService;
import com.asiainfo.msooimonitor.service.UploadService;
import com.asiainfo.msooimonitor.thread.WriteFileThread;
import com.asiainfo.msooimonitor.utils.FtpUtil;
import com.asiainfo.msooimonitor.utils.SqlUtil;
import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author yx
 * @date 2019/9/6  17:29
 * Description
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    DecimalFormat df = new DecimalFormat("0.000000");
    @Autowired
    FileDataService fileDataService;
    @Autowired
    SendMessage sendMessage;
    @Autowired
    InterfaceInfoMpper interfaceInfoMpper;
    @Autowired
    GetFileDataMapper getFileDataMapper;


    @Value("${file.path17}")
    private String path17;

    @Value("${ftp.path}")
    private String path228;

    @Autowired
    WriteFileThread writeFileThread;
    @Autowired
    UploadService uploadService;

    @Override
    public void saveAll93003(String month) throws Exception {
        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, Object> map = null;
        List<Map<String, String>> activitys = fileDataService.getData93003(month);
        for (Map<String, String> activity : activitys) {
            map = new HashMap<>(14);
            //1行号
            //2营销活动类型,必填
            map.put("A2", activity.get("activityType"));
            //3统计月份,必填，比如，201704
            map.put("A3", month);
            //4省份,必填，参考9.1省公司编码
            map.put("A4", CommonConstant.SC);
            //5地市,可为空，参考9.2市公司编码，当营销活动涉及多个地市时，以逗号分隔
            map.put("A5", activity.get("cityCode"));
            //6渠道编码,必填，比如,00108xxxx,001：一级分类,08：二级分类,当营销活动涉及多个活动时，以逗号分隔
            map.put("A6", activity.get("channelCode"));
            //7渠道名称,可为空，当营销活动涉及多个活动时，以逗号分隔
            map.put("A7", activity.get("channelName"));
            //8营销活动数,必填
            map.put("A8", activity.get("activityNum"));
            //9活动总客户数（人次）,必填
            map.put("A9", activity.get("allUserNum"));
            //10成功接触用户数（人次）,必填
            map.put("A10", activity.get("successUserNum"));
            //11营销成功用户数（人次）,必填
            map.put("A11", activity.get("successMarkingUserNum"));
            //12预留字段名称,预留字段
            //13运营活动参与用户（人次）,选填
            map.put("A13",activity.get("operateUserNum"));
            //140x0D0A
            list.add(map);
        }
        SqlUtil.getInsert("93003", list);

    }

    @Override
    public void saveBase93004(String activityEndDate) throws Exception {
        interfaceInfoMpper.truncateTable("93004");
        List<UploadDetailInfo> uploadDetailInfoList = new ArrayList<>();
        List<Act93004Info> activityList = getFileDataMapper.getBase93004(activityEndDate);
        List<String> successList = new ArrayList<>();
        for (Act93004Info activity : activityList) {
            String activityId = activity.getActivityId();
            //1行号
            //2省份
            //3营销活动编号
            activity.setActivityId(CommonConstant.SC + activity.getActivityId().substring(1));
            //4营销活动名称
            //5活动开始时间
            activity.setStartTime(TimeUtil.getOoiDate(activity.getStartTime()));
            //6活动结束时间
            //String endTime = sdf.format(activity.get("end_time").toString());
            activity.setEndTime(TimeUtil.getOoiDate(activity.getEndTime()));
            //7子活动编号
            activity.setCampaignId(activity.getCampaignId().substring(1));
            //8子活动名称
            //9子活动开始时间
            //String childStartTime = sdf.format(activity.get("start_time").toString());
            activity.setCampaignStartTime(activity.getStartTime());
            //10子活动结束时间
            //String childEndTime = sdf.format(activity.get("end_time").toString());
            activity.setCampaignEndTime(activity.getEndTime());
            //11渠道编码
            //12渠道编码一级分类0100520917
            activity.setChanneTypeOne(activity.getChannelId().substring(0, 3));
            //13渠道编码二级分类
            activity.setChanneTypeTwo(activity.getChannelId().substring(3, 5));
            //14渠道名称
            //15运营位编码
            //16运营位编码一级分类
            activity.setPositionidOne(activity.getPositionId().substring(0, 3));
            //17运营位编码二级分类
            activity.setPositionidTwo(activity.getPositionId().substring(3, 5));
            //18运营位名称
            //19用户号码OBJ_1090774410875366/A603613154957590528
            //20IMEImap.put("A",startTime);
            //21产品名称
            Map<String, String> baseOfferBo = getFileDataMapper.getBaseOfferBo(activityId);
            String prcName = baseOfferBo.get("prc_name");
            activity.setProName(prcName);
            //22产品编码"0428000" + activity.get("activity_id").toString().substring(1)
            String prcId = baseOfferBo.get("prc_id");
            activity.setProCode(prcId);
            activity.setProCode(activity.getProCodeSplite() + prcId);
            //23产品编码截取
            //240x0D0A
            try {
                interfaceInfoMpper.insertIop93004(activity);
                successList.add(activityId);
            } catch (Exception e) {
                log.error("93004 生成数据出现异常{}", e);
                uploadDetailInfoList.add(UploadDetailInfo.builder()
                        .activityId(activityId)
                        .activityTime(activityEndDate)
                        .interfaceId("93004")
                        .activitytype("base")
                        .failDesc("数据融合出现异常")
                        .build());
            }

        }
//        String activityIds = "'" + StringUtils.join(successList, "','") + "'";
        fileDataService.insertFailDetails(uploadDetailInfoList);
//        getFileDataMapper.updateUploadTime(activityEndDate, activityIds);
        UploadCountInfo uploadCountInfo = new UploadCountInfo();
        uploadCountInfo.setInterfaceId("93004");
        uploadCountInfo.setUploadNum(successList.size());
        uploadCountInfo.setFailNum(uploadDetailInfoList.size());
        uploadCountInfo.setActivityTime(activityEndDate);
        getFileDataMapper.insertUploadCount(uploadCountInfo);

    }

    @Override
    public void saveAll93006(String activityEndDate) throws Exception {
        UploadDetailInfo uploadDetailInfo = null;

        // 清空数据表
        interfaceInfoMpper.truncateTable("93006_info");
        interfaceInfoMpper.truncateTable("93006");
        // 查看当日表是否存在
        int num = interfaceInfoMpper.tableIsExit("iop_public", CommonConstant.EFFECT_DAY_TABLE + activityEndDate);
        if (num == 0) {
            uploadDetailInfo = UploadDetailInfo.builder()
                    .activityId("93006")
                    .activityTime(activityEndDate)
                    .interfaceId("93006")
                    .activitytype("base")
                    .failDesc("明细数据表" + CommonConstant.EFFECT_DAY_TABLE + activityEndDate + "不存在")
                    .build();
            sendMessage.sendSms("93006接口的明细数据表" + CommonConstant.EFFECT_DAY_TABLE + activityEndDate + "不存在");
        } else {
            try {
                log.info("效果明细目标表{}存在，开始加载数据", CommonConstant.EFFECT_DAY_TABLE + activityEndDate);
                //  根据活动id查询集团下发活动id以及iop关联活动
                List<Act93006Info> jtActivityInfos = getFileDataMapper.getJTActivityInfo(activityEndDate);
                for (Act93006Info activityInfo :
                        jtActivityInfos) {
                    activityInfo.setCountTime(TimeUtil.getOoiDate(activityEndDate));
                    activityInfo.setCity(CommonConstant.cityMap.get("1"));
                    activityInfo.setProvince(CommonConstant.SC);
                }

                //省级策划省级执行
                List<Act93006Info> iopActivityInfo = getFileDataMapper.getIOPActivityInfo(activityEndDate);
                log.info("省级策划省级执行的活动有{}条", iopActivityInfo.size());
                for (Act93006Info activityInfo :
                        iopActivityInfo) {
                    activityInfo.setCountTime(TimeUtil.getOoiDate(activityEndDate));
                    activityInfo.setProvince(CommonConstant.SC);
                    activityInfo.setCity(CommonConstant.SC + activityInfo.getActivityId().substring(1));
                    activityInfo.setCity(CommonConstant.cityMap.get(activityInfo.getCity()));
                }
                iopActivityInfo.addAll(jtActivityInfos);

                // 将数据插入gabse表中
                interfaceInfoMpper.insert93006Info(iopActivityInfo);

                // 融合数据
                interfaceInfoMpper.insertiop93006(activityEndDate);
            } catch (Exception e) {
                log.error("93006 生成数据出现异常{}", e);
                uploadDetailInfo = UploadDetailInfo.builder()
                        .activityId("93006")
                        .activityTime(activityEndDate)
                        .interfaceId("93006")
                        .activitytype("base")
                        .failDesc("数据融合出现异常")
                        .build();
            }

        }
        // 获取生成数据量
        int rows = interfaceInfoMpper.getTableRowsByTableName("iop_93006");
        List<UploadDetailInfo> uploadDetailInfoList = new ArrayList<>();
        uploadDetailInfoList.add(uploadDetailInfo);
        fileDataService.insertFailDetails(uploadDetailInfoList);
        UploadCountInfo uploadCountInfo = new UploadCountInfo();
        uploadCountInfo.setInterfaceId("93006");
        uploadCountInfo.setUploadNum(rows);
        uploadCountInfo.setFailNum(0);
        uploadCountInfo.setActivityTime(activityEndDate);
        getFileDataMapper.insertUploadCount(uploadCountInfo);

    }

    @Override
    public void saveMarking93001(String activityEndDate) throws Exception {
        log.info("saveMarking93001运行传输{}数据", activityEndDate);
        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, String> map = null;
        Map<String, Object> resultmap = null;
        List<UploadDetailInfo> uploadDetailInfos = new LinkedList<>();
        String iopActivityIds = "";
        List<Map<String, String>> activitys = getFileDataMapper.getMarkingInfo93001(activityEndDate);
        //属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-50子活动效果评估指标
        for (Map<String, String> activity : activitys) {
            map = new HashMap<>();
            //2统计时间必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getOoiDate(activityEndDate));
            //3省份必填(长度3位),省份与互联网编码参考9.1省公司编码
            map.put("A3", CommonConstant.SC);
            //4地市必填,长度： 每个地市长度3位或4位（未知地市为00000除外）
            map.put("A4", CommonConstant.cityMap.get(activity.getOrDefault("city_id", "1")));

            //营销活动相关信息
            //5营销活动编号必填,前三位必须为省份编码
            String activityId = activity.get("activity_id");
            map.put("A5", activityId);
            //6营销活动名称必填
            map.put("A6", activity.get("activity_name"));
            //7活动开始时间必填,长度14位,为数据生成时间
            map.put("A7", TimeUtil.getOoiDate(activity.get("activity_starttime")));
            //8活动结束时间必填,长度14位,为数据生成时间,活动结束时间不早于活动开始时间
            map.put("A8", TimeUtil.getOoiDate(activity.get("activity_endtime")));
            //9营销活动类型必填，填写枚举值ID
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10营销活动目的必填，填写枚举值ID
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11营销活动描述对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
            //12PCC策略编码（当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.getOrDefault("pcc_id", ""));
            //13所属流程,必填,数字枚举值
            map.put("A13", "1");
//    42	活动专题ID，当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            resultmap.put("A42", activity.get("spetopic_id"));
            //子活动相关信息
            List<Map<String, Object>> campaignedList = getFileDataMapper.getBeforeCampaignedInfo(activityId, activityEndDate);
            for (Map<String, Object> campaignedmap : campaignedList) {
                resultmap = new HashMap<>();
                //14子活动编号必填,参考附录1统一编码规则中的营销子活动编号编码规则,所属流程,为2、8、9、10时，前3位编号为省份编码
                final String iopActivityId = campaignedmap.get("iop_activity_id").toString();
                resultmap.put("A14", "280_" + campaignedmap.get("campaign_id") + "_" + iopActivityId.substring(1));
                //15子活动名称必填
                resultmap.put("A15", campaignedmap.get("activity_name"));
                //16子活动开始时间必填,长度14位,为数据生成时间
                resultmap.put("A16", TimeUtil.getOoiDate(campaignedmap.get("campaign_starttime").toString()));
                //17子活动结束时间必填,长度14位,为数据生成时间,子活动结束时间不早于子活动开始时间
                resultmap.put("A17", TimeUtil.getOoiDate(campaignedmap.get("end_time").toString()));
                //23产品编码必填,前七位需符合8.1产品编码规则
                String proCode = campaignedmap.get("offer_code").toString();
                resultmap.put("A23", proCode);
                //24产品编码截取必填,长度限制7位,（省份截取前7位）
                resultmap.put("A24", proCode.substring(0, 7));
                //25产品名称必填
                resultmap.put("A25", campaignedmap.get("offer_name").toString());
                //26产品分类必填，填写枚举值ID
                resultmap.put("A26", campaignedmap.get("offer_type").toString());
                //27渠道编码必填,比如,00108xxxx,001：一级分类,08：二级分类,Xxxx：自定义渠道编码,编码规则参考8.2渠道和运营位编码规则
                String channel_id = campaignedmap.get("channel_id").toString();
                resultmap.put("A27", channel_id);
                //28渠道编码一级分类必填,长度3位,（省份截取前3位）
                resultmap.put("A28", channel_id.substring(0, 3));
                //29渠道编码二级分类必填,长度2位,（省份截取第4、5位）
                resultmap.put("A29", channel_id.substring(3, 5));
                //30渠道名称必填
                resultmap.put("A30", campaignedmap.get("channel_name"));
                //31渠道类型必填,参考10附录3渠道类型编码,位数为偶数位
                resultmap.put("A31", campaignedmap.get("channe_type"));
                //32渠道接触规则必填
                String channelRule = campaignedmap.get("channel_rule").toString();
                if ("null".equals(channelRule) || "".equals(channelRule)) {
                    channelRule = getFileDataMapper.getBaseChannelInfo(iopActivityId).get("channel_id");
                }
                resultmap.put("A32", channelRule);
                //33时机识别必填，填写枚举值ID
                resultmap.put("A33", campaignedmap.get("time_id"));
                //34时机识别描述可为空
                resultmap.put("A34", campaignedmap.get("time_distindes"));
                //35客户质量情况描述性信息,可为空
                resultmap.put("A35", "");
                //36资源使用情况描述性信息,可为空
                resultmap.put("A36", "");

                //子活动效果评估指标
                Map<String, String> mapEffect = interfaceInfoMpper.getSummaryEffect(iopActivityId, activityEndDate);
                String beforeDate = TimeUtil.getLastDaySql(TimeUtil.strToDate(activityEndDate));
                Map<String, String> mapEffect1 = interfaceInfoMpper.getSummaryEffect(iopActivityId, beforeDate);
                if (mapEffect == null) {
                    uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93001")
                            .activityId(iopActivityId)
                            .activitytype("1")
                            .failDesc("效果数据表ooi_activity_summary_effect为空")
                            .activityTime(activityEndDate)
                            .build());
                    continue;
                }
                if (mapEffect1 == null) {
                    String maxDate = interfaceInfoMpper.getSummaryEffectMaxDate(iopActivityId, beforeDate);
                    if (maxDate == null || maxDate.equals("")) {
                        //43成功接触客户数日指标，必填,口径：inser运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                        resultmap.put("A43", mapEffect.get("touch_num"));
                        //44接触成功率日指标，必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
                        resultmap.put("A44", df.format(Float.parseFloat(mapEffect.get("touhe_rate"))));
                        //45响应率日指标，必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
                        resultmap.put("A45", df.format(Float.parseFloat(mapEffect.get("response_rate"))));
                        //46营销成功用户数日指标，必填；,口径：根据运营目的，成功办理或者成功使用的用户数
                        resultmap.put("A46", mapEffect.get("vic_num"));
                        //47营销成功率NUMBER (20,6)日指标,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%
                        resultmap.put("A47", df.format(Float.parseFloat(mapEffect.get("vic_rate"))));
                        //48	使用用户数,日指标，必填：,	,运营成功用户产生本业务使用行为的用户
                        resultmap.put("A48", mapEffect.get("vic_num"));
                        //494G终端4G流量客户占比日指标，必填且取值小于1；,口径：4G流量客户数/4G终端用户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
                        resultmap.put("A49", df.format(Float.parseFloat(mapEffect.get("terminal_flow_rate"))));
                    } else {
                        iopActivityIds = iopActivityIds + "," + iopActivityId;
                        continue;
                    }
                } else {
                    //成功接触客户数
                    int touchNum = Integer.parseInt(mapEffect.get("touch_num")) - Integer.parseInt(mapEffect1.get("touch_num"));
                    //营销成功用户数
                    int vicNum = Integer.parseInt(mapEffect.get("vic_num")) - Integer.parseInt(mapEffect1.get("vic_num"));
                    //
                    String responseRate = String.valueOf(Float.valueOf(mapEffect.get("response_rate")) - Float.valueOf(mapEffect1.get("response_rate")));
                    DecimalFormat df = new DecimalFormat("0.000000");
                    //43成功接触客户数日指标，必填,口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                    resultmap.put("A43", touchNum);
                    //44接触成功率日指标，必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
                    resultmap.put("A44", df.format(Float.valueOf(mapEffect.get("touhe_rate")) - Float.valueOf(mapEffect1.get("touhe_rate"))));
                    //45响应率日指标，必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
                    resultmap.put("A45", df.format(Float.parseFloat(responseRate)));
                    //resultmap.put("A44", String.valueOf(Float.valueOf(mapEffect.get("response_rate")) - Float.valueOf(mapEffect1.get("response_rate"))));
                    //46营销成功用户数日指标，必填；,口径：根据运营目的，成功办理或者成功使用的用户数
                    resultmap.put("A46", vicNum);
                    //47营销成功率NUMBER (20,6)日指标,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%
                    resultmap.put("A47", df.format(Float.parseFloat(responseRate)));
                    //48	使用用户数		日指标，必填：,	,运营成功用户产生本业务使用行为的用户
                    resultmap.put("A48", mapEffect.get("vic_num"));
                    //494G终端4G流量客户占比日指标，必填且取值小于1；,口径：4G流量客户数/4G终端用户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
                    resultmap.put("A49", df.format(Float.parseFloat(mapEffect.get("terminal_flow_rate"))));
                }

                //18目标客户群编号必填
                resultmap.put("A18", mapEffect.get("customer_group_id"));
                //19目标客户群名称必填
                resultmap.put("A19", mapEffect.get("customer_group_name"));
                //20目标客户群规模可为空
                resultmap.put("A20", mapEffect.get("customer_num"));
                //21目标客户群描述可为空
                resultmap.put("A21", "");
                //22目标客户筛选标准必填
                resultmap.put("A22", mapEffect.get("customer_filter_rule"));
                //50 4G流量客户数,日指标，选填，口径：统计周期内，使用4G网络产生4G流量的客户数
                map.put("A50", "");
                resultmap.putAll(map);
                list.add(resultmap);
            }
        }
        if (StringUtils.isNotEmpty(iopActivityIds)) {
            iopActivityIds = iopActivityIds.substring(1);
            String message = "93001接口的活动：" + iopActivityIds + "在" + TimeUtil.getLastDaySql(TimeUtil.strToDate(activityEndDate)) + "出现效果数据断层情况，请核查";
            sendMessage.sendSms(message);
        }
        SqlUtil.getInsert("93001", list);

        fileDataService.insertFailDetails(uploadDetailInfos);
        UploadCountInfo uploadCountInfo = new UploadCountInfo();
        uploadCountInfo.setInterfaceId("93001");
        uploadCountInfo.setUploadNum(list.size());
        uploadCountInfo.setFailNum(uploadDetailInfos.size());
        uploadCountInfo.setActivityTime(activityEndDate);
        getFileDataMapper.insertUploadCount(uploadCountInfo);
    }

    @Override
    public void saveMarking93005(String activityEndDate) throws Exception {
        log.info("saveMarking93005运行传输{}数据", activityEndDate);
        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, Object> map = null;
        List<UploadDetailInfo> uploadDetailInfos = new LinkedList<>();
        List<Map<String, String>> activitys = fileDataService.getMarkingInfo93005(activityEndDate);
        //属性编码 5-13、18-33为营销活动相关信息，14-17子活动相关信息，40-84营销活动效果评估指标
        for (Map<String, String> activity : activitys) {
            map = new HashMap<>();
            //1 行号
            //2 统计时间 必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getOoiDate(activityEndDate));
            //3 省份 必填(长度3位),省份与互联网编码参考9.1省公司编码
            map.put("A3", CommonConstant.SC);
            //4 地市 必填 ,长度： 每个地市长度3位或4位（未知地市为00000除外）
            map.put("A4", CommonConstant.cityMap.get(activity.getOrDefault("city_id", "1")));

            /**
             *  5-13、18-33为营销活动相关信息
             */
            //5 营销活动编号 必填,前三位必须为省份编码
            String activityId = activity.get("activity_id");
            map.put("A5", activityId);
            //6 营销活动名称 必填
            map.put("A6", activity.get("activity_name"));
            //7 活动开始时间 必填,长度14位
            map.put("A7", TimeUtil.getOoiDate(activity.get("activity_starttime")));
            //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
            map.put("A8", TimeUtil.getOoiDate(activity.get("activity_endtime")));
            //9 营销活动类型 必填，填写枚举值ID
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10 营销活动目的 必填，填写枚举值ID
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11 营销活动描述 对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
            //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13 所属流程, 必填，填写枚举值ID
            map.put("A13", "1");        //待定
            //39,活动专题ID，当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A39", activity.get("spetopic_id"));
            // 根据集团下发活动获取效果信息
            final Map<String, String> mapEffect = fileDataService.getSummaryEffectAll(activityId, activityEndDate);
            if (mapEffect == null) {
                uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93005")
                        .activityId(activityId)
                        .activitytype("1")
                        .failDesc("子活动和的效果数据表SummaryEffect为空")
                        .activityTime(activityEndDate)
                        .build());
                continue;
            }
            //18 目标客户群编号 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A18", mapEffect.get("customer_group_id"));
            //19 目标客户群名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A19", mapEffect.get("customer_group_name"));
            //20 目标客户群规模 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A20", mapEffect.get("customer_num"));
            //21 目标客户群描述 可为空
            map.put("A21", "");
            //22 目标客户筛选标准 必填
            map.put("A22", mapEffect.get("customer_filter_rule"));

            //23 产品编码 可为空
            //比如0200100xxxx
            //02：一级分类
            //001：二级分类
            //00：三级分类
            //Xxxx：自定义产品编号
            //编码规则参考8.1产品编码规则
            //当营销活动涉及多子活动时，以逗号分隔
            map.put("A23", activity.get("offer_code"));
            //24 产品名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A24", activity.get("offer_name"));
            //25 产品分类 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A25", activity.get("offer_type"));
            //26 渠道编码 可为空
            //比如
            //00108xxxx
            //001：一级分类
            //08：二级分类
            //Xxxx：自定义渠道编码
            //编码规则参考8.2渠道和运营位编码规则
            //当营销活动涉及多子活动时，以逗号分隔
            map.put("A26", activity.get("channel_id"));
            //27 渠道名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A27", activity.get("channel_name"));
            //28 渠道类型 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A28", activity.get("channel_type"));
            //    map.put("A28", "");
            //渠道类型为偶数位
            //29 渠道接触规则 可为空channel_rule
            //    map.put("A29", "");
            map.put("A29", activity.get("channel_rule"));
            //30 时机识别 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A30", "");
            //31 时机识别描述 可为空
            map.put("A31", activity.get("time_distindes"));
            //    map.put("A31", activity.get("time_distindes"));
            //32 客户质量情况 描述性信息
            //可为空
            map.put("A32", "");
            //33 资源使用情况 描述性信息
            //可为空
            map.put("A33", "");

            /**
             * 14-17子活动相关信息
             */

            //14 子活动编号 可为空，参考附录1统一编码规则中的营销子活动编号编码规则；当营销活动涉及多子活动时，以逗号分隔
            map.put("A14", activity.get("campaign_id"));
            //15 子活动名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A15", activity.get("campaign_name"));
            //16 子活动开始时间 可为空,格式：YYYYMMDDHH24MISS,示例：20170213161140
            String campaignStarttime = activity.get("campaign_starttime");
            map.put("A16", campaignStarttime.split(",")[0]);
            //17 子活动结束时间 可为空,格式：YYYYMMDDHH24MISS,子活动结束时间不早于子活动开始时间,示例：20170213161140
            String campaignEndtime = activity.get("campaign_starttime");
            map.put("A17", campaignEndtime.split(",")[0]);
            /**
             * 40-84营销活动效果评估指标
             */
            //40 成功接触客户数 必填
            //口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
            map.put("A40", mapEffect.get("touch_num"));
            //41 接触成功率 必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
            map.put("A41", mapEffect.get("touhe_rate"));
            //42 响应率 必填且取值小于1,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A42", mapEffect.get("response_rate"));
            //43 营销成功用户数 必填,口径：根据运营目的，成功办理或者成功使用的用户数
            map.put("A43", mapEffect.get("vic_num"));
            //44 营销成功率 必填且取值小于1,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A44", mapEffect.get("vic_rate"));
            //45,使用用户数,必填,运营成功用户产生本业务使用行为的用户
            map.put("A45", mapEffect.get("vic_num"));
            //46,活动总用户数,必填：
            String allActivityUserCount = mapEffect.get("customer_num");
            map.put("A46", allActivityUserCount);
            //47,PCC签约用户数,选填：,该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
            map.put("A47", "");
            //48,PCC策略生效用户数,选填：,该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
            map.put("A48", "");
            // 49,PCC策略生效次数,选填：,该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
            map.put("A49", "");
            // 50,签约客户转化率,选填：,该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）,例：填0.1代表10%
            map.put("A50", "");
            list.add(map);
        }
        SqlUtil.getInsert("93005", list);

        fileDataService.insertFailDetails(uploadDetailInfos);
        UploadCountInfo uploadCountInfo = new UploadCountInfo();
        uploadCountInfo.setInterfaceId("93005");
        uploadCountInfo.setUploadNum(list.size());
        uploadCountInfo.setFailNum(uploadDetailInfos.size());
        uploadCountInfo.setActivityTime(activityEndDate);
        getFileDataMapper.insertUploadCount(uploadCountInfo);
    }

    @Override
    public void saveBase93005(String activityEndDate) throws Exception {
        log.info("saveBase93005运行传输{}数据", activityEndDate);

        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, Object> map = null;
        List<UploadDetailInfo> uploadDetailInfos = new LinkedList<>();
        List<Map<String, Object>> activitys = getFileDataMapper.getBaseInfo93005(activityEndDate);
        for (Map<String, Object> activity : activitys) {
            map = new HashMap<>();
            //1 行号
            //2 统计时间 必填,长度14位,为数据生成时间
            map.put("A2", TimeUtil.getOoiDate(activityEndDate));
            //3 省份 必填(长度3位),省份与互联网编码参考9.1省公司编码
            map.put("A3", CommonConstant.SC);
            //4 地市 必填 ,长度： 每个地市长度3位或4位（未知地市为00000除外）
            map.put("A4", CommonConstant.cityMap.get(activity.getOrDefault("city_id", "1")).toString());
            //5 营销活动编号 必填,前三位必须为省份编码
            String activityId = activity.get("activity_id").toString();
            map.put("A5", CommonConstant.SC + activityId.substring(1));
            //6 营销活动名称 必填
            map.put("A6", activity.get("activity_name").toString());
            //7 活动开始时间 必填,长度14位
            String start_time = activity.get("start_time").toString();
            map.put("A7", TimeUtil.getOoiDate(start_time));
            //8 活动结束时间 必填，长度14位，活动结束时间不早于活动开始时间
            String end_time = activity.get("end_time").toString();
            map.put("A8", TimeUtil.getOoiDate(end_time));
            //9 营销活动类型 必填，填写枚举值ID
            map.put("A9", activity.getOrDefault("activity_type", "9").toString());
            //10 营销活动目的 必填，填写枚举值ID
            map.put("A10", activity.getOrDefault("activity_objective", "9").toString());
            //11 营销活动描述 对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")).toString());
            //12 PCC策略编码 （当采用了PCC能力时，相关内容必填）
            map.put("A12", "");
            //13 所属流程, 必填，填写枚举值ID
            map.put("A13", activity.get("flow").toString());        //待定
            Map<String, String> mapEffect = interfaceInfoMpper.getSummaryEffect(activity.get("activity_id").toString(), activityEndDate);
            if (mapEffect == null) {
                uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93005")
                        .activityId(activityId)
                        .activitytype(activity.get("flow").toString())
                        .failDesc("子活动和的效果数据表SummaryEffect为空")
                        .activityTime(activityEndDate)
                        .build());
                continue;
            }
            //18 目标客户群编号 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A18", mapEffect.get("customer_group_id"));
            //19 目标客户群名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A19", mapEffect.get("customer_group_name"));
            //20 目标客户群规模 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A20", mapEffect.get("customer_num"));
            //21 目标客户群描述 可为空
            map.put("A21", mapEffect.get(""));
            //22 目标客户筛选标准 必填
            map.put("A22", mapEffect.get("customer_filter_rule"));
            final Map<String, String> baseOfferBo = getFileDataMapper.getBaseOfferBo(activityId);
            if (baseOfferBo == null) {
                uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93005")
                        .activityId(activityId)
                        .activitytype(activity.get("flow").toString())
                        .failDesc("产品信息为空")
                        .activityTime(activityEndDate)
                        .build());
                continue;
            }
            //23 产品编码 可为空
            //比如0200100xxxx
            //02：一级分类
            //001：二级分类
            //00：三级分类
            //Xxxx：自定义产品编号
            //编码规则参考8.1产品编码规则
            //当营销活动涉及多子活动时，以逗号分隔
            map.put("A23", "0428000" + baseOfferBo.get("prc_id"));
            //24 产品名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A24", baseOfferBo.get("prc_name"));
            //25 产品分类 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A25", baseOfferBo.get("prc_type"));
            //26 渠道编码 可为空
            //比如
            //00108xxxx
            //001：一级分类
            //08：二级分类
            //Xxxx：自定义渠道编码
            //编码规则参考8.2渠道和运营位编码规则
            //当营销活动涉及多子活动时，以逗号分隔
            map.put("A26", activity.get("channel_code").toString());
            //27 渠道名称 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A27", activity.get("channel_name").toString());
            //28 渠道类型 可为空，当营销活动涉及多子活动时，以逗号分隔
            map.put("A28", activity.get("channel_type").toString());
            /**
             * 40-84营销活动效果评估指标
             */
            //40 成功接触客户数 必填
            //口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
            map.put("A40", mapEffect.get("touch_num"));
            //41 接触成功率 必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
            map.put("A41", mapEffect.get("touhe_rate"));
            //42 响应率 必填且取值小于1,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A42", mapEffect.get("response_rate"));
            //43 营销成功用户数 必填,口径：根据运营目的，成功办理或者成功使用的用户数
            map.put("A43", mapEffect.get("vic_num"));
            //44 营销成功率 必填且取值小于1,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A44", mapEffect.get("vic_rate"));
            //45,使用用户数,必填,运营成功用户产生本业务使用行为的用户
            map.put("A45", mapEffect.get("vic_num"));
            //46,活动总用户数,必填：
            String allActivityUserCount = mapEffect.get("customer_num");
            map.put("A46", allActivityUserCount);
            //47,PCC签约用户数,选填：,该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
            map.put("A47", "");
            //48,PCC策略生效用户数,选填：,该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
            map.put("A48", "");
            // 49,PCC策略生效次数,选填：,该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
            map.put("A49", "");
            // 50,签约客户转化率,选填：,该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）,例：填0.1代表10%
            map.put("A50", "");
            list.add(map);
        }
        SqlUtil.getInsert("93005", list);

        fileDataService.insertFailDetails(uploadDetailInfos);
        UploadCountInfo uploadCountInfo = new UploadCountInfo();
        uploadCountInfo.setInterfaceId("93005");
        uploadCountInfo.setUploadNum(list.size());
        uploadCountInfo.setFailNum(uploadDetailInfos.size());
        uploadCountInfo.setActivityTime(activityEndDate);
        getFileDataMapper.insertUploadCount(uploadCountInfo);
    }

    @Override
    public void saveMarking93002(String activityEndDate) throws Exception {
        log.info("saveMarking93002运行传输{}数据", activityEndDate);
        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, String> map = null;
        Map<String, Object> resultmap = null;
        List<UploadDetailInfo> uploadDetailInfos = new LinkedList<>();
        // 根据子活动来
        List<Map<String, String>> activitys = getFileDataMapper.getMarkingInfo93002(activityEndDate);
        //属性编码 5-13为营销活动相关信息，14-36子活动相关信息，42-83子活动效果评估指标
        for (Map<String, String> activity : activitys) {
            map = new HashMap<>();
            //2,统计时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间
            map.put("A2", TimeUtil.getOoiDate(activityEndDate));
            //3,省份
            map.put("A3", CommonConstant.SC);
            //4,地市
            map.put("A4", CommonConstant.cityMap.get(activity.getOrDefault("city_id", "1")));

            /**
             * 5-13为营销活动相关信息
             */
            //5,营销活动编号
            String activity_id = activity.get("activity_id");
            map.put("A5", activity_id);
            //6,营销活动名称,必填
            map.put("A6", activity.get("activity_name"));
            //7,活动开始时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间
            map.put("A7", TimeUtil.getOoiDate(activity.get("activity_starttime")));
            //8,活动结束时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间,活动结束时间不早于活动开始时间
            map.put("A8", TimeUtil.getOoiDate(activity.get("activity_endtime")));
            //9,营销活动类型,1：入网类,必填，填写枚举值ID,2：终端类,3：流量类,4：数字化服务类,5：基础服务类,6：客户保有类,7：宽带类,8：融合套餐类,9：其它类
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10,营销活动目的,1：新增客户类,必填，填写枚举值ID,2：存量保有类,3：价值提升类,4：离网预警类,9：其它类
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11,营销活动描述,对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
            //12,PCC策略编码,（当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13,所属流程,
            map.put("A13", "1");
            //42,活动专题ID，当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A42", activity.get("spetopic_id"));
            /**
             * 14-36子活动相关信息
             */
            final List<Map<String, Object>> campaignedInfo = getFileDataMapper.getCampaignedEndInfo(activity_id, activityEndDate);
            for (Map<String, Object> campaignedmap : campaignedInfo) {
                resultmap = new HashMap<>();
                final String iop_activity_id = campaignedmap.get("iop_activity_id").toString();
                //14,子活动编号,必填,参考附录1 统一编码规则中的营销子活动编号编码规则
                resultmap.put("A14", "280_" + campaignedmap.get("campaign_id") + "_" + iop_activity_id.substring(1));
                //15,子活动名称,必填
                resultmap.put("A15", campaignedmap.get("activity_name"));
                //16,子活动开始时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间
                resultmap.put("A16", TimeUtil.getOoiDate(campaignedmap.get("campaign_starttime").toString()));
                //17,子活动结束时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间,子活动结束时间不早于子活动开始时间
                resultmap.put("A17", TimeUtil.getOoiDate(campaignedmap.get("end_time").toString()));
                Map<String, String> mapEffect1 = interfaceInfoMpper.getSummaryEffect(iop_activity_id, activityEndDate);
                if (mapEffect1 == null) {
                    uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93002")
                            .activityId(activity_id)
                            .activitytype("1")
                            .failDesc("效果数据表ooi_activity_summary_effect为空")
                            .activityTime(activityEndDate)
                            .build());
                    continue;
                }
                //18,目标客户群编号,必填
                resultmap.put("A18", mapEffect1.get("customer_group_id"));
                //19,目标客户群名称,必填
                resultmap.put("A19", mapEffect1.get("customer_group_name"));
                //20,目标客户群规模,可为空
                resultmap.put("A20", mapEffect1.get("customer_num"));
                //21,目标客户群描述,可为空
                resultmap.put("A21", "");
                //22,目标客户筛选标准,必填
                resultmap.put("A22", mapEffect1.get("customer_filter_rule"));
                List<Map<String, String>> offerMaps = getFileDataMapper.getOfferBo(campaignedmap.get("campaign_id").toString());
                if (offerMaps == null || offerMaps.size() == 0) {
                    uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93002")
                            .activityId(activity_id)
                            .activitytype("1")
                            .failDesc("活动的产品信息OfferBo为空")
                            .activityTime(activityEndDate)
                            .build());
                    continue;
                }
                Map<String, String> offerMap = offerMaps.get(0);
                //23,产品编码,必填,比如0200100xxxx,02：一级分类,001：二级分类,00：三级分类,Xxxx：自定义产品编号,编码规则参考8.1产品编码规则
                String proCode = offerMap.get("offer_code");
                resultmap.put("A23", proCode);
                //24,产品编码截取,截取“产品编码”前7位,必填,编码规则参考8.1产品编码规则,长度限制7位（省份截取前7位）
                resultmap.put("A24", proCode.substring(0, 7));
                //25,产品名称,必填
                resultmap.put("A25", offerMap.get("offer_name"));
                //26,产品分类,1：电信服务,必填，填写枚举值ID,2：客户服务,3：数字内容服务,4：实物,5：虚拟物品
                resultmap.put("A26", offerMap.get("offer_type"));
                //27,渠道编码,必填,比如,00108xxxx,001：一级分类,08：二级分类,Xxxx：自定义渠道编码,编码规则参考8.2渠道和运营位编码规则
                String channel_id = campaignedmap.get("channel_id").toString();
                resultmap.put("A27", channel_id);
                //28,渠道编码一级分类,截取“渠道编码”前三位,必填,编码规则参考8.2渠道和运营位编码规则,长度3位,（省份截取前3位）
                resultmap.put("A28", channel_id.substring(0, 3));
                //29,渠道编码二级分类,截取“渠道编码”第四、五位,必填,编码规则参考8.2渠道和运营位编码规则,长度2位,（省份截取第4、5位）
                resultmap.put("A29", channel_id.substring(3, 5));
                //30,渠道名称,必填
                resultmap.put("A30", campaignedmap.get("channel_name"));
                //31,渠道类型,参考10附录3渠道类型编码,必填,渠道类型为偶数位
                resultmap.put("A31", campaignedmap.get("channe_type"));
                //32,渠道接触规则,可为空
                resultmap.put("A32", campaignedmap.get("channel_rule"));
                //33,时机识别,1：互联网使用事件,必填，填写枚举值ID,2：社会事件,3：位置行踪事件,4：业务办理事件,5：业务使用事件,6：周期业务事件,7：自助系统接触事件,8：PCC事件,9：其它事件,0：无事件
                resultmap.put("A33", campaignedmap.get("time_id"));
                //34,时机识别描述,可为空
                resultmap.put("A34", campaignedmap.get("time_distindes"));
                //35,客户质量情况,描述性信息,可为空
                resultmap.put("A35", "");
                //36,资源使用情况,描述性信息,可为空
                resultmap.put("A36", "");
                Map<String, String> mapEffect = interfaceInfoMpper.getSummaryEffect(iop_activity_id, activityEndDate);

                if (mapEffect == null) {
                    uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93002")
                            .activityId(iop_activity_id)
                            .activitytype("1")
                            .failDesc("子活动和的效果数据表SummaryEffect为空")
                            .activityTime(activityEndDate)
                            .build());
                    continue;
                }
                //43成功接触客户数	NUMBER(32)		必填，口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
                map.put("A43", mapEffect.get("touch_num"));
                //44,接触成功率,必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
                map.put("A44", mapEffect.get("touhe_rate"));
                //45,响应率,必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
                map.put("A45", mapEffect.get("response_rate"));
                //46,营销成功用户数,必填；,口径：根据运营目的，成功办理或者成功使用的用户数
                map.put("A46", mapEffect.get("vic_num"));
                //47,营销成功率,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
                map.put("A47", mapEffect.get("vic_rate"));
                //48,使用用户数,必填：，运营成功用户产生本业务使用行为的用户
                map.put("A48", mapEffect.get("vic_num"));
                // 49,活动总用户数 ,必填：
                String allActivityUserCount = mapEffect.get("customer_num");
                map.put("A49", allActivityUserCount);
                //50,PCC签约用户数,该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
                map.put("A50", "");
                //51,PCC策略生效用户数,选填：,该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
                map.put("A51", "");
                //52,PCC策略生效次数,选填,该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
                map.put("A52", "");
                //53,签约客户转化率,选填：,该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）,例：填0.1代表10%
                map.put("A53", "");
                //90投入产出比,NUMBER (20,6),必填且取值小于1；,口径：,统计周期（活动开始时间至活动结束时间）,运营活动成功用户产生的收入/运营活动投入的成本,例：填0.1代表10%
                map.put("A90", mapEffect.get("in_out_rate"));
                resultmap.putAll(map);
                list.add(resultmap);
            }
        }
        SqlUtil.getInsert("93002", list);

        fileDataService.insertFailDetails(uploadDetailInfos);
        ;
        UploadCountInfo uploadCountInfo = new UploadCountInfo();
        uploadCountInfo.setInterfaceId("93002");

        uploadCountInfo.setUploadNum(list.size());
        uploadCountInfo.setFailNum(uploadDetailInfos.size());
        uploadCountInfo.setActivityTime(activityEndDate);
        getFileDataMapper.insertUploadCount(uploadCountInfo);
    }

    @Override
    public void saveBase93002(String activityEndDate) throws Exception {
        log.info("saveBase93002运行传输{}数据", activityEndDate);
        List<Map<String, Object>> list = new LinkedList<>();
        Map<String, Object> map = null;
        List<UploadDetailInfo> uploadDetailInfos = new LinkedList<>();
        List<Map<String, Object>> activitys = getFileDataMapper.getBaseInfo93002(activityEndDate);
        //属性编码 5-13为营销活动相关信息，14-36子活动相关信息，43-90子活动效果评估指标
        for (Map<String, Object> activity : activitys) {
            map = new HashMap<>();
            //2,统计时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间
            map.put("A2", TimeUtil.getOoiDate(activityEndDate));
            //3,省份
            map.put("A3", CommonConstant.SC);
            //4,地市
            map.put("A4", CommonConstant.cityMap.get(activity.getOrDefault("city_id", "1")));
            /**
             * 5-13为营销活动相关信息
             */
            //5,营销活动编号
            String activity_id = activity.get("activity_id").toString();
            map.put("A5", CommonConstant.SC + activity_id.substring(1));
            //6,营销活动名称,必填
            map.put("A6", activity.get("activity_name"));
            //7,活动开始时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间
            map.put("A7", TimeUtil.getOoiDate(activity.get("start_time").toString()));
            //8,活动结束时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间,活动结束时间不早于活动开始时间
            map.put("A8", TimeUtil.getOoiDate(activity.get("end_time").toString()));
            //9,营销活动类型,1：入网类,必填，填写枚举值ID,2：终端类,3：流量类,4：数字化服务类,5：基础服务类,6：客户保有类,7：宽带类,8：融合套餐类,9：其它类
            map.put("A9", activity.getOrDefault("activity_type", "9"));
            //10,营销活动目的,1：新增客户类,必填，填写枚举值ID,2：存量保有类,3：价值提升类,4：离网预警类,9：其它类
            map.put("A10", activity.getOrDefault("activity_objective", "9"));
            //11,营销活动描述,对产品、服务等信息进行简要描述
            map.put("A11", activity.getOrDefault("activity_describe", activity.get("activity_name")));
            //12,PCC策略编码,（当采用了PCC能力时，相关内容必填）
            map.put("A12", activity.get("pcc_id"));
            //13,所属流程,
            map.put("A13", activity.get("flow"));
            //42,活动专题ID，当创建营销活动引用到一级IOP下发的活动专题时，此字段必填
            map.put("A42", "");
            /**
             * 43-90子活动效果评估指标
             */
            Map<String, String> mapEffect = null;
            mapEffect = interfaceInfoMpper.getSummaryEffect(activity_id, activityEndDate);
            if (mapEffect == null) {
                uploadDetailInfos.add(UploadDetailInfo.builder().interfaceId("93002")
                        .activityId(activity_id)
                        .activitytype(activity.get("flow").toString())
                        .failDesc("效果数据表ooi_activity_summary_effect为空")
                        .activityTime(activityEndDate)
                        .build());
                continue;
            }
//43成功接触客户数	NUMBER(32)		必填，口径：运营活动中，通过各触点，接触到的用户数量，如短信下发成功用户数、外呼成功接通用户数、APP成功弹出量等
            map.put("A43", mapEffect.get("touch_num"));
            //44,接触成功率,必填且取值小于1；,口径：成功接触客户数/活动总客户数,例：填0.1代表10%（注意需填小数，而不是百分数）
            map.put("A44", mapEffect.get("touhe_rate"));
            //45,响应率,必填且取值小于1；,口径：运营活动参与用户/成功接触用户,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A45", mapEffect.get("response_rate"));
            //46,营销成功用户数,必填；,口径：根据运营目的，成功办理或者成功使用的用户数
            map.put("A46", mapEffect.get("vic_num"));
            //47,营销成功率,必填且取值小于1；,口径：营销成功用户数/成功接触客户数,例：填0.1代表10%,（注意需填小数，而不是百分数）
            map.put("A47", mapEffect.get("vic_rate"));
            //48,使用用户数,必填：，运营成功用户产生本业务使用行为的用户
            map.put("A48", mapEffect.get("vic_num"));
            // 49,活动总用户数	,必填：
            String allActivityUserCount = mapEffect.get("customer_num");
            map.put("A49", allActivityUserCount);
            //50,PCC签约用户数,该PCC策略活动签约用户的总数（当采用了PCC能力时，相关内容必填）
            map.put("A50", "");
            //51,PCC策略生效用户数,选填：,该PCC策略在活动期间内生效的签约用户总数（当采用了PCC能力时，相关内容必填）
            map.put("A51", "");
            //52,PCC策略生效次数,选填,该PCC策略在活动期间内签约用户一共生效的次数（当采用了PCC能力时，相关内容必填）
            map.put("A52", "");
            //53,签约客户转化率,选填：,该PCC策略活动期间签约用户的转化率（当采用了PCC能力时，相关内容必填）,例：填0.1代表10%
            map.put("A53", "");
            //90投入产出比,NUMBER (20,6),必填且取值小于1；,口径：,统计周期（活动开始时间至活动结束时间）,运营活动成功用户产生的收入/运营活动投入的成本,例：填0.1代表10%
            map.put("A90", mapEffect.get("in_out_rate"));

/**
 * 14-36子活动相关信息
 */
            //14,子活动编号,必填,参考附录1 统一编码规则中的营销子活动编号编码规则
            map.put("A14", activity.get("activity_id").toString().substring(1));
            //15,子活动名称,必填
            map.put("A15", activity.get("activity_name"));
            //16,子活动开始时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间
            map.put("A16", TimeUtil.getOoiDate(activity.get("start_time").toString()));
            //17,子活动结束时间,格式：YYYYMMDDHH24MISS,必填,,为数据生成时间,子活动结束时间不早于子活动开始时间
            map.put("A17", TimeUtil.getOoiDate(activity.get("end_time").toString()));
            //18,目标客户群编号,必填
            map.put("A18", mapEffect.get("customer_group_id"));
            //19,目标客户群名称,必填
            map.put("A19", mapEffect.get("customer_group_name"));
            //20,目标客户群规模,可为空
            map.put("A20", mapEffect.get("customer_num"));
            //21,目标客户群描述,可为空
            map.put("A21", "");
            //22,目标客户筛选标准,必填
            map.put("A22", mapEffect.get("customer_filter_rule"));
            Map<String, String> baseOfferBo = getFileDataMapper.getBaseOfferBo(activity_id);

            //23,产品编码,必填,比如0200100xxxx,02：一级分类,001：二级分类,00：三级分类,Xxxx：自定义产品编号,编码规则参考8.1产品编码规则
            String proCode = "0428000" + baseOfferBo.get("prc_id");
            map.put("A23", proCode);
            //24,产品编码截取,截取“产品编码”前7位,必填,编码规则参考8.1产品编码规则,长度限制7位（省份截取前7位）
            map.put("A24", proCode.substring(0, 7));
            //25,产品名称,必填
            map.put("A25", baseOfferBo.get("prc_name"));
            //26,产品分类,1：电信服务,必填，填写枚举值ID,2：客户服务,3：数字内容服务,4：实物,5：虚拟物品
            map.put("A26", baseOfferBo.get("prc_type"));
            String channelCode = activity.get("channel_code").toString() + activity.get("channel_id");
            String channelName = activity.get("channel_name").toString();
            String channelType = activity.get("channel_type").toString();
            //27,渠道编码,必填,比如,00108_xxxx,001：一级分类,08：二级分类,xxxx：自定义渠道编码,编码规则参考_8_2_渠道和运营位编码规则
            map.put("A27", channelCode);   //024800
            //28,渠道编码一级分类,截取“渠道编码”前三位,必填,编码规则参考8.2渠道和运营位编码规则,长度3位,（省份截取前3位）
            map.put("A28", channelCode.substring(0, 3));
            //29,渠道编码二级分类,截取“渠道编码”第四、五位,必填,编码规则参考8.2渠道和运营位编码规则,长度2位,（省份截取第4、5位）
            map.put("A29", channelCode.substring(3, 5));
            //30,渠道名称,必填
            map.put("A30", channelName);
            //31,渠道类型,参考10附录3渠道类型编码,必填,渠道类型为偶数位
            map.put("A31", channelType);
            //32,渠道接触规则,可为空
            map.put("A32", "");
            //33,时机识别,1：互联网使用事件,必填，填写枚举值ID,2：社会事件,3：位置行踪事件,4：业务办理事件,5：业务使用事件,6：周期业务事件,7：自助系统接触事件,8：PCC事件,9：其它事件,0：无事件
            map.put("A33", "0");
            //34,时机识别描述,可为空
            map.put("A34", "");
            //35,客户质量情况,描述性信息,可为空
            map.put("A35", "");
            //36,资源使用情况,描述性信息,可为空
            map.put("A36", "");
            list.add(map);
        }
        SqlUtil.getInsert("93002", list);

        fileDataService.insertFailDetails(uploadDetailInfos);
        UploadCountInfo uploadCountInfo = new UploadCountInfo();
        uploadCountInfo.setInterfaceId("93002");
        uploadCountInfo.setUploadNum(list.size());
        uploadCountInfo.setFailNum(uploadDetailInfos.size());
        uploadCountInfo.setActivityTime(activityEndDate);
        getFileDataMapper.insertUploadCount(uploadCountInfo);
    }

    @Override
    public void uploadFile() {

        // 查询数据已准备完成的
        List<Map<String, String>> canCreateFileInterface = uploadService.getCanCreateFileInterface();

        if (canCreateFileInterface.size() == 0) {
            log.info("暂无待生成文件！！！！");
            return;
        }

        for (Map<String, String> map :
                canCreateFileInterface) {
            String interfaceId = "";
            String tableName = "";
            String date = "";
            String fileName = "";
            String localPath = "";
            String remotePath = "";
            // 设置基本属性
            // TODO 后面修改表模型然后优化
            for (Map.Entry enty :
                    map.entrySet()) {
                String k = (String) enty.getKey();
                String v = (String) enty.getValue();
                switch (k) {
                    case "interface_id":
                        interfaceId = v;
                        break;
                    case "table_name":
                        tableName = v;
                        break;
                    case "data_time":
                        date = v;
                        break;
                    case "file_name":
                        fileName = v;
                        break;
                    case "interface_cycle":
                        if (("1").equals(v) || "2".equals(v)) {
                            localPath = path17 + File.separator + "upload" + File.separator + "time/day";
                            remotePath = path228 + File.separator + "upload" + File.separator + "time/day";
                        } else if ("3".equals(v)) {
                            localPath = path17 + File.separator + "upload" + File.separator + "time/month";
                            remotePath = path228 + File.separator + "upload" + File.separator + "time/month";
                        }
                        break;
                    default:
                        break;
                }
            }
            localPath = localPath.replaceAll("time", date);
            remotePath = remotePath.replaceAll("time", date);
            log.info("interfaceId:{},fileName：{}", interfaceId, fileName);
            writeFileThread.write(interfaceId, fileName, tableName, localPath, remotePath, date);
        }
    }

    @Override
    public List<Map<String, String>> getCheckFileByDate(String fileDate) throws IOException {
        log.info("需要获取校验文件的日期{}", fileDate);
        String remotePath = path228 + "/tmp";
        String loaclPath = path17 + "/tmp";
        boolean b = FtpUtil.downloadCheckFileFTP(remotePath, loaclPath);
        if (!b) {
            return new ArrayList<>();
        }
        File file = new File(loaclPath);
        final File[] files = file.listFiles();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        int numbers = 1;
        for (File file1 : files) {
            log.info("删除校验文件{}", file1.getAbsolutePath());
            String fileName = file1.getName();
            map = new HashMap<>();
            String interfaceId = fileName.substring(23, 28);
            map.put("numbers", String.valueOf(numbers++));
            map.put("interfaceId", interfaceId);
            String fileType = fileName.substring(0, 1);
            map.put("filetype", "f".equals(fileType) ? "文件级校验" : "记录级校验");
            map.put("filename", fileName);
            map.put("date", fileDate);
            FileInputStream in = null;
            String fileContent = "";
            try {
                Long filelength = file1.length();
                byte[] filecontent = new byte[filelength.intValue()];
                in = new FileInputStream(file1);
                in.read(filecontent);
                fileContent = new String(filecontent, "GBK");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            map.put("filecontent", fileContent);
            list.add(map);
        }
        return list;
    }
}