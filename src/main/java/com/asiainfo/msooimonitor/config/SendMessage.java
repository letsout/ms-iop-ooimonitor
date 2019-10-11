package com.asiainfo.msooimonitor.config;

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
}
