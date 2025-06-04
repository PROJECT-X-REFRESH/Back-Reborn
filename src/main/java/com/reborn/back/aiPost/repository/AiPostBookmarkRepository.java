package com.reborn.back.aiPost.repository;

import com.reborn.back.domain.aiPost.AiPost;
import com.reborn.back.domain.aiPost.AiPostBookmark;
import com.reborn.back.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiPostBookmarkRepository extends JpaRepository<AiPostBookmark, Integer> {
    boolean existsByAiPostAndUser(AiPost aiPost, User user);

    List<AiPostBookmark> findByUser(User user, Pageable pageable);

    Optional<AiPostBookmark> findByAiPostAndUser(AiPost aiPost, User user);
}