package com.reborn.back.board.dto;

import com.reborn.back.domain.entity.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BoardRequestDto {

    // 프론트에게 전달받을 정보
    @Schema(description = "BoardReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReqDto {
        @Schema(description = "게시판 종류")
        private BoardType category;

        @Schema(description = "게시판 내용")
        private String content;
    }
}
