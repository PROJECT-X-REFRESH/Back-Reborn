package com.reborn.back.review.recollection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemindDto {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
