package com.reborn.back.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.reborn.back.fcm.dto.FcmRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final FirebaseMessaging firebaseMessaging;

    public String sendMessage(FcmRequestDto dto) {
        Message message = Message.builder()
                .setToken(dto.getToken())
                .setNotification(Notification.builder()
                        .setTitle(dto.getTitle())
                        .setBody(dto.getBody())
                        .build())
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("FCM 전송 성공: {}", response);
            return response;
        } catch (Exception e) {
            log.error("FCM 전송 실패: {}", e.getMessage(), e);
            throw new RuntimeException("FCM 전송 오류", e);
        }
    }
}
