package com.reborn.back.aiPost.service;

import com.reborn.back.aiPost.repository.AiPostRepository;
import com.reborn.back.domain.aiPost.AiPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiPostService {
    private final AiPostRepository aiPostRepository;
    public List<AiPost> getRecentAiPosts(){
        return aiPostRepository.findTopPost();
    }
}
