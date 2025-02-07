package com.reborn.back.mypage.controller;

import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.login.service.UserService;
import com.reborn.back.mypage.dto.MessageDto;
import com.reborn.back.mypage.dto.SmsResponseDto;
import com.reborn.back.mypage.dto.SmsVerifyRequest;
import com.reborn.back.mypage.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {
    private final SmsService smsService;
    private final RedisUtil redisUtil;
    private final UserService userService;

    // 1) 인증번호 발송
    @PostMapping("/send")
    public SmsResponseDto sendSms(@RequestBody MessageDto messageDto)
            throws Exception {
        return smsService.sendSms(messageDto);
    }

    // 2) 인증번호 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verifySms(@RequestBody SmsVerifyRequest request) {
        String phoneNumber = request.getPhoneNum();
        String inputCode = request.getConfirmNum();

        // 1) Redis에서 저장된 코드 가져옴
        String storedCode = redisUtil.getData("sms:" + phoneNumber);
        if (storedCode == null) {
            // 시간 만료 또는 잘못된 접근
            throw new GeneralException(ErrorCode.SMS_CODE_EXPIRED_OR_NOT_FOUND);
        }

        // 2) 비교
        if (!storedCode.equals(inputCode)) {
            // 불일치 -> 에러
            throw new GeneralException(ErrorCode.SMS_CODE_MISMATCH);
        }

        // 3) 인증 성공 -> Redis 인증번호 삭제
        redisUtil.deleteData("sms:" + phoneNumber);

        // 4) DB에 동일 전화번호가 있는지 확인
        //    (UserService에 "findByPhoneNumber" 같은 메서드가 필요)
        User existingUser = userService.findByPhoneNum(phoneNumber);

        // 5) 현재 로그인 중인 User (ex: SecurityContext에서 가져옴)
        //    여기서는 예시로 "userService.getCurrentUser()"라 가정
        User currentUser = userService.getCurrentUser();

        if (existingUser == null) {
            // 전화번호가 DB에 없으므로, 현재 유저의 phoneNumber로 저장
            currentUser.setPhoneNum(phoneNumber);
            userService.save(currentUser);
            return ResponseEntity.ok("전화번호 인증 성공 (새 번호 등록 완료)");
        } else {
            // 이미 동일 번호가 DB에 존재 -> 계정 통합 로직
            if (existingUser.getId().equals(currentUser.getId())) {
                // 이미 동일 유저가 동일 번호로 등록해둔 경우
                return ResponseEntity.ok("이미 등록된 본인 번호입니다.");
            } else {
                // 서로 다른 계정 -> 통합
                userService.unifyAccounts(existingUser, currentUser);
                return ResponseEntity.ok("전화번호 인증 및 계정 통합 완료");
            }
        }
    }
}
