package com.reborn.back.review.recollection.dto;

import com.reborn.back.domain.entity.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
public class RecordDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @Schema(description = "Record 응답 DTO")
    public static class RecordResDto {
        private Integer id;
        private String content;
        private Emotion emotion;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @Schema(description = "Record 요청 DTO")
    public static class RecordReqDto {
        private String content;
        private Emotion emotion;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @Schema(description = "Record 짧은 응답 DTO")
    public static class RecordSimpleResDto {
        private Integer id;
        private LocalDateTime createdAt;
    }
}
