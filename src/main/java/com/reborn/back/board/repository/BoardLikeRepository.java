package com.reborn.back.board.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardLike;
import com.reborn.back.domain.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    // 게시판 좋아요 개수 조회
    Long countAllByBoard(Board board);

    // 사용자와 게시판에 대한 좋아요 정보 조회
    // AiPostLike findByUserAndBoard(User user, Board board); - 이전 로직
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT bl FROM AiPostLike bl WHERE bl.user = :user AND bl.board = :board")
    Optional<BoardLike> findByUserAndBoardWithLock(@Param("user") User user, @Param("board") Board board);

    void deleteAll(List<BoardLike> boardLikes);
}