package com.reborn.back.board.dto;

import com.reborn.back.domain.entity.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class BoardResponseDto {

    // Front에 전달할 Board 정보
    @Schema(description = "BoardResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardResDto {
        @Schema(description = "게시판 id")
        private Integer id;

        @Schema(description = "게시판 종류")
        private BoardType category;

        @Schema(description = "게시판 작성자")
        private String writerName;

        @Schema(description = "게시판 작성자 프로필")
        private String writerProfileImage;

        @Schema(description = "좋아요 수")
        private Integer likeCount;

        @Schema(description = "댓글 수")
        private Integer commentCount;

        @Schema(description = "게시판 내용")
        private String content;

        @Schema(description = "게시판 작성일", example = "2024-03-21", pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;

        @Schema(description = "게시판 이미지")
        private String attachImg;
    }

    @Schema(description = "BoardListResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardListResDto {

        @Schema(description = "게시물 리스트")
        private List<BoardResDto> boardList;

    }
}
