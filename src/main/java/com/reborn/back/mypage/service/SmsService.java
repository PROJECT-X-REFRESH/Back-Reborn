package com.reborn.back.mypage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.mypage.dto.MessageDto;
import com.reborn.back.mypage.dto.SmsRequestDto;
import com.reborn.back.mypage.dto.SmsResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sun.org.apache.bcel.internal.classfile.Utility.getSignature;

@Service
public class SmsService {
    private final RedisUtil redisUtil;

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;
    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;
    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;
    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    public SmsService(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    // 인증코드 만들기
    public static String createSmsKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 5; i++) { // 인증코드 5자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    // 실제 SMS 발송
    public SmsResponseDto sendSms(MessageDto messageDto)
            throws JsonProcessingException, RestClientException, URISyntaxException,
            InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        // 1) 인증번호 생성
        String smsConfirmNum = createSmsKey();

        // 2) 헤더 설정
        String time = Long.toString(System.currentTimeMillis());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", getSignature(time));

        // 3) Request Body 생성
        List<MessageDto> messages = new ArrayList<>();
        messages.add(messageDto); // 메시지 DTO(수신번호 포함)

        SmsRequestDto request = SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content("[테스트] 인증번호 [" + smsConfirmNum + "]를 입력해주세요.") // 문구
                .messages(messages)
                .build();

        // 4) SENS API 요청
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto smsResponseDto = restTemplate.postForObject(
                new URI("https://sens.apigw.ntruss.com/sms/v2/services/"
                        + serviceId + "/messages"),
                httpBody,
                SmsResponseDto.class
        );

        // 5) Redis에 "sms:[전화번호]" -> 인증번호를 3분간 저장
        String phoneNumber = messageDto.getTo(); // 수신번호
        redisUtil.setDataExpire("sms:" + phoneNumber, smsConfirmNum, 180);

        // 응답을 그대로 리턴하거나, 필요하면 인증번호는 보안상 숨기는 게 좋음
        return smsResponseDto;
    }
}
