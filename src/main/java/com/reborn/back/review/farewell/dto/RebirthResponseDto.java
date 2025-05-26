package com.reborn.back.review.farewell.dto;

import com.reborn.back.domain.entity.RebirthStep;
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
        @Schema(
                description = "wash, clothes, ribbon, post, outro= 중 하나",
                example = "clothes"
        )
        private RebirthStep nextStep;
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
