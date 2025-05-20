package com.reborn.back.review.farewell.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecognizeRequestDto {
    // Front에 전달받을 Recognize 내용
    @Schema(description = "RecognizeReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecognizeReqDto {
        @Schema(description = "검사 결과 점수")
        private Integer score;
    }
}
