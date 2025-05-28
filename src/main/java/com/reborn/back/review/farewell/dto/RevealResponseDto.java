package com.reborn.back.review.farewell.dto;

import com.reborn.back.domain.entity.EmotionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class RevealResponseDto {

    // Front에 전달할 Reveal 정보
    @Schema(description = "SimpleRevealDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleRevealDto {
        @Schema(description = "나의 감정 드러내기 id")
        private Integer id;
    }

    @Schema(description = "DetailRevealDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRevealDto {
        @Schema(description = "사료 진행 상태")
        private Boolean feed;

        @Schema(description = "간식 진행 상태")
        private Boolean snack;

        @Schema(description = "산책 진행 상태")
        private Boolean walk;

        @Schema(description = "결과로 나온 감정 상태(SUN, CLOUD, RAIN)")
        private String emotionState;
    }

    @Schema(description = "ReviewRevealDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewRevealDto {
        @Schema(description = "나의 감정 드러내기 작성일")
        private LocalDateTime createdAt;

        @Schema(description = "나의 감정 드러내기 내용")
        private String contents;

        @Schema(description = "결과로 나온 감정 상태(SUNN, CLOUD, RAIN)")
        private EmotionState emotionState;
    }
}
