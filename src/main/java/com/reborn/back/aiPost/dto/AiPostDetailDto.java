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

}
