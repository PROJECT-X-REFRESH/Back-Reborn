package com.reborn.back.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmRequestDto {
    private String token;                 // 단말 토큰
    private String title;                 // 푸시 제목
    private String body;                  // 푸시 내용
    private Map<String, String> data;     // 추가 데이터 (boardId 등)
}