package com.reborn.back.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommentRequestDto {
    // Front에 전달받을 Comment 내용
    @Schema(description = "CommentReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentReqDto {
        @Schema(description = "댓글 내용")
        private String contents;
    }
}
