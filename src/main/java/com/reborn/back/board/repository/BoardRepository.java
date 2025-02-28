package com.reborn.back.board.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.entity.BoardType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 특정 게시물을 ID로 조회
    Optional<Board> findById(Integer bId);

    /*
     * 특정 카테고리의 게시물을 좋아요 개수 기준으로 내림차순 정렬하여 조회
     * @return 좋아요 개수가 많은 순으로 정렬된 게시물 리스트 (Slice 형태)
     */
    @Query("SELECT b FROM Board b WHERE b.category = :boardType ORDER BY b.likeCount DESC")
    Slice<Board> findByCategoryOrderByLikeCountDesc(@Param("boardType") BoardType boardType, Pageable pageable);

    /*
     * 특정 카테고리의 게시물을 생성일 기준으로 내림차순 정렬하여 조회
     * @return 최신 게시물이 먼저 출력되는 게시물 리스트 (Slice 형태)
     */
    @Query("SELECT b FROM Board b WHERE b.category = :boardType ORDER BY b.createdAt DESC")
    Slice<Board> findByCategoryOrderByCreatedAtDesc(@Param("boardType") BoardType boardType, Pageable pageable);

    /*
     * 사용자가 북마크한 특정 카테고리의 게시물을 조회
     * @return 사용자가 북마크한 최신 게시물 리스트 (Slice 형태)
     */
    @Query("SELECT b FROM Board b JOIN b.boardBookmarkList bm WHERE bm.user.uid = :userId AND b.category = :boardType ORDER BY b.createdAt DESC")
    Slice<Board> findByBookmarkList_User_UidOrderByCreatedAtDesc(@Param("userId") String userId, @Param("boardType") BoardType boardType, Pageable pageable);
}