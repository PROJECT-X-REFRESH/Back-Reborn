package com.reborn.back.board.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardLike;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    // 특정 사용자가 특정 게시글을 좋아요 했는지 확인
    boolean existsByUserAndBoard(User user, Board board);

    // 특정 게시글의 좋아요 개수 조회
    Integer countByBoard(Board board);

    // 특정 사용자의 좋아요 삭제
    void deleteByUserAndBoard(User user, Board board);

    /*
    좋아요 추가 (ON DUPLICATE KEY UPDATE 사용)
    → 중복 삽입을 방지하고, 이미 존재하는 경우 업데이트
     */
    @Modifying
    @Query(value = "INSERT INTO board_like (uid, b_id) VALUES (:userId, :boardId) " +
            "ON DUPLICATE KEY UPDATE bl_id = LAST_INSERT_ID(bl_id)", nativeQuery = true)
    void insertOrUpdateLike(@Param("userId") String userId, @Param("boardId") Integer boardId);
}