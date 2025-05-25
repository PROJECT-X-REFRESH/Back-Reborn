package com.reborn.back.review.recollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class RemindDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "RemindResDto")
    public static class RemindResDto {
        private Integer id;
        private String userName;
        private String petName;
        private String title;
        private String content;
        private LocalDateTime createdAt;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "RemindReqDto")
    public static class RemindReqDto {
        private String title;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "Remind 짧은 응답")
    public static class RemindSimpleResDto {
        private Integer id;
        private String title;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "Remind 요청 정보")
    public static class RemindInfoDto {
        private String userName;
        private String petName;
    }
}
