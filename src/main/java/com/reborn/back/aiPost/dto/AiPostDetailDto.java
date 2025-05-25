package com.reborn.back.aiPost.dto;

import com.reborn.back.domain.aiPost.AiPost;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AiPostDetailDto {
    private Integer id;
    private String title;
    private String content;
    private String url;
    private String attachImg;
    private boolean bookmarked;

    public static AiPostDetailDto from(AiPost post, boolean bookmarked) {
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
