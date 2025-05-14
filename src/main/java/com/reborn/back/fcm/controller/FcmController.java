package com.reborn.back.fcm.controller;

import com.reborn.back.fcm.dto.FcmRequestDto;
import com.reborn.back.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/push")
    public ResponseEntity<String> push(@Validated @RequestBody FcmRequestDto dto) {
        String msgId = fcmService.sendMessage(dto);
        return ResponseEntity.ok("푸시 발송 완료 (messageId=" + msgId + ")");
    }
}
