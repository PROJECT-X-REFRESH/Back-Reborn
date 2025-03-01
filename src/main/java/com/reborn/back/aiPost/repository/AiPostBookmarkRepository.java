package com.reborn.back.aiPost.repository;

import com.reborn.back.domain.board.BoardBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiPostBookmarkRepository extends JpaRepository<BoardBookmark, Long> {

}