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
    }

    @Schema(description = "SimpleRecognizeResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleRecognizeResDto {
        @Schema(description = "검사 결과 점수")
        private Integer score;
    }

    @Schema(description = "DetailRecognizeDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRecognizeDto {
        @Schema(description = "사료 진행 상태")
        private Boolean feed;

        @Schema(description = "간식 진행 상태")
        private Boolean snack;

        @Schema(description = "산책 진행 상태")
        private Boolean walk;

        @Schema(description = "검사 여부")
        private Boolean contents1;

        @Schema(description = "검사 여부(반드시 false)")
        private Boolean contents2;
    }
}
