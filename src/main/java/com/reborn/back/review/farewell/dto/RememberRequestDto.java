package com.reborn.back.review.farewell.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RememberRequestDto {

    // 프론트에게 전달받을 정보
    @Schema(description = "RememberReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RememberReqDto {
        @Schema(description = "반려동물과 추억 정리하기 내용")
        private String contents;
    }
}
