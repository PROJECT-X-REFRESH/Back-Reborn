package com.reborn.back.aiPost.repository;

import com.reborn.back.domain.board.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiPostLikeRepository extends JpaRepository<BoardLike, Long> {
}