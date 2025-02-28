package com.reborn.back.board.service;

import com.reborn.back.board.repository.BoardBookmarkRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardBookmark;
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
public class BoardBookmarkService {
    private final BoardBookmarkRepository boardBookmarkRepository;
    private final BoardRepository boardRepository;

    // 북마크 생성 및 삭제
    @Transactional
    public boolean toggleBookmark(Integer bId, User user) {
        Board board = boardRepository.findById(bId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.BOARD_NOT_FOUND));

        boolean exists = boardBookmarkRepository.existsByUserAndBoard(user, board);

        if (exists) {
            boardBookmarkRepository.deleteByUserAndBoard(user, board);
            return false;  // 북마크 취소됨
        } else {
            boardBookmarkRepository.save(BoardBookmark.builder().user(user).board(board).build());
            return true;  // 북마크 설정됨
        }
    }

}
