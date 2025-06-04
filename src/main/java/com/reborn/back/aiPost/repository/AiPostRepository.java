package com.reborn.back.aiPost.repository;

import com.reborn.back.domain.aiPost.AiPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiPostRepository extends JpaRepository<AiPost, Integer> {
}