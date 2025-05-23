package com.reborn.back.board.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.entity.BoardType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 특정 게시물을 ID로 조회
    Optional<Board> findById(Integer bId);

    // 특정 게시물 ID로 viewCount 증가
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :boardId")
    void incrementViewCount(@Param("boardId") Integer boardId);

    // 전체 게시물 최신순
    Slice<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 특정 카테고리 최신순
    Slice<Board> findByCategoryOrderByCreatedAtDesc(BoardType boardType, Pageable pageable);

    // 사용자가 좋아요한 게시글을 최신순으로 조회
    Slice<Board> findByBoardLikeList_User_UidOrderByCreatedAtDesc(String userId, Pageable pageable);

    // 특정 게시물 ID로 commentCount 증가
    @Modifying
    @Query("UPDATE Board b SET b.commentCount = b.commentCount + 1 WHERE b.id = :boardId")
    void incrementCommentCount(@Param("boardId") Integer boardId);

    // 특정 게시물 ID로 commentCount 감소
    @Modifying
    @Query("UPDATE Board b SET b.commentCount = b.commentCount - 1 WHERE b.id = :boardId")
    void decrementCommentCount(@Param("boardId") Integer boardId);
}