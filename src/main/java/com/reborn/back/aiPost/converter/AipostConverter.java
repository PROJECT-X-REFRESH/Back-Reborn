package com.reborn.back.aiPost.converter;

import com.reborn.back.aiPost.dto.AiPostDetailDto;
import com.reborn.back.domain.aiPost.AiPost;

public class AipostConverter {
    public static AiPostDetailDto domainToDto(AiPost post, boolean bookmarked) {
        return AiPostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .url(post.getUrl())
                .attachImg(post.getAttachImg())
                .bookmarked(bookmarked)
                .build();
    }
}
