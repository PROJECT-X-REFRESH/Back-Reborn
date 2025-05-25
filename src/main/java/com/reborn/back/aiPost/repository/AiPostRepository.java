package com.reborn.back.aiPost.repository;

import com.reborn.back.domain.aiPost.AiPost;
import com.reborn.back.domain.board.Board;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiPostRepository extends JpaRepository<AiPost, Integer> {
}