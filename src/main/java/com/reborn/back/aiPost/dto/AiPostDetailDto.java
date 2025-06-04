package com.reborn.back.aiPost.dto;

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
