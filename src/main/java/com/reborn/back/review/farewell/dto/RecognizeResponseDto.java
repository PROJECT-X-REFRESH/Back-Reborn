package com.reborn.back.review.farewell.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecognizeResponseDto {

    @Schema(description = "RecognizeResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecognizeResDto {
        @Schema(description = "나의 상태 알아보기 id")
        private Integer id;

        @Schema(description = "검사 결과 점수")
        private Integer score;
    }
}
