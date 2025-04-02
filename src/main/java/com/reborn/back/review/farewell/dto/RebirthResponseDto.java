package com.reborn.back.review.farewell.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RebirthResponseDto {
    @Schema(description = "DetailRebirthDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRebirthDto {
        @Schema(description = "씻기기 상태")
        private Boolean wash;

        @Schema(description = "옷 입히기 상태")
        private Boolean dress;

        @Schema(description = "리본 선택")
        private String ribbon;
    }

    @Schema(description = "ReviewRebirthDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewRebirthDto {
        @Schema(description = "건강한 작별하기 id")
        private Integer id;

        @Schema(description = "반려동물 편지")
        private String petPost;
    }
}
