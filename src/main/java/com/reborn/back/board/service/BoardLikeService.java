package com.reborn.back.board.service;

import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;


    @Transactional
    public Board toggleLike(Integer boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorCode.BOARD_NOT_FOUND));

        boolean isLiked = boardLikeRepository.existsByUserAndBoard(user, board);

        if (isLiked) {
            boardLikeRepository.deleteByUserAndBoard(user, board); // 좋아요 취소
        } else {
            boardLikeRepository.insertOrUpdateLike(user.getUid(), board.getId()); // 좋아요 추가 (중복 삽입 방지)
        }

        return board;
    }

    // 특정 사용자가 해당 게시글을 좋아요 했는지 확인
    public boolean checkLike(Integer boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorCode.BOARD_NOT_FOUND));

        return boardLikeRepository.existsByUserAndBoard(user, board);
    }

    // 특정 게시글의 좋아요 수 조회
    public Integer getLikeCount(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorCode.BOARD_NOT_FOUND));

        return boardLikeRepository.countByBoard(board);
    }
}
