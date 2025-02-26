package com.reborn.back.comment.repository;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}

