package com.reborn.back.review.farewell.dto;

import com.reborn.back.domain.entity.OrganizeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class RememberResponseDto {

    // Front에 전달할 Remember 정보
    @Schema(description = "SimpleRememberDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleRememberDto {
        @Schema(description = "추억 정리하기 id")
        private Integer id;
    }

    @Schema(description = "DetailRememberDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRememberDto {
        @Schema(description = "사료 진행 상태")
        private Boolean feed;

        @Schema(description = "간식 진행 상태")
        private Boolean snack;

        @Schema(description = "산책 진행 상태")
        private Boolean walk;

        @Schema(description = "추억 정리하기 내용")
        private String contents;

        @Schema(description = "남은 정리 품목")
        private List<OrganizeType> remainingThings;
    }

    @Schema(description = "ReviewRememberDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewRememberDto {
        @Schema(description = "추억 정리하기 작성일")
        private LocalDateTime createdAt;

        @Schema(description = "이미지 url")
        private String url;

        @Schema(description = "추억 정리하기 내용")
        private String contents;
    }
}
