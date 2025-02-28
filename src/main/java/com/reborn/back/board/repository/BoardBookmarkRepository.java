package com.reborn.back.board.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardBookmark;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {

    // 북마크 존재 여부 확인 (불필요한 조회 방지)
    boolean existsByUserAndBoard(User user, Board board);

    // 북마크 삭제
    void deleteByUserAndBoard(User user, Board board);
}