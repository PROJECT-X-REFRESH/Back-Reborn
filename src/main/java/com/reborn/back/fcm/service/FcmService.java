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
        // ── 알림( Notification ) 구성 ─────────────────────────────
        Notification notification = Notification.builder()
                .setTitle(dto.getTitle())
                .setBody(dto.getBody())
                .build();

        // ── 메시지( Message ) 빌더 ───────────────────────────────
        Message.Builder builder = Message.builder()
                .setToken(dto.getToken())
                .setNotification(notification);

        // ⚡ data 페이로드가 있으면 추가
        if (dto.getData() != null && !dto.getData().isEmpty()) {
            builder.putAllData(dto.getData());
        }

        Message message = builder.build();

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
