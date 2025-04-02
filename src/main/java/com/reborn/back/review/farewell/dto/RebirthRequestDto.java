package com.reborn.back.review.farewell.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RebirthRequestDto {

    // 프론트에게 전달받을 정보
    @Schema(description = "RebirthReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RebirthReqDto {
        @Schema(description = "건강한 작별하기 내용")
        private String petPost;
    }

}
