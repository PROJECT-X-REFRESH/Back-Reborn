package com.reborn.back.board.converter;

import com.reborn.back.board.dto.BoardRequestDto.BoardReqDto;
import com.reborn.back.board.dto.BoardResponseDto.BoardListResDto;
import com.reborn.back.board.dto.BoardResponseDto.BoardResDto;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.user.User;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class BoardConverter {
    // 저장
    public static Board saveBoard(BoardReqDto board, User user) {
        return Board.builder()
                .user(user)
                .category(board.getCategory())
                .commentCount(0)
                .viewCount(0)
                .content(board.getContent())
                .attachImg(null)
                .build();
    }

    public static BoardResDto simpleBoardDto(Board board, boolean liked) {
        return BoardResDto.builder()
                .id(board.getId())
                .category(board.getCategory())
                .writerName(board.getUser().getNickname())
                .writerProfileImage(board.getUser().getImg())
                .commentCount(board.getCommentCount())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .attachImg(board.getAttachImg())
                .like(liked)
                .build();
    }

    /* 목록용 BOARD → DTO (liked 집합 O(1) 체크) */
    public static BoardListResDto boardListResDto(List<Board> boards,
                                                  Set<Integer> likedBoardIds) {

        List<BoardResDto> dtos = boards.stream()
                .map(b -> simpleBoardDto(b, likedBoardIds.contains(b.getId())))
                .toList();

        return BoardListResDto.builder().boardList(dtos).build();
    }

    /* liked 정보가 필요 없는 경우를 위한 기본 오버로드 */
    public static BoardListResDto boardListResDto(List<Board> boards) {
        return boardListResDto(boards, Collections.emptySet());
    }
}
