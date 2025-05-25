package com.reborn.back.review.farewell.dto;

import com.reborn.back.domain.entity.EmotionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RevealRequestDto {

    // 프론트에게 전달받을 정보
    @Schema(description = "RevealReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RevealReqDto {
        @Schema(description = "나의 감정 드러내기 내용")
        private String contents;
        @Schema(description = "결과로 나온 감정 상태(SUNNY, CLOUDY, RAINY)")
        private EmotionState emotionState;
    }
}
