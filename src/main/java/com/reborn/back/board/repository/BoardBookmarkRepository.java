package com.reborn.back.board.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardBookmark;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {

    // 사용자와 게시판에 대한 북마크 정보 조회
    BoardBookmark findByUserAndBoard(User user, Board board);
}