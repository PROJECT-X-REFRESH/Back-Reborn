package com.reborn.back.board.converter;

import com.reborn.back.board.dto.BoardRequestDto.BoardReqDto;
import com.reborn.back.board.dto.BoardResponseDto.BoardListResDto;
import com.reborn.back.board.dto.BoardResponseDto.BoardResDto;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.user.User;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public static BoardResDto simpleBoardDto(Board board) {
        return BoardResDto.builder()
                .id(board.getId())
                .category(board.getCategory())
                .writerName(board.getUser().getName())
                .writerProfileImage(board.getUser().getImg())
                .commentCount(board.getCommentCount())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .attachImg(board.getAttachImg())
                .build();
    }

    public static BoardListResDto boardListResDto(List<Board> boards) {
        List<BoardResDto> boardResDtos
                = boards.stream().map(BoardConverter::simpleBoardDto).toList();

        return BoardListResDto.builder()
                .boardList(boardResDtos)
                .build();
    }
}
