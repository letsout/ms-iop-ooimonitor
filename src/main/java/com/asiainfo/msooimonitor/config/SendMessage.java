package com.asiainfo.msooimonitor.config;

import com.asiainfo.msooimonitor.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.text.SimpleDateFormat;

/**
 * @Author H
 * @Date 2019/9/26 14:11
 * @Desc
 **/
@Component
@Slf4j
public class SendMessage {

    private BASE64Encoder base64Decoder = new BASE64Encoder();

    public void sendSms(String smsContent) {
        String phones = "13541008413,13438061830";
        log.info("开始发送短信：{},号码P:[{}]", smsContent, phones);
        String encode = base64Decoder.encode(base64Decoder.encode(smsContent.getBytes()).getBytes());
        String url = "http://10.113.251.152:11105/sendMsg/send/sendMsgInterface";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("rev_phone_no", phones);
        multiValueMap.add("content", encode);
        restTemplate.postForObject(url, multiValueMap, String.class);
    }

    public void sendFileUploadSuccess(String interfaceId) {
        String str = "今日" + interfaceId + "接口数据已按要求于" + TimeUtil.getNowTime() + "生成上传至228主机,请与228主机检验文件，无误后移动到同目录下的day目录内供集团获取";
        sendSms(str);
    }

    public void sendFileUploadFail(String interfaceId) {
        String str = "今日" + interfaceId + "接口数据生成失败，请查看错误修改后并手动上传";
        sendSms(str);
    }
    public void sendCheckFileSuccess(String interfaceId) {
        String str = "今日"+interfaceId+"接口数据已于" + TimeUtil.getNowTime() + "成功解析集团校验文件未出现问题,今日文件接口保障完成。";
        sendSms(str);
    }
    public void sendCheckFileFail(String interfaceId) {
        String str = "今日"+interfaceId+"接口数据已于" + TimeUtil.getNowTime() + "成功解析集团校验文件,但校验文件出现问题，请查看校验文件并在11点之前重新上传。";
        sendSms(str);
    }
}
