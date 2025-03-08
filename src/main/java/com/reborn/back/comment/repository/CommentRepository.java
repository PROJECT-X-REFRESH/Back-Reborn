package com.reborn.back.comment.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시물을 ID로 조회
    Optional<Comment> findById(Integer cId);

    // 삭제되지 않은 특정 게시글의 댓글을 내림차순 정렬하여 조회
    List<Comment> findAllByBoardAndIsDeletedFalseOrderByIdDesc(Board board);
}

