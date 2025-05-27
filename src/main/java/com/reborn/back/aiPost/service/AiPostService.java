package com.reborn.back.aiPost.service;

import com.reborn.back.aiPost.dto.AiPostDetailDto;
import com.reborn.back.aiPost.repository.AiPostBookmarkRepository;
import com.reborn.back.aiPost.repository.AiPostRepository;
import com.reborn.back.domain.aiPost.AiPost;
import com.reborn.back.domain.aiPost.AiPostBookmark;
import com.reborn.back.domain.user.User;
import com.reborn.back.login.dto.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiPostService {
    private final AiPostRepository aiPostRepository;
    private final AiPostBookmarkRepository bookmarkRepository;

    public List<UserResponseDto.AiPostSimpleDto> getRecentAiPosts() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<AiPost> posts = aiPostRepository.findAll(pageable).getContent();
        return posts.stream()
                .map(post -> UserResponseDto.AiPostSimpleDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .imgUrl(post.getAttachImg())
                        .build())
                .toList();
    }

    public List<AiPost> getPostList(int scrollPosition, int fetchSize) {
        Pageable pageable = PageRequest.of(scrollPosition, fetchSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return aiPostRepository.findAll(pageable).getContent();
    }

    public AiPostDetailDto getPostDetail(Integer postId, User user) {
        AiPost aiPost = aiPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("AiPost not found"));
        boolean bookmarked = bookmarkRepository.existsByAiPostAndUser(aiPost, user);
        return AiPostDetailDto.from(aiPost, bookmarked);
    }

    public boolean toggleBookmark(Integer postId, User user) {
        AiPost aiPost = aiPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("AiPost not found"));

        Optional<AiPostBookmark> bookmarkOpt = bookmarkRepository.findByAiPostAndUser(aiPost, user);
        if (bookmarkOpt.isPresent()) {
            bookmarkRepository.delete(bookmarkOpt.get());
            return false;
        } else {
            AiPostBookmark bookmark = AiPostBookmark.builder()
                    .aiPost(aiPost)
                    .user(user)
                    .build();
            bookmarkRepository.save(bookmark);
            return true;
        }
    }

    public List<AiPost> getMyBookmarkedPosts(User user, int scrollPosition, int fetchSize) {
        Pageable pageable = PageRequest.of(scrollPosition, fetchSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return bookmarkRepository.findByUser(user, pageable).stream()
                .map(AiPostBookmark::getAiPost)
                .collect(Collectors.toList());
    }
}
