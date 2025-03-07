package com.reborn.back.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class CommentResponseDto {
    // Front에 전달할 Comment 정보
    @Schema(description = "CommentResDto")
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResDto {
        @Schema(description = "댓글 id")
        private Integer id;

        @Schema(description = "글 작성자")
        private String commentWriter;

        @Schema(description = "댓글 작성자 프로필 이미지")
        private String writerProfileImage;

        @Schema(description = "댓글 내용")
        private String contents;

        @Schema(description = "댓글 작성 시간")
        private LocalDateTime createdAt;

    }

    @Schema(description = "CommentListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentListResDto {

        @Schema(description = "댓글 리스트")
        private List<CommentResDto> commentList;

    }
}
